package com.jetbrains.godot.findUsages

import com.intellij.find.usages.api.PsiUsage
import com.intellij.find.usages.api.SearchTarget
import com.intellij.find.usages.api.UsageOptions
import com.intellij.find.usages.impl.AllSearchOptions
import com.intellij.find.usages.impl.buildQuery
import com.intellij.find.usages.impl.searchTargets
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

/**
 * The search targets that Find Usages offers at [offset] of the file in the editor. Find Usages
 * asks the user to choose when it has more than one target, so a test that counts them guards
 * against a duplicate.
 */
internal fun CodeInsightTestFixture.searchTargetsAt(offset: Int): List<SearchTarget> =
    searchTargets(file, offset)

/**
 * The usages of the single search target at [offset], without the declaration itself. A GDScript
 * declaration is searched through its PolySymbol, so the classic
 * `CodeInsightTestFixture.findUsages(PsiElement)` finds no handler for it.
 */
internal fun CodeInsightTestFixture.symbolUsages(offset: Int): List<PsiUsage> {
    val target = searchTargetsAt(offset).single()
    val scope = target.maximalSearchScope ?: GlobalSearchScope.allScope(project)
    return buildQuery(project, target, AllSearchOptions(UsageOptions.createOptions(scope), true))
        .findAll()
        .filterIsInstance<PsiUsage>()
        .filter { !it.declaration }
}

/** The text of the usage, as it reads in the file. */
internal val PsiUsage.text: String
    get() = file.text.substring(range.startOffset, range.endOffset)
