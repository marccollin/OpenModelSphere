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
        options.release = 21
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
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks.named("clean") {
        doLast {
            delete(file("bin"), file("build"))
        }
    }
}