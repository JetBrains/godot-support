package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnNodeHeader
import tscn.psi.impl.TscnNodeHeaderElementType

class TscnNodeHeaderStubImpl : StubBase<TscnNodeHeader>, TscnNodeHeaderStub {

    private var name: String = "";
    private var type: String = "";
    private var parentPath: String = "";
    private var isUniqueNameOwner: Boolean = false;

    constructor(
        parent: StubElement<*>?,
        name: String,
        type: String,
        parentPath: String,
        isUniqueNameOwner: Boolean,
    ) : super(parent,
        TscnNodeHeaderElementType) {
        this.name = name;
        this.type = type;
        this.parentPath = parentPath;
        this.isUniqueNameOwner = isUniqueNameOwner;
    }

    override fun name(): String = name;

    override fun type(): String = type;

    override fun nodePath(): String {
        val isRoot = parentPath == ".";

        return "${if (isRoot) "" else "$parentPath/"}${name}"
    }

    override fun isUniqueNameOwner(): Boolean = isUniqueNameOwner;

    override fun parentPath(): String = parentPath;

}
