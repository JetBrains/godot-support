package tscn.psi

import tscn.psi.utils.TscnNodeUtil
import tscn.psi.utils.TscnResourceUtil

object TscnPsiUtils {

    /** Node */
    @JvmStatic fun getName(element: TscnNodeHeader): String = TscnNodeUtil.getName(element);
    @JvmStatic fun getType(element: TscnNodeHeader): String = TscnNodeUtil.getType(element);
    @JvmStatic fun getParentPath(element: TscnNodeHeader): String = TscnNodeUtil.getParentPath(element);
    @JvmStatic fun isUniqueNameOwner(element: TscnNodeHeader): Boolean = TscnNodeUtil.isUniqueNameOwner(element);
    @JvmStatic fun getScriptResource(element: TscnNodeHeader): String = TscnNodeUtil.getScriptResource(element);
    @JvmStatic fun getNodePath(element: TscnNodeHeader): String = TscnNodeUtil.getNodePath(element);
    @JvmStatic fun hasScript(element: TscnNodeHeader): Boolean = TscnNodeUtil.hasScript(element);

    /** ext_resource */
    @JvmStatic fun getType(element: TscnResourceHeader): String = TscnResourceUtil.getType(element);
    @JvmStatic fun getPath(element: TscnResourceHeader): String = TscnResourceUtil.getPath(element);
    @JvmStatic fun getId(element: TscnResourceHeader): String = TscnResourceUtil.getId(element);

}
