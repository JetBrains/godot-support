package com.jetbrains.godot.tscn.findUsages

import com.intellij.lang.findUsages.LanguageFindUsages
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.findUsages.searchTargetsAt
import com.jetbrains.godot.findUsages.symbolUsages
import com.jetbrains.godot.findUsages.text
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.psi.GdMethodDeclTl
import kotlin.io.path.pathString

class ConnectionMethodFindUsagesTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/findUsages/connectionMethod").pathString
    }

    /**
     * A connection reports the handler once, through the PolySymbol own reference on the `method`
     * value - see `tscn.psi.impl.TscnNamedElementImpl.getOwnReferences`.
     *
     * The test guards the count against a second reference on the same value.
     */
    fun testConnectionListedOnce() {
        myFixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        val method = PsiTreeUtil.findChildOfType(myFixture.file, GdMethodDeclTl::class.java)?.methodIdNmi
        assertNotNull("the script must declare the handler", method)

        val sceneUsages = myFixture.symbolUsages(method!!.textOffset).filter { it.file.name == "scene_a.tscn" }
        assertEquals(sceneUsages.joinToString("\n") { it.text }, 1, sceneUsages.size)
        assertEquals("_on_goto_scene_pressed", sceneUsages.single().text)
    }

    /**
     * Find Usages offers one target on the declaration, so it starts the search at once instead of
     * asking which target to search - see `gdscript.codeInsight.GdUsageProvider.canFindUsagesFor`.
     */
    fun testSingleTarget() {
        myFixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        val method = PsiTreeUtil.findChildOfType(myFixture.file, GdMethodDeclTl::class.java)?.methodIdNmi
        assertNotNull("the script must declare the handler", method)

        val symbolTargets = myFixture.searchTargetsAt(method!!.textOffset)
        assertEquals(symbolTargets.joinToString("\n") { it.presentation().presentableText }, 1, symbolTargets.size)
        assertFalse("the declaration must offer no classic PSI target", LanguageFindUsages.canFindUsagesFor(method))
    }
}
