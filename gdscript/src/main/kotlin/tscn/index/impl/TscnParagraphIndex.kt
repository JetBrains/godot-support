package tscn.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import tscn.index.TscnIndices
import tscn.psi.TscnParagraph

object TscnParagraphIndex : StringStubIndexExtensionExt<TscnParagraph>() {

    override fun getKey(): StubIndexKey<String, TscnParagraph> = TscnIndices.PARAGRAPH_INDEX;

    override fun getVersion(): Int {
        return TscnIndices.VERSION;
    }

}
