using System.IO;
using System.Reflection;
using JetBrains.Application;
using JetBrains.Application.Parts;
using JetBrains.Application.Settings;
using JetBrains.Diagnostics;
using JetBrains.Lifetimes;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.LiveTemplates
{
    [ShellComponent(Instantiation.DemandAnyThreadSafe)]
    public class GodotTemplatesDefaultSettings : IHaveDefaultSettingsStream
    {
        public Stream GetDefaultSettingsStream(Lifetime lifetime)
        {
            var stream = Assembly.GetExecutingAssembly().GetManifestResourceStream("JetBrains.ReSharper.Plugins.Godot.Templates.templates.dotSettings");
            Assertion.AssertNotNull(stream, "stream != null");
            lifetime.AddDispose(stream);
            return stream;
        }

        public string Name => "Godot default LiveTemplates";
    }
}