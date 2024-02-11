package gdscript.run

import com.intellij.execution.configurations.LocatableRunConfigurationOptions
import com.intellij.openapi.components.StoredProperty

class GdRunConfigurationOptions : LocatableRunConfigurationOptions {

    constructor() : super() {
        isNameGenerated = true;
    }

    private val godotExe: StoredProperty<String?> =
        string("").provideDelegate(this, "godotExe")

    private val tscn: StoredProperty<String?> =
        string("").provideDelegate(this, "godot.tscn")

    private val debugShapes: StoredProperty<Boolean> =
        property(false).provideDelegate(this, "debugShapes")

    fun getGodotExe(): String = godotExe.getValue(this) ?: "";

    fun setGodotExe(godotExe: String) {
        this.godotExe.setValue(this, godotExe);
    };

    fun getTscn(): String = tscn.getValue(this) ?: "";

    fun setTscn(tscn: String) {
        this.tscn.setValue(this, tscn);
    };

    fun getDebugShapes(): Boolean = debugShapes.getValue(this);

    fun setDebugShapes(debugShapes: Boolean) {
        this.debugShapes.setValue(this, debugShapes);
    }

}
