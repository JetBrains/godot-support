#nullable enable
using System.Linq;
using JetBrains.Annotations;
using JetBrains.ReSharper.Psi;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Feature.RunMarkers
{
    public static class ChickensoftTestRunMarkerUtil
    {
        [Pure]
        public static bool IsSuitableMethod(IMethod method)
        {
            return method.GetAttributeInstances(false).Any(a => a.GetClrName().Equals(KnownTypes.ChickensoftTestAttribute));
        }
    }
}