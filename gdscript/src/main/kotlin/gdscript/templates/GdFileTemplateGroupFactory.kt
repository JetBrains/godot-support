package gdscript.templates

import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory
import com.intellij.rider.plugins.godot.community.icons.RiderPluginsGodotCommunityIcons
import javax.swing.Icon

/**
 * RIDER-130491 Editable gdscript filetemplates
 */
class GdFileTemplateGroupFactory : FileTemplateGroupDescriptorFactory {
  override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
    val groupName = "GDScript"
    val groupIcon: Icon = RiderPluginsGodotCommunityIcons.GDScript
    val group = FileTemplateGroupDescriptor(groupName, groupIcon)

    group.addTemplate(FileTemplateDescriptor("Node default.gd"))
    group.addTemplate(FileTemplateDescriptor("Object empty.gd"))
    group.addTemplate(FileTemplateDescriptor("CharacterBody2D basic_movement.gd"))
    group.addTemplate(FileTemplateDescriptor("CharacterBody3D basic_movement.gd"))
    group.addTemplate(FileTemplateDescriptor("EditorPlugin plugin.gd"))
    group.addTemplate(FileTemplateDescriptor("EditorScenePostImport basic_import_script.gd"))
    group.addTemplate(FileTemplateDescriptor("EditorScenePostImport no_comments.gd"))
    group.addTemplate(FileTemplateDescriptor("EditorScript basic_editor_script.gd"))
    group.addTemplate(FileTemplateDescriptor("VisualShaderNodeCustom basic.gd"))

    return group
  }
}
