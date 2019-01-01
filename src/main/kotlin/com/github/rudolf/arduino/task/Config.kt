package com.github.rudolf.arduino.task

import java.io.File

fun generateConfig(workingDir: File, additionalBoards: List<String>) =
"""
board_manager:
    additional_urls:
${additionalBoards.map { "        - $it" }.joinToString("\n")}
proxy_type: auto
sketchbook_path: ${workingDir.absolutePath}
arduino_data: ${workingDir.absolutePath}

""".trimIndent()

