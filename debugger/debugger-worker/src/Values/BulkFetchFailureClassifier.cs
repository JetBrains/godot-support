using System;
using Mono.Debugging.Evaluation;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// What to do after an attempt to bulk-fetch a Godot.Node's children failed.
    /// </summary>
    public enum BulkFetchFailureAction
    {
        /// <summary>
        /// Not our failure to handle - see <see cref="EvaluationFailures.ShouldPropagate"/>.
        /// </summary>
        Rethrow,

        /// <summary>
        /// Specific to this evaluation, not to the debuggee. Fall back for this node but try again next time.
        /// </summary>
        RetryLater,

        /// <summary>
        /// The debuggee cannot run our helper lambdas. Stop trying for the rest of the session.
        /// </summary>
        DisableForSession,
    }

    /// <summary>
    /// Decides whether a bulk-fetch failure is transient or a permanent verdict about the debuggee.
    /// </summary>
    /// <remarks>
    /// Extracted so the classification is directly testable. Getting it wrong is invisible: latching on a transient
    /// failure silently downgrades every later expansion in the session to the slow path, and a gold test cannot tell
    /// the two paths apart because they render the same tree.
    /// </remarks>
    public static class BulkFetchFailureClassifier
    {
        public static BulkFetchFailureAction Classify(Exception exception)
        {
            if (EvaluationFailures.ShouldPropagate(exception))
                return BulkFetchFailureAction.Rethrow;

            // A timeout is specific to this evaluation and the current timeout setting, so allow a later expansion to
            // retry - the user may raise the timeout, or the debuggee may simply be less busy.
            if (exception is TimeOutException)
                return BulkFetchFailureAction.RetryLater;

            // Debuggee code throwing says something about the data we ran over, not about the debuggee's ability to
            // run our lambdas: one freed node among a thousand is enough to reach here. Treating it as a verdict
            // would let a single disposed child cost the session its fast path for every node it has left.
            if (exception is EvaluatorExceptionThrownExceptionBase)
                return BulkFetchFailureAction.RetryLater;

            return BulkFetchFailureAction.DisableForSession;
        }
    }
}
