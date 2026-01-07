package com.jetbrains.rider.plugins.godot.projectView

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider


class GodotNestingRulesProvider: ProjectViewNestingRulesProvider {
    override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
        // keep in sync with GdFileNesting
        arrayOf("cs", "gd", "gdshader", "gdextension").forEach {
            consumer.addNestingRule(".$it", ".$it.uid")
        }
    }
}