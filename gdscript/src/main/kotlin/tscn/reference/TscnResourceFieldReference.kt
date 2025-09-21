package tscn.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.parentOfType
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdVarNmi
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.utils.VirtualFileUtil.localPath
import tscn.psi.TscnDataLineNm
import tscn.psi.TscnParagraph
import tscn.psi.TscnResourceHeader
import tscn.psi.utils.TscnHeaderUtils
import tscn.psi.utils.TscnParagraphUtil

class TscnResourceFieldReference : PsiReferenceBase<PsiNamedElement> {

    constructor(element: PsiNamedElement) : super(element, TextRange(0, element.textLength))

    override fun resolve(): PsiElement? {
        val cache = ResolveCache.getInstance(element.project)
        return cache.resolveWithCaching(
            this,
            ResolveCache.Resolver { _, _ -> resolveScriptVariable(this.element) },
            false,
            false,
        )
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        element.setName(newElementName)
        return element
    }

    private fun resolveScriptVariable(fieldElement: PsiNamedElement): PsiNamedElement? {
        if (fieldElement !is TscnDataLineNm) return null
        val fieldName = fieldElement.name
        val resourceId = getScriptResourceIdOfContainingParagraph(fieldElement) ?: return null
        val path = getScriptPathFromResourceId(fieldElement, resourceId) ?: return null
        val classDeclaration = getTopLevelClassDeclarationFromPath(fieldElement, path) ?: return null
        val classVarDeclaration = findClassVarDeclaration(classDeclaration, fieldName)
        return classVarDeclaration
    }

    private fun getScriptResourceIdOfContainingParagraph(fieldElement: TscnDataLineNm): String? {
        val paragraph = fieldElement.parentOfType<TscnParagraph>() ?: return null
        val scriptDataLine = TscnParagraphUtil.getDataLine(paragraph, "script") ?: return null
        val scriptExprVal = scriptDataLine.dataLineValue.value.exprValue ?: return null
        if (scriptExprVal.identifierEx.text != "ExtResource") return null
        val scriptExprValArg0 = scriptExprVal.argList?.valueList?.firstOrNull() ?: return null
        return scriptExprValArg0.text.trim('"')
    }

    private fun getScriptPathFromResourceId(fieldElement: PsiNamedElement, resourceId: String): String? {
        // Find resource header with matching id, get script_class header value
        val resourceHeader = fieldElement.containingFile
            .childrenOfType<TscnParagraph>()
            .mapNotNull { it.header as? TscnResourceHeader }
            .firstOrNull { TscnHeaderUtils.getValue(it.headerValueList, "id") == resourceId }
            ?: return null

        val path = TscnHeaderUtils.getValue(resourceHeader.headerValueList, "path")

        return path.removePrefix("res://")
    }

    private fun getTopLevelClassDeclarationFromPath(fieldElement: PsiNamedElement, path: String): GdClassNameNmi? {
        val searchScope = GlobalSearchScope.projectScope(fieldElement.project)
        val candidateFiles = FileTypeIndex.getFiles(GdFileType, searchScope)
        val sourceFile = candidateFiles.firstOrNull {
            it.localPath().ifEmpty { it.path }.endsWith(path)
        } ?: return null
        val sourceGdPsi = PsiManager.getInstance(fieldElement.project).findFile(sourceFile) ?: return null
        val classDeclaration = sourceGdPsi.childrenOfType<GdClassNaming>().firstOrNull()?.classNameNmi ?: return null
        return classDeclaration
    }

    private fun findClassVarDeclaration(classDeclaration: GdClassNameNmi, fieldName: String): GdVarNmi? {
        val declarationCandidates = GdClassMemberUtil.listDeclarations(classDeclaration, fieldName)

        if (declarationCandidates.isEmpty()) return null

        for (declaration in declarationCandidates) {
            if (declaration is GdClassVarDeclTl && declaration.name == fieldName) {
                return declaration.varNmi
            }
        }

        return null
    }
}
