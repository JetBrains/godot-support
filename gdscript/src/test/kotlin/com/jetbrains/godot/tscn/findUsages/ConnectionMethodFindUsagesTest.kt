package com.jetbrains.godot.tscn.findUsages

import com.intellij.lang.findUsages.LanguageFindUsages
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.findUsages.searchTargetsAt
import com.jetbrains.godot.findUsages.symbolUsages
import com.jetbrains.godot.findUsages.text
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdMethodDeclTl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Find usages"])
@TestApplication
class ConnectionMethodFindUsagesTest {

    private val gd: GdFixture by gdLightFixture("tscn/findUsages/connectionMethod")

    /**
     * A connection reports the handler once, through the PolySymbol own reference on the `method`
     * value - see `tscn.psi.impl.TscnNamedElementImpl.getOwnReferences`.
     *
     * The test guards the count against a second reference on the same value.
     */
    @Test
    fun testConnectionListedOnce() {
        gd.fixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        val offset = runReadActionBlocking {
            val method = PsiTreeUtil.findChildOfType(gd.fixture.file, GdMethodDeclTl::class.java)?.methodIdNmi
            assertNotNull(method, "the script must declare the handler")
            method!!.textOffset
        }

        val (sceneCount, singleText, usagesText) = runReadActionBlocking {
            val sceneUsages = gd.fixture.symbolUsages(offset).filter { it.file.name == "scene_a.tscn" }
            Triple(sceneUsages.size, sceneUsages.singleOrNull()?.text, sceneUsages.joinToString("\n") { it.text })
        }
        assertEquals(1, sceneCount, usagesText)
        assertEquals("_on_goto_scene_pressed", singleText)
    }

    /**
     * Find Usages offers one target on the declaration, so it starts the search at once instead of
     * asking which target to search - see `gdscript.codeInsight.GdUsageProvider.canFindUsagesFor`.
     */
    @Test
    fun testSingleTarget() {
        gd.fixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        val method = runReadActionBlocking {
            val method = PsiTreeUtil.findChildOfType(gd.fixture.file, GdMethodDeclTl::class.java)?.methodIdNmi
            assertNotNull(method, "the script must declare the handler")
            method!!
        }

        val (targetCount, targetsText) = runReadActionBlocking {
            val symbolTargets = gd.fixture.searchTargetsAt(method.textOffset)
            symbolTargets.size to symbolTargets.joinToString("\n") { it.presentation().presentableText }
        }
        assertEquals(1, targetCount, targetsText)
        assertFalse(
            runReadActionBlocking { LanguageFindUsages.canFindUsagesFor(method) },
            "the declaration must offer no classic PSI target",
        )
    }
}
