package com.jetbrains.godot.gdscript.embeddedDocs

import gdscript.embeddedDocs.GdDocXmlMerger
import gdscript.embeddedDocs.newHardenedDocumentBuilderFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import java.nio.file.Files
import java.util.concurrent.CancellationException

@RunWith(JUnit4::class)
class GdDocXmlMergerTest {
  @Test
  fun testMergesClassAndNamedMemberProseOntoTargetStructure() {
    val target =
      """
        <class name="Thing" inherits="TargetBase" api_type="extension">
          <brief_description>old brief</brief_description>
          <description>old class</description>
          <tutorials>
            <link title="old">old-url</link>
          </tutorials>
          <methods>
            <method name="documented" keywords="old">
              <return type="TargetReturn" enum="Target.Result" is_bitfield="true"/>
              <param index="0" name="target_arg" type="TargetArg" default="7"/>
              <description>old method</description>
            </method>
            <method name="target_only">
              <return type="int"/>
              <description>keep me</description>
            </method>
          </methods>
          <signals>
            <signal name="changed">
              <param index="0" name="value" type="int"/>
              <description>old signal</description>
            </signal>
          </signals>
          <annotations>
            <annotation name="@tool">
              <description>old annotation</description>
            </annotation>
          </annotations>
          <members>
            <member name="setting" type="int" setter="set_setting" getter="get_setting" enum="Thing.Kind" is_bitfield="true" default="1">old member</member>
          </members>
          <constants>
            <constant name="VALUE" value="1" enum="Thing.Kind" is_bitfield="true">old value</constant>
          </constants>
        </class>
      """.trimIndent()
    val source =
      """
        <class name="Thing" inherits="SourceBase" api_type="core" keywords="class words" is_deprecated="true" deprecated="class deprecated" is_experimental="true" experimental="class experimental">
          <brief_description>new brief</brief_description>
          <description>new class</description>
          <tutorials>
            <link title="new">new-url</link>
          </tutorials>
          <methods>
            <method name="documented" keywords="method words" deprecated="method deprecated" experimental="method experimental">
              <return type="SourceReturn"/>
              <param index="0" name="source_arg" type="SourceArg" default="9"/>
              <description>new method</description>
            </method>
            <method name="source_only">
              <description>ignore me</description>
            </method>
          </methods>
          <signals>
            <signal name="changed" keywords="signal words">
              <description>new signal</description>
            </signal>
          </signals>
          <annotations>
            <annotation name="@tool" keywords="annotation words">
              <description>new annotation</description>
            </annotation>
          </annotations>
          <members>
            <member name="setting" type="String" setter="wrong" getter="wrong" default="2" keywords="member words">new member</member>
          </members>
          <constants>
            <constant name="VALUE" value="2" keywords="value words">new value</constant>
          </constants>
          <enum name="Kind">
            <description>must be ignored</description>
          </enum>
        </class>
      """.trimIndent()

    val expected =
      """
        <?xml version="1.0" encoding="UTF-8" standalone="no"?>
        <class api_type="extension" deprecated="class deprecated" experimental="class experimental" inherits="TargetBase" is_deprecated="true" is_experimental="true" keywords="class words" name="Thing">
          <brief_description>new brief</brief_description>
          <description>new class</description>
          <tutorials>
            <link title="new">new-url</link>
          </tutorials>
          <methods>
            <method deprecated="method deprecated" experimental="method experimental" keywords="method words" name="documented">
              <return enum="Target.Result" is_bitfield="true" type="TargetReturn"/>
              <param default="7" index="0" name="target_arg" type="TargetArg"/>
              <description>new method</description>
            </method>
            <method name="target_only">
              <return type="int"/>
              <description>keep me</description>
            </method>
          </methods>
          <signals>
            <signal keywords="signal words" name="changed">
              <param index="0" name="value" type="int"/>
              <description>new signal</description>
            </signal>
          </signals>
          <annotations>
            <annotation keywords="annotation words" name="@tool">
              <description>new annotation</description>
            </annotation>
          </annotations>
          <members>
            <member default="1" enum="Thing.Kind" getter="get_setting" is_bitfield="true" keywords="member words" name="setting" setter="set_setting" type="int">new member</member>
          </members>
          <constants>
            <constant enum="Thing.Kind" is_bitfield="true" keywords="value words" name="VALUE" value="1">new value</constant>
          </constants>
        </class>
      """.trimIndent()
    assertXmlEquals(expected, GdDocXmlMerger.merge(target, source))
  }

