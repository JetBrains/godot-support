namespace JetBrains.ReSharper.Plugins.Godot.Tscn.Psi.Parsing.TokenNodeTypes
{
    internal class TscnCommentTokenNodeType : TscnGenericTokenNodeType
    {
        public TscnCommentTokenNodeType(int index) : base("SINGLE_LINE_COMMENT", index, "; single line comment")
        {
        }

        public override bool IsFiltered => true;
        public override bool IsComment => true;
    }
}