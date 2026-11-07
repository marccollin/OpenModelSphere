plugins {
    java
    `java-library` // Expose les APIs aux autres modules
}

dependencies {
    //api(rootProject.libs.jazzy.core)
    implementation(files("lib/jazzy-core.jar"))

    //bcel comes from the version catalog (api below)


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
