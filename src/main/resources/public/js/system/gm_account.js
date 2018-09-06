var form;
$(document).ready(function () {
    form = $("#form").ligerForm({
        fields: [
            {display: "当前密码", name: "password", type: "text", validate: {required: true}},
            {display: "新密码", name: "new_password", type: "text", validate: {required: true}},
            {display: "确认密码", name: "confirm_password", type: "text", validate: {required: true}},
        ],
        buttons: [
            {text: "修改密码", width: 60, click: modify_password},
        ]
    });
});

function modify_success(data) {
    if (data == 0)
        $.ligerDialog.alert("修改失败，密码错误。");
    else
        $.ligerDialog.alert("修改成功。");
}

function modify_password() {
    var data = form.getData();
    var operate = "";

    if (data.new_password == "" || data.new_password != data.confirm_password)
        return;
    $.ligerDialog.confirm("确认修改", function (conform) {
        if (!conform)
            return;
        do_ajax("modify_password.do", {password: data.password, new_password: data.new_password}, modify_success);
    });
}
