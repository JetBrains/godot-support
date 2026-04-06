using System.Linq;
using Godot;
using TCGHandLayoutPlugin.Scripts.Layouts;

namespace TCGHandLayoutPlugin.Scripts.Mechanics;

[GlobalClass, Tool]
public partial class DeckDrawer : Control
{
    [Export] public int InitialCards { get; set; } = 5;
    [Export] public double DrawTime { get; set; } = 0.5;
    [Export] public AudioStream DrawSound { get; set; }
    [Export] public AudioStreamPlayer DrawSoundPlayer { get; set; }
    
    public static DeckDrawer Singleton { get; private set; }

    public override void _EnterTree()
    {
	    Singleton = this;
    }

    public void DrawCard(Deck deck, Layout hand)
    {
	    var topCard = deck.Cards.Last();
	    hand.DrawingCard = true;
	    hand.CardDrawed = topCard;
	    hand.AddChild(topCard);
	    topCard.GlobalPosition = deck.GlobalPosition;
	    topCard.ZIndex = 1;
	    deck.Cards.Remove(topCard);
	    PlayDrawSound();
    }
    
    public async void InitialDraw(Deck deck, Layout hand)
    {
	    for (var i = 0; i < InitialCards; i++)
	    {
		    DrawCard(deck, hand);
		    await ToSignal(GetTree().CreateTimer(DrawTime), SceneTreeTimer.SignalName.Timeout);
	    }
    }
    
    private void PlayDrawSound()
	{
	    if (DrawSoundPlayer == null || DrawSound == null) return;
	    DrawSoundPlayer.Stream = DrawSound;
	    DrawSoundPlayer.Play();
	}
}