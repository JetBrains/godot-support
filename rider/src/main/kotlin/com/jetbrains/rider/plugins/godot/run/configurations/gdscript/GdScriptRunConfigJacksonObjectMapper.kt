package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class GdScriptRunConfigJacksonObjectMapper {
    companion object {
        fun getService(project:Project): GdScriptRunConfigJacksonObjectMapper = project.getService(GdScriptRunConfigJacksonObjectMapper::class.java)
    }
    val mapper: ObjectMapper = jacksonObjectMapper().apply {
        configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true)
        configure(JsonParser.Feature.ALLOW_COMMENTS, true)
    }
}