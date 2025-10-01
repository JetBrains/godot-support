package project

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
import project.parser.ProjectParser
import project.psi.ProjectFile
import project.psi.ProjectTypes

class ProjectParserDefinition : ParserDefinition {

    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS = TokenSet.create(ProjectTypes.COMMENT)
        val FILE = IStubFileElementType<PsiFileStub<ProjectFile>>("GdScriptProjectFile", ProjectLanguage)
    }

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun createLexer(project: Project?): Lexer = ProjectLexerAdapter()

    override fun createParser(project: Project?): PsiParser = ProjectParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun createElement(node: ASTNode?): PsiElement = ProjectTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = ProjectFile(viewProvider)

}