  @Test
  fun testMatchesThemeItemsAndEnumValuesWithoutAddingEnums() {
    val target =
      """
        <class name="Control">
          <brief_description/>
          <description/>
          <tutorials/>
          <constants>
            <constant name="READY" value="1" enum="State">old enum value</constant>
          </constants>
          <theme_items>
            <theme_item name="panel" data_type="color" type="Color" default="red">old color</theme_item>
            <theme_item name="panel" data_type="style" type="StyleBox">old style</theme_item>
          </theme_items>
        </class>
      """.trimIndent()
    val source =
      """
        <class name="Control">
          <brief_description/>
          <description/>
          <tutorials/>
          <constants>
            <constant name="READY" value="9" enum="OtherState">enum value docs</constant>
          </constants>
          <enum name="State">
            <description>enum docs</description>
          </enum>
          <theme_items>
            <theme_item name="panel" data_type="color" type="Wrong" keywords="color words">new color</theme_item>
            <theme_item name="panel" data_type="font" type="Font">wrong type docs</theme_item>
          </theme_items>
        </class>
      """.trimIndent()

    val expected =
      """
        <?xml version="1.0" encoding="UTF-8" standalone="no"?>
        <class name="Control">
          <brief_description/>
          <description/>
          <tutorials/>
          <constants>
            <constant enum="State" name="READY" value="1">enum value docs</constant>
          </constants>
          <theme_items>
            <theme_item data_type="color" default="red" keywords="color words" name="panel" type="Color">new color</theme_item>
            <theme_item data_type="style" name="panel" type="StyleBox">old style</theme_item>
          </theme_items>
        </class>
      """.trimIndent()
    assertXmlEquals(expected, GdDocXmlMerger.merge(target, source))
  }

  @Test
  fun testMatchesConstructorsAndOperatorsByOrderedSignature() {
    val target =
      """
        <class name="Vector">
          <brief_description/>
          <description/>
          <tutorials/>
          <constructors>
            <constructor name="Vector" keywords="target constructor">
              <param name="x" type="int"/>
              <param name="y" type="String"/>
              <description>old int string</description>
            </constructor>
            <constructor name="Vector">
              <param name="x" type="String"/>
              <param name="y" type="int"/>
              <description>old string int</description>
            </constructor>
          </constructors>
          <operators>
            <operator name="operator +" keywords="target operator">
              <return type="Vector"/>
              <param name="right" type="int"/>
              <description>old int operator</description>
            </operator>
            <operator name="operator +">
              <return type="Vector"/>
              <param name="right" type="String"/>
              <description>old string operator</description>
            </operator>
          </operators>
        </class>
      """.trimIndent()
    val source =
      """
        <class name="Vector">
          <brief_description/>
          <description/>
          <tutorials/>
          <constructors>
            <constructor name="Vector" keywords="source constructor" deprecated="constructor deprecated">
              <param name="a" type="String"/>
              <param name="b" type="int"/>
              <description>new string int</description>
            </constructor>
          </constructors>
          <operators>
            <operator name="operator +" keywords="source operator" experimental="operator experimental">
              <param name="other" type="String"/>
              <description>new string operator</description>
            </operator>
          </operators>
        </class>
      """.trimIndent()

    val expected =
      """
        <?xml version="1.0" encoding="UTF-8" standalone="no"?>
        <class name="Vector">
          <brief_description/>
          <description/>
          <tutorials/>
          <constructors>
            <constructor keywords="target constructor" name="Vector">
              <param name="x" type="int"/>
              <param name="y" type="String"/>
              <description>old int string</description>
            </constructor>
            <constructor deprecated="constructor deprecated" name="Vector">
              <param name="x" type="String"/>
              <param name="y" type="int"/>
              <description>new string int</description>
            </constructor>
          </constructors>
          <operators>
            <operator keywords="target operator" name="operator +">
              <return type="Vector"/>
              <param name="right" type="int"/>
              <description>old int operator</description>
            </operator>
            <operator experimental="operator experimental" name="operator +">
              <return type="Vector"/>
              <param name="right" type="String"/>
              <description>new string operator</description>
            </operator>
          </operators>
        </class>
      """.trimIndent()
    assertXmlEquals(expected, GdDocXmlMerger.merge(target, source))
  }

  @Test
  fun testKeepsFirstSourceMemberForDuplicateMatchingKeys() {
    val target =
      """
        <class name="Thing">
          <methods>
            <method name="run">
              <description>old</description>
            </method>
          </methods>
        </class>
      """.trimIndent()
    val source =
      """
        <class name="Thing">
          <methods>
            <method name="run">
              <description>first</description>
            </method>
            <method name="run">
              <description>second</description>
            </method>
          </methods>
        </class>
      """.trimIndent()
    val expected =
      """
        <?xml version="1.0" encoding="UTF-8" standalone="no"?>
        <class name="Thing">
          <methods>
            <method name="run">
              <description>first</description>
            </method>
          </methods>
        </class>
      """.trimIndent()
    assertXmlEquals(expected, GdDocXmlMerger.merge(target, source))
  }

