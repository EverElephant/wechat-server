var form;
var grid;
var button_form;
var form_count;
var form_output;
var query_url = "compare_order.do"
var gridData;
$(function () {
    form = $("#form").ligerForm({
        fields: [
            {display: "开始时间", name: "start", type: "date"},
            {display: "结束时间", name: "end", type: "date", newline: false},
        ]
    });

    button_form = $("#button_form").ligerForm({
        buttons: [
            {text: "对账", width: 60, click: do_query}
        ]
    })

    form_count = $("#form_count").ligerForm({
        fields: [
            {display: "gmt账单数量", name: "wechat_order_count", type: "text", newline: false, readonly:true},
            {display: "gmt账单金额", name: "wechat_rmb_count", type: "text", newline: false, readonly:true},
            {display: "gmt对错账单数量", name: "wechat_order_error", type:"text", newline: false, readonly:true},
            {display: "gmt对错金额", name: "wechat_rmb_error", type: "text", newline: false, readonly:true},

            {display: "财务账单数量", name: "finance_order_count", type: "text", newline: true, readonly:true},
            {display: "财务账单金额", name: "finance_rmb_count", type: "text", newline: false, readonly:true},
            {display: "财务对错账单数量", name: "finance_order_error", type:"text", newline: false, readonly:true},
            {display: "财务对错金额", name: "finance_rmb_error", type: "text", newline: false, readonly:true}
        ]
    })

    grid = $("#grid").ligerGrid({
        allowAdjustColWidth: false,
        columns: [
            {display: "序号", name: "id",width:100},
            {display: "订单时间", name:"time"},
            {display: "第三方订单号", name: "channel_order_id",width:250},
            {display: "商户订单号", name: "order_id",width:220},
            {display: "订单金额", name: "rmb"},
            {display: "订单类型", name: "order_type",width:100,render:function(r,i,v){
                if(v==4){
                    return "支出";
                }
                if(v==6){
                    return "收入";
                }
            }},
            {display: "地区", name:"area",width:100},
            {display: "备注", name:"note"}
        ]
    });

    form_output = $("#form_output").ligerForm({
        buttons: [
            {text: "导出", width: 60, click: output}
        ]
    })
})


function output() {
    var header = ["序号", "订单时间", "第三方订单号", "商户订单号","订单金额","地区","备注"];
    outputExcel("对账结果表", gridData, header);
}

function do_query() {
    var data = form.getData();
    if ($("#order_list").val()==""){
        $.ligerDialog.alert("请上传财务账单！");
        return;
    }
    if (data.start == null || data.end == null) {
        $.ligerDialog.alert("请输入时间");
        return;
    }
    if(data.end < data.start) {
        $.ligerDialog.alert("起始时间不能大于结束时间");
        return;
    }


    var formDOM = $("#orderListForm")[0];
    var formData = new FormData(formDOM);
    formData.append("start",data.start);
    formData.append("end",data.end);

    $.ajax({
        url :query_url,
        type : 'POST',
        data : formData,
        async : false,
        cache : false,
        contentType : false,
        processData : false,
        success : function(res) {
            if (res.data == "-1") {
                $.ligerDialog.alert("上传文件格式不对");
                return;
            }else if (res.data == "-2") {
                $.ligerDialog.alert("未知错误，请重试");
                return;
            }else if (res.data == "-3") {
                $.ligerDialog.alert("文件不符合约定的规范");
                return;
            }

            gridData = res.data.list;
            grid.set({data: {Rows: gridData}});
            fix_grid();

            form_count.setData(res.data.count)
        },
        error : function(returndata) {
            $.ligerDialog.alert("对账失败");
        }
    });
}


