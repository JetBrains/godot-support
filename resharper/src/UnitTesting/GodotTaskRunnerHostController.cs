using System.Diagnostics;
using JetBrains.Annotations;
using JetBrains.Application.Processes;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Extensions;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.ReSharper.UnitTestFramework.Processes;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    public class GodotTaskRunnerHostController:TaskRunnerHostControllerBase
    {
        [NotNull] private readonly ISolutionProcessStartInfoPatcher myProcessStartInfoPatcher;

        public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, IUnitTestRun run, ILogger logger)
        {
            var godotProcessStartInfo =
                new ProcessStartInfo(
                    "/home/ivan-shakhov/Downloads/Godot_v3.2.3-stable_mono_x11_64/Godot_v3.2.3-stable_mono_x11.64");
            godotProcessStartInfo.Arguments = $"--path \"/home/ivan-shakhov/Work/godot-demo-projects/mono/dodge_the_creeps\" --unit_test_assembly \"{startInfo.FileName}\" --unit_test_args \"{startInfo.Arguments}\"";

            
            var rawProcessInfo = new JetProcessStartInfo(godotProcessStartInfo);
      
            return new PreparedProcess(rawProcessInfo, logger);
        }

        public override string HostId => "GodotProcess";

        public GodotTaskRunnerHostController(      
            [NotNull] IUnitTestLaunch launch,
            [NotNull] ISolutionProcessStartInfoPatcher processStartInfoPatcher,
            [NotNull] ILogger logger
        ) : base(logger, launch)
        {
            myProcessStartInfoPatcher = processStartInfoPatcher;
        }
    }
}