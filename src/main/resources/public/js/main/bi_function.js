
function init_form()
{
	form2 = $("#form2").ligerForm({
		fields: [
		    { display: "起始时间", name: "date_from", type: "date", format : "yyyy-MM-dd", editor: { type : "date", format: "yyyy-MM-dd", showTime: true, cancelable: false }, validate:{ required: true }, newline: false },
		    { display: "结束时间", name: "date_to", type: "date", format : "yyyy-MM-dd", editor: { type : "date", format: "yyyy-MM-dd", showTime: true, cancelable: false }, validate:{ required: true }, newline: false },
		    { display: "快速选择", name: "date_select", width: 300, type: "radiolist", newline : false, options: { rowSize : 5, onSelect : date_select, data: [ { text: "今天", id: 0 }, { text: "昨天", id: 1 }, { text: "近7日", id: 2 }, { text: "近30日", id: 3 }, { text: "全部", id: 4 } ] } },
		  //  { display: "平台选择", name: "platform_select", width: 300, type: "radiolist", options: { rowSize : 4, onSelect : platform_select, data: [ { text: "苹果", id: 3 }, { text: "安卓", id: 2 }, { text: "越狱", id: 0 },{ text: "全部", id: 1 } ] } },
		  //  { display: "渠道", name: "channel_id", type: "select", comboboxName: "select_channel", validate:{ required: true } },
		    //{ display: "区服", name: "area_id", type: "text", validate:{ required: true } },
		],
		buttons: [
			//{ text: "查询", width: 60, click: do_query },
			{ text: "导出excel", width: 60, click: download_excel },
		]
	});
	//$(".l-trigger-cancel").remove();
	//form.setData({ platform_select : 2});
}
function download_excel()
{
	var data=form2.getData();
	if(data.date_from==null||data.date_from==""||data.date_to==null||data.date_to==""||data.date_from>data.date_to)
	{
		alert("请选择正确的时间");
		return;
	}
	if(data.date_from=="-1"&&data.date_to=="-1")
		var url="download_project.do?date_from=-1&date_to=-1";
	else
		var url="download_project.do?date_from="+format_date(data.date_from)+"&date_to="+format_date(data.date_to);
	//do_ajax(url, { date_from : data.date_from,date_to:data.date_to }, query_success);
    window.open(url);
	
}
function query_channel_success(data)
{
	liger.get("select_channel").setData(data);
}

function query_channel()
{
	do_ajax("../function/query_list.do", { type : "channel" }, query_channel_success);
}

function platform_select(selected)
{
	switch (selected.value) 
	{
	    case "0":
	    	liger.get("select_channel").setData([{text : "官网",id : "0"}]);
	    	break;
		case "1":
			liger.get("select_channel").setData([{text : "IOS正版",id : "30001"}, {text : "应用宝",id : "20002"}, {text : "360",id : "20003"},{text : "小米",id : "20001"},{text : "官网",id : "0"}]);
			break;
		case "2":
			liger.get("select_channel").setData([{text : "应用宝",id : "20002"}, {text : "360",id : "20003"},{text : "小米",id : "20001"}]);
			break;
		case "3":
			liger.get("select_channel").setData([{text : "IOS正版",id : "30001"}]);
			break;
	}
}

function date_select(selected)
{
	switch (selected.value)
	{
		case "0":
			form2.setData({ date_from : format_date()});
			form2.setData({ date_to : format_date(format_milliseconds() + 86400000)});
			break;
		case "1":
			form2.setData({ date_from : format_date(format_milliseconds() - 86400000)});
			form2.setData({ date_to : format_date(format_milliseconds())});
			break;
		case "2":
			form2.setData({ date_from : format_date(format_milliseconds() - 86400000 * 7)});
			form2.setData({ date_to : format_date(format_milliseconds())});
			break;
		case "3":
			form2.setData({ date_from : format_date(format_milliseconds() - 86400000 * 30)});
			form2.setData({ date_to : format_date(format_milliseconds())});
			break;
		case "4":
			form2.setData({ date_from : "-1"});
			form2.setData({ date_to : "-1"});
			break;
	}
}