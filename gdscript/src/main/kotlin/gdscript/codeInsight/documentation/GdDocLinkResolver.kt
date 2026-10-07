package gdscript.codeInsight.documentation

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.sdk.xml.GdNameSanitizer
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParamList
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil
import gdscript.utils.PsiElementUtil.psi
import gdscript.utils.unquote

object GdDocLinkResolver {

    private val memberKinds = setOf("method", "member", "signal", "constant", "enum")
    private val ignoredKinds = setOf("theme_item")

    fun resolve(psiManager: PsiManager?, link: String, context: PsiElement): PsiElement? {
        val project = context.project
        val normalizedLink = link.unquote()
        val separator = normalizedLink.indexOf(':')
        if (separator > 0) {
            val kind = normalizedLink.substring(0, separator)
            val payload = normalizedLink.substring(separator + 1)
            when (kind) {
                "param" -> return resolveParam(payload, context)
                "annotation" -> return GdAnnotationAnchors.find(project, payload)
                in ignoredKinds -> return null
                in memberKinds -> return resolveMember(kind, payload, context)
            }
        }

        resolveType(normalizedLink, context, project)?.let { return it }
        return resolvePsiDeclaration(normalizedLink, context)
    }

    private fun resolveType(name: String, context: PsiElement, project: Project): PsiElement? {
        val typeName = name.removeArrayWrapper()
        classNameVariants(typeName).forEach { className ->
            GdPolySymbolQueriesUtil.getSdkClassSymbol(project, className)
                ?.syntheticSourceElement(project)
                ?.let { return it }
        }

        classNameVariants(typeName).forEach { className ->
            GdClassUtil.getClassIdElement(className, context, project)?.let { return it }
        }
        return null
    }

    private fun resolveMember(kind: String, payload: String, context: PsiElement): PsiElement? {
        val (ownerName, memberName) = splitMemberReference(payload)
        val owner = ownerName ?: syntheticOwnerName(context) ?: GdClassUtil.getOwningClassName(context)
        val project = context.project

        classNameVariants(owner).forEach { className ->
            val symbol = when (kind) {
                "method" -> GdPolySymbolQueriesUtil.getSdkMethodSymbol(project, className, memberName)
                "member" -> GdPolySymbolQueriesUtil.getSdkPropertySymbol(project, className, memberName)
                "signal" -> GdPolySymbolQueriesUtil.getSdkSignalSymbol(project, className, memberName)
                "constant" -> GdPolySymbolQueriesUtil.getSdkConstantSymbol(project, className, memberName)
                "enum" -> GdPolySymbolQueriesUtil.getSdkEnumSymbol(project, className, memberName)
                else -> null
            }
            symbol?.syntheticSourceElement(project)?.let { return it }
        }

        if (ownerName == null && syntheticOwnerName(context) != null) {
            resolvePsiMember(context.containingFile ?: context, memberName)?.let { return it }
        }

        classNameVariants(owner).forEach { className ->
            val ownerElement = GdClassUtil.getClassIdElement(className, context, project) ?: return@forEach
            resolvePsiMember(ownerElement, memberName)?.let { return it }
        }

        return if (ownerName == null) resolvePsiDeclaration(memberName, context) else null
    }

    /**
     * Resolves `[param name]` to the parameter of the documented function or signal.
     * The context is a `##` comment or an element inside the documented declaration.
     */
    private fun resolveParam(name: String, context: PsiElement): PsiElement? {
        val declaration = if (context is PsiComment) GdVirtualDocComment(listOf(context)).owner
        else PsiTreeUtil.getParentOfType(context, false, GdMethodDeclTl::class.java, GdSignalDeclTl::class.java)
        val params: GdParamList? = when (declaration) {
            is GdMethodDeclTl -> declaration.paramList
            is GdSignalDeclTl -> declaration.paramList
            else -> null
        }
        return params?.paramList?.firstOrNull { it.varNmi.name == name }?.varNmi
    }

    private fun resolvePsiDeclaration(name: String, context: PsiElement): PsiElement? {
        GdClassMemberUtil.listDeclarations(context, name).firstOrNull()?.psi()?.let { declaration ->
            GdClassMemberUtil.identifierOf(declaration)?.let { return it }
        }
        return null
    }

    private fun resolvePsiMember(owner: PsiElement, name: String): PsiElement? {
        GdClassMemberUtil.listClassMemberDeclarations(owner, static = null, search = name, constructors = true)
            .firstOrNull()
            ?.let { GdClassMemberUtil.identifierOf(it) }
            ?.let { return it }

        GdClassMemberUtil.listDeclarations(owner, name, ignoreGlobalScope = true).firstOrNull()?.psi()?.let { declaration ->
            GdClassMemberUtil.identifierOf(declaration)?.let { return it }
        }
        return null
    }

    private fun syntheticOwnerName(context: PsiElement): String? {
        return context.containingFile?.getUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY)
    }

    private fun splitMemberReference(reference: String): Pair<String?, String> {
        val separator = reference.lastIndexOf('.')
        if (separator < 0) return null to reference
        return reference.substring(0, separator) to reference.substring(separator + 1)
    }

    private fun classNameVariants(name: String): Sequence<String> = sequence {
        yield(name)
        val sanitized = GdNameSanitizer.sanitizeClassName(name)
        if (sanitized != name) yield(sanitized)
        if (name.startsWith("_")) yield("@${name.substring(1)}")
    }.distinct()

    private fun String.removeArrayWrapper(): String {
        return if (startsWith("Array[") && endsWith("]")) substring(6, length - 1) else this
    }
}
