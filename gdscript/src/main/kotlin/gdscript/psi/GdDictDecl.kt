package gdscript.psi

import com.intellij.psi.PsiElement

interface GdDictDecl : PsiElement {
    val keyValueList: List<GdKeyValue>

    val newLineEndList: List<GdNewLineEnd>
}
