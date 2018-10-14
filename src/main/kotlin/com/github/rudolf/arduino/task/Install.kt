package com.github.rudolf.arduino.task

import com.github.rudolf.arduino.OS
import com.github.rudolf.arduino.extension.Arduino
import com.github.rudolf.arduino.extension.Arduino.Dependency
import org.apache.commons.io.output.TeeOutputStream
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.ExecException
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files

open class Install : DefaultTask() {

    private var idePath = File(project.buildDir, "arduino-cli")
    private val arduinoExt = project.extensions.getByType(Arduino::class.java)

    @TaskAction
    fun action() {

        val ext = when (OS.current) {
            OS.linux64 -> "tar.bz2"
            OS.linux32 -> "tar.bz2"
            OS.linuxarm -> "tar.bz2"
            OS.windows -> "zip"
            OS.osx -> "zip"

        }
        println("OS ${OS.OS_ARCH}")
        val dependencyNotation = "arduino-cli:${OS.current}:${arduinoExt.cliVersion}@$ext"

        println("Dependency $dependencyNotation")

        project.configurations.create("arduinoCli")

        project.dependencies.add("arduinoCli", dependencyNotation)

        val depsConfig = project.configurations.getByName("arduinoCli")
        val files = depsConfig.files.firstOrNull { it.name.contains("arduino-cli") }
        if (!idePath.exists() && files != null) {
            extract(files, idePath)
        }
        val workingDir = findDirWithContent(idePath)
        val executable = workingDir.listFiles().first()

        println("Generating config")
        Files.write(File(workingDir, "config.yml").toPath(), generateConfig(arduinoExt.additionalBoards).toByteArray())

        executable.exec("config", "dump")

        installDependencies("core", executable, arduinoExt.cores)
        installDependencies("lib", executable, arduinoExt.libs)

        println("Compiling sketches")
        arduinoExt.sketches.forEach { sketch ->

            println("Compiling $sketch")
            val outputDir = File(project.buildDir, "${sketch.path}_${sketch.board}")
            val cacheDir = File(project.buildDir, "${sketch.path}_${sketch.board}_cache")
            outputDir.mkdirs()

            executable.exec("compile", "--fqbn", sketch.board, File(project.rootDir, sketch.path).absolutePath,
                    "--build-path", outputDir.absolutePath,
                    "--build-cache-path", cacheDir.absolutePath
            )
        }
        println("Done")
    }

    fun installDependencies(type: String, executable: File, expectedDeps: MutableSet<Dependency>) {
        println("Fetching installed $type")

        val installedDeps = try {
            executable.exec(type, "list").split("\n")
        } catch (e: ExecException) {
            executable.exec(type, "update-index")
            executable.exec(type, "list").split("\n")
        }

        val depsToInstall = expectedDeps.filter { search -> !dependencyInList(installedDeps, search) }

        if (depsToInstall.isNotEmpty()) {
            println("Updating $type index")
            executable.exec(type, "update-index")

            depsToInstall.forEach { dependency ->
                println("Installing $dependency")
                executable.exec(type, "install", dependency.name + (if (dependency.version != null) "@${dependency.version}" else ""))
            }
        }
    }

    private fun dependencyInList(installedDependencies: List<String>, dependency: Dependency): Boolean {
        return installedDependencies.any { installed -> installed.startsWith(dependency.name) && (dependency.version == null || installed.contains(dependency.version)) }
    }

    private fun File.exec(vararg f: String): String {
        val bos = ByteArrayOutputStream()
        val errOs = TeeOutputStream(bos, System.err)
        val stdOs = TeeOutputStream(bos, System.out)
        project.exec {
            setStandardOutput(stdOs)
            setErrorOutput(errOs)
            commandLine(this@exec.absolutePath)
            workingDir(this@exec.parentFile.absolutePath)
            args("--config-file", "./config.yml", "--debug")
            args(*f)
        }.assertNormalExitValue()
        return bos.toString(Charset.defaultCharset().name())
    }
}
