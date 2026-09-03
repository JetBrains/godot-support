package tscn.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistryImpl
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.parentOfType
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import gdscript.index.impl.GdFileResIndex
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.polySymbols.psi.GdPsiMethodSymbol
import gdscript.polySymbols.psi.GdPsiPropertySymbol
import gdscript.polySymbols.psi.GdPsiSignalSymbol
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdFile
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil
import gdscript.utils.VirtualFileUtil.localPath
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Unmodifiable
import tscn.psi.TscnConnectionHeader
import tscn.psi.TscnDataLine
import tscn.psi.TscnDataLineNm
import tscn.psi.TscnHeaderValue
import tscn.psi.TscnHeaderValueNm
import tscn.psi.TscnHeaderValueVal
import tscn.psi.TscnJsonPair
import tscn.psi.TscnJsonValue
import tscn.psi.TscnNamedElement
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnParagraph
import tscn.psi.TscnResourceHeader
import tscn.psi.TscnTypes
import tscn.psi.utils.TscnHeaderUtils
import tscn.psi.utils.TscnNodeUtil
import tscn.psi.utils.TscnParagraphUtil

private const val SCRIPT_CLASS_KEY = "script_class"

/** The key of the method of an animation track key, and the value of the `type` of such a track. */
private const val METHOD_TRACK_KEY = "\"method\""

private val NODE_PATH = Regex("""NodePath\("(.*)"\)""")

abstract class TscnNamedElementImpl(node: @NotNull ASTNode) : ASTWrapperPsiElement(node), TscnNamedElement {

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistryImpl.getReferencesFromProviders(this)
    }

    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> = when (this) {
        is TscnHeaderValueVal -> headerValueOwnReferences(this)
        is TscnDataLineNm -> resourceFieldOwnReferences(this)
        is TscnJsonValue -> animationTrackMethodOwnReferences(this)
        else -> emptyList()
    }

}

/**
 * Dispatches a header value to the resolve walk of its key - see [scriptClassOwnReferences] and
 * [connectionOwnReferences]. A key with no walk keeps the classic mechanism, for example the plain
 * `res://` path on `tscn.reference.TscnResourceReference`.
 */
private fun headerValueOwnReferences(element: TscnHeaderValueVal): List<PsiSymbolReference> {
    val parent = element.parent as? TscnHeaderValue ?: return emptyList()
    return when (PsiTreeUtil.getChildOfType(parent, TscnHeaderValueNm::class.java)?.text) {
        SCRIPT_CLASS_KEY -> scriptClassOwnReferences(element)
        TscnHeaderUtils.HL_SIGNAL -> connectionOwnReferences(element, signal = true)
        TscnHeaderUtils.HL_METHOD -> connectionOwnReferences(element, signal = false)
        else -> emptyList()
    }
}

/**
 * `script_class="MyClass"` header values resolved as a PolySymbol reference to the GDScript class -
 * the key gate in [headerValueOwnReferences] mirrors the pre-existing classic contributor this
 * replaces. This element is also the (unrelated) host for plain `res://` resource paths, which
 * are left on the classic `tscn.reference.TscnResourceReference` mechanism.
 */
private fun scriptClassOwnReferences(element: TscnHeaderValueVal): List<PsiSymbolReference> {
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

    return polySymbolOwnReferences(element) {
        reference(unquotedRange(text), GdPolySymbolKind.CLASS) { listOf(symbol) }
    }
}

/**
 * `[connection signal="my_signal" from="." to="." method="_on_my_signal"]` values resolved as
 * PolySymbol references to the GDScript declarations. The signal belongs to the script of the
 * `from` node, and the method belongs to the script of the `to` node.
 */
private fun connectionOwnReferences(element: TscnHeaderValueVal, signal: Boolean): List<PsiSymbolReference> {
    val header = element.parentOfType<TscnConnectionHeader>() ?: return emptyList()
    val text = element.text
    val name = text.trim('"')
    val scriptFile = resolveNodeScript(element, if (signal) header.from else header.to) ?: return emptyList()

    // Resolved eagerly for the same reason as scriptClassOwnReferences: an engine signal, for
    // example "pressed", is declared by no script in the project and must stay silent.
    val declaration = GdClassMemberUtil.listDeclarations(scriptFile, name)
        .firstOrNull {
            if (signal) it is GdSignalDeclTl && it.getName() == name
            else it is GdMethodDeclTl && it.getName() == name
        } ?: return emptyList()

    val symbol = when (declaration) {
        is GdSignalDeclTl -> declaration.signalIdNmi?.let { GdPsiSignalSymbol(it) }
        is GdMethodDeclTl -> declaration.methodIdNmi?.let { GdPsiMethodSymbol(it) }
        else -> null
    } ?: return emptyList()

    val kind = if (signal) GdPolySymbolKind.SIGNAL else GdPolySymbolKind.METHOD

    return polySymbolOwnReferences(element) {
        reference(unquotedRange(text), kind) { listOf(symbol) }
    }
}

