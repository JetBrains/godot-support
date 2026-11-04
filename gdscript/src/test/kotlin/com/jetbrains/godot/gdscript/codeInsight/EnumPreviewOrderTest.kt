package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.completion.utils.GdEnumCompletionUtil.preview
import gdscript.psi.GdEnumDeclTl
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Documentation"])
@TestApplication
class EnumPreviewOrderTest {
    private val gd: GdFixture by gdLightFixture("gdscript/parser")

    @Test
    fun testEnumOrderPreservedInPreview() {
        val content = """
            enum Tile{
                Ground,
                Wall,
                Empty
            }
        """.trimIndent()

        val psiFile = gd.fixture.configureByText("a.gd", content)
        val previewText = runReadActionBlocking {
            val enumDecl = PsiTreeUtil.findChildOfType(psiFile, GdEnumDeclTl::class.java)
            requireNotNull(enumDecl) { "Enum declaration not found in PSI" }
            enumDecl.preview()
        }
        val groundIndex = previewText.indexOf("Ground")
        val wallIndex = previewText.indexOf("Wall")
        val emptyIndex = previewText.indexOf("Empty")

        assertTrue(groundIndex in 0 until wallIndex, "'Ground' should come before 'Wall' in preview: $previewText")
        assertTrue(wallIndex in 0 until emptyIndex, "'Wall' should come before 'Empty' in preview: $previewText")
    }
}
