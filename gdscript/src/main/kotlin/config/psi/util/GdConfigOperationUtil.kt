package config.psi.util

import config.psi.ConfigOperation

object GdConfigOperationUtil {

    fun getOperand(element: ConfigOperation): String {
        val stub = element.stub
        if (stub != null) return stub.operand()

        return element.opType.text
    }

    fun getLeftTyped(element: ConfigOperation): String {
        val stub = element.stub
        if (stub != null) return stub.leftTyped()

        return element.leftType.text
    }

    fun getRightTyped(element: ConfigOperation): String {
        val stub = element.stub
        if (stub != null) return stub.rightTyped()

        return element.rightType.text
    }

}
