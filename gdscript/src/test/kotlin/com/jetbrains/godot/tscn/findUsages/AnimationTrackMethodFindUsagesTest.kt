package com.jetbrains.godot.tscn.findUsages

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.findUsages.symbolUsages
import com.jetbrains.godot.findUsages.text
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdMethodDeclTl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

/**
 * The scene copies `networking/multiplayer_bomber/bomb.tscn` of the Godot demo projects.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Find usages"])
@TestApplication
class AnimationTrackMethodFindUsagesTest {

    private val gd: GdFixture by gdLightFixture("tscn/findUsages/animationTrackMethod")

    /**
     * An animation method track reports the method once, through the PolySymbol own reference on the
     * `&"name"` key - see `tscn.psi.impl.TscnNamedElementImpl.getOwnReferences`.
     *
     * Track 2 calls the same name on `NodePath("Sprite")`, and that node carries no script, so it
     * adds no usage.
     */
    @Test
    fun testTrackListedOnce() {
        gd.fixture.configureByFiles("bomb.gd", "bomb.tscn")
        val method = PsiTreeUtil.findChildrenOfType(gd.fixture.file, GdMethodDeclTl::class.java)
            .firstOrNull { it.getName() == "explode" }
            ?.methodIdNmi
        assertNotNull(method, "the script must declare the method")

        val sceneUsages = runReadActionBlocking { gd.fixture.symbolUsages(method!!.textOffset).filter { it.file.name == "bomb.tscn" } }
        assertEquals(1, sceneUsages.size, sceneUsages.joinToString("\n") { it.text })
        assertEquals("explode", sceneUsages.single().text)
    }
}
