using System;
using JetBrains.Threading;
using Mono.Debugging.Evaluation;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    internal static class EvaluationFailures
    {
        /// <summary>
        /// True when the evaluation was aborted or cancelled from outside, rather than failing on its own.
        /// </summary>
        /// <remarks>
        /// Handlers must let these through: turning one into an error row or into a permanent verdict about the
        /// debuggee makes routine stepping look like a bug.
        /// <para>
        /// What the outermost boundary of a presentation path does with them is that path's own decision, which is
        /// why each one spells out its catch clauses instead of sharing a helper. A boundary whose result decides
        /// whether to evaluate again (NodeChildrenEnumerator.EnumerateChildrenSafe,
        /// SceneTreeNodeValue.GetNodeRoleSafe) lets them propagate, because "no result" would send it into a second
        /// evaluation that is just as doomed as the aborted one. A boundary that owns rows besides the failing one
        /// (NodeObjectChildrenRenderer, GodotAdditionalValuesProvider - both inside iterators) absorbs them, because
        /// escaping there would drop those other rows too.
        /// </para>
        /// </remarks>
        public static bool ShouldPropagate(Exception exception) =>
            exception is EvaluatorAbortedException || exception.IsOperationCanceled();
    }
}
