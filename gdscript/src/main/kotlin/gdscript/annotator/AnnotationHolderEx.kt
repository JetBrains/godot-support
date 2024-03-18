package gdscript.annotator

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.Project
import gdscript.utils.RiderGodotSupportPluginUtil
import gdscript.utils.isRiderGodotSupportPluginInstalled
import org.jetbrains.annotations.NotNull

private val isGodotSupportInstalled = PluginManagerCore.isRiderGodotSupportPluginInstalled()
fun AnnotationHolder.newAnnotationGd(
        project: Project,
        @NotNull severity: HighlightSeverity,
        @NotNull message: String
): AnnotationBuilder {
    if (severity == HighlightSeverity.ERROR && isGodotSupportInstalled && RiderGodotSupportPluginUtil.isGodotSupportLSPRunning(project))
        return this.newSilentAnnotation(HighlightSeverity.INFORMATION)
    return this.newAnnotation(severity, message)
}
