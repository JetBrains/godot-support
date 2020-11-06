using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.ReSharper.UnitTestFramework;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [ZoneDefinition]
    [ZoneDefinitionConfigurableFeature("Exploration for Godot Tests", "", false)]
    public interface IGodotUnitTestingZone : IZone, IRequire<IUnitTestingZone>
    {
    }
}