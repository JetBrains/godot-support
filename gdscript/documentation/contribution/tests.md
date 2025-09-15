# Tests overview and how to add new ones

This project uses the IntelliJ Platform test framework (JUnit 4 on the JUnit Platform). Most tests extend BasePlatformTestCase or ParsingTestCase and use myFixture to interact with PSI, completion, highlighting, formatting, and resolution.

Run tests:
- All tests: ./gradlew test
- One class: ./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest"
- One method: ./gradlew test --tests "com.jetbrains.godot.gdscript.formatter.GdFormattingTest.testLambdaInConnectIndent"

Test sources live under src/test/kotlin.
Test data files live under testData/gdscript/... where applicable.

Below are the main test types you can add, with where they live today and minimal examples.

1) Parser tests (PSI structure)
- Purpose: Verify the GDScript parser builds the expected PSI tree and recovers from errors.
- Where:
  - Kotlin: src/test/kotlin/com/jetbrains/godot/gdscript/parser
  - Data: testData/gdscript/parser and subfolders (data, recoveryData, returnType, complex cases)
- Typical base classes: ParsingTestCase or project-specific test classes (GdParserTest, GdRecoveryParserTest, GdComplexParserTest).
- Pattern: pair a .gd source with a golden .txt PSI dump that ParsingTestCase compares against.
- Minimal example:
  @Test fun testClassName() = doTest(true)
  // expects ClassName.gd and ClassName.txt next to the test's data root
- Notes:
  - For valid code, ensure no PsiErrorElement in the result before accepting the .txt.
  - For recovery tests, the .txt intentionally includes PsiErrorElement entries.
  - For larger scripts, use a dedicated “complex” test and directory to keep cases organized.
  - See also Return Type tests below that inspect PSI and types built by the parser.

2) Highlighting (inspections/annotator) tests
- Purpose: Verify error/warning highlighting ranges and messages.
- Where:
  - Kotlin: src/test/kotlin/com/jetbrains/godot/gdscript/highlight
  - Data: testData/gdscript/highlighting
- Base class: BasePlatformTestCase
- Pattern: put a .gd file in testData with <error descr="...">text</error> and/or <warning ...> ranges. Then:
  override fun getTestDataPath() = base.resolve("testData/gdscript/highlighting").pathString
  fun testNestedClassErrors() { myFixture.testHighlighting("NestedClassErrors.gd") }

3) Code completion tests
- Purpose: Ensure completion variants are correct in a given context.
- Where: src/test/kotlin/com/jetbrains/godot/gdscript/completion
- Base class: BasePlatformTestCase
- Pattern: configure a file in-memory with a <caret> marker and call myFixture.completeBasic():
  myFixture.configureByText("a.gd", """
    class A1:
        func ppa1(): pass
    func f(): A1.new().<caret>
  """.trimIndent())
  val lookups = myFixture.completeBasic()?.map { it.lookupString }?.toSet().orEmpty()
  assertTrue(lookups.contains("ppa1"))

4) Formatting tests
- Purpose: Ensure CodeStyle formatting keeps or applies expected indentation/spaces.
- Where: src/test/kotlin/com/jetbrains/godot/gdscript/formatter
- Base class: BasePlatformTestCase
- Pattern: configure text, run CodeStyleManager.reformatText inside a write action, and compare strings.
  val psi = myFixture.configureByText("a.gd", before)
  WriteCommandAction.runWriteCommandAction(project) {
      CodeStyleManager.getInstance(project).reformatText(psi, listOf(psi.textRange))
  }
  assertEquals(expected, myFixture.getDocument(psi).text)

5) Resolve tests (reference resolution, navigation)
- Purpose: Verify that references resolve to the expected PSI targets; useful for navigation, rename, etc.
- Where:
  - Kotlin: src/test/kotlin/com/jetbrains/godot/gdscript/resolve
  - Data: testData/gdscript/resolve
- Base: ResolveTestBase (project helper extending BasePlatformTestCase)
  - Provides loadByTestName() / loadFilesByTestName() and golden comparison helpers.
  - Multi-file: create a folder under testData/gdscript/resolve matching the test name and place several .gd files inside; the base will load all of them.
- Approaches:
  - Programmatic: walk all references and assert resolves are non-null or point to specific elements.
  - Golden dump: dump annotated source with inline markers and compare to a .txt file via assertGold.

6) Return type tests (PSI/type inference snippets)
- Purpose: Validate return types and type inference produced from parsed PSI.
- Where:
  - Kotlin: src/test/kotlin/com/jetbrains/godot/gdscript/returntype
  - Data: testData/gdscript/parser/returnType
- Base: BasePlatformTestCase
- Pattern: myFixture.configureByFile("TestName.gd") then query PSI (e.g., GdClassVarDeclTl) and assert returnType strings.

7) Code insight unit tests (utility logic without full fixture setup)
- Purpose: Validate small code insight utilities using a light ParsingTestCase without IDE fixture.
- Where: src/test/kotlin/com/jetbrains/godot/gdscript/codeInsight
- Base: ParsingTestCase with GdParserDefinition
- Pattern: createPsiFile("a.gd", content), find PSI nodes, run the utility, and assert on results.

Conventions and tips
- Use getBaseTestDataPath() to build data paths; many tests override getTestDataPath() = getBaseTestDataPath().resolve(...).pathString
- Name tests so test data files are found by myFixture.getTestName(false) + ".gd" and optional ".txt".
- For completion/highlighting tests, prefer in-memory configureByText when the input is small; for larger scenarios, use files under testData.
- Keep test names descriptive (what behavior is validated) and add brief comments for expectations.
- See README.md and the project guidelines for JDK/Gradle/test configuration details.

Official IntelliJ docs: https://plugins.jetbrains.com/docs/intellij/parsing-test.html
