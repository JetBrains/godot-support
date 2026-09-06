import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven

plugins {
    `maven-publish`
}

val rdVersion = providers.gradleProperty("rdVersion")
val godotCppVersion = providers.gradleProperty("godotCppVersion")
val rdArchive = file(providers.gradleProperty("rdArchive").orElse("artifacts/rd.zip").get())
val godotCppArchive = file(providers.gradleProperty("godotCppArchive").orElse("artifacts/godot-cpp.zip").get())
val repositoryUrl = providers.gradleProperty("repositoryUrl")
    .orElse(providers.environmentVariable("GODOT_RD_MAVEN_REPOSITORY_URL"))
val repositoryUsername = providers.environmentVariable("GODOT_RD_MAVEN_REPOSITORY_USER")
val repositoryToken = providers.environmentVariable("GODOT_RD_MAVEN_REPOSITORY_TOKEN")

publishing {
    publications {
        create<MavenPublication>("rdCpp") {
            groupId = "com.jetbrains.rider.godot"
            artifactId = "rd-cpp"
            version = rdVersion.orElse("unspecified").get()
            artifact(rdArchive) {
                extension = "zip"
            }
            pom {
                packaging = "zip"
            }
        }

        create<MavenPublication>("godotCpp") {
            groupId = "com.jetbrains.rider.godot"
            artifactId = "godot-cpp"
            version = godotCppVersion.orElse("unspecified").get()
            artifact(godotCppArchive) {
                extension = "zip"
            }
            pom {
                packaging = "zip"
            }
        }
    }

    repositories {
        maven {
            name = "godotRdDependencies"
            url = uri(repositoryUrl.orElse("https://repository-url-not-configured.invalid").get())
            credentials {
                username = repositoryUsername.orNull
                password = repositoryToken.orNull
            }
        }
    }
}

tasks.withType<AbstractPublishToMaven>().configureEach {
    if (name.endsWith("ToGodotRdDependenciesRepository")) {
        doFirst {
            check(repositoryUrl.isPresent) {
                "Pass -PrepositoryUrl=<URL> or set GODOT_RD_MAVEN_REPOSITORY_URL."
            }
            check(repositoryUsername.isPresent) { "Set GODOT_RD_MAVEN_REPOSITORY_USER." }
            check(repositoryToken.isPresent) { "Set GODOT_RD_MAVEN_REPOSITORY_TOKEN." }
        }
    }
    when {
        name.startsWith("publishRdCppPublication") -> doFirst {
            check(rdVersion.isPresent) { "Pass the RD version with -PrdVersion=<version>." }
            check(rdArchive.isFile) { "RD archive does not exist: $rdArchive" }
        }
        name.startsWith("publishGodotCppPublication") -> doFirst {
            check(godotCppVersion.isPresent) { "Pass the godot-cpp version with -PgodotCppVersion=<version>." }
            check(godotCppArchive.isFile) { "godot-cpp archive does not exist: $godotCppArchive" }
        }
    }
}
