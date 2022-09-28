package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.completion.util.*
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*
import gdscript.psi.utils.PsiGdExprUtil
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.psi.utils.PsiGdNamedUtil

// TODO Signals, ...?
// Variables, Constants, Enums, Locals
class GdClassMemberReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = "";
    private var methodOnly: Boolean = false;

    constructor(
        element: PsiElement,
        textRange: TextRange? = null,
        methodOnly: Boolean = false,
    ) : super(element as GdNamedElement, textRange) {
        val range = textRange ?: TextRange(0, element.textLength);
        key = element.text.substring(range.startOffset, range.endOffset);
        this.methodOnly = methodOnly;
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        myElement.setName(newElementName);

        return myElement;
    }

    override fun resolve(): PsiElement? {
        var file = element.containingFile;
        val fromClass = PsiGdExprUtil.getAttrOrCallParentClass(element);
        if (fromClass != null) {
            file = GdClassNamingIndex
                .get(fromClass, element.project, GlobalSearchScope.allScope(element.project))
                .firstOrNull()?.containingFile;
        }

        val direct =
            when (val element = PsiGdNamedUtil.findInParent(myElement, includingSelf = true, containingFile = file)) {
                is GdClassVarDeclTl -> element.classVarIdNmi;
                is GdVarDeclSt -> element.varNmi;
                is GdConstDeclSt -> element.varNmi;
                is GdConstDeclTl -> element.constIdNmi;
                is GdEnumDeclTl -> element.enumDeclNmi;
                is GdEnumValue -> element.enumValueNmi;
                is GdMethodDeclTl -> element.methodIdNmi;
                is GdParam -> element.varNmi;
                is GdVarNmi -> element;
                else -> null
            }
        if (direct != null) return direct;

        return GdClassNamingIndex
            .get(myElement.text, myElement.project, GlobalSearchScope.allScope(myElement.project))
            .firstOrNull()?.containingFile;
    }

    override fun getVariants(): Array<Any> {
        val files: ArrayList<PsiFile> = ArrayList();
        val results = ArrayList<LookupElement>();
        val static = referencesClassName(myElement);

        val file = getBaseFile(myElement) ?: return results.toArray();
        files.add(file);
        files.addAll(PsiGdNamedUtil.listParents(file).map { it.containingFile });

        val members = PsiGdNamedUtil.listLocalDecls(myElement);
        members.addAll(files.flatMap { PsiGdFileUtil.listMembers(it) });

        members.forEach {
            when (it) {
                // TODO je potřeba methodOnly? -> není to len pre static
                is GdVarDeclSt -> !methodOnly && !static && results.add(GdClassVarCompletionUtil.lookup(it))
                is GdConstDeclSt -> !methodOnly && !static && results.add(GdConstCompletionUtil.lookup(it))
                is GdClassVarDeclTl -> !methodOnly && !static && results.add(GdClassVarCompletionUtil.lookup(it))
                is GdConstDeclTl -> !methodOnly && results.add(GdConstCompletionUtil.lookup(it))
                is GdEnumDeclTl -> !methodOnly && !static && results.addAll(GdEnumCompletionUtil.lookup(it))
                is GdMethodDeclTl -> {
                    if (it.isStatic == static && !it.isConstructor) {
                        results.add(GdMethodCompletionUtil.lookup(it))
                    }
                }
                is GdParam -> !methodOnly && !static && results.add(GdMethodCompletionUtil.lookup(it))
                is GdForSt -> !methodOnly && !static && results.add(GdForLoopCompletionUtil.lookup(it))
            }
        };

        return results.toArray();
    }

    private fun referencesClassName(element: PsiElement): Boolean {
        val parent = element.parent?.parent;
        if (parent != null) {
            val type = parent.elementType;
            if (type == GdTypes.ATTRIBUTE_EX || type == GdTypes.CALL_EX) {
                val txt = parent.firstChild.text;
                return GdClassNamingIndex
                    .get(txt, element.project, GlobalSearchScope.allScope(element.project))
                    .firstOrNull() != null;
            }
        }

        return false;
    }

    private fun getBaseFile(element: PsiElement): PsiFile? {
        val parent =
            element.parent?.parent; // TODO tohle by mělo jít odstranit -> potřeba vyřešit return file class_name
        if (parent != null) {
            val type = parent.elementType;
            if (type == GdTypes.ATTRIBUTE_EX || type == GdTypes.CALL_EX) {
                if (parent.firstChild.text == GdKeywords.SELF) {
                    return element.containingFile;
                }

                val sdg = parent.firstChild;
                val sdsdg = parent.firstChild.text;
                var typed = (parent.firstChild as GdExpr).returnType;
                if (typed.isEmpty()) {
                    // TODO resolve call/attr expr to get hint
                    return null;
                } else if (typed.startsWith("Array")) { // TODO někam vykopnout
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