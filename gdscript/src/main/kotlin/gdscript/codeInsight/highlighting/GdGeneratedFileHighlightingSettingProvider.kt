package gdscript.codeInsight.highlighting

import com.intellij.codeInsight.daemon.impl.analysis.DefaultHighlightingSettingProvider
import com.intellij.codeInsight.daemon.impl.analysis.FileHighlightingSetting
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

private const val GENERATED_SUFFIX = ".generated.gd"

/**
 * Disables error and warning highlighting for generated GDScript files.
 */
class GdGeneratedFileHighlightingSettingProvider : DefaultHighlightingSettingProvider(), DumbAware {
    override fun getDefaultSetting(project: Project, file: VirtualFile): FileHighlightingSetting? =
        if (file.name.endsWith(GENERATED_SUFFIX, ignoreCase = true)) FileHighlightingSetting.SKIP_HIGHLIGHTING else null
}
