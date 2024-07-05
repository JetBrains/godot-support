using System;
using JetBrains.Annotations;
using JetBrains.Util;
using Mono.Debugging.Evaluation;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger
{
    public static class LoggerHelper
    {
        [CanBeNull]
        public static T CatchEvaluatorException<TValue, T>(this ILogger logger, [InstantHandle] Func<T> action,
                                                           Action<EvaluatorExceptionThrownException<TValue>> onEvaluatorException)
            where TValue : class
        {
            try
            {
                return action();
            }
            catch (EvaluatorAbortedException e)
            {
                // Evaluation has been aborted, e.g. the user has continued before evaluation has completed
                logger.LogExceptionSilently(e);
            }
            catch (EvaluatorExceptionThrownException<TValue> e)
            {
                // The code being evaluated threw an exception. This might be expected, might not
                onEvaluatorException(e);
            }
            catch (Exception e)
            {
                // We're not expecting this exception, log it as an error so we can fix it
                logger.LogException(e);
            }

            return default;
        }
    }
}