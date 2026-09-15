using JetBrains.Annotations;
using JetBrains.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [ProjectFileTypeDefinition(Name)]
    public class GdScriptProjectFileType : UnknownProjectFileType
    {
        public new const string Name = "GDSCRIPT";
        public const string GD_EXTENSION = ".gd";

        [UsedImplicitly] public new static GdScriptProjectFileType? Instance { get; private set; }

        private GdScriptProjectFileType() : base(Name, "GDScript", new[] { GD_EXTENSION }) { }
    }
}
