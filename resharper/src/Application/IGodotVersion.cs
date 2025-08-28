using System;
using JetBrains.Application.Parts;

namespace JetBrains.ReSharper.Plugins.Godot.Application;

[DerivedComponentsInstantiationRequirement(InstantiationRequirement.DeadlockSafe)]
public interface IGodotVersion
{
    public Version? ActualVersionForSolution { get; }
}