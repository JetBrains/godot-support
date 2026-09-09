using System;
using System.Threading;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values;
using Mono.Debugging.Evaluation;
using NUnit.Framework;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Tests
{
    /// <summary>
    /// Whether a bulk-fetch failure disables the fast path for the whole session - see
    /// <see cref="BulkFetchFailureClassifier"/> for why this cannot be seen from an integration test.
    /// </summary>
    [TestFixture]
    public class BulkFetchFailureClassifierTests
    {
        [Test]
        public void AbortedEvaluationIsNotOurFailure()
        {
            // The user stepped, resumed, or the session died. Nothing was learned about the debuggee.
            Assert.That(BulkFetchFailureClassifier.Classify(new EvaluatorAbortedException()),
                Is.EqualTo(BulkFetchFailureAction.Rethrow));
        }

        [Test]
        public void CancellationIsNotOurFailure()
        {
            Assert.That(BulkFetchFailureClassifier.Classify(new OperationCanceledException()),
                Is.EqualTo(BulkFetchFailureAction.Rethrow));
        }

        [Test]
        public void TaskCancellationIsNotOurFailure()
        {
            Assert.That(BulkFetchFailureClassifier.Classify(new TaskCanceledExceptionStub()),
                Is.EqualTo(BulkFetchFailureAction.Rethrow));
        }

        [Test]
        public void TimeoutIsTransient()
        {
            // Specific to this evaluation and the current timeout setting - a later expansion may well succeed, so
            // one slow node must not cost the session its fast path.
            Assert.That(BulkFetchFailureClassifier.Classify(new TimeOutException(timeoutMs: 5000)),
                Is.EqualTo(BulkFetchFailureAction.RetryLater));
        }

        [Test]
        public void DebuggeeThrowingIsTransient()
        {
            // Reading one child's Name is enough to reach here - Godot's wrapper for a freed object throws on every
            // member access. That says nothing about whether the debuggee can run our lambdas, so the session must
            // keep its fast path for the nodes it has left.
            Assert.That(BulkFetchFailureClassifier.Classify(
                    new SyntheticEvaluatorExceptionThrownException("System.ObjectDisposedException", "freed")),
                Is.EqualTo(BulkFetchFailureAction.RetryLater));
        }

        [Test]
        public void DebuggeeThrowingWithARealExceptionObjectIsTransient()
        {
            // The shape we actually get from a failed delegate invocation - classification must not depend on
            // which of the two thrown-exception types the platform produced.
            Assert.That(BulkFetchFailureClassifier.Classify(
                    new EvaluatorExceptionThrownException<object>(new object(), "System.ObjectDisposedException")),
                Is.EqualTo(BulkFetchFailureAction.RetryLater));
        }

        [Test]
        public void EvaluationFailureIsAPermanentVerdict()
        {
            // The debuggee could not compile or run our helper lambdas - retrying for every node would just log the
            // same warning thousands of times.
            Assert.That(BulkFetchFailureClassifier.Classify(new EvaluatorException("nope")),
                Is.EqualTo(BulkFetchFailureAction.DisableForSession));
        }

        [Test]
        public void MissingAssemblyInDebuggeeIsAPermanentVerdict()
        {
            // What a lambda that breaks the constraint documented on DebuggeeLambdas throws inside the debuggee.
            Assert.That(BulkFetchFailureClassifier.Classify(new System.IO.FileNotFoundException("GodotSharp")),
                Is.EqualTo(BulkFetchFailureAction.DisableForSession));
        }

        [Test]
        public void UnknownFailureIsAPermanentVerdict()
        {
            Assert.That(BulkFetchFailureClassifier.Classify(new InvalidOperationException()),
                Is.EqualTo(BulkFetchFailureAction.DisableForSession));
        }

        [Test]
        public void DerivedAbortIsAlsoNotOurFailure()
        {
            // Classification must be by "is a", not by exact type - the platform is free to introduce a more specific
            // abort exception, and treating it as a permanent verdict would disable the fast path on every step.
            Assert.That(BulkFetchFailureClassifier.Classify(new DerivedAbortedException()),
                Is.EqualTo(BulkFetchFailureAction.Rethrow));
        }

        [Test]
        public void DerivedTimeoutIsStillTransient()
        {
            Assert.That(BulkFetchFailureClassifier.Classify(new DerivedTimeOutException()),
                Is.EqualTo(BulkFetchFailureAction.RetryLater));
        }

        private sealed class TaskCanceledExceptionStub : OperationCanceledException
        {
        }

        private sealed class DerivedAbortedException : EvaluatorAbortedException
        {
        }

        private sealed class DerivedTimeOutException : TimeOutException
        {
            public DerivedTimeOutException() : base("timed out")
            {
            }
        }
    }
}