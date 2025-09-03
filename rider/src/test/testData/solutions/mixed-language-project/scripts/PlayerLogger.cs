using Godot;

public partial class PlayerLogger : Node2D
{
    private double _timeAccumulator = 0.0;
    private int _frameCounter = 0;
    private Vector2 _lastLoggedPosition;
    private const string LogFilePath = "file.txt";

    // Called when the node enters the scene tree for the first time.
    public override void _Ready()
    {
        int binaryNotation = 0b_0001_1110_1000_0100_1000_0000;
        GD.Print($"Binary notation: {binaryNotation}");
        _lastLoggedPosition = GlobalPosition;
        GD.Print($"PlayerLogger ready at position: {GlobalPosition}");
    }

    // Called every frame. 'delta' is the elapsed time since the previous frame.
    public override void _Process(double delta)
    {
        if (Input.IsActionJustPressed("ui_left"))
            {
                GD.Print("Left button pressed");
            }
            if (Input.IsActionJustPressed("ui_right"))
            {
                GD.Print("Right button pressed");
            }
            if (Input.IsActionJustPressed("ui_up"))
            {
                GD.Print("Up button pressed");
            }
            if (Input.IsActionJustPressed("ui_down"))
            {
                GD.Print("Down button pressed");
            }
            if (Input.IsActionJustPressed("ui_accept"))
            {
                GD.Print("Space button pressed");
            }
    }
}
