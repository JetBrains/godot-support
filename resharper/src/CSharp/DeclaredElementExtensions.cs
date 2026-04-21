using JetBrains.Metadata.Reader.API;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Plugins.Godot.CSharp.Completions;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp
{
    public static class DeclaredElementExtensions
    {
        private static bool DerivesFrom(this ITypeElement? candidate, IClrTypeName baseTypeName)
        {
            if (candidate == null)
                return false;
            
            var baseTypeElement = TypeFactory.CreateTypeByCLRName(baseTypeName, candidate.Module).GetTypeElement();
            return candidate.IsDescendantOf(baseTypeElement);
        }

        public static bool DerivesFromGodotObject(this ITypeElement? candidate)
        {
            return candidate.DerivesFrom(GodotTypes.GodotObject);
        }

        public static bool DerivesFromChickenTest(this ITypeElement? candidate)
        {
            return candidate.DerivesFrom(KnownTypes.ChickensoftTest);
        }
    }
}