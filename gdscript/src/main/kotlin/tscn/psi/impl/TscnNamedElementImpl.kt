package tscn.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistryImpl
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.parentOfType
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.polySymbols.psi.GdPsiPropertySymbol
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil
import gdscript.utils.VirtualFileUtil.localPath
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Unmodifiable
import tscn.psi.TscnDataLineNm
import tscn.psi.TscnHeaderValue
import tscn.psi.TscnHeaderValueNm
import tscn.psi.TscnHeaderValueVal
import tscn.psi.TscnNamedElement
import tscn.psi.TscnParagraph
import tscn.psi.TscnResourceHeader
import tscn.psi.utils.TscnHeaderUtils
import tscn.psi.utils.TscnParagraphUtil

private const val SCRIPT_CLASS_KEY = "script_class"

abstract class TscnNamedElementImpl(node: @NotNull ASTNode) : ASTWrapperPsiElement(node), TscnNamedElement {

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistryImpl.getReferencesFromProviders(this)
    }

    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> = when (this) {
        is TscnHeaderValueVal -> scriptClassOwnReferences(this)
        is TscnDataLineNm -> resourceFieldOwnReferences(this)
        else -> emptyList()
    }

}

/**
 * `script_class="MyClass"` header values resolved as a PolySymbol reference to the GDScript class -
 * see [tscn.psi.TscnHeaderValueVal] pattern gate mirrored from the pre-existing classic contributor
 * this replaces. This element is also the (unrelated) host for plain `res://` resource paths, which
 * are left on the classic `tscn.reference.TscnResourceReference` mechanism - see the pattern check below.
 */
private fun scriptClassOwnReferences(element: TscnHeaderValueVal): List<PsiSymbolReference> {
    val parent = element.parent as? TscnHeaderValue ?: return emptyList()
    val key = PsiTreeUtil.getChildOfType(parent, TscnHeaderValueNm::class.java)?.text
    if (key != SCRIPT_CLASS_KEY) return emptyList()

    val text = element.text
    val className = text.trim('"')
    // Resolved eagerly, not inside reference()'s lazy resolver: a reference is only registered when
    // resolution actually succeeds, so an unresolved script_class value stays silent (matching the
    // classic reference it replaces) instead of surfacing a PolySymbolHighlightingAnnotator
    // "Unrecognized name" warning for every plain/unresolved value.
    val symbol = GdClassUtil.getClassIdElement(className, element, element.project)
        ?.let { GdPsiClassSymbolFactory.create(it) }
        ?.takeIf { it.name == className }
        ?: return emptyList()

    val range =
        if (text.length >= 2 && text.startsWith('"') && text.endsWith('"')) TextRange(1, text.length - 1)
        else TextRange(0, text.length)

    return polySymbolOwnReferences(element) {
        reference(range, GdPolySymbolKind.CLASS) { listOf(symbol) }
    }
}

/**
 * `.tres` data line keys resolved as a PolySymbol reference to the matching `@export var` in the
 * script referenced by the containing `ExtResource` - the resolve walk is copied from the classic
 * `tscn.reference.TscnResourceFieldReference` this replaces.
 */
private fun resourceFieldOwnReferences(element: TscnDataLineNm): List<PsiSymbolReference> {
    // Resolved eagerly for the same reason as scriptClassOwnReferences: most data line keys are
    // built-in resource properties (resource_name, script, ...), not @export var fields, and must
    // stay silent rather than register a reference that will fail to resolve.
    val symbol = resolveScriptVariable(element, element.name) ?: return emptyList()

    return polySymbolOwnReferences(element) {
        reference(TextRange(0, element.textLength), GdPolySymbolKind.PROPERTY) { listOf(symbol) }
    }
}

private fun resolveScriptVariable(fieldElement: TscnDataLineNm, fieldName: String): PolySymbol? {
    val resourceId = getScriptResourceIdOfContainingParagraph(fieldElement) ?: return null
    val path = getScriptPathFromResourceId(fieldElement, resourceId) ?: return null
    val classDeclaration = getTopLevelClassDeclarationFromPath(fieldElement, path) ?: return null
    val varNmi = GdClassMemberUtil.listDeclarations(classDeclaration, fieldName)
        .filterIsInstance<GdClassVarDeclTl>()
        .firstOrNull { it.getName() == fieldName }
        ?.varNmi ?: return null
    return GdPsiPropertySymbol(varNmi)
}

private fun getScriptResourceIdOfContainingParagraph(fieldElement: TscnDataLineNm): String? {
    val paragraph = fieldElement.parentOfType<TscnParagraph>() ?: return null
    val scriptDataLine = TscnParagraphUtil.getDataLine(paragraph, "script") ?: return null
    val scriptExprVal = scriptDataLine.dataLineValue.value.exprValue ?: return null
    if (scriptExprVal.identifierEx.text != "ExtResource") return null
    val scriptExprValArg0 = scriptExprVal.argList?.valueList?.firstOrNull() ?: return null
    return scriptExprValArg0.text.trim('"')
}

private fun getScriptPathFromResourceId(fieldElement: TscnDataLineNm, resourceId: String): String? {
    val resourceHeader = fieldElement.containingFile
        .childrenOfType<TscnParagraph>()
        .mapNotNull { it.header as? TscnResourceHeader }
        .firstOrNull { TscnHeaderUtils.getValue(it.headerValueList, "id") == resourceId }
        ?: return null

    val path = TscnHeaderUtils.getValue(resourceHeader.headerValueList, "path")
    return path.removePrefix("res://")
}

private fun getTopLevelClassDeclarationFromPath(fieldElement: TscnDataLineNm, path: String): GdClassNameNmi? {
    val searchScope = GlobalSearchScope.projectScope(fieldElement.project)
    val candidateFiles = FileTypeIndex.getFiles(GdFileType, searchScope)
    val sourceFile = candidateFiles.firstOrNull {
        it.localPath().ifEmpty { it.path }.endsWith(path)
    } ?: return null
    val sourceGdPsi = fieldElement.manager.findFile(sourceFile) ?: return null
    return sourceGdPsi.childrenOfType<GdClassNaming>().firstOrNull()?.classNameNmi
}
