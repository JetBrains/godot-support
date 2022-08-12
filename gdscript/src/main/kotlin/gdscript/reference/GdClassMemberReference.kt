package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.elementType
import gdscript.completion.util.GdClassVarCompletionUtil
import gdscript.completion.util.GdConstCompletionUtil
import gdscript.completion.util.GdEnumCompletionUtil
import gdscript.completion.util.GdMethodCompletionUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.psi.utils.PsiGdNamedUtil

// TODO Signals, Locals...?
// Variables, Constants, Enums,
class GdClassMemberReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = "";
    private var methodOnly: Boolean = false;

    constructor(element: PsiElement, textRange: TextRange, methodOnly: Boolean = false) : super(element as GdNamedElement, textRange) {
        key = element.text.substring(textRange.startOffset, textRange.endOffset);
        this.methodOnly = methodOnly;
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        myElement.setName(newElementName);

        return myElement;
    }

    override fun resolve(): PsiElement? =
        when (val element = PsiGdNamedUtil.findInParent(myElement, includingSelf = true)) {
            is GdClassVarDeclTl -> element.classVarIdNmi;
            is GdConstDeclTl -> element.constIdNmi;
            is GdEnumDeclTl -> element.enumDeclNmi;
            is GdEnumValue -> element.enumValueNmi;
            is GdMethodDeclTl -> element.methodIdNmi;
            else -> null
        }

    override fun getVariants(): Array<Any> {
        val files: ArrayList<PsiFile> = ArrayList();
        val results = ArrayList<LookupElement>();

        val file = getBaseFile(myElement) ?: return results.toArray();
        files.add(file);
        files.addAll(PsiGdNamedUtil.listParents(file).map { it.containingFile });

        val members = files.flatMap { PsiGdFileUtil.listMembers(it) }

        members.forEach {
            when (it) {
                is GdClassVarDeclTl -> !methodOnly && results.add(GdClassVarCompletionUtil.lookup(it))
                is GdConstDeclTl -> !methodOnly && results.add(GdConstCompletionUtil.lookup(it))
                is GdEnumDeclTl -> !methodOnly && results.addAll(GdEnumCompletionUtil.lookup(it))
                is GdMethodDeclTl -> results.add(GdMethodCompletionUtil.lookup(it))
            }
        };

        return results.toArray();
    }

    private fun getBaseFile(element: PsiElement): PsiFile? {
        val parent = element.parent?.parent;
        if (parent != null) {
            val type = parent.elementType;
            if (type == GdTypes.ATTRIBUTE_EX || type == GdTypes.CALL_EX) {
                var typed = (parent.firstChild as GdExpr).returnType;
                if (typed.isEmpty()) {
                    // TODO resolve call/attr expr to get hint
                    return null;
                } else if (typed.startsWith("Array")) {
                    typed = "Array";
                }
                val gdClass = GdClassNamingIndex
                    .get(typed, element.project, GlobalSearchScope.allScope(element.project))
                    .firstOrNull();

                return gdClass?.containingFile;
            }
        }

        return element.containingFile;
    }

}