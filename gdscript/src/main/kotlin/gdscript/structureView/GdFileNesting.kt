package gdscript.structureView

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider

class GdFileNesting : ProjectViewNestingRulesProvider {

    // TODO no idea how to create some wildcards/pattern matches
    override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
        arrayOf(
            "svg",
            "png",
            "jpg",
            "jpeg",
            "webp",
            "fbx",
            "glb",
            "gltf"
        ).forEach { consumer.addNestingRule(".$it", ".$it.import") }

        // keep in sync with GodotNestingRulesProvider
        arrayOf("cs", "gd", "gdshader", "gdextension").forEach {
            consumer.addNestingRule(".$it", ".$it.uid")
        }
    }
}
