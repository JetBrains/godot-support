using System;
using System.Collections.Generic;
using System.Linq;
using JetBrains.Application.BuildScript;
using JetBrains.Application.BuildScript.Compile;
using JetBrains.Application.BuildScript.Solution;
using JetBrains.Build;
using JetBrains.Build.Helpers.TeamCity;
using JetBrains.Util;
using JetBrains.Util.Storage;

namespace JetBrains.ReSharper.Plugins.Godot.BuildScript
{
  public class CopyGodotAnnotations
  {
    [BuildStep]
    public static SubplatformFileForPackagingFast[] Run(AllAssembliesOnEverything allass, ProductHomeDirArtifact homedir)
    {
      if (allass.FindSubplatformByClass<CopyGodotAnnotations>() is SubplatformOnSources subplatform)
      {
        FileSystemPath dirAnnotations = homedir.ProductHomeDir / subplatform.Name.RelativePath / "src" / "annotations";
        return dirAnnotations.GetChildFiles().SelectMany(CopyFileToOutputRequest).ToArray();

        IEnumerable<SubplatformFileForPackagingFast> CopyFileToOutputRequest(FileSystemPath path)
        {
          yield return new SubplatformFileForPackagingFast(
            subplatform.Name,
            // "com.intellij.rider.godot" is plugin id
            ImmutableFileItem.CreateFromDisk(path).WithRelativePath((RelativePath)"Extensions" / 
                                                                    "com.intellij.rider.godot" / 
                                                                    "annotations" / path.Name)); 

          // see ExtensionsExternalAnnotationsFileProvider
          // HACK similar to the one in `CopyUnityAnnotations`, only for running locally
          if (!TeamCityProperties.GetIsRunningInTeamCity())
          {
              // "JetBrains.Plugins.godot-support.resharper" is a JetBrains.Application.BuildScript.Application.IApplicationPackage.Id
              // from the JetBrains.Application.ApplicationPackagesFilesEx.GetExtensionsFiles
              yield return new SubplatformFileForPackagingFast(
                  subplatform.Name,
                  ImmutableFileItem.CreateFromDisk(path).WithRelativePath((RelativePath)"Extensions" /
                                                                          "JetBrains.Plugins.godot-support.resharper" /
                                                                          "annotations" / path.Name));
          }
        }
      }

      return Array.Empty<SubplatformFileForPackagingFast>();
    }
  }
}