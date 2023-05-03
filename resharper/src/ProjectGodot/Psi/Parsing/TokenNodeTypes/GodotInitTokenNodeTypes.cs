using System.Collections.Generic;
using JetBrains.ReSharper.Psi.Parsing;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectGodot.Psi.Parsing.TokenNodeTypes;

public class GodotInitTokenNodeTypes
{
  public static readonly ProjectGodotTokenType SEMICOLON = new("SEMICOLON", 0);
    
  public static readonly ProjectGodotTokenType L_SBRACKET = new("L_SBRACKET", 1);
  public static readonly ProjectGodotTokenType R_SBRACKET = new("R_SBRACKET", 2);
  public static readonly ProjectGodotTokenType L_BRACKET = new("L_BRACKET", 3);
  public static readonly ProjectGodotTokenType R_BRACKET = new("R_BRACKET", 4);

  public static readonly ProjectGodotTokenType ADD_WITH_CHECK = new("ADD_WITH_CHECK", 5);
  public static readonly ProjectGodotTokenType ADD = new("ADD", 6);
  public static readonly ProjectGodotTokenType RM_PROP = new("RM_PROP", 7);
  public static readonly ProjectGodotTokenType RM_LN = new("RM_LN", 8);
  public static readonly ProjectGodotTokenType STAR = new("STAR", 9);

  public static readonly ProjectGodotTokenType EQ = new("EQ", 10);

  public static readonly ProjectGodotTokenType COMMA = new("COMMA", 11);
  public static readonly ProjectGodotTokenType COLON = new("COLON", 12);
  public static readonly ProjectGodotTokenType QUOTE_MK = new("QUOTE_MK", 13);

  public static readonly ProjectGodotTokenType BACKSLASH = new("BACKSLASH", 15);
  public static readonly ProjectGodotTokenType LINE_CONTINUATOR = new("LINE_CONTINUATOR", 16);
    
  public static readonly ProjectGodotTokenType NEWLINE = new("NEWLINE", 17);
  public static readonly ProjectGodotTokenType WHITESPACE = new("WHITESPACE", 18);
    
  public static readonly ProjectGodotTokenType LITERAL = new("LITERAL", 19);
  public static readonly ProjectGodotTokenType STRING_LITERAL = new("STRING_LITERAL", 20);
  public static readonly ProjectGodotTokenType COMMENT = new("COMMENT", 21);
    
  public static readonly ProjectGodotTokenType BAD_CHAR = new("BAD_CHAR", 22);
    
  // public static readonly GodotIniSectionCompositeNodeType SECTION = new("Ini_SECTION", 100);
  // public static readonly GodotIniSectionHeaderCompositeNodeType SECTION_HEADER = new("Ini_SECTION_HEADER", 101);
  // public static readonly GodotIniCompositeNodeType PROPERTY = new GodotIniPropertyCompositeNodeType("Ini_PROPERTY", 102);
  // public static readonly GodotIniCompositeNodeType KEY = new GodotIniKeyCompositeNodeType("Ini_KEY", 103);
  // public static readonly GodotIniCompositeNodeType VAL = new GodotIniValueCompositeNodeType("Ini_VAL", 104);
    
  public static readonly HashSet<TokenNodeType> Newlines = new() {NEWLINE, LINE_CONTINUATOR};
    
  public static readonly HashSet<TokenNodeType> Whitespaces = new() {WHITESPACE, NEWLINE, LINE_CONTINUATOR, SEMICOLON, COMMENT};
    
  public static readonly HashSet<TokenNodeType> PropOperators = new() {ADD_WITH_CHECK, ADD, RM_PROP, RM_LN, STAR};
}