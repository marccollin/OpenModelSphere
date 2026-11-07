plugins {
    java
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Dépendances core minimales
    api(project(":org.modelsphere.jack"))
}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
        resources {
            srcDirs("src")
            include("**/*.xml", "**/*.xsl", "**/*.properties", "**/*.html", "**/*.css", "**/*.js", "**/*.txt", "**/*.dic", "**/*.gif", "**/*.png", "**/*.jpg")
            exclude("**/*.java", "**/*.class")
        }
    }
}

// Configuration du JAR (pour usage interne, pas pour plugins/)
tasks.jar {
    archiveBaseName.set("org.modelsphere.guide")
    archiveVersion.set("")
    from(sourceSets.main.get().output)
}

// Clean étendu pour compatibilité avec Eclipse/Ant
tasks.named("clean") {
    doLast {
        delete(file("bin"), file("build"))
    }
}