package tscn.psi

import tscn.psi.utils.TscnHeaderUtils

object TscnPsiUtils {

    /** Node */
    @JvmStatic fun getName(element: TscnNodeHeader): String = TscnHeaderUtils.getValue(element.headerValueList, "name");
    @JvmStatic fun getType(element: TscnNodeHeader): String = TscnHeaderUtils.getValue(element.headerValueList, "type");
    @JvmStatic fun getParentPath(element: TscnNodeHeader): String = TscnHeaderUtils.getValue(element.headerValueList, "parent");
    @JvmStatic fun getNodePath(element: TscnNodeHeader): String = TscnHeaderUtils.getNodePath(element);
    @JvmStatic fun isUniqueNameOwner(element: TscnNodeHeader): Boolean = TscnHeaderUtils.isUniqueNameOwner(element);

    /** Ext resource */
    @JvmStatic fun getType(element: TscnExtHeader): String = TscnHeaderUtils.getValue(element.headerValueList, "type");
    @JvmStatic fun getPath(element: TscnExtHeader): String = TscnHeaderUtils.getPath(element.headerValueList);

}