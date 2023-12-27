package gdscript.utils

import com.intellij.openapi.project.Project
import config.index.impl.GdConfigAnnotationDataIndex
import gdscript.model.GdAnnotation
import gdscript.psi.GdAnnotationTl

object GdAnnotationUtil {

    fun get(annotation: GdAnnotationTl): GdAnnotation? {
        return get(annotation.annotationType.text, annotation.project)
    }

    fun get(name: String, project: Project): GdAnnotation? {
        val annotation = GdConfigAnnotationDataIndex.INSTANCE.getGlobally(name.trimStart('@'), project)
            .firstOrNull() ?: return null

        return GdAnnotation(annotation.isVariadic, annotation.requiredCount(), annotation.params)
    }

}
