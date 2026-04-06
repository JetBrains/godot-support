using System.Threading.Tasks;
using Godot;
using TCGHandLayoutPlugin.Scripts.Mechanics.Effects.Chance;

namespace TCGHandLayoutPlugin.Scripts.Mechanics.Effects;

[GlobalClass, Tool]
public abstract partial class EffectAction : Resource
{
    [ExportGroup("Settings")]
    [Export] public string EffectName;
    [Export(PropertyHint.MultilineText)] public string Description { get; set; } = "No description.";
    
    [ExportGroup("Animation")]
    [Export] public StringName AnimationName = "";
    [Export] public bool AwaitCompletion = false;

    [ExportGroup("Activation")]
    [Export] public bool UseChance = false;
    [Export(PropertyHint.Range, "0,1,0.01")] 
    public float ActivationChance = 1.0f; // 1.0 = 100%
    
    public async Task<bool> Execute(DuelContext ctx)
    {
        GD.Print("Animation Name: ", AnimationName);
        GD.Print("Effect Name: ", EffectName);
        
        if (UseChance && RNG.Attempt(ActivationChance) == AttemptOutcome.Failure)
        {
            await OnActivationFailed(ctx);
            return false;
        }

        var success = await OnExecute(ctx);

        if (success)
            await OnActivationSuccess(ctx);
        else
            await OnActivationFailed(ctx);

        return success;
    }

    protected abstract Task<bool> OnExecute(DuelContext ctx);

    protected virtual async Task OnActivationSuccess(DuelContext ctx)
    {
        /* That's the part where you're free to edit at will without breaking anything.
         Insert animations, VFX, change data somewhere, go wild.
         ... On the inherited class, of course.
         */
    }

    protected virtual async Task OnActivationFailed(DuelContext ctx)
    {
        /* That's the part where you're free to edit at will without breaking anything.
         Insert animations, VFX, change data somewhere, go wild.
         ... On the inherited class, of course.
         */
    }
}