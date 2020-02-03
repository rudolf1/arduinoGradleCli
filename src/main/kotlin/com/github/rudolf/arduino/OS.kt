package com.github.rudolf.arduino

enum class OS(val extension:String, vararg val tokens: String) {
    Windows_64bit("zip", "windows"),
    macOS_64bit("tar.gz", "mac"),
    Linux_64bit("tar.gz", "linux", "64"),
    Linux_32bit("tar.gz", "linux", "86"),
    Linux_ARM64("tar.gz", "linux", "arm");

//    windows("zip", "windows"),
//    osx("zip", "mac"),
//    linux64("tar.bz2", "linux", "64"),
//    linux32("tar.bz2", "linux", "86"),
//    linuxarm("tar.bz2", "linux", "arm");

//    arduino-cli_0.7.2_Windows_64bit.zip
//    arduino-cli_0.7.2_macOS_64bit.tar.gz
//    arduino-cli_0.7.2_Linux_64bit.tar.gz
//    arduino-cli_0.7.2_Linux_32bit.tar.gz
//    arduino-cli_0.7.2_Linux_ARM64.tar.gz
//    arduino-cli_0.7.2_Linux_ARMv6.tar.gz
//    arduino-cli_0.7.2_Linux_ARMv7.tar.gz
//    arduino-cli_0.7.2_Windows_32bit.zip



    companion object {
        val OS_ARCH = "${System.getProperty("os.name")}#${System.getProperty("os.arch")}".toLowerCase()

        val current: OS = OS.values().firstOrNull { candidate -> candidate.tokens.all { token -> OS_ARCH.contains(token) } }
                ?: throw java.lang.UnsupportedOperationException("OS not supported ${OS_ARCH}")
    }
}
