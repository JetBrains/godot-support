using JetBrains.Annotations;
using JetBrains.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [ProjectFileTypeDefinition(Name)]
    public class GodotResourceProjectFileType : UnknownProjectFileType
    {
        public new const string Name = "GODOT_RESOURCE";
        public const string TSCN_EXTENSION = ".tscn";
        public const string TRES_EXTENSION = ".tres";
        public const string IMPORT_EXTENSION = ".import";

        [UsedImplicitly] public new static GodotResourceProjectFileType? Instance { get; private set; }

        private GodotResourceProjectFileType()
            : base(Name, "Godot Resource", new[] { TSCN_EXTENSION, TRES_EXTENSION, IMPORT_EXTENSION }) { }
    }
}
