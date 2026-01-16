package gdscript.annotator

import com.intellij.codeInspection.util.InspectionMessage
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.Project
import gdscript.utils.RiderGodotSupportPluginUtil
import gdscript.utils.isRiderGodotSupportPluginInstalled
import org.jetbrains.annotations.NotNull

val isGodotSupportInstalled = PluginManagerCore.isRiderGodotSupportPluginInstalled()
fun AnnotationHolder.newAnnotationGd(
        project: Project,
        @NotNull severity: HighlightSeverity,
        @NotNull @InspectionMessage message: String
): AnnotationBuilder {
    if (severity == HighlightSeverity.ERROR && isGodotSupportInstalled && RiderGodotSupportPluginUtil.isGodotSupportLSPRunning(project))
        return this.newSilentAnnotation(HighlightSeverity.INFORMATION)
    return this.newAnnotation(severity, message)
}
// TODO: We can either pass raw strings here or pass in the names of the resources and convert it here
