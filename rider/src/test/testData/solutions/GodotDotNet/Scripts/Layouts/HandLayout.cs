using System;
using Godot;
using Godot.Collections;

namespace TCGHandLayoutPlugin.Scripts.Layouts;

[GlobalClass]
public partial class HandLayout : RefCounted
{
    public bool _needRecalculateCurve = true;
    private Array<LayoutCardInfo> _baseLayoutInfos = [];

    private int _layoutCardsNumber;
    private bool _layoutDynamicRadius;
    private float _layoutDynamicRadiusFactor;
    private float _layoutCardRadius;
    private float _layoutCirclePercentage;
    private float _layoutHoverPadding;
    private int _layoutHoveredIndex;
    private Vector2 _layoutHoverRelativePosition;

    public Array<LayoutCardInfo> GetCardLayouts(Layout layout){
        if (_needRecalculateCurve){
            RecalculateLayouts(layout);
        }
        return SampleCurve(layout);
    }
        
    private void RecalculateLayouts(Layout layout){
        _baseLayoutInfos.Clear();
        if (layout._dynamicRadius){
            _layoutCardRadius = _layoutCardsNumber * _layoutDynamicRadiusFactor;
        }
        var totalDegree = (float) Math.Tau * layout.CirclePercentage;
        var initialAngle = (float) -(Math.PI / 2);
        var step = 0f;
        if (_layoutCardsNumber > 1){
            initialAngle -= totalDegree/2f;
            step = totalDegree / (_layoutCardsNumber - 1);
        }
        var origin = Vector2.Down * _layoutCardRadius;
        for (var i = 0; i < _layoutCardsNumber; i++){
            var angle = initialAngle;
            if (_layoutCardsNumber > 1){
                angle += step * i;
            }
            var x = _layoutCardRadius * Mathf.Cos(angle);
            var y = _layoutCardRadius * Mathf.Sin(angle);
            var layoutInfo = new LayoutCardInfo();
            layoutInfo.Position = new Vector2(x,y) + origin;
            layoutInfo.Rotation = angle + (float)(Math.PI / 2f);
            _baseLayoutInfos.Add(layoutInfo);
        }
        _needRecalculateCurve = false;
    }
    private Array<LayoutCardInfo> SampleCurve(Layout layout){
        var result = new Array<LayoutCardInfo>();
        var intervalNumber = Math.Max(_layoutCardsNumber - 1, 1);
        for (var i = 0; i < _layoutCardsNumber; i++){
            var layoutInfo = new LayoutCardInfo();
            layoutInfo.Copy(_baseLayoutInfos[i]);
            if (i == _layoutHoveredIndex){
                Vector2 targetPosition = new();
                if (layout.HoverMode == HoverLayout.HoverMode.Standard){
                    targetPosition = _layoutHoverRelativePosition * layout.HoverScale * layout.HoverScale;
                }
                else if (!layout.CalculateOffsetWhenHovered){
                    targetPosition = _layoutHoverRelativePosition * layout.HoverScale;
                }
                layoutInfo.Position += targetPosition;
                layoutInfo.Rotation = 0;
            }
            else if (_layoutHoveredIndex != -1){
                var i_diff = i - _layoutHoveredIndex;
                if (i_diff < 0){
                    layoutInfo.Position.X -= _layoutHoverPadding;
                }
                else{
                    layoutInfo.Position.X += _layoutHoverPadding;
                }
            }
            result.Add(layoutInfo);
        }
        return result;
    }
    public void SetData( int layoutCardsNumber, bool layoutDynamicRadius, 
        float layoutDynamicRadiusFactor, float layoutCardRadius, 
        float layoutCirclePercentage, float layoutHoverPadding, 
        int layoutHoveredIndex, Vector2 layoutHoverRelativePosition
    ){
        _layoutHoveredIndex = layoutHoveredIndex;
        _layoutHoverPadding = layoutHoverPadding;
        _layoutCirclePercentage = layoutCirclePercentage;
        _layoutDynamicRadiusFactor = layoutDynamicRadiusFactor;
        _layoutCardRadius = layoutCardRadius;
        _layoutDynamicRadius = layoutDynamicRadius;
        _layoutHoverRelativePosition = layoutHoverRelativePosition;
        _layoutCardsNumber = layoutCardsNumber;
        _needRecalculateCurve = true;
    }
}