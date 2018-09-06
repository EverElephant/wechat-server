var form, grid, add_form, edit_form;
var insert_url = "insert_new_agency_account_temp.do"
var query_url = "query_new_agency_account_temp.do";
var update_url = "modify_new_agency_account_temp.do";
var delete_url = "delete_new_agency_account_temp.do";


$(document).ready(function () {
    form = $("#form").ligerForm({
        fields: [
            {display: "手机号", name: "phone", type: "text"}
        ],
        buttons: [
            {text: "添加", width: 60, click: to_insert},
            {text: "查询", width: 60, click: do_query}
        ]
    });
    grid = $("#grid").ligerGrid({
        columns: [
            {display: "ID", name: "id"},
            {display: "昵称", name: "user_name"},
            {display: "微信号", name: "weixin"},
            {display: "手机号", name: "phone"},
            {display: "是否激活", name: "active", render: init_status},
            {display: "代理等级", name: "agency_lv"},
            {display: "创建时间", name: "create_time", render: time_render},
            {display: "操作", name: "manage_operate", render: init_operate}
        ],
        allowAdjustColWidth : false,
    });

    //新增
    add_form = $("#add_form").ligerForm({
        fields: [
            {display: "昵称", name: "user_name", type: "text", validate: {required: true}},
            {display: "微信号", name: "weixin", type: "text", validate: {required: true}},
            {display: "手机号", name: "phone", type: "text", validate: {required: true}},
            {display: "代理等级", name: "agency_lv", type: "select", comboboxName: "select_agency_lv"}
        ],
        buttons: [
            {text: '提交', width: 60, click: do_insert},
            {text: '关闭', width: 60, click: hide_add},
        ]
    });
    //设置select内容
    liger.get("select_agency_lv").setData([{id: 0, text: "普通代理"}, {id: 1, text: "总代"}, {id: 2, text: "区域总代"}]);

    //修改
    edit_form = $("#edit_form").ligerForm({
        fields: [
            //{ display : "ID", name : "id", type : "hidden"},
            {display: "ID", name: "id", type: "int", readonly: true},
            {display: "昵称", name: "userName", type: "text", validate: {required: true}}
        ],
        buttons: [
            {text: '提交', width: 60, click: do_modify},
            {text: '关闭', width: 60, click: hide_edit}
        ]
    });

    var left = $(window).width() - $(parent).width() / 2 - 300;
    var top = $(window).height() - $(parent).height() / 2 - 300;
    var top = "170";

    $("#add").css({
        "position": "absolute",
        "width": "600px",
        "height": "500px",
        "top": top + "px",
        "left": left + "px",
        "border": "1px solid #BED5F3",
        "background-color": "#FFFFFF"
    }).hide();
    $("#add_form").hide();
    $("#edit").css({
        "position": "absolute",
        "width": "600px",
        "height": "500px",
        "top": top + "px",
        "left": left + "px",
        "border": "1px solid #BED5F3",
        "background-color": "#FFFFFF"
    }).hide();
    $("#edit_form").hide();
    do_query();
});

//grid的列active渲染
function init_status(rowdata, rowindex, value) {
    var operate = "";
    if (rowdata.active == 0) {
        operate += "否";
    }
    else if (rowdata.active == 1) {
        operate += "是";
    }
    return operate;
}
//grid的列createTime渲染
function time_render(rowdata, rowindex, value) {
    return format_time(value);
}

function reset_add() {
    add_form.setData({user_name: "", weixin: ""})
}
function reset_edit() {
    edit_form.setData({user_name: "", weixin: "", phone: "", agency_lv: 0});
}
function do_insert() {
    var data = add_form.getData();

    if (data.user_name == "" || data.weixin == "" || data.phone == "") {
        alert("所有项不能为空！");
        return;
    }
    //select 特殊处理
    if (data.agency_lv == "") {
        data.agency_lv = 0;
    }
    $.ligerDialog.confirm("确认提交", function (conform) {
        if (!conform)
            return;
        do_ajax(insert_url, data, do_insert_seccess);
    });

}
function do_insert_seccess(data) {
    if (data == -1) {
        alert("昵称重复！");
        return;
    }
    if (data == -2) {
        alert("微信重复！");
        return;
    }
    if (data == -3) {
        alert("手机重复！");
        return;
    }
    hide_add();
    do_query();
}
function do_modify() {
    var data = edit_form.getData();
    if (data.id == "" || data.user_name == "") {
        alert("所有项不能为空！");
        return;
    }
    $.ligerDialog.confirm("确认提交", function (conform) {
        if (!conform)
            return;
        do_ajax(update_url, data, do_modify_success);
    });
}

function do_modify_success(data) {
    if (data == 0) {
        alert("昵称重复！");
        return;
    }
    hide_edit();
    do_query();
}

function do_query() {
    var data = form.getData();
    do_ajax(query_url, data, query_success);
}
