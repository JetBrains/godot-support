package common.index

import com.intellij.util.indexing.ID
import com.intellij.util.indexing.ScalarIndexExtension

abstract class ScalarIndexExtensionExt<K : Any> : ScalarIndexExtension<K>() {

    // Cannot use directly getName due to OverrideOnly annotation api violation
    abstract val id: ID<K, Void>

    override fun getName(): ID<K, Void> {
        return id
    }

}
