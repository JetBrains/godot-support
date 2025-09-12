package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.index.impl.*
import gdscript.model.BoolVal
import gdscript.psi.*
import project.psi.util.ProjectAutoloadUtil

object GdClassMemberUtil {

    /**
     * Finds declaration (const, var, enum, signal, method, ...) of given NamedElement skipping itself
     */
    fun findDeclaration(
        element: PsiElement,
        onlyLocalScope: Boolean = false,
        ignoreParents: Boolean = false,
        ignoreGlobalScope: Boolean = false,
    ): Any? {
        return listDeclarations(element, element, onlyLocalScope, ignoreParents, ignoreGlobalScope).firstOrNull()
    }

    /**
     * List available declarations (const, var, enum, signal, method, ...) from given PsiElement skipping itself
     * @param searchFor stops and returns matching element
     */
    @SuppressWarnings()
    fun listDeclarations(
        element: PsiElement,
        searchFor: PsiElement,
        onlyLocalScope: Boolean = false,
        ignoreParents: Boolean = false,
        ignoreGlobalScope: Boolean = false,
    ): Array<Any> {
        return listDeclarations(element, searchFor.text, onlyLocalScope, ignoreParents, ignoreGlobalScope)
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
        ignoreGlobalScope: Boolean = false,
        allowResource: Boolean = false,
    ): Array<Any> {
        var static: Boolean? = false
        val project = element.project

        val result = mutableListOf<Any>()
        var calledOn: String? = GdKeywords.SELF

        val calledOnPsi: GdExpr? = calledUpon(element)
        val calledOnPsiName = calledOnPsi?.text ?: ""
        if (calledOnPsi != null && calledOnPsiName != GdKeywords.SELF) {
            // Check if there is an assertion check 'if (node is Node3D):'
            var isCheckedSuccess = false
            val isChecked = findIsTypeCheck(element)
            if (isChecked != null) {
                val isExpr = isChecked.expr.firstChild
                if (isExpr is GdRefIdRef && isExpr.text == calledOnPsiName) {
                    calledOn = PsiGdExprUtil.fromTyped(isChecked.typedVal)
                    isCheckedSuccess = true
                }
            }

            if (!isCheckedSuccess) {
                calledOn = calledOnPsi.getReturnTypeOrRes(allowResource)
            }

            if (calledOn != null) {
                if (calledOn.startsWith("Array[")) calledOn = "Array"
                // solution to check if statically called on current inner class - would be nice to come up with clean solution
                static = (calledOn == calledOnPsi.text || calledOn == "${GdClassUtil.getFullClassId(calledOnPsi)}.${calledOnPsi.text}") && checkGlobalStaticMatch(element, calledOn)
                if (calledOn.endsWith(".gd")) {
                    static = null
                }
            }
        }

        if (static == false && (calledOn == null || calledOn == GdKeywords.SELF)) {
            when (val ownerMethod = PsiTreeUtil.getParentOfType(
                calledOnPsi ?: element,
                GdMethodDeclTl::class.java,
            )) {
                is GdMethodDeclTl -> {
                    static = ownerMethod.isStatic
                }
            }
        }

        when (calledOn) {
            GdKeywords.SELF -> calledOn = null
            GdKeywords.SUPER -> calledOn = GdInheritanceUtil.getExtendedClassId(element)
        }

        var parent: PsiElement?

        // If it's stand-alone ref_id, adds also _Global & ClassNames - Classes are added as last due to matching name of some GlobalVars with class_name
        if (calledOn == null && !ignoreGlobalScope) {
            arrayOf(GdKeywords.GLOBAL_SCOPE, GdKeywords.GLOBAL_GD_SCRIPT).forEach {
                val globalParent = GdClassUtil.getClassIdElement(it, element)
                if (globalParent != null) {
                    val local = addsParentDeclarations(
                        GdClassUtil.getOwningClassElement(globalParent),
                        result,
                        null,
                        searchFor
                    )
                    if (searchFor != null && local != null) return arrayOf(local)
                }
            }
        }

        val hitLocal = BoolVal.new()
        // Checks locals only when it's not attribute/call expression moving declaration possibly outside
        if (calledOn == null) {
            val locals = listLocalDeclarationsUpward(element, onlyLocalScope, hitLocal)
            if (searchFor != null && locals.containsKey(searchFor)) return arrayOf(locals[searchFor]!!)
            result.addAll(locals.values)

            // This class is already scanned via localDecl - so move to extended one
            parent = if (onlyLocalScope) {
                GdInheritanceUtil.getExtendedElement(element)
            } else {
                GdClassUtil.getOwningClassElement(element)
            }
        } else {
            // Normalize typed Array[K] to base Array
            if (calledOn.startsWith("Array[")) calledOn = "Array"

            // Normalize typed Dictionary[K, V] to base Dictionary
            if (calledOn.startsWith("Dictionary[")) {
                val firstChild = PsiTreeUtil.collectElementsOfType(calledOnPsi, GdRefIdRef::class.java).lastOrNull()
                if (firstChild != null) {
                    val dictDecl = findDeclaration(firstChild)
                    if (dictDecl is GdEnumDeclTl) {
                        if (searchFor != null) {
                            val localVal = dictDecl.enumValueList.find { eval -> eval.enumValueNmi.name == searchFor }
                            if (localVal != null) return arrayOf(localVal)
                        }
                        result.addAll(dictDecl.enumValueList)
                    }
                }
                calledOn = "Dictionary"
            }

            // If qualifier resolves to a named enum, expose its values for member lookup (e.g., _Anim.FLOOR)
            findDeclaration(calledOnPsi!!)?.let { decl ->
                if (decl is GdEnumDeclTl) {
                    if (searchFor != null) {
                        val localVal = decl.enumValueList.find { eval -> eval.enumValueNmi.name == searchFor }
                        if (localVal != null) return arrayOf(localVal)
                    }
                    result.addAll(decl.enumValueList)
                    if (searchFor == null) return result.toTypedArray()
                }
            }

            parent = GdClassUtil.getClassIdElement(calledOn, element, project)
            if (parent == null) {
                val classId = GdClassUtil.getFullClassId(element)
                parent = GdClassUtil.getClassIdElement(
                    "$classId.${calledOn}",
                    element,
                )
                // Try autoload classes
                if (parent == null) {
                    parent = ProjectAutoloadUtil.findFromAlias(calledOn, element)
                    if (parent != null) static = false
                }
            }

            if (parent != null) {
                parent = GdClassUtil.getOwningClassElement(parent)
            }
        }

        // Recursively iterate over all extended classes
        if (!ignoreParents && !hitLocal.value) {
            val local = collectFromParents(parent, result, project, static, searchFor)
            if (local != null) return arrayOf(local)
        }

        if (calledOn == null) {
            val autoLoads = ProjectAutoloadUtil.listGlobals(project)
            if (searchFor != null) {
                val localClass = GdClassNamingIndex.INSTANCE.getGlobally(searchFor, element).firstOrNull()
                if (localClass != null) return arrayOf(localClass)
                val autoLoaded = autoLoads.find { it.key == searchFor }
                if (autoLoaded != null) return arrayOf(autoLoaded)
            }
            result.addAll(GdClassNamingIndex.INSTANCE.getAllValues(project))
            result.addAll(autoLoads)
        }

        if (searchFor != null) return emptyArray()
        return result.toTypedArray()
    }

