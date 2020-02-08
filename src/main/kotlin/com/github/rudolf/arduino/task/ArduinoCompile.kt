package com.github.rudolf.arduino.task

import com.github.rudolf.arduino.extension.Arduino
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.work.InputChanges
import java.io.File

open class ArduinoCompile : SourceTask() {

    @OutputDirectory
    val generatedFileDir = project.file("${project.buildDir}/firmware")

    private val arduinoExt = project.extensions.getByType(Arduino::class.java)

    @Input
    val input = arduinoExt.toString()

    private val parent = project.(arduinoExt.installDirectory)()

    @InputDirectory
    var idePath = File(parent, "arduino-cli-${arduinoExt.cliVersion}")

    @TaskAction
    fun action(inputs: InputChanges) {
        val sources: FileTree = source.asFileTree
        val sketchName = sources.single { it.extension == "ino" }.nameWithoutExtension
        val collectedSources = project.file("${project.buildDir}/${sketchName}")
        collectedSources.delete()
        println("Compiling sketches ${sources}")
        sources.visit {
            this.copyTo(File(collectedSources, this.relativePath.pathString))
        }
        arduinoExt.sketches.forEach { sketch ->

            val deniedChars = ",: /\\".toSet()
            val board = sketch.fqdn.filter { !deniedChars.contains(it) }
            val outputDir = File(project.buildDir, "output_$board")
            val cacheDir = File(project.buildDir, "output_${board}_cache")
            outputDir.mkdirs()

            val executable = idePath.walkTopDown().first(cliCondition)
            executable.exec(this.project,
                    "compile", "--fqbn", sketch.fqdn, collectedSources.absolutePath,
                    "--build-path", outputDir.absolutePath,
                    "--build-cache-path", cacheDir.absolutePath,
                    "--output", File(generatedFileDir, "${sketchName}_${board}").absolutePath
            )
        }
        println("Done")
    }

}
