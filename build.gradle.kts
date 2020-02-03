group = "com.github.rudolf.arduino"
version = "0.8"

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

gradlePlugin {
    plugins {
        register("com_github_rudolf_arduino") {
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
    implementation("de.undercouch", "gradle-download-task", "4.0.4")
    implementation("org.apache.ant", "ant-compress", "1.5")
    implementation("commons-io", "commons-io", "2.6")
}

publishing {
    repositories {
        maven(url = "build/repository")
    }
}