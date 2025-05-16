package gdscript.structureView

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider

class GdFileNesting : ProjectViewNestingRulesProvider {

    // TODO no idea who to create some wildcards/pattern matches so for now just listing some suffixes
    val exts = arrayOf(
        "svg",
        "png",
        "jpg",
        "jpeg",
    )

    override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
        exts.forEach { consumer.addNestingRule(".$it", ".$it.import") }
    }

}
