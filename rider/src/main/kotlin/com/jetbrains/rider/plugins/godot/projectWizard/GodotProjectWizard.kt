package com.jetbrains.rider.plugins.godot.projectWizard

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.NewProjectWizardChainStep.Companion.nextStep
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.ide.wizard.RootNewProjectWizardStep
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.projectView.projectTemplates.ProjectTemplatesCollector
import com.jetbrains.rider.projectView.projectTemplates.templateTypes.GameDevTemplateType
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.adapter.RiderProjectWizardMode
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.adapter.RiderWizardFusInfo
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.adapter.WizardMode
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.RiderNewProjectWizardBaseStep
import javax.swing.Icon

class GodotProjectWizard : RiderProjectWizardMode, RiderWizardFusInfo, GameDevTemplateType {

    override val id: String = "Rider.GodotProject"

    override val name: String = GodotPluginBundle.message("wizard.godot.project.name")

    override val icon: Icon = GodotIcons.Icons.GodotLogoGray

    override val description: String = GodotPluginBundle.message("wizard.godot.project.description")

    override val wizardMode: WizardMode = WizardMode.NEW_SOLUTION_ONLY

    override val fusProjectType: ProjectTemplatesCollector.ProjectType = ProjectTemplatesCollector.ProjectType.Godot

    override fun createStep(context: WizardContext): NewProjectWizardStep =
        RootNewProjectWizardStep(context)
            .nextStep { RiderNewProjectWizardBaseStep(it).also { step ->
                step.defaultName = "godot-sample" } }
            .nextStep(::GodotProjectAssetsStep)
}
