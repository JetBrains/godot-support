package gdscript.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiCodeFragment
import com.intellij.psi.PsiElement
import com.intellij.psi.SingleRootFileViewProvider
import com.intellij.psi.impl.PsiManagerEx
import com.intellij.psi.impl.source.tree.FileElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.testFramework.LightVirtualFile
import com.jetbrains.rider.godot.community.gdscript.GdLanguage

/**
 * A single GDScript expression that is inside debugger's evaluate/immediate window, or breakpoint condition.
 *
 * In the image of Java's `PsiCodeFragmentImpl`:
 *  - [GdFile] because GDScript completion and resolve branch on that type;
 *  - [PsiCodeFragment] the platform keys its fragment-preserving reparse on that interface
 *
 * @param physical `true` for a fragment that backs an editor document, which is what
 *  [com.intellij.xdebugger.evaluation.XDebuggerEditorsProviderBase.createDocument] asks for;
 *  `false` for a throw-away fragment, such as the copies completion works on ([clone]).
 */
class GdPsiCodeFragment(
    project: Project,
    name: String,
    text: CharSequence,
    context: PsiElement?,
    physical: Boolean,
) : GdFile(
    PsiManagerEx.getInstanceEx(project).fileManager.createFileViewProvider(
        LightVirtualFile(name, GdLanguage, text),
        physical,
    )
), PsiCodeFragment {

    /** Never read directly, always through [getContext]. */
    private var contextElement: PsiElement? = context

    /** The element this fragment pretends to have been typed at, or `null` once that element has been invalidated. */
    override fun getContext(): PsiElement? {
        val context = contextElement
        if (context == null) return super.getContext()
        if (context.isValid) return context

        // Forget the dead element so its tree can be collected; invalidation is permanent.
        contextElement = null
        return super.getContext()
    }

    private var isPhysicalFragment: Boolean = physical
    override fun isPhysical(): Boolean = isPhysicalFragment

    private var forcedResolveScope: GlobalSearchScope? = null
    override fun getForcedResolveScope(): GlobalSearchScope? = forcedResolveScope
    override fun forceResolveScope(scope: GlobalSearchScope?) {
        forcedResolveScope = scope
    }

    /** Non-null only on copies produced by [clone], which is where this is explained. */
    private var shadowViewProvider: FileViewProvider? = null
    override fun getViewProvider(): FileViewProvider = shadowViewProvider ?: super.getViewProvider()

    init {
        init(GdTypes.CODE_FRAGMENT, GdTypes.CODE_FRAGMENT)
        // Publishes this fragment, so it must stay the last statement of construction.
        (viewProvider as SingleRootFileViewProvider).forceCachedPsi(this)
    }

    /**
     * Completion clones the file it runs in and edits the copy, so the clone must keep [contextElement] and the
     * [GdTypes.CODE_FRAGMENT] content type, but must not share the original's document.
     *
     * Modeled on `PsiCodeFragmentImpl` / `PyExpressionCodeFragmentImpl`.
     *
     * Uses `cloneImpl` (tree clone + `Object.clone()`) instead of the inherited `PsiFileImpl.clone()`, because the
     * latter rebuilds via the view provider into a plain [GdFile], dropping both. `Object.clone()` copies every
     * field, including the view provider reference — so without [shadowViewProvider] the clone would share the
     * live document, and completion's dummy-identifier insertion would corrupt it. `myViewProvider` is
     * `private final` in the superclass, hence the extra field instead of reassigning it directly.
     */
    override fun clone(): GdPsiCodeFragment {
        val clone = cloneImpl(calcTreeElement().clone() as FileElement) as GdPsiCodeFragment
        val cloneViewProvider = (manager as PsiManagerEx).fileManager.createFileViewProvider(
            LightVirtualFile(name, GdLanguage, text),
            false,
        ) as SingleRootFileViewProvider
        clone.isPhysicalFragment = false
        clone.myOriginalFile = this
        clone.shadowViewProvider = cloneViewProvider
        // Publishes the copy, so again it comes last.
        cloneViewProvider.forceCachedPsi(clone)
        return clone
    }

    override fun toString(): String = "GdScript code fragment"
}
