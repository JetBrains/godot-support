# Publishing Godot-RD dependencies

This standalone Gradle project publishes the ZIP archives used to build Godot-RD:

| Maven coordinates | Version property | Archive property | Default archive |
|---|---|---|---|
| `com.jetbrains.rider.godot:rd-cpp` | `rdVersion` | `rdArchive` | `artifacts/rd.zip` |
| `com.jetbrains.rider.godot:godot-cpp` | `godotCppVersion` | `godotCppArchive` | `artifacts/godot-cpp.zip` |

Use the RD release as `rdVersion` (for example, `2026.2.5`) and the full godot-cpp commit hash as `godotCppVersion`. A JDK 17 or newer is required.

Run the commands below from this directory. On Windows, use `gradlew.bat` instead of `./gradlew`.

## Publish to a repository

Configure the target repository and credentials through environment variables:

```shell
export GODOT_RD_MAVEN_REPOSITORY_URL=https://packages.jetbrains.team/maven/p/net/godot-rd-dependencies
export GODOT_RD_MAVEN_REPOSITORY_USER=your-username
export GODOT_RD_MAVEN_REPOSITORY_TOKEN=your-token
```

The repository URL can alternatively be passed as `-PrepositoryUrl=<repository-url>`.

Publish either archive with its corresponding task:

```shell
./gradlew publishRdCppPublicationToGodotRdDependenciesRepository \
  -PrdVersion=<version> \
  -PrdArchive=/absolute/path/to/rd.zip
```

```shell
./gradlew publishGodotCppPublicationToGodotRdDependenciesRepository \
  -PgodotCppVersion=<version> \
  -PgodotCppArchive=/absolute/path/to/godot-cpp.zip
```

The Gradle wrapper and publishing configuration are committed so that publishing is reproducible. Generated files in `.gradle/`, `artifacts/`, and `build/` are ignored and must not be committed.
