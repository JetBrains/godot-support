// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package gdscript.competion.utils;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.AutoCompletionPolicy;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Copied directly from com.intellij.codeInsight.lookup.LookupElementBuilder
 * The only change is a new create() method with different lookup string & object -> a hack to add extra String for partial text match completions
 *
 * @author peter
 *
 * @see com.intellij.codeInsight.completion.PrioritizedLookupElement
 */
public final class GdLookupElementBuilder extends LookupElement {
    @NotNull private final String myLookupString;
    @NotNull private final Object myObject;
    @Nullable private final SmartPsiElementPointer<?> myPsiElement;
    private final boolean myCaseSensitive;
    @Nullable private final InsertHandler<LookupElement> myInsertHandler;
    @Nullable private final LookupElementRenderer<LookupElement> myRenderer;
    @Nullable private final LookupElementRenderer<LookupElement> myExpensiveRenderer;
    @Nullable private final LookupElementPresentation myHardcodedPresentation;
    @NotNull private final Set<String> myAllLookupStrings;

    private GdLookupElementBuilder(@NotNull String lookupString, @NotNull Object object, @Nullable InsertHandler<LookupElement> insertHandler,
                                 @Nullable LookupElementRenderer<LookupElement> renderer,
                                 @Nullable LookupElementRenderer<LookupElement> expensiveRenderer,
                                 @Nullable LookupElementPresentation hardcodedPresentation,
                                 @Nullable SmartPsiElementPointer<?> psiElement,
                                 @NotNull Set<String> allLookupStrings,
                                 boolean caseSensitive) {
        myLookupString = lookupString;
        myObject = object;
        myInsertHandler = insertHandler;
        myRenderer = renderer;
        myExpensiveRenderer = expensiveRenderer;
        myHardcodedPresentation = hardcodedPresentation;
        myPsiElement = psiElement;
        myAllLookupStrings = Collections.unmodifiableSet(allLookupStrings);
        myCaseSensitive = caseSensitive;
    }

    private GdLookupElementBuilder(@NotNull String lookupString, @NotNull Object object) {
        this(lookupString, object, null, null, null, null, null, Collections.singleton(lookupString), true);
    }

    public static @NotNull GdLookupElementBuilder create(@NotNull String lookupString) {
        return new GdLookupElementBuilder(lookupString, lookupString);
    }

    public static @NotNull GdLookupElementBuilder create(@NotNull String lookupString, @NotNull String lookupObject) {
        return new GdLookupElementBuilder(lookupString, lookupObject);
    }

    public static @NotNull GdLookupElementBuilder create(@NotNull Object object) {
        return new GdLookupElementBuilder(object.toString(), object);
    }

    public static @NotNull GdLookupElementBuilder createWithSmartPointer(@NotNull String lookupString, @NotNull PsiElement element) {
        PsiUtilCore.ensureValid(element);
        return new GdLookupElementBuilder(lookupString,
                SmartPointerManager.getInstance(element.getProject()).createSmartPsiElementPointer(element));
    }

    public static @NotNull GdLookupElementBuilder create(@NotNull PsiNamedElement element) {
        PsiUtilCore.ensureValid(element);
        return new GdLookupElementBuilder(StringUtil.notNullize(element.getName()), element);
    }

    public static @NotNull GdLookupElementBuilder createWithIcon(@NotNull PsiNamedElement element) {
        PsiUtilCore.ensureValid(element);
        return create(element).withIcon(element.getIcon(0));
    }

    public static @NotNull GdLookupElementBuilder create(@NotNull Object lookupObject, @NotNull String lookupString) {
        if (lookupObject instanceof PsiElement) {
            PsiUtilCore.ensureValid((PsiElement)lookupObject);
        }
        return new GdLookupElementBuilder(lookupString, lookupObject);
    }

    private @NotNull GdLookupElementBuilder cloneWithUserData(@NotNull String lookupString, @NotNull Object object,
                                                            @Nullable InsertHandler<LookupElement> insertHandler,
                                                            @Nullable LookupElementRenderer<LookupElement> renderer,
                                                            @Nullable LookupElementRenderer<LookupElement> expensiveRenderer,
                                                            @Nullable LookupElementPresentation hardcodedPresentation,
                                                            @Nullable SmartPsiElementPointer<?> psiElement,
                                                            @NotNull Set<String> allLookupStrings,
                                                            boolean caseSensitive) {
        GdLookupElementBuilder result = new GdLookupElementBuilder(lookupString, object, insertHandler, renderer, expensiveRenderer,
                hardcodedPresentation, psiElement, allLookupStrings, caseSensitive);
        copyUserDataTo(result);
        return result;
    }

