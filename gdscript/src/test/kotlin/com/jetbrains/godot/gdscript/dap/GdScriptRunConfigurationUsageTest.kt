package com.jetbrains.godot.gdscript.dap

import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.internal.statistic.service.fus.collectors.FeatureUsageCollectorExtension
import com.intellij.platform.dap.DapStartRequest
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import gdscript.dap.GdScriptConfigurationType
import gdscript.dap.GdScriptRunConfiguration
import gdscript.dap.GdScriptRunFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

@TestApplication
class GdScriptRunConfigurationUsageTest {
    companion object {
        private val projectFixture = projectFixture()
    }

    @ParameterizedTest
    @CsvSource(
        "Launch, main, player",
        "Launch, current, current_scene",
        "Launch, res://private/scene.tscn, player",
        "Attach, main, running_session",
        "Attach, current, running_session",
        "Attach, res://private/scene.tscn, running_session",
    )
    fun testConfigurationKind(request: String, scene: String, expected: String) {
        val configuration = createConfiguration()
        configuration.json = """{"request":"$request","scene":"$scene"}"""
        assertUsageData(configuration, expected)
    }

    @Test
    fun testDefaultConfiguration() {
        val configuration = createConfiguration()
        assertEquals(GdScriptRunFactory.DEFAULT_FULL_JSON, configuration.json)
        assertUsageData(configuration, "player")
    }

    @ParameterizedTest
    @CsvSource(
        "run.configuration.type, configured.in.project",
        "run.configuration.exec, started"
    )
    fun testFieldRegistered(groupId: String, eventId: String) {
        val fields = FeatureUsageCollectorExtension.EP_NAME.extensionList
            .filter { it.groupId == groupId && it.eventId == eventId }
            .flatMap { it.extensionFields }
        assertTrue(fields.any { it.name == "gdscript_configuration_kind" })
    }

    @Test
    fun testChangesUseSettingsRatherThanName() {
        val configuration = createConfiguration()
        configuration.json = """{"scene":"current"}"""
        assertUsageData(configuration, "current_scene")
        configuration.json = """{"request":"Attach","scene":"current"}"""
        assertUsageData(configuration, "running_session")
        configuration.json = """{"scene":"main"}"""
        assertUsageData(configuration, "player")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "{}", "invalid JSON", "[]", "null", """{"scene":null}"""])
    fun testIncompleteSettings(json: String) {
        val configuration = createConfiguration()
        configuration.json = json
        assertUsageData(configuration, "player")
    }

    @Test
    fun testNonStringRequestUsesLaunchDefault() {
        val configuration = createConfiguration()
        configuration.json = """{"request":42,"scene":"current"}"""
        assertEquals(DapStartRequest.Launch, configuration.structured.request)
        assertUsageData(configuration, "current_scene")
    }

    @Test
    fun testUnsupportedRequestOmitsConfigurationKind() {
        val configuration = createConfiguration()
        configuration.json = """{"request":"Restart","scene":"current"}"""
        assertThrows(IllegalArgumentException::class.java) { configuration.structured }
        assertTrue(configuration.getAdditionalUsageData().none { it.field.name == "gdscript_configuration_kind" })
    }

    private fun createConfiguration(): GdScriptRunConfiguration {
        val factory = GdScriptConfigurationType().factory
        return GdScriptRunConfiguration("Private configuration name", projectFixture.get(), factory)
    }

    private fun assertUsageData(configuration: GdScriptRunConfiguration, expected: String) {
        val data = configuration.getAdditionalUsageData().single()
        assertEquals("gdscript_configuration_kind", data.field.name)
        val usageData = FeatureUsageData("FUS")
        data.addData(usageData)
        assertEquals(mapOf("gdscript_configuration_kind" to expected), usageData.build())
    }
}
