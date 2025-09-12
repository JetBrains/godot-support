Project-specific development guidelines for gdscript module (JetBrains GDScript plugin)

This document captures practical, project-specific knowledge to help advanced contributors build, test, and extend the GDScript plugin efficiently.

1) Build and configuration

Prerequisites
- JDK: Java 21 (enforced via kotlin.jvmToolchain in build.gradle.kts)
- Gradle: use the included Gradle wrapper (./gradlew)
- OS packages: none required beyond standard JDK + network access (tests/downloads fetch artifacts)

Repository layout assumptions
- This module (gdscript) is expected to live inside a larger mono-repo alongside community and rider modules. Several build steps depend on artifacts built in ../community.
- build.gradle.kts resolves the repository root as projectDir.parentFile and then references ../community/build/distributions/rider-godot-community.zip as a local plugin dependency.

Building the required sibling plugin (mandatory before testing/building gdscript)
1. From gdscript’s parent directory, build the community plugin archive:
   - cd ../community
   - ./gradlew buildPlugin
   - Result: ../community/build/distributions/rider-godot-community.zip
2. Return to the gdscript module before continuing:
   - cd ../gdscript

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

Precondition for tests
- Ensure the sibling community plugin is built as described above so that build.gradle.kts can resolve the localPlugin ZIP. Without this, test classpath/sandbox preparation will fail early.

Run all tests
- ./gradlew test

Run a specific test class or method
- Single class:
  - ./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest"
- Single method:
  - ./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest.testLambdaInConnectIndent"

IDE test execution
- You can run tests from IntelliJ IDEA using Gradle run configurations. Ensure Gradle JDK is set to 21, and “Delegate IDE build/run to Gradle” is enabled for consistent behavior.

Adding new tests
- Location: src/test/kotlin
- Two common base classes in use:
  - com.intellij.testFramework.fixtures.BasePlatformTestCase for PSI/editor/formatter/inspections, etc.
  - com.intellij.testFramework.ParsingTestCase for grammar/lexer/parser test data driven tests. Use GdParserDefinition() and provide .gd test data files alongside the test if the specific test requires external data.
- Minimal BasePlatformTestCase example:
  - package com.jetbrains.godot.gdscript.example
  - import com.intellij.testFramework.fixtures.BasePlatformTestCase
  - class ExampleTest : BasePlatformTestCase() {
      fun testBasic() {
        val psiFile = myFixture.configureByText("a.gd", "func _ready():\n\tpass\n")
        assertEquals("a.gd", psiFile.name)
      }
    }
- ParsingTestCase example pattern:
  - class GdParserTest : ParsingTestCase("", "gd", GdParserDefinition()) { /* … */ }
  - See existing tests under src/test/kotlin/com/jetbrains/godot/gdscript/parser for convention and helpers.

Creating and running a simple test (verified)
- We validated the setup by creating a minimal BasePlatformTestCase that asserts the test project is initialized. The test passed via the repository’s test harness, after which the file was removed to keep the repo clean. To reproduce locally:
  1) Ensure ../community is built (see above).
  2) Create a test similar to ExampleTest above under src/test/kotlin.
  3) Run it:
     - ./gradlew test --tests "<your.package>.ExampleTest"
  4) Remove the temporary test file if it was only for verification.

3) Additional development information

Kotlin/JVM and language level
- Kotlin targets JVM with Java 21 bytecode. Do not introduce APIs requiring higher Java without updating toolchains.

Plugin sandbox and external SDK
- prepareSandbox depends on a prepare step that downloads a GDScript SDK tarball into build/sdk and copies it into the sandbox. If network or CI constraints exist, cache this artifact or pre-provision it to avoid repeated downloads.

Logging and debugging tests
- You can print to stdout in tests; Gradle testLogging is configured with showStandardStreams=true and TestExceptionFormat.FULL for easier diagnosis.

Local runIde vs. Rider
- The build is currently wired to run IDEA Community (runIde). Rider support is intentionally commented (see todo in build.gradle.kts). If you need Rider-based runs, implement a custom task (e.g., runRider) or re-enable rider() in intellijPlatform with proper installer settings.

Working with generated sources
- src/main/gen is added to main source set. If you overhaul grammar/lexer, keep generated files in sync. In PRs that modify grammar, include regenerated sources to keep CI green unless the project adopts on-demand generation.

Contact points
- Start from README.md for plugin overview and links to features, changelog, and marketplace. For Grammar-Kit or PSI concerns, consult IntelliJ Platform SDK docs.
