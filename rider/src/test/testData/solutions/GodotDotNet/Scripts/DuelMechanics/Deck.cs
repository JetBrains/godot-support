using System.Linq;
using Godot;
using Godot.Collections;
using TCGHandLayoutPlugin.Scripts.Layouts;

namespace TCGHandLayoutPlugin.Scripts.Mechanics;

[GlobalClass, Tool, Icon("res://Assets/Editor/Icons/deck.png")]
public partial class Deck : Control
{
	public PackedScene card = GD.Load<PackedScene>("res://Example/Card.tscn");
	public Layout hand;
	public Array<Card> Cards = [];
	
	[ExportToolButton("Draw Initial Cards", Icon = "File")] public Callable DrawInitialCards => Callable.From(() =>
	{
		if (Cards.Count == 0)
		{
			InitializeDeck();
		}
		DeckDrawer.Singleton.InitialDraw(this, hand);
	});
	
	[ExportToolButton("Draw Single Card", Icon = "File")] public Callable DrawSingleCard => Callable.From(() =>
	{
		if (Cards.Count == 0)
		{
			InitializeDeck();
		}
		DeckDrawer.Singleton.DrawCard(this, hand);
	});
	
	public override void _Ready()
	{
		if (Engine.IsEditorHint()) return;
		
		InitializeDeck();
		GuiInput += Input;
		DeckDrawer.Singleton.InitialDraw(this, hand);
	}

	private void InitializeDeck()
	{
		hand = GetNode<Layout>("../HandLayout");
		/* 40 card sample deck. */
		for (var i = 0; i < 40; i++){
			// even cards are "fireball", odd cards are "ice beam"
			var newCard = card.Instantiate<Card>();
			newCard.CardEffect = i % 2 == 0 ? GD.Load<Effect>("res://Data/CardEffects/Fireball.tres") : GD.Load<Effect>("res://Data/CardEffects/IceBeam.tres");
			Cards.Add(newCard);
		}
	}
	
	private void Input(InputEvent @event)
	{
		if (@event is not InputEventMouseButton mouseButton) return;
		if (!mouseButton.Pressed || mouseButton.ButtonIndex != MouseButton.Left) return;
		
		DeckDrawer.Singleton.DrawCard(this, hand);
	}
}