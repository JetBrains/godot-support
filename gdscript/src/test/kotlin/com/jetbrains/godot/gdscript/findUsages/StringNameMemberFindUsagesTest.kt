package com.jetbrains.godot.gdscript.findUsages

import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.findUsages.symbolUsages
import com.jetbrains.godot.findUsages.text
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.GdPolySymbolsTestUtils
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdSignalDeclTl
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Find Usages / Rename of a signal or method that is only named inside a string or StringName
 * argument of `connect(...)` / `Callable(...)`. Own-references on the literal bridge the site -
 * see `gdscript.psi.utils.GdStringNameMemberReferenceUtil`.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Find usages"])
@TestApplication
class StringNameMemberFindUsagesTest {

    private val gd: GdFixture by gdLightFixture("gdscript/findUsages/stringNameMember")

    @BeforeEach
    fun registerSdk() {
        GdLspSettingsFlowService.getInstance(gd.project).setLspConnectionMode(GdLspConnectionMode.Never)
        gd.fixture.copyFileToProject("../../project.godot", "project.godot")
        // Register SDK from the real test-data paths. Temp VFS copies have no nio path.
        val sdkRoot = getBaseTestDataPath(StringNameMemberFindUsagesTest::class.java).resolve("testData/gdscript/sdk")
        GdPolySymbolsTestUtils.registerSdk(
            gd.project,
            listOf(sdkRoot.resolve("gdextensions"), sdkRoot.resolve("4.5.0")),
            gd.testRootDisposable,
        )
    }

    @Test
    fun testConnectSignalFindUsages() {
        gd.fixture.configureByFiles("EventBus.gd", "HUD.gd")
        val signal = PsiTreeUtil.findChildrenOfType(gd.fixture.file, GdSignalDeclTl::class.java)
            .firstOrNull { it.getName() == "battle_state_changed" }
            ?.signalIdNmi
        assertNotNull(signal, "EventBus.gd must declare the signal")

        val usages = runReadActionBlocking { gd.fixture.symbolUsages(signal!!.textOffset) }
        assertEquals(1, usages.size, usages.joinToString("\n") { "${it.file.name}:${it.text}" })
        assertEquals("battle_state_changed", usages.single().text)
        assertEquals("HUD.gd", usages.single().file.name)
    }

    @Test
    fun testCallableSelfMethodFindUsages() {
        gd.fixture.configureByFiles("EventBus.gd", "HUD.gd")
        gd.fixture.configureByFile("HUD.gd")
        val method = PsiTreeUtil.findChildrenOfType(gd.fixture.file, GdMethodDeclTl::class.java)
            .firstOrNull { it.getName() == "_on_battle_state_changed" }
            ?.methodIdNmi
        assertNotNull(method, "HUD.gd must declare the handler")

        val usages = runReadActionBlocking { gd.fixture.symbolUsages(method!!.textOffset) }
        assertEquals(1, usages.size, usages.joinToString("\n") { it.text })
        assertEquals("_on_battle_state_changed", usages.single().text)
    }

    @Test
    fun testCallableClassStringNameMethodFindUsages() {
        gd.fixture.configureByFiles("Node25D.gd", "YSort25D.gd")
        gd.fixture.configureByFile("Node25D.gd")
        val method = PsiTreeUtil.findChildrenOfType(gd.fixture.file, GdMethodDeclTl::class.java)
            .firstOrNull { it.getName() == "y_sort_slight_xz" }
            ?.methodIdNmi
        assertNotNull(method, "Node25D.gd must declare the static method")

        val usages = runReadActionBlocking { gd.fixture.symbolUsages(method!!.textOffset) }
        assertEquals(1, usages.size, usages.joinToString("\n") { "${it.file.name}:${it.text}" })
        assertEquals("y_sort_slight_xz", usages.single().text)
        assertEquals("YSort25D.gd", usages.single().file.name)
    }

    @Test
    fun testRenameSignalUpdatesConnectString() = timeoutRunBlocking {
        gd.fixture.configureByFiles("EventBus.gd", "HUD.gd")
        val offset = gd.fixture.file.text.indexOf("battle_state_changed")
        assertTrue(offset >= 0, "EventBus.gd must declare the signal")
        withContext(Dispatchers.EDT) {
            gd.fixture.editor.caretModel.moveToOffset(offset)
            gd.fixture.renameSymbolAtCaret("battle_phase_changed")
        }

        gd.fixture.checkResultByFile("EventBus.gd", "EventBus.gd.after", false)
        gd.fixture.checkResultByFile("HUD.gd", "HUD.gd.afterSignalRename", false)
    }

    @Test
    fun testRenameMethodUpdatesCallableString() = timeoutRunBlocking {
        gd.fixture.configureByFiles("EventBus.gd", "HUD.gd")
        gd.fixture.configureByFile("HUD.gd")
        val methodOffset = gd.fixture.file.text.indexOf("func _on_battle_state_changed")
        assertTrue(methodOffset >= 0)
        withContext(Dispatchers.EDT) {
            gd.fixture.editor.caretModel.moveToOffset(methodOffset + "func ".length)
            gd.fixture.renameSymbolAtCaret("_on_battle_phase_changed")
        }

        gd.fixture.checkResultByFile("HUD.gd", "HUD.gd.afterMethodRename", false)
    }
}
