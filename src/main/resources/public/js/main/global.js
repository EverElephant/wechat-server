var areaMap = {
    "10002":"贵阳",
    "10001":"兴义",
    "10003":"毕节",
    "10004":"安顺",
    "10005":"贵州"
}

var payTypeMap = {
    "1":"支付宝",
    "2":"微信",
    "3":"苹果内购",
    "6":"兴业微信支付"
}

var serverTypeMap = {
    "1":"正式服",
    "2":"提审服",
    "3":"测试服"
}

var orderStatusMap = {
    "1":"创建",
    "2":"已验签",
    "3":"推送中",
    "4":"成功",
    "5":"失败"
}

var selectArea = new Array();

function maoToOptions(map) {
    var arr = new Array();
    $.each(map, function (key, value) {
        var o = {
            id : key,
            text : value
        };
        arr.push(o);
    })
    return arr;
}

$.each(areaMap, function (key, value) {
    var o = {
        id : key,
        text : value
    };
    selectArea.push(o)
})

var selectStatus = [{id:4,text:"成功"},{id:5,text:"失败"}];
