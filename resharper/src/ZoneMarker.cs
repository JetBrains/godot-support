using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.DocumentModel;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Host.Product;
using JetBrains.ReSharper.Resources.Shell;
using JetBrains.Rider.Model;

namespace JetBrains.ReSharper.Plugins.Godot
{
    [ZoneMarker]
    public class ZoneMarker : IRequire<IRiderProductEnvironmentZone>, IRequire<IProjectModelZone>, IRequire<IRiderModelZone>, IRequire<IDocumentModelZone>, IRequire<PsiFeaturesImplZone>
    {
    }
}