package com.jetbrains.godot.gdscript.redCode

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement

// Shared helper for red-code tests: collect all PsiErrorElements recursively
fun collectErrors(element: PsiElement): List<PsiErrorElement> {
  val list = mutableListOf<PsiErrorElement>()
  if (element is PsiErrorElement) list.add(element)
  element.children.forEach { list.addAll(collectErrors(it)) }
  return list
}
