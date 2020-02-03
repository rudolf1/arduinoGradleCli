package com.github.rudolf.arduino

import com.github.rudolf.arduino.extension.Arduino
import com.github.rudolf.arduino.task.ArduinoCompile
import com.github.rudolf.arduino.task.InstallCli
import org.gradle.api.Plugin
import org.gradle.api.Project

class ArduinoCliPlugin : Plugin<Project> {


    override fun apply(project: Project) {
        project.extensions.add("Arduino", Arduino::class.java)

        project.tasks.create("install_cli", InstallCli::class.java)
        project.tasks.create("arduino_compile", ArduinoCompile::class.java) {
            dependsOn("install_cli")
        }
    }
}

