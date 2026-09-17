package com.jetbrains.godot.tscn.findUsages

import com.intellij.lang.findUsages.LanguageFindUsages
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import tscn.psi.TscnNodeHeader

@RunWith(JUnit4::class)
class NodePathFindUsagesTest : GdTestCaseWithSdk("completion") {

    /**
     * A scene node carries no PolySymbol declaration of its own, so Find Usages started from its
     * `.tscn` header goes through the classic `ReferencesSearch`, bridged by
     * `gdscript.search.GdOwnReferencesSearcher` to the `$Plain` own reference in the `.gd` script -
     * see `gdscript.psi.impl.GdNodePathImpl.getOwnReferences`.
     */
    @Test
    fun testNodeDeclarationFindsGdUsage() = doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "unique_names.gd",
        fileContents = "extends Node2D\n\nfunc _ready():\n\tvar node = \$Plain\n",
    ) {
        val tscnFile = PsiManager.getInstance(project).findFile(myFixture.findFileInTempDir("unique_names.tscn"))!!
        val header = PsiTreeUtil.findChildrenOfType(tscnFile, TscnNodeHeader::class.java).first { it.name == "Plain" }

        val references = ReferencesSearch.search(header, GlobalSearchScope.allScope(project)).findAll()
        assertEquals(references.joinToString("\n") { it.element.text }, 1, references.size)
        assertEquals("\$Plain", references.single().element.text)

        assertFalse("a scene node must offer no classic PSI Find Usages", LanguageFindUsages.canFindUsagesFor(header))
    }
}
