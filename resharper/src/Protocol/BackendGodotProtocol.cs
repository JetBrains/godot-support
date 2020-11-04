using JetBrains.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.Protocol
{
    [SolutionComponent]
    public class BackendGodotProtocol
    {
        public BackendGodotProtocol()
        {
            var backendUnityModel = new BackendGodotModel(connectionLifetime, protocol);
        }
    }
}