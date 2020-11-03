using System;
using JetBrains.ProjectModel.Properties;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel.Flavours
{
    [ProjectFlavor]
    public class GodotProjectFlavor : IProjectFlavor
    {
        public static Guid GodotProjectFlavorGuid = new Guid("{8f3e2df0-c35c-4265-82fc-bea011f4a7ed}");

        public Guid Guid => GodotProjectFlavorGuid;
        public string FlavorName => "Godot Project";
    }
}