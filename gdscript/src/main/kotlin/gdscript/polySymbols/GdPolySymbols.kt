package gdscript.polySymbols

import com.intellij.model.Symbol
import com.intellij.openapi.project.Project
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolProperty
import com.intellij.polySymbols.PolySymbolQualifiedName
import com.intellij.polySymbols.context.PolyContext
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.search.PolySymbolSearchTarget
import com.intellij.polySymbols.utils.kindName
import com.intellij.polySymbols.utils.namespace
import com.intellij.psi.PsiElement
import java.util.Locale

abstract class GdPolySymbol : PolySymbol {
    override val psiContext: PsiElement? = null

    // Documentation will be provided by the LSP only
    override fun getDocumentationTarget(location: PsiElement?): DocumentationTarget? = null

    /**
     * For SDK symbols it's equivalent to [declaringClassId] since they have no inner classes.
     *
     * For PSI symbols it's the leaf (simple) name.
     */
    abstract val declaringClassName: String

    /** Stable identity key for the declaring class - dotted classId for PSI, className for SDK. */
    @PolySymbol.Property(GdDeclaringClassIdProperty::class)
    abstract val declaringClassId: String

    @PolySymbol.Property(GdReturnTypeProperty::class)
    abstract val returnType: String

    /**
     * We compare by [declaringClassId] (stable, supports inner classes)
     * rather than [declaringClassName] which is display-oriented.
     */
    override fun isEquivalentTo(symbol: Symbol): Boolean {
        if (this === symbol) return true
        if (symbol !is GdPolySymbol) return false

        return qualifiedName == symbol.qualifiedName && declaringClassId == symbol.declaringClassId
    }

    override val priority: PolySymbol.Priority get() = PolySymbol.Priority.NORMAL

    /** Tail text shown next to the name in completion popups (e.g. parameter list for methods). */
    @PolySymbol.Property(GdCompletionTailTextProperty::class)
    open val completionTailText: String? get() = null

    /** Type text shown on the right side of completion popups (e.g. return type for methods/properties). */
    @PolySymbol.Property(GdCompletionTypeTextProperty::class)
    open val completionTypeText: String? get() = null

    val qualifiedName: PolySymbolQualifiedName
        get() = kind.withName(name)

    // overridden because the default impl makes names like "class" -> "clas" because it removes the last "s" indiscriminately
    override val presentation: TargetPresentation
        get() {
            val kindName = kindName.replace('-', ' ').lowercase(Locale.US)
            val description = "$namespace $kindName '$name'"
            return TargetPresentation.builder(description)
                .icon(icon)
                .presentation()
        }
}

interface GdClassSymbol : PolySymbol {
    /** Full class identifier */
    val classId: String
    /** Leaf (simple) name */
    val declaringClassName: String
    val directMemberScope: PolySymbolScope
    fun resolveSuperClassSymbol(): GdClassSymbol?

    /** Implementation must terminate on circular inheritance chains. */
    fun inheritedQueryScopes(): List<PolySymbolScope>
}