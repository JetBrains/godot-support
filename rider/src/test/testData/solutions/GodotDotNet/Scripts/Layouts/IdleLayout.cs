using Godot;

namespace TCGHandLayoutPlugin.Scripts.Layouts;

[GlobalClass]
public partial class IdleLayout : Control
{
    public static void SetDynamicRadius(Layout layout, bool dynamicRadius, bool value){
        if (dynamicRadius == value){
            return;
        }
        layout._dynamicRadius = value;
        layout.HandLayout._needRecalculateCurve = true;
        layout.NotifyPropertyListChanged();
        LayoutService.ResetPositionsIfInTree(layout);
    }
    public static void SetDynamicRadiusFactor(Layout layout, float value){
        layout._dynamicRadiusFactor = value;
        layout.HandLayout._needRecalculateCurve = true;
        LayoutService.ResetPositionsIfInTree(layout);
    }
    public static void SetRadius(Layout layout, float value){
        layout._cardRadius = value;
        LayoutService.ResetPositionsIfInTree(layout);
    }
    public static void SetCirclePercentage(Layout layout, float value){
        layout._circlePercentage = value;
        LayoutService.ResetPositionsIfInTree(layout);
    }
}