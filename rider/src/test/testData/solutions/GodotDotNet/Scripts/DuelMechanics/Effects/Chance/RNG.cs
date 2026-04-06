using Godot;

namespace TCGHandLayoutPlugin.Scripts.Mechanics.Effects.Chance;

public static class RNG
{
    public static AttemptOutcome Attempt(float chance)
    {
        var rng = new RandomNumberGenerator();
        rng.Randomize();
        return rng.Randf() <= chance ? AttemptOutcome.Success : AttemptOutcome.Failure;
    }
}