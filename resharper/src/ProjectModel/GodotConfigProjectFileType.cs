using JetBrains.Annotations;
using JetBrains.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [ProjectFileTypeDefinition(Name)]
    public class GodotConfigProjectFileType : UnknownProjectFileType
    {
        public new const string Name = "GODOT_CONFIG";
        public const string GODOT_EXTENSION = ".godot";
        public const string GDCONF_EXTENSION = ".gdconf";

        [UsedImplicitly] public new static GodotConfigProjectFileType? Instance { get; private set; }

        private GodotConfigProjectFileType()
            : base(Name, "Godot Config", new[] { GODOT_EXTENSION, GDCONF_EXTENSION }) { }
    }
}
