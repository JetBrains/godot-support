using JetBrains.ReSharper.Psi.Parsing;

namespace JetBrains.ReSharper.Plugins.Godot.Tscn.Psi.Parsing.TokenNodeTypes
{
    public partial class TscnTokenNodeTypes
    {
        public static readonly TokenNodeType BAD_CHARACTER = new TscnGenericTokenNodeType("BAD_CHARACTER", LAST_GENERATED_TOKEN_TYPE_INDEX + 1, "�");
        
        public static readonly TokenNodeType WHITESPACE = new TscnWhitespaceTokenNodeType(LAST_GENERATED_TOKEN_TYPE_INDEX + 2);
        
        public static readonly TokenNodeType NEW_LINE = new TscnNewLineTokenNodeType(LAST_GENERATED_TOKEN_TYPE_INDEX + 3);
        
        public const int IDENTIFIER_NODE_TYPE_INDEX = LAST_GENERATED_TOKEN_TYPE_INDEX + 4;
        public static readonly TokenNodeType IDENTIFIER = new TscnIdentifierTokenNodeType(IDENTIFIER_NODE_TYPE_INDEX);
        
        public static readonly TokenNodeType COMMENT = new TscnCommentTokenNodeType(LAST_GENERATED_TOKEN_TYPE_INDEX + 5);
        
        public static readonly TokenNodeType EOF = new TscnGenericTokenNodeType("EOF", LAST_GENERATED_TOKEN_TYPE_INDEX + 6, "EOF");
        
        public static readonly TokenNodeType NUMERIC_LITERAL = new TscnNumericLiteralTokenNodeType(LAST_GENERATED_TOKEN_TYPE_INDEX + 7);
        
        public static readonly TokenNodeType COLOR_LITERAL = new TscnColorLiteralTokenNodeType(LAST_GENERATED_TOKEN_TYPE_INDEX + 8);
        
        public static readonly TokenNodeType STRING_LITERAL = new TscnStringLiteralTokenNodeType(LAST_GENERATED_TOKEN_TYPE_INDEX + 9);
        
        public static readonly TokenNodeType STRING_NAME_LITERAL = new TscnStringNameLiteralTokenNodeType(LAST_GENERATED_TOKEN_TYPE_INDEX + 10);
    }
}