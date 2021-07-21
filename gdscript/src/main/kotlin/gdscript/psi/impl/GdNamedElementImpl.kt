package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import gdscript.psi.GdNamedElement
import org.jetbrains.annotations.NotNull

abstract class GdNamedElementImpl(node: @NotNull ASTNode) : ASTWrapperPsiElement(node), GdNamedElement
