package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import gdscript.GdKeywords
import gdscript.index.impl.*
import gdscript.psi.*

object PsiGdNamedUtil {

    /**
     * List all parent Classes
     */
    fun listParents(element: PsiElement): MutableList<PsiFile> {
        var parentName = PsiGdInheritanceUtil.getFirstParentName(element);
        val parents = mutableListOf<PsiFile>();

        while (parentName !== null) {
            val parent = PsiGdInheritanceUtil.getPsiFile(parentName, element.project);
            if (parent === null) {
                return parents;
            }

            parents.add(parent);
            parentName = PsiGdInheritanceUtil.getParentName(parent);
        }

        return parents;
    }

    /**
     * Check if is a child of given Class
     */
    fun hasParent(myType: String, lookFor: String, project: Project): Boolean {
        var parentName: String? = myType;

        while (parentName !== null) {
            val parent = PsiGdInheritanceUtil.getPsiFile(parentName, project);
            if (parent === null) {
                return false;
            }

            parentName = PsiGdInheritanceUtil.getParentName(parent);
            if (lookFor == parentName) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds local declaration of NamedElement from its own position
     */
    @Deprecated("classmemeber util")
    private fun findLocalDecl(
        element: GdNamedElement,
    ): PsiElement? {
        // TODO use listLocals?
        val thisName = element.name.orEmpty();
        var parent: PsiElement = element;

        // To avoid matching self
        when (parent.parent) {
            is GdClassVarDeclTl,
            is GdConstDeclTl,
            is GdConstDeclSt,
            is GdVarDeclSt,
            is GdMethodDeclTl,
            is GdSignalDeclTl -> {
                parent = parent.parent;
            }
        }

        while (true) {
            parent = parent.prevSibling ?: parent.parent ?: return null;
            val match = when (parent) {
                is GdClassVarDeclTl -> if (parent.name == thisName) parent else null;
                is GdConstDeclTl -> if (parent.name == thisName) parent else null;
                is GdConstDeclSt -> if (parent.name == thisName) parent else null;
                is GdVarDeclSt -> if (parent.name == thisName) parent else null;
                is GdForSt -> if (parent.varNmi.name == thisName) parent.varNmi else null;
                is GdSetDecl -> if (parent.varNmi?.name == thisName) parent.varNmi else null;
                is GdSignalDeclTl -> if (parent.signalIdNmi?.name == thisName) parent.signalIdNmi else null;
                is GdPatternList -> {
                    val bindings = PsiTreeUtil.findChildrenOfType(parent, GdBindingPattern::class.java);
                    val patternMatch = bindings.find {
                        it.varNmi.name == thisName
                    }?.varNmi;
                    parent = PsiTreeUtil.getParentOfType(parent, GdMatchSt::class.java)!!;

                    patternMatch
                };
                is GdMethodDeclTl -> {
                    return parent.paramList?.paramList?.find {
                        it.varNmi.text == thisName
                    }
                };
                is GdClassDeclTl -> {
                    return null;
                };
                else -> null;
            }

            if (match != null) return match;
        }
    }

    // TODO ii
    fun listLocalDecls(
        element: PsiElement,
    ): MutableList<PsiElement> {
        val list = mutableListOf<PsiElement>();
        var parent: PsiElement = element;
        while (true) {
            parent = parent.prevSibling ?: parent.parent ?: return list;
            when (parent) {
                is GdConstDeclSt, is GdVarDeclSt, is GdForSt, is GdSetDecl, is GdSignalDeclTl -> list.add(parent);
                is GdPatternList -> {
                    list.addAll(PsiTreeUtil.findChildrenOfType(parent, GdBindingPattern::class.java));
                }
                is GdMethodDeclTl -> {
                    list.addAll(parent.paramList?.paramList?.toList() ?: emptyList());
                    return list;
                };
            }
        }
    }

    @Deprecated("findDeclaration")
    fun findInParent(
        element: GdNamedElement,
        method: Boolean = true,
        variables: Boolean = true,
        includingSelf: Boolean = false,
        containingFile: PsiFile? = null,
        withLocalScopes: Boolean = true,
    ): PsiElement? {
        val thisName = element.name.orEmpty();

        if (withLocalScopes) {
            val local = this.findLocalDecl(element);
            if (local != null) return local;
        }

        var parent: PsiElement? = PsiGdClassUtil.getParentContainer(containingFile ?: element);
        var parentName = if (parent!= null) PsiGdInheritanceUtil.getFirstParentName(parent).orEmpty() else "";

        if (includingSelf && parent != null) {
            lookFor(parent, thisName, variables, method)?.let {
                return it
            }
        }

        // TODO ii
//        while (parent !== null) {
//            parent = PsiGdInheritanceUtil.getPsiFile(parentName, parent.project);
//            if (parent == null) break;
//            lookFor(parent,
//                thisName,
//                variables,
//                method,
//            )?.let {
//                return it
//            }
//
//            parentName = PsiGdInheritanceUtil.getParentName(parent);
//        }

        return null;
    }

    @Deprecated("list Decls")
    private fun lookFor(
        rootElement: PsiElement,
        thisName: String,
        variables: Boolean,
        method: Boolean,
    ): PsiElement? {
        if (variables) {
            // Constants
            val parentConst = PsiTreeUtil.getStubChildrenOfTypeAsList(rootElement, GdConstDeclTl::class.java);
            parentConst.find { it.name == thisName }?.let { return it };

            // Variables
            val parentVar = PsiTreeUtil.getStubChildrenOfTypeAsList(rootElement, GdClassVarDeclTl::class.java);
            parentVar.find { it.name == thisName }?.let { return it };

            // Signals
            val signalVar = PsiTreeUtil.getStubChildrenOfTypeAsList(rootElement, GdSignalDeclTl::class.java);
            signalVar.find { it.name == thisName }?.let { return it };

            // Enums
            val enums = PsiTreeUtil.getStubChildrenOfTypeAsList(rootElement, GdEnumDeclTl::class.java);
            enums.forEach {
                val name = PsiGdEnumUtil.name(it);
                if (name != null) {
                    if (name == thisName) {
                        return it;
                    }
                } else {
                    it.enumValueList.forEach { value ->
                        if (value.enumValueNmi.name == thisName) {
                            return value;
                        }
                    }
                }
            }
        }

        if (method) {
            val parentMethod = PsiTreeUtil.getStubChildrenOfTypeAsList(rootElement, GdMethodDeclTl::class.java);
            parentMethod.find { it.name == thisName }?.let { return it };
        }

        return null;
    }

    fun setName(element: GdNamedElement, newName: String?): PsiElement {
        // TODO INF u const + signal u param name
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = GdElementFactory.identifier(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }
        return element
    }

    fun getName(element: GdNamedElement): String {
        return element.text;
    }

    fun getNameIdentifier(element: GdNamedIdElement): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);
        return keyNode?.psi;
    }

}