    /**
     * @deprecated use {@link #withInsertHandler(InsertHandler)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder setInsertHandler(@Nullable InsertHandler<LookupElement> insertHandler) {
        return withInsertHandler(insertHandler);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withInsertHandler(@Nullable InsertHandler<LookupElement> insertHandler) {
        return cloneWithUserData(myLookupString, myObject, insertHandler, myRenderer, myExpensiveRenderer, myHardcodedPresentation,
                myPsiElement, myAllLookupStrings, myCaseSensitive);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withRenderer(@Nullable LookupElementRenderer<LookupElement> renderer) {
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, renderer, myExpensiveRenderer, myHardcodedPresentation,
                myPsiElement, myAllLookupStrings, myCaseSensitive);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withExpensiveRenderer(@Nullable LookupElementRenderer<LookupElement> expensiveRenderer) {
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, myRenderer, expensiveRenderer, myHardcodedPresentation,
                myPsiElement, myAllLookupStrings, myCaseSensitive);
    }

    @Override
    @NotNull
    public Set<String> getAllLookupStrings() {
        return myAllLookupStrings;
    }

    /**
     * @deprecated use {@link #withIcon(Icon)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder setIcon(@Nullable Icon icon) {
        return withIcon(icon);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withIcon(@Nullable Icon icon) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setIcon(icon);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
                myAllLookupStrings, myCaseSensitive);
    }

    @NotNull
    private LookupElementPresentation copyPresentation() {
        final LookupElementPresentation presentation = new LookupElementPresentation();
        if (myHardcodedPresentation != null) {
            presentation.copyFrom(myHardcodedPresentation);
        } else {
            presentation.setItemText(myLookupString);
        }
        return presentation;
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withLookupString(@NotNull String another) {
        final Set<String> set = new HashSet<>(myAllLookupStrings);
        set.add(another);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, myRenderer, myExpensiveRenderer, myHardcodedPresentation,
                myPsiElement, Collections.unmodifiableSet(set), myCaseSensitive);
    }
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withLookupStrings(@NotNull Collection<String> another) {
        Set<String> set = new HashSet<>(myAllLookupStrings.size() + another.size());
        set.addAll(myAllLookupStrings);
        set.addAll(another);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, myRenderer, myExpensiveRenderer, myHardcodedPresentation,
                myPsiElement, Collections.unmodifiableSet(set), myCaseSensitive);
    }

    @Override
    public boolean isCaseSensitive() {
        return myCaseSensitive;
    }

    /**
     * @param caseSensitive if this lookup item should be completed in the same letter case as prefix
     * @return modified builder
     * @see com.intellij.codeInsight.completion.CompletionResultSet#caseInsensitive()
     */
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withCaseSensitivity(boolean caseSensitive) {
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, myRenderer, myExpensiveRenderer, myHardcodedPresentation,
                myPsiElement, myAllLookupStrings, caseSensitive);
    }

    /**
     * Allows to pass custom PSI that will be returned from {@link #getPsiElement()}.
     */
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withPsiElement(@Nullable PsiElement psi) {
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, myRenderer, myExpensiveRenderer, myHardcodedPresentation,
                psi == null ? null : SmartPointerManager.createPointer(psi),
                myAllLookupStrings, myCaseSensitive);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withItemTextForeground(@NotNull Color itemTextForeground) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setItemTextForeground(itemTextForeground);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation,
                myPsiElement, myAllLookupStrings, myCaseSensitive);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withItemTextUnderlined(boolean underlined) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setItemTextUnderlined(underlined);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation,
                myPsiElement, myAllLookupStrings, myCaseSensitive);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withItemTextItalic(boolean italic) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setItemTextItalic(italic);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation,
                myPsiElement, myAllLookupStrings, myCaseSensitive);
    }

    /**
     * @deprecated use {@link #withTypeText(String)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder setTypeText(@Nullable String typeText) {
        return withTypeText(typeText);
    }
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withTypeText(@Nullable String typeText) {
        return withTypeText(typeText, false);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withTypeText(@Nullable String typeText, boolean grayed) {
        return withTypeText(typeText, null, grayed);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withTypeText(@Nullable String typeText, @Nullable Icon typeIcon, boolean grayed) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setTypeText(typeText, typeIcon);
        presentation.setTypeGrayed(grayed);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
                myAllLookupStrings, myCaseSensitive);
    }

    public @NotNull GdLookupElementBuilder withTypeIconRightAligned(boolean typeIconRightAligned) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setTypeIconRightAligned(typeIconRightAligned);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
                myAllLookupStrings, myCaseSensitive);
    }

    /**
     * @deprecated use {@link #withPresentableText(String)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder setPresentableText(@NotNull String presentableText) {
        return withPresentableText(presentableText);
    }
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withPresentableText(@NotNull String presentableText) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setItemText(presentableText);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
                myAllLookupStrings, myCaseSensitive);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder bold() {
        return withBoldness(true);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withBoldness(boolean bold) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setItemTextBold(bold);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
                myAllLookupStrings, myCaseSensitive);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder strikeout() {
        return withStrikeoutness(true);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withStrikeoutness(boolean strikeout) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setStrikeout(strikeout);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
                myAllLookupStrings, myCaseSensitive);
    }

    /**
     * @deprecated use {@link #withTailText(String)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder setTailText(@Nullable String tailText) {
        return withTailText(tailText);
    }
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withTailText(@Nullable String tailText) {
        return withTailText(tailText, false);
    }

    /**
     * @deprecated use {@link #withTailText(String, boolean)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2021.3")
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder setTailText(@Nullable String tailText, boolean grayed) {
        return withTailText(tailText, grayed);
    }
    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder withTailText(@Nullable String tailText, boolean grayed) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.setTailText(tailText, grayed);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
                myAllLookupStrings, myCaseSensitive);
    }

    @Contract(pure=true)
    public @NotNull GdLookupElementBuilder appendTailText(@NotNull String tailText, boolean grayed) {
        final LookupElementPresentation presentation = copyPresentation();
        presentation.appendTailText(tailText, grayed);
        return cloneWithUserData(myLookupString, myObject, myInsertHandler, null, myExpensiveRenderer, presentation, myPsiElement,
                myAllLookupStrings, myCaseSensitive);
    }

    @Contract(pure=true)
    public LookupElement withAutoCompletionPolicy(AutoCompletionPolicy policy) {
        return policy.applyPolicy(this);
    }

    @NotNull
    @Override
    public String getLookupString() {
        return myLookupString;
    }

    @Nullable
    public InsertHandler<LookupElement> getInsertHandler() {
        return myInsertHandler;
    }

    @Nullable
    public LookupElementRenderer<LookupElement> getRenderer() {
        return myRenderer;
    }

    @Override
    public @Nullable LookupElementRenderer<? extends LookupElement> getExpensiveRenderer() {
        return myExpensiveRenderer;
    }

    @NotNull
    @Override
    public Object getObject() {
        return myObject;
    }

    @Nullable
    @Override
    public PsiElement getPsiElement() {
        if (myPsiElement != null) return myPsiElement.getElement();
        return super.getPsiElement();
    }

    @Override
    public void handleInsert(@NotNull InsertionContext context) {
        if (myInsertHandler != null) {
            myInsertHandler.handleInsert(context, this);
        }
    }

    @Override
    public void renderElement(LookupElementPresentation presentation) {
        if (myRenderer != null) {
            myRenderer.renderElement(this, presentation);
        }
        else if (myHardcodedPresentation != null) {
            presentation.copyFrom(myHardcodedPresentation);
        } else {
            presentation.setItemText(myLookupString);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GdLookupElementBuilder that = (GdLookupElementBuilder)o;

        final InsertHandler<LookupElement> insertHandler = that.myInsertHandler;
        if (myInsertHandler != null && insertHandler != null ? !myInsertHandler.getClass().equals(insertHandler.getClass())
                : myInsertHandler != insertHandler) return false;
        if (!myLookupString.equals(that.myLookupString)) return false;
        if (!myObject.equals(that.myObject)) return false;

        final LookupElementRenderer<LookupElement> renderer = that.myRenderer;
        if (myRenderer != null && renderer != null ? !myRenderer.getClass().equals(renderer.getClass()) : myRenderer != renderer) return false;

        return true;
    }

    @Override
    public String toString() {
        return "GdLookupElementBuilder: string=" + getLookupString() + "; handler=" + myInsertHandler;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (myInsertHandler != null ? myInsertHandler.getClass().hashCode() : 0);
        result = 31 * result + (myLookupString.hashCode());
        result = 31 * result + (myObject.hashCode());
        result = 31 * result + (myRenderer != null ? myRenderer.getClass().hashCode() : 0);
        return result;
    }

}
