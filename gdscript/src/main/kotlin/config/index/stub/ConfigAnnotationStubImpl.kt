package config.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import config.psi.ConfigAnnotation
import config.psi.impl.GdConfigAnnotationElementType

class ConfigAnnotationStubImpl : StubBase<ConfigAnnotation>, ConfigAnnotationStub {

    private val isVariadic: Boolean
    private val requiredCount: Int
    private val name: String
    private val params: HashMap<String, String>

    constructor(parent: StubElement<*>?, isVariadic: Boolean, requiredCount: Int, name: String, params: HashMap<String, String>) : super(parent, GdConfigAnnotationElementType) {
        this.isVariadic = isVariadic
        this.requiredCount = requiredCount
        this.name = name
        this.params = params
    }

    override fun isVariadic(): Boolean = isVariadic
    override fun requiredCount(): Int = requiredCount
    override fun name(): String = name
    override fun params(): HashMap<String, String> = params

}
