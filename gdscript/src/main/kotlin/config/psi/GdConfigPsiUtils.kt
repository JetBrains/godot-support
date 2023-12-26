package config.psi

import config.psi.util.GdConfigAnnotationUtil
import config.psi.util.GdConfigOperationUtil
import config.psi.util.GdConfigOperatorUtil

object GdConfigPsiUtils {

    /** Operator **/
    @JvmStatic fun getName(element: ConfigOperator): String = GdConfigOperatorUtil.getName(element)

    /** Operation **/
    @JvmStatic fun getOperand(element: ConfigOperation): String = GdConfigOperationUtil.getOperand(element)
    @JvmStatic fun getLeftTyped(element: ConfigOperation): String = GdConfigOperationUtil.getLeftTyped(element)
    @JvmStatic fun getRightTyped(element: ConfigOperation): String = GdConfigOperationUtil.getRightTyped(element)

    /** Annotation **/
    @JvmStatic fun isVariadic(element: ConfigAnnotation): Boolean = GdConfigAnnotationUtil.isVariadic(element)
    @JvmStatic fun requiredCount(element: ConfigAnnotation): Int = GdConfigAnnotationUtil.requiredCount(element)
    @JvmStatic fun getName(element: ConfigAnnotation): String = GdConfigAnnotationUtil.getName(element)
    @JvmStatic fun getParams(element: ConfigAnnotation): HashMap<String, String> = GdConfigAnnotationUtil.getParams(element)

}
