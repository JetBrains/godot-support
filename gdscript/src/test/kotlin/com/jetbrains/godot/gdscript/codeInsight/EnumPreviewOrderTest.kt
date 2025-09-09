package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.ParsingTestCase
import gdscript.GdParserDefinition
import gdscript.completion.utils.GdEnumCompletionUtil.preview
import gdscript.psi.GdEnumDeclTl
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EnumPreviewOrderTest : ParsingTestCase("", "gd", GdParserDefinition()) {

    @Test
    fun testEnumOrderPreservedInPreview() {
        val content = """
            enum Tile{
                Ground,
                Wall,
                Empty
            }
        """.trimIndent()

        val psiFile = createPsiFile("a.gd", content)
        val enumDecl = PsiTreeUtil.findChildOfType(psiFile, GdEnumDeclTl::class.java)
        requireNotNull(enumDecl) { "Enum declaration not found in PSI" }

        val previewText = enumDecl.preview()
        val groundIndex = previewText.indexOf("Ground")
        val wallIndex = previewText.indexOf("Wall")
        val emptyIndex = previewText.indexOf("Empty")

        assertTrue("'Ground' should come before 'Wall' in preview: $previewText", groundIndex in 0 until wallIndex)
        assertTrue("'Wall' should come before 'Empty' in preview: $previewText", wallIndex in 0 until emptyIndex)
    }

    override fun getTestDataPath(): String = ""

    override fun skipSpaces(): Boolean = false

    override fun includeRanges(): Boolean = true
}
