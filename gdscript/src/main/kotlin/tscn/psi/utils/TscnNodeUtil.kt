package tscn.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import tscn.index.impl.TscnNodeIndex
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnParagraph
import tscn.psi.TscnResourceHeader

/**
 * Node line utils
 * [node name="Outer" type="Node3D" parent="."]
 */
object TscnNodeUtil {

    /** Header lines */

    fun getName(element: TscnNodeHeader): String {
        val stub = element.stub;
        if (stub != null) return stub.getName();

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_NAME);
    }

    fun getType(element: TscnNodeHeader): String {
        val stub = element.stub
        if (stub != null) return stub.getType()

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_TYPE)
    }

    fun getParentPath(element: TscnNodeHeader): String {
        val stub = element.stub;
        if (stub != null) return stub.getParentPath();

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_PARENT);
    }

    fun getGroups(element: TscnNodeHeader): Array<String> {
        val stub = element.stub
        if (stub != null) return stub.getGroups()

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_GROUPS)
            .trim('[', ']')
            .split(",")
            .map { it.trim(' ', '"') }
            .toTypedArray()
    }

    /** Data lines */

    fun isUniqueNameOwner(element: TscnNodeHeader): Boolean {
        val stub = element.stub;
        if (stub != null) return stub.isUniqueNameOwner();

        return TscnHeaderUtils.getDataValue(element.parent as TscnParagraph, TscnHeaderUtils.DL_UNIQUE) == "true"
    }

    fun getScriptResource(element: TscnNodeHeader): String {
        val stub = element.stub;
        if (stub != null) return stub.getScriptResource();

        // ExtResource("2_s5kgd"), ExtResource( 1 )
        var id = TscnHeaderUtils.getDataValue(element.parent as TscnParagraph, TscnHeaderUtils.DL_SCRIPT);
        if (id.isBlank()) return "";
        id = id.removePrefix("ExtResource(").removeSuffix(")").trim('"', ' ')

        val scripts = PsiTreeUtil.findChildrenOfType(element.containingFile, TscnResourceHeader::class.java);

        return scripts.find { it.id == id }?.path ?: "";
    }

    /** Other */

    fun getNodePath(element: TscnNodeHeader): String {
        val stub = element.stub;
        if (stub != null) return stub.getNodePath();

        val parentPath = element.parentPath;

        return when(parentPath) {
            "" -> "..";
            "." -> "../${element.name}";
            else -> "../$parentPath/${element.name}";
        }
    }

    fun getDirectParentPath(element: TscnNodeHeader): String {
        return when(val parentPath = element.parentPath) {
            "" -> ".";
            "." -> element.name;
            else -> "$parentPath/${element.name}";
        }
    }

    fun hasScript(element: TscnNodeHeader): Boolean {
        val stub = element.stub;
        if (stub != null) return stub.hasScript();

        return getScriptResource(element).isNotBlank();
    }

    fun listAllGroups(element: PsiElement): Array<String> {
        return listAllGroups(element.project)
    }

    fun listAllGroups(project: Project): Array<String> {
        return TscnNodeIndex.getAllValues(project).flatMap {
            it.groups.toList()
        }.toTypedArray()
    }

}
