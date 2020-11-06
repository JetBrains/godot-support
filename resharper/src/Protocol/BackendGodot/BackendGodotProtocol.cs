using System;
using System.Collections.Generic;
using System.Linq;
using JetBrains.Annotations;
using JetBrains.Application;
using JetBrains.Application.FileSystemTracker;
using JetBrains.Application.Threading;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd;
using JetBrains.Rd.Base;
using JetBrains.Rd.Impl;
using JetBrains.ReSharper.Psi.Util;
using JetBrains.Rider.Model.Notifications;
using JetBrains.Util;
using Newtonsoft.Json;

namespace JetBrains.ReSharper.Plugins.Godot.Protocol.BackendGodot
{
    [SolutionComponent]
    public class BackendGodotProtocol
    {
        private readonly Lifetime myLifetime;
        private readonly SequentialLifetimes mySessionLifetimes;
        private readonly ILogger myLogger;
        private readonly BackendGodotHost myBackendGodotHost;
        private readonly IScheduler myDispatcher;
        private readonly IShellLocks myLocks;
        private readonly ISolution mySolution;

        public BackendGodotProtocol(Lifetime lifetime, ILogger logger,
            BackendGodotHost backendGodotHost,
            IScheduler dispatcher, IShellLocks locks, ISolution solution,
            IApplicationWideContextBoundSettingStore settingsStore,
            NotificationsModel notificationsModel,
            IHostProductInfo hostProductInfo, IFileSystemTracker fileSystemTracker)
        {
            myLifetime = lifetime;
            myLogger = logger;
            myBackendGodotHost = backendGodotHost;
            myDispatcher = dispatcher;
            myLocks = locks;
            mySolution = solution;
            mySessionLifetimes = new SequentialLifetimes(lifetime);

            // connect on start of Rider
            var solFolder = mySolution.SolutionDirectory;
            var protocolInstancePath = solFolder.Combine("./mono/metadata/ProtocolInstance.json");
            CreateProtocol(protocolInstancePath);
        }

        private void SafeExecuteOrQueueEx(string name, Action action)
        {
            if (myLifetime.IsAlive) myLocks.ExecuteOrQueueEx(myLifetime, name, action);
        }

        private void CreateProtocol(FileSystemPath protocolInstancePath)
        {
            var protocolInstance = GetProtocolInstanceData(protocolInstancePath);
            if (protocolInstance == null)
                return;

            myLogger.Info(
                $"EditorPlugin protocol port {protocolInstance.Port} for Solution: {protocolInstance.SolutionName}.");

            try
            {
                var thisSessionLifetime = mySessionLifetimes.Next();
                myLogger.Info("Create protocol...");

                myLogger.Info("Creating SocketWire with port = {0}", protocolInstance.Port);
                var wire = new SocketWire.Client(thisSessionLifetime, myDispatcher, protocolInstance.Port,
                    "godotClient")
                {
                    BackwardsCompatibleWireFormat = true
                };

                var protocol = new Rd.Impl.Protocol("godotEditorPlugin",
                    new Serializers(thisSessionLifetime, null, null),
                    new Identities(IdKind.Client), myDispatcher, wire, thisSessionLifetime)
                {
                    ThrowErrorOnOutOfSyncModels = true
                };

                wire.Connected.WhenTrue(thisSessionLifetime, connectionLifetime =>
                {
                    myLogger.Info("WireConnected.");

                    var backendGodotModel = new BackendGodotModel(connectionLifetime, protocol);

                    SafeExecuteOrQueueEx("setModel",
                        () => myBackendGodotHost.BackendGodotModel.SetValue(backendGodotModel));

                    connectionLifetime.OnTermination(() =>
                    {
                        SafeExecuteOrQueueEx("clearModel", () =>
                        {
                            myLogger.Info("Wire disconnected.");

                            // Clear model
                            myBackendGodotHost.BackendGodotModel.SetValue(null);
                        });
                    });
                });
            }
            catch (Exception ex)
            {
                myLogger.Error(ex);
            }
        }

        [CanBeNull]
        private ProtocolInstance GetProtocolInstanceData(FileSystemPath protocolInstancePath)
        {
            if (!protocolInstancePath.ExistsFile)
                return null;

            List<ProtocolInstance> protocolInstanceList;
            try
            {
                protocolInstanceList = ProtocolInstance.FromJson(protocolInstancePath.ReadAllText2().Text);
            }
            catch (Exception e)
            {
                myLogger.Warn($"Unable to parse {protocolInstancePath}" + Environment.NewLine + e);
                return null;
            }

            if (protocolInstanceList == null)
            {
                myLogger.Info("No protocols listed in ProtocolInstance.json");
                return null;
            }

            var protocolInstance = protocolInstanceList.SingleOrDefault(a =>
                a.SolutionName == mySolution.SolutionFilePath.NameWithoutExtension);
            if (protocolInstance == null)
            {
                myLogger.Info($"Cannot find a protocol for the current solution. {protocolInstanceList.Count} options");
            }

            return protocolInstance;
        }

        // ReSharper disable once ClassNeverInstantiated.Global
        internal class ProtocolInstance
        {
            public readonly int Port;
            public readonly string SolutionName;
            public readonly Guid ProtocolGuid;

            public ProtocolInstance(int port, string solutionName, Guid protocolGuid)
            {
                Port = port;
                SolutionName = solutionName;
                ProtocolGuid = protocolGuid;
            }

            public static List<ProtocolInstance> FromJson(string json)
            {
                return JsonConvert.DeserializeObject<List<ProtocolInstance>>(json);
            }
        }
    }
}