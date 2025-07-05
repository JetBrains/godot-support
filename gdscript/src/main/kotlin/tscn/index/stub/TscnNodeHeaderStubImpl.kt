package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnNodeHeader
import tscn.psi.impl.TscnNodeHeaderElementType

class TscnNodeHeaderStubImpl : StubBase<TscnNodeHeader>, TscnNodeHeaderStub {

    private var name: String = ""
    private var type: String = ""
    private var parentPath: String = ""
    private var nodePath: String = ""
    private var scriptResource: String = ""
    private var instnaceResource: String = ""
    private var isUniqueNameOwner: Boolean = false
    private var visible: Boolean = true
    private var groups: Array<String> = emptyArray()
    private var index: Int = -1

    constructor(
        parent: StubElement<*>?,
        name: String,
        type: String,
        parentPath: String,
        nodePath: String,
        isUniqueNameOwner: Boolean,
        visible: Boolean,
        scriptResource: String,
        instanceResource: String,
        groups: Array<String>,
        index: Int,
    ) : super(parent,
        TscnNodeHeaderElementType) {
        this.name = name
        this.type = type
        this.parentPath = parentPath
        this.nodePath = nodePath
        this.isUniqueNameOwner = isUniqueNameOwner
        this.visible = visible
        this.scriptResource = scriptResource
        this.instnaceResource = instanceResource
        this.groups = groups
        this.index = index
    }

    override fun getName(): String = name

    override fun getType(): String = type

    override fun getScriptResource(): String = scriptResource

    override fun getInstanceResource(): String = instnaceResource

    override fun getNodePath(): String = nodePath

    override fun isUniqueNameOwner(): Boolean = isUniqueNameOwner

    override fun isVisible(): Boolean = visible

    override fun hasScript(): Boolean = scriptResource.isNotBlank()

    override fun getParentPath(): String = parentPath

    override fun getGroups(): Array<String> = groups

    override fun getIndex() = index

}
