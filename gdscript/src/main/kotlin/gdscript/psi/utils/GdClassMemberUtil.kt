package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdClassVarDeclIndex
import gdscript.psi.*
import gdscript.settings.GdSettingsState
import gdscript.model.BoolVal

object GdClassMemberUtil {

    /**
     * Finds declaration (const, var, enum, signal, method, ...) of given NamedElement skipping itself
     */
    fun findDeclaration(
        element: GdNamedElement,
        onlyLocalScope: Boolean = false,
        ignoreParents: Boolean = false,
    ): PsiElement? {
        return listDeclarations(element, element, onlyLocalScope, ignoreParents).firstOrNull();
    }

    /**
     * List available declarations (const, var, enum, signal, method, ...) from given PsiElement skipping itself
     * @param searchFor stops and returns matching element
     */
    fun listDeclarations(
        element: PsiElement,
        searchFor: GdNamedElement,
        onlyLocalScope: Boolean = false,
        ignoreParents: Boolean = false,
    ): Array<PsiElement> {
        return listDeclarations(element, searchFor.name, onlyLocalScope, ignoreParents);
    }

    /**
     * List available declarations (const, var, enum, signal, method, ...) from given PsiElement skipping itself
     * @param searchFor stops and returns matching element
     */
    fun listDeclarations(
        element: PsiElement,
        searchFor: String? = null,
        onlyLocalScope: Boolean = false,
        ignoreParents: Boolean = false,
    ): Array<PsiElement> {
        var static = false;

        val result = mutableListOf<PsiElement>()
        var calledOn: String? = GdKeywords.SELF;

        val calledOnPsi: GdExpr? = calledUpon(element);
        if (calledOnPsi != null) {
            // Check if there is an assertion check 'if (node is Node3D):'
            calledOn = findIsTypeCheck(calledOnPsi) ?: calledOnPsi.returnType
            static = (calledOn == calledOnPsi.text) && checkGlobalStaticMatch(element, calledOn);
        }

        when (calledOn) {
            GdKeywords.SELF -> calledOn = null;
            GdKeywords.SUPER -> calledOn = GdInheritanceUtil.getExtendedClassId(element);
        }

        var parent: PsiElement?;

        // If it's stand-alone ref_id, adds also _Global & ClassNames - Classes are added as last due to matching name of some GlobalVars with class_name
        if (calledOn == null) {
            arrayOf(GdKeywords.GLOBAL_SCOPE, GdKeywords.GLOBAL_GD_SCRIPT).forEach {
                val globalParent = GdClassUtil.getClassIdElement(it, element)
                if (globalParent != null) {
                    val local = addsParentDeclarations(
                        GdClassUtil.getOwningClassElement(globalParent),
                        result,
                        static,
                        searchFor
                    );
                    if (searchFor != null && local != null) return arrayOf(local);
                }
            }
        }

        val hitLocal = BoolVal.new();
        // Checks locals only when it's not attribute/call expression moving declaration possibly outside
        if (calledOn == null) {
            val locals = listLocalDeclarationsUpward(element, onlyLocalScope, hitLocal);
            if (searchFor != null && locals.containsKey(searchFor)) return arrayOf(locals[searchFor]!!);
            result.addAll(locals.values);

            // This class is already scanned via localDecl - so move to extended one
            parent = if (onlyLocalScope) {
                GdInheritanceUtil.getExtendedElement(element);
            } else {
                GdClassUtil.getOwningClassElement(element);
            }
        } else {
            // For Enum add also all it's values
            if (calledOn.endsWith("Dictionary") && calledOnPsi != null /*&& calledOnPsi.firstChild is GdRefIdNm*/) {
                val firstChild = PsiTreeUtil.collectElementsOfType(calledOnPsi, GdRefIdNm::class.java).lastOrNull();
                if (firstChild != null) {
                    val dictDecl = findDeclaration(firstChild);
                    if (dictDecl is GdEnumDeclTl) {
                        if (searchFor != null) {
                            val localVal = dictDecl.enumValueList.find { eval -> eval.enumValueNmi.name == searchFor };
                            if (localVal != null) return arrayOf(localVal);
                        }
                        result.addAll(dictDecl.enumValueList);
                    }
                }
            }

            parent = GdClassUtil.getClassIdElement(calledOn, element)
            if (parent == null) {
                parent = GdClassUtil.getClassIdElement(
                    "${GdClassUtil.getFullClassId(element)}.${calledOn}",
                    element,
                );
            }

            if (parent != null) {
                parent = GdClassUtil.getOwningClassElement(parent);
            }
        }

        // Recursively iterate over all extended classes
        if (!ignoreParents && !hitLocal.value) {
            val local = collectFromParents(parent, result, static, searchFor);
            if (local != null) return arrayOf(local);
        }

        if (calledOn == null) {
            if (searchFor != null) {
                val localClass = GdClassNamingIndex.getGlobally(searchFor, element).firstOrNull();
                if (localClass != null) return arrayOf(localClass);
            }
            result.addAll(GdClassNamingIndex.getAllValues(element.project));
        }

        if (searchFor != null) return emptyArray();
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
        onlyLocalScope: Boolean = false,
        hitLocal: BoolVal? = null,
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
            is GdParam,
            is GdForSt, // todo neodzkoušeno
            is GdBindingPattern, // todo neodzkoušeno
            -> {
                it = it.parent;
            }
        }
        var isParam = false;
        when (it) {
            is GdParam,
            -> {
                isParam = true;
                it = it.prevSibling ?: it.parent;
            }
        }

