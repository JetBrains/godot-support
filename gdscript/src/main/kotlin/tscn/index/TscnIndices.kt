package tscn.index

import com.intellij.psi.stubs.StubIndexKey
import tscn.psi.TscnExtHeader
import tscn.psi.TscnNodeHeader

object TscnIndices {

    val SCRIPT_INDEX = StubIndexKey.createIndexKey<String, TscnExtHeader>("tscn.extResource")
    val NODE_INDEX = StubIndexKey.createIndexKey<String, TscnNodeHeader>("tscn.nodeResource")

}
