using System.Linq;
using System.Threading.Tasks;
using Godot;
using TCGHandLayoutPlugin.Scripts.Animations;
using TCGHandLayoutPlugin.Scripts.DuelMechanics.Effects.Damage;

namespace TCGHandLayoutPlugin.Scripts.Mechanics.Effects.Status;

[GlobalClass, Tool]
public partial class DamageOverTime : EffectAction
{
    [Export] public int DamagePerTurn = 5;
    [Export] public int DurationInTurns = 3;
    [Export] public DamageType DamageType = DamageType.FIRE;
    
    protected override Task<bool> OnExecute(DuelContext ctx)
    {
        if (ctx.Target == null)
        {
            return Task.FromResult(false);
        }
        
        return Task.FromResult(true);
    }

    protected override async Task OnActivationFailed(DuelContext ctx)
    {
        GD.Print($"{EffectName} not applied.");
    }

    protected override async Task OnActivationSuccess(DuelContext ctx)
    {
        // Here you would implement the actual damage over time logic, e.g., adding a status effect to the target.
        if (!AnimationName.IsEmpty && ctx.Caster.AnimationPlayer.GetAnimationList().Contains<string>(AnimationName))
        {   
            await AnimationHandler.PlayAnimation(ctx.Caster, AnimationName, AwaitCompletion);
        }
        GD.Print($"Applied {EffectName} over time: {DamagePerTurn} {DamageType} damage for {DurationInTurns} turns to {ctx.Target.Name}.");
    }
}