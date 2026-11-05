plugins {
    java
    `java-library` // Expose les APIs aux autres modules
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    //api(rootProject.libs.jazzy.core)
    implementation(files("lib/jazzy-core.jar"))

    implementation("org.apache.bcel:bcel:6.8.1")


    api(rootProject.libs.jgrapht.core)
    api(rootProject.libs.velocity.engine)
    api(rootProject.libs.commons.lang3)
    api(rootProject.libs.jython.standalone)
    api(rootProject.libs.antlr)
    api(rootProject.libs.stringtemplate)
    api(rootProject.libs.bcel)
    api(rootProject.libs.log4j.api)
    api(rootProject.libs.log4j.core)

    api(rootProject.libs.jdom)


}

// Si ce module a un build.xml Ant existant, on peut aussi utiliser son dossier bin/
sourceSets {
    main {
        java {
            srcDirs("src")
            exclude("test/**")
        }
        resources {
            srcDirs("src")
            include("**/*.xml", "**/*.properties", "**/*.html", "**/*.txt", "**/*.dic", "**/*.gif", "**/*.png", "**/*.jpg")
            exclude("**/*.java", "**/*.class")
        }
    }
}

// Si ce module produit un JAR spécifique, configure-le ici
tasks.jar {
    archiveBaseName.set("org.modelsphere.jack")
    archiveVersion.set("")
}

val testPluginStartupTiming by tasks.registering(JavaExec::class) {
    group = "verification"
    description = "Checks opt-in plugin startup timing without a test framework"
    dependsOn(tasks.testClasses)
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("org.modelsphere.jack.plugins.PluginStartupTimingTest")
}

tasks.check {
    dependsOn(testPluginStartupTiming)
}

val testPluginJarDiscovery by tasks.registering(JavaExec::class) {
    group = "verification"
    description = "Checks plugin JAR discovery metadata and file handle cleanup"
    dependsOn(tasks.testClasses)
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("org.modelsphere.jack.plugins.io.PluginJarDiscoveryTest")
}

tasks.check {
    dependsOn(testPluginJarDiscovery)
}

val testPluginXmlDiscovery by tasks.registering(JavaExec::class) {
    group = "verification"
    description = "Checks XML plugin identity and UTF-8/legacy resource decoding"
    dependsOn(tasks.testClasses)
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("org.modelsphere.jack.plugins.PluginXmlDiscoveryTest")
}

tasks.check {
    dependsOn(testPluginXmlDiscovery)
}
