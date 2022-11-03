package tscn.index

import com.intellij.psi.stubs.StubIndexKey
import tscn.psi.TscnResourceHeader
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnParagraph

object TscnIndices {

    val VERSION = 3;

    val PARAGRAPH_INDEX = StubIndexKey.createIndexKey<String, TscnParagraph>("tscn.nodeParagraph")
    val RESOURCE_INDEX = StubIndexKey.createIndexKey<String, TscnResourceHeader>("tscn.extResource")
    val NODE_INDEX = StubIndexKey.createIndexKey<String, TscnNodeHeader>("tscn.nodeResource")

}
