using Godot;

namespace TCGHandLayoutPlugin.Scripts.Layouts;
public partial class LayoutCardInfo : RefCounted
{
    public Vector2 Position;
    public float Rotation;

    public void Copy(LayoutCardInfo other){
        Position = other.Position;
        Rotation = other.Rotation;
    }
}