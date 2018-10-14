package com.github.rudolf.arduino

enum class OS(vararg val tokens: String) {
    windows("windows"),
    osx("mac"),
    linux64("linux", "64"),
    linux32("linux", "86"),
    linuxarm("linux", "arm");


    companion object {
        val OS_ARCH = "${System.getProperty("os.name")}#${System.getProperty("os.arch")}".toLowerCase()

        val current: OS = OS.values().firstOrNull { candidate -> candidate.tokens.all { token -> OS_ARCH.contains(token) } }
                ?: throw java.lang.UnsupportedOperationException("OS not supported ${OS_ARCH}")
    }
}