package tscn.psi.utils

import tscn.psi.TscnResourceHeader

/**
 * Resource line utils
 * [ext_resource type="Script" path="res://Asd.gd" id="1_3apro"]
 */
object TscnResourceUtil {

    fun getId(element: TscnResourceHeader): String {
        val stub = element.stub;
        if (stub != null) return stub.getId();

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_ID);
    }

    fun getPath(element: TscnResourceHeader): String {
        val stub = element.stub;
        if (stub != null) return stub.getPath();

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_PATH);
    }

    fun getType(element: TscnResourceHeader): String {
        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_TYPE);
    }

}
