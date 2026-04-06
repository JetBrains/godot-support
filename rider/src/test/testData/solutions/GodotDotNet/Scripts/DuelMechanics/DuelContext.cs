using Godot;
using Godot.Collections;

namespace TCGHandLayoutPlugin.Scripts.Mechanics;

[GlobalClass, Tool]
public partial class DuelContext : Node
{
    [Export] public Actor Caster { get; set; }
    [Export] public Actor Target { get; set; }
    [Export] public Dictionary Metadata { get; set; } = new();
    private RandomNumberGenerator RNG { get; set; }
    
    public static DuelContext Singleton { get; private set; }
    
    public override void _EnterTree()
    {
        Singleton = this;
    }
}