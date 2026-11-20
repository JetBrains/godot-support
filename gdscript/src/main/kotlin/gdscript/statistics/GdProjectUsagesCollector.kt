@file:Suppress("UnstableApiUsage")

package gdscript.statistics

import com.intellij.internal.statistic.beans.MetricEvent
import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.eventLog.events.EventFields
import com.intellij.internal.statistic.service.fus.collectors.ProjectUsagesCollector
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.GdProjectGodotService

class GdProjectUsagesCollector : ProjectUsagesCollector() {

    private val GROUP = EventLogGroup("godot.gdscript.plugin.project", 2)
    private val GODOT_VERSION = EventFields.Version
    private val PROJECT = GROUP.registerEvent("project", GODOT_VERSION)

    override fun getGroup(): EventLogGroup = GROUP

    override fun getMetrics(project: Project): Set<MetricEvent> {
        val version = GdProjectGodotService.getInstance(project).godotVersion ?: return emptySet()
        // Keep the "major.minor" form of config/features, for example "4.3", not "4.3.0".
        return setOf(PROJECT.metric("${version.major}.${version.minor}"))
    }
}
