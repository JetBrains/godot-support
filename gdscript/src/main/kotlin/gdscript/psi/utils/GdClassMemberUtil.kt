package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*

object GdClassMemberUtil {

    /**
     * Finds declaration (const, var, enum, signal, method, ...) of given NamedElement skipping itself
     */
    fun findDeclaration(
        element: GdNamedElement,
    ): PsiElement? {
        val name = element.name.orEmpty();
        val locals = listLocalDeclarationsUpward(element);
        if (locals.containsKey(name)) return locals[name];

        // This class is already scanned via localDecl - so move to extended ones
        var parent: PsiElement = PsiGdClassUtil.getParentClassElement(element);

        while (true) {
            parent = GdInheritanceUtil.getExtendedElement(parent) ?: return null;
            val members = listClassMemberDeclarations(parent);
            if (members.containsKey(name)) {
                return members[name]!!.first();
            }
        }
    }

    /**
     * Finds local declarations from current position upwards
     *
     * @return HashMap<name, PsiElement>
     */
    fun listLocalDeclarationsUpward(
        element: PsiElement,
    ): HashMap<String, PsiElement> {
        val locals: HashMap<String, PsiElement> = hashMapOf();
        var it: PsiElement = element;

        // To avoid matching self
        when (it.parent) {
            is GdClassVarDeclTl,
            is GdVarDeclSt,
            is GdConstDeclTl,
            is GdConstDeclSt,
            is GdEnumDeclTl,
            is GdSetDecl,
            is GdSignalDeclTl,
            is GdMethodDeclTl,
            -> {
                it = it.parent;
            }
        }

        while (true) {
            val movedToParent = it.prevSibling == null;
            it = it.prevSibling ?: it.parent ?: break;
            when (it) {
                is GdClassVarDeclTl -> locals[it.name] = it;
                is GdVarDeclSt -> locals[it.name] = it;
                is GdConstDeclTl -> locals[it.name] = it;
                is GdConstDeclSt -> locals[it.name] = it;
                is GdEnumDeclTl -> locals[it.name] = it;
                is GdSignalDeclTl -> locals[it.name] = it;
                is GdForSt -> if (movedToParent) locals[it.varNmi.name] = it;
                is GdPatternList -> {
                    if (movedToParent) {
                        PsiTreeUtil.getChildrenOfType(it, GdBindingPattern::class.java)
                            ?.map { b -> locals[b.varNmi.name] = b }
                    }
                }
                is GdSetDecl -> {
                    if (movedToParent) {
                        locals[it.varNmi?.name.orEmpty()] = it
                    }
                };
                is GdMethodDeclTl -> {
                    if (movedToParent) {
                        it.paramList?.paramList?.toList()
                            ?.map { p -> locals[p.varNmi.name] = p }
                    } else {
                        locals[it.name] = it;
                    }
                }

                // End of scope
                is GdClassDeclTl -> break;
            }
        }

        return locals;
    }

    /**
     * Finds local declarations from current position upwards
     *
     * @param classElement GdClassDecl|GdFile class containing element
     *
     * @return HashMap<name, MutableList<PsiElement>>
     */
    private fun listClassMemberDeclarations(classElement: PsiElement): HashMap<String, MutableList<PsiElement>> {
        val members: HashMap<String, MutableList<PsiElement>> = hashMapOf();

        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdConstDeclTl::class.java).forEach {
            val name = it.name;
            if (!members.containsKey(name)) members[name] = mutableListOf();
            members[name]!!.add(it)
        }
        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassVarDeclTl::class.java).forEach {
            val name = it.name;
            if (!members.containsKey(name)) members[name] = mutableListOf();
            members[name]!!.add(it)
        }
        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdEnumDeclTl::class.java).forEach {
            val name = it.name;
            if (!members.containsKey(name)) members[name] = mutableListOf();
            members[name]!!.add(it)
        }
        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdMethodDeclTl::class.java).forEach {
            val name = it.name;
            if (!members.containsKey(name)) members[name] = mutableListOf();
            members[name]!!.add(it)
        }
        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdSignalDeclTl::class.java).forEach {
            val name = it.name;
            if (!members.containsKey(name)) members[name] = mutableListOf();
            members[name]!!.add(it)
        }
        // TODO ii
        //PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassDeclTl::class.java).map { members[it.name] = it; }

        return members;
    }

}
