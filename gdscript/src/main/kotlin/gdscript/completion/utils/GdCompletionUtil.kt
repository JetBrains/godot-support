package gdscript.completion.utils

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.*
import gdscript.psi.utils.PsiGdExprUtil
import gdscript.utils.StringUtil.parseFromSquare

@Deprecated("move into assigned methods")
object GdCompletionUtil {

    fun lookups(element: PsiElement): Array<LookupElement> {
        return when (element) { // TODO je tu vše? musí sedět vůši GdClassMemberUtil.listMembers
            is GdClassNaming -> arrayOf(lookup(element));
            is GdClassVarDeclTl -> arrayOf(lookup(element));
            is GdVarDeclSt -> arrayOf(lookup(element));
            is GdConstDeclTl -> arrayOf(lookup(element));
            is GdConstDeclSt -> arrayOf(lookup(element));
            is GdEnumDeclTl -> lookup(element);
            is GdEnumValue -> arrayOf(GdEnumCompletionUtil.lookup(element));
            is GdKeyValue -> arrayOf(lookup(element));
            is GdMethodDeclTl -> arrayOf(lookup(element));
            is GdForSt -> arrayOf(lookup(element));
            is GdParam -> arrayOf(lookup(element));
            is GdSetDecl -> arrayOf(lookup(element));
            is GdBindingPattern -> arrayOf(lookup(element));
            is GdSignalDeclTl -> arrayOf(lookup(element));
            else -> emptyArray();
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
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            typed = variable.returnType,
            priority = GdLookup.USER_DEFINED,
        )

    fun lookup(variable: GdVarDeclSt): LookupElement =
        GdLookup.create(
            variable.name,
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            typed = variable.returnType,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(constant: GdConstDeclTl): LookupElement =
        GdLookup.create(
            constant.name,
            icon = GdIcon.getEditorIcon(GdIcon.CONST_MARKER),
            typed = constant.returnType,
            priority = GdLookup.USER_DEFINED,
        )

    fun lookup(constant: GdConstDeclSt): LookupElement =
        GdLookup.create(
            constant.name,
            icon = GdIcon.getEditorIcon(GdIcon.CONST_MARKER),
            typed = constant.returnType,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(enum: GdEnumDeclTl): Array<LookupElement> {
        val name = enum.enumDeclNmi;

        return if (name != null) {
            arrayOf(GdEnumCompletionUtil.lookup(
                name.name,
            ));
        } else {
            enum.values.map {
                GdEnumCompletionUtil.lookup(it.key, it.value)
            }.toTypedArray();
        }
    }

    fun lookup(method: GdMethodDeclTl): LookupElement {
        return GdLookup.create(
            method.name.orEmpty(),
            lookup = "()${if (method.paramList?.paramList?.isNotEmpty() == true) "_" else ""}",
            presentable = method.name.orEmpty(),
            typed = method.returnType,
            icon = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER),
            priority = GdLookup.USER_DEFINED,
            tail = GdMethodCompletionUtil.buildParamHint(method),
        );
    }

    fun lookup(loop: GdForSt): LookupElement =
        GdLookup.create(
            loop.varNmi.name,
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            typed = loop.expr?.returnType?.parseFromSquare() ?: "",
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(param: GdParam): LookupElement =
        GdLookup.create(
            param.varNmi.name,
            typed = param.returnType,
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(decl: GdSetDecl): LookupElement =
        GdLookup.create(
            decl.varNmi?.name ?: "",
            typed = decl.typed?.text ?: "",
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(binding: GdBindingPattern): LookupElement {
        return GdLookup.create(
            binding.varNmi.name,
//            typed = binding.typed?.text ?: "", TODO dá se v match zjistit typ?
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            priority = GdLookup.LOCAL_USER_DEFINED,
        );
    }

    fun lookup(signal: GdSignalDeclTl): LookupElement {
        return GdLookup.create(
            signal.signalIdNmi?.name ?: "",
            typed = "signal",
            icon = GdIcon.getEditorIcon(GdIcon.SIGNAL_MARKER),
            priority = GdLookup.LOCAL_USER_DEFINED,
        );
    }

    fun lookup(element: GdKeyValue): LookupElement {
        val id = element.firstChild;
        val _val = element.lastChild;
        var tail: String? = null;
        if (_val is GdLiteralEx) {
            tail = "(${_val.text})";
        }

        return GdLookup.create(
            id.text.orEmpty(),
            typed = GdPsiUtils.returnType(_val),
            icon = GdIcon.getEditorIcon(GdIcon.CONST_MARKER),
            tail = tail,
            priority = GdLookup.LOCAL_USER_DEFINED,
        );
    }

}