/**
 * `"method": &"explode"` inside the `keys` of an animation method track, resolved as a PolySymbol
 * reference to the GDScript method:
 *
 * ```
 * tracks/1/type = "method"
 * tracks/1/path = NodePath(".")
 * tracks/1/keys = {
 * "values": [{ "args": [], "method": &"explode" }]
 * }
 * ```
 *
 * The `path` of the track names the node that runs the method, so the walk ends in the script of
 * that node. This replaces the text scan of `tscn.psi.search.AbstractTscnSearcher` for Find Usages
 * and adds a rename that keeps the track in step.
 */
private fun animationTrackMethodOwnReferences(element: TscnJsonValue): List<PsiSymbolReference> {
    if (element.node.firstChildNode?.elementType != TscnTypes.STRING_REF) return emptyList()

    val text = element.text
    if (!text.startsWith("&\"")) return emptyList()

    val pair = element.parent as? TscnJsonPair ?: return emptyList()
    val values = pair.jsonValueList
    if (values.size != 2 || values[1] !== element || values[0].text != METHOD_TRACK_KEY) return emptyList()

    val nodePath = methodTrackNodePath(element) ?: return emptyList()
    val scriptFile = resolveNodeScript(element, nodePath) ?: return emptyList()

    val name = text.removePrefix("&").trim('"')
    // Resolved eagerly for the same reason as scriptClassOwnReferences: a track that calls an engine
    // method, for example "queue_free", must stay silent.
    val symbol = GdClassMemberUtil.listDeclarations(scriptFile, name)
        .filterIsInstance<GdMethodDeclTl>()
        .firstOrNull { it.getName() == name }
        ?.methodIdNmi
        ?.let { GdPsiMethodSymbol(it) }
        ?: return emptyList()

    return polySymbolOwnReferences(element) {
        reference(TextRange(2, text.length - 1), GdPolySymbolKind.METHOD) { listOf(symbol) }
    }
}

/**
 * The node path of the track that holds [element], or `null` when the track is not a method track.
 * A track spreads over one data line per field, so the walk reads the `type` and the `path` line of
 * the same track prefix.
 */
private fun methodTrackNodePath(element: TscnJsonValue): String? {
    val keysLine = element.parentOfType<TscnDataLine>() ?: return null
    val header = keysLine.dataLineHeader.text
    if (!header.endsWith("/keys")) return null
    val track = header.removeSuffix("/keys")

    val paragraph = keysLine.parentOfType<TscnParagraph>() ?: return null
    if (trackField(paragraph, "$track/type") != METHOD_TRACK_KEY) return null

    val path = trackField(paragraph, "$track/path") ?: return null
    return NODE_PATH.matchEntire(path)?.groupValues?.get(1)
}

private fun trackField(paragraph: TscnParagraph, key: String): String? =
    TscnParagraphUtil.getDataLine(paragraph, key)?.dataLineValue?.text?.trim(' ', '\n')

/** The script attached to the node that [nodePath] points to, in the scene of [element]. */
private fun resolveNodeScript(element: PsiElement, nodePath: String): GdFile? {
    if (nodePath.isEmpty()) return null
    val nodeHeaders = element.containingFile.descendantsOfType<TscnNodeHeader>()
    val node = TscnNodeUtil.findNode(nodeHeaders, nodePath) ?: return null
    val resource = nodeScriptResource(element, node) ?: return null
    val scriptFile = GdFileResIndex.getFiles(resource, element.project).firstOrNull() ?: return null
    return element.manager.findFile(scriptFile) as? GdFile
}

/**
 * The script resource of [node]. A node that instances another scene carries no script of its own,
 * so the script of the root node of that scene applies.
 */
private fun nodeScriptResource(element: PsiElement, node: TscnNodeHeader): String? {
    node.scriptResource.takeIf { it.isNotBlank() }?.let { return it }

    val instance = node.instanceResource.takeIf { it.isNotBlank() } ?: return null
    val sceneFile = GdFileResIndex.getFiles(instance, element.project).firstOrNull() ?: return null
    val scene = element.manager.findFile(sceneFile) ?: return null
    val root = scene.descendantsOfType<TscnNodeHeader>().firstOrNull { it.parentPath.isEmpty() } ?: return null
    return root.scriptResource.takeIf { it.isNotBlank() }
}

/** The range of a header value without the quotes around it. */
private fun unquotedRange(text: String): TextRange =
    if (text.length >= 2 && text.startsWith('"') && text.endsWith('"')) TextRange(1, text.length - 1)
    else TextRange(0, text.length)

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
