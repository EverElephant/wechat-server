/**
 * 通用抽象方法
 */

//显示新增弹窗
function show_add() {
    $("#add").show();
    $("#add_form").show();
}

//隐藏新增弹窗
function hide_add() {
    $("#add").hide();
    $("#add_form").hide();
}

//隐藏修改弹窗
function hide_edit() {
    $("#edit_form").hide();
    $("#edit").hide();
}

//显示修改弹窗
function show_edit() {
    $("#edit").show();
    $("#edit_form").show();
}

//需要特定页面自定义
function reset_edit() {
    edit_form.setData({id: ""});
}

//需要特定页面自定义
function reset_add() {
    edit_form.setData({id: ""});
}

function init_operate(rowdata, rowindex, value) {
    var operate = "";
    operate += "<a href='javascript:do_update()'>修改</a> ";
    operate += "<a href='javascript:do_delete()'>删除</a>";
    return operate;
}

//通用方法，如果不支撑请在具体页面js覆盖
function do_query() {
    do_ajax(query_url, {}, query_success);
}

//通用方法，如果不支撑请在具体页面js覆盖
function to_insert() {
    hide_edit();
    reset_add();
    show_add();
}

//通用方法，如果不支撑请在具体页面js覆盖
function to_update() {
    hide_add();
    reset_edit();
    edit_form.setData(grid.getSelectedRow());
    show_edit();
}

//通用方法，如果不支撑请在具体页面js覆盖
function do_insert() {
    var data = add_form.getData();
    $.ligerDialog.confirm("确认提交", function (conform) {
        if (!conform)
            return;
        do_ajax(insert_url, data, query_success);
    });
}

//通用方法，如果不支撑请在具体页面js覆盖
function do_update() {
    var data = edit_form.getData();
    $.ligerDialog.confirm("确认提交", function (conform) {
        if (!conform)
            return;
        do_ajax(modify_url, data, query_success);
    });
}

//通用方法，如果不支撑请在具体页面js覆盖
function do_delete() {
    $.ligerDialog.confirm("确认删除", function (conform) {
        if (!conform)
            return;
        do_ajax(delete_url, {id: grid.getSelectedRow().id}, query_success);
    });
}

//通用方法，如果不支撑请在具体页面js覆盖
function query_success(data) {
    grid.set({data: {Rows: data}});
    fix_grid();
}

//grid格式化，显示grid隐藏的内容
function fix_grid() {
    $(".l-grid2 .l-grid-row-cell-inner").css("word-break", "break-all").css("height", "100%");
}
