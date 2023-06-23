using System;
using System.Threading.Tasks;
using GodotTools.IdeMessaging;
using JetBrains.Diagnostics;
using JetBrains.ProjectModel;
using JetBrains.Util;
using JetBrains.Util.Logging;
using ILogger = JetBrains.Util.ILogger;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [SolutionComponent]
    public class GodotMessagingClient : IMessageHandler, GodotTools.IdeMessaging.ILogger
    {
        private const string Identity = "Rider";

        private readonly ILogger myLogger;

        private Client myClient;

        public GodotMessagingClient(ISolution solution, ILogger logger)
        {
            var testLogger = Logger.GetLogger<GodotMessagingClient>();
            testLogger.Log(LoggingLevel.INFO, "GODOT Omg WTF!");
            myLogger = logger;
            myClient = new Client(Identity, solution.SolutionDirectory.Name, this, this);
            myClient.Connected += () => logger.Info("CONNECTETDSGDSDKOFGJKLSDF YES!");
            logger.Info("Started GodotMessagingClient");
            logger.Warn("avocado");
            Console.WriteLine("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% AVOCADO %%%%%%%%%%%%%%%%%%%%%%%%%");
        }

        public Task<MessageContent> HandleRequest(Peer peer, string id, MessageContent content,
            GodotTools.IdeMessaging.ILogger logger)
        {
            throw new NotImplementedException("does not work yet!");
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