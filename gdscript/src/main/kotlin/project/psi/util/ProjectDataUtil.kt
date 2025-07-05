package project.psi.util

import project.psi.ProjectData

object ProjectDataUtil {

    fun getKey(element: ProjectData): String {
        val stub = element.stub;
        if (stub != null) return stub.getKey();

        return element.dataKey.text;
    }

    fun getValue(element: ProjectData): String {
        return element.dataValue.text;
    }

}
