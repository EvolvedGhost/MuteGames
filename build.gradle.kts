plugins {
    val kotlinVersion = "1.8.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.14.0"
}

group = "com.evolvedghost"
version = "0.0.4"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
