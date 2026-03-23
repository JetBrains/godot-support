package gdscript.polySymbols.psi

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.scope.GdSdkGlobalPolySymbolScope
import gdscript.psi.GdExpr
import gdscript.psi.GdFile
import gdscript.psi.GdPsiUtils
import gdscript.psi.GdRefIdRef
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil

object GdPsiPolySymbolUtil {

    fun getOwnerClassId(source: PsiElement): String {
        val ownerElement = getOwnerClassElement(source)
        return when (ownerElement) {
            is GdFile -> GdClassUtil.getOwningClassName(ownerElement)
            else -> GdClassUtil.getFullClassId(ownerElement)
        }
    }

    /**
     * Returns one of: [gdscript.psi.GdClassDeclTl], [GdFile].
     */
    fun getOwnerClassElement(source: PsiElement): PsiElement {
        return GdClassUtil.getOwningClassElement(source)
    }

    fun getClassId(source: PsiElement): String {
        return GdClassUtil.getFullClassId(source)
    }

    /**
     * Extracts the leaf (simple) name from a possibly-dotted [classId].
     */
    fun getLeafName(classId: String): String {
        if (classId.startsWith("\"")) {
            val closingQuote = classId.indexOf('"', 1)
            if (closingQuote >= 0 && closingQuote < classId.length - 1) {
                // There is content after the closing quote, e.g. ".Outer.Inner"
                val suffix = classId.substring(closingQuote + 1)
                val lastDot = suffix.lastIndexOf('.')
                return if (lastDot >= 0) suffix.substring(lastDot + 1) else classId
            }
            return classId
        }
        val dot = classId.lastIndexOf('.')
        return if (dot >= 0) classId.substring(dot + 1) else classId
    }


    fun isStatic(element: GdRefIdRef): Boolean {
        val qualifier = GdClassMemberUtil.calledUpon(element) ?: return false
        val typeName = GdPsiUtils.getReturnType(qualifier)
        if (typeName.isEmpty()) return false
        return isStaticAccessByName(element, qualifier, typeName)
    }


    /**
     * @GlobalScope has matching variables with classes
     *
     * Mirrors [gdscript.psi.utils.GdClassMemberUtil.isStaticAccessByName]
     */
    fun isStaticAccessByName(element: PsiElement, qualifier: GdExpr, typeName: String): Boolean {
        // We consider it a static class access when:
        // - The resolved type name equals the qualifier text (e.g., 'Outer'), OR
        // - The resolved type name equals 'FullOwnerId.QualifierText' to handle nested or file-qualified contexts.
        // And we additionally ensure there is no conflicting global variable with the same name in @GlobalScope.
        val qualifierText = qualifier.text
        val fullOwnerId = getClassId(qualifier)
        val looksLikeClassName = (typeName == qualifierText) || (typeName == "$fullOwnerId.$qualifierText")
        return looksLikeClassName && checkGlobalStaticMatch(element.project, typeName)
    }

    /**
     * Returns true if there is no global variable with the same name as the provided type name.
     */
    private fun checkGlobalStaticMatch(project: Project, name: String): Boolean {
        val executor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScope(GdSdkGlobalPolySymbolScope(project))
        }
        return GdPolySymbolQueriesUtil.getSdkPropertySymbol(executor, GdKeywords.GLOBAL_SCOPE, name) == null
    }
}
