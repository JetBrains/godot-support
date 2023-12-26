package config.psi.util

import config.psi.ConfigAnnotation

object GdConfigAnnotationUtil {

    fun isVariadic(element: ConfigAnnotation): Boolean {
        val stub = element.stub
        if (stub != null) return stub.isVariadic()

        return element.variadicMark != null
    }

    fun requiredCount(element: ConfigAnnotation): Int {
        val stub = element.stub
        if (stub != null) return stub.requiredCount()

        return element.required.text.toInt()
    }

    fun getName(element: ConfigAnnotation): String {
        val stub = element.stub
        if (stub != null) return stub.name()

        return element.annotationNm.text
    }

    fun getParams(element: ConfigAnnotation): HashMap<String, String> {
        val stub = element.stub
        if (stub != null) return stub.params()

        val params = hashMapOf<String, String>()
        element.paramList.forEach {
            params[it.paramName.text] = it.type.text
        }

        return params
    }

}
