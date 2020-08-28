using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;
using JetBrains.Text;

namespace JetBrains.ReSharper.Plugins.Godot.Tscn.Psi.Parsing.TokenNodeTypes
{
    internal class TscnIdentifierTokenNodeType : TscnTokenNodeTypeBase
    {
        public TscnIdentifierTokenNodeType(int index) : base("IDENTIFIER", index)
        {
        }

        public override LeafElementBase Create(IBuffer buffer, TreeOffset startOffset, TreeOffset endOffset)
        {
            throw new System.NotImplementedException();
        }

        public override bool IsIdentifier => true;
        public override string TokenRepresentation => "identifier";
    }
}