using System;
using JetBrains.Application.BuildScript.Compile;
using JetBrains.Application.BuildScript.PackageSpecification;
using JetBrains.Application.BuildScript.Solution;
using JetBrains.Build;
using JetBrains.Rider.Backend.BuildScript;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.BuildScript
{
  /// <summary>
  ///   Defines a bundled plugin which drives adding the referenced packages as a plugin for Rider.
  /// </summary>
  public class GodotInRiderProduct
  {
    public static readonly SubplatformName ThisSubplatformName = new((RelativePath)"Plugins" / "godot-support" / "resharper");

    public static readonly RelativePath DotFilesFolder = @"plugins\rider-plugins-godot\dotnet";

    public const string ProductTechnicalName = "Godot";

    [BuildStep]
    public static SubplatformComponentForPackagingFast[] ProductMetaDependency(AllAssembliesOnSources allassSrc)
    {
      if (!allassSrc.Has(ThisSubplatformName))
        return Array.Empty<SubplatformComponentForPackagingFast>();

      return new[]
      {
        new SubplatformComponentForPackagingFast
        (
          ThisSubplatformName,
          new JetPackageMetadata
          {
            Spec = new JetSubplatformSpec
            {
              ComplementedProductName = RiderConstants.ProductTechnicalName
            }
          }
        )
      };
    }
  }
  
  public class GodotDebuggerProduct
  {
      public static readonly SubplatformName SubplatformName = new((RelativePath)"Plugins" / "godot-support" / "debugger" / "debugger-worker");

      public static readonly RelativePath PluginFolder = @"plugins\rider-godot\dotnetDebuggerWorker";

      public const string ProductTechnicalName = "Godot.Debugger";

      [BuildStep]
      public static SubplatformComponentForPackagingFast[] ProductMetaDependency(AllAssembliesOnSources allassSrc)
      {
          if (!allassSrc.Has(SubplatformName))
              return Array.Empty<SubplatformComponentForPackagingFast>();

          return new[]
          {
              new SubplatformComponentForPackagingFast
              (
                  SubplatformName,
                  new JetPackageMetadata
                  {
                      Spec = new JetSubplatformSpec
                      {
                          ComplementedProductName = RiderConstants.ProductTechnicalName
                      }
                  }
              )
          };
      }
  }

}