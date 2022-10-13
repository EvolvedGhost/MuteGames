plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.12.3"
}

group = "com.evolvedghost"
version = "0.0.3"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
