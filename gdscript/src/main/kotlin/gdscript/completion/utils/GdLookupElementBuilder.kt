// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package gdscript.competion.utils

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.codeInsight.lookup.LookupElementRenderer
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.util.PsiUtilCore
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import org.jetbrains.annotations.Contract
import java.awt.Color
import java.util.Collections
import javax.swing.Icon

/**
 * Copied directly from com.intellij.codeInsight.lookup.LookupElementBuilder
 * The only change is a new create() method with different lookup string & object -> a hack to add extra String for partial text match completions
 *
 * @author peter
 *
 * @see com.intellij.codeInsight.completion.PrioritizedLookupElement
 */
class GdLookupElementBuilder private constructor(
    private val myLookupString: String,
    private val myObject: Any,
    val insertHandler: InsertHandler<LookupElement>? = null,
    val renderer: LookupElementRenderer<LookupElement>? = null,
    private val myExpensiveRenderer: LookupElementRenderer<LookupElement>? = null,
    private val myHardcodedPresentation: LookupElementPresentation? = null,
    private val myPsiElement: SmartPsiElementPointer<*>? = null,
    allLookupStrings: Set<String> = setOf(myLookupString),
    caseSensitive: Boolean = true
) : LookupElement() {
    private val myCaseSensitive: Boolean
    private val myAllLookupStrings: Set<String>

    init {
        myAllLookupStrings = Collections.unmodifiableSet(allLookupStrings)
        myCaseSensitive = caseSensitive
    }

    private fun cloneWithUserData(
        lookupString: String, `object`: Any,
        insertHandler: InsertHandler<LookupElement>?,
        renderer: LookupElementRenderer<LookupElement>?,
        expensiveRenderer: LookupElementRenderer<LookupElement>?,
        hardcodedPresentation: LookupElementPresentation?,
        psiElement: SmartPsiElementPointer<*>?,
        allLookupStrings: Set<String>,
        caseSensitive: Boolean
    ): GdLookupElementBuilder {
        val result = GdLookupElementBuilder(
            lookupString, `object`, insertHandler, renderer, expensiveRenderer,
            hardcodedPresentation, psiElement, allLookupStrings, caseSensitive
        )
        copyUserDataTo(result)
        return result
    }

    @ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure = true)
    @Deprecated("use {@link #withInsertHandler(InsertHandler)}")
    fun setInsertHandler(insertHandler: InsertHandler<LookupElement>?): GdLookupElementBuilder {
        return withInsertHandler(insertHandler)
    }

    @Contract(pure = true)
    fun withInsertHandler(insertHandler: InsertHandler<LookupElement>?): GdLookupElementBuilder {
        return cloneWithUserData(
            myLookupString, myObject, insertHandler,
            renderer, myExpensiveRenderer, myHardcodedPresentation,
            myPsiElement, myAllLookupStrings, myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun withRenderer(renderer: LookupElementRenderer<LookupElement>?): GdLookupElementBuilder {
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler, renderer, myExpensiveRenderer, myHardcodedPresentation,
            myPsiElement, myAllLookupStrings, myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun withExpensiveRenderer(expensiveRenderer: LookupElementRenderer<LookupElement>?): GdLookupElementBuilder {
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler,
            renderer, expensiveRenderer, myHardcodedPresentation,
            myPsiElement, myAllLookupStrings, myCaseSensitive
        )
    }

    override fun getAllLookupStrings(): Set<String> {
        return myAllLookupStrings
    }

    @ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure = true)
    @Deprecated("use {@link #withIcon(Icon)}")
    fun setIcon(icon: Icon?): GdLookupElementBuilder {
        return withIcon(icon)
    }

    @Contract(pure = true)
    fun withIcon(icon: Icon?): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.icon = icon
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
            myAllLookupStrings, myCaseSensitive
        )
    }

    private fun copyPresentation(): LookupElementPresentation {
        val presentation = LookupElementPresentation()
        if (myHardcodedPresentation != null) {
            presentation.copyFrom(myHardcodedPresentation)
        } else {
            presentation.itemText = myLookupString
        }
        return presentation
    }

    @Contract(pure = true)
    fun withLookupString(another: String): GdLookupElementBuilder {
        val set: MutableSet<String> = HashSet(myAllLookupStrings)
        set.add(another)
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler,
            renderer, myExpensiveRenderer, myHardcodedPresentation,
            myPsiElement, Collections.unmodifiableSet(set), myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun withLookupStrings(another: Collection<String>): GdLookupElementBuilder {
        val set: MutableSet<String> = HashSet(myAllLookupStrings.size + another.size)
        set.addAll(myAllLookupStrings)
        set.addAll(another)
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler,
            renderer, myExpensiveRenderer, myHardcodedPresentation,
            myPsiElement, Collections.unmodifiableSet(set), myCaseSensitive
        )
    }

    override fun isCaseSensitive(): Boolean {
        return myCaseSensitive
    }

    /**
     * @param caseSensitive if this lookup item should be completed in the same letter case as prefix
     * @return modified builder
     * @see com.intellij.codeInsight.completion.CompletionResultSet.caseInsensitive
     */
    @Contract(pure = true)
    fun withCaseSensitivity(caseSensitive: Boolean): GdLookupElementBuilder {
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler,
            renderer, myExpensiveRenderer, myHardcodedPresentation,
            myPsiElement, myAllLookupStrings, caseSensitive
        )
    }

    /**
     * Allows to pass custom PSI that will be returned from [.getPsiElement].
     */
    @Contract(pure = true)
    fun withPsiElement(psi: PsiElement?): GdLookupElementBuilder {
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler,
            renderer, myExpensiveRenderer, myHardcodedPresentation,
            if (psi == null) null else SmartPointerManager.createPointer(psi),
            myAllLookupStrings, myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun withItemTextForeground(itemTextForeground: Color): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.itemTextForeground = itemTextForeground
        return cloneWithUserData(
            myLookupString, myObject, insertHandler, null, myExpensiveRenderer, presentation,
            myPsiElement, myAllLookupStrings, myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun withItemTextUnderlined(underlined: Boolean): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.isItemTextUnderlined = underlined
        return cloneWithUserData(
            myLookupString, myObject, insertHandler, null, myExpensiveRenderer, presentation,
            myPsiElement, myAllLookupStrings, myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun withItemTextItalic(italic: Boolean): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.isItemTextItalic = italic
        return cloneWithUserData(
            myLookupString, myObject, insertHandler, null, myExpensiveRenderer, presentation,
            myPsiElement, myAllLookupStrings, myCaseSensitive
        )
    }

    @ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure = true)
    @Deprecated("use {@link #withTypeText(String)}")
    fun setTypeText(typeText: String?): GdLookupElementBuilder {
        return withTypeText(typeText)
    }

    @Contract(pure = true)
    fun withTypeText(typeText: String?): GdLookupElementBuilder {
        return withTypeText(typeText, false)
    }

    @Contract(pure = true)
    fun withTypeText(typeText: String?, grayed: Boolean): GdLookupElementBuilder {
        return withTypeText(typeText, null, grayed)
    }

    @Contract(pure = true)
    fun withTypeText(typeText: String?, typeIcon: Icon?, grayed: Boolean): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.setTypeText(typeText, typeIcon)
        presentation.isTypeGrayed = grayed
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
            myAllLookupStrings, myCaseSensitive
        )
    }

    fun withTypeIconRightAligned(typeIconRightAligned: Boolean): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.isTypeIconRightAligned = typeIconRightAligned
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
            myAllLookupStrings, myCaseSensitive
        )
    }

    @ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure = true)
    @Deprecated("use {@link #withPresentableText(String)}")
    fun setPresentableText(presentableText: String): GdLookupElementBuilder {
        return withPresentableText(presentableText)
    }

    @Contract(pure = true)
    fun withPresentableText(presentableText: String): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.itemText = presentableText
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
            myAllLookupStrings, myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun bold(): GdLookupElementBuilder {
        return withBoldness(true)
    }

    @Contract(pure = true)
    fun withBoldness(bold: Boolean): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.isItemTextBold = bold
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
            myAllLookupStrings, myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun strikeout(): GdLookupElementBuilder {
        return withStrikeoutness(true)
    }

    @Contract(pure = true)
    fun withStrikeoutness(strikeout: Boolean): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.isStrikeout = strikeout
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
            myAllLookupStrings, myCaseSensitive
        )
    }

    @ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure = true)
    @Deprecated("use {@link #withTailText(String)}")
    fun setTailText(tailText: String?): GdLookupElementBuilder {
        return withTailText(tailText)
    }

    @Contract(pure = true)
    fun withTailText(tailText: String?): GdLookupElementBuilder {
        return withTailText(tailText, false)
    }

    @ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure = true)
    @Deprecated("use {@link #withTailText(String, boolean)}")
    fun setTailText(tailText: String?, grayed: Boolean): GdLookupElementBuilder {
        return withTailText(tailText, grayed)
    }

    @Contract(pure = true)
    fun withTailText(tailText: String?, grayed: Boolean): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.setTailText(tailText, grayed)
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
            myAllLookupStrings, myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun appendTailText(tailText: String, grayed: Boolean): GdLookupElementBuilder {
        val presentation = copyPresentation()
        presentation.appendTailText(tailText, grayed)
        return cloneWithUserData(
            myLookupString, myObject,
            insertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
            myAllLookupStrings, myCaseSensitive
        )
    }

    @Contract(pure = true)
    fun withAutoCompletionPolicy(policy: AutoCompletionPolicy): LookupElement {
        return policy.applyPolicy(this)
    }

    override fun getLookupString(): String {
        return myLookupString
    }

    override fun getExpensiveRenderer(): LookupElementRenderer<out LookupElement>? {
        return myExpensiveRenderer
    }

    override fun getObject(): Any {
        return myObject
    }

    override fun getPsiElement(): PsiElement? {
        return if (myPsiElement != null) myPsiElement.element else super.getPsiElement()
    }

    override fun handleInsert(context: InsertionContext) {
        if (insertHandler != null) {
            insertHandler.handleInsert(context, this)
        }
    }

    override fun renderElement(presentation: LookupElementPresentation) {
        if (renderer != null) {
            renderer.renderElement(this, presentation)
        } else if (myHardcodedPresentation != null) {
            presentation.copyFrom(myHardcodedPresentation)
        } else {
            presentation.itemText = myLookupString
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GdLookupElementBuilder
        val insertHandler = that.insertHandler
        if (if (this.insertHandler != null && insertHandler != null) insertHandler.javaClass != insertHandler.javaClass else this.insertHandler !== insertHandler) return false
        if (myLookupString != that.myLookupString) return false
        if (myObject != that.myObject) return false
        val renderer = that.renderer
        return if (if (this.renderer != null && renderer != null) renderer.javaClass != renderer.javaClass else this.renderer !== renderer) false else true
    }

    override fun toString(): String {
        return "GdLookupElementBuilder: string=" + lookupString + "; handler=" + insertHandler
    }

    override fun hashCode(): Int {
        var result = 0
        result = 31 * result + if (insertHandler != null) insertHandler.javaClass.hashCode() else 0
        result = 31 * result + myLookupString.hashCode()
        result = 31 * result + myObject.hashCode()
        result = 31 * result + if (renderer != null) renderer.javaClass.hashCode() else 0
        return result
    }

    companion object {
        fun create(lookupString: String): GdLookupElementBuilder {
            return GdLookupElementBuilder(lookupString, lookupString)
        }

        fun create(lookupString: String, lookupObject: String): GdLookupElementBuilder {
            return GdLookupElementBuilder(lookupString, lookupObject)
        }

        fun create(`object`: Any): GdLookupElementBuilder {
            return GdLookupElementBuilder(`object`.toString(), `object`)
        }

        fun createWithSmartPointer(lookupString: String, element: PsiElement): GdLookupElementBuilder {
            PsiUtilCore.ensureValid(element)
            return GdLookupElementBuilder(
                lookupString,
                SmartPointerManager.getInstance(element.project).createSmartPsiElementPointer(element)
            )
        }

        fun create(element: PsiNamedElement): GdLookupElementBuilder {
            PsiUtilCore.ensureValid(element)
            return GdLookupElementBuilder(StringUtil.notNullize(element.name), element)
        }

        fun createWithIcon(element: PsiNamedElement): GdLookupElementBuilder {
            PsiUtilCore.ensureValid(element)
            return create(element).withIcon(element.getIcon(0))
        }

        fun create(lookupObject: Any, lookupString: String): GdLookupElementBuilder {
            if (lookupObject is PsiElement) {
                PsiUtilCore.ensureValid(lookupObject)
            }
            return GdLookupElementBuilder(lookupString, lookupObject)
        }
    }
}