    /**
     * Recursively iterate over all extended classes
     * Separately used for method overriding completion
     */
    fun collectFromParents(
        parent: PsiElement?,
        result: MutableList<Any>,
        project: Project,
        static: Boolean? = null,
        search: String? = null,
    ): PsiElement? {
        var par = parent
        while (par != null) {
            val local = addsParentDeclarations(par, result, static, search)
            if (search != null && local != null) return local
            if (par is GdClassDeclTl) {
                // When within classDecl, check also root of current file, not only what the class is extending
                val local = addsParentDeclarations(par.containingFile, result, static, search)
                if (search != null && local != null) return local
            }

            par = GdInheritanceUtil.getExtendedElement(par, project)
        }

        return null
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
        val locals: HashMap<String, PsiElement> = hashMapOf()
        var it: PsiElement = element

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
            is GdForSt,
            is GdBindingPattern,
            -> {
                it = it.parent
            }
        }
        var isParam = false
        when (it) {
            is GdParam,
            -> {
                isParam = true
                it = it.prevSibling ?: it.parent
            }
        }

        while (true) {
            val movedToParent = it.prevSibling == null
            it = it.prevSibling ?: it.parent ?: break
            if (it is PsiFile) break // avoid directory traversal
            when (it) {
                is GdClassVarDeclTl -> if (!locals.contains(it.name)) locals[it.name] = it
                is GdVarDeclSt -> if (!locals.contains(it.name)) locals[it.name] = it
                is GdConstDeclTl -> if (!locals.contains(it.name)) locals[it.name] = it
                is GdConstDeclSt -> if (!locals.contains(it.name)) locals[it.name] = it
                is GdEnumDeclTl -> if (!locals.contains(it.name)) locals[it.name] = it
                is GdSignalDeclTl -> if (!locals.contains(it.name)) locals[it.name] = it
                is GdParam -> {
                    if (!locals.contains(it.varNmi.name)) locals[it.varNmi.name] = it
                }

                is GdForSt -> if (movedToParent && !locals.contains(it.varNmi?.name ?: "")) locals[it.varNmi?.name
                    ?: ""] = it

                is GdPatternList -> {
                    if (movedToParent) {
                        PsiTreeUtil.getChildrenOfType(it, GdBindingPattern::class.java)
                            ?.map { b -> if (!locals.contains(b.varNmi.name)) locals[b.varNmi.name] = b }
                    }
                }

                is GdSetDecl -> {
                    if (movedToParent) {
                        if (!locals.contains(it.varNmi?.name.orEmpty())) locals[it.varNmi?.name.orEmpty()] = it.varNmi!!
                    }
                }

                is GdFuncDeclEx -> {
                    if (movedToParent && !isParam) {
                        it.paramList?.paramList?.forEach { p ->
                            if (!locals.contains(p.varNmi.name)) locals[p.varNmi.name] = p
                        }
                    }
                }

                is GdMethodDeclTl -> {
                    if (onlyLocalScope) {
                        if (!isParam) {
                            it.paramList?.paramList?.forEach { p ->
                                if (!locals.contains(p.varNmi.name)) locals[p.varNmi.name] = p
                            }
                        }
                        if (hitLocal != null) hitLocal.value = true
                        break
                    }
                    if (movedToParent) {
                        it.paramList?.paramList?.forEach { p ->
                            if (!locals.contains(p.varNmi.name)) locals[p.varNmi.name] = p
                        }
                    } else {
                        if (!locals.contains(it.name)) locals[it.name] = it
                    }
                }

                // End of scope
                is GdClassDeclTl -> { if (movedToParent) { break } }
            }
        }

