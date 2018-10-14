package com.github.rudolf.arduino

import com.github.rudolf.arduino.extension.Arduino
import com.github.rudolf.arduino.task.Install
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.IvyArtifactRepository

class ArduinoCliPlugin : Plugin<Project> {

    // TODO Implement generic task to execute any command line

    override fun apply(project: Project) {
        project.extensions.add("Arduino", Arduino::class.java)

        val x = object : Action<IvyArtifactRepository> {
            override fun execute(t: IvyArtifactRepository) {
                t.artifactPattern("https://downloads.arduino.cc/arduino-cli/[organisation]-[revision]-[module].[ext]")
            }
        }
        project.repositories.ivy(x)


        project.tasks.create("compile-arduino", Install::class.java)
    }
}

//        "https://downloads.arduino.cc/arduino-cli/arduino-cli-0.3.1-alpha.preview-linux64.tar.bz2"
//        "https://downloads.arduino.cc/arduino-cli/arduino-cli-0.3.1-alpha.preview-linux32.tar.bz2"
//        "https://downloads.arduino.cc/arduino-cli/arduino-cli-0.3.1-alpha.preview-linuxarm.tar.bz2"
//        "https://downloads.arduino.cc/arduino-cli/arduino-cli-0.3.1-alpha.preview-windows.zip"
//        "https://downloads.arduino.cc/arduino-cli/arduino-cli-0.3.1-alpha.preview-osx.zip"

//        arduino-cli:linux64:0.3.1-alpha.preview@tar.bz2
//        arduino-cli:linux32:0.3.1-alpha.preview@tar.bz2
//        arduino-cli:linuxarm:0.3.1-alpha.preview@tar.bz2
//        arduino-cli:windows:0.3.1-alpha.preview@zip
//        arduino-cli:osx:0.3.1-alpha.preview@zip

//        arduino-cli-0.3.1-alpha.preview-osx.zip"
//        "arduino-cli:osx:0.3.1-alpha@zip"
//        organisation revision
//        arduinoIde("arduino:linux64:1.8.7@tar.xz")
//        https@ //downloads.arduino.cc/arduino-1.8.7-linux64.tar.xz

