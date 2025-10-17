Project-specific development guidelines for gdscript module (JetBrains GDScript plugin)

This document captures practical, project-specific knowledge to help advanced contributors build, test, and extend the GDScript plugin efficiently.

1. Build and configuration

Prerequisites
- Gradle: use the included Gradle wrapper (./gradlew)

IntelliJ Platform and SDK preparation
- build.gradle.kts uses the IntelliJ Platform Gradle plugin to resolve IDEA Community and JetBrains Runtime. No manual setup is required for those.

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

2.1. Lexer and Parser

Gd.flex is used to generate the GdLexer.java. There is an action "Run JFlex Generator" to re-generate the GdLexer.java.

### GDScript indentation rules (for Gd.flex)

* Maintain:

    * `paren_depth` counter for `()[]{}`
    * `indent_stack` for indentation levels
    * `indent_active` flag
    * `reactivation_stack` for indentation reactivation inside parens

#### Core

* At `NEWLINE`:

    * If `indent_active == true && paren_depth == 0`, compare indentation with top of `indent_stack`:

        * emit `INDENT` or `DEDENT`s accordingly
    * Otherwise, ignore indentation (treat leading whitespace as insignificant)

#### Paren control

* On `(`, `[`, `{` → `paren_depth++`
* On `)`, `]`, `}` → `paren_depth--`
* If `paren_depth > 0` → `indent_active = false` (unless reactivated)

#### Colon behavior

* After `:`:

    * If next token is `NEWLINE` → start **block suite**

        * push state (`indent_active = true`)
        * on next line, emit `INDENT` if deeper
    * Else → **inline suite** (no `INDENT`/`DEDENT`)

#### Reactivation inside parens

* If inside parens and encounter `:` followed by `NEWLINE` after block-forming keyword (`func`, `if`, `elif`, `else`, `for`, `while`, `match`):

    * push `indent_active = true` (reactivate)
    * emit dedents normally until returning to parent level, then restore previous `indent_active` (usually false)

#### Dedents

* Emit dedents only on indentation decrease at `NEWLINE`
* Never emit dedents on `)` `]` `}` directly

3. Testing: running, configuring, and adding tests

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

4. Additional development information

Contact points
- Start from README.md for plugin overview and links to features, changelog, and marketplace. For Grammar-Kit or PSI concerns, consult IntelliJ Platform SDK docs.
