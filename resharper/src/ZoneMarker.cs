using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Host.Product;
using JetBrains.ReSharper.Resources.Shell;
using JetBrains.ReSharper.UnitTestFramework;

namespace JetBrains.ReSharper.Plugins.Godot
{
    [ZoneMarker]
    public class ZoneMarker : IRequire<IUnitTestingZone>, IRequire<IRiderProductEnvironmentZone>
    {
    }
}