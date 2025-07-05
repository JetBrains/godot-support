package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdIcon
import gdscript.GdKeywords
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
class GdTypeHintReference : PsiReferenceBase<GdTypeHintRef> {

    private var key: String = ""
    private var project: Project

    constructor(element: GdTypeHintRef) : super(element, TextRange(0, element.textLength)) {
        key = element.parent.text.substring(0, element.textRangeInParent.endOffset)
        this.project = element.project
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        return element.replace(GdElementFactory.typeHintRef(element.project, newElementName))
    }

    override fun resolve(): PsiElement? {
        val cache = ResolveCache.getInstance(project)
        return cache.resolveWithCaching(
            this,
            ResolveCache.Resolver { _, _ ->
                val classId = GdClassNamingIndex.INSTANCE.getGlobally(key, project).firstOrNull()?.classNameNmi
                if (classId != null) return@Resolver classId

                val container = if (key.contains(".")) {
                    val ownerClassId = getOwnerClass() ?: return@Resolver null
                    GdClassUtil.getOwningClassElement(ownerClassId)
                } else {
                    GdClassUtil.getOwningClassElement(element)
                }

                resolveInner(container)?.let { return@Resolver it }
                if (container is GdClassDeclTl) resolveInner(container.parent)?.let { return@Resolver it }

                GdClassUtil.getClassIdElement(GdKeywords.GLOBAL_SCOPE, project)?.let {
                    return@Resolver resolveInner(GdClassUtil.getOwningClassElement(it))
                }
            },
            false,
            false,
        )
    }

    override fun getVariants(): Array<LookupElement> {
        val variants = mutableListOf<LookupElement>()
        val container: PsiElement?

        if (key.contains(".")) {
            val classId = getOwnerClass() ?: return emptyArray()
            container = GdClassUtil.getOwningClassElement(classId)
        } else {
            container = GdClassUtil.getOwningClassElement(element)
            variants.addAll(GdClassCompletionUtil.allRootClasses(project))
            PsiTreeUtil.getParentOfType(element, GdMethodDeclTl::class.java)?.let { methodDecl ->
                PsiTreeUtil.findChildOfType(methodDecl, GdSuite::class.java)?.let { suite ->
                    variants.addAll(loadedClasses(suite).map {
                        GdLookup.create(
                            GdCommonUtil.getName(it),
                            priority = GdLookup.LOCAL_USER_DEFINED,
                            icon = GdScriptPluginIcons.GDScriptIcons.OBJECT,
                        )
                    })
                }
            }
            ProjectAutoloadUtil.listGlobals(project).forEach {
                variants.add(GdLookup.create(
                    it.key,
                    priority = GdLookup.USER_DEFINED,
                    icon = GdScriptPluginIcons.GDScriptIcons.OBJECT,
                ))
            }
        }

        variantsInner(container, variants)
        if (container is GdClassDeclTl) variantsInner(container.parent, variants)

        return variants.toTypedArray()
    }

    private fun resolveInner(container: PsiElement): PsiElement? {
        val myName = element.text
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

    private fun variantsInner(container: PsiElement, variants: MutableList<LookupElement>) {
        variants.addAll(innerClasses(container).map { it.lookup() })
        variants.addAll(enums(container).mapNotNull { it.lookup() })
        variants.addAll(loadedClasses(container).map {
            GdLookup.create(
                GdCommonUtil.getName(it),
                priority = GdLookup.USER_DEFINED,
                icon = GdScriptPluginIcons.GDScriptIcons.OBJECT,
            )
        })
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
