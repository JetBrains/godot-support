package gdscript.annotator

import com.intellij.codeInspection.util.InspectionMessage
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.Key
import com.intellij.testFramework.TestModeFlags
import gdscript.utils.isRiderGodotSupportPluginInstalled
import org.jetbrains.annotations.NotNull

/**
 * In tests, set this flag to preserve original annotator severities instead of
 * the production downgrade applied in [newAnnotationGd].
 */
@JvmField
val GD_ANNOTATOR_ORIGINAL_SEVERITY: Key<Boolean> = Key.create("gdAnnotatorOriginalSeverity")

val isGodotSupportInstalled = PluginManagerCore.isRiderGodotSupportPluginInstalled()

/**
 * Downgrades GDScript annotator severities to [HighlightSeverity.INFORMATION] in production
 * to avoid showing false-positive squiggles before the LSP connects. Hover text is still shown.
 */
fun AnnotationHolder.newAnnotationGd(
        @NotNull severity: HighlightSeverity,
        @NotNull @InspectionMessage message: String
): AnnotationBuilder {
    val effectiveSeverity = if (TestModeFlags.`is`(GD_ANNOTATOR_ORIGINAL_SEVERITY)) severity
    else HighlightSeverity.INFORMATION

    return this.newAnnotation(effectiveSeverity, message)
}
// TODO: We can either pass raw strings here or pass in the names of the resources and convert it here
