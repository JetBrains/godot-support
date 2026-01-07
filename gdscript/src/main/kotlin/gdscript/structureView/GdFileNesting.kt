package gdscript.structureView

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider

class GdFileNesting : ProjectViewNestingRulesProvider {

    // TODO no idea how to create some wildcards/pattern matches
    override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
        /* @tool
        extends EditorScript

        func _run():
        var extensions := ResourceLoader.get_recognized_extensions_for_type("Resource")
        print(extensions)
        */

        arrayOf(
            "tscn", "tres", "anim", "atlastex", "curvetex", "dpitex", "fontdata", "lmbake", "material", "mesh", "meshlib", "meshtex", "mp3str", "multimesh", "occ", "oggvorbisstr", "phymat", "repl", "res", "sample", "scn", "shape", "stylebox", "tex", "theme", "translation", "png", "bmp", "hdr", "jpg", "jpeg", "svg", "tga", "exr", "webp", "ttf", "ttc", "otf", "otc", "woff", "woff2", "pfb", "pfm", "font", "fnt", "csv", "wav", "obj", "glsl", "dae", "escn", "fbx", "gltf", "glb", "blend", "mp3", "ogg"
        ).forEach { consumer.addNestingRule(".$it", ".$it.import") }

        // keep in sync with GodotNestingRulesProvider
        arrayOf("cs", "gd", "gdshader", "gdextension").forEach {
            consumer.addNestingRule(".$it", ".$it.uid")
        }
    }
}
