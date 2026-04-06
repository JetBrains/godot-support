using System.Linq;
using System.Threading.Tasks;
using Godot;
using TCGHandLayoutPlugin.Scripts.Animations;
using TCGHandLayoutPlugin.Scripts.Mechanics;
using TCGHandLayoutPlugin.Scripts.Mechanics.Effects;

namespace TCGHandLayoutPlugin.Scripts.DuelMechanics.Effects.Damage;

[GlobalClass, Tool]
public partial class DealDamage : EffectAction
{
    [Export] public int DamageAmount { get; set; } = 1;
    [Export] public DamageType DamageType { get; set; } = DamageType.FIRE;

    protected override async Task<bool> OnExecute(DuelContext ctx)
    {
        if (ctx.Target == null)
        {
            return false;
        }
        return true;
    }


    protected override async Task OnActivationFailed(DuelContext ctx)
    {   
        GD.PrintErr("DealDamage action failed.");
    }
    
    protected override async Task OnActivationSuccess(DuelContext ctx)
    {
        // Here you would implement the actual damage logic, e.g., reducing the target's health.
        if (!AnimationName.IsEmpty && ctx.Caster.AnimationPlayer.GetAnimationList().Contains<string>(AnimationName))
        {   
            await AnimationHandler.PlayAnimation(ctx.Caster, AnimationName, AwaitCompletion);
        }
        GD.Print($"{EffectName} dealt {DamageAmount} {DamageType} damage to {ctx.Target.Name}.");
    }
}