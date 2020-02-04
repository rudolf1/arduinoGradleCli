package com.github.rudolf.arduino.task

import com.github.rudolf.arduino.extension.Arduino
import org.apache.commons.io.output.TeeOutputStream
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.Charset

fun File.exec(project: Project, vararg f: String): String {
    val arduinoExt = project.extensions.getByType(Arduino::class.java)

    val bos = ByteArrayOutputStream()
    val errOs = TeeOutputStream(bos, System.err)
    val stdOs = TeeOutputStream(bos, System.out)
    project.exec {
        setStandardOutput(stdOs)
        setErrorOutput(errOs)
        commandLine(this@exec.absolutePath)
        workingDir(this@exec.parentFile.absolutePath)
        val args = arduinoExt.additionalBoards.flatMap { listOf("--additional-urls", it)  }
                .plus("--verbose")
                .plus(f)
        args(args)
    }.assertNormalExitValue()
    return bos.toString(Charset.defaultCharset().name())
}

