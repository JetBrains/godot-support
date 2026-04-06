using Godot;

namespace TCGHandLayoutPlugin.Scripts.Mechanics;

[GlobalClass, Tool]
public partial class Actor : Node2D
{
    [Export] public AnimatedSprite2D Sprite;
    [Export] public AnimationPlayer AnimationPlayer;
}