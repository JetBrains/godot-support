package gdscript.utils

import com.intellij.lexer.FilterLexer
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.intellij.psi.TokenType
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.concurrency.annotations.RequiresReadLock
import com.jetbrains.rider.godot.community.GodotProjectSettingsProvider
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import project.psi.ProjectFile
import project.psi.ProjectSection
import tscn.TscnLexerAdapter
import tscn.psi.TscnTypes

class GdScriptGodotProjectSettingsProvider : GodotProjectSettingsProvider {
    @RequiresReadLock
    override fun getEnabledEditorPlugins(project: Project): Set<String>? {
        val basePath = GodotCommunityUtil.getGodotProjectBasePath(project) ?: return null
        val virtualFile = LocalFileSystem.getInstance().findFileByNioFile(basePath.resolve("project.godot")) ?: return null
        val file = PsiManager.getInstance(project).findFile(virtualFile) as? ProjectFile ?: return null
        return CachedValuesManager.getCachedValue(file) {
            val section = PsiTreeUtil.getStubChildrenOfTypeAsList(file, ProjectSection::class.java)
                .lastOrNull { it.name == "[editor_plugins]" }
            val value = section?.dataList?.lastOrNull { it.key.trim() == "enabled" }?.value
            if (value == null) {
                CachedValueProvider.Result.create(emptySet(), file)
            } else {
                CachedValueProvider.Result.create(parseEnabledEditorPlugins(value), file)
            }

        }
    }

    private fun parseEnabledEditorPlugins(value: String): Set<String>? {
        val lexer = FilterLexer(TscnLexerAdapter()) { it == TokenType.WHITE_SPACE || it == TscnTypes.COMMENT }
        lexer.start(value)
        if (lexer.tokenText != "PackedStringArray") return null
        lexer.advance()
        if (lexer.tokenType != TscnTypes.LRBR) return null
        lexer.advance()

        val paths = linkedSetOf<String>()
        while (lexer.tokenType != TscnTypes.RRBR) {
            if (lexer.tokenType != TscnTypes.STRING) return null
            val literal = lexer.tokenText
            if (!literal.startsWith('"')) return null
            paths.add(StringUtil.unescapeStringCharacters(literal.substring(1, literal.length - 1)))
            lexer.advance()
            if (lexer.tokenType != TscnTypes.COMMA) break
            lexer.advance()
        }

        if (lexer.tokenType != TscnTypes.RRBR) return null
        lexer.advance()
        return paths.takeIf { lexer.tokenType == null }
    }
}