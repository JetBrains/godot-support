package tscn.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import tscn.index.TscnIndices
import tscn.psi.TscnParagraph

class TscnSceneUidIndex : StringStubIndexExtensionExt<TscnParagraph>() {

    companion object {
        val INSTANCE = TscnSceneUidIndex()
    }

    override fun getKey(): StubIndexKey<String, TscnParagraph> = TscnIndices.SCENE_UID_INDEX

    override fun getVersion(): Int {
        return TscnIndices.VERSION
    }

}
