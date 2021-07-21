package gdscript.psi.utils

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.openapi.project.Project
import gdscript.competion.staticLoader.StaticClassLoader
import gdscript.index.Indices
import java.util.stream.Stream

object PsiGdFileUtil {
    fun customs(project: Project): Collection<String> =
            Indices.keys(Indices.FILE_NAMES, project)

    fun customs(parameters: CompletionParameters): Collection<String> =
            customs(parameters.position.project);

    fun statics(): Collection<String> =
            StaticClassLoader.getClasses().keys;

    fun all(project: Project): Stream<String> =
            Stream.concat(
                    statics().stream(),
                    customs(project).stream()
            )

    fun myFilename(parameters: CompletionParameters): String {
        val name = parameters.originalFile.name;
        return name.substring(0, name.length - 3)
    }

}
