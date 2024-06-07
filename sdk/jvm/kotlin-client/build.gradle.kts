plugins {
    id("org.openapi.generator") version "6.2.1"
}

version = "0.17-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion = "2.1.3"
    val kotlinVersion = "1.7.10"
    val jacksonVersion = "2.13.4"

    implementation(kotlin("reflect", kotlinVersion))
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.compileKotlin {
    dependsOn("openApiGenerate")
}

sourceSets.main {
    java.srcDir("build/generate/src/main/kotlin")
}

java {
    withSourcesJar()
}

tasks.openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$rootDir/../../openapi-schema.yaml")
    outputDir.set("$buildDir/generate")
    generateApiTests.set(false)
    generateApiDocumentation.set(false)
    generateModelDocumentation.set(false)
    apiPackage.set("org.yawa.klient.resource")
    modelPackage.set("org.yawa.klient.model")
    packageName.set("org.yawa.klient")
    configOptions.set(
        mapOf(
            "enumPropertyNaming" to "UPPERCASE",
            "omitGradleWrapper" to "true",
            "library" to "jvm-ktor",
            "serializationLibrary" to "jackson",
        )
    )
    typeMappings.set(
        mapOf(
            "float" to "java.math.BigDecimal",
        )
    )
}
