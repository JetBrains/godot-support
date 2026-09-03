package com.jetbrains.godot.tscn.findUsages

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.findUsages.symbolUsages
import com.jetbrains.godot.findUsages.text
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.psi.GdMethodDeclTl
import kotlin.io.path.pathString

/**
 * The scene copies `networking/multiplayer_bomber/bomb.tscn` of the Godot demo projects.
 */
class AnimationTrackMethodFindUsagesTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/findUsages/animationTrackMethod").pathString
    }

    /**
     * An animation method track reports the method once, through the PolySymbol own reference on the
     * `&"name"` key - see `tscn.psi.impl.TscnNamedElementImpl.getOwnReferences`.
     *
     * Track 2 calls the same name on `NodePath("Sprite")`, and that node carries no script, so it
     * adds no usage.
     */
    fun testTrackListedOnce() {
        myFixture.configureByFiles("bomb.gd", "bomb.tscn")
        val method = PsiTreeUtil.findChildrenOfType(myFixture.file, GdMethodDeclTl::class.java)
            .firstOrNull { it.getName() == "explode" }
            ?.methodIdNmi
        assertNotNull("the script must declare the method", method)

        val sceneUsages = myFixture.symbolUsages(method!!.textOffset).filter { it.file.name == "bomb.tscn" }
        assertEquals(sceneUsages.joinToString("\n") { it.text }, 1, sceneUsages.size)
        assertEquals("explode", sceneUsages.single().text)
    }
}
