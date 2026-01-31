package gdscript.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.GlobalSearchScope.getScopeRestrictedByFileTypes
import com.intellij.psi.search.ProjectScope
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import tscn.TresFileType
import tscn.TscnFileType

object ProjectUtil {

    fun Project.contentScope(): GlobalSearchScope {
        val base = ProjectScope.getContentScope(this)
        return getScopeRestrictedByFileTypes(base, GdFileType, TscnFileType, TresFileType)
    }

}
