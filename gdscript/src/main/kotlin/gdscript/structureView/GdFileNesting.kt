package gdscript.structureView

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider

class GdFileNesting : ProjectViewNestingRulesProvider {

    override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
        consumer.addNestingRule(".gd", ".tscn")
    }

}
