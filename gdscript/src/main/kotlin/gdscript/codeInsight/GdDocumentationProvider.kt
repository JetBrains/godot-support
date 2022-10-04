package gdscript.codeInsight

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import gdscript.psi.GdRefIdNm
import java.util.*

class GdDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (element is GdRefIdNm) {
//            val key: String = (element as SimpleProperty).getKey()
//            val value: String = (element as SimpleProperty).getValue()
//            val file = SymbolPresentationUtil.getFilePathPresentation(element.containingFile)
//            val docComment: String = SimpleUtil.findDocumentationComment(element as SimpleProperty)
            return renderFullDoc();
        }

        return null
    }

    fun findDocumentationComment(property: GdRefIdNm): String? {

//        val result: MutableList<String> = LinkedList()
//        var element: PsiElement = property.getPrevSibling()
//        while (element is PsiComment || element is PsiWhiteSpace) {
//            if (element is PsiComment) {
//                val commentText = element.getText().replaceFirst("[!# ]+".toRegex(), "")
//                result.add(commentText)
//            }
//            element = element.prevSibling
//        }
//        return StringUtil.join(Lists.reverse(result), "\n ")
        return null;
    }

    private fun renderFullDoc(): String {
        val sb = StringBuilder()
        sb.appendLine("Losos");
        sb.appendLine("Losos");
//        sb.append(DocumentationMarkup.DEFINITION_START)
//        sb.append("Simple Property")
//        sb.append(DocumentationMarkup.DEFINITION_END)
//        sb.append(DocumentationMarkup.CONTENT_START)
//        sb.append(value)
//        sb.append(DocumentationMarkup.CONTENT_END)
//        sb.append(DocumentationMarkup.SECTIONS_START)
//        addKeyValueSection("Key:", key, sb)
//        addKeyValueSection("Value:", value, sb)
//        addKeyValueSection("File:", file, sb)
//        addKeyValueSection("Comment:", docComment, sb)
//        sb.append(DocumentationMarkup.SECTIONS_END)
        return sb.toString()
    }

}