package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.registry.Registry
import org.jetbrains.annotations.VisibleForTesting

enum class GdCompletionSource {
    LSP,
    // POLY_SYMBOLS - at the moment it is enough to track LSP and non-LSP
}

/**
 * Deduplicates completion items when both the Godot LSP and the built-in completion produce items with the same name.
 * The built-in items carry GDScript icons, tail text, type text, and priorities, so they are preferred.
 *
 * The contributor keeps the first item for a name and drops a later LSP item with the same name.
 * It therefore needs every built-in contributor to run before the platform `LspCompletionContributor`.
 * That holds for each contributor that is not `order="last"`.
 * A contributor that is `order="last"` must therefore declare `order="last, before LspCompletionContributor"`
 *
 * The match uses the name only. A method and a variable with the same name collide, and the built-in item wins.
 */
class GdCompletionDeduplicatingContributor : CompletionContributor() {

    companion object {

        val SERVICE_COMPLETION_KEY: Key<GdCompletionSource> = Key.create("lsp.service.item")

        private val LOG = logger<GdCompletionDeduplicatingContributor>()

        private const val REGISTRY_KEY = "gdscript.completion.deduplicateLspItems"
        fun isEnabled(): Boolean = Registry.`is`(REGISTRY_KEY)

        /** The key is the element's lookup string with one trailing group removed, tested in this order: "()", then "(…)", then "(". */
        @VisibleForTesting
        fun normalizeKey(lookupString: String): String {
            return when {
                lookupString.endsWith("()") -> lookupString.dropLast(2)
                lookupString.endsWith("(\u2026)") -> lookupString.dropLast(3) // The ellipsis is the single character U+2026, not three periods.
                lookupString.endsWith("(") -> lookupString.dropLast(1)
                else -> lookupString
            }
        }

        /** Decides whether to keep a completion item, given the keys already seen for earlier items.*/
        @VisibleForTesting
        fun shouldKeep(seenKeys: MutableSet<String>, source: GdCompletionSource?, lookupString: String): Boolean {
            val key = normalizeKey(lookupString)
            return if (source == GdCompletionSource.LSP) {
                // LSP result: keep only if the key was not already produced by PolySymbols.
                !seenKeys.contains(key)
            } else {
                // Non-LSP result: always keep and record the key.
                seenKeys.add(key)
                true
            }
        }
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (!isEnabled()) {
            return
        }

        val seenKeys = mutableSetOf<String>()
        result.runRemainingContributors(parameters) { completionResult ->
            try {
                val completionSource = completionResult.lookupElement.getUserData(SERVICE_COMPLETION_KEY)
                if (shouldKeep(seenKeys, completionSource, completionResult.lookupElement.lookupString)) {
                    result.passResult(completionResult)
                }
            } catch (e: Throwable) {
                rethrowControlFlowException(e)
                LOG.error("Error during completion deduplication", e)
                result.passResult(completionResult)
            }
        }
    }
}
