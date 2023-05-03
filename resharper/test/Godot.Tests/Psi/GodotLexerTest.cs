using JetBrains.ReSharper.Plugins.Godot.ProjectGodot.Psi.Parsing;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.ReSharper.TestFramework;
using JetBrains.Text;
using NUnit.Framework;

namespace JetBrains.ReSharper.Plugins.Godot.Tests.Psi
{
    [TestFileExtension(".godot")]
    public sealed class GodotLexerTest : LexerTestBase
    {
        protected override string RelativeTestDataPath => @"Psi\Lexer";

        protected override ILexer CreateLexer(IBuffer buffer) =>
            new ProjectGodotLexer(new StringBuffer(buffer.GetText().Replace("\r\n", "\n")));

        [TestCase("project")]
        public void TestLexer(string name) => DoOneTest(name);

        [TestCase("ForgottenBlockEnd")]
        [TestCase("ForgottenBlockEnd2")]
        public void TestErrorRecovery(string name) => DoOneTest(name);
    }
}