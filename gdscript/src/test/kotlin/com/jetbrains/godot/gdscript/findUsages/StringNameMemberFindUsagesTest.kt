package com.jetbrains.godot.gdscript.findUsages

import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.findUsages.symbolUsages
import com.jetbrains.godot.findUsages.text
import com.jetbrains.godot.gdscript.GdPolySymbolsTestUtils
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdSignalDeclTl
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService
import kotlin.io.path.pathString

/**
 * Find Usages / Rename of a signal or method that is only named inside a string or StringName
 * argument of `connect(...)` / `Callable(...)`. Own-references on the literal bridge the site -
 * see `gdscript.psi.utils.GdStringNameMemberReferenceUtil`.
 */
class StringNameMemberFindUsagesTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/findUsages/stringNameMember").pathString
    }

    override fun setUp() {
        super.setUp()
        GdLspSettingsFlowService.getInstance(project).setLspConnectionMode(GdLspConnectionMode.Never)
        myFixture.copyFileToProject("../../project.godot", "project.godot")
        // Register SDK from the real test-data paths. Temp VFS copies have no nio path.
        val sdkRoot = getBaseTestDataPath().resolve("testData/gdscript/sdk")
        GdPolySymbolsTestUtils.registerSdk(
            project,
            listOf(sdkRoot.resolve("gdextensions"), sdkRoot.resolve("4.5.0")),
        )
    }

    fun testConnectSignalFindUsages() {
        myFixture.configureByFiles("EventBus.gd", "HUD.gd")
        val signal = PsiTreeUtil.findChildrenOfType(myFixture.file, GdSignalDeclTl::class.java)
            .firstOrNull { it.getName() == "battle_state_changed" }
            ?.signalIdNmi
        assertNotNull("EventBus.gd must declare the signal", signal)

        val usages = myFixture.symbolUsages(signal!!.textOffset)
        assertEquals(usages.joinToString("\n") { "${it.file.name}:${it.text}" }, 1, usages.size)
        assertEquals("battle_state_changed", usages.single().text)
        assertEquals("HUD.gd", usages.single().file.name)
    }

    fun testCallableSelfMethodFindUsages() {
        myFixture.configureByFiles("EventBus.gd", "HUD.gd")
        myFixture.configureByFile("HUD.gd")
        val method = PsiTreeUtil.findChildrenOfType(myFixture.file, GdMethodDeclTl::class.java)
            .firstOrNull { it.getName() == "_on_battle_state_changed" }
            ?.methodIdNmi
        assertNotNull("HUD.gd must declare the handler", method)

        val usages = myFixture.symbolUsages(method!!.textOffset)
        assertEquals(usages.joinToString("\n") { it.text }, 1, usages.size)
        assertEquals("_on_battle_state_changed", usages.single().text)
    }

    fun testCallableClassStringNameMethodFindUsages() {
        myFixture.configureByFiles("Node25D.gd", "YSort25D.gd")
        myFixture.configureByFile("Node25D.gd")
        val method = PsiTreeUtil.findChildrenOfType(myFixture.file, GdMethodDeclTl::class.java)
            .firstOrNull { it.getName() == "y_sort_slight_xz" }
            ?.methodIdNmi
        assertNotNull("Node25D.gd must declare the static method", method)

        val usages = myFixture.symbolUsages(method!!.textOffset)
        assertEquals(usages.joinToString("\n") { "${it.file.name}:${it.text}" }, 1, usages.size)
        assertEquals("y_sort_slight_xz", usages.single().text)
        assertEquals("YSort25D.gd", usages.single().file.name)
    }

    fun testRenameSignalUpdatesConnectString() {
        myFixture.configureByFiles("EventBus.gd", "HUD.gd")
        val offset = myFixture.file.text.indexOf("battle_state_changed")
        assertTrue("EventBus.gd must declare the signal", offset >= 0)
        myFixture.editor.caretModel.moveToOffset(offset)
        myFixture.renameSymbolAtCaret("battle_phase_changed")

        myFixture.checkResultByFile("EventBus.gd", "EventBus.gd.after", false)
        myFixture.checkResultByFile("HUD.gd", "HUD.gd.afterSignalRename", false)
    }

    fun testRenameMethodUpdatesCallableString() {
        myFixture.configureByFiles("EventBus.gd", "HUD.gd")
        myFixture.configureByFile("HUD.gd")
        val methodOffset = myFixture.file.text.indexOf("func _on_battle_state_changed")
        assertTrue(methodOffset >= 0)
        myFixture.editor.caretModel.moveToOffset(methodOffset + "func ".length)
        myFixture.renameSymbolAtCaret("_on_battle_phase_changed")

        myFixture.checkResultByFile("HUD.gd", "HUD.gd.afterMethodRename", false)
    }
}
