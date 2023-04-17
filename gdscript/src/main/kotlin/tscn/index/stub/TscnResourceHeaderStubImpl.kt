package tscn.index.stub

import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import tscn.psi.TscnResourceHeader
import tscn.psi.impl.TscnResourceHeaderElementType

/**
 * Stubs only "Script" types as others are not required
 *
 * [ext_resource type="Script" path="res://grid_movement/grid/Grid.gd" id="3"]
 * [ext_resource type="PackedScene" uid="uid://c70qhbxkbrvth" path="res://combat/combatants/Opponent.tscn" id="7"]
 */
class TscnResourceHeaderStubImpl : StubBase<TscnResourceHeader>, TscnResourceHeaderStub {

    private var id: String = ""
    private var path: String = ""

    constructor(parent: StubElement<*>?, id: String, path: String) : super(parent, TscnResourceHeaderElementType) {
        this.id = id
        this.path = path
    }

    override fun getId(): String = id

    override fun getPath(): String = path

}
