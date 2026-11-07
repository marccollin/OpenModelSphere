plugins {
    java
    distribution
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    // Dépendances core minimales
    implementation(project(":org.modelsphere.jack"))
    implementation(project(":org.modelsphere.sms"))
    implementation(project(":org.modelsphere.guide"))
}

// ─────────────────────────────────────────────────────────────
// Tâche : Collecter automatiquement TOUTES les dépendances runtime
// ─────────────────────────────────────────────────────────────
val copyRuntimeLibs by tasks.registering(Copy::class) {
    val distLib = layout.buildDirectory.dir("distribution/OpenModelSphere-3.2/lib")

    // Gradle résout toutes les dépendances transitives automatiquement
    from(configurations.runtimeClasspath)
    into(distLib)
}

// ─────────────────────────────────────────────────────────────
// Tâche : Créer modelsphere.jar (classes uniquement)
// ─────────────────────────────────────────────────────────────
val jarApp by tasks.registering(Jar::class) {
    archiveBaseName.set("modelsphere")
    archiveVersion.set("")
    destinationDirectory.set(layout.buildDirectory.dir("distribution/OpenModelSphere-3.2"))

    from(project(":org.modelsphere.jack").sourceSets.main.get().output)
    from(project(":org.modelsphere.sms").sourceSets.main.get().output)
    from(project(":org.modelsphere.guide").sourceSets.main.get().output)

    manifest {
        attributes(
            "Main-Class" to "org.modelsphere.sms.Application",
            "Built-By" to System.getProperty("user.name"),
            "Version" to "3.2.0"
        )
    }

    dependsOn(
        ":org.modelsphere.jack:build",
        ":org.modelsphere.sms:build",
        ":org.modelsphere.guide:build"
    )
}

// ─────────────────────────────────────────────────────────────
// Tâche : Créer resources.zip
// ─────────────────────────────────────────────────────────────
val zipResources by tasks.registering(Zip::class) {
    archiveFileName.set("resources.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distribution/OpenModelSphere-3.2"))

    from(project(":org.modelsphere.jack").sourceSets.main.get().resources)
    from(project(":org.modelsphere.sms").sourceSets.main.get().resources)
    include("**/*.gif", "**/*.jpg", "**/*.png", "**/*.txt", "**/*.properties", "**/*.dic")
}

// ─────────────────────────────────────────────────────────────
// Tâche : Copier les plugins vers la distribution
// ─────────────────────────────────────────────────────────────
val copyPlugins by tasks.registering(Copy::class) {
    description = "Copie les plugins vers le dossier de distribution"
    group = "distribution"

    val pluginsDir = layout.buildDirectory.dir("distribution/OpenModelSphere-3.2/plugins")

    // SOURCE : org.modelsphere.sms/plugins/ (contient repository_functions.jar)
    from("${rootProject.projectDir}/org.modelsphere.sms/plugins/") {
        include("*.jar")
        exclude("*.zip", "*.bak", "*.tmp")
    }

    into(pluginsDir)

    doLast {
        val jarCount = pluginsDir.get().asFile.listFiles { f -> f.name.endsWith(".jar") }?.size ?: 0
        logger.lifecycle("$jarCount plugin(s) copié(s)")

        // Vérification explicite du fichier critique
        val repoJar = file("${pluginsDir.get().asFile}/repository_functions.jar")
        if (repoJar.exists()) {
            logger.lifecycle("repository_functions.jar présent")
        } else {
            logger.lifecycle("repository_functions.jar MANQUANT")
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Tâche principale : assembler la distribution
// ─────────────────────────────────────────────────────────────
val distRelease by tasks.registering(Zip::class) {
    archiveBaseName.set("OpenModelSphere")
    archiveVersion.set("3.2.0")
    archiveExtension.set("zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))

    from(layout.buildDirectory.dir("distribution/OpenModelSphere-3.2"))

    dependsOn(jarApp, zipResources, copyRuntimeLibs, copyPlugins)

    doLast {
        logger.lifecycle("Distribution créée: ${archiveFile.get().asFile.absolutePath}")
    }
}

tasks.assemble { dependsOn(distRelease) }

tasks.named("clean") {
    doLast { delete(layout.buildDirectory) }
}

tasks.register<JavaExec>("run") {
    description = "Lance OpenModelSphere en mode développement"
    group = "application"

    mainClass.set("org.modelsphere.sms.Application")

    // Working directory = dossier de distribution (comme Ant)
    workingDir = layout.buildDirectory.dir("distribution/OpenModelSphere-3.2").get().asFile

    // Classpath de base (sans les plugins : chargés dynamiquement via pluginpath)
    classpath = files(
        project(":org.modelsphere.jack").sourceSets.main.get().output,
        project(":org.modelsphere.sms").sourceSets.main.get().output,
        project(":org.modelsphere.guide").sourceSets.main.get().output,
        project(":org.modelsphere.jack").sourceSets.main.get().resources,
        project(":org.modelsphere.sms").sourceSets.main.get().resources,
        project(":org.modelsphere.guide").sourceSets.main.get().resources,
        configurations.runtimeClasspath
    )

    // JVM arguments
    jvmArgs = listOf(
        "-ms32m",
        "-mx1024m",
        "-ss16m",
        "-Dfile.encoding=UTF-8",
        "-Dsun.jnu.encoding=Cp1252",
        "-Dpluginpath=plugins",  // Chemin relatif : l'app résout ./plugins

        // Flags Java 11+ pour la réflexion (requis pour charger des plugins dynamiquement)
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens=java.base/java.io=ALL-UNNAMED",
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.base/java.util.jar=ALL-UNNAMED",
        "--add-opens=java.desktop/java.awt=ALL-UNNAMED",
        "--add-opens=java.desktop/javax.swing=ALL-UNNAMED",
        "--add-exports=java.desktop/sun.awt.shell=ALL-UNNAMED"
    )

    // Dépendances de build
    dependsOn(
        copyTargets,   // fichiers .typ
        copyPlugins,   // plugins JAR (depuis OMS_binaries)
        copyRuntimeLibs, //dépendances
        ":org.modelsphere.jack:build",
        ":org.modelsphere.sms:build",
        ":org.modelsphere.guide:build"
    )
}

// ─────────────────────────────────────────────────────────────
// Tâche : Copier les fichiers .typ (équivalent de copyTargetFiles dans Ant)
// ─────────────────────────────────────────────────────────────
val copyTargets by tasks.registering(Sync::class) {
    description = "Copie les fichiers .typ vers le dossier de distribution"
    group = "distribution"

    // Source : privilégier OMS_binaries (plus complet) sinon org.modelsphere.sms
    val sourceTargets = file("${rootProject.projectDir}/OMS_binaries/targets")
        .takeIf { it.exists() }
        ?: file("${rootProject.projectDir}/org.modelsphere.sms/targets")

    from(sourceTargets)
    into(layout.buildDirectory.dir("distribution/OpenModelSphere-3.2/targets"))
    include("*.typ")
}

// ─────────────────────────────────────────────────────────────
// Intégrer copyTargets dans le flux de build
// ─────────────────────────────────────────────────────────────
tasks.named("jarApp") {
    dependsOn(copyTargets)
}