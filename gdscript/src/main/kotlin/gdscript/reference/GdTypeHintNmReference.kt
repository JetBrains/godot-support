package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.completion.utils.GdClassCompletionUtil
import gdscript.completion.utils.GdClassCompletionUtil.lookup
import gdscript.completion.utils.GdEnumCompletionUtil.lookup
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.GdCommonUtil
import gdscript.psi.utils.GdInheritanceUtil
import project.psi.util.ProjectAutoloadUtil
import kotlin.reflect.KClass

/**
 * ReturnType reference to ClassId & EnumDecl
 */
class GdTypeHintNmReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = ""

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        key = element.parent.text.substring(0, element.textRangeInParent.endOffset)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        element.setName(newElementName)
        return element
    }

    override fun resolve(): PsiElement? {
        val classId = GdClassNamingIndex.INSTANCE.getGlobally(key, element).firstOrNull()?.classNameNmi
        if (classId != null) return classId

        val container = if (key.contains(".")) {
            val ownerClassId = getOwnerClass() ?: return null
            GdClassUtil.getOwningClassElement(ownerClassId)
        } else {
            GdClassUtil.getOwningClassElement(element)
        }

        val myName = element.name
        enums(container).forEach {
            if (it.name == myName) return it.enumDeclNmi
        }
        innerClasses(container).forEach {
            if (it.name == myName) return it.classNameNmi
        }
        loadedClasses(container).forEach {
            if (GdCommonUtil.getName(it) == myName) return GdClassMemberReference.resolveId(it)
        }
        PsiTreeUtil.getParentOfType(element, GdMethodDeclTl::class.java)?.let { methodDecl ->
            PsiTreeUtil.findChildOfType(methodDecl, GdSuite::class.java)?.let { suite ->
                loadedClasses(suite).forEach {
                    if (GdCommonUtil.getName(it) == myName) return GdClassMemberReference.resolveId(it)
                }
            }
        }

        return null
    }

    override fun getVariants(): Array<LookupElement> {
        val variants = mutableListOf<LookupElement>()
        val container: PsiElement?

        if (key.contains(".")) {
            val classId = getOwnerClass() ?: return emptyArray()
            container = GdClassUtil.getOwningClassElement(classId)
        } else {
            container = GdClassUtil.getOwningClassElement(element)
            variants.addAll(GdClassCompletionUtil.allRootClasses(element.project))
            PsiTreeUtil.getParentOfType(element, GdMethodDeclTl::class.java)?.let { methodDecl ->
                PsiTreeUtil.findChildOfType(methodDecl, GdSuite::class.java)?.let { suite ->
                    variants.addAll(loadedClasses(suite).map {
                        GdLookup.create(
                            GdCommonUtil.getName(it),
                            priority = GdLookup.LOCAL_USER_DEFINED,
                            icon = GdIcon.getEditorIcon(GdIcon.OBJECT),
                        )
                    })
                }
            }
            ProjectAutoloadUtil.listGlobals(element.project).forEach {
                variants.add(GdLookup.create(
                    it.key,
                    priority = GdLookup.USER_DEFINED,
                    icon = GdIcon.getEditorIcon(GdIcon.OBJECT),
                ))
            }
        }

        variants.addAll(innerClasses(container).map { it.lookup() })
        variants.addAll(enums(container).mapNotNull { it.lookup() })
        variants.addAll(loadedClasses(container).map {
            GdLookup.create(
                GdCommonUtil.getName(it),
                priority = GdLookup.USER_DEFINED,
                icon = GdIcon.getEditorIcon(GdIcon.OBJECT),
            )
        })

        return variants.toTypedArray()
    }

    private fun getOwnerClass(): PsiElement? {
        val withoutLast = key.substring(0, key.lastIndexOf("."))
        val global = GdClassUtil.getClassIdElement(withoutLast, element)
        if (global != null) return global

        val myId = GdClassUtil.getFullClassId(element)
        GdClassUtil.getClassIdElement("${myId}.${withoutLast}", element)?.let { return it }

        ProjectAutoloadUtil.findFromAlias(withoutLast, element)?.let { return it }

        return null
    }

    private fun enums(ownerClass: PsiElement): List<GdEnumDeclTl> {
        return findRecursiveOfType(ownerClass, GdEnumDeclTl::class)
    }

    private fun innerClasses(ownerClass: PsiElement): List<GdClassDeclTl> {
        return findRecursiveOfType(ownerClass, GdClassDeclTl::class)
    }

    private fun loadedClasses(ownerElement: PsiElement): List<PsiElement> {
        val list = mutableListOf<PsiElement>()

        PsiTreeUtil.getChildrenOfAnyType(
            ownerElement,
            GdClassVarDeclTl::class.java,
            GdConstDeclTl::class.java,
            GdVarDeclSt::class.java,
            GdConstDeclSt::class.java,
        ).forEach {
            val expr = when (it) {
                is GdClassVarDeclTl -> it.expr
                is GdConstDeclTl -> it.expr
                is GdVarDeclSt -> it.expr
                is GdConstDeclSt -> it.expr
                else -> return list
            }
            if (expr is GdCallEx && arrayOf("preload", "load").contains(expr.expr.text)) {
                list.add(it)
            }
        }

        return list
    }

    private fun <T : PsiElement> findRecursiveOfType(ownerClass: PsiElement, kClass: KClass<T>): List<T> {
        val results = mutableListOf<T>()
        var par: PsiElement? = ownerClass
        while (par != null) {
            results.addAll(PsiTreeUtil.getStubChildrenOfTypeAsList(par, kClass.java))
            par = GdInheritanceUtil.getExtendedElement(par)
        }
        return results
    }
}
