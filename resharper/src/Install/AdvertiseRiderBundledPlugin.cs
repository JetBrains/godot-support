using JetBrains.Build;
using JetBrains.ReSharper.Plugins.Godot.BuildScript;
using JetBrains.Rider.Backend.Install;

namespace JetBrains.ReSharper.Plugins.Godot.Install
{
  public static class AdvertiseRiderBundledPlugin
  {
    [BuildStep]
    public static RiderBundledProductArtifact[] ShipGodotWithRider()
    {
      return new[]
      {
        new RiderBundledProductArtifact(
            GodotInRiderProduct.ProductTechnicalName,
            GodotInRiderProduct.ThisSubplatformName,
            GodotInRiderProduct.DotFilesFolder,
          allowCommonPluginFiles: false),
        new RiderBundledProductArtifact(
            GodotDebuggerProduct.ProductTechnicalName,
            GodotDebuggerProduct.SubplatformName,
            GodotDebuggerProduct.PluginFolder,
            allowCommonPluginFiles: false),
      };
    }
  }
}