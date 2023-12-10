# Code flow

First of all - there isn't a straightforward flow to follow, sorry.  
Each feature is for the most part run independently of others, but there is one shared prerequisite which is a PsiTree.

Each file is first processed in two steps: Lexer & Parser, resulting in a PsiTree.

## Lexer
Walks file from begging to end matching it to many different regular expressions and translating words/letters/sentences into tokens - more or less regex that matches larger segment wins.  
Each token than represents part of the code like keywords (func, for, var, class, class_name, ...), brackets, white space, signs (+, -, *, =, <, ...) and so on.  
Lexer doesn't care for grammar and is generated from `.flex` files resulting in simple array of tokens passed to Parser.

## Parser
Takes token array and parser it into PsiElement and PsiTree based on language grammar.  
GdScript uses pure .kt parsers, while Scene and Project are using .bnf grammar as they are context free grammar unlike GdScript.

Parser matches incoming tokens against rules f.e.: tokens CLASS_NAME IDENTIFIER does match ClassNameParser.kt which check that there is CLASS_NAME token - consumes it, and then checks if next token is IDENTIFIER (some text). If yes then great - IDENTIFIER as marked as ClassNameNmiElement and both of them together as ClassNamingElement and added into PsiTree -> if there wasn't an identifier than instead it adds an error marker.

Resulting PsiTree, and it's PsiElements are used for pretty much everything else - Indexes for quick search, completion hints, LineMarkers, formatters, references, in-editor documentation, ...  
All of which are invoked by editor itself when required.

For more in-depth explanations please check official [tutorial](https://plugins.jetbrains.com/docs/intellij/custom-language-support.html)  
