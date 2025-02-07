package com.jetbrains.rider.plugins.godot.projectView

import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandListener
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.BaseProjectDirectories.Companion.getBaseDirectories
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.vfs.newvfs.events.VFileMoveEvent
import com.intellij.openapi.vfs.newvfs.events.VFilePropertyChangeEvent
import com.intellij.openapi.vfs.readBytes
import com.intellij.util.PathUtil
import com.intellij.util.application
import com.intellij.util.concurrency.annotations.RequiresEdt
import com.jetbrains.rd.platform.util.getLogger
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.reactive.adviseUntil
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService
import com.jetbrains.rider.projectView.VfsBackendRequester
import org.jetbrains.annotations.Nls
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

// for file outside csproj, just nesting rules are enough
// but users may decide to include some files in the csproj
// when files are in the project model and their uid is not, we need to take the same approach as Unity plugin

class GodotUidTrackerInitializer : ProjectActivity {
    override suspend fun execute(project: Project) {
        val lifetime = GodotProjectLifetimeService.getLifetime(project)
        val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
        godotDiscoverer.godotDescriptor.adviseUntil(lifetime) {
            if (it != null && !it.isPureGdScriptProject) {
                GodotUidTracker.getInstance().register(project)
                return@adviseUntil true
            }
            false
        }
    }
}

@Service(Service.Level.APP)
class GodotUidTracker : VfsBackendRequester {

    private val lock = Object()
    private var projects = mutableSetOf<Project>()

    companion object {
        fun getInstance(): GodotUidTracker = service<GodotUidTracker>()
        private val logger = getLogger<GodotUidTracker>()
    }

    fun register(project: Project) {
        val lifetime = GodotProjectLifetimeService.getLifetime(project)
        lifetime.bracketIfAlive({ synchronized(lock) { projects.add(project) } },
                                { synchronized(lock) { projects.remove(project) } })
    }

    private val actionsPerProject = mutableMapOf<Project, UidActionList>()

    fun onEvent(events: MutableList<out VFileEvent>) {

        val validProjects = synchronized(lock) { mutableListOf<Project>().also { it.addAll(projects) } }.filter {
            !isUndoRedoInProgress(it) && !it.isDisposed
        }.toList()

        for (event in events) {
            if (!isValidEvent(event)) continue
            for (project in validProjects) {
                if (isApplicableForProject(event, project)) {
                    val actions = getOrCreate(project)
                    if (isUidFile(event)) // Collect modified uid files at first (LocalHistory or git or something else)
                        actions.addInitialSetOfChangedUidFiles(Paths.get(event.path))
                    else {
                        try {
                            when (event) {
                                is VFileDeleteEvent -> {
                                    val uidFile = getUidFile(event.path) ?: continue
                                    actions.add(uidFile, project) {
                                        val fileToDelete = VfsUtil.findFile(uidFile, true)
                                        if (fileToDelete != null) {
                                            fileToDelete.readBytes() // Preload file content into VFS to allow local history to restore it on undo operation
                                            fileToDelete.delete(this)
                                        }
                                    }
                                }
                                is VFileMoveEvent -> {
                                    val uidFile = getUidFile(event.oldPath) ?: continue
                                    actions.add(uidFile, project) { VfsUtil.findFile(uidFile, true)?.move(this, event.newParent)
                                    }
                                }
                                is VFilePropertyChangeEvent -> {
                                    if (!event.isRename) continue
                                    val uidFile = getUidFile(event.oldPath) ?: continue
                                    actions.add(uidFile, project) {
                                        val target = getUidFileName(event.newValue as String)
                                        val origin = VfsUtil.findFile(uidFile, true)
                                        val conflictingUid = origin?.parent?.findChild(target)
                                        if (conflictingUid != null) {
                                            logger.warn("Removing conflicting uid $conflictingUid")
                                            conflictingUid.delete(this)
                                        }
                                        origin?.rename(this, target)
                                    }
                                }
                            }
                        }
                        catch (t: Throwable) {
                            logger.error(t)
                            continue
                        }
                    }
                }
            }
        }
    }

