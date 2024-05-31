package tscn.index.stub

import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnNodeHeader

interface TscnNodeHeaderStub : StubElement<TscnNodeHeader> {

    fun getName(): String
    fun getType(): String
    fun getScriptResource(): String
    fun getInstanceResource(): String
    fun getParentPath(): String
    fun getNodePath(): String
    fun isUniqueNameOwner(): Boolean
    fun isVisible(): Boolean
    fun hasScript(): Boolean
    fun getGroups(): Array<String>
    fun getIndex(): Int

}
