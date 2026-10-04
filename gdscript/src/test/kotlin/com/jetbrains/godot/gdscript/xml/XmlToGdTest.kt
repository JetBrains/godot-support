package com.jetbrains.godot.gdscript.xml

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.polySymbols.sdk.xml.XmlToGd
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.io.path.writeText

@RunWith(JUnit4::class)
class XmlToGdTest : BasePlatformTestCase() {

    private val converter = XmlToGd()

    companion object {
        const val TEST_DATA_PATH = "testData/gdscript/sdk/"
        const val SDK_VERSION = "4.5.0"
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve(TEST_DATA_PATH).pathString
    }

    fun getXmlTestDataPath(): String {
        return getBaseTestDataPath().resolve(TEST_DATA_PATH).resolve(SDK_VERSION).pathString
    }

    @Test
    fun testNodeConversion() {
        doTest("Node.xml", "Node.gd")
    }

    @Test
    fun testNode2DConversion() {
        doTest("Node2D.xml", "Node2D.gd")
    }

    @Test
    fun testVector2Conversion() {
        doTest("Vector2.xml", "Vector2.gd")
    }

    @Test
    fun testCodeBlockIndentationPreserved() {
        val classData = classData(
            "Description.\n" +
                "\t[codeblocks]\n" +
                "\t[gdscript]\n" +
                "\tfunc _get(property):\n" +
                "\t\tif property == \"fake_property\":\n" +
                "\t\t\tprint(\"Getting my property!\")\n" +
                "\t\treturn null\n" +
                "\t[/gdscript]\n" +
                "\t[/codeblocks]"
        )

        val actual = converter.convert(classData)

        assertEquals(
            "class_name Example\n\n" +
                "## Description.\n" +
                "## [codeblocks]\n" +
                "## [gdscript]\n" +
                "## func _get(property):\n" +
                "## \tif property == \"fake_property\":\n" +
                "## \t\tprint(\"Getting my property!\")\n" +
                "## \treturn null\n" +
                "## [/gdscript]\n" +
                "## [/codeblocks]\n\n",
            actual,
        )
    }

    @Test
    fun testCsharpCodeBlocksAreOmitted() {
        val classData = classData(
            "Description.\n" +
                "[codeblocks]\n" +
                "[gdscript]\n" +
                "print(\"GDScript\")\n" +
                "[/gdscript]\n" +
                "[csharp]\n" +
                "GD.Print(\"C#\")\n" +
                "[/csharp]\n" +
                "[/codeblocks]"
        )

        val actual = converter.convert(classData)

        assertEquals(
            "class_name Example\n\n" +
                "## Description.\n" +
                "## [codeblocks]\n" +
                "## [gdscript]\n" +
                "## print(\"GDScript\")\n" +
                "## [/gdscript]\n" +
                "## [/codeblocks]\n\n",
            actual,
        )
    }

    @Test
    fun testAnnotationsAreWrittenAsCommentAnchors() {
        val float = GdSdkData.TypeData("float")
        val classData = classData("Description.").copy(
            annotations = listOf(
                GdSdkData.AnnotationData("export", "Marks a property as exported.", isVariadic = false, parameters = emptyList()),
                GdSdkData.AnnotationData(
                    "export_range",
                    "Exports a range. See [annotation @export].",
                    isVariadic = true,
                    parameters = listOf(
                        GdSdkData.ParameterData("min", float),
                        GdSdkData.ParameterData("step", float, "1.0"),
                        GdSdkData.ParameterData("extra_hints", GdSdkData.TypeData("String"), "\"\""),
                    ),
                ),
            )
        )

        val actual = converter.convert(classData)

        assertEquals(
            "class_name Example\n\n" +
                "## Description.\n\n" +
                "\n#region Annotations\n\n" +
                "## Marks a property as exported.\n" +
                "# @export\n\n" +
                "## Exports a range. See [annotation @export].\n" +
                "# @export_range(min: float, step: float = 1.0, ...extra_hints: String)\n\n" +
                "#endregion Annotations\n\n",
            actual,
        )
        val file = myFixture.configureByText("Example.gd", actual)
        assertNull(PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java))
    }

    private fun classData(description: String): GdSdkData.ClassData = GdSdkData.ClassData(
        name = "Example",
        inherits = null,
        briefDescription = null,
        description = description,
        constructors = emptyList(),
        methods = emptyList(),
        properties = emptyList(),
        signals = emptyList(),
        constants = emptyList(),
        enums = emptyList(),
        themeItems = emptyList(),
        tutorials = emptyList(),
        isDeprecated = false,
        isExperimental = false,
    )

    private fun doTest(xmlFileName: String, gdFileName: String) {
        // Get the XML file from the test data directory
        val xmlFile = Path.of(getXmlTestDataPath(),xmlFileName)
        assertTrue("XML file does not exist: $xmlFile", xmlFile.exists())

        // Convert the XML to GD
        val actualGd = converter.convert(xmlFile)

        // Get the expected GD content from the gold file
        val goldFile = Path.of(testDataPath, gdFileName)
        if (!goldFile.exists()) {
            goldFile.writeText(actualGd)
            fail("Gold file did not exist and was created: $goldFile")
        }
        val expectedGd = goldFile.readText()
        assertEquals("Generated GD content does not match the gold file", actualGd, expectedGd)
    }
}