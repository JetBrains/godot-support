package config.psi.util

import config.psi.ConfigOperator

object GdConfigOperatorUtil {

    fun getName(element: ConfigOperator): String {
        val stub = element.stub
        if (stub != null) return stub.name()

        return element.operatorNm.text
    }

}
