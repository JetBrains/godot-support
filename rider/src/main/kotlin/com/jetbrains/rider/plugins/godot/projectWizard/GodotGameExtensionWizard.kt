package com.jetbrains.rider.plugins.godot.projectWizard

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.GeneratorNewProjectWizard
import com.intellij.ide.wizard.NewProjectWizardChainStep.Companion.nextStep
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.ide.wizard.RootNewProjectWizardStep
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.adapter.RiderProjectWizardMode
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.adapter.WizardMode
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.RiderGitNewProjectWizardStep
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.riderNewProjectWizardBaseStep
import javax.swing.Icon

class GodotGameExtensionWizard : GeneratorNewProjectWizard, RiderProjectWizardMode {

    override val id: String = "Rider.GodotGameExtension"

    override val name: String = GodotPluginBundle.message("wizard.godot.game.extension.name")

    override val icon: Icon = GodotIcons.Icons.GodotLogoGray

    override val ordinal: Int = 201

    override val description: String = GodotPluginBundle.message("wizard.godot.game.extension.description")

    override val groupName: String = GodotPluginBundle.message("wizard.godot.editor.extension.group")

    override val wizardMode: WizardMode = WizardMode.NEW_SOLUTION_ONLY

    override fun createStep(context: WizardContext): NewProjectWizardStep =
        RootNewProjectWizardStep(context)
            .nextStep { riderNewProjectWizardBaseStep(it).also { step -> step.defaultName = "MyGameExtension" } }
            .nextStep { RiderGitNewProjectWizardStep(it, defaultEnabled = true) }
            .nextStep(::GodotGameExtensionAssetsStep)
}