        while (true) {
            val movedToParent = it?.prevSibling == null;
            if (it is GdFile) break
            it = it.prevSibling ?: it.parent ?: break;
            when (it) {
                is GdClassVarDeclTl -> locals[it.name] = it;
                is GdVarDeclSt -> locals[it.name] = it;
                is GdConstDeclTl -> locals[it.name] = it;
                is GdConstDeclSt -> locals[it.name] = it;
                is GdEnumDeclTl -> locals[it.name] = it;
                is GdSignalDeclTl -> locals[it.name] = it;
                is GdParam -> {
                    locals[it.varNmi.name] = it;
                };
                is GdForSt -> if (movedToParent) locals[it.varNmi?.name ?: ""] = it;
                is GdPatternList -> {
                    if (movedToParent) {
                        PsiTreeUtil.getChildrenOfType(it, GdBindingPattern::class.java)
                            ?.map { b -> locals[b.varNmi.name] = b }
                    }
                }
                is GdSetDecl -> {
                    if (movedToParent) {
                        locals[it.varNmi?.name.orEmpty()] = it.varNmi!!
                    }
                };
                is GdMethodDeclTl -> {
                    if (onlyLocalScope) {
                        if (!isParam) {
                            it.paramList?.paramList?.forEach { p ->
                                locals[p.varNmi.name] = p;
                            }
                        }
                        if (hitLocal != null) hitLocal.value = true;
                        break;
                    }
                    if (movedToParent) {
                        it.paramList?.paramList?.forEach { p ->
                            locals[p.varNmi.name] = p;
                        }
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

    fun firstNamedDeclaration(element: PsiElement): PsiElement? {
        return PsiTreeUtil.findFirstParent(element) {
            it is GdClassVarDeclTl
                    || it is GdVarDeclSt
                    || it is GdConstDeclTl
                    || it is GdConstDeclSt
                    || it is GdMethodDeclTl
                    || it is GdClassDeclTl
                    || it is GdParam
        }
    }

    fun firstNamedDeclarationName(element: PsiElement): String? {
        return when (val it = firstNamedDeclaration(element)) {
            is GdClassVarDeclTl -> it.name
            is GdVarDeclSt -> it.name
            is GdConstDeclTl -> it.name
            is GdConstDeclSt -> it.name
            is GdMethodDeclTl -> it.name
            is GdClassDeclTl -> it.name
            is GdParam -> it.varNmi.name
            is GdSignalDeclTl -> it.name
            else -> null
        }
    }

    /**
     * Filters out GdMethodsDeclTl
     */
    fun Array<PsiElement>.methods(): Array<GdMethodDeclTl> {
        return this.filterIsInstance<GdMethodDeclTl>().toTypedArray();
    }

    /**
     * Filters out GdMethodsDeclTl
     */
    fun List<PsiElement>.methods(): Array<GdMethodDeclTl> {
        return this.filterIsInstance<GdMethodDeclTl>().toTypedArray();
    }

    /**
     * Filters out GdClassVarDeclTl
     */
    fun List<PsiElement>.variables(): Array<GdClassVarDeclTl> {
        return this.filterIsInstance<GdClassVarDeclTl>().toTypedArray();
    }

    /**
     * Filters out GdEnumDeclTl
     */
    fun List<PsiElement>.enums(): Array<GdEnumDeclTl> {
        return this.filterIsInstance<GdEnumDeclTl>().toTypedArray();
    }

    /**
     * Filters out GdConstDeclTl
     */
    fun List<PsiElement>.constants(): Array<GdConstDeclTl> {
        return this.filterIsInstance<GdConstDeclTl>().toTypedArray();
    }

    /**
     * Filters out GdSignalDeclTl
     */
    fun List<PsiElement>.signals(): Array<GdSignalDeclTl> {
        return this.filterIsInstance<GdSignalDeclTl>().toTypedArray();
    }

    /**
     * Finds local declarations from current position upwards
     *
     * @param element GdClassDecl|GdFile class containing element
     *
     * @return HashMap<name, MutableList<PsiElement>>
     */
    fun listClassMemberDeclarations(
        element: PsiElement,
        static: Boolean? = false,
        search: String? = null,
        constructors: Boolean = false,
    ): MutableList<PsiElement> {
        val classElement = when (element) {
            is GdFile, is GdClassDeclTl -> element;
            else -> GdClassUtil.getOwningClassElement(element);
        }

        val members = mutableListOf<PsiElement>();

        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdConstDeclTl::class.java).forEach {
            if (search != null && it.name == search) return mutableListOf(it);
            members.add(it)
        }
        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdEnumDeclTl::class.java).forEach {
            if (it.enumDeclNmi != null) {
                if (search != null && it.name == search) return mutableListOf(it)
                members.add(it)
            } else {
                it.enumValueList.forEach { value ->
                    if (search != null && value.enumValueNmi.name == search) return mutableListOf(value)
                    members.add(value)
                }
            }
        }
        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdSignalDeclTl::class.java).forEach {
            if (search != null && it.name == search) return mutableListOf(it);
            members.add(it)
        }
        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassDeclTl::class.java).forEach {
            if (search != null && it.name == search) return mutableListOf(it);
            members.add(it)
            members.addAll(listClassMemberDeclarations(it, static, search))
        }

        if (static != true) {
            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassVarDeclTl::class.java).forEach {
                if (search != null && it.name == search) return mutableListOf(it);
                members.add(it)
            }
        }

        PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdMethodDeclTl::class.java).forEach {
            if (it.name == "from_euler") {
                var asd = 11
            }
            if ((static == null || it.isStatic == static)) {
                if (constructors || !it.isConstructor) {
                    if (search != null && it.name == search) return mutableListOf(it);
                    members.add(it)
                }
            }
        }
        if (search != null) return mutableListOf();

        return members;
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
        val list = listClassMemberDeclarations(classElement, static, search);
        if (search != null) return list.firstOrNull();
        result.addAll(list);

        return null;
    }

    /**
     * NamedClassMembers to Array
     */
    fun HashMap<String, MutableList<PsiElement>>.array(): Array<PsiElement> {
        return this.entries.flatMap {
            if (!GdSettingsState.getInstance().state.hidePrivate || !it.key.startsWith("_")) {
                it.value
            } else {
                emptyList()
            }
        }.toTypedArray();
    }

    fun calledUpon(element: PsiElement): GdExpr? {
        val getAttrIfAny = fun (el: PsiElement): GdExpr? {
            val previous = PsiTreeUtil.prevVisibleLeaf(element)
            if (previous?.elementType == GdTypes.DOT && previous?.parent?.elementType == GdTypes.ATTRIBUTE_EX) {
                val nestedLast = PsiTreeUtil.collectElementsOfType(
                    (previous!!.parent as GdAttributeEx).exprList.first(),
                    GdExpr::class.java,
                )
                return nestedLast.last()
                //return (previous!!.parent as GdAttributeEx).exprList.first()
            }
            return null
        }

        val attr = getAttrIfAny(element)
        if (attr != null) return attr

        val next = PsiTreeUtil.nextVisibleLeaf(element)
        if (next?.elementType == GdTypes.LRBR && next?.parent?.elementType == GdTypes.CALL_EX) {
            return getAttrIfAny(next!!.parent)
        }

        return null
    }

    /**
     * _GlobalScope has matching variables with classes
     */
    private fun checkGlobalStaticMatch(element: PsiElement, name: String): Boolean {
        val virtualFile = FilenameIndex.getVirtualFilesByName(
            "${GdKeywords.GLOBAL_SCOPE}.gd",
            GlobalSearchScope.allScope(element.project)
        ).firstOrNull() ?: return true;
        val psiFile = PsiManager.getInstance(element.project).findFile(virtualFile) ?: return true;

        return GdClassVarDeclIndex.get(
            name,
            element.project,
            GlobalSearchScope.fileScope(psiFile),
        ).isEmpty();
    }

    /**
     * Looks for statements of type checks
     *  if node is Node3D:
     *  while next is Node3D:
     * and returns correct type for hint & validation
     */
    private fun findIsTypeCheck(element: PsiElement): String? {
        val getParent = fun (stmt: PsiElement?): PsiElement? {
            return PsiTreeUtil.getParentOfType(stmt, GdIfSt::class.java, GdWhileSt::class.java, GdElifSt::class.java)
        }
        val getIsType = fun (element: PsiElement, stmt: PsiElement?): String? {
            val expr = PsiTreeUtil.findChildOfType(stmt, GdIsEx::class.java) ?: return null
            if (element.text == expr.expr.text) return expr.returnType
            return null
        }

        var parent = getParent(element)
        while (parent != null) {
            when (parent) {
                is GdIfSt -> {
                    val typed = getIsType(element, parent.expr)
                    if (typed != null) return typed
                }
                is GdElifSt -> {
                    val typed = getIsType(element, parent.expr)
                    if (typed != null) return typed
                    // To avoid matching from base condition that is not part of this suite
                    parent = PsiTreeUtil.getParentOfType(parent, GdIfSt::class.java)
                }
                is GdWhileSt -> {
                    val typed = getIsType(element, parent.expr)
                    if (typed != null) return typed
                }
            }
            parent = getParent(parent)
        }

        return null
    }

}
