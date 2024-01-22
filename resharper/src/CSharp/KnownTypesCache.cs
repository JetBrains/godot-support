#nullable enable

using System.Collections.Concurrent;
using JetBrains.Metadata.Reader.API;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Modules;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp
{
    [SolutionComponent]
    public class KnownTypesCache
    {
        private readonly ConcurrentDictionary<IClrTypeName, IDeclaredType> myTypes = new();

        public IDeclaredType GetByClrTypeName(IClrTypeName typeName, IPsiModule module)
        {
            // at lest for now the type is in the #nullable disable context 
            const NullableAnnotation nullableAnnotation = NullableAnnotation.Unknown;

            var type = module.GetPredefinedType().TryGetType(typeName, nullableAnnotation);
            if (type != null)
                return type;

            // Make sure the type is still valid before handing it out. It might be invalid if the module used to create
            // it has been changed
            type = myTypes.AddOrUpdate(typeName, name => TypeFactory.CreateTypeByCLRName(name, nullableAnnotation, module),
                (name, existingValue) => existingValue.Module.IsValid()
                    ? existingValue
                    : TypeFactory.CreateTypeByCLRName(name, nullableAnnotation, module));
            return type;
        }
    }
}
