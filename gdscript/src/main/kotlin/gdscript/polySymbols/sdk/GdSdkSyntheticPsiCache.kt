package gdscript.polySymbols.sdk

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.polySymbols.sdk.xml.XmlToGd
import gdscript.psi.GdFile
import java.lang.ref.SoftReference
import java.util.concurrent.ConcurrentHashMap

/**
 * Project-scoped cache of synthetic GDScript [GdFile]s generated from SDK class data on GoTo navigation.
 */
@Service(Service.Level.PROJECT)
class GdSdkSyntheticPsiCache(private val project: Project) {

    companion object {
        fun getInstance(project: Project): GdSdkSyntheticPsiCache = project.service()
    }

    private class FileEntry(val classData: GdSdkData.ClassData, file: GdFile) {
        val ref: SoftReference<GdFile> = SoftReference(file)
    }

    private val syntheticFileCache = ConcurrentHashMap<String, FileEntry>()

    fun getOrCreateSyntheticFile(classId: String, classData: GdSdkData.ClassData): GdFile {
        syntheticFileCache[classId]?.let { entry ->
            val cached = entry.ref.get()
            if (cached != null && cached.isValid && entry.classData === classData) return cached
        }

        val syntheticFile = createFile(classId, classData)
        syntheticFileCache.compute(classId) { _, existing ->
            val cached = existing?.ref?.get()
            if (cached != null && cached.isValid && existing.classData === classData) existing
            else FileEntry(classData, syntheticFile)
        }

        return syntheticFile
    }

    private fun createFile(classId: String, classData: GdSdkData.ClassData): GdFile {
        val text = XmlToGd().convert(classData)
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText("$classId.gd", GdLanguage, text) as GdFile
        file.putUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY, classId)
        return file
    }
}
