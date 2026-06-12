Project-specific development guidelines for gdscript module (JetBrains Godot plugin)

This document captures practical, project-specific knowledge to help contributors and AI agents build, test, and extend the GDScript plugin efficiently.

## 0. Two scenarios

This plugin can be opened in **two different ways**, and the right build/test commands differ between them. Detect the scenario before you do anything:

- **Monorepo scenario** — the plugin is checked out as part of `ultimate` (the IntelliJ Ultimate monorepo). The workspace root contains `tests.cmd`, `bazel-build-all.cmd`, and a top-level `CLAUDE.md`, and the plugin lives at `dotnet/Plugins/godot-support/gdscript/`. The real build is **Bazel**; `build.gradle.kts` is present but stale relative to the platform jars in the monorepo.
- **Standalone plugin scenario** — the plugin is checked out as its own project root (no `tests.cmd` next to it; `build.gradle.kts` sits next to `src/`). The build is the **IntelliJ Platform Gradle plugin** against a downloaded IDEA SDK or Rider SDK.

Detection rule for agents: if a `tests.cmd` exists at the workspace root, you are in the monorepo. Otherwise you are standalone.

## 1. Build and configuration

### Monorepo scenario

- Follow the root `CLAUDE.md` / `dotnet/AGENTS.md` for the canonical build & test workflow.
- **Do not run `./gradlew`** from the agent in this scenario. `./gradlew compileKotlin` may fail.
- For compilation use Bazel (`bazel build <target>`) or — preferably — IDE-driven build via the JetBrains MCP / ijproxy run configurations (`get_run_configurations`, `execute_run_configuration`).
- For tests use `./tests.cmd --module <module> --test <FQN>` or an IDE run configuration. See `community/.agents/skills/testing/SKILL.md`.

### Standalone plugin scenario

Prerequisites
- Gradle: use the included Gradle wrapper (`./gradlew`).

IntelliJ Platform and SDK preparation
- `build.gradle.kts` uses the IntelliJ Platform Gradle plugin to resolve IDEA Community and JetBrains Runtime. No manual setup is required for those.

Common tasks
- Build (verifies sources compile and prepares the plugin):
  - `./gradlew build`
- Run the plugin in a sandboxed IDE (IntelliJ IDEA Community):
  - `./gradlew runIde`
  - Notes: `runIde` is configured with `-Xmx1500m` in gradle; adjust via Gradle property if needed.
- Clean:
  - `./gradlew clean`

Notes about code generation and sources
- Generated PSI/Parser sources are placed under `src/main/gen` and are registered as a source root (see `sourceSets` in `build.gradle.kts`).

## 2. Lexer and Parser (GDScript)

Instructions are in the `Gd.flex` file.

### Regenerating the lexer

When working in the IntelliJ Ultimate monorepo, after editing `Gd.flex`, regenerate the Java lexer using:

```
"<JBR_HOME>/bin/java" -Xmx512m \
  -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 \
  -jar dotnet/Plugins/godot-support/gdscript/jflex-1.9.2.jar \
  -skel dotnet/Plugins/godot-support/gdscript/idea-flex.skeleton \
  -d dotnet/Plugins/godot-support/gdscript/src/main/gen/gdscript \
  dotnet/Plugins/godot-support/gdscript/src/main/kotlin/gdscript/Gd.flex
```

The generated file is `src/main/gen/gdscript/GdLexer.java` and must be committed alongside changes to `Gd.flex`.

If the `jflex-1.9.2.jar` is not present, run the IDE action "Generate JFlex Lexer" from the `Gd.flex` file context menu.

## 3. Testing: running, configuring, and adding tests

### Monorepo scenario

- Run tests via `./tests.cmd --module <module> --test <FQN>` (FQN required; simple class names do not match) or via an IDE run configuration. Do not use `./gradlew test`.

### Standalone plugin scenario

Test frameworks and configuration
- Tests are run on JUnit Platform

Run all tests
- `./gradlew test`

Run a specific test class or method is slower, prefer running all tests at once
- Single class:
  - `./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest"`
- Single method:
  - `./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest.testLambdaInConnectIndent"`

IDE test execution
- You can run tests from IntelliJ IDEA using Gradle run configurations. Ensure Gradle JDK is set to 21, and "Delegate IDE build/run to Gradle" is enabled for consistent behavior.

### Adding new tests (both scenarios)

- Location: `src/test/kotlin`

## 4. Additional development information

Contact points
- Start from `README.md` for plugin overview and links to features, changelog, and marketplace. For Grammar-Kit or PSI concerns, consult IntelliJ Platform SDK docs.
