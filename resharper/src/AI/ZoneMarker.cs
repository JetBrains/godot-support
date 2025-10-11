using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.ReSharper.Feature.Services.AI;
using JetBrains.Rider.Backend.Env;
using JetBrains.Rider.Backend.Product;

namespace JetBrains.ReSharper.Plugins.Godot.AI
{
    [ZoneMarker]
    public class ZoneMarker : IRequire<IRiderProductEnvironmentZone>, IRequire<IRiderFeatureZone>, IRequire<IArtificialIntelligenceZone>
    {
    }
}