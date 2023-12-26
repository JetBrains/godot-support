package config.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import config.index.GdConfigIndices
import config.psi.ConfigAnnotation

class GdConfigAnnotationDataIndex : StringStubIndexExtensionExt<ConfigAnnotation>() {

    companion object {
        val INSTANCE = GdConfigAnnotationDataIndex()
    }

    override fun getKey(): StubIndexKey<String, ConfigAnnotation> {
        return GdConfigIndices.ANNOTATION_INDEX
    }

    override fun getVersion(): Int {
        return GdConfigIndices.VERSION
    }

}
