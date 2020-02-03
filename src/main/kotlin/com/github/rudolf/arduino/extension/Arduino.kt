package com.github.rudolf.arduino.extension

import org.gradle.api.Project
import java.io.File


open class Arduino() {

    data class Sketch(val fqdn: String)

    data class Dependency(val name: String, val version: String?)

    public var cliVersion = "0.7.2"
    public var installDirectory: Project.() -> File = { this.gradle.gradleUserHomeDir }

    internal val additionalBoards = mutableListOf<String>()
    internal val sketches = mutableListOf<Sketch>()
    internal val cores = mutableSetOf<Dependency>()
    internal val libs = mutableSetOf<Dependency>()

    fun additionalBoards(url: String) {
        additionalBoards.add(url)
    }

    fun sketch(fqdn: String) {
        sketches.add(Sketch(fqdn))
    }

    fun core(core: String, version: String? = null) {
        cores.add(Dependency(core, version))
    }

    fun library(lib: String, version: String? = null) {
        libs.add(Dependency(lib, version))
    }

    override fun toString() = listOf(this.cores, this.additionalBoards, this.libs, this.sketches).toString()

}
