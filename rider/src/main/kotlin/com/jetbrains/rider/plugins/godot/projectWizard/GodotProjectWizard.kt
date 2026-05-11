package com.jetbrains.rider.plugins.godot.projectWizard

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.GeneratorNewProjectWizard
import com.intellij.ide.wizard.NewProjectWizardChainStep.Companion.nextStep
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.ide.wizard.RootNewProjectWizardStep
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.projectView.projectTemplates.ProjectTemplatesCollector
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.adapter.RiderProjectWizardMode
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.adapter.RiderWizardFusInfo
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.adapter.WizardMode
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.RiderGitNewProjectWizardStep
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.riderNewProjectWizardBaseStep
import javax.swing.Icon

class GodotProjectWizard : GeneratorNewProjectWizard, RiderProjectWizardMode, RiderWizardFusInfo {

    override val id: String = "Rider.GodotProject"

    override val name: String = GodotPluginBundle.message("wizard.godot.project.name")

    override val icon: Icon = GodotIcons.Icons.GodotLogoGray

    override val ordinal: Int = 200

    override val description: String = GodotPluginBundle.message("wizard.godot.project.description")

    override val groupName: String = GodotPluginBundle.message("wizard.godot.editor.extension.group")

    override val wizardMode: WizardMode = WizardMode.NEW_SOLUTION_ONLY

    override val fusProjectType = ProjectTemplatesCollector.ProjectType.Godot

    override fun createStep(context: WizardContext): NewProjectWizardStep =
        RootNewProjectWizardStep(context)
            .nextStep { riderNewProjectWizardBaseStep(it).also { step ->
                step.defaultName = GodotPluginBundle.message("wizard.godot.project.name").replace(" ", "-") } }
            .nextStep { RiderGitNewProjectWizardStep(it, defaultEnabled = true) }
            .nextStep(::GodotProjectAssetsStep)
}
