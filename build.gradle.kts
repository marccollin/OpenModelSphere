plugins {
    java
    `java-library`
}

allprojects {
    group = "org.modelsphere"
    version = "3.2.0"

    repositories {
        mavenCentral()
        flatDir {
            dirs(rootProject.file("lib"), rootProject.file("OMS_binaries"))
        }
    }

    //CONFIGURATION ENCODING
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
        //options.encoding = "ISO-8859-1"
        options.compilerArgs.add("-Xlint:unchecked")
    }

    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
        //options.encoding = "ISO-8859-1"
    }
}

subprojects {
    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.named("clean") {
        doLast {
            delete(file("bin"), file("build"))
        }
    }
}