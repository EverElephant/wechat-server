var form, grid, edit_form, edit_tree, edit_button;
var modify;
var query_url = "query_gm_account.do";
var delete_url = "delete_gm_account.do";
$(document).ready(function () {
    form = $("#form").ligerForm({
        buttons: [
            {text: "添加", width: 60, click: to_insert},
            {text: "刷新", width: 60, click: do_query},
        ]
    });
    grid = $("#grid").ligerGrid({
        columns: [
            {display: "账号", width: 150, name: "id", type: "text"},
            //{ display : "密码", width: 150, name : "password", type : "text" },
            {display: "权限", name: "operate", type: "text"},
            {display: "操作", width: 150, name: "manage_operate", render: init_operate}
        ],
        allowAdjustColWidth: false,
    });
    edit_form = $("#edit_form").ligerForm({
        fields: [
            {display: "账号", name: "id", type: "text", validate: {required: true}},
            {display: "密码", name: "password", type: "text", validate: {required: true}},
        ]
    });
    edit_button = $("#edit_button").ligerForm({
        buttons: [
            {text: '提交', width: 60, click: do_update},
            {text: '关闭', width: 60, click: hide_edit},
        ]
    });
    var left = $(window).width() - $(parent).width() / 2 - 300;
    var top = $(window).height() - $(parent).height() / 2 - 300;
    var top = "170";
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
    query_operate_names();
    do_query();
});

function query_operate_names_success(data) {
    var list = {};
    var tree = [{text: "操作权限", id: "0000", children: []}];
    $.each(data, function (i, item) {
        if (item.id % 100 == 0) {
            item.children = [];
            tree[0].children.push(item);
            list[parseInt(item.id / 100)] = tree[0].children.length - 1;
        }
        else {
            tree[0].children[list[parseInt(item.id / 100)]].children.push(item);
        }
    });
    edit_tree = $("#edit_tree").ligerTree({
        data: tree,
        isExpand: 2,
        nodeWidth: 300,
        treeLine: false,
        enabledCompleteCheckbox: false,
        childIcon: "",
        parentIcon: ""
    });
}

function query_operate_names() {
    do_ajax("query_operate_names.do", {}, query_operate_names_success);
}

function to_update() {
    reset_edit();
    edit_form.setData({id: grid.getSelectedRow().id, password: ""});
    $.each(edit_tree.getData(), function (i0, item0) {
        $.each(item0.children, function (i1, item1) {
            $.each(item1.children, function (i2, item2) {
                if (grid.getSelectedRow().operate.indexOf(item2.id) != -1)
                    edit_tree.selectNode(item2.id);
            })
            if (grid.getSelectedRow().operate.indexOf(item1.id) != -1)
                edit_tree.selectNode(item1.id);
        })
        if (grid.getSelectedRow().operate != "")
            edit_tree.selectNode(item0.id);
    })
    show_edit();
    modify = 1;
}
function to_insert(){
    reset_edit();
    show_edit();
    modify = 0;
}
function reset_edit() {
    edit_form.setData({id: "", password: ""});
    $.each(edit_tree.getCheckedData(), function (i, item) {
        edit_tree.cancelSelect(item.treedataindex);
    })
}

function do_update() {
    var data = edit_form.getData();
    var operate = "";


    if (data.id == "")
        return;
    if (modify == 0 && data.password == "") {
        return;
    }
    $.each(edit_tree.getCheckedData(), function (i, item) {
        if (item.treedataindex != 0)
            operate += "," + item.id;
    })
    $.ligerDialog.confirm("确认提交", function (conform) {
        if (!conform)
            return;
        do_ajax("modify_gm_account.do", {
            modify: modify,
            id: data.id,
            password: data.password,
            operate: operate.slice(1)
        }, modify_success);
        hide_edit();
    });
}
function modify_success(data) {
    if (data == -1)
        $.ligerDialog.alert("用户已存在");
    else{
        do_query();
    }
}
