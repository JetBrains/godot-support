package com.jetbrains.godot.gdscript.sdk.xml

import com.intellij.openapi.application.PathManager
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.polySymbols.sdk.xml.XmlToGd
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.nio.file.Path
import kotlin.io.path.readText

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/SDK"])
class XmlToGdTest {

    private val xmlDir: Path
        get() {
            val home = PathManager.getHomeDirFor(XmlToGdTest::class.java)
            val base = if (home != null) {
                home.resolve("dotnet/Plugins/godot-support/gdscript/src/test")
            } else {
                PathManager.getPluginsDir().parent.parent.parent.parent.parent.resolve("src/test")
            }
            return base.resolve("testData/gdscript/xml")
        }

    @ParameterizedTest
    @ValueSource(strings = ["ExampleClass", "ExampleGlobals"])
    fun testConvertXmlToGdf(className: String) {
        val xmlFile = xmlDir.resolve("doc_classes/$className.xml")
        val expected = xmlDir.resolve("$className.generated.gd").readText()
        val actual = XmlToGd().convert(xmlFile)
        Assertions.assertEquals(expected, actual)
    }
}
