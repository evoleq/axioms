
group = "org.evoleq"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}


plugins {
    //kotlin("jvm") version "1.9.24" apply false //version "2.0.21"
    kotlin("multiplatform") version "1.9.24" apply false
    //`maven-publish` apply false
}
/*

dependencies {



    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
*/