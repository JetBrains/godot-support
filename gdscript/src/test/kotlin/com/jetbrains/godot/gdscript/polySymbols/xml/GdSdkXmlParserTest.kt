package com.jetbrains.godot.gdscript.polySymbols.xml

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Path
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdSdkXmlParserTest : BasePlatformTestCase() {

    companion object {
        const val TEST_DATA_PATH = "testData/gdscript/polySymbols/sdk/"
        const val SDK_VERSION = "4.5.0"
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve(TEST_DATA_PATH).resolve(SDK_VERSION).pathString
    }

    @Test
    fun testParseNodeXml() {
        val className = "Node"
        val path = Path.of(testDataPath, "$className.xml")

        // class
        val clazz = GdSdkXmlParser.parseClass(path)
        assertNotNull("GdSdkXmlParser failed to parse the class $className", clazz)

        assertEquals(className, clazz!!.name)
        assertEquals("Object", clazz.inherits)
        assertTrue(clazz.description?.contains("Nodes are Godot's building blocks") == true)

        // methods
        val methods = clazz.methods
        assertTrue(methods.any { it.name == "_ready" })
        val getConfigurationWarningsMethod = methods.find { it.name == "_get_configuration_warnings" }
        assertNotNull(getConfigurationWarningsMethod)
        assertEquals("PackedStringArray", getConfigurationWarningsMethod!!.returnType.name)
        assertNull(getConfigurationWarningsMethod.returnType.enumName)
        assertFalse(getConfigurationWarningsMethod.returnType.isBitField)
        assertTrue(getConfigurationWarningsMethod.qualifiers.isVirtual)
        assertTrue(getConfigurationWarningsMethod.qualifiers.isConst)
        assertFalse(getConfigurationWarningsMethod.qualifiers.isVariadic)
        assertFalse(getConfigurationWarningsMethod.qualifiers.isRequired)
        assertFalse(getConfigurationWarningsMethod.qualifiers.isStatic)
        assertTrue(getConfigurationWarningsMethod.description?.contains("The elements in the array returned") == true)

        val addChildMethod = methods.find { it.name == "add_child" }
        assertNotNull(addChildMethod)
        assertEquals("void", addChildMethod!!.returnType.name)
        assertSize(3, addChildMethod.parameters)
        assertEquals("node", addChildMethod.parameters[0].name)
        assertEquals("Node", addChildMethod.parameters[0].type.name)
        assertNull(addChildMethod.parameters[0].type.enumName)
        assertFalse(addChildMethod.parameters[0].type.isBitField)

        val getChildrenMethod = methods.find { it.name == "get_children" }
        assertNotNull(getChildrenMethod)
        assertEquals("Array[Node]", getChildrenMethod!!.returnType.name)
        assertTrue(getChildrenMethod.qualifiers.isConst)

        // properties
        val properties = clazz.properties
        assertTrue(properties.any { it.name == "name" })
        assertTrue(properties.any { it.name == "owner" })
        val nameProperty = properties.find { it.name == "name" }
        assertEquals("StringName", nameProperty!!.type.name)
        assertEquals("set_name", nameProperty.setter)
        assertEquals("get_name", nameProperty.getter)

        // signals
        val signals = clazz.signals
        assertTrue(signals.any { it.name == "ready" })
        assertTrue(signals.any { it.name == "tree_entered" })

        // tutorials
        val tutorials = clazz.tutorials
        assertNotNull(tutorials)
        assertSize(2, tutorials!!)
        assertTrue(tutorials.any { it.name == "Nodes and scenes" })
        val tutorial = tutorials.find { it.name == "All Demos" }
        assertNotNull(tutorial)
        assertEquals("https://github.com/godotengine/godot-demo-projects/", tutorial!!.url)
    }

    @Test
    fun testParseGDScriptXml() {
        val className = "@GDScript"
        val path = Path.of(testDataPath, "$className.xml")

        // class
        val clazz = GdSdkXmlParser.parseClass(path)
        assertNotNull("GdSdkXmlParser failed to parse the class $className", clazz)

        assertEquals(className, clazz!!.name)
        assertEmpty(clazz.inherits)

        // constructors
        assertEmpty(clazz.constructors)

        // methods
        val methods = clazz.methods
        assertNotEmpty(methods)
        assertTrue(methods.any { it.name == "range" })
        assertTrue(methods.any { it.name == "len" })

        val convertMethod = methods.find { it.name == "convert" }
        assertNotNull(convertMethod)
        assertEquals("Variant", convertMethod!!.returnType.name)
        assertSize(2, convertMethod.parameters)
        assertEquals("what", convertMethod.parameters[0].name)
        assertEquals("Variant", convertMethod.parameters[0].type.name)
        assertEmpty(convertMethod.parameters[0].type.enumName)
        assertEquals("type", convertMethod.parameters[1].name)
        assertEquals("int", convertMethod.parameters[1].type.name)
        assertEquals("Variant.Type", convertMethod.parameters[1].type.enumName)

        // properties
        assertEmpty(clazz.properties)

        // constants
        val constants = clazz.constants
        assertNotEmpty(constants)
        assertTrue(constants.any { it.name == "PI" })
        assertTrue(constants.any { it.name == "TAU" })
        assertTrue(constants.any { it.name == "INF" })
        assertTrue(constants.any { it.name == "NAN" })

        val infConstant = constants.find { it.name == "INF" }
        assertNotNull(infConstant)
        assertEquals("inf", infConstant!!.value)

        // enums
        assertEmpty(clazz.enums)

        // operations
        val operations = GdSdkXmlParser.parseOperations(path)
        assertNull(operations)

        // annotations
        val annotations = GdSdkXmlParser.parseAnnotations(path)
        assertNotNull(annotations)
        assertNotEmpty(annotations)
        assertTrue(annotations!!.any { it.name == "tool" })

        val onreadyAnnotation = annotations.find { it.name == "onready" }
        assertNotNull(onreadyAnnotation)
        assertEmpty(onreadyAnnotation!!.parameters)
        assertFalse(onreadyAnnotation.isVariadic)

        val exportCustomAnnotation = annotations.find { it.name == "export_custom" }
        assertNotNull(exportCustomAnnotation)
        assertSize(3, exportCustomAnnotation!!.parameters)
        assertEquals("hint", exportCustomAnnotation.parameters[0].name)
        assertEquals("int", exportCustomAnnotation.parameters[0].type.name)
        assertEquals("PropertyHint", exportCustomAnnotation.parameters[0].type.enumName)
        assertFalse(exportCustomAnnotation.parameters[0].type.isBitField)
        assertEmpty(exportCustomAnnotation.parameters[0].default)
        assertEquals("usage", exportCustomAnnotation.parameters[2].name)
        assertEquals("int", exportCustomAnnotation.parameters[2].type.name)
        assertEquals("PropertyUsageFlags", exportCustomAnnotation.parameters[2].type.enumName)
        assertTrue(exportCustomAnnotation.parameters[2].type.isBitField)
        assertEquals("6", exportCustomAnnotation.parameters[2].default)

        val warningIgnoreAnnotation = annotations.find { it.name == "warning_ignore" }
        assertNotNull(warningIgnoreAnnotation)
        assertTrue(warningIgnoreAnnotation!!.isVariadic)
    }

    @Test
    fun testParseVector2Xml() {
        val className = "Vector2"
        val path = Path.of(testDataPath, "$className.xml")

        // class
        val clazz = GdSdkXmlParser.parseClass(path)
        assertNotNull("GdSdkXmlParser failed to parse the class $className", clazz)

        assertEquals(className, clazz!!.name)
        assertEmpty(clazz.inherits)

        // constructors
        val constructors = clazz.constructors
        val emptyConstructor = constructors.find { it.parameters.isEmpty() }
        assertNotNull(emptyConstructor)

        val xyConstructor = constructors.find { it.parameters.size == 2 }
        assertNotNull(xyConstructor)
        assertEquals("x", xyConstructor!!.parameters[0].name)
        assertEquals("float", xyConstructor.parameters[0].type.name)
        assertEquals("y", xyConstructor.parameters[1].name)
        assertEquals("float", xyConstructor.parameters[1].type.name)

        // constants
        val constants = clazz.constants
        val constAxisX = constants.find { it.name == "AXIS_X" }
        assertNotNull(constAxisX)
        assertEquals("0", constAxisX!!.value)
        assertTrue(constants.any { it.name == "AXIS_Y" })

        // enums
        val enums = clazz.enums
        val enumAxis = enums.find { it.name == "Axis" }
        assertNotNull(enumAxis)
        assertEquals("AXIS_X", enumAxis!!.values[0].name)
        assertEquals("0", enumAxis.values[0].value)
        assertEquals("AXIS_Y", enumAxis.values[1].name)

        // operations
        val operation = GdSdkXmlParser.parseOperations(path)
        assertNotNull(operation)
        assertEquals(className, operation!!.left)

        val operators = operation.operators
        val operatorNotEquals = operators.find { it.operator == "!=" }
        assertNotNull(operatorNotEquals)
        assertEquals("Vector2", operatorNotEquals!!.right)
        assertEquals("bool", operatorNotEquals.returnType.name)

        val operatorUnaryMinus = operators.find { it.operator == "-" && it.isUnary}
        assertNotNull(operatorUnaryMinus)
        assertEquals("", operatorUnaryMinus!!.right)
        assertEquals("Vector2", operatorUnaryMinus.returnType.name)

        val operatorMinus = operators.find { it.operator == "-" && !it.isUnary}
        assertNotNull(operatorMinus)
        assertEquals("Vector2", operatorMinus!!.right)
        assertEquals("Vector2", operatorMinus.returnType.name)

        // annotations
        val annotations = GdSdkXmlParser.parseAnnotations(path)
        assertNotNull(annotations)
        assertEmpty(annotations!!)
    }

    @Test
    fun testParseLabelXml() {
        val className = "Label"
        val path = Path.of(testDataPath, "$className.xml")

        // class
        val clazz = GdSdkXmlParser.parseClass(path)
        assertNotNull("GdSdkXmlParser failed to parse the class $className", clazz)

        assertEquals(className, clazz!!.name)

        // properties
        val properties = clazz.properties
        assertNotEmpty(properties)
        val mouseFilterProperty = properties.find { it.name == "mouse_filter"}
        assertNotNull(mouseFilterProperty)
        assertEquals("int", mouseFilterProperty!!.type.name)
        assertEquals("Control.MouseFilter", mouseFilterProperty.type.enumName)
        assertEquals("2", mouseFilterProperty.default)
        assertFalse(mouseFilterProperty.type.isBitField)
        assertEquals("Control", mouseFilterProperty.overrides)

        // theme items
        val themeItems = clazz.themeItems
        assertNotEmpty(themeItems)
        val fontItem = themeItems.find { it.name == "font"}
        assertNotNull(fontItem)
        assertEquals("font", fontItem!!.dataType)
        assertEquals("Font", fontItem.type)
        assertNull(fontItem.default)

        val fontColorItem = themeItems.find { it.name == "font_color"}
        assertNotNull(fontColorItem)
        assertEquals("color", fontColorItem!!.dataType)
        assertEquals("Color", fontColorItem.type)
        assertEquals("Color(1, 1, 1, 1)", fontColorItem.default)
    }
}
