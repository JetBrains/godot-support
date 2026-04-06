using Godot;

namespace TCGHandLayoutPlugin.Scripts.Layouts;

[GlobalClass]
public partial class DragLayout : Control
{
    public const float TIME_TO_DRAG = 0.1f;
    public void SetEnableDrag(Layout layout, bool value){
        layout._enableDrag = value;
        NotifyPropertyListChanged();
    }
}