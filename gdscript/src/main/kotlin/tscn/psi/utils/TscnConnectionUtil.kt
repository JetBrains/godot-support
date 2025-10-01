package tscn.psi.utils

import tscn.psi.TscnConnectionHeader

/**
 * Connection (signal) line
 * [connection signal="sig_c" from="." to="Outer/Uniqe/Node3D" method="_on_form_sig_c"]
 */
object TscnConnectionUtil {

    fun getFrom(element: TscnConnectionHeader): String {
        val stub = element.stub
        if (stub != null) return stub.getFrom()

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_FROM)
    }

    fun getTo(element: TscnConnectionHeader): String {
        val stub = element.stub
        if (stub != null) return stub.getTo()

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_TO)
    }

    fun getSignal(element: TscnConnectionHeader): String {
        val stub = element.stub
        if (stub != null) return stub.getSignal()

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_SIGNAL)
    }

    fun getMethod(element: TscnConnectionHeader): String {
        val stub = element.stub
        if (stub != null) return stub.getMethod()

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_METHOD)
    }

}
