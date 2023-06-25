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
            myClient.Connected += () => logger.Info("Godot Editor disconnected...");
            myClient.Start();
        }

        public Task<MessageContent> HandleRequest(Peer peer, string id, MessageContent content,
            GodotTools.IdeMessaging.ILogger logger)
        {
            throw new NotImplementedException("oh no");
        }

        public async Task<CodeCompletionResponse> SendNodePathRequest()
        {
            var response = await myClient.SendRequest<CodeCompletionResponse>(new CodeCompletionRequest()
            {
                Kind = CodeCompletionRequest.CompletionKind.NodePaths,
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
            myLogger.Log(LoggingLevel.VERBOSE, message);
        }

        public void LogInfo(string message)
        {
            myLogger.Log(LoggingLevel.INFO, message);
        }

        public void LogWarning(string message)
        {
            myLogger.Log(LoggingLevel.WARN, message);
        }

        public void LogError(string message)
        {
            myLogger.Log(LoggingLevel.ERROR, message);
        }

        public void LogError(string message, Exception e)
        {
            myLogger.Log(LoggingLevel.ERROR, message, e);
        }
    }
}