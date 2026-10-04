Development guidelines for the gdscript module (JetBrains Godot plugin).

## Environment Check

Read `../AGENTS.md` section `2. Bazel vs Gradle` first.
Before you run a build or test command, check the repository environment:
- Bazel. Follow Section 1 and Section 2.
- Gradle. Follow Section 3.

---

## 1. Monorepo: Build and test

To compile only:

```
./bazel.cmd build //dotnet/Plugins:rider-plugins-godot //dotnet/Plugins:rider-plugins-godot-gdscript
```
- Test commands: run `./tests.cmd`. See the Platform instructions.
- Test location: `src/test/kotlin`.
- Generated PSI and parser sources sit under `src/main/gen`.

## 2. Lexer and Parser (GDScript)

Instructions are in the `Gd.flex` file.
See also `GdHighlight.flex`.

### Regenerating the lexer

After you edit `Gd.flex`, regenerate the Java lexer from the workspace root:

```
"<JBR_HOME>/bin/java" -Xmx512m \
  -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 \
  -jar dotnet/Plugins/godot-support/gdscript/jflex-1.9.2.jar \
  -skel dotnet/Plugins/godot-support/gdscript/idea-flex.skeleton \
  -d dotnet/Plugins/godot-support/gdscript/src/main/gen/gdscript \
  dotnet/Plugins/godot-support/gdscript/src/main/kotlin/gdscript/Gd.flex
```

The generated file is `src/main/gen/gdscript/GdLexer.java`. Commit it together with the changes to `Gd.flex`.

If `jflex-1.9.2.jar` is not present, run the IDE action "Generate JFlex Lexer" from the `Gd.flex` file context menu.

---

## 3. Gradle repository: Gradle build and test

Use these instructions only when the root of workspace directly contains folders `rider`, `gdscript`

### Build and configuration

- Gradle: use the included Gradle wrapper (`./gradlew`).
- Build: `./gradlew build`
- Run the plugin in a sandboxed IDE: `./gradlew runIde`
- Clean: `./gradlew clean`

### Testing

Tests run on the JUnit Platform.

- All tests: `./gradlew test`
- Single test class: `./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest"`
- Single test method: `./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest.testLambdaInConnectIndent"`

### IDE test execution

Run the tests from IntelliJ IDEA with Gradle run configurations. Set the Gradle JDK to 21 and enable "Delegate IDE build/run to Gradle".

---

## 4. Additional development information

- Start from `README.md` for the plugin overview and the links to features, changelog, and marketplace.
- For Grammar-Kit or PSI concerns, read the IntelliJ Platform SDK docs.
