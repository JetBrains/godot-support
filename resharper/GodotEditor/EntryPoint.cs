using System;
using System.Collections.Generic;
using System.IO;
using Godot;
using JetBrains.Annotations;
using JetBrains.Collections.Viewable;
using JetBrains.Diagnostics;
using JetBrains.Lifetimes;
using JetBrains.Rd;
using JetBrains.Rd.Impl;
using JetBrains.ReSharper.Plugins.Godot.Protocol;
using Directory = System.IO.Directory;
using File = System.IO.File;
using Path = System.IO.Path;

namespace JetBrains.Rider.Godot.Editor
{
    [UsedImplicitly]
    public static class EntryPoint
    {
        private static readonly ILog ourLogger = Log.GetLog("EntryPoint");
        
        private static readonly string EditorPathSettingName = "mono/editor/editor_path_optional";

        static EntryPoint()
        {
            var lifetimeDefinition = Lifetime.Define(Lifetime.Eternal);
            var lifetime = lifetimeDefinition.Lifetime;

            TimerBasedDispatcher.Instance.Start();
            var editorSettings = new EditorPlugin().GetEditorInterface().GetEditorSettings();
            var riderPath = editorSettings.GetSetting(EditorPathSettingName);

            var protocolInstanceJsonPath = Path.GetFullPath(".mono/metadata/ProtocolInstance.json");
            InitializeProtocol(lifetime, protocolInstanceJsonPath);
            ourLogger.Verbose("InitializeProtocol");

            AppDomain.CurrentDomain.DomainUnload += (sender, args) => { lifetimeDefinition.Terminate(); };
        }

        private static void InitializeProtocol(Lifetime lifetime, string protocolInstancePath)
        {
            lifetime.Bracket(() =>
            {
                var solutionName = (string) ProjectSettings.GetSetting("application/config/name");

                var allProtocolsLifetimeDefinition = lifetime.CreateNested();
                var port = CreateProtocolForSolution(allProtocolsLifetimeDefinition.Lifetime, solutionName,
                    () => { allProtocolsLifetimeDefinition.Terminate(); });

                var protocol = new ProtocolInstance(solutionName, port);

                var result = ProtocolInstance.ToJson(new[] {protocol});
                File.WriteAllText(protocolInstancePath, result);
            }, () =>
            {
                ourLogger.Verbose("Deleting ProtocolInstance.json");
                File.Delete(protocolInstancePath);
            });
        }

        private static int CreateProtocolForSolution(Lifetime lifetime, string solutionName, Action onDisconnected)
        {
            try
            {
                var dispatcher = TimerBasedDispatcher.Instance;
                var currentWireAndProtocolLifetimeDef = lifetime.CreateNested();
                var currentWireAndProtocolLifetime = currentWireAndProtocolLifetimeDef.Lifetime;

                var riderProtocolController = new RiderProtocolController(dispatcher, currentWireAndProtocolLifetime);

                var serializers = new Serializers(lifetime, null, null);
                var identities = new Identities(IdKind.Server);

                TimerBasedDispatcher.AssertThread();
                var protocol = new Protocol("GodotEditorPlugin" + solutionName, serializers, identities,
                    TimerBasedDispatcher.Instance, riderProtocolController.Wire, currentWireAndProtocolLifetime);
                riderProtocolController.Wire.Connected.WhenTrue(currentWireAndProtocolLifetime, connectionLifetime =>
                {
                    ourLogger.Log(LoggingLevel.VERBOSE, "Create godotModel and advise for new sessions...");
                    var model = new BackendGodotModel(connectionLifetime, protocol);
                    
                    // todo: need a callback from Godot to open script
                    // Can be called like: 
                    // model.OpenFileLineCol.Start(modelLifetime.Lifetime, new RdOpenFileArgs(assetFilePath, line, column));

                    ourLogger.Verbose("godotModel initialized.");
                    // var pair = new ModelWithLifetime(model, connectionLifetime);
                    // connectionLifetime.OnTermination(() => { godotModels.Remove(pair); });
                    // godotModels.Add(pair);

                    connectionLifetime.OnTermination(() =>
                    {
                        ourLogger.Verbose($"Connection lifetime is not alive for {solutionName}, destroying protocol");
                        onDisconnected();
                    });
                });

                return riderProtocolController.Wire.Port;
            }
            catch (Exception ex)
            {
                ourLogger.Error("Init Rider Plugin " + ex);
                return -1;
            }
        }
    }
}