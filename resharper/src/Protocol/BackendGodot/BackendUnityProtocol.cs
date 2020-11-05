// using System;
// using System.Collections.Generic;
// using System.Linq;
// using JetBrains.Annotations;
// using JetBrains.Application;
// using JetBrains.Application.changes;
// using JetBrains.Application.FileSystemTracker;
// using JetBrains.Application.Settings;
// using JetBrains.Application.Threading;
// using JetBrains.Collections.Viewable;
// using JetBrains.Lifetimes;
// using JetBrains.ProjectModel;
// using JetBrains.Rd;
// using JetBrains.Rd.Base;
// using JetBrains.Rd.Impl;
// using JetBrains.ReSharper.Host.Features;
// using JetBrains.ReSharper.Plugins.Unity.ProjectModel;
// using JetBrains.ReSharper.Plugins.Unity.Settings;
// using JetBrains.ReSharper.Psi.Util;
// using JetBrains.Rider.Model.Notifications;
// using JetBrains.Rider.Model.Unity.BackendUnity;
// using JetBrains.Rider.Unity.Editor.NonUnity;
// using JetBrains.Util;
// using Newtonsoft.Json;
//
// namespace JetBrains.ReSharper.Plugins.Unity.Rider.Protocol
// {
//     [SolutionComponent]
//     public class BackendGodotProtocol
//     {
//         private readonly Lifetime myLifetime;
//         private readonly SequentialLifetimes mySessionLifetimes;
//         private readonly ILogger myLogger;
//         private readonly BackendUnityHost myBackendUnityHost;
//         private readonly IScheduler myDispatcher;
//         private readonly IShellLocks myLocks;
//         private readonly ISolution mySolution;
//         private readonly NotificationsModel myNotificationsModel;
//         private readonly IHostProductInfo myHostProductInfo;
//         private readonly IContextBoundSettingsStoreLive myBoundSettingsStore;
//         private readonly JetHashSet<FileSystemPath> myPluginInstallations;
//
//         private DateTime myLastChangeTime;
//
//         public BackendGodotProtocol(Lifetime lifetime, ILogger logger,
//                                     BackendUnityHost backendUnityHost,
//                                     IScheduler dispatcher, IShellLocks locks, ISolution solution,
//                                     IApplicationWideContextBoundSettingStore settingsStore,
//                                     NotificationsModel notificationsModel,
//                                     IHostProductInfo hostProductInfo, IFileSystemTracker fileSystemTracker)
//         {
//             myPluginInstallations = new JetHashSet<FileSystemPath>();
//
//             myLifetime = lifetime;
//             myLogger = logger;
//             myBackendUnityHost = backendUnityHost;
//             myDispatcher = dispatcher;
//             myLocks = locks;
//             mySolution = solution;
//             myNotificationsModel = notificationsModel;
//             myHostProductInfo = hostProductInfo;
//             myBoundSettingsStore = settingsStore.BoundSettingsStore;
//             mySessionLifetimes = new SequentialLifetimes(lifetime);
//             
//             // connect on start of Rider
//             CreateProtocol(protocolInstancePath);
//         }
//
//         private void SafeExecuteOrQueueEx(string name, Action action)
//         {
//             if (myLifetime.IsAlive) myLocks.ExecuteOrQueueEx(myLifetime, name, action);
//         }
//
//         private void CreateProtocol(FileSystemPath protocolInstancePath)
//         {
//             var protocolInstance = GetProtocolInstanceData(protocolInstancePath);
//             if (protocolInstance == null)
//                 return;
//
//             myLogger.Info($"EditorPlugin protocol port {protocolInstance.Port} for Solution: {protocolInstance.SolutionName}.");
//
//             if (protocolInstance.ProtocolGuid != ProtocolCompatibility.ProtocolGuid)
//             {
//                 OnOutOfSync(myLifetime);
//                 myLogger.Info("Avoid attempt to create protocol, incompatible.");
//                 return;
//             }
//
//             try
//             {
//                 var thisSessionLifetime = mySessionLifetimes.Next();
//                 myLogger.Info("Create protocol...");
//
//                 myLogger.Info("Creating SocketWire with port = {0}", protocolInstance.Port);
//                 var wire = new SocketWire.Client(thisSessionLifetime, myDispatcher, protocolInstance.Port, "UnityClient")
//                 {
//                     BackwardsCompatibleWireFormat = true
//                 };
//
//                 var protocol = new Rd.Impl.Protocol("UnityEditorPlugin", new Serializers(thisSessionLifetime, null, null),
//                     new Identities(IdKind.Client), myDispatcher, wire, thisSessionLifetime)
//                 {
//                     ThrowErrorOnOutOfSyncModels = false
//                 };
//
//                 protocol.OutOfSyncModels.AdviseOnce(thisSessionLifetime, _ => OnOutOfSync(thisSessionLifetime));
//
//                 wire.Connected.WhenTrue(thisSessionLifetime, connectionLifetime =>
//                 {
//                     myLogger.Info("WireConnected.");
//
//                     var backendUnityModel = new BackendUnityModel(connectionLifetime, protocol);
//
//                     SafeExecuteOrQueueEx("setModel",
//                         () => myBackendUnityHost.BackendUnityModel.SetValue(backendUnityModel));
//
//                     connectionLifetime.OnTermination(() =>
//                     {
//                         SafeExecuteOrQueueEx("clearModel", () =>
//                         {
//                             myLogger.Info("Wire disconnected.");
//
//                             // Clear model
//                             myBackendUnityHost.BackendUnityModel.SetValue(null);
//                         });
//                     });
//                 });
//             }
//             catch (Exception ex)
//             {
//                 myLogger.Error(ex);
//             }
//         }
//
//         [CanBeNull]
//         private ProtocolInstance GetProtocolInstanceData(FileSystemPath protocolInstancePath)
//         {
//             if (!protocolInstancePath.ExistsFile)
//                 return null;
//
//             List<ProtocolInstance> protocolInstanceList;
//             try
//             {
//                 protocolInstanceList = ProtocolInstance.FromJson(protocolInstancePath.ReadAllText2().Text);
//             }
//             catch (Exception e)
//             {
//                 myLogger.Warn($"Unable to parse {protocolInstancePath}" + Environment.NewLine + e);
//                 return null;
//             }
//
//             if (protocolInstanceList == null)
//             {
//                 myLogger.Info("No protocols listed in ProtocolInstance.json");
//                 return null;
//             }
//
//             var protocolInstance = protocolInstanceList.SingleOrDefault(a =>
//                 a.SolutionName == mySolution.SolutionFilePath.NameWithoutExtension);
//             if (protocolInstance == null)
//             {
//                 myLogger.Info($"Cannot find a protocol for the current solution. {protocolInstanceList.Count} options");
//             }
//
//             return protocolInstance;
//         }
//
//         // ReSharper disable once ClassNeverInstantiated.Global
//     internal class ProtocolInstance
//     {
//         public readonly int Port;
//         public readonly string SolutionName;
//         public readonly Guid ProtocolGuid;
//
//         public ProtocolInstance(int port, string solutionName, Guid protocolGuid)
//         {
//             Port = port;
//             SolutionName = solutionName;
//             ProtocolGuid = protocolGuid;
//         }
//
//         public static List<ProtocolInstance> FromJson(string json)
//         {
//             return JsonConvert.DeserializeObject<List<ProtocolInstance>>(json);
//         }
//     }
// }