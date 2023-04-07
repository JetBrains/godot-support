package tscn.index

import com.intellij.psi.stubs.StubIndexKey
import tscn.psi.TscnConnectionHeader
import tscn.psi.TscnResourceHeader
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnParagraph

object TscnIndices {

    val VERSION = 4

    val PARAGRAPH_INDEX = StubIndexKey.createIndexKey<String, TscnParagraph>("tscn.nodeParagraph")
    val RESOURCE_INDEX = StubIndexKey.createIndexKey<String, TscnResourceHeader>("tscn.extResource")
    val CONNECTION_INDEX = StubIndexKey.createIndexKey<String, TscnConnectionHeader>("tscn.connection")
    val NODE_INDEX = StubIndexKey.createIndexKey<String, TscnNodeHeader>("tscn.nodeResource")

}
