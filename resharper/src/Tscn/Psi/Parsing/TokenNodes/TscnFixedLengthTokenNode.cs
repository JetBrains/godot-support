using JetBrains.ReSharper.Plugins.Godot.Tscn.Psi.Parsing.TokenNodeTypes;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;

namespace JetBrains.ReSharper.Plugins.Godot.Tscn.Psi.Parsing.TokenNodes
{
    internal class TscnFixedLengthTokenNode : TscnTokenNodeBase
    {
        private readonly TscnFixedLengthTokenNodeType myTokenNodeType;

        public TscnFixedLengthTokenNode(TscnFixedLengthTokenNodeType tokenNodeType)
        {
            myTokenNodeType = tokenNodeType;
        }

        public override int GetTextLength()
        {
            return myTokenNodeType.TokenRepresentation.Length;
        }

        public override string GetText()
        {
            return myTokenNodeType.TokenRepresentation;
        }

        public override NodeType NodeType => myTokenNodeType;
    }
}