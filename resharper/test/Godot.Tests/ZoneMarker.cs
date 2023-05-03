using JetBrains.Application.BuildScript.Application.Zones;

namespace JetBrains.ReSharper.Plugins.Godot.Tests
{
    [ZoneMarker]
    public class ZoneMarker : IRequire<IGodotTestZone>
    {
    }
}