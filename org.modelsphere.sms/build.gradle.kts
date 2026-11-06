plugins {
    java
    `java-library`
}

dependencies {
    api(project(":org.modelsphere.jack")) // 'api' expose la dépendance aux plugins

    implementation(rootProject.libs.checkboxtree)
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

tasks.jar {
    archiveBaseName.set("org.modelsphere.sms")
    archiveVersion.set("")
    from(sourceSets.main.get().output)
}



// ─────────────────────────────────────────────────────────────
// TÂCHE CLEAN ÉTENDUE
// ─────────────────────────────────────────────────────────────
tasks.named("clean") {
    doLast {
        delete(file("bin"), file("build"))
    }
}



