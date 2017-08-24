$(function() {
    $("#jqGrid").jqGrid({
        url: baseUrl+"platform/risk/managementinfoList",
        datatype: "json",
        colModel: [ {
            label: '企业工信号',
            name: 'creditCode',
            width: 75
        }, {
            label: '时段',
            name:'period',
            width: 90
        }],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function() {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({
                "overflow-x": "hidden"
            });
        }
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            queryStr: null
        },
        showList: true,
        title: null,
        roleList: {},
        user: {
            status: 1,
            roleIdList: []
        }
    },
    methods: {
        query: function() {
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'queryStr': vm.q.queryStr},
                page:1
            }).trigger("reloadGrid");
        },
        add: function() {
        },
        update: function(userId) {
        },
        del: function() {
        },
        saveOrUpdate: function(event) {
        },
        getUser: function(userId) {

        },
        reload: function(event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'queryStr': vm.q.queryStr
                },
                page: page
            }).trigger("reloadGrid");
        }
    }
});

