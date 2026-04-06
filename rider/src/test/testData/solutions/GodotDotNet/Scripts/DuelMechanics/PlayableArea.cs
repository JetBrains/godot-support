using Godot;
using TCGHandLayoutPlugin.Scripts.Layouts;

namespace TCGHandLayoutPlugin.Scripts.Mechanics;

[GlobalClass, Tool, Icon("res://Assets/Editor/Icons/field.png")]
public partial class PlayableArea : Control
{
    public static PlayableArea Singleton { get; private set; }
    
    public override void _EnterTree()
    {
        Singleton = this;
    }
    public static bool IsInPlayableArea(Layout layout, Card card){
        var playableArea = Singleton.GetGlobalRect();
        if (!playableArea.Intersects(card.GetGlobalRect())){
            return false;
        }
        return playableArea.HasPoint(card.GlobalPosition) && playableArea.HasPoint(card.GlobalPosition + card.Size / 2);
    }
}