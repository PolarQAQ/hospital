"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[761],{7761:function(e,t,a){a.r(t),a.d(t,{default:function(){return c}});var i=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticClass:"search"},[t("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"请输入科室名称查询"},model:{value:e.name,callback:function(t){e.name=t},expression:"name"}}),t("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"info",plain:""},on:{click:function(t){return e.load(1)}}},[e._v("查询")]),t("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"warning",plain:""},on:{click:e.reset}},[e._v("重置")])],1),t("div",{staticClass:"operation"},[t("el-button",{attrs:{type:"primary",plain:""},on:{click:e.handleAdd}},[e._v("新增")]),t("el-button",{attrs:{type:"danger",plain:""},on:{click:e.delBatch}},[e._v("批量删除")])],1),t("div",{staticClass:"table"},[t("el-table",{attrs:{data:e.tableData,stripe:""},on:{"selection-change":e.handleSelectionChange}},[t("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),t("el-table-column",{attrs:{prop:"id",label:"序号",width:"80",align:"center",sortable:""}}),t("el-table-column",{attrs:{prop:"name",label:"科室名称","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"description",label:"科室描述","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{label:"操作",width:"180",align:"center"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-button",{attrs:{plain:"",type:"primary",size:"mini"},on:{click:function(t){return e.handleEdit(a.row)}}},[e._v("编辑")]),t("el-button",{attrs:{plain:"",type:"danger",size:"mini"},on:{click:function(t){return e.del(a.row.id)}}},[e._v("删除")])]}}])})],1),t("div",{staticClass:"pagination"},[t("el-pagination",{attrs:{background:"","current-page":e.pageNum,"page-sizes":[5,10,20],"page-size":e.pageSize,layout:"total, prev, pager, next",total:e.total},on:{"current-change":e.handleCurrentChange}})],1)],1),t("el-dialog",{attrs:{title:"信息",visible:e.fromVisible,width:"40%","close-on-click-modal":!1,"destroy-on-close":""},on:{"update:visible":function(t){e.fromVisible=t}}},[t("el-form",{ref:"formRef",staticStyle:{"padding-right":"50px"},attrs:{"label-width":"100px",model:e.form,rules:e.rules}},[t("el-form-item",{attrs:{prop:"name",label:"科室名称"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),t("el-form-item",{attrs:{prop:"description",label:"科室描述"}},[t("el-input",{attrs:{type:"textarea",rows:5,autocomplete:"off"},model:{value:e.form.description,callback:function(t){e.$set(e.form,"description",t)},expression:"form.description"}})],1)],1),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.fromVisible=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.save}},[e._v("确 定")])],1)],1)],1)},s=[],l={name:"Department",data(){return{tableData:[],pageNum:1,pageSize:10,total:0,name:null,fromVisible:!1,form:{},user:JSON.parse(localStorage.getItem("xm-user")||"{}"),rules:{name:[{required:!0,message:"请输入科室名称",trigger:"blur"}]},ids:[]}},created(){this.load(1)},methods:{handleAdd(){this.form={},this.fromVisible=!0},handleEdit(e){this.form=JSON.parse(JSON.stringify(e)),this.fromVisible=!0},save(){this.$refs.formRef.validate((e=>{e&&this.$request({url:this.form.id?"/department/update":"/department/add",method:this.form.id?"PUT":"POST",data:this.form}).then((e=>{"200"===e.code?(this.$message.success("保存成功"),this.load(1),this.fromVisible=!1):this.$message.error(e.msg)}))}))},del(e){this.$confirm("您确定删除吗？","确认删除",{type:"warning"}).then((t=>{this.$request.delete("/department/delete/"+e).then((e=>{"200"===e.code?(this.$message.success("操作成功"),this.load(1)):this.$message.error(e.msg)}))})).catch((()=>{}))},handleSelectionChange(e){this.ids=e.map((e=>e.id))},delBatch(){this.ids.length?this.$confirm("您确定批量删除这些数据吗？","确认删除",{type:"warning"}).then((e=>{this.$request.delete("/department/delete/batch",{data:this.ids}).then((e=>{"200"===e.code?(this.$message.success("操作成功"),this.load(1)):this.$message.error(e.msg)}))})).catch((()=>{})):this.$message.warning("请选择数据")},load(e){e&&(this.pageNum=e),this.$request.get("/department/selectPage",{params:{pageNum:this.pageNum,pageSize:this.pageSize,name:this.name}}).then((e=>{this.tableData=e.data?.list,this.total=e.data?.total}))},reset(){this.name=null,this.load(1)},handleCurrentChange(e){this.load(e)}}},r=l,n=a(3736),o=(0,n.Z)(r,i,s,!1,null,"052070c0",null),c=o.exports}}]);
//# sourceMappingURL=761.19d1835a.js.map