#nullable enable
using System;
using JetBrains.Application.Parts;
using JetBrains.Collections.Viewable;

namespace JetBrains.ReSharper.Plugins.Godot.Application;

[DerivedComponentsInstantiationRequirement(InstantiationRequirement.DeadlockSafe)]
public interface IGodotVersion
{
    public ViewableProperty<Version> ActualVersionForSolution { get; }
}