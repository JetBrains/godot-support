using System;
using System.Threading.Tasks;
using GodotTools.IdeMessaging;
using GodotTools.IdeMessaging.Requests;
using JetBrains.Diagnostics;
using JetBrains.ProjectModel;
using JetBrains.Util;
using ILogger = JetBrains.Util.ILogger;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [SolutionComponent]
    public class GodotMessagingClient : IMessageHandler, GodotTools.IdeMessaging.ILogger
    {
        private const string Identity = "Rider";

        private readonly ILogger myLogger;

        private readonly Client myClient;

        public GodotMessagingClient(ISolution solution, ILogger logger)
        {
            myLogger = logger;
            myClient = new Client(Identity, solution.SolutionDirectory.FullPath, this, this);
            myClient.Connected += () => logger.Info("Godot Editor connected...");
            myClient.Disconnected += () => logger.Info("Godot Editor disconnected...");
            myClient.Start();
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