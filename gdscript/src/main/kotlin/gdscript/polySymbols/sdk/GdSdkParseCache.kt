package gdscript.polySymbols.sdk

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import gdscript.library.GdDocClassesFoldersService
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser
import java.util.concurrent.ConcurrentHashMap

/**
 * Project-scoped cache of parsed SDK XML data, keyed by [VirtualFile] URL.
 */
@Service(Service.Level.PROJECT)
class GdSdkParseCache(private val project: Project) {

    companion object {
        fun getInstance(project: Project): GdSdkParseCache = project.service()
    }

    private val cache = ConcurrentHashMap<String, CachedValue<GdSdkData.ClassData?>>()

    fun getOrParseClassData(sourceFile: VirtualFile): GdSdkData.ClassData? {
        if (!sourceFile.isValid) {
            cache.remove(sourceFile.url)
            return null
        }

        val cv = cache.computeIfAbsent(sourceFile.url) {
            CachedValuesManager.getManager(project).createCachedValue(
                {
                    val parsed = if (sourceFile.isValid) GdSdkXmlParser.parseClass(sourceFile) else null
                    CachedValueProvider.Result.create(
                        parsed,
                        GdDocClassesFoldersService.getInstance(project).modificationTracker
                    )
                },
                false
            )
        }

        return cv.value
    }
}