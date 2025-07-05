package gdscript.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ProjectScope

object ProjectUtil {

    fun Project.contentScope(): GlobalSearchScope {
        return ProjectScope.getContentScope(this)
    }

}
