using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;
using JetBrains.Text;

namespace JetBrains.ReSharper.Plugins.Godot.Tscn.Psi.Parsing.TokenNodeTypes
{
    internal class TscnWhitespaceTokenNodeType : TscnTokenNodeTypeBase
    {
        public TscnWhitespaceTokenNodeType(int index) : base("WHITESPACE", index)
        {
        }

        public override LeafElementBase Create(IBuffer buffer, TreeOffset startOffset, TreeOffset endOffset)
        {
            throw new System.NotImplementedException();
        }

        public override bool IsWhitespace => true;
        public override string TokenRepresentation => " ";
    }
}