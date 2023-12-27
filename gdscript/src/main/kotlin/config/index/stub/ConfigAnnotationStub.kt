package config.index.stub

import com.intellij.psi.stubs.StubElement
import config.psi.ConfigAnnotation

interface ConfigAnnotationStub : StubElement<ConfigAnnotation> {

    fun isVariadic(): Boolean
    fun requiredCount(): Int
    fun name(): String
    fun params(): LinkedHashMap<String, String>

}
