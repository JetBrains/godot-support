using System.Threading.Tasks;
using Godot;
using TCGHandLayoutPlugin.Scripts.Mechanics;

namespace TCGHandLayoutPlugin.Scripts.Animations;

public static class AnimationHandler
{
    public static async Task PlayAnimation(Actor actor, StringName animationName, bool awaitCompletion)
    {
        actor.AnimationPlayer.Play(animationName);

        if (awaitCompletion)
        {
            await actor.AnimationPlayer.ToSignal(actor.AnimationPlayer, AnimationPlayer.SignalName.AnimationFinished);
        }
    }
}