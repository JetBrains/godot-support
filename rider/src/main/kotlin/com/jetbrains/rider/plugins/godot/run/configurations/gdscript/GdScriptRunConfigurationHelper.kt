package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.platform.dap.DapStartRequest

object GdScriptRunConfigurationHelper {
    fun parseArgumentsToMap(text: String): Map<String, Any?> {
        if (text.isBlank()) return emptyMap()
        val json = text.trim()
        return try {
            val mapper = jacksonObjectMapper()
            mapper.configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true)
            @Suppress("UNCHECKED_CAST")
            val map: Map<String, Any?> = mapper.readValue(json, mutableMapOf<String, Any?>().javaClass)
            map
        } catch (_: Exception) {
            emptyMap()
        }
    }

    fun mapToPrettyJson(map: Map<String, Any?>): String {
        if (map.isEmpty()) return GdScriptRunFactory.DEFAULT_EMPTY_JSON
        val mapper = jacksonObjectMapper()
        return try {
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map)
        } catch (e: Exception) {
            thisLogger().error("Failed to serialize map to JSON: $map", e)
            GdScriptRunFactory.DEFAULT_EMPTY_JSON
        }
    }

    fun parse(text: String): GdScriptStructuredArguments {
        val map = parseArgumentsToMap(text)
        val req = (map["request"] as? String)?.let { DapStartRequest.valueOf(it) } ?: DapStartRequest.Launch
        val port = when (val v = map["debugServer"]) {
            is Number -> v.toInt()
            is String -> v.toIntOrNull() ?: GdScriptRunFactory.DEFAULT_PORT
            else -> GdScriptRunFactory.DEFAULT_PORT
        }
        val scene = (map["scene"] as? String) ?: GdScriptRunFactory.DEFAULT_SCENE
        // Remove known keys and reconstruct the rest
        val rest = map.toMutableMap().apply {
            remove("request")
            remove("debugServer")
            remove("scene")
        }
        val restText = mapToPrettyJson(rest)
        return GdScriptStructuredArguments(req, port, restText, scene)
    }

    fun serialize(structured: GdScriptStructuredArguments): String {
        val map = mutableMapOf<String, Any?>(
            "request" to structured.request,
            "debugServer" to structured.debugServerPort,
            "scene" to structured.scene
        )
        val rest = parseArgumentsToMap(structured.remainingArguments)
        rest.forEach { (k, v) -> if (k !in map.keys) map[k] = v }
        return mapToPrettyJson(map)
    }
}