  @Test
  fun testChecksCancellationWhileMergingMembers() {
    val target =
      """
        <class name="Thing">
          <methods>
            <method name="run">
              <description>old</description>
            </method>
          </methods>
        </class>
      """.trimIndent()
    val source =
      """
        <class name="Thing">
          <methods>
            <method name="run">
              <description>new</description>
            </method>
          </methods>
        </class>
      """.trimIndent()
    assertThrows(CancellationException::class.java) {
      GdDocXmlMerger.merge(target, source) { throw CancellationException("stop") }
    }
  }

  @Test
  fun testPermitsInternalDoctypeDeclaration() {
    val target =
      """
        <class name="Thing">
          <description>old</description>
        </class>
      """.trimIndent()
    val source =
      """
        <!DOCTYPE class [<!ELEMENT class ANY>]>
        <class name="Thing">
          <description>new</description>
        </class>
      """.trimIndent()
    val expected =
      """
        <?xml version="1.0" encoding="UTF-8" standalone="no"?>
        <class name="Thing">
          <description>new</description>
        </class>
      """.trimIndent()
    assertXmlEquals(expected, GdDocXmlMerger.merge(target, source))
  }

  @Test
  fun testFlattensInternalEntityReferencesInDescriptions() {
    val target =
      """
        <class name="Foo">
          <description>old</description>
        </class>
      """.trimIndent()
    val source =
      """
        <!DOCTYPE class [<!ENTITY nb "x">]>
        <class name="Foo">
          <description>a&nb;b</description>
        </class>
      """.trimIndent()

    val merged = GdDocXmlMerger.merge(target, source)
    val reparsed = parse(merged)

    assertEquals("ab", reparsed.getElementsByTagName("description").item(0).textContent)
    assertFalse(merged.contains("&nb;"))
  }

  @Test
  fun testKeepsTutorialLinksStructured() {
    val target =
      """
        <class name="Foo">
          <tutorials/>
        </class>
      """.trimIndent()
    val source =
      """
        <class name="Foo">
          <tutorials>
            <link title="Guide">https://example.com/guide</link>
          </tutorials>
        </class>
      """.trimIndent()

    val merged = GdDocXmlMerger.merge(target, source)
    val link = parse(merged).getElementsByTagName("link").item(0) as Element

    assertEquals("Guide", link.getAttribute("title"))
    assertEquals("https://example.com/guide", link.textContent)
  }

  @Test
  fun testSystemEntityDoesNotReadLocalFile() {
    val secret = Files.createTempFile("gd-doc-secret", ".txt")
    try {
      Files.writeString(secret, "secret-content")
      val target =
        """
          <class name="Thing">
            <description>old</description>
          </class>
        """.trimIndent()
      val source =
        """
          <!DOCTYPE class [<!ENTITY leaked SYSTEM "${secret.toUri()}">]>
          <class name="Thing">
            <description>&leaked;</description>
          </class>
        """.trimIndent()
      assertFalse(GdDocXmlMerger.merge(target, source).contains("secret-content"))
    }
    finally {
      Files.deleteIfExists(secret)
    }
  }

  @Test
  fun testReturnsSourceUnchangedWhenTargetIsMissing() {
    val source =
      """
        <?xml version="1.0"?>
        <class name="Fallback">
          <description>authored</description>
        </class>
      """.trimIndent()
    assertEquals(source, GdDocXmlMerger.merge(null, source))
  }

  @Test
  fun testSkipsSourceClassThatTargetOmits() {
    val target =
      """
        <class name="Target">
          <description>target docs</description>
        </class>
      """.trimIndent()
    val source =
      """
        <class name="Source">
          <description>source docs</description>
        </class>
      """.trimIndent()
    assertEquals(target, GdDocXmlMerger.merge(target, source))
  }

  private fun parse(xml: String): Document =
    newHardenedDocumentBuilderFactory().newDocumentBuilder().parse(InputSource(StringReader(xml)))

  private fun assertXmlEquals(expected: String, actual: String) {
    assertEquals(normalizeInterElementWhitespace(expected), normalizeInterElementWhitespace(actual))
  }

  private fun normalizeInterElementWhitespace(xml: String): String = xml.replace(INTER_ELEMENT_WHITESPACE, "><")

  companion object {
    private val INTER_ELEMENT_WHITESPACE = Regex(">\\R[ \\t]*<")
  }
}
