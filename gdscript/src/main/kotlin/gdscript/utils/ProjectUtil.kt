package gdscript.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope

object ProjectUtil {

    fun Project.globalSearchScope(): GlobalSearchScope {
        return GlobalSearchScope.allScope(this)
    }

}
