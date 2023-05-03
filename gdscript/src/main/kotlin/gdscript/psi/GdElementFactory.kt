package gdscript.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdFileType

/**
 * Create PsiElements for refactoring purposes
 */
object GdElementFactory {

    /**
     * PsiElement<IDENTIFIER> of class_name {NAME}
     */
    fun classNameNmi(project: Project, name: String): PsiElement {
        val file = createFile(project, "class_name $name\n")

        return PsiTreeUtil.findChildOfType(file, GdClassNameNmi::class.java)!!.firstChild
    }

    fun enumDeclNmi(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nenum $name {VALUE = 1}\n")

        return PsiTreeUtil.findChildOfType(file, GdEnumDeclNmi::class.java)!!.firstChild
    }

    fun enumValueNmi(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nenum Enum {$name = 1}\n");

        return PsiTreeUtil.findChildOfType(file, GdEnumValueNmi::class.java)!!.firstChild
    }

    fun funcDeclIdNmi(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nfunc $name():\n\tvar variable = func $name(): return 1\n")

        return PsiTreeUtil.findChildOfType(file, GdFuncDeclIdNmi::class.java)!!.firstChild
    }

    fun getMethodIdNm(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nvar variable:\n\tget = $name\n");

        return PsiTreeUtil.findChildOfType(file, GdGetMethodIdNm::class.java)!!.firstChild
    }

    fun inheritanceIdNm(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends $name\n")

        return PsiTreeUtil.findChildOfType(file, GdInheritanceIdNm::class.java)!!.firstChild
    }

    fun inheritanceSubIdNm(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node.$name\n")

        return PsiTreeUtil.findChildOfType(file, GdInheritanceSubIdNm::class.java)!!.firstChild
    }

    fun methodIdNmi(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nfunc $name():\n\tpass\n")

        return PsiTreeUtil.findChildOfType(file, GdMethodIdNmi::class.java)!!.firstChild
    }

    fun refIdNm(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nfunc fu():\n\t$name\n")

        return PsiTreeUtil.findChildOfType(file, GdRefIdNm::class.java)!!.firstChild
    }

    fun setMethodIdNm(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nvar variable:\n\tset = $name\n")

        return PsiTreeUtil.findChildOfType(file, GdSetMethodIdNm::class.java)!!.firstChild
    }

    fun signalIdNmi(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nsignal $name\n")

        return PsiTreeUtil.findChildOfType(file, GdSignalIdNmi::class.java)!!.firstChild
    }

    @Deprecated("")
    fun typeHintNm(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nvar variable: $name\n")

        return PsiTreeUtil.findChildOfType(file, GdTypeHintNm::class.java)!!.firstChild
    }

    fun typed(project: Project, type: String): PsiElement {
        val file = createFile(project, "extends Node\nvar variable: $type\n")

        return PsiTreeUtil.findChildOfType(file, GdTyped::class.java)!!
    }

    fun returnHintVal(project: Project, type: String): PsiElement {
        val file = createFile(project, "extends Node\nfunc fn() -> $type:\n\tpass")

        return PsiTreeUtil.findChildOfType(file, GdReturnHintVal::class.java)!!
    }

    fun varNmi(project: Project, name: String): PsiElement {
        val file = createFile(project, "extends Node\nfunc fu():\n\tvar $name = 1\n")

        return PsiTreeUtil.findChildOfType(file, GdVarNmi::class.java)!!.firstChild
    }

    fun callExpr(project: Project, expr: String): PsiElement {
        val file = createFile(project, "extends Node\nfunc fu():\n\t$expr");

        return PsiTreeUtil.findChildOfType(file, GdExpr::class.java)!!
    }

    fun shortAssignTyped(project: Project): PsiElement {
        val file = createFile(project, "extends Node\nvar a := 1\n")

        return PsiTreeUtil.findChildOfType(file, GdAssignTyped::class.java)!!
    }

    private fun createFile(project: Project, text: String) =
            PsiFileFactory.getInstance(project).createFileFromText("dum.gd", GdFileType, text) as GdFile
}