    private fun getOrCreate(project: Project): UidActionList {
        var actions = actionsPerProject[project]
        if (actions == null) {
            actions = UidActionList(project)
            actionsPerProject.addUnique(GodotProjectLifetimeService.getLifetime(project), project, actions)
        }
        return actions
    }

    private fun isValidEvent(event: VFileEvent): Boolean {
        if (event.isFromRefresh) return false
        if (event.fileSystem !is LocalFileSystem) return false
        return CommandProcessor.getInstance().currentCommand != null
    }

    private fun isUndoRedoInProgress(project: Project): Boolean {
        return UndoManager.getInstance(project).isUndoOrRedoInProgress
    }

    private fun isUidFile(event: VFileEvent): Boolean {
        val extension = event.file?.extension ?: PathUtil.getFileExtension(event.path)
        return "uid".equals(extension, true)
    }

    @RequiresEdt
    private fun isApplicableForProject(event: VFileEvent, project: Project): Boolean {
        val file = event.file ?: return false
        return VfsUtilCore.isUnder(file, project.getBaseDirectories())
    }

    private fun getUidFile(path: String?): Path? {
        path ?: return null
        val file = Paths.get(path)
        val uidFileName = getUidFileName(file.name)
        return file.parent.resolve(uidFileName)
    }

    private fun getUidFileName(fileName: String) = "$fileName.uid"

    private class UidActionList(project: Project) {

        init {
            val connection = project.messageBus.connect(GodotProjectLifetimeService.getLifetime(project).createNestedDisposable())
            connection.subscribe(CommandListener.TOPIC, object : CommandListener {
                override fun beforeCommandFinished(event: CommandEvent) {
                    // apply all changes from Map<Runnable, List<Path>> and add our changes to uid files

                    execute()
                    clear()

                    super.beforeCommandFinished(event)
                }
            })
        }

        @RequiresEdt
        private fun clear() {
            changedUidFiles.clear()
            actions.clear()
        }

        private val changedUidFiles = HashSet<Path>()
        private val actions = mutableListOf<UidAction>()

        private var nextGroupIdIndex = 0

        fun addInitialSetOfChangedUidFiles(path: Path) {
            changedUidFiles.add(path)
        }

        fun add(uidFile: Path, project: Project, action: () -> Unit) {
            if (changedUidFiles.contains(uidFile)) return
            actions.add(UidAction(uidFile, project, action))
        }

        fun execute() {
            if (actions.isEmpty()) return

            val commandProcessor = CommandProcessor.getInstance()
            var groupId = commandProcessor.currentCommandGroupId
            if (groupId == null) {
                groupId = UidGroupId(nextGroupIdIndex++)
                commandProcessor.currentCommandGroupId = groupId
            }

            commandProcessor.allowMergeGlobalCommands {
                actions.forEach {
                    commandProcessor.executeCommand(it.project, {
                        application.runWriteAction {
                            if (!changedUidFiles.contains(it.uidFile)) // the uid file got restored by LocalHistory or git or maybe undo
                                it.execute()
                        }
                    }, getCommandName(), groupId)
                }
            }
        }

        @Nls
        fun getCommandName(): String {
            return if (actions.count() == 1)
                GodotPluginBundle.message("process.one.uid.file", actions.single().uidFile.name)
            else
                GodotPluginBundle.message("process.several.uid.files", actions.count())
        }
    }

    private class UidAction(val uidFile: Path, val project: Project, private val action: () -> Unit) {
        fun execute() {
            try {
                action()
            }
            catch (ex: Throwable) {
                logger.error(ex)
            }
        }
    }

    private class UidGroupId(val index: Int) {
        override fun toString() = "UidGroupId$index"
    }
}

class UidTrackerListener : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        GodotUidTracker.getInstance().onEvent(events)
    }
}