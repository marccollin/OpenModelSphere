plugins {
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val projectName = "org.modelsphere.plugins.export.links"
val targetPluginsDir = rootProject.projectDir.resolve("org.modelsphere.sms/plugins")

dependencies {
    implementation(project(":org.modelsphere.jack"))
    implementation(project(":org.modelsphere.sms"))
    implementation("org.apache.velocity:velocity:1.6.1")
}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
        resources {
            srcDirs("src")
            include("**/*.xml", "**/*.properties", "**/*.html", "**/*.txt", "**/*.dic", "**/*.gif", "**/*.png", "**/*.jpg", "**/*.vm")
            exclude("**/*.java", "**/*.class")
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
    assemble { dependsOn(dist) }
}

tasks.named("clean") {
    doLast { delete(file("bin"), file("build")) }
}