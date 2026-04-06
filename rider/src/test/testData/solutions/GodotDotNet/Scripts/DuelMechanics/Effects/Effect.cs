using System.Threading.Tasks;
using Godot;
using TCGHandLayoutPlugin.Scripts.Mechanics.Effects;

namespace TCGHandLayoutPlugin.Scripts.Mechanics;

[GlobalClass, Tool]
public partial class Effect : Resource
{
    [Export] public StringName CardName = "New Card";
    [Export] public Godot.Collections.Array<EffectAction> Actions = [];

    public virtual async Task Activate(DuelContext ctx)
    {
        foreach (var action in Actions)
        {
            if (action is EffectAction)
            {
                var success = await action.Execute(ctx);
                /* You can log or handle the success/failure of each action here if needed */
            }
        }
    }
}