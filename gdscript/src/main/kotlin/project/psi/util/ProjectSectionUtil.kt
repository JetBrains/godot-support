package project.psi.util

import project.psi.ProjectSection

object ProjectSectionUtil {

    fun getName(element: ProjectSection): String {
        val stub = element.stub;
        if (stub != null) return stub.getName();

        return element.sectionNm?.text ?: "";
    }

}
