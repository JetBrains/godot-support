package com.jetbrains.godot.tscn.findUsages

import com.intellij.lang.findUsages.LanguageFindUsages
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import tscn.psi.TscnNodeHeader

@TestApplication
class NodePathFindUsagesTest {
    private val gd: GdSdkFixture by gdSdkFixture("completion")

    /**
     * A scene node carries no PolySymbol declaration of its own, so Find Usages started from its
     * `.tscn` header goes through the classic `ReferencesSearch`, bridged by
     * `gdscript.search.GdOwnReferencesSearcher` to the `$Plain` own reference in the `.gd` script -
     * see `gdscript.psi.impl.GdNodePathImpl.getOwnReferences`.
     */
    @Test
    fun testNodeDeclarationFindsGdUsage() = gd.doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "unique_names.gd",
        fileContents = "extends Node2D\n\nfunc _ready():\n\tvar node = \$Plain\n",
    ) {
        val tscnFile = PsiManager.getInstance(gd.project).findFile(gd.fixture.findFileInTempDir("unique_names.tscn"))!!
        val header = PsiTreeUtil.findChildrenOfType(tscnFile, TscnNodeHeader::class.java).first { it.name == "Plain" }

        val references = ReferencesSearch.search(header, GlobalSearchScope.allScope(gd.project)).findAll()
        assertEquals(1, references.size, references.joinToString("\n") { it.element.text })
        assertEquals("\$Plain", references.single().element.text)

        assertFalse(LanguageFindUsages.canFindUsagesFor(header), "a scene node must offer no classic PSI Find Usages")
    }
}
