/**
 * Created by pym on 2016/3/22.
 */
define(function(){
    return {
        rect: function (x, y, width, height, fillColor, strokeColor, strokeWidth) {
            var rect = $("<rect></rect>");
            rect.attr("x", x);
            rect.attr("y", y);
            rect.attr("width", width);
            rect.attr("height", height);
            rect.css("fill", fillColor);
            rect.css("stroke-width", strokeWidth);
            rect.css("stroke", strokeColor);

            return rect;
        },

        circle: function (cx, cy, r, fillColor, strokeColor, strokeWidth) {
            var circle = $("<circle></circle>");
            circle.attr("cx", cx);
            circle.attr("cy", cy);
            circle.attr("r", r);
            circle.css("fill", fillColor);
            circle.css("stroke", strokeColor);
            circle.css("stroke-width", strokeWidth);
            return circle;
        },

        ellipse: function (cx, cy, rx, ry, fillColor, strokeColor, strokeWidth) {
            var ellipse = $("<ellipse></ellipse>");
            ellipse.attr("cx", cx);
            ellipse.attr("cy", cy);
            ellipse.attr("rx", rx);
            ellipse.attr("ry", ry);
            ellipse.css("fill", fillColor);
            ellipse.css("stroke", strokeColor);
            ellipse.css("stroke-width", strokeWidth);
            return ellipse;
        },

        line: function (x1, y1, x2, y2, strokeWidth, strokeColor) {
            var line = $("<line></line>");
            line.attr("x1", x1);
            line.attr("y1", y1);
            line.attr("x2", x2);
            line.attr("y2", y2);
            line.css("stroke-width", strokeWidth);
            line.css("stroke", strokeColor);
            return line;
        },

        polygon: function (points, fillColor, strokeWidth, strokeColor) {
            var polygon = $("<polygon></polygon>");
            polygon.attr("points", points);
            polygon.css("fill", fillColor);
            polygon.css("stroke", strokeColor);
            polygon.css("stroke-width", strokeWidth);
            return polygon;
        },

        polyline: function (points, strokeWidth, strokeColor) {
            var polyline = $("<polyline></polyline>");
            polyline.attr("points", points);
            polyline.css("stroke", strokeColor);
            polyline.css("stroke-width", strokeWidth);
            return polyline;
        },

        text: function (x, y, fillColor, text) {
            var text = $("<text></text>");
            text.attr("x", x);
            text.attr("y", y);
            text.css("fill", fillColor);
            text.text(text);
            return text;
        }
    }
});