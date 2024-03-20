using System.Collections.Generic;
using System.Linq;
using JetBrains.Application.BuildScript.PreCompile.Autofix;
using JetBrains.Application.BuildScript.Solution;
using JetBrains.Build;

namespace JetBrains.ReSharper.Plugins.Godot.BuildScript
{
    public static class DefineGodotConstants
    {
        [BuildStep]
        public static IEnumerable<AutofixAllowedDefineConstant> YieldAllowedDefineConstantsForGodot()
        {
            var constants = new List<string>();

            constants.AddRange(new[]
                { "JET_MODE_ASSERT", "JET_MODE_REPORT_EXCEPTIONS", "RIDER", "$(CommandLineConstants)" });

            return constants.SelectMany(s => new[]
            {
                new AutofixAllowedDefineConstant(new SubplatformName("Plugins\\godot-support\\resharper"), s),
            });
        }
    }
}