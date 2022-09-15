using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.RdBackend.Common.Env;
using JetBrains.ReSharper.UnitTestFramework;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [ZoneMarker]
    public class ZoneMarker : IRequire<IUnitTestingZone>, IRequire<IResharperHostCoreFeatureZone>
    {
    }
}