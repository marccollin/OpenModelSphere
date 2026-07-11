plugins {
    java
}

val projectName = "org.modelsphere.plugins.ansi_forward"
val targetPluginsDir = rootProject.projectDir.resolve("org.modelsphere.sms/plugins")

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Dépendances core minimales
    implementation(project(":org.modelsphere.jack"))
    implementation(project(":org.modelsphere.sms"))

    // ✅ Velocity pour la génération de templates SQL (hérité de jack)
    //implementation(files("../org.modelsphere.jack/lib/velocity-1.6.1/velocity-1.6.1.jar"))
    //implementation(files("../org.modelsphere.jack/lib/velocity-1.6.1/lib/commons-collections-3.2.1.jar"))
    //implementation(files("../org.modelsphere.jack/lib/velocity-1.6.1/lib/commons-lang-2.4.jar"))

    // ⚠️ Autres dépendances potentielles (à ajuster selon le .classpath réel)
    // implementation(files("lib/some-lib.jar"))
}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
        resources {
            srcDirs("src")
            include("**/*.xml", "**/*.properties", "**/*.html", "**/*.txt", "**/*.dic", "**/*.gif", "**/*.png", "**/*.jpg", "**/*.tpl")
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

    assemble {
        dependsOn(dist)
    }
}

tasks.named("clean") {
    doLast {
        delete(file("bin"), file("build"))
    }
}