package gdscript.psi.impl

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassNameNm

class ClassNameNmImpl : StubBase<GdClassNameNm> {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?) : super(parent, elementType)

}
