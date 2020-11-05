using System;
using System.Collections.Generic;
using System.IO;
using JetBrains.Annotations;
using JetBrains.Collections.Viewable;
using JetBrains.Diagnostics;
using JetBrains.Lifetimes;
using JetBrains.Rd;
using JetBrains.Rd.Impl;
using JetBrains.ReSharper.Plugins.Godot.Protocol;

namespace JetBrains.Rider.Godot.Editor
{
    [UsedImplicitly]
    public static class EntryPoint
    {
        private static readonly ILog ourLogger = Log.GetLog("EntryPoint");
        
        static EntryPoint()
        {
            var lifetimeDefinition = Lifetime.Define(Lifetime.Eternal);
            var lifetime = lifetimeDefinition.Lifetime;

            TestDispatcher.Instance.Start();
            var editorSettings = TestEditorPlugin.Instance.GetEditorInterface().GetEditorSettings();

            var protocolInstanceJsonPath = Path.GetFullPath(".mono/ProtocolInstance.json");
            InitializeProtocol(lifetime, protocolInstanceJsonPath);
            //ourLogger.Verbose("InitializeProtocol");

            AppDomain.CurrentDomain.DomainUnload += (sender, args) => { lifetimeDefinition.Terminate(); };
        }

        private static void InitializeProtocol(Lifetime lifetime, string protocolInstancePath)
        {
            lifetime.Bracket(() =>
            {
                var currentDirectory = new DirectoryInfo(Directory.GetCurrentDirectory());
                var solutionNames = new List<string>() {currentDirectory.Name};

                var solutionFiles = currentDirectory.GetFiles("*.sln", SearchOption.TopDirectoryOnly);
                foreach (var solutionFile in solutionFiles)
                {
                    var solutionName = Path.GetFileNameWithoutExtension(solutionFile.FullName);
                    if (!solutionName.Equals(currentDirectory.Name))
                    {
                        solutionNames.Add(solutionName);
                    }
                }

                var protocols = new List<ProtocolInstance>();

                // if any protocol connection losts, we will drop all protocol and recreate them
                var allProtocolsLifetimeDefinition = lifetime.CreateNested();
                foreach (var solutionName in solutionNames)
                {
                    var port = CreateProtocolForSolution(allProtocolsLifetimeDefinition.Lifetime, solutionName,
                        () => { allProtocolsLifetimeDefinition.Terminate(); });

                    if (port == -1)
                        continue;

                    protocols.Add(new ProtocolInstance(solutionName, port));
                }

                allProtocolsLifetimeDefinition.Lifetime.OnTermination(() =>
                {
                    if (lifetime.IsAlive)
                    {
                        ourLogger.Verbose("Recreating protocol, project lifetime is alive");
                        InitializeProtocol(lifetime, protocolInstancePath);
                    }
                    else
                    {
                        ourLogger.Verbose("Protocol will be recreating on next domain load, project lifetime is not alive");
                    }
                });


                var result = ProtocolInstance.ToJson(protocols);
                File.WriteAllText(protocolInstancePath, result);
            }, () =>
            {
                ourLogger.Verbose("Deleting Library/ProtocolInstance.json");
                File.Delete(protocolInstancePath);
            });

        }

        private static int CreateProtocolForSolution(Lifetime lifetime, string solutionName, Action onDisconnected)
        {
            try
            {
                var dispatcher = TestDispatcher.Instance;
                var currentWireAndProtocolLifetimeDef = lifetime.CreateNested();
                var currentWireAndProtocolLifetime = currentWireAndProtocolLifetimeDef.Lifetime;

                var riderProtocolController = new RiderProtocolController(dispatcher, currentWireAndProtocolLifetime);

                var serializers = new Serializers(lifetime, null, null);
                var identities = new Identities(IdKind.Server);

                TestDispatcher.AssertThread();
                var protocol = new Protocol("GodotEditorPlugin" + solutionName, serializers, identities,
                    TestDispatcher.Instance, riderProtocolController.Wire, currentWireAndProtocolLifetime);
                riderProtocolController.Wire.Connected.WhenTrue(currentWireAndProtocolLifetime, connectionLifetime =>
                {
                    ourLogger.Log(LoggingLevel.VERBOSE, "Create UnityModel and advise for new sessions...");
                    var model = new BackendGodotModel(connectionLifetime, protocol);
                    
                    ourLogger.Verbose("UnityModel initialized.");
                    // var pair = new ModelWithLifetime(model, connectionLifetime);
                    // connectionLifetime.OnTermination(() => { UnityModels.Remove(pair); });
                    // UnityModels.Add(pair);

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