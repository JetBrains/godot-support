using System;
using System.Threading.Tasks;
using GodotTools.IdeMessaging;
using GodotTools.IdeMessaging.Requests;
using JetBrains.Application.Threading;
using JetBrains.Application.Threading.Tasks;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Base;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Threading;
using JetBrains.Util;
using ILogger = JetBrains.Util.ILogger;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [SolutionComponent]
    public class GodotMessagingClient : IMessageHandler, GodotTools.IdeMessaging.ILogger
    {
        private const string Identity = "Rider";

        private readonly ILogger myLogger;

        private Client myClient;

        public GodotMessagingClient(ISolution solution, ILogger logger, Lifetime lifetime, IThreading threading)
        {
            myLogger = logger; 
            var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel();
            
            model.MainProjectBasePath.AdviseOnce(lifetime, baseDir =>
            {
                myClient = new Client(Identity, baseDir, this, this);
                SubscribeConnected(logger, threading, model);
                SubscribeDisconnected(logger, threading, model); 
                myClient.Start();
            });
        }

        private void SubscribeDisconnected(ILogger logger, IThreading threading, GodotFrontendBackendModel model)
        {
            // it looks like it subscribes to be called just once
            myClient.AwaitDisconnected().ContinueWith(task =>
            {
                logger.Info("Godot Editor disconnected...");
                model.EditorState.SetValue(GodotEditorState.Disconnected);
                SubscribeDisconnected(logger, threading, model);
            }, threading.Tasks.GuardedMainThreadScheduler);
        }

        private void SubscribeConnected(ILogger logger, IThreading threading, GodotFrontendBackendModel model)
        {
            myClient.AwaitConnected().ContinueWith(task =>
            {
                logger.Info("Godot Editor connected...");
                model.EditorState.SetValue(GodotEditorState.Connected);
                SubscribeConnected(logger, threading, model);
            }, threading.Tasks.GuardedMainThreadScheduler);
        }

        public Task<MessageContent> HandleRequest(Peer peer, string id, MessageContent content,
            GodotTools.IdeMessaging.ILogger logger)
        {
            // TODO: unsure what this is for. Maybe PlayRequests?
            throw new NotImplementedException("Not implemented yet!");
        }

        public async Task<CodeCompletionResponse> SendNodePathRequest(string fullPath)
        {
            var response = await myClient.SendRequest<CodeCompletionResponse>(new CodeCompletionRequest()
            {
                Kind = CodeCompletionRequest.CompletionKind.NodePaths,
                ScriptFile = fullPath
            });

            return response;
        }
        
        public async Task<CodeCompletionResponse> SendInputActionsRequest()
        {
            var response = await myClient.SendRequest<CodeCompletionResponse>(new CodeCompletionRequest()
            {
                Kind = CodeCompletionRequest.CompletionKind.InputActions,
            });
            
            return response;
        }

        public void LogDebug(string message)
        {
            myLogger.Verbose(message);
        }

        public void LogInfo(string message)
        {
            myLogger.Info(message);
        }

        public void LogWarning(string message)
        {
            myLogger.Warn(message);
        }

        public void LogError(string message)
        {
            myLogger.Error(message);
        }

        public void LogError(string message, Exception e)
        {
            myLogger.Error(message, e);
        }
    }
}