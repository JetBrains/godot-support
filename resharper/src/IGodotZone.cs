using JetBrains.Application.BuildScript.Application.Zones;

namespace JetBrains.ReSharper.Plugins.Godot
{
    [ZoneDefinition]
    [ZoneDefinitionConfigurableFeature("Godot", "Provides support for Godot Engine.", IsInProductSection: false)]
    public interface IGodotZone: IZone
    {
    }
}