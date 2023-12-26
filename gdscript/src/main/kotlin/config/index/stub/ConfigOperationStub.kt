package config.index.stub

import com.intellij.psi.stubs.StubElement
import config.psi.ConfigOperation

interface ConfigOperationStub : StubElement<ConfigOperation> {

    fun operand(): String
    fun leftTyped(): String
    fun rightTyped(): String

}
