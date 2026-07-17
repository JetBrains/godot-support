package gdscript.polySymbols.sdk

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.utils.PolySymbolScopeWithCache
import gdscript.GdKeywords
import gdscript.library.GdDocClassesFoldersService
import gdscript.polySymbols.GdEnumValuesProperty
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolNamespace
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdNamedElement
import javax.swing.Icon

class GdSdkEnumSymbol(
    override val project: Project,
    override val declaringClassName: String,
    override val data: GdSdkData.EnumData
) : GdSdkPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.ENUM
    override val declaringClassId: String get() = declaringClassName
    override val name: String get() = data.name
    override val returnType: String get() = "EnumDictionary"

    @PolySymbol.Property(GdEnumValuesProperty::class)
    private val enumValues: List<String> get() = data.values.map { it.name }

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.ENUM_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.BUILT_IN
    override val completionTypeText: String get() = GdKeywords.INT

    // TODO once we change the caching system, change this one, probably should be held by the parent class in some way
    private val scopeDelegate get() = object : PolySymbolScopeWithCache<Project, String>(project, project, "$declaringClassId.$name") {

        override fun initialize(consumer: (PolySymbol) -> Unit, cacheDependencies: MutableSet<Any>) {
            data.values.forEach { eVData ->
                consumer(GdSdkEnumValueSymbol(project, declaringClassName, name, eVData))
            }

            cacheDependencies.add(GdDocClassesFoldersService.getInstance(project).modificationTracker)
        }

        override fun provides(kind: PolySymbolKind): Boolean =
            kind.namespace == GdPolySymbolNamespace.NAMESPACE && kind == GdPolySymbolKind.ENUM_VALUE

        override fun createPointer(): Pointer<out PolySymbolScopeWithCache<Project, String>> =
            Pointer.hardPointer(this)
    }

    override val queryScope: List<PolySymbolScope>
        get() = listOf(scopeDelegate)

    override val modifiers: Set<PolySymbolModifier>
        get() = setOf(
            PolySymbolModifier.STATIC
        )

    override fun psiElementRepresentsSdkSymbol(element: GdNamedElement): Boolean {
        return super.psiElementRepresentsSdkSymbol(element) && element is GdEnumDeclNmi
    }

    override fun createPointer(): Pointer<out GdSdkEnumSymbol> {
        val projectRef = project
        val className = declaringClassName
        val enumName = data.name

        return Pointer {
            if (projectRef.isDisposed) return@Pointer null

            val classData = getOwnerClassData(projectRef, className) ?: return@Pointer null

            val freshData = classData
                .enums
                .find { it.name == enumName }
                ?: return@Pointer null

            GdSdkEnumSymbol(projectRef, className, freshData)
        }
    }
}