using System.Collections.Generic;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Completions
{
    internal static class InputActionMethods
    {
        internal static readonly List<string> Methods = new()
        {
            "ActionPress", "ActionRelease", "GetActionRawStrength", "GetActionStrength", "GetAxis",
            "IsActionJustPressed", "IsActionJustReleased", "IsActionPressed"
        };
    }
}