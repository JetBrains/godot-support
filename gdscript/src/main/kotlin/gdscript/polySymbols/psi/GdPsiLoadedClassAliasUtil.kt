package gdscript.polySymbols.psi

import com.intellij.psi.PsiElement
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdCallEx
import gdscript.psi.GdFile
import gdscript.utils.PsiFileUtil.toAbsoluteResource
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.unquote

/**
 * True when [this] is a `preload`/`load` call whose argument resolves to a GDScript script file.
 * A scene (`.tscn`) or a resource (`.tres`) loads to a value, never a type, so only a script's own
 * class can back a `preload`/`load`-initialized var/const used as a [GdPsiLoadedClassAliasSymbol].
 */
fun GdCallEx.loadsScriptClass(context: PsiElement): Boolean {
    if (expr.text !in setOf("preload", "load")) return false
    val resourceArg = argList?.argExprList?.firstOrNull() ?: return false
    val resource = resourceArg.text.unquote().toAbsoluteResource(context, context.project)
    return GdFileResIndex.getFiles(resource, context)
        .firstOrNull()
        ?.getPsiFile(context.project) is GdFile
}
