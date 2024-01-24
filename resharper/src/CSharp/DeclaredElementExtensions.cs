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

            var solution = candidate.GetSolution();
            var knownTypesCache = solution.GetComponent<KnownTypesCache>();
            var baseType = GetTypeElement(baseTypeName, knownTypesCache, candidate.Module);
            return candidate.IsDescendantOf(baseType);
        }

        private static ITypeElement GetTypeElement(IClrTypeName typeName, KnownTypesCache knownTypesCache,
            IPsiModule module)
        {
            using (CompilationContextCookie.GetExplicitUniversalContextIfNotSet())
            {
                var type = knownTypesCache.GetByClrTypeName(typeName, module);
                return type.GetTypeElement();
            }
        }

        public static bool DerivesFromGodotObject([CanBeNull] this ITypeElement candidate)
        {
            return candidate.DerivesFrom(KnownTypes.GodotObject);
        }
    }
}