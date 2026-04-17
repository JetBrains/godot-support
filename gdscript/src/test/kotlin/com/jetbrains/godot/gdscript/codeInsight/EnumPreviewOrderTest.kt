package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.GdCodeInsightTestBase
import gdscript.completion.utils.GdEnumCompletionUtil.preview
import gdscript.psi.GdEnumDeclTl
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EnumPreviewOrderTest : GdCodeInsightTestBase() {

    @Test
    fun testEnumOrderPreservedInPreview() {
        val content = """
            enum Tile{
                Ground,
                Wall,
                Empty
            }
        """.trimIndent()

        val psiFile = myFixture.configureByText("a.gd", content)
        val enumDecl = PsiTreeUtil.findChildOfType(psiFile, GdEnumDeclTl::class.java)
        requireNotNull(enumDecl) { "Enum declaration not found in PSI" }

        val previewText = enumDecl.preview()
        val groundIndex = previewText.indexOf("Ground")
        val wallIndex = previewText.indexOf("Wall")
        val emptyIndex = previewText.indexOf("Empty")

        assertTrue(groundIndex in 0 until wallIndex, "'Ground' should come before 'Wall' in preview: $previewText")
        assertTrue(wallIndex in 0 until emptyIndex, "'Wall' should come before 'Empty' in preview: $previewText")
    }
}
