package project.psi.util

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.descendantsOfType
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import gdscript.index.impl.GdFileResIndex
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.getMainProjectBasePath
import project.index.impl.ProjectSectionIndex
import project.psi.ProjectData
import project.psi.model.GdAutoload
import tscn.TscnFileType
import tscn.psi.TscnNodeHeader
import tscn.psi.utils.TscnNodeUtil
import tscn.psi.utils.TscnNodeUtil.getScriptResource
import kotlin.io.path.pathString

@Service(Service.Level.PROJECT)
class ProjectAutoloadCache(private val project: Project) : Disposable {

    // -------------- Globals(Project Setting) -------------------
    // Name            Path                       Global Variable
    // MySingleton     res://my_singleton.gd      [v] Enable
    // AutoloadScene   res://autoload_scene.tscn  [ ] Enable
    // -----------------------------------------------------------
    // {key: MySingleton,   element: GdScript file, enable: true}
    // {key: AutoloadScene, element: GdScript file, enable: false}
    data class AutoloadInfo(val key: String, val element: PsiFile, val enabled: Boolean)

    private data class Snapshot(
        val byAlias: Map<String, AutoloadInfo>, // gdscript file enabled or disabled on autoload e.g. { "MySingleton": { key: "MySingleton", element: GdScript File, enabled: true }, ... }
        val globals: List<GdAutoload>,          // gdscript file enabled             on autoload e.g. [ 0: { key: "MySingleton", element: GdScript File }, ... ]
        val watchedFiles: Set<VirtualFile>,     // tscn     file enabled or disabled on autoload e.g. [ { "file://D:/godot-projects/temp/autoload_scene.tscn" }, ... ]
    )

    companion object {
        fun getInstance(project: Project): ProjectAutoloadCache = project.service()
    }

    @Volatile
    private var snapshot = Snapshot(emptyMap(), emptyList(), emptySet())

    init {
        refresh()
        subscribeToChangesPsi()
    }

    override fun dispose() {
    }

    private fun refresh() {
        if (project.isDisposed) return
        if (DumbService.isDumb(project)) {
            DumbService.getInstance(project).runWhenSmart { refresh() }
            return
        }
        snapshot = ReadAction.compute<Snapshot, RuntimeException> { buildSnapshot() }
    }

    private fun refreshAfterCommit() {
        if (project.isDisposed) return
        PsiDocumentManager.getInstance(project).performWhenAllCommitted {
            refresh()
        }
    }

    private fun subscribeToChangesPsi() {
        val psiManager = PsiManager.getInstance(project)
        psiManager.addPsiTreeChangeListener(object : PsiTreeChangeAdapter() {
            override fun childrenChanged(event: PsiTreeChangeEvent) {
                val vf = event.file?.virtualFile ?: return
                if (!isProjectFileDirty(vf.path) && !isTscnContextDirty(vf)) return
                refreshAfterCommit()
            }
        }, this)
    }

    private fun isTscnContextDirty(file: VirtualFile): Boolean {
        return snapshot.watchedFiles.contains(file)
    }

    private fun isProjectFileDirty(path: String): Boolean {
        val basePath = project.getMainProjectBasePath()
        if (basePath != null) {
            val projectFilePath = basePath.resolve("project.godot").pathString
            if (path == projectFilePath) return true
        }
        return path.endsWith("/project.godot") || path.endsWith("\\project.godot")
    }

    private fun buildSnapshot(): Snapshot {
        val byAlias = mutableMapOf<String, AutoloadInfo>()
        val globals = mutableListOf<GdAutoload>()
        val watchedFiles = mutableSetOf<VirtualFile>()

        val section = ProjectSectionIndex.INSTANCE.getGlobally(ProjectSectionIndex.AUTOLOAD_SECTION, project)
            .firstOrNull() ?: return Snapshot(byAlias, globals, watchedFiles)

        val entries = PsiTreeUtil.getStubChildrenOfTypeAsList(section, ProjectData::class.java)
        entries.forEach { data ->
            /* e.g.
             data.value = "*res://my_singleton.gd"
             parsed     = { enabled:true, path: "res://my_singleton.gd" }
             file       = { "file://D:/godot-projects/temp/my_singleton.gd" }
             autoload   = { key: "MySingleton", element: GdScript File }
             info       = { key: "MySingleton", element: GdScript File, enabled: true }
             */
            val parsed = parseAutoloadValue(data.value) ?: return@forEach
            val resource = GdFileResIndex.getFiles(parsed.path, project).firstOrNull() ?: return@forEach
            val file = resource.getPsiFile(project) ?: return@forEach
            if (file.fileType is TscnFileType) {
                file.virtualFile?.let { watchedFiles.add(it) }
            }

            val autoload = when {
                file.fileType is GdFileType -> GdAutoload(data.key, file)
                file.fileType is TscnFileType -> findGdFileForTscnFile(project, data.key, file)
                else -> null
            } ?: return@forEach

            val info = AutoloadInfo(data.key, autoload.element, parsed.enabled)
            byAlias[data.key] = info
            if (parsed.enabled) globals.add(autoload)
        }

        return Snapshot(byAlias, globals, watchedFiles)
    }

    private fun parseAutoloadValue(raw: String): ParsedAutoloadValue? {
        /* e.g.
         raw      = "*res://my_singleton.gd"
         unquoted = *res://my_singleton.gd
         enabled  = true
         path     = res://my_singleton.gd
         */
        val unquoted = raw.trim('"')
        if (unquoted.isEmpty()) return null
        val enabled = unquoted.startsWith("*")
        val path = if (enabled) unquoted.substring(1) else unquoted
        if (path.isBlank()) return null
        return ParsedAutoloadValue(enabled, path)
    }

    private fun findGdFileForTscnFile(project: Project, key: String, tscnFile: PsiFile): GdAutoload? {
        tscnFile.descendantsOfType<TscnNodeHeader>().forEach { header ->
            if (TscnNodeUtil.hasScript(header)) {
                val resource = GdFileResIndex.getFiles(getScriptResource(header), project).firstOrNull()
                val gdFile = resource?.getPsiFile(project)
                if (gdFile != null && gdFile.fileType is GdFileType) return GdAutoload(key, gdFile)
            }
        }
        return null
    }

    private data class ParsedAutoloadValue(val enabled: Boolean, val path: String)

    fun getGlobals(): List<GdAutoload> = snapshot.globals

    fun getAutoloadInfoByAlias(name: String): AutoloadInfo? = snapshot.byAlias[name]

    fun forceRefresh() {
        refresh()
    }
}
