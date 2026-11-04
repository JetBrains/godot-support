package com.jetbrains.godot.gdscript

import com.intellij.model.Symbol
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.polySymbols.testFramework.HybridFixtureContext
import com.intellij.polySymbols.testFramework.psiSymbolReferences
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import com.intellij.testFramework.junit5.fixture.TestFixture
import org.jetbrains.annotations.TestOnly
import org.junit.jupiter.api.Assertions.assertEquals
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.readText

/**
 * Creates a [GdResolveFixture] for the test data directory [testCasePath]. Declare the result as an instance property.
 */
@TestOnly
fun gdResolveFixture(testCasePath: String): TestFixture<GdResolveFixture> =
    gdLightFixture { GdResolveFixture(it, testCasePath) }

/**
 * A [GdFixture] for resolve tests that:
 * - sets test data path to testData/gdscript/resolve (via the [testCasePath] fixture argument)
 * - provides helpers to load files by test name and compare with a golden file
 * - exposes minimal resolve utilities aimed at reference-at-offset scenarios
 * - provides utilities to walk a file, collect all references, and resolve them
 */
open class GdResolveFixture(context: HybridFixtureContext, testCasePath: String) : GdFixture(context, testCasePath) {

    fun loadByTestName(): PsiFile = fixture.configureByFile(getTestName(false) + ".gd")

    /**
     * Loads test files for the current test name.
     * If a subdirectory named exactly as the test (e.g., testName/) exists under testData/..., all .gd files from it
     * are loaded (sorted by filename) and returned. Otherwise, falls back to a single file testName.gd.
     */
    fun loadFilesByTestName(): Array<PsiFile> {
        val basePath = fixture.testDataPath
        val testName = getTestName(false)
        val dir = Path.of(basePath, testName)
        if (dir.isDirectory()) {
            val gdFiles = dir.listDirectoryEntries().filter { it.isRegularFile() && it.name.endsWith(".gd") }.sortedBy { it.name }
            if (gdFiles.isNotEmpty()) {
                val relPaths = gdFiles.map { testName + "/" + it.name }.toTypedArray()
                return fixture.configureByFiles(*relPaths)
            }
        }
        return arrayOf(loadByTestName())
    }

    fun readGold(): String {
        val expectedPath = fixture.testDataPath + "/" + getTestName(false) + ".txt"
        return Path.of(expectedPath).readText()
    }

    fun assertGold(actual: String) {
        assertEquals(readGold().trim(), actual.trim())
    }

    /**
     * Produce the original file text annotated with inline numbered markers for every reference,
     * followed by a details section mapping each number to its resolve result.
     * Example output:
     *   ...source...[1]...
     *
     *   1. [`offset] name -> resolved:TargetName
     */
    fun dumpResolvesWithInlineMarkers(file: PsiFile): String {
        data class Item(
            val start: Int,
            val end: Int,
            val ref: PsiSymbolReference,
            val target: Symbol?,
            val canon: String
        )
        // Collect references and their resolve targets
        val prelim = mutableListOf<Triple<Int, PsiSymbolReference, Symbol?>>()
        file.accept(object : PsiRecursiveElementWalkingVisitor() {
            override fun visitElement(element: PsiElement) {
                val refs = element.psiSymbolReferences()
                if (refs.isNotEmpty()) {
                    val baseOffset = element.textRange.startOffset
                    for (ref in refs) {
                        val refStart = baseOffset + ref.rangeInElement.startOffset
                        val target = runCatching { ref.resolveReference() }
                            .getOrNull()
                            ?.singleOrNull()
                        prelim += Triple(refStart, ref, target)
                    }
                }
                super.visitElement(element)
            }
        })
        val items = mutableListOf<Item>()
        for ([start, ref, target] in prelim.sortedBy { it.first }) {
            val element = ref.element
            val absStart = start
            val absEnd = element.textRange.startOffset + ref.rangeInElement.endOffset
            val canon = run {
                val text = element.text
                val s = ref.rangeInElement.startOffset
                val e = ref.rangeInElement.endOffset.coerceAtMost(element.textLength)
                text.substring(s, e)
            }
            items += Item(absStart, absEnd, ref, target, canon)
        }
        // Render annotated source by inserting markers after each reference span.
        // Markers contain the start offset and resolution status (r/u) to make diffs obvious.
        val src = file.text
        val annotated = StringBuilder()
        var last = 0
        items.forEach { it ->
            val insertAt = it.end.coerceAtMost(src.length)
            if (insertAt < last) return@forEach
            annotated.append(src, last, insertAt)
            val statusChar = if (it.target == null) 'u' else 'r'
            annotated.append('[').append(statusChar).append(']')
            last = insertAt
        }
        annotated.append(src, last, src.length)

        return annotated.toString()
    }

    /**
     * Dump resolves for multiple files. Each file is separated by a header with its name for stability.
     */
    fun dumpResolvesForFiles(files: Array<PsiFile>): String {
        val sb = StringBuilder()
        files.sortedBy { it.name }.forEachIndexed { idx, file ->
            if (idx > 0) sb.append("\n\n")
            sb.append("== ").append(file.name).append(" ==\n")
            sb.append(dumpResolvesWithInlineMarkers(file))
        }
        return sb.toString()
    }

    fun loadFilesFromSubdirAsProjectRoot(): Array<PsiFile> {
        val testName = getTestName(false)
        val dir = fixture.copyDirectoryToProject(testName, "")
        return fixture.configureByFiles(*dir.children.map { it.name }.toTypedArray())
    }
}
