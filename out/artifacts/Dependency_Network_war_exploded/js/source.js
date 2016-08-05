define(function() {
	var source = {
		"stocks": [
			"3In",
			"Aa",
			"Aal",
			"Abf",
			"Aca",
			"Adm",
			"Adn",
			"Agk",
			"Agr",
			"Aht",
			"Ald"
		],
		"data": [
			{
				"x": "Aca",
				"y": "3In",
				"z": "Aa"
			},
			{
				"x": "Aca",
				"y": "Agr",
				"z": "Aa"
			},
			{
				"x": "Aca",
				"y": "Aht",
				"z": "Aa"
			},
			{
				"x": "Adn",
				"y": "Aal",
				"z": "Agk"
			},
			{
				"x": "Agk",
				"y": "3In",
				"z": "Aa"
			},
			{
				"x": "Agr",
				"y": "Aa",
				"z": "3In"
			},
			{
				"x": "Agr",
				"y": "Abf",
				"z": "Aht"
			},
			{
				"x": "Agr",
				"y": "Aca",
				"z": "Aa"
			},
			{
				"x": "Aht",
				"y": "Aca",
				"z": "Aa"
			},
			{
				"x": "3In",
				"y": "Aca",
				"z": "Aa"
			},
			{
				"x": "3In",
				"y": "Agk",
				"z": "Aa"
			},
			{
				"x": "Aa",
				"y": "Abf",
				"z": "Aht"
			},
			{
				"x": "Aa",
				"y": "Agr",
				"z": "3In"
			},
			{
				"x": "Aal",
				"y": "Adn",
				"z": "Agk"
			},
			{
				"x": "Abf",
				"y": "Aa",
				"z": "Aht"
			},
			{
				"x": "Abf",
				"y": "Agr",
				"z": "Aht"
			}
		],
		"result": "true"
	};

	var stockArray;
	var option = {
		title: {
			text: 'PCTN',
			x:'right',
			y:'bottom'
		},
		tooltip: {
			trigger: 'item',
			formatter: '{a} {b}'
		},
		toolbox: {
			show: true,
			feature: {
				saveAsImage: {show: true, title : 'Save as image'}
			}
		},
		legend: {
			x: 'left',
			data:[],
			textStyle: {
				fontSize: 14
			}
		},
		series: [
			{
				type: 'force',
				categories: [],
				linkSymbol: 'arrow',
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'bottom',
							textStyle: {
								color: '#000',
								fontSize: 14,
								fontWeight: 'bold',
							},
							labelLine: {
					            show: true,
					            length: 400
					        }
						},
						nodeStyle: {
							brushType: 'both',
							borderColor: 'rgba(255,215,0,0.4)',
							borderWidth: 2
						}
					}
				},
				center: ['50%', '50%'],
				minRadius: 5,
				maxRadius: 30,
				gravity: 2,
				scaling: 2,
				roam: true,
				nodes:[],
				links: []
			}
		]
	};

	var myChart;
	var finalData;

	var setArgs = function(obj){
		myChart = obj["chart"];
	}

	var getArgs = function(){
		return {
			"option": option,
			"chart": myChart
		}
	}

	var getSource = function () {
		$.ajax({
			type : "POST",
			crossDomain: true,
			url : "http://localhost:8080/get/?stocks=all",
			success : function (data) {
				finalData = data;
				setChartData();
			},
			error: function(){
				// myChart.showLoading();
				finalData = source;
				setChartData();
			}
		});


		return this;
	};

	var parseToEchart = function (){
		var tem = finalData;

		if(tem){
			var data = tem["data"];

			var seriesCata = [];
			var seriesNode = [];
			var seriesLink = [];
			var seriesLegend = tem["stocks"];

			for (var i = 0; i < seriesLegend.length; i++) {
				var n = seriesLegend[i];
				var cata = {"name": n};
				var node = {"category": i, "name": n};
				seriesCata.push(cata);
				seriesNode.push(node);
			};

			for (var j = 0; j < data.length; j++) {
				var link = {source: null, target: null, weight: 25, name: null, itemStyle: {normal: {color: "#5795FF", width: 2 } } };
				var x = data[j]["x"];
				var y = data[j]["y"];
				var z = data[j]["z"];

				link["source"] = z;
				link["target"] = x;
				seriesLink.push(link);
				link["target"] = x;
				seriesLink.push(link);

			}
			option["legend"]["data"] = seriesLegend;
			option["series"][0]["categories"] = seriesCata;
			option["series"][0]["nodes"] = seriesNode;
			option["series"][0]["links"] = seriesLink;
			return option;
		}else{
			console.log("No Data");
			return null;
		}
	};

	var setChartData = function(){
		parseToEchart();
		myChart.setOption(option);
	};

	var textGenerator = function(){

	}

	return {"setArgs": setArgs, "getSource": getSource, "getArgs": getArgs, "textGenerator": textGenerator};
})

