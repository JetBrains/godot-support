package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.completion.utils.*
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

    fun resolveDeclaration(): PsiElement? {
        val file = PsiGdExprUtil.getAttrOrCallParentFile(element) ?: element.containingFile;

        val local = PsiGdNamedUtil.findInParent(myElement, includingSelf = true, containingFile = file);
        if (local != null) return local;

        return PsiGdNamedUtil.findInParent(myElement, includingSelf = true, containingFile = PsiGdFileUtil.getGlobalFile(element.project));
    }

    override fun resolve(): PsiElement? {
        val direct =
            when (val element = resolveDeclaration()) {
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
            .get(element.text, element.project, GlobalSearchScope.allScope(element.project))
            .firstOrNull()?.containingFile;
    }

    override fun getVariants(): Array<Any> {
        val files: ArrayList<PsiFile> = ArrayList();
        val results = ArrayList<LookupElement>();
        val static = referencesClassName(element);

        // TODO zkontrolovat, jestli se nevrací Array[String]
        var file = PsiGdExprUtil.getAttrOrCallParentFile(element);
        val local = file == null;
        file = file ?: element.containingFile;
        files.add(file!!);
        files.addAll(PsiGdNamedUtil.listParents(file).map { it.containingFile });

        // TODO je tohle ok?
        val members = if (local) PsiGdNamedUtil.listLocalDecls(element) else mutableListOf();
        members.addAll(files.flatMap { PsiGdFileUtil.listMembers(it) });
        members.addAll(files.flatMap { PsiGdFileUtil.listMembers(PsiGdFileUtil.getGlobalFile(element.project)) });

        members.forEach {
            when (it) {
                is GdMethodDeclTl -> {
                    // TODO constructor to asi bude chtít napovídat...
                    if (it.isStatic == static && !it.isConstructor) {
                        results.add(GdCompletionUtil.lookup(it))
                    }
                }

                is GdConstDeclTl -> {
                    if (!methodOnly) {
                        results.add(GdCompletionUtil.lookup(it));
                    }
                }

                is GdVarDeclSt, is GdConstDeclSt, is GdClassVarDeclTl,
                is GdParam, is GdForSt, is GdEnumDeclTl, is GdSetDecl -> {
                    if (!methodOnly && !static) {
                        results.addAll(GdCompletionUtil.lookups(it));
                    }
                }
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

//    private fun getBaseFile(element: PsiElement): PsiFile? {
//        // TODO tohle by mělo jít odstranit -> potřeba vyřešit return file class_name
//        val parent = element.parent?.parent;
//        if (parent != null) {
//            val type = parent.elementType;
//            if (type == GdTypes.ATTRIBUTE_EX || type == GdTypes.CALL_EX) {
//                if (parent.firstChild.text == GdKeywords.SELF) {
//                    return element.containingFile;
//                }
//
//                var typed = (parent.firstChild as GdExpr).returnType;
//                if (typed.isEmpty()) {
//                    // TODO resolve call/attr expr to get hint
//                    return null;
//                } else if (typed.startsWith("Array")) { // TODO někam vykopnout
//                    typed = "Array";
//                }
//
//                val gdClass = GdClassNamingIndex
//                    .get(typed, element.project, GlobalSearchScope.allScope(element.project))
//                    .firstOrNull();
//
//                return gdClass?.containingFile;
//            }
//        }
//
//        return element.containingFile;
//    }

}