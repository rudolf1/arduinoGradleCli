# Overview

[![](https://jitpack.io/v/rudolf1/arduinoGradleCli.svg)](https://jitpack.io/#rudolf1/arduinoGradleCli)

This repository contains [Gradle](https://gradle.org/) plugin to compile Arduino sketches using [Arduino cli](https://github.com/arduino/arduino-cli)

# Appying the Plugin <a name="applying"></a>

Currently, plugin distributed with jitpack. To use it please add:

`build.gradle.kts`:

```
buildscript {
    repositories {
        ...
        maven {
            url = uri("https://jitpack.io")
        }
    }
    dependencies {
        classpath("com.github.rudolf1.arduinoGradleCli:build:0.1")
    }
}

apply {
    plugin("arduino-cli-plugin")
}

```

# Configuration <a name="configuration"></a>

`build.gradle.kts`:

```
extensions.configure<Arduino>("Arduino") {

    cliVersion = "0.3.1-alpha.preview" // cli version
    additionalBoards("http://arduino.esp8266.com/stable/package_esp8266com_index.json") // additional package repository

    core("esp8266:esp8266") // core to install
    library("ArduinoJson", "5.13.3") // library to install

    sketch("node", "esp8266:esp8266:generic") // sketch to compile. Directory with sources and board identifier
}
```
Latest version of arduino-cli you can find on the [main page](https://github.com/arduino/arduino-cli)

In provided example, directory **node** is on the same level as **build.gradle.kts** and contains file **node.ino** 

Also, you can read how to find [libraries](https://github.com/arduino/arduino-cli#step-7-add-libraries) and [cores with board identifiers](https://github.com/arduino/arduino-cli#step-4-find-and-install-the-right-core).

# TODO <a name="todo"></a>

- Task to execute arduino-cli with any bunch of arguments

- Additional compilation parameters

- Distribute via gradle plugin portal

- Move compiled binaries to **build** directory

