package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.index.impl.GdClassIdIndex
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*
import gdscript.settings.GdSettingsState

object GdClassMemberUtil {

    /**
     * Finds declaration (const, var, enum, signal, method, ...) of given NamedElement skipping itself
     */
    fun findDeclaration(
        element: GdNamedElement,
    ): PsiElement? {
        return listDeclarations(element, element).firstOrNull();
    }

    /**
     * List available declarations (const, var, enum, signal, method, ...) from given PsiElement skipping itself
     * @param searchFor stops and returns matching element
     */
    fun listDeclarations(element: PsiElement, searchFor: GdNamedElement? = null): Array<PsiElement> {
        val search = searchFor != null;
        val name: String? = searchFor?.name;
        var static = false;

        val result = mutableListOf<PsiElement>()
        var calledOn: String? = GdKeywords.SELF;
        when (val parent = element.parent?.parent) {
            is GdAttributeEx -> {
                if (element.parent.prevSibling != null) {
                    val ex = parent.exprList.first()!!;
                    calledOn = ex.returnType;
                    static = calledOn == ex.text;
                }
            }
            is GdCallEx -> {
                val prev = parent.parent;
                if (prev.text != GdKeywords.SELF && prev is GdAttributeEx) {
                    if (parent.prevSibling != null) {
                        val ex = prev.exprList.first()!!;
                        calledOn = ex.returnType;
                        static = calledOn == ex.text;
                    }
                }
            }
        }

        when (calledOn) {
            GdKeywords.SELF -> calledOn = null;
            GdKeywords.SUPER -> calledOn = GdInheritanceUtil.getExtendedClassId(element);
        }

        var parent: PsiElement?;

        // If it's stand-alone ref_id, adds also _GlobalScope & ClassNames
        if (calledOn == null) {
            parent = GdClassIdIndex.getGloballyResolved(GdKeywords.GLOBAL_SCOPE, element.project).firstOrNull();
            val local = addsParentDeclarations(GdClassUtil.getOwningClassElement(parent!!), result, static, name);
            if (search && local != null) return arrayOf(local);

            if (search) {
                val localClass = GdClassNamingIndex.getGlobally(searchFor!!).firstOrNull();
                if (localClass != null) return arrayOf(localClass);
            }
            result.addAll(GdClassNamingIndex.getAllValues(element.project));
        }

        // Checks locals only when it's not attribute/call expression moving declaration possibly outside
        if (calledOn == null) {
            val locals = listLocalDeclarationsUpward(element);
            if (search && locals.containsKey(name)) return arrayOf(locals[name]!!);
            result.addAll(locals.values);

            // This class is already scanned via localDecl - so move to extended one
            parent = GdInheritanceUtil.getExtendedElement(element);
        } else {
            parent = GdClassIdIndex.getGloballyResolved(calledOn, element.project).firstOrNull();
            if (parent != null)
                parent = GdClassUtil.getOwningClassElement(parent);
        }

        // Recursively iterate over all extended classes
        val local = collectFromParents(parent, result, static, name);
        if (local != null) return arrayOf(local);

        if (search) return emptyArray();
        return result.toTypedArray();
    }

    /**
     * Recursively iterate over all extended classes
     * Separately used for method overriding completion
     */
    fun collectFromParents(
        parent: PsiElement?,
        result: MutableList<PsiElement>,
        static: Boolean? = null,
        search: String? = null,
    ): PsiElement? {
        var par = parent;
        while (par != null) {
            val local = addsParentDeclarations(par, result, static, search);
            if (search != null && local != null) return local;
            par = GdInheritanceUtil.getExtendedElement(par);
        }

        return null;
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

    // TODO ii
    //    abs() -> Variant:
    //    Tady je potřeba z variant udělat cokoliv? .. resp. to je jako generic?

    /**
     * Filters out GdMethodsDeclTl & returns typed array
     */
    fun Array<PsiElement>.methods(): Array<GdMethodDeclTl> {
        return this.filterIsInstance<GdMethodDeclTl>().toTypedArray();
    }

    /**
     * List given element's class declarations
     */
    fun listClassMemberDeclarations(element: PsiElement, static: Boolean? = false): Array<PsiElement> {
        return listNamedClassMemberDeclarations(element, static).array();
    }

    /**
     * @param classElement GdClassDecl|GdFile class containing element
     * @param search String|null if looking for specific declaration
     *
     * @return should search param be not null, returns matching element
     */
    private fun addsParentDeclarations(
        classElement: PsiElement,
        result: MutableList<PsiElement>,
        static: Boolean? = false,
        search: String? = null,
    ): PsiElement? {
        val members = listNamedClassMemberDeclarations(classElement, static);
        if (search != null && members.containsKey(search)) {
            return members[search]!!.first();
        }

        result.addAll(members.array());

        return null;
    }

    /**
     * Finds local declarations from current position upwards
     *
     * @param element GdClassDecl|GdFile class containing element
     *
     * @return HashMap<name, MutableList<PsiElement>>
     */
    private fun listNamedClassMemberDeclarations(
        element: PsiElement,
        static: Boolean? = false,
    ): HashMap<String, MutableList<PsiElement>> {
        val classElement = when (element) {
            is GdFile, is GdClassDeclTl -> element;
            else -> GdClassUtil.getOwningClassElement(element);
        }

        val members: HashMap<String, MutableList<PsiElement>> = hashMapOf();

        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdConstDeclTl::class.java).forEach {
            val name = it.name;
            if (!members.containsKey(name)) members[name] = mutableListOf();
            members[name]!!.add(it)
        }

        if (static != true) {
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
            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdSignalDeclTl::class.java).forEach {
                val name = it.name;
                if (!members.containsKey(name)) members[name] = mutableListOf();
                members[name]!!.add(it)
            }
        }

        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdMethodDeclTl::class.java).forEach {
            if ((static == null || it.isStatic == static) && !it.isConstructor) {
                val name = it.name;
                if (!members.containsKey(name)) members[name] = mutableListOf();
                members[name]!!.add(it);
            }
        }
        // TODO ii inner classes musí napovídat také po resource ... :/
        //PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassDeclTl::class.java).map { members[it.name] = it; }

        return members;
    }

    /**
     * NamedClassMembers to Array
     */
    fun HashMap<String, MutableList<PsiElement>>.array(): Array<PsiElement> {
        return this.entries.flatMap {
            if (!GdSettingsState.hidePrivate || !it.key.startsWith("_")) {
                it.value
            } else {
                emptyList()
            }
        }.toTypedArray();
    }

}
