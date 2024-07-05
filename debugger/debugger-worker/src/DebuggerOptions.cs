using JetBrains.Collections.Viewable;
using Mono.Debugging.Autofac;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger
{
    public interface IOptions
    {
        bool ExtensionsEnabled { get; }
    }
    
    [DebuggerGlobalComponent]
    public class DebuggerOptions : IOptions
    {
        private readonly DebuggerWorkerHost myHost;

        public DebuggerOptions(DebuggerWorkerHost host)
        {
            myHost = host;
        }

        public bool ExtensionsEnabled => myHost.Model.ShowCustomRenderers.HasTrueValue();
    }
}