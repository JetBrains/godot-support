package com.jetbrains.rider.plugins.godot.projectView

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider


class GodotNestingRulesProvider: ProjectViewNestingRulesProvider {
    override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
        consumer.addNestingRule(".cs", ".cs.uid")
        consumer.addNestingRule(".gd", ".gd.uid")
        consumer.addNestingRule(".gdshader", ".gdshader.uid")
    }
}