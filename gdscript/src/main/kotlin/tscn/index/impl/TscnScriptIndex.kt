package tscn.index.impl

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import tscn.index.TscnIndices
import tscn.psi.TscnExtHeader

object TscnScriptIndex : StringStubIndexExtension<TscnExtHeader>() {

    override fun getKey(): StubIndexKey<String, TscnExtHeader> = TscnIndices.SCRIPT_INDEX;

}
