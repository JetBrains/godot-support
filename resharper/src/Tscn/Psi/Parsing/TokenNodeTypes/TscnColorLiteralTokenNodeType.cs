using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;
using JetBrains.Text;

namespace JetBrains.ReSharper.Plugins.Godot.Tscn.Psi.Parsing.TokenNodeTypes
{
    internal class TscnColorLiteralTokenNodeType : TscnTokenNodeTypeBase
    {
        public TscnColorLiteralTokenNodeType(int index) : base("COLOR_LITERAL", index)
        {
        }

        public override LeafElementBase Create(IBuffer buffer, TreeOffset startOffset, TreeOffset endOffset)
        {
            throw new System.NotImplementedException();
        }

        public override bool IsConstantLiteral => true;
        public override string TokenRepresentation => "#000000";
    }
}