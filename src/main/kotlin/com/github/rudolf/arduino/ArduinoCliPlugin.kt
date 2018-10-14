package com.github.rudolf.arduino

import com.github.rudolf.arduino.extension.Arduino
import com.github.rudolf.arduino.task.Install
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.IvyArtifactRepository

class ArduinoCliPlugin : Plugin<Project> {


    override fun apply(project: Project) {
        println("Plugin init")
        project.extensions.add("Arduino", Arduino::class.java)

        val x = object : Action<IvyArtifactRepository> {
            override fun execute(t: IvyArtifactRepository) {
                t.artifactPattern("https://downloads.arduino.cc/arduino-cli/[organisation]-[revision]-[module].[ext]")
            }
        }
        project.repositories.ivy(x)

        project.tasks.create("compile-arduino", Install::class.java)
        println("Plugin init finished")
    }
}

