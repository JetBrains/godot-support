Project-specific development guidelines for gdscript module (JetBrains GDScript plugin)

This document captures practical, project-specific knowledge to help advanced contributors build, test, and extend the GDScript plugin efficiently.

1) Build and configuration

Prerequisites
- JDK: Java 21 (enforced via kotlin.jvmToolchain in build.gradle.kts)
- Gradle: use the included Gradle wrapper (./gradlew)
- OS packages: none required beyond standard JDK + network access (tests/downloads fetch artifacts)

IntelliJ Platform and SDK preparation
- build.gradle.kts uses the IntelliJ Platform Gradle plugin to resolve IDEA Community and JetBrains Runtime. No manual setup is required for those.
- The custom prepare task downloads a temporary GDScript SDK archive used during sandbox preparation. It is wired automatically via prepareSandbox.dependsOn("prepare"). You typically don’t need to invoke it manually.

Common tasks
- Build (verifies sources compile and prepares the plugin):
  - ./gradlew build
- Run the plugin in a sandboxed IDE (IntelliJ IDEA Community):
  - ./gradlew runIde
  - Notes: runIde is configured with -Xmx1500m in gradle; adjust via Gradle property if needed.
- Clean:
  - ./gradlew clean

Notes about code generation and sources
- Generated PSI/Parser sources are placed under src/main/gen and are registered as a source root (see sourceSets in build.gradle.kts).
- Gd.bnf is outdated and no longer in use

2) Testing: running, configuring, and adding tests

Test frameworks and configuration
- Tests are run on JUnit Platform with the Vintage engine (JUnit 4 code style is used in the project). Dependencies:
  - testImplementation("junit:junit:4.13.2")
  - testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.10.0")
- IntelliJ test framework is bundled via the IntelliJ Platform Gradle plugin:
  - intellijPlatform { testFramework(TestFrameworkType.Bundled) }
  - The plugin also bundles lib/testFramework.jar.

Run all tests
- ./gradlew test

Run a specific test class or method is slower, prefer running all tests at once
- Single class:
  - ./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest"
- Single method:
  - ./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest.testLambdaInConnectIndent"

IDE test execution
- You can run tests from IntelliJ IDEA using Gradle run configurations. Ensure Gradle JDK is set to 21, and “Delegate IDE build/run to Gradle” is enabled for consistent behavior.

Adding new tests
- Location: src/test/kotlin

3) Additional development information

Contact points
- Start from README.md for plugin overview and links to features, changelog, and marketplace. For Grammar-Kit or PSI concerns, consult IntelliJ Platform SDK docs.
