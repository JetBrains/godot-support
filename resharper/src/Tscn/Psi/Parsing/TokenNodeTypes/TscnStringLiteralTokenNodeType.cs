using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;
using JetBrains.Text;

namespace JetBrains.ReSharper.Plugins.Godot.Tscn.Psi.Parsing.TokenNodeTypes
{
    internal class TscnStringLiteralTokenNodeType : TscnTokenNodeTypeBase
    {
        public TscnStringLiteralTokenNodeType(int index) : base("STRING_LITERAL", index)
        {
        }

        public override LeafElementBase Create(IBuffer buffer, TreeOffset startOffset, TreeOffset endOffset)
        {
            throw new System.NotImplementedException();
        }

        public override bool IsStringLiteral => true;
        public override string TokenRepresentation { get; } = "\"string\"";
    }
}