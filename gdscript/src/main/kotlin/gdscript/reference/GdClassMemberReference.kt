package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.GlobalSearchScope
import gdscript.completion.utils.*
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*
import gdscript.psi.utils.*

// TODO Signals, ...? otestovat!!
/**
 * RefId reference to ClassNames, Variables, Constants, etc...
 */
class GdClassMemberReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = "";

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        key = element.text;
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        myElement.setName(newElementName); // TODO ii !! projít všude zda je srávná instance

        return myElement;
    }

    fun resolveDeclaration(): PsiElement? {
        return GdClassMemberUtil.findDeclaration(element);
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
                is GdSignalDeclTl -> element.signalIdNmi;
                is GdParam -> element.varNmi;
                is GdVarNmi -> element;
                else -> null
            }
        if (direct != null) return direct;

        return GdClassNamingIndex
            .get(element.text, element.project, GlobalSearchScope.allScope(element.project))
            .firstOrNull()?.containingFile;
    }

    override fun getVariants(): Array<LookupElement> {
        val members = GdClassMemberUtil.listDeclarations(element);
        val results = ArrayList<LookupElement>();

        members.forEach {
            when (it) {
                is GdMethodDeclTl -> results.add(GdCompletionUtil.lookup(it))
                is GdConstDeclTl -> results.add(GdCompletionUtil.lookup(it));
                is GdVarDeclSt, is GdConstDeclSt, is GdClassVarDeclTl, is GdSignalDeclTl, is GdClassNaming,
                is GdParam, is GdForSt, is GdEnumDeclTl, is GdSetDecl, is GdBindingPattern -> {
                    results.addAll(GdCompletionUtil.lookups(it));
                }
            }
        }

        return results.toTypedArray();
    }

}
