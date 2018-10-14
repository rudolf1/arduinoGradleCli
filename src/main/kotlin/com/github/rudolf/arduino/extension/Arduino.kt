package com.github.rudolf.arduino.extension

open class Arduino {

    data class Sketch(val path: String, val board: String)

    data class Dependency(val name: String, val version: String?)

    public var cliVersion = "0.3.1-alpha.preview"

    internal val additionalBoards = mutableListOf<String>()
    internal val sketches = mutableListOf<Sketch>()
    internal val cores = mutableSetOf<Dependency>()
    internal val libs = mutableSetOf<Dependency>()

    fun additionalBoards(url: String) {
        additionalBoards.add(url)
    }

    fun sketch(path: String, board: String) {
        sketches.add(Sketch(path, board))
    }

    fun core(core: String, version: String? = null) {
        cores.add(Dependency(core, version))
    }

    fun library(lib: String, version: String? = null) {
        libs.add(Dependency(lib, version))
    }

}
