using System;
using JetBrains.Annotations;
using JetBrains.Util;
using Mono.Debugging.Backend.Values;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client.CallStacks;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.Evaluation;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// Describes an exception that code inside the debuggee threw while we were evaluating.
    /// </summary>
    /// <remarks>
    /// The wrapper exception's own message only names the type ("Exception of type 'X' was thrown"), so without
    /// reaching into the debuggee for the real Message both our log entries and our error rows say nothing a user
    /// can act on.
    /// </remarks>
    internal static class ThrownDebuggeeException
    {
        /// <summary>
        /// Reads <c>Message</c> off the exception object living in the debuggee.
        /// </summary>
        /// <returns>Null if the message could not be read - callers must cope without it.</returns>
        [CanBeNull]
        public static string GetMessage<TValue>(
            [NotNull] EvaluatorExceptionThrownException<TValue> exception,
            [NotNull] IStackFrame frame,
            [NotNull] IValueServicesFacade<TValue> valueServices,
            [NotNull] IValueFetchOptions options,
            [NotNull] ILogger logger)
            where TValue : class
        {
            // Reading Message invokes a property getter. We are already describing a failure rather than presenting
            // a value, so ask for the invoke explicitly instead of inheriting what the failed evaluation was allowed
            // to do - otherwise the message disappears exactly when target invocation is off.
            var messageOptions = options.WithOverridden(o => o.AllowTargetInvoke = true);

            try
            {
                return new SimpleValueReference<TValue>(exception.Exception, frame, valueServices.RoleFactory)
                    .AsObjectSafe(messageOptions)
                    ?.GetInstancePropertyReference("Message", searchInBases: true)
                    ?.AsStringSafe(messageOptions)
                    ?.GetString();
            }
            catch (Exception e) when (!EvaluationFailures.ShouldPropagate(e))
            {
                // An evaluation failing while we describe a failed evaluation. Don't let it replace the original.
                logger.LogExceptionSilently(e);
                return null;
            }
        }

        /// <summary>
        /// Text for an error row standing in for a value we could not fetch.
        /// </summary>
        /// <remarks>
        /// Uses the exception's own message unless the debuggee threw, in which case that message names only the
        /// type and the interesting half has to be fetched from the debuggee.
        /// </remarks>
        [NotNull]
        public static string Describe<TValue>(
            [NotNull] Exception exception,
            [NotNull] IStackFrame frame,
            [NotNull] IValueServicesFacade<TValue> valueServices,
            [NotNull] IValueFetchOptions options,
            [NotNull] ILogger logger)
            where TValue : class
        {
            if (exception is not EvaluatorExceptionThrownException<TValue> thrown)
                return exception.Message;

            var message = GetMessage(thrown, frame, valueServices, options, logger);
            return message == null ? exception.Message : $"{thrown.ExceptionTypeName}: {message}";
        }

        /// <summary>
        /// Logs an exception thrown by the debuggee, silently when it is one we expect to see during ordinary
        /// debugging and as an error otherwise.
        /// </summary>
        public static void LogThrown<TValue>(
            [NotNull] this ILogger logger,
            [NotNull] EvaluatorExceptionThrownException<TValue> exception,
            [NotNull] IStackFrame frame,
            [NotNull] IValueServicesFacade<TValue> valueServices,
            [NotNull] IValueFetchOptions options)
            where TValue : class
        {
            var message = GetMessage(exception, frame, valueServices, options, logger);

            // Godot disposes the C# wrapper when the object it stands for is freed, and every member access on it
            // throws from then on. Freeing nodes is ordinary game code, so a scene tree that still holds a reference
            // to one is not a Rider bug and must not be reported as one.
            if (exception.ExceptionTypeName == "System.ObjectDisposedException")
                logger.Verbose(exception, comment: message);
            else
                logger.Error(exception, "Exception thrown by evaluated code: {0}", message);
        }
    }
}
