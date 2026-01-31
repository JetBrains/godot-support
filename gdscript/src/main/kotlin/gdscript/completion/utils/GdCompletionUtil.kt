package gdscript.completion.utils

import GdScriptPluginIcons
import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.completion.utils.GdClassCompletionUtil.lookup
import gdscript.psi.GdBindingPattern
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue
import gdscript.psi.GdForSt
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParam
import gdscript.psi.GdSetDecl
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.GdTyped
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVarNmi
import gdscript.psi.utils.PsiGdExprUtil
import gdscript.utils.StringUtil.parseFromSquare
import project.psi.model.GdAutoload

/**
 * Deprecated aggregator for building completion lookup elements.
 *
 * Use dedicated utils instead, depending on the element type:
 * - Classes: GdClassCompletionUtil.lookup(GdClassDeclTl) or GdClassCompletionUtil.allRootClasses(Project)
 * - Methods: GdMethodCompletionUtil.buildParamHint(...) with GdLookup.create(...)
 * - Enums/Enum values: GdEnumCompletionUtil.lookup(...)
 * - Class variables: GdClassVarCompletionUtil or build via GdLookup.create(...)
 * - Parameters/locals/signals: build via specific utils or GdLookup.create(...)
 *
 * If you previously used lookups(any), replace the call site with a
 * when(element) { ... } that dispatches to the specific util above.
 */
@Deprecated(
    message = "Use dedicated utils (GdClassCompletionUtil/GdMethodCompletionUtil/GdEnumCompletionUtil/...) instead of this aggregator.",
    replaceWith = ReplaceWith("when(element){ is GdClassDeclTl -> element.lookup() else -> GdLookup.create(\"\") }")
)
object GdCompletionUtil {

    fun lookups(element: Any, isCallable: Boolean = false): Array<LookupElement> {
        return when (element) {
            is GdClassDeclTl -> arrayOf(element.lookup())
            is GdClassNaming -> arrayOf(lookup(element))
            is GdClassVarDeclTl -> arrayOf(lookup(element))
            is GdVarDeclSt -> arrayOf(lookup(element))
            is GdConstDeclTl -> arrayOf(lookup(element))
            is GdConstDeclSt -> arrayOf(lookup(element))
            is GdEnumDeclTl -> lookup(element)
            is GdEnumValue -> arrayOf(GdEnumCompletionUtil.lookup(element))
            is GdMethodDeclTl -> arrayOf(lookup(element, isCallable))
            is GdForSt -> arrayOf(lookup(element))
            is GdParam -> arrayOf(lookup(element))
            is GdSetDecl -> arrayOf(lookup(element))
            is GdBindingPattern -> arrayOf(lookup(element))
            is GdSignalDeclTl -> arrayOf(lookup(element))
            is GdVarNmi -> arrayOf(lookup(element))
            is GdAutoload -> arrayOf(lookup(element))
            else -> emptyArray()
        }
    }

    fun lookup(className: GdClassNaming): LookupElement =
        GdLookup.create(
            className.classname,
            priority = GdLookup.USER_DEFINED,
            icon = GdIcon.getEditorIcon(className.classname),
        )

    fun lookup(variable: GdClassVarDeclTl): LookupElement =
        GdLookup.create(
            variable.name,
            icon = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER,
            typed = variable.returnType,
            priority = GdLookup.USER_DEFINED,
        )

    fun lookup(variable: GdVarDeclSt): LookupElement =
        GdLookup.create(
            variable.name,
            icon = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER,
            typed = variable.returnType,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(constant: GdConstDeclTl): LookupElement =
        GdLookup.create(
            constant.name,
            icon = GdScriptPluginIcons.GDScriptIcons.CONST_MARKER,
            typed = constant.returnType,
            priority = GdLookup.USER_DEFINED,
        )

    fun lookup(constant: GdConstDeclSt): LookupElement =
        GdLookup.create(
            constant.name,
            icon = GdScriptPluginIcons.GDScriptIcons.CONST_MARKER,
            typed = constant.returnType,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(enum: GdEnumDeclTl): Array<LookupElement> {
        val name = enum.enumDeclNmi

        return if (name != null) {
            arrayOf(GdEnumCompletionUtil.lookup(
                name.name,
            ))
        } else {
            enum.values.map {
                GdEnumCompletionUtil.lookup(it.key, it.value)
            }.toTypedArray()
        }
    }

    fun lookup(method: GdMethodDeclTl, isCallable: Boolean = false): LookupElement {
        return GdLookup.create(
            method.name,
            lookup = "${if (isCallable) "" else "()"}${if (method.paramList?.paramList?.isNotEmpty() == true || method.isVariadic) "_" else ""}",
            presentable = method.name,
            typed = method.returnType,
            icon = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER,
            priority = GdLookup.USER_DEFINED,
            tail = GdMethodCompletionUtil.buildParamHint(method),
        )
    }

    fun lookup(loop: GdForSt): LookupElement =
        GdLookup.create(
            loop.varNmi?.name ?: "",
            icon = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER,
            typed = loop.expr?.returnType?.parseFromSquare() ?: "",
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(param: GdParam): LookupElement =
        GdLookup.create(
            param.varNmi.name,
            typed = param.returnType,
            icon = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(variable: GdVarNmi): LookupElement {
        var typed: GdTyped? = null
        val next = variable.nextSibling
        if (next is GdTyped) {
            typed = next
        }

        return GdLookup.create(
            variable.name,
            typed = PsiGdExprUtil.fromTyped(typed),
            icon = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )
    }

    fun lookup(decl: GdSetDecl): LookupElement =
        GdLookup.create(
            decl.varNmi?.name ?: "",
            typed = decl.typed?.text ?: "",
            icon = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(binding: GdBindingPattern): LookupElement {
        return GdLookup.create(
            binding.varNmi.name,
//            typed = binding.typed?.text ?: "", TODO dá se v match zjistit typ?
            icon = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )
    }

    fun lookup(signal: GdSignalDeclTl): LookupElement {
        return GdLookup.create(
            signal.signalIdNmi?.name ?: "",
            typed = "signal",
            icon = GdScriptPluginIcons.GDScriptIcons.SIGNAL_MARKER,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )
    }

    fun lookup(file: GdAutoload): LookupElement =
        GdLookup.create(
            file.key,
            priority = GdLookup.REMOTE_DEFINED,
            icon = GdScriptPluginIcons.GDScriptIcons.OBJECT,
        )

}
