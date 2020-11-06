using JetBrains.Application.Settings;
using JetBrains.Application.Settings.Extentions;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Psi.Caches;
using JetBrains.ReSharper.Psi.Modules;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Elements;
using JetBrains.ReSharper.UnitTestFramework.Strategy;
using JetBrains.ReSharper.UnitTestProvider.nUnit.v30;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [SolutionComponent]
    public class GodotNUnitServiceProvider : NUnitServiceProvider
    {
        private readonly GodotStrategy myStrategy;

        public GodotNUnitServiceProvider(ISolution solution,
            IPsiModules psiModules,
            ISymbolCache symbolCache,
            IUnitTestElementIdFactory idFactory,
            IUnitTestElementManager elementManager,
            NUnitTestProvider provider,
            IUnitTestingSettings settings,
            ISettingsStore settingsStore,
            ISettingsOptimization settingsOptimization,
            ISettingsCache settingsCache,
            UnitTestingCachingService cachingService,
            INUnitTestParametersProvider testParametersProvider,
            GodotStrategy strategy)
            : base(solution, psiModules, symbolCache, idFactory, elementManager, provider, settings, settingsStore, settingsOptimization, settingsCache, cachingService, testParametersProvider)
        {
            myStrategy = strategy;
        }
        
        
        public override IUnitTestRunStrategy GetRunStrategy(IUnitTestElement element)
        {
            return myStrategy;
        }

    }
}