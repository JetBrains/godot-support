package gdscript.polySymbols.sdk

import com.intellij.polySymbols.PolySymbolModifier
import gdscript.polySymbols.GdPolySymbolModifier
import gdscript.polySymbols.sdk.xml.GdSdkData

object GdSdkPolySymbolsUtil {
    fun List<GdSdkData.ParameterData>.hasSameSignature(
        other: List<GdSdkData.ParameterData>) : Boolean {
        if (this.size != other.size) return false
        return this.zip(other).all { (param1, param2) -> param1.type == param2.type }
    }

    fun getModifiersFromQualifierData(data: GdSdkData.QualifierData?): Set<PolySymbolModifier> {
        val mods = mutableSetOf<PolySymbolModifier>()
        if (data?.isConst == true)
            mods.add(GdPolySymbolModifier.CONST)
        if (data?.isStatic == true)
            mods.add(GdPolySymbolModifier.STATIC)
        if (data?.isVirtual == true)
            mods.add(GdPolySymbolModifier.VIRTUAL)
        if (data?.isVariadic == true)
            mods.add(GdPolySymbolModifier.VARIADIC)
        if (data?.isRequired == true)
            mods.add(GdPolySymbolModifier.REQUIRED)
        return mods
    }
}