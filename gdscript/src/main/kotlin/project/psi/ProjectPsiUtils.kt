package project.psi

import project.psi.util.ProjectDataUtil
import project.psi.util.ProjectSectionUtil

object ProjectPsiUtils {

    /** Section */
    @JvmStatic fun getName(element: ProjectSection): String = ProjectSectionUtil.getName(element)

    /** Data */
    @JvmStatic fun getKey(element: ProjectData): String = ProjectDataUtil.getKey(element)
    @JvmStatic fun getValue(element: ProjectData): String = ProjectDataUtil.getValue(element)

}
