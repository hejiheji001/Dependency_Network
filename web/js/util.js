/**
 * Created by FireAwayH on 12/08/2016.
 */

var h = window.outerHeight;
var w = window.outerWidth;
$(".graph").css("height", h);

var pctn_hint = $("#pctn_hint");
var popPCTN = '<div>\
				<p>1. Edge\'s color equals to source node\'s color</p>\
				<p>2. Node\'s size is proportional to it\'s indegree or outdegree number</p>\
				<p>3. Edge\'s width is proportional to it\'s dependency value</p>\
			</div>';
pctn_hint.popover({
    content: popPCTN,
    title: "<span>Useful Info</span>"
});

var flscn = $(".flscn");
flscn.bootstrapToggle({
    on: '<span class="glyphicon glyphicon-resize-small"></span>',
    off: '<span class="glyphicon glyphicon-resize-full"></span>'
});
flscn.each(function(){
    $(this).on({
        change: function(e){
            var full = $(this).prop('checked');
            var p = document.getElementById($(this).attr("data"));
            if(full){
                if(p.webkitRequestFullScreen){
                    p.webkitRequestFullScreen();
                }else{
                    p.mozRequestFullScreen();
                }
            }else{
                if(document.webkitCancelFullScreen){
                    document.webkitCancelFullScreen();
                }else{
                    document.mozCancelFullScreen();
                }
            }
        }
    });
});

var degree_switch = $(".visual");

var legend = $(".legend");

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}