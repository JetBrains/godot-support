using JetBrains.Util;

 // ReSharper disable InconsistentNaming

namespace JetBrains.ReSharper.Plugins.Godot.Tscn.Psi.Parsing
{
    public static class ParserMessages
    {
        public const string IDS_TSCN_FILE = "tscn file";
        public const string IDS_VARIANT_LITERAL = "variant literal";
        public const string IDS_VARIANT_VALUE = "variant value";
        
        public static string GetString(string id) => id;
        
        public static string GetUnexpectedTokenMessage() => "Unexpected token";
        
        public static string GetExpectedMessage(string tokenRepr)
        {
            return string.Format(GetString("{0} expected"), tokenRepr).Capitalize(); // why the GetString?
        }
        
        public static string GetExpectedMessage(string firstExpectedSymbol, string secondExpectedSymbol)
        {
            return string.Format(GetString("{0} or {1} expected"), firstExpectedSymbol, secondExpectedSymbol).Capitalize();
        }
    }
}