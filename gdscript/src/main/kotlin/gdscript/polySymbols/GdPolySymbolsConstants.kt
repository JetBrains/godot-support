package gdscript.polySymbols

import gdscript.GdKeywords

object GdPolySymbolsConstants {

    // apparently there's both @GDScript and GDScript
    val GLOBAL_CLASSES: List<String> = listOf( GdKeywords.GLOBAL_SCOPE, GdKeywords.GLOBAL_GD_SCRIPT, "GDScript")

    /**
     * Annotations are only found in the @GdScript.xml file
     */
    const val ANNOTATIONS_FILE_NAME: String = GdKeywords.GLOBAL_GD_SCRIPT
}
