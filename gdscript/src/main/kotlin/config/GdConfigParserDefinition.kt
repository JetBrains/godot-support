package config

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
import config.parser.ConfigParser
import config.psi.GdConfigFile
import config.psi.GdConfigTypes

class GdConfigParserDefinition : ParserDefinition {

    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS = TokenSet.create()
        val FILE = IStubFileElementType<PsiFileStub<GdConfigFile>>("GdConfigFile", GdConfigLanguage)
    }

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun createLexer(project: Project?): Lexer = GdConfigLexerAdapter()

    override fun createParser(project: Project?): PsiParser = ConfigParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun createElement(node: ASTNode?): PsiElement = GdConfigTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = GdConfigFile(viewProvider)

}
