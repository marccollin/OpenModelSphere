plugins {
    java
}

val projectName = "org.modelsphere.plugins.layout.cluster.rectanglepacker"
val targetPluginsDir = rootProject.projectDir.resolve("org.modelsphere.sms/plugins")

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    // Dépendances core minimales
    implementation(project(":org.modelsphere.jack"))
    implementation(project(":org.modelsphere.sms"))
}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
        resources {
            srcDirs("src")
            include("**/*.xml", "**/*.properties", "**/*.html", "**/*.txt", "**/*.dic", "**/*.gif", "**/*.png", "**/*.jpg")
            exclude("**/*.java", "**/*.class")
        }
    }
}

tasks {
    val dist by registering(Jar::class) {
        archiveBaseName.set(projectName)
        archiveVersion.set("")

        // Inclure classes + ressources
        from(sourceSets.main.get().output)
        entryCompression = ZipEntryCompression.STORED

        destinationDirectory.set(layout.buildDirectory.dir("libs"))

        // Optionnel : générer/mettre à jour plugin.xml avec les tokens
        // doFirst {
        //     val pluginXml = file("plugin.xml")
        //     if (pluginXml.exists()) {
        //         val content = pluginXml.readText()
        //             .replace("@@@CLASSNAME@@@", "org.modelsphere.plugins.layout.cluster.rectanglepacker.RectanglePackerPlugin")
        //             .replace("@@@TYPE@@@", "org.modelsphere.jack.srtool.features.layout.LayoutPlugin")
        //             .replace("@@@REQUIREDBUILD@@@", "950")
        //             .replace("@@@NAME@@@", "Layout Algorithm - Rectangle Packer")
        //             .replace("@@@VERSION@@@", "0.1")
        //             .replace("@@@AUTHOR@@@", "modelsphere.org")
        //         file("$buildDir/resources/main/plugin.xml").writeText(content)
        //     }
        // }

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
        delete(file("bin"), file("build"), file("dist"))
    }
}