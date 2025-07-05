package config.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import config.psi.ConfigOperator
import config.psi.impl.GdConfigOperatorElementType

class ConfigOperatorStubImpl : StubBase<ConfigOperator>, ConfigOperatorStub {

    private val name: String


    constructor(parent: StubElement<*>?, name: String) : super(parent, GdConfigOperatorElementType) {
        this.name = name
    }

    override fun name(): String = name

}
