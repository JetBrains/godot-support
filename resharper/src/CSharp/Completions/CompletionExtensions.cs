using System.Collections.Generic;
using JetBrains.Metadata.Reader.API;
using JetBrains.ReSharper.Feature.Services.CSharp.CodeCompletion.Infrastructure;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.CSharp.Tree;
using JetBrains.ReSharper.Psi.CSharp.Util.Literals;
using JetBrains.ReSharper.Psi.Tree;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Completions
{
    static class CompletionExtensions
    {
        public static ICSharpLiteralExpression StringLiteral(this CSharpCodeCompletionContext context)
            => context.NodeInFile is ITokenNode nodeInFile
               && nodeInFile.Parent is ICSharpLiteralExpression literalExpression
               && literalExpression.Literal.IsAnyStringLiteral()
                ? literalExpression
                : null;

        public static bool IsGodotLoad(this IInvocationExpression invocation)
        {
            var containingType = invocation.InvokedMethodContainingType();
            return (GodotTypes.ResourceLoader.Equals(containingType)
                    || GodotTypes.GD.Equals(containingType))
                   && "Load".Equals(invocation.InvokedMethodName());
        }

        public static IClrTypeName InvokedMethodContainingType(this IInvocationExpression invocation)
            => invocation.Reference.Resolve().DeclaredElement is IMethod method
               && method.ContainingType is ITypeElement type
                ? type.GetClrName()
                : null;

        public static string InvokedMethodName(this IInvocationExpression invocation)
            => invocation.Reference.Resolve().DeclaredElement is IMethod method
                ? method.ShortName
                : null;

        public static IClrTypeName InvokedMethodFirstTypeArgument(this IInvocationExpression invocation)
        {
            var typeArgs = invocation.Reference.Invocation.TypeArguments;
            return typeArgs.Count == 1
                   && typeArgs[0] is IDeclaredType t
                ? t.GetClrName()
                : null;
        }

        public static IClrTypeName AssignmentDestType(this IInvocationExpression invocation)
            => AssignmentExpressionNavigator.GetBySource(invocation) is IAssignmentExpression assignment 
               && assignment.Dest.Type() is IDeclaredType lhsType
                ? lhsType.GetClrName()
                : null;

        public static IClrTypeName IfGodotLoadGetResourceType(this CSharpCodeCompletionContext context)
        {
            if (!(
                    InvocationExpressionNavigator.GetByArgument(
                            CSharpArgumentNavigator.GetByValue(
                                context.NodeInFile.Parent as ICSharpLiteralExpression))
                        is IInvocationExpression invocation
                    && invocation.IsGodotLoad()))
            {
                return null;
            }

            return invocation.InvokedMethodFirstTypeArgument()
                   ?? invocation.AssignmentDestType();
        }

        public static void InsertOrAppendAtEach<K, V>(this IDictionary<K, IList<V>> d, IEnumerable<K> keys, params V[] value)
        {
            foreach (var key in keys)
            {
                d.InsertOrAppend(key, value);
            }
        }
        public static void InsertOrAppend<K, V>(this IDictionary<K, IList<V>> d, K key, params V[] value)
        {
            if (d.ContainsKey(key))
            {
                d[key].AddRange(value);
            }
            else
            {
                d[key] = new List<V>(value);
            }
        }

    }
}