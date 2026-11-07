plugins {
    java
}

val projectName = "org.modelsphere.plugins.helloworld_v3.1"
val targetPluginsDir = rootProject.projectDir.resolve("org.modelsphere.sms/plugins")

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    // Dépendances core minimales
    implementation(project(":org.modelsphere.jack"))
}

sourceSets {
    main {
        java {
            srcDir("src")
        }
    }
}

tasks {
    val dist by registering(Jar::class) {
        archiveBaseName.set(projectName)
        archiveVersion.set("")
        from(sourceSets.main.get().output)
        entryCompression = ZipEntryCompression.STORED
        destinationDirectory.set(layout.buildDirectory.dir("libs"))

        doLast {
            if (!targetPluginsDir.exists()) {
                targetPluginsDir.mkdirs()
            }
            val destFile = targetPluginsDir.resolve(archiveFileName.get())
            archiveFile.get().asFile.copyTo(destFile, overwrite = true)
            logger.lifecycle("Plugin '$projectName' copié vers: $destFile")
        }
    }

    assemble {
        dependsOn(dist)
    }
}

tasks.named("clean") {
    doLast {
        delete(file("bin"), file("build"))
    }
}