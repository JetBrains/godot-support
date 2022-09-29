package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.action.GdCreateMethodAction
import gdscript.index.impl.GdMethodDeclIndex
import gdscript.psi.*

class GdSetGetAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val type = if (element is GdSetMethodIdNm) {
            "set"
        } else if (element is GdGetMethodIdNm) {
            "get"
        } else {
            return;
        }

        if (GdMethodDeclIndex.get(element.text, element.project, GlobalSearchScope.fileScope(element.containingFile))
                .isEmpty()
        ) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Method [${element.text}] does not exist")
                .range(element.textRange)
                .withFix(if (type == "set") setMethod(element as GdSetMethodIdNm) else getMethod(element as GdGetMethodIdNm))
                .create()
        }
    }

    private fun setMethod(element: GdSetMethodIdNm): GdCreateMethodAction {
        val paramType = variableType(element);
        val param = if (paramType != null) {
            "value: $paramType"
        } else {
            "value"
        }

        return GdCreateMethodAction(
            element.name,
            parameters = arrayOf(param),
            bodyLines = arrayOf("${GdKeywords.SELF}.${variableName(element)} = value") // +;
        );
    }

    private fun getMethod(element: GdGetMethodIdNm): GdCreateMethodAction {
        return GdCreateMethodAction(
            element.name,
            returnType = variableType(element),
            bodyLines = arrayOf("return ${GdKeywords.SELF}.${variableName(element)}") // +;
        );
    }

    private fun variableName(element: PsiElement): String? {
        val varDecl = PsiTreeUtil.getParentOfType(element, GdClassVarDeclTl::class.java);

        return varDecl?.name;
    }

    private fun variableType(element: PsiElement): String? {
        val varDecl = PsiTreeUtil.getParentOfType(element, GdClassVarDeclTl::class.java);
        // TODO add hint to var decl stub & util
        val typed = PsiTreeUtil.getChildOfType(varDecl, GdTyped::class.java);

        return typed?.typeHintNmList?.first()?.text;
    }

}
