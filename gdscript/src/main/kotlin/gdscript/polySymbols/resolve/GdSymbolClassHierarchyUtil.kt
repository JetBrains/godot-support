package gdscript.polySymbols.resolve

import com.intellij.polySymbols.query.PolySymbolScope
import gdscript.polySymbols.GdClassSymbol

object GdSymbolClassHierarchyUtil {

    /**
     * Walks the superclass chain starting from [start] and collects each ancestor's
     * [GdClassSymbol.directMemberScope].
     */
    fun collectInheritedScopes(
        start: GdClassSymbol,
        visited: MutableSet<String> = LinkedHashSet(),
    ): List<PolySymbolScope> {
        val result = mutableListOf<PolySymbolScope>()

        var current = start.resolveSuperClassSymbol()
        while (current != null) {
            if (!visited.add(current.classId)) break
            result.add(current.directMemberScope)
            current = current.resolveSuperClassSymbol()
        }

        return result
    }
}
