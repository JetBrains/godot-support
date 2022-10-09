extends RefCounted
#brief Base class for the implementation of core resource importers.
#desc This is the base class for the resource importers implemented in core. To implement your own resource importers using editor plugins, see [EditorImportPlugin].
class_name ResourceImporter

#desc The default import order.
const IMPORT_ORDER_DEFAULT = 0;

#desc The import order for scenes, which ensures scenes are imported [i]after[/i] all other core resources such as textures. Custom importers should generally have an import order lower than [code]100[/code] to avoid issues when importing scenes that rely on custom resources.
const IMPORT_ORDER_SCENE = 100;





