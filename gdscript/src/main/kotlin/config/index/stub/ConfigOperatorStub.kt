package config.index.stub

import com.intellij.psi.stubs.StubElement
import config.psi.ConfigOperator

interface ConfigOperatorStub : StubElement<ConfigOperator> {

    fun name(): String

}
