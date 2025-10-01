package tscn

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.stubs.PsiFileStub
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.IStubFileElementType
import com.intellij.psi.tree.TokenSet
import tscn.parser.TscnParser
import tscn.psi.TscnFile
import tscn.psi.TscnTypes

class TscnParserDefinition : ParserDefinition {

    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS = TokenSet.create(TscnTypes.COMMENT)
        val FILE = IStubFileElementType<PsiFileStub<TscnFile>>("GdScriptTscnFile", TscnLanguage)
    }

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun createLexer(project: Project?): Lexer = TscnLexerAdapter()

    override fun createParser(project: Project?): PsiParser = TscnParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun createElement(node: ASTNode?): PsiElement = TscnTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = TscnFile(viewProvider)

}
