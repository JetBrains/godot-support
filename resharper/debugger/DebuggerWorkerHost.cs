using Autofac;
using JetBrains.Debugger.Model.Plugins.Godot;
using JetBrains.Debugger.Worker;
using Mono.Debugging.Autofac;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger
{
    [DebuggerGlobalComponent]
    public class DebuggerWorkerHost : IStartable
    {
        public DebuggerWorkerHost(RiderDebuggerWorker debuggerWorker)
        {
            // Get/create the model. This registers serialisers for all types in the model, including the start info
            // derived types used in the root protocol, not in our extension
            Model = debuggerWorker.FrontendModel.GetGodotDebuggerWorkerModel();
        }

        public GodotDebuggerWorkerModel Model { get; }

        void IStartable.Start()
        {
            // Do nothing. IStartable means Autofac will eagerly create the component but we do all our work in the ctor
        }
    }
}