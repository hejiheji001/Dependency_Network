/**
 * Created by FireAwayH on 12/08/2016.
 */

var loaded = [];
var data = {};

degree_switch.bootstrapToggle({
    off: '<span>In-Deg</span>',
    on: '<span>Out-Deg</span>'
});
degree_switch.each(function(){
    $(this).on({
        change: function(e){
            var on = $(this).prop('checked');
            var graph = $(this).attr('data');
            ss[graph].kill();
            if(on){
                getGraph(graph, 'all', 'out');
            }else{
                getGraph(graph, 'all', 'in');
            }
            ss[graph].refresh();
        }
    });
})

sigma.classes.graph.addMethod('neighbors', function(nodeId) {
    var k, neighbors = {}, index = this.allNeighborsIndex[nodeId] || {};
    for (k in index)
        neighbors[k] = this.nodesIndex[k];

    return neighbors;
});

sigma.classes.graph.addMethod('restore', function(graph) {
    console.log(graph);
});

getGraph('pctn', 'all', 'in');
getGraph('pcpg', 'all', 'in');

function getGraph(graphName, stocks, mode){
    var loading = echarts.init(document.getElementById(graphName + '_load'));
    $("#" + graphName + "_load").show();
    $("#" + graphName + "_in").hide();
    $("#" + graphName + "_out").hide();
    loading.resize();
    loading.showLoading();
    sigma.parsers.gexf('/getGephi/?graph=' + graphName + '&stocks=' + stocks + '&mode=' + mode, {container: graphName + '_' + mode}, function (s) {
        loading.hideLoading();
        $("#" + graphName + "_load").hide();
        $("#" + graphName + "_" + mode).show();
        var nodes = s.graph.nodes();
        var edges = s.graph.edges();

        nodes.forEach(function (n) {
//                    n.originalColor = n.color;
            n.size += 2;
            if (n.size < 4) {
                n.size += 2;
            }
            n.originalSize = n.size;
        });
        edges.forEach(function (e) {
            e.color = null;
            e.size += 1;
            e.originalSize = e.size;
            e.label = e.source + " -> " + e.target;
        });

        s.settings({
            borderSize: 1,
            minArrowSize: 40,
            maxNodeSize: 20,
            minNodeSize: 0,
            minEdgeSize: 0,
            maxEdgeSize: 20,
            labelThreshold: 7,
            edgeLabelSize: 'proportional'
        });

        s.bind('clickNode', function (e) {
            var nodeId = e.data.node.id;

            var toKeep = s.graph.neighbors(nodeId);
            toKeep[nodeId] = e.data.node;

            s.graph.nodes().forEach(function (n) {
                if (toKeep[n.id]) {
//                            n.color = n.originalColor;
                    n.size = n.originalSize + 5;
                } else {
//                            n.color = '#fff';
                    n.size = 0;
                }
            });

            var x = 0;
            s.graph.edges().forEach(function (e) {
                if (toKeep[e.source] && toKeep[e.target]) {
                    if (toKeep[e.source].id == nodeId || toKeep[e.target].id == nodeId) {
//                                e.color = e.originalColor;
                        e.size = e.originalSize;
                    } else {
//                                e.color = '#fff';
                        e.size = 0.0001;
                    }
                } else {
//                            e.color = '#fff';
                    e.size = 0.0001;
                }
                x++;
            });

            // Since the data has been modified, we need to
            // call the refresh method to make the colors
            // update effective.
            s.refresh();
        });

        s.bind('clickStage', function (e) {
            s.graph.nodes().forEach(function (n) {
//                        n.color = n.originalColor;
                n.size = n.originalSize;
            });

            s.graph.edges().forEach(function (e) {
//                        e.color = e.originalColor;
                e.size = e.originalSize;
            });

            // Same as in the previous event:
            s.refresh();
        });

        if(!window.ss) {
            window.ss = {};
        }

        window.ss[graphName] = s;
        loaded.push(mode);
        data[mode] = s;

        s.refresh();
        s.refresh();
    });
}