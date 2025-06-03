import java.security.MessageDigest

plugins {
    id("java")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

val phpDir = rootProject.projectDir.resolve("php")
val resultingDir = layout.buildDirectory.dir("artifacts")
val buildNumber = System.getenv("BUILD_NUMBER") ?: "SNAPSHOT"
val baseVersion = property("baseVersion")?.toString() ?: error("Base version is not specified")
val fullVersion = "$baseVersion.$buildNumber"

val phpSdkOutput = phpDir.resolve("gdscriptsdk.tar.xz")
val resultingSdkBaseName = "gdscriptsdk"
val sdkResultingFullName = "$resultingSdkBaseName-$fullVersion.tar.xz"

val runPhpScript = tasks.register<Exec>("runPhpScript") {
    workingDir = phpDir
    commandLine("php", phpDir.resolve("sdkBuilder.php").absolutePath)
    outputs.file(phpSdkOutput)
}

val prepareTar = tasks.register<Copy>("prepareTarForPublish") {
    dependsOn(runPhpScript)
    from(phpSdkOutput)
    into(resultingDir)
    rename { sdkResultingFullName }

    // Define the output file for this task
    outputs.file(resultingDir.map { it.asFile.resolve(sdkResultingFullName) })
}

val generateSha512 = tasks.register("generateSha512") {
    val tarArtifact = resultingDir.map { it.asFile.resolve(sdkResultingFullName) }
    val sha512File = resultingDir.map { it.asFile.resolve("$sdkResultingFullName.sha512") }

    dependsOn("prepareTarForPublish")
    outputs.file(sha512File)

    doLast {
        val sha512 = MessageDigest.getInstance("SHA-512")
            .digest(tarArtifact.get().readBytes())
            .joinToString("") { "%02x".format(it) }

        sha512File.get().writeText(sha512)
    }
}

tasks.register("prepareArtifacts") {
    dependsOn(prepareTar)
    dependsOn(generateSha512)
}

tasks.named("publish") {
    dependsOn(generateSha512)
}

publishing {
    publications {
        create<MavenPublication>("tar") {
            artifact(prepareTar.flatMap { it.outputs.files.elements }.map { it.first() }) {
                extension = "tar.xz"
                classifier = null
            }
            // Add SHA-512 file as an artifact
            artifact(generateSha512.map { it.outputs.files.singleFile }) {
                extension = "tar.xz.sha512"
                classifier = null
            }
            groupId = "rider-gdscript.sdkBuilder"
            artifactId = resultingSdkBaseName
            version = fullVersion
        }
    }
    repositories {
        maven {
            val publicationRepositoryUrl = property("publicationRepositoryUrl")?.toString()
            if (publicationRepositoryUrl.isNullOrBlank()) return@maven
            url = uri(publicationRepositoryUrl)
            credentials {
                username = property("publicationUsername").toString()
                password = property("publicationPassword").toString()
            }
        }
    }
}
