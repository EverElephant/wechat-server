$(document).ready(function () {
    init();
});

function query_menu_list_success(data) {
    $.each(data, function (i, item) {
        if (item.id % 100 == 0) {
            $("#accordion").append("<div title=" + item.name + " class='l-scroll'><ul id=tree-" + parseInt(item.id / 100) + " class='tree'></ul></div>");
            $("#tree-" + parseInt(item.id / 100)).ligerTree({
                checkbox: false,
                autoCheckboxEven: false,
                single: true,
                slide: false,
                needCancel: false,
                nodeWidth: 145,
                childIcon: '',
                enabledCompleteCheckbox: false,
                attribute: ['url'],
                textFieldName: 'text',
                onSelect: function (node) {
                    if (!node.data.url)
                        return;
                    //liger.get("framecenter").removeAll();
                    liger.get("framecenter").addTabItem({
                        tabid: node.data.tabid,
                        url: node.data.url
                    });
                }
            });
        }
        else {
            var data = liger.get("tree-" + parseInt(item.id / 100)).getData();
            data.push({id: parseInt(item.id / 100), text: item.name, tabid: item.name, url: item.url});
            liger.get("tree-" + parseInt(item.id / 100)).setData(data);
        }
    });
    $("#accordion").ligerAccordion({speed: 100, changeHeightOnResize: true});
}

function init() {
    window.loading = $("#pageloading");
    do_ajax("html/function/query_list.do", {type: "menu"}, query_menu_list_success);
    var tab = null;
    var tree = null;
    var tabItems = [];
    var cacheData = {
        playerId: "",
        selectServerId: "",
        selectAreaId: "",
        guildId: "",
    };
    $(function () {
        $("#layout").ligerLayout({leftWidth: 190});
        var height = $(".l-layout-center").height();
        $("#framecenter").ligerTab({
            height: height,
            showSwitchInTab: true,
            showSwitch: true,
            onAfterAddTabItem: function (tabdata) {
                tabItems.push(tabdata);
            },
            onAfterRemoveTabItem: function (tabid) {
                for (var i = 0; i < tabItems.length; i++) {
                    var o = tabItems[i];
                    if (o.tabid == tabid) {
                        tabItems.splice(i, 1);
                        break;
                    }
                }
            },
            onReload: function (tabdata) {
                var tabid = tabdata.tabid;
                addFrameSkinLink(tabid);
            }
        });


        $(".l-link").hover(function () {
            $(this).addClass("l-link-over");
        }, function () {
            $(this).removeClass("l-link-over");
        });
    });
}
