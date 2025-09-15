using System.Collections.Generic;
using JetBrains.Application;
using JetBrains.Application.Parts;
using JetBrains.HabitatDetector;
using JetBrains.ProjectModel.ProjectsHost.Impl;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.Application;

// Provide Godot-specific solution configuration defaults
[ShellComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotSolutionConfigurationDefaults : ISolutionConfigurationDefaults
{
  // This doesn't matter that much, because the defaults of SolutionConfigurationDefaults do not match with godot specific ones
  public int PriorityAsc => 100;

  public IReadOnlyList<string> DefaultConfigurations { get; } = ["Debug"];

  public IReadOnlyList<string> DefaultPlatforms { get; } = CreateDefaultPlatforms();

  private static IReadOnlyList<string> CreateDefaultPlatforms()
  {
    var list = new List<string>();

    switch (PlatformUtil.RuntimePlatform)
    {
      case JetPlatform.Windows:
      {
        // Detect CPU and select corresponding Godot platform
        switch (HabitatInfo.OSArchitecture)
        {
          case JetArchitecture.X64:
          default:
            list.Add("windows-x86_64");
            break;
          case JetArchitecture.X86:
            list.Add("windows-x86_32");
            break;
        }
        break;
      }
      case JetPlatform.MacOsX:
      {
        // Godot uses a single "macos" platform regardless of CPU
        list.Add("macos");
        break;
      }
      case JetPlatform.Linux:
      {
        switch (HabitatInfo.OSArchitecture)
        {
          case JetArchitecture.X64: 
          default:
            list.Add("linux-x86_64");
            break;
          case JetArchitecture.Arm64:
            list.Add("linux-arm64");
            break;
          case JetArchitecture.RiscV64:
            list.Add("linux-rv64");
            break;
        }
        break;
      }
    }

    return list;
  }
}
