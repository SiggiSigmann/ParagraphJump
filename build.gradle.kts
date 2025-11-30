plugins {
    id("scala")
    id("org.jetbrains.intellij.platform") version "2.10.5"
}

group = "com.github.siggisigmann"
version = "1.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure IntelliJ Platform Gradle Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        create("IC", "2025.2")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

      // Add necessary plugin dependencies for compilation here, example:
      //bundledPlugin("com.intellij.java")
    }
    
    implementation("org.scala-lang:scala-library:2.13.12")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
        }

        changeNotes = """
            Initial version
        """.trimIndent()
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    // Bundle scala-library into the plugin JAR
    withType<org.jetbrains.intellij.platform.gradle.tasks.BuildPluginTask> {
        from(configurations.runtimeClasspath) {
            include("scala-library-*.jar")
            into("lib")
        }
    }
}