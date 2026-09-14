package gdscript.polySymbols.sdk

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import gdscript.library.GdSdkDocsTracker
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser
import java.util.concurrent.ConcurrentHashMap

/**
 * Project-scoped cache of parsed SDK XML data, keyed by [VirtualFile] URL.
 *
 * The cached closure resolves the URL again on every computation. A binding to one [VirtualFile] instance
 * would keep a stale result after a delete and a create of the same path.
 *
 * A transient read failure stays in its own cache entry, and the entry goes away at once. A caller that
 * already holds that entry still reads the failure once. The next caller parses the file again.
 */
@Service(Service.Level.PROJECT)
class GdSdkParseCache(private val project: Project) {

    companion object {
        fun getInstance(project: Project): GdSdkParseCache = project.service()
    }

    private val cache = ConcurrentHashMap<String, CachedValue<GdSdkXmlParser.ParseResult<GdSdkData.ClassData>>>()

    fun getOrParseClassData(sourceFile: VirtualFile): GdSdkData.ClassData? {
        if (!sourceFile.isValid) {
            cache.remove(sourceFile.url)
            return null
        }

        val url = sourceFile.url
        val cachedValue = cache.computeIfAbsent(url) { fileUrl ->
            CachedValuesManager.getManager(project).createCachedValue(
                {
                    val file = VirtualFileManager.getInstance().findFileByUrl(fileUrl)
                    val parsed = when {
                        file == null || !file.isValid -> GdSdkXmlParser.ParseResult.ReadFailure
                        else -> GdSdkXmlParser.parseClassResult(file)
                    }
                    CachedValueProvider.Result.create(
                        parsed,
                        GdSdkDocsTracker.getInstance(project),
                    )
                },
                false
            )
        }

        val result = cachedValue.value
        // A read failure can succeed later, so the entry goes away and the next request parses the file again.
        // The removal names the value too, so it cannot drop a good entry that another thread just stored.
        if (result is GdSdkXmlParser.ParseResult.ReadFailure) cache.remove(url, cachedValue)
        return result.valueOrNull()
    }
}
