package com.jetbrains.godot.gdscript.xml

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import gdscript.polySymbols.sdk.xml.XmlToGd
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