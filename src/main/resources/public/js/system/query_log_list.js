var form, grid;
$(document).ready(function () {
    form = $("#form").ligerForm({
        fields: [
            {display: "账号", name: "account", type: "select", comboboxName: "select_account"},
            {display: "操作", name: "operate", type: "select", comboboxName: "select_operate", newline: false},
            {
                display: "开始时间",
                name: "date_from",
                type: "date",
                editor: {type: "date", format: "yyyy-MM-dd hh:mm:ss", showTime: true, cancelable: false},
                validate: {required: true}
            },
            {
                display: "结束时间",
                name: "date_to",
                type: "date",
                newline: false,
                editor: {type: "date", format: "yyyy-MM-dd hh:mm:ss", showTime: true, cancelable: false},
                validate: {required: true}
            },
        ],
        buttons: [
            {text: '查询', width: 60, click: query_log_list}
        ]
    });
    $(".l-trigger-cancel").remove();
    query_log_account();
    query_log_operate();
    grid = $("#grid").ligerGrid({
        columns: [
            {display: "ID", name: "id", width: 150},
            {display: "账号", name: "account", width: 150},
            {display: "操作", name: "operate", width: 150},
            {display: "时间", name: "time", width: 150},
            {display: "IP", name: "ip", width: 150},
            {display: "内容", name: "content"},
        ],
        allowAdjustColWidth: false,
    });
});

function query_log_account_success(data) {
    data.unshift({id: "", text: "全部"});
    liger.get("select_account").setData(data);
}

function query_log_account() {
    do_ajax("query_log_account.do", {}, query_log_account_success);
}

function query_log_operate_success(data) {
    data.unshift({id: "", text: "全部"});
    liger.get("select_operate").setData(data);
}

function query_log_operate() {
    do_ajax("query_log_operate.do", {}, query_log_operate_success);
}

function query_log_list() {
    var data = form.getData();

    if (data.date_from == null || data.date_to == null)
        return;
    do_ajax("query_log_list.do", {
        account: data.account, operate: data.operate, date_from: format_time(data.date_from),
        date_to: format_time(data.date_to)
    }, query_success);
}