

degree_switch.bootstrapToggle({
	on: '<span class="glyphicon glyphicon-eye-close"></span>',
	off: '<span class="glyphicon glyphicon-eye-open"></span>'
});
degree_switch.each(function(){
	$(this).on({
		change: function(e){
			var on = $(this).prop('checked');
			var graph = echarts.getInstanceByDom(document.getElementById($(this).attr("data")));
			if(on){
				graph.setOption({
					visualMap: [{
								type: 'continuous',
								min: 0,
								max: 50,
								dimension: 1,
								text:['Outdegree',''],
								realtime: false,
								calculable : true
							}
				   ,{
							type: 'continuous',
							min: 0,
							max: 1000,
							bottom: 200,
							dimension: 0,
							text:['Indegree',''],
							realtime: false,
							calculable : true,
							color: ['orangered','yellow','lightskyblue']
						}
					]
				})
			}else{
				graph.setOption(op, true);
			}
		}
	});
})


legend.bootstrapToggle({
	on: '<span class="glyphicon glyphicon-th"></span>',
	off: '<span class="glyphicon glyphicon-th"></span>'
});
legend.each(function(){
	$(this).on({
		change: function(e){
			var on = $(this).prop('checked');
			var graph = echarts.getInstanceByDom(document.getElementById($(this).attr("data")));
			if(on){
				graph.setOption({
					legend:{
						show: true
					}
				})
			}else{
				graph.setOption({
					legend:{
						show: false
					}
				})
			}
		}
	});
})

var getXY = function(x, y){
	var temX = x;
	var temY = y;

	if(x < 20){
		x = 50 * x;
	}

	if(y < 20){
		y = 50 * y;
	}

	if(y > h){
		y = h - 50;
	}

	if(x > y){
		y = h - y;
		x = w - x;
	}

	if(x < y){

	}

	if(temX > (w / 2) || temY > (h / 2)){
		x = (Math.log(temX) + w) / 2;
		y = (Math.log(temY) + h) / 2;
	}

	x = (Math.random() * 5) * x;
	y = (Math.random() * 5) * y;
	// x = Math.exp(Math.log(124 * x));
	// y = Math.exp(Math.log(321 * y));

	return [x, y];
}

var h = window.outerHeight;
var w = window.outerWidth;
$(".graph").css("height", h);

function getZ(num){
    var rounded;
    rounded = (0.5 + num) | 0;
    // A double bitwise not.
    rounded = ~~ (0.5 + num);
    // Finally, a left bitwise shift.
    rounded = (0.5 + num) << 0;

    return rounded;
}

var pctn_graph = echarts.init(document.getElementById('getGraph'));

var opGeneral = {
	title: {
		text: 'PCTN',
		top: 'bottom',
		left: 'right'
	},
	backgroundColor: '#F4F4F4',
	itemStyle: {
		normal: {
			borderColor: 'white',
			borderWidth: 1
		}
	},
	color: ['#FF0DF1', '#00C031', '#61a0a8', '#36C036',
			'#8900FF', '#FAAD00', '#bda29a', '#FF0066',
			'#7D7EFF', '#D7FF00', '#FF925E', '#61a0a8',
			'#d48265', '#91c7ae', '#749f83', '#ca8622',
			],
	// animation: false,
	// animationThreshold: 100,
	// hoverAnimation: true,
	toolbox: {
		 show : true,
		 orient : 'vertical',
		 left: 'right',
		 top: 'center',
		 itemGap: 20,
		 feature : {
			 mark : {
				show: true
			 },
			 restore : {
				show: true,
				title: 'Restore Graph'
			 },
			 saveAsImage : {
				show: true,
				title: 'Save As Image'
			 }
		 }
	},
	animationDurationUpdate: 1500,
	animationEasingUpdate: 'quinticInOut',
	legend: [{
		// selectedMode: 'multiple',
		left: 'left',
		show: false
	}],
	series: {
		type: 'graph',
		layout: 'none',
		edgeSymbol: ['none', 'arrow'],
		edgeSymbolSize: [0, 4],
		// progressiveThreshold: 100,
		edgeLabel: {
			emphasis: {
				show: true,
				textStyle: {
					color: 'red',
					fontSize: 18,
					fontWeight: 'bold'
				}
			},
			normal: {
				show: true,
				position: 'middle',
				formatter: function(d){
					console.log(d);
					return "dd";
				},
				textStyle: {
					color: 'transparent'
				}
			}
		},
		label: {
			emphasis: {
				// position: 'top',
				show: true,
				textStyle: {
					color: '#262626',
					fontSize: 16
				}
			}
		},
		force: {
			repulsion: 400,
			edgeLength: 250,
			initLayout: 'circular'
			// layoutAnimation: false
		},
		roam: true,
		focusNodeAdjacency: true,
		// draggable: true,
		lineStyle: {
			normal: {
				width: 1,
				// curveness: 0.3,
				color: 'target'
			},
			emphasis: {
				width: 5,
				color: 'red'
			}
		}
	}
}
pctn_graph.showLoading();
 $.get('/res/pctn_in.gexf', function (xml) {
	pctn_graph.hideLoading();
	 var graph = echarts.dataTool.gexf.parse(xml);
	//var graph = window.testData;

	var categories = graph.nodes.map(function(d){
	    return {name:d.name};
	});

	graph.nodes.forEach(function (node) {
	    node.value = node.symbolSize;
	});

	graph.links.forEach(function (link) {
	    link.lineStyle.normal.color = "source";
	});

	pctn_graph.hideLoading();
	pctn_graph.setOption(option = {
		title: {
			text: 'PCTN'
		},
		legend: [{
			data: categories.map(function (a) {
                return a.name;
            })
		}],
		series: {
			type: 'graph',
			layout: 'none',
			zlevel: 1,
			categories: categories,
			data: graph.nodes,
			links: graph.links
		}
	}, true);
	pctn_graph.setOption(opGeneral);
	window.op = pctn_graph.getOption();
 }, 'xml');

pctn_graph.on('mouseover', function (params) {
	if(params.dataType == "edge"){
		pctn_graph.dispatchAction({
		    type: 'highlight',
		    batch: [{
	            name: params.data.source
		    }, {
	            name: params.data.target
	        }]
		});
	}
});

pctn_graph.on('mouseout', function (params) {
	if(params.dataType == "edge"){
		pctn_graph.dispatchAction({
		    type: 'downplay',
		    batch: [{
	            name: params.data.source
		    }, {
	            name: params.data.target
	        }]
		});
	}
});

x = document.getElementById('getGraph');
window.onresize = function(){
	pctn_graph.resize();
}
