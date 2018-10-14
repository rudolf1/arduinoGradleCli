group = "com.github.rudolf.arduino"
version = "0.2"


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
    compile("org.apache.ant", "ant-compress", "1.5")
    compile("commons-io", "commons-io", "2.6")
}

publishing {
    repositories {
        maven(url = "build/repository")
    }
}