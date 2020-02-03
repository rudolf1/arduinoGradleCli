package com.github.rudolf.arduino.task

fun generateConfig(additionalBoards: List<String>) =
"""
board_manager:
    additional_urls:
        ${additionalBoards.map { "- $it" }.joinToString("\n")}
proxy_type: auto
sketchbook_path: .
arduino_data: .

""".trimIndent()

