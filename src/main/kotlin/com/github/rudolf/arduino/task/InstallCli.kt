package com.github.rudolf.arduino.task

import com.github.rudolf.arduino.OS
import com.github.rudolf.arduino.extension.Arduino
import org.apache.tools.ant.taskdefs.Get
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.ExecException
import org.gradle.work.InputChanges
import java.io.File
import java.net.URL
import java.nio.file.Files

open class InstallCli : DefaultTask() {

    private val arduinoExt = project.extensions.getByType(Arduino::class.java)

    @Input
    val input = arduinoExt.toString()

    private val parent = project.(arduinoExt.installDirectory)()

    @OutputDirectory
    var idePath = File(parent, "arduino-cli-${arduinoExt.cliVersion}")

    @TaskAction
    fun action(inputs: InputChanges) {
        println("Install start")

        val ext = OS.current.extension
        println("OS ${OS.OS_ARCH}")

        val archiveFileName = "arduino-cli_${arduinoExt.cliVersion}_${OS.current}.$ext"
        val urlString = "https://github.com/arduino/arduino-cli/releases/download/${arduinoExt.cliVersion}/$archiveFileName"
        val files = File(parent, archiveFileName)

        val gt = Get()
        gt.project = this.project.ant.antProject
        gt.setSrc(URL(urlString))
        gt.setDest(files)
        gt.setSkipExisting(true)
        gt.execute()

        println("Ide path ${idePath.absolutePath}, files ${files}")
        if (idePath.walkTopDown().none(cliCondition)) {
            println("Unpack ide $files to $idePath")
            extract(files, idePath)
        } else {
            println("Ide already extracted ${files}")
        }
        val executable = idePath.walkTopDown().first(cliCondition)
        val workingDir = executable.parentFile
        println("Working dir ${workingDir}")

        println("Generating config")
        Files.write(File(workingDir, "arduino-cli.yaml").toPath(), generateConfig().toByteArray())

        executable.exec(this.project, "config", "dump")

        installDependencies("core", executable, arduinoExt.cores)
        installDependencies("lib", executable, arduinoExt.libs)
    }

    fun installDependencies(type: String, executable: File, expectedDeps: MutableSet<Arduino.Dependency>) {
        println("Fetching installed $type")

        fun dependencyInList(installedDependencies: List<String>, dependency: Arduino.Dependency): Boolean {
            return installedDependencies.any { installed -> installed.startsWith(dependency.name) && (dependency.version == null || installed.contains(dependency.version)) }
        }

        val installedDeps = try {
            executable.exec(this.project, type, "list").split("\n")
        } catch (e: ExecException) {
            executable.exec(this.project, type, "update-index")
            executable.exec(this.project, type, "list").split("\n")
        }

        val depsToInstall = expectedDeps.filter { search -> !dependencyInList(installedDeps, search) }

        if (depsToInstall.isNotEmpty()) {
            println("Updating $type index")
            executable.exec(this.project, type, "update-index")

            depsToInstall.forEach { dependency ->
                println("Installing $dependency")
                executable.exec(this.project, type, "install", dependency.name + (if (dependency.version != null) "@${dependency.version}" else ""))
            }
        }
    }


}