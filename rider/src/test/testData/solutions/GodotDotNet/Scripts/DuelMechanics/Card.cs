using Godot;
using System;

namespace TCGHandLayoutPlugin.Scripts.Mechanics;

// This is the node to your card class, so it can have its own functions.
[GlobalClass, Tool, Icon("res://Assets/Editor/Icons/card.png")]
public partial class Card : TextureRect
{
	public bool AllSignalsConected = false;
	
	[Signal]
	public delegate void ActivatedEventHandler();

	[Export] public Effect CardEffect { get; set; }
	
	public override void _Ready(){
	    int binaryNotation = 0b_0001_1110_1000_0100_1000_0000;
        GD.Print($"Binary notation: {binaryNotation}");
		RandomizeColor();
		Activated += HandleEffect;
	}
	private void HandleEffect(){
		CardEffect.Activate(DuelContext.Singleton);
		DeleteActiveCard();
		QueueFree();
	}

	// Called every frame. 'delta' is the elapsed time since the previous frame.
	public override void _Process(double delta)
	{
	}

	private void DeleteActiveCard()
	{
		var layout = GetParent<Layouts.Layout>();
		layout.DraggingCard = null;
		layout._draggingCardIndex = -100;
		layout.CardsNumber -= 1;
		Layouts.LayoutService.ResetPositionsIfInTree(layout);
	}

	private void RandomizeColor()
	{
		/* Random color so it can differ from other cards, since it's the same sprite
		 Not necessary for the plugin to work.
		 */
		var rng = new Random();
		Modulate = new Color(
		Color.Color8(
			(byte) rng.Next(0, 255),
			(byte) rng.Next(0, 255),
			(byte) rng.Next(0, 255)
		));
	}
}