package gdscript.statistics

import com.intellij.execution.impl.statistics.RunConfigurationTypeDefs
import com.intellij.execution.impl.statistics.RunConfigurationTypeUsagesCollector
import com.intellij.internal.statistic.eventLog.events.EventField
import com.intellij.internal.statistic.eventLog.events.EventFields
import com.intellij.internal.statistic.service.fus.collectors.FeatureUsageCollectorExtension
import gdscript.dap.GdScriptRunConfigurationKind

internal object GdScriptRunConfigurationUsageData {
    val CONFIGURATION_KIND = EventFields.Enum(
        "gdscript_configuration_kind", GdScriptRunConfigurationKind::class.java
    ) { it.name.lowercase() }
}

internal class GdScriptConfigurationTypeCollectorExtension : FeatureUsageCollectorExtension {
    override fun getGroupId(): String = RunConfigurationTypeDefs.TYPE_USAGES_GROUP_ID

    override fun getEventId(): String = RunConfigurationTypeUsagesCollector.CONFIGURED_IN_PROJECT

    override fun getExtensionFields(): List<EventField<*>> = listOf(GdScriptRunConfigurationUsageData.CONFIGURATION_KIND)
}

internal class GdScriptConfigurationUsageCollectorExtension : FeatureUsageCollectorExtension {
    override fun getGroupId(): String = RunConfigurationTypeDefs.TRIGGER_USAGES_GROUP_ID

    override fun getEventId(): String = "started"

    override fun getExtensionFields(): List<EventField<*>> = listOf(GdScriptRunConfigurationUsageData.CONFIGURATION_KIND)
}
