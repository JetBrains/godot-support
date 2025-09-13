package com.jetbrains.godot.gdscript.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import java.io.File
import kotlin.io.path.pathString

/**
 * A small base for resolve tests that:
 * - sets test data path to testData/gdscript/resolve
 * - provides helpers to load files by test name and compare with a golden file
 * - exposes minimal resolve utilities aimed at reference-at-offset scenarios
 * - provides utilities to walk a file, collect all references, and resolve them
 */
abstract class ResolveTestBase : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/resolve").pathString
    }

    protected fun loadByTestName(): PsiFile = myFixture.configureByFile(getTestName(false) + ".gd")

    /**
     * Loads test files for the current test name.
     * If a subdirectory named exactly as the test (e.g., testName/) exists under testData/..., all .gd files from it
     * are loaded (sorted by filename) and returned. Otherwise, falls back to a single file testName.gd.
     */
    protected fun loadFilesByTestName(): Array<PsiFile> {
        val basePath = myFixture.getTestDataPath()
        val testName = getTestName(false)
        val dir = File(basePath, testName)
        if (dir.isDirectory) {
            val gdFiles = dir.listFiles { f -> f.isFile && f.name.endsWith(".gd") }?.sortedBy { it.name } ?: emptyList()
            if (gdFiles.isNotEmpty()) {
                val relPaths = gdFiles.map { testName + "/" + it.name }.toTypedArray()
                return myFixture.configureByFiles(*relPaths)
            }
        }
        return arrayOf(loadByTestName())
    }

    protected fun readGold(): String {
        val expectedPath = myFixture.getTestDataPath() + "/" + getTestName(false) + ".txt"
        return File(expectedPath).readText()
    }

    protected fun assertGold(actual: String) {
        assertEquals(readGold().trim(), actual.trim())
    }

    /**
     * Walks the PSI tree of the given file, collects ALL PsiReference instances from every element,
     * calls resolve() on each, and returns the list of pairs (reference, resolvedTargetOrNull).
     * The references are ordered by their absolute start offset in the file for stability.
     */
    protected fun resolveAllReferences(file: PsiFile): List<Pair<PsiReference, PsiElement?>> {
        val entries = mutableListOf<Triple<Int, PsiReference, PsiElement?>>()
        file.accept(object : PsiRecursiveElementWalkingVisitor() {
            override fun visitElement(element: PsiElement) {
                // Collect references present on this element
                val refs = element.references
                if (refs.isNotEmpty()) {
                    val baseOffset = element.textRange.startOffset
                    for (ref in refs) {
                        val refStart = baseOffset + ref.rangeInElement.startOffset
                        val target = kotlin.runCatching { ref.resolve() }.getOrNull()
                        entries += Triple(refStart, ref, target)
                    }
                }
                super.visitElement(element)
            }
        })
        return entries.sortedBy { it.first }.map { it.second to it.third }
    }

    /**
     * Produce the original file text annotated with inline numbered markers for every reference,
     * followed by a details section mapping each number to its resolve result.
     * Example output:
     *   ...source...[1]...
     *
     *   1. [`offset] name -> resolved:TargetName
     */
    protected fun dumpResolvesWithInlineMarkers(file: PsiFile): String {
        data class Item(
            val start: Int,
            val end: Int,
            val ref: PsiReference,
            val target: PsiElement?,
            val canon: String
        )
        // Collect references and their resolve targets
        val prelim = mutableListOf<Triple<Int, PsiReference, PsiElement?>>()
        file.accept(object : PsiRecursiveElementWalkingVisitor() {
            override fun visitElement(element: PsiElement) {
                val refs = element.references
                if (refs.isNotEmpty()) {
                    val baseOffset = element.textRange.startOffset
                    for (ref in refs) {
                        val refStart = baseOffset + ref.rangeInElement.startOffset
                        val target = kotlin.runCatching { ref.resolve() }.getOrNull()
                        prelim += Triple(refStart, ref, target)
                    }
                }
                super.visitElement(element)
            }
        })
        val items = mutableListOf<Item>()
        for ((start, ref, target) in prelim.sortedBy { it.first }) {
            val element = ref.element
            val absStart = start
            val absEnd = element.textRange.startOffset + ref.rangeInElement.endOffset
            val canon = kotlin.runCatching { ref.canonicalText }.getOrElse {
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
            annotated.append('[').append(it.start).append(statusChar).append(']')
            last = insertAt
        }
        annotated.append(src, last, src.length)

        return annotated.toString()
    }

    /**
     * Dump resolves for multiple files. Each file is separated by a header with its name for stability.
     */
    protected fun dumpResolvesForFiles(files: Array<PsiFile>): String {
        val sb = StringBuilder()
        files.sortedBy { it.name }.forEachIndexed { idx, file ->
            if (idx > 0) sb.append("\n\n")
            sb.append("== ").append(file.name).append(" ==\n")
            sb.append(dumpResolvesWithInlineMarkers(file))
        }
        return sb.toString()
    }
}
