package config.index

import com.intellij.psi.stubs.StubIndexKey
import config.psi.ConfigAnnotation
import config.psi.ConfigOperation
import config.psi.ConfigOperator

object GdConfigIndices {

    val VERSION = 2

    val OPERATOR_INDEX = StubIndexKey.createIndexKey<String, ConfigOperator>("gdconf.operator")
    val OPERATION_INDEX = StubIndexKey.createIndexKey<String, ConfigOperation>("gdconf.operation")
    val ANNOTATION_INDEX = StubIndexKey.createIndexKey<String, ConfigAnnotation>("gdconf.annotation")

}
