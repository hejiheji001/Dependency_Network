var myChart = echarts.init(document.getElementById('getGraph'));
myChart.showLoading();
// $.get('/res/getGraph.gexf', function (xml) {
    myChart.hideLoading();

    // var graph = echarts.dataTool.gexf.parse(xml);
    // console.log(graph);
    var categories = testData.nodes.map(function(d){
        return {name:d.name};
    });

    graph.nodes.forEach(function (node) {
        node.value = node.symbolSize;
    });
    option = {
        title: {
            text: 'Les Miserables',
            subtext: 'Default layout',
            top: 'bottom',
            left: 'right'
        },
        tooltip: {},
        legend: [{
            // selectedMode: 'single',
            data: categories.map(function (a) {
                return a.name;
            }),
            show: false
        }],
        animation: true,
        focusNodeAdjacency: true,
        series : [
            {
                name: 'Les Miserables',
                type: 'graph',
                layout: 'none',
                data: graph.nodes,
                links: graph.links,
                categories: categories,
                roam: true,
                label: {
                    normal: {
                        position: 'right'
                    }
                },
                force: {
                    repulsion: 100
                }
            }
        ]
    };

    myChart.setOption(option);
// }, 'xml');