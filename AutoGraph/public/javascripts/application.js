/**
 * Created by liufengkai on 16/1/21.
 */

var codeToSVG = angular.module('codeToSVG', []);

var header = "var g = new dagreD3.graphlib.Graph().setGraph({});";

var foot = `var svg = d3.select("svg"),
    inner = svg.select("g");

// Set up zoom support
var zoom = d3.behavior.zoom().on("zoom", function() {
      inner.attr("transform", "translate(" + d3.event.translate + ")" +
                                  "scale(" + d3.event.scale + ")");
    });
svg.call(zoom);

// Create the renderer
var render = new dagreD3.render();

// Run the renderer. This is what draws the final graph.
render(inner, g);

// Center the graph
var initialScale = 0.75;
zoom
  .translate([(svg.attr("width") - g.graph().width * initialScale) / 2, 20])
  .scale(initialScale)
  .event(svg);
svg.attr('height', g.graph().height * initialScale + 40);`;

codeToSVG.controller('MainController', function ($scope) {
    $scope.openDialog = function () {
        console.log("==");
    }
    $scope.report = function () {
        $('#myModal').modal('hide');
        var text = $('#textView').val();
        if (text != "") {
            eval(header + text.toString() + foot);
            //$('#svg-page').load("modules.html");
            //$('#svg-page').append("<script>" + header + text.toString() + foot + "</script>");
            $('#svg-page').show();
            $('#first-page').hide();
        }
    };


});


