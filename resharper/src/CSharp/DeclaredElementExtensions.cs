using System.Linq;
using JetBrains.Annotations;
using JetBrains.Metadata.Reader.API;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Modules;
using JetBrains.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp
{
    public static class DeclaredElementExtensions
    {
        private static bool DerivesFrom([CanBeNull] this ITypeElement candidate, IClrTypeName baseTypeName)
        {
            if (candidate == null)
                return false;
            
            var baseTypeElement = TypeFactory.CreateTypeByCLRName(baseTypeName, candidate.Module).GetTypeElement();
            return candidate.IsDescendantOf(baseTypeElement);
        }

        public static bool DerivesFromGodotObject([CanBeNull] this ITypeElement candidate)
        {
            return candidate.DerivesFrom(KnownTypes.GodotObject);
        }

        public static bool DerivesFromChickenTest([CanBeNull] this ITypeElement candidate)
        {
            return candidate.DerivesFrom(KnownTypes.ChickensoftTest);
        }
    }
}