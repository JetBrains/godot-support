package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnNodeHeader
import tscn.psi.impl.TscnNodeHeaderElementType

class TscnNodeHeaderStubImpl : StubBase<TscnNodeHeader>, TscnNodeHeaderStub {

    private var name: String = "";
    private var type: String = "";
    private var parentPath: String = "";
    private var nodePath: String = "";
    private var scriptResource: String = "";
    private var isUniqueNameOwner: Boolean = false;

    constructor(
        parent: StubElement<*>?,
        name: String,
        type: String,
        parentPath: String,
        nodePath: String,
        isUniqueNameOwner: Boolean,
        scriptResource: String,
    ) : super(parent,
        TscnNodeHeaderElementType) {
        this.name = name;
        this.type = type;
        this.parentPath = parentPath;
        this.nodePath = nodePath;
        this.isUniqueNameOwner = isUniqueNameOwner;
        this.scriptResource = scriptResource;
    }

    override fun getName(): String = name;

    override fun getType(): String = type;

    override fun getScriptResource(): String = scriptResource;

    override fun getNodePath(): String = nodePath;

    override fun isUniqueNameOwner(): Boolean = isUniqueNameOwner;

    override fun hasScript(): Boolean = scriptResource.isNotBlank();

    override fun getParentPath(): String = parentPath;

}
