
// Commas, square/curly brackets, colons
"," { return sf.newSymbol("Comma",sym.COMMA); }
"[" { return sf.newSymbol("Left Square Bracket",sym.LSQBRACKET); }
"]" { return sf.newSymbol("Right Square Bracket",sym.RSQBRACKET); }
"{" { return sf.newSymbol("Left Curly Bracket", sym.LCBRACKET);}
"}" { return sf.newSymbol("Right Curly Bracket", sym.RCBRACKET);}
":" { return sf.newSymbol("Colon", sym.COLON);}

//Scan for unicode strings (using unicode directive
{string} { return sf.newSymbol("Unicode String", sym.STRING, new String(yytext()));}

//Scan for boolean strings
"true"|"false" { return sf.newSymbol("Boolean True", sym.BOOLEAN, new Boolean(yytext()));}

//Scan for null strings
"null" { return sf.newSymbol("Null Symbol", sym.NULL);}

//Scan for numbers: 0, integers or real numbers (flop)
{any_number} { return sf.newSymbol("Integral Number",sym.NUMBER, new Double(yytext())); }

// Do nothing for white spaces
[ \b\r\n\f\t\s] { }

//Look for and locate error
. { System.err.println("Illegal character "+yytext()+" at line "+(yyline+1)); }
