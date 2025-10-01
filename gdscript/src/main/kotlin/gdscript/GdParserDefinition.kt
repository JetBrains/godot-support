package gdscript

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.stubs.PsiFileStub
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.IStubFileElementType
import com.intellij.psi.tree.TokenSet
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.parser.GdRootParser
import gdscript.psi.GdFile
import gdscript.psi.GdTypes

class GdParserDefinition : ParserDefinition {

    companion object {
        val COMMENTS = TokenSet.create(GdTypes.COMMENT, GdTypes.BACKSLASH)
        val STRING_LITERALS = TokenSet.create(GdTypes.STRING)
        val FILE = IStubFileElementType<PsiFileStub<GdFile>>("GdScriptFile", GdLanguage)
    }

    override fun createLexer(project: Project): Lexer {
        return GdLexerAdapter()
    }

    override fun getCommentTokens(): TokenSet {
        return COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return STRING_LITERALS
    }

    override fun createParser(project: Project): PsiParser {
        return GdRootParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return GdFile(viewProvider)
    }

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): SpaceRequirements {
        return SpaceRequirements.MAY
    }

    override fun createElement(node: ASTNode): PsiElement {
        return GdTypes.Factory.createElement(node)
    }

}
