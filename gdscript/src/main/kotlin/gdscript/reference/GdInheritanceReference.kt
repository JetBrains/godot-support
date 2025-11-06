package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import gdscript.completion.GdLookup
import gdscript.completion.utils.GdClassCompletionUtil
import gdscript.completion.utils.GdClassCompletionUtil.lookup
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.*
import gdscript.psi.utils.GdClassUtil
import gdscript.utils.PsiFileUtil.toAbsoluteResource
import gdscript.utils.VirtualFileUtil.resourcePath
import java.nio.file.Paths

/**
 * Inheritance reference to classId
 */
class GdInheritanceReference : PsiReferenceBase<PsiElement> {

    private var key: String = ""
    private var project: Project

    constructor(element: PsiElement) : super(element, TextRange(0, element.textLength)) {
        key = element.parent.text.substring(0, element.textRangeInParent.endOffset)
        this.project = element.project

        if (isResource() && !key.startsWith("\"res://")) {
            key = key.trim('"').toAbsoluteResource(element, project)
        }
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // TODO rename resource -> rename file
        if (isResource()) return element

        when (element) {
            is GdInheritanceIdRef -> element.replace(GdElementFactory.inheritanceIdNm(element.project, newElementName))
            is GdInheritanceSubIdRef -> element.replace(GdElementFactory.inheritanceSubIdNm(element.project, newElementName))
        }

        return element
    }

    override fun resolve(): PsiElement? {
        val cache = ResolveCache.getInstance(project)
        return cache.resolveWithCaching(
            this,
            ResolveCache.Resolver { _, _ ->
                GdClassUtil.getClassIdElement(key, element, project)
            },
            false,
            false,
        )
    }

    override fun getVariants(): Array<LookupElement> {
        if (isResource()) { // At Top level "res://Asd.gd" can be used
            val myResource = element.containingFile.originalFile.virtualFile.resourcePath(true)
            val myPath = Paths.get(myResource.substring(0, myResource.lastIndexOf("/")))

            return GdFileResIndex.getNonEmptyKeys(element).flatMap fileLoop@{
                val lookUps = mutableListOf(GdLookup.create("\"$it", lookup = "\""))
                if (it.endsWith(".gd")) {
                    lookUps.add(GdLookup.create("\"$it", lookup = "\"", priority = GdLookup.USER_DEFINED))

                    val itPath = Paths.get(it)
                    val relative = myPath.relativize(itPath).toString()
                    if (relative.isNotBlank() && !relative.startsWith(".")) {
                        lookUps.add(GdLookup.create("\"$relative", lookup = "\"", priority = GdLookup.LOCAL_USER_DEFINED))
                    }
                }
                lookUps
            }.toTypedArray()
        } else if (element is GdInheritanceSubIdRef) { // While at nested position, only InnerClasses
            val classId = GdClassUtil.getClassIdElement(key.substring(0, key.lastIndexOf(".")), element)
                ?: return emptyArray()
            val container = GdClassUtil.getOwningClassElement(classId)
            val inners = PsiTreeUtil.getStubChildrenOfTypeAsList(container, GdClassDeclTl::class.java)
            if (container is GdClassDeclTl) {
                PsiTreeUtil.getStubChildrenOfTypeAsList(container.parent, GdClassDeclTl::class.java)
            }

            return inners.map { it.lookup() }.toTypedArray()
        }

        return GdClassCompletionUtil.allRootClasses(element.project)
    }

    private fun isResource(): Boolean {
        return key.startsWith('"') || key.startsWith("res://")
    }

}
