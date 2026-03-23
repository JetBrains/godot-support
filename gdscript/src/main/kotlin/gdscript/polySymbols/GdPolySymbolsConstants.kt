package gdscript.polySymbols

import gdscript.GdKeywords

object GdPolySymbolsConstants {

    /**
     * If true, poly symbols will be loaded and used.
     * If false, poly symbols won't be used and everything will work with PSI as before.
     * If you are enabling poly symbols in a project, make sure to erase the libraries/ folder in the .idea folder to not load the old sdk.
     */
    val USING_POLY_SYMBOLS: Boolean = true // TODO delete after deleting the whole PSI implementation


    // apparently there's both @GDScript and GDScript
    val GLOBAL_CLASSES: List<String> = listOf( GdKeywords.GLOBAL_SCOPE, GdKeywords.GLOBAL_GD_SCRIPT, "GDScript")

    /**
     * Annotations are only found in the @GdScript.xml file
     */
    const val ANNOTATIONS_FILE_NAME: String = GdKeywords.GLOBAL_GD_SCRIPT
}
