function do_ajax(url, data, success) {
    parent.loading.show();
    $.ajax({
        type: "post",
        url: url,
        data: data,
        dataType: "json",
        timeout: 20000,
        success: function (data) {
            parent.loading.hide();
            switch (data.result) {
                case 0:
                    $.ligerDialog.alert("操作失败。");
                    return;
                case -1:
                    $.ligerDialog.alert("manager转发时出现异常。");
                    return;
                case -2:
                    $.ligerDialog.alert("没有权限。");
                    return;
                case -3:
                    top.location.href = "login.html";
                    return;
                case -10:
                    $.ligerDialog.alert("manager服务器连接异常。");
                    return;
                case -11:
                    $.ligerDialog.alert("game服务器功能处理异常。");
                    return;
                case -20:
                    $.ligerDialog.alert("auth服务器连接异常。");
                    return;
                case -21:
                    $.ligerDialog.alert("auth服务器功能处理异常。");
                    return;
                case -30:
                    $.ligerDialog.alert("game服务器连接异常。");
                    return;
                case -31:
                    $.ligerDialog.alert("game服务器功能处理异常。");
                    return;
                case -22:
                    $.ligerDialog.alert("不能修改当前正在播放的提示");
                    return;
                case -23:
                    $.ligerDialog.alert("间隔时间错误");
                    return;
                case -24:
                    $.ligerDialog.alert("时间设置错误");
                    return;
                case -222:
                    $.ligerDialog.alert("暂无数据！");
                    return;
            }
            success(data.data);
        },
        error: function (data) {
            parent.loading.hide();
            $.ligerDialog.alert("未知错误。");

        }
    });
}

Date.prototype.Format = function (format) {
    var o =
        {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S": this.getMilliseconds()
        }
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

String.prototype.format = function (args) {
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if (args[key] != undefined) {
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    var reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}

function format_milliseconds(date) {
    if (date == null || date == "")
        return new Date().getTime();
    return new Date(date).getTime();
}

function format_date(date) {
    if (date == null)
        return new Date().Format("yyyy-MM-dd");
    return new Date(date).Format("yyyy-MM-dd");
}

function format_time(time) {
    if (time == null)
        return new Date().Format("yyyy-MM-dd hh:mm:ss");
    return new Date(time).Format("yyyy-MM-dd hh:mm:ss");
}

function format_minute(time) {
    if (time == null)
        return new Date().Format("yyyy-MM-dd hh:mm");
    return new Date(time).Format("yyyy-MM-dd hh:mm");
}

function time_difference(date) {
    var date1 = new Date(date);
    var milliseconds = new Date().getTime() - date1.getTime();
    return milliseconds;
}

function JSONToCSVConvertor(data) {
    var data2 = form.getData();
    var datefrom = data2.date_from.Format('yyyy-MM-dd');
    var dateto = data2.date_to.Format('yyyy-MM-dd');
    var ShowLabel = true;

    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    var arrData = typeof data != 'object' ? JSON.parse(data) : data;
    var CSV = '';
    //This condition will generate the Label/Header
    if (ShowLabel) {
        var row = "";

        //This loop will extract the label from 1st index of on array
        for (var index in arrData[0]) {
            //Now convert each value to string and comma-seprated
            row += index + ',';
        }
        // append Label row with line break
        CSV += row.slice(0, -1) + '\r\n';
    }

    //1st loop is to extract each row
    for (var i = 0; i < arrData.length; i++) {
        var row = "";
        //2nd loop will extract each column and convert it in string comma-seprated
        for (var index in arrData[i]) {
            row += '"' + arrData[i][index] + '",';
        }
        row.slice(0, row.length - 1);
        //add a line break after each row
        CSV += row + '\r\n';
    }

    if (CSV == '') {
        $.ligerDialog.alert("Invalid data");
        return;
    }

    //this trick will generate a temp "a" tag
    var link = document.createElement("a");
    link.id = "lnkDwnldLnk";

    //this part will append the anchor tag and remove it after automatic click
    document.body.appendChild(link);

    var csv = CSV;
    blob = new Blob([csv], {type: 'text/csv'});
    var csvUrl = (window.URL || window.webkitURL).createObjectURL(blob);
    var filename = datefrom + "/" + dateto + ".csv";
    $("#lnkDwnldLnk")
        .attr({
            'download': filename,
            'href': csvUrl
        });

    $('#lnkDwnldLnk')[0].click();
    document.body.removeChild(link);
}

/**
 * 对于obj的null值属性进行过滤
 * @param obj
 */
function filter(obj) {
    var res = {}
    $.each(obj, function (key, value) {
        if(value != null) {
            res[key] = value;
        }
    })
    return res;
}

/**
 * 导出excel
 * @param filename 导出名称
 * @param data json数据
 * @param header excel头
 * @param keys 导出的顺序，可选
 */
function outputExcel(filename, data, header, keys) {
    if (!!keys) {
        var dataList = new Array();
        for (var i = 0; i < data.length; i++) {
            var newRow = {};
            var row = data[i];
            for (var j = 0; j < keys.length; j++) {
                newRow[keys[j]] = row[keys[j]];
            }
            dataList.push(newRow);
        }
        data = dataList;
    }
    var wopts = { bookType:'xlsx', bookSST:false, type:'binary' };
    var workbook = { SheetNames: ['Sheet1'], Sheets: {}, Props: {} };
    var sheet1 = XLSX.utils.json_to_sheet(data);
    var range = XLSX.utils.decode_range(sheet1['!ref']);
    for (var C = range.s.c, i = 0; C <= range.e.c; C++,i++) {
        var address = XLSX.utils.encode_col(C) + "1";
        if (!sheet1[address]) continue;
        sheet1[address].v = header[i];
    }
    workbook.Sheets['Sheet1'] = sheet1;
    var wbout = XLSX.write(workbook,wopts);
    /* the saveAs call downloads a file on the local machine */
    saveAs(new Blob([s2ab(wbout)],{type:"application/octet-stream"}), filename + ".xlsx");
}

function s2ab(s) {
    var buf = new ArrayBuffer(s.length);
    var view = new Uint8Array(buf);
    for (var i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
}