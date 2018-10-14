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
            id = "arduino-plugin"
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

//pluginBundle {
//    website = "https://github.com/zhurlik"
//    vcsUrl = "https://github.com/zhurlik/gradle-arduino-plugin"
//    description = "A gradle plugin for using Arduino IDE in the gradle projects"
//    tags = listOf("gradle", "gradle-plugin", "arduino", "arduino-ide")
//
//    plugins {
//        register("arduinoPlugin") {
//            id = "com.github.zhurlik.arduino"
//            displayName = "Gradle Arduino IDE plugin"
//        }
//    }
//}

//// for local testing
//uploadArchives {
//    repositories {
//        mavenDeployer {
//            repository(url = uri("tmp-repo"))
//        }
//    }
//}
