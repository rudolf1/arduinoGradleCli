group = "com.github.rudolf.arduino"
version = "1.0"


plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("maven-publish")
}

gradlePlugin {
    plugins {
        register("com.github.rudolf.arduino") {
            id = "arduino-cli-plugin"
            implementationClass = "com.github.rudolf.arduino.ArduinoCliPlugin"
        }
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile("org.apache.ant", "ant-compress", "1.5")
    compile("commons-io", "commons-io", "2.6")
}

pluginBundle {
    website = "https://github.com/rudolf1/arduinoGradleCli"
    vcsUrl = "https://github.com/rudolf1/arduinoGradleCli"
    description = "A gradle plugin for compiling sketch with ardiono-cli"
    tags = listOf("gradle", "gradle-plugin", "arduino", "arduino-cli")

    plugins {
        register("arduino-cli-plugin") {
            id = "arduino-cli-plugin"
            displayName = "Gradle Arduino CLIE plugin"
        }
    }
}
