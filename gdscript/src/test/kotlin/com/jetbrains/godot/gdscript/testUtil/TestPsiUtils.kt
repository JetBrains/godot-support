package com.jetbrains.godot.gdscript.testUtil

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement

/**
 * Test utility functions for PSI inspections in tests.
 */
fun collectErrors(element: PsiElement): List<PsiErrorElement> {
    val list = mutableListOf<PsiErrorElement>()
    if (element is PsiErrorElement) list.add(element)
    element.children.forEach { list.addAll(collectErrors(it)) }
    return list
}