        return locals
    }

    @SuppressWarnings()
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
    fun Array<Any>.methods(): Array<GdMethodDeclTl> {
        return this.filterIsInstance<GdMethodDeclTl>().toTypedArray()
    }

    /**
     * Filters out GdMethodsDeclTl
     */
    fun List<Any>.methods(): Array<GdMethodDeclTl> {
        return this.filterIsInstance<GdMethodDeclTl>().toTypedArray()
    }

    /**
     * Filters out GdMethodsDeclTl of constructors
     */
    fun List<PsiElement>.constructors(): Array<GdMethodDeclTl> {
        return this.filterIsInstance<GdMethodDeclTl>().filter { it.isConstructor }.toTypedArray()
    }

    /**
     * Filters out GdClassVarDeclTl
     */
    fun List<PsiElement>.variables(): Array<GdClassVarDeclTl> {
        return this.filterIsInstance<GdClassVarDeclTl>().toTypedArray()
    }

    /**
     * Filters out GdEnumDeclTl
     */
    fun List<PsiElement>.enums(): Array<GdEnumDeclTl> {
        return this.filterIsInstance<GdEnumDeclTl>().toTypedArray()
    }

    /**
     * Filters out GdConstDeclTl
     */
    fun List<PsiElement>.constants(): Array<GdConstDeclTl> {
        return this.filterIsInstance<GdConstDeclTl>().toTypedArray()
    }

    /**
     * Filters out GdSignalDeclTl
     */
    fun List<PsiElement>.signals(): Array<GdSignalDeclTl> {
        return this.filterIsInstance<GdSignalDeclTl>().toTypedArray()
    }

    /**
     * Retrieves the declarations of class members such as methods, variables, signals, enums, constants,
     * or inner classes from a specified class element.
     *
     * @param element The PsiElement representing the class or file whose members are to be listed.
     * @param static Optional parameter that filters the results to include only static members
     * if true, only non-static members if false, or all members if null.
     * @param search Optional filter parameter to search for a specific member by name. If null, all members will be returned.
     * @param constructors If true, includes constructor members in the result; otherwise, filters them out.
     * @param isRecursive If true, includes members from nested classes by traversing recursively.
     * @return A mutable list of PsiElement representing the class member declarations matching the provided criteria.
     */
    fun listClassMemberDeclarations(
        element: PsiElement,
        static: Boolean? = false,
        search: String? = null,
        constructors: Boolean = false,
        isRecursive: Boolean = false,
    ): MutableList<PsiElement> {
        val classElement = when (element) {
            is GdFile, is GdClassDeclTl -> element
            else -> GdClassUtil.getOwningClassElement(element)
        }

        val members = mutableListOf<PsiElement>()

        if (search != null) {
            val project = element.project
            val scope = GlobalSearchScope.fileScope(element.containingFile)

            GdMethodDeclIndex.INSTANCE.getScoped(search, project, scope).firstOrNull()?.let {
                if ((static == null || it.isStatic == static)) {
                    if (constructors || !it.isConstructor) {
                        if (GdClassUtil.getOwningClassElement(it) == classElement) {
                            return mutableListOf(it)
                        }
                    }
                }
            }
            GdConstDeclIndex.INSTANCE.getScoped(search, project, scope).firstOrNull()
                ?.let { if (GdClassUtil.getOwningClassElement(it) == classElement) it else null }?.let { return mutableListOf(it) }
            GdEnumDeclIndex.INSTANCE.getScoped(search, project, scope).firstOrNull()
                ?.let { if (GdClassUtil.getOwningClassElement(it) == classElement) it else null }?.let { return mutableListOf(it) }
            GdSignalDeclIndex.INSTANCE.getScoped(search, project, scope).firstOrNull()
                ?.let { if (GdClassUtil.getOwningClassElement(it) == classElement) it else null }?.let { return mutableListOf(it) }
            GdClassVarDeclIndex.INSTANCE.getScoped(search, project, scope).firstOrNull()
                ?.let { if (GdClassUtil.getOwningClassElement(it) == classElement) it else null }
                ?.let { if (static != true || it.isStatic) return mutableListOf(it) }

            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassDeclTl::class.java).forEach {
                if (it.name == search) return mutableListOf(it)
                if (isRecursive) {
                    members.addAll(listClassMemberDeclarations(it, static, search))
                    if (members.size > 0) return members
                }
            }
            if (classElement is GdClassDeclTl && !isRecursive) {
                PsiTreeUtil.getStubChildrenOfTypeAsList(classElement.parent, GdClassDeclTl::class.java).forEach {
                    if (it.name == search) return mutableListOf(it)
                    members.addAll(listClassMemberDeclarations(it, static, search, false, true))
                    if (members.size > 0) return members
                }
            }

            // TODO Create stub for unnamed enums
            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdEnumDeclTl::class.java).forEach {
                if (it.name.isBlank()) {
                    it.enumValueList.forEach { value ->
                        if (value.enumValueNmi.name == search) return mutableListOf(value)
                    }
                }
            }
        } else {
            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdConstDeclTl::class.java).forEach {
                members.add(it)
            }
            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdEnumDeclTl::class.java).forEach {
                if (it.name.isNotBlank()) {
                    members.add(it)
                } else {
                    it.enumValueList.forEach { value ->
                        members.add(value)
                    }
                }
            }
            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdSignalDeclTl::class.java).forEach {
                members.add(it)
            }
            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassDeclTl::class.java).forEach {
                // For completion and general member listing, include only direct inner classes,
                // not members of their inner trees. Deeper members are reachable after further qualification.
                members.add(it)
            }
            if (classElement is GdClassDeclTl && !isRecursive) {
                PsiTreeUtil.getStubChildrenOfTypeAsList(classElement.parent, GdClassDeclTl::class.java).forEach {
                    members.addAll(listClassMemberDeclarations(it, static, null, false, true))
                }
            }

            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassVarDeclTl::class.java).forEach {
                if (static != true || it.isStatic) {
                    members.add(it)
                }
            }

            PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdMethodDeclTl::class.java).forEach {
                if ((static == null || it.isStatic == static)) {
                    if (constructors || !it.isConstructor) {
                        members.add(it)
                    }
                }
            }
        }
        if (search != null) return mutableListOf()

        return members
    }

    /**
     * @param classElement GdClassDecl|GdFile class containing element
     * @param search String|null if looking for specific declaration
     *
     * @return should search param be not null, returns matching element
     */
    private fun addsParentDeclarations(
        classElement: PsiElement,
        result: MutableList<Any>,
        static: Boolean? = false,
        search: String? = null,
    ): PsiElement? {
        val list = listClassMemberDeclarations(classElement, static, search)
        if (search != null) return list.firstOrNull()
        result.addAll(list)

        return null
    }

    fun calledUpon(element: PsiElement): GdExpr? {
        val getAttrIfAny = fun(el: PsiElement): GdExpr? {
            val previous = PsiTreeUtil.prevVisibleLeaf(el) ?: return null
            val parent = previous.parent ?: return null
            if (previous.elementType == GdTypes.DOT && parent is GdAttributeEx) {
                return parent.expr
            }
            return null
        }

        val attr = getAttrIfAny(element)
        if (attr != null) return attr

        val next = PsiTreeUtil.nextVisibleLeaf(element)
        if (next?.elementType == GdTypes.LRBR && next.parent?.elementType == GdTypes.CALL_EX) {
            return getAttrIfAny(next.parent)
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
        ).firstOrNull() ?: return true
        val psiFile = PsiManager.getInstance(element.project).findFile(virtualFile) ?: return true

        return GdClassVarDeclIndex.INSTANCE.get(
            name,
            element.project,
            GlobalSearchScope.fileScope(psiFile),
        ).isEmpty()
    }

    /**
     * Looks for statements of type checks
     *  if node is Node3D:
     *  while next is Node3D:
     * and returns correct type for hint & validation
     */
    private fun findIsTypeCheck(element: PsiElement): GdIsEx? {
        // TODO je to dost na hrubo a nekontroluje to negace a pod
        return getConditioned(element) { el, stmt ->
            val expr = stmt as? GdIsEx ?: PsiTreeUtil.findChildOfType(stmt, GdIsEx::class.java)
            if (expr != null) return@getConditioned expr
            null
        }
    }

    /**
     * Looks for statements of has_method
     *  if node[.subnodes].has_method("asd"):
     */
    fun hasMethodCheck(element: PsiElement): Boolean {
        // TODO je to dost na hrubo a nekontroluje to negace a pod
        return getConditioned(element) { el, stmt ->
            val expressions = if (stmt is GdCallEx) listOf(stmt)
            else PsiTreeUtil.findChildrenOfType(stmt, GdCallEx::class.java)
            val hasMethodExpr = expressions.filter { it.expr.textMatches("has_method") }.firstOrNull()
            if (hasMethodExpr != null) {
                if (hasMethodExpr.argList?.argExprList?.firstOrNull()?.textMatches("\"${el.text}\"") == true) {
                    return@getConditioned true
                }
            }
            null
        } ?: false
    }

    private fun <T> getConditioned(element: PsiElement, action: (element: PsiElement, stmt: PsiElement?) -> T?): T? {
        val getParent = fun(stmt: PsiElement?): PsiElement? {
            return PsiTreeUtil.getParentOfType(stmt, GdIfSt::class.java, GdWhileSt::class.java, GdElifSt::class.java)
        }

        var parent = getParent(element)
        while (parent != null) {
            when (parent) {
                is GdIfSt -> {
                    val typed = action(element, parent.expr)
                    if (typed != null) return typed
                }

                is GdElifSt -> {
                    val typed = action(element, parent.expr)
                    if (typed != null) return typed
                    // To avoid matching from base condition that is not part of this suite
                    parent = PsiTreeUtil.getParentOfType(parent, GdIfSt::class.java)
                }

                is GdWhileSt -> {
                    val typed = action(element, parent.expr)
                    if (typed != null) return typed
                }
            }
            parent = getParent(parent)
        }

        return null
    }

}
