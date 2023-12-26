package config.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import config.psi.ConfigOperation
import config.psi.impl.GdConfigOperationElementType

class ConfigOperationStubImpl : StubBase<ConfigOperation>, ConfigOperationStub {

    private val operand: String
    private val leftTyped: String
    private val rightTyped: String

    constructor(parent: StubElement<*>?, operand: String, leftTyped: String, rightTyped: String) : super(parent, GdConfigOperationElementType) {
        this.operand = operand
        this.leftTyped = leftTyped
        this.rightTyped = rightTyped
    }

    override fun operand(): String = operand
    override fun leftTyped(): String = leftTyped
    override fun rightTyped(): String = rightTyped

}
