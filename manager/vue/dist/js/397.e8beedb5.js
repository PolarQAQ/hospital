"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[397],{7397:function(t,e,a){a.r(e),a.d(e,{default:function(){return d}});var i=function(){var t=this,e=t._self._c;return e("div",[e("div",{staticClass:"search"},[e("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"请选择科室"},model:{value:t.departmentId,callback:function(e){t.departmentId=e},expression:"departmentId"}},t._l(t.departmentData,(function(t){return e("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})})),1),e("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"info",plain:""},on:{click:function(e){return t.load(1)}}},[t._v("查询")]),e("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"warning",plain:""},on:{click:t.reset}},[t._v("重置")])],1),e("div",{staticClass:"table",staticStyle:{padding:"15px 20px"}},[e("el-row",{attrs:{gutter:20}},t._l(t.tableData,(function(a){return e("el-col",{staticStyle:{"margin-bottom":"20px"},attrs:{span:6}},[e("div",{staticClass:"card",staticStyle:{"text-align":"center","background-color":"#ecf8fd"}},[e("img",{staticStyle:{width:"100px",height:"100px","border-radius":"50%"},attrs:{src:a.avatar,alt:""}}),e("div",{staticStyle:{"font-weight":"550","margin-top":"10px"}},[t._v(" "+t._s(a.name)+" "),e("span",{staticStyle:{color:"#383535","margin-left":"5px","font-weight":"500"}},[t._v(t._s(a.departmentName))])]),e("div",{staticStyle:{"margin-top":"20px",color:"#353523",padding:"0 10px","text-align":"left",overflow:"hidden","text-overflow":"ellipsis",display:"-webkit-box","-webkit-box-orient":"vertical","-webkit-line-clamp":"4"}},[t._v(" 简介："+t._s(a.description)+" ")]),e("div",{staticStyle:{"margin-top":"15px"}},[t._v(" 挂号费："),e("span",{staticStyle:{color:"red","font-weight":"550","margin-right":"20px"}},[t._v("￥"+t._s(a.price))]),t._v(" 剩余："+t._s(a.num)+" ")]),e("div",{staticStyle:{"margin-top":"15px"}},[e("el-button",{attrs:{type:"primary",size:"mini"},on:{click:function(e){return t.reserve(a.id)}}},[t._v("挂号")])],1)])])})),1),e("div",{staticClass:"pagination"},[e("el-pagination",{attrs:{background:"","current-page":t.pageNum,"page-sizes":[5,10,20],"page-size":t.pageSize,layout:"total, prev, pager, next",total:t.total},on:{"current-change":t.handleCurrentChange}})],1)],1)])},r=[],s={name:"Doctor",data(){return{tableData:[],pageNum:1,pageSize:10,total:0,departmentId:null,fromVisible:!1,form:{},user:JSON.parse(localStorage.getItem("xm-user")||"{}"),rules:{},ids:[],departmentData:[]}},created(){this.load(1),this.loadDepartment()},methods:{reserve(t){if("USER"!==this.user.role)return void this.$message.warning("您的角色不支持挂号操作");let e={userId:this.user.id,doctorId:t};this.$request.post("/reserve/add",e).then((t=>{"200"===t.code?(this.$message.success("挂号成功"),this.load(1)):this.$message.error(t.msg)}))},loadDepartment(){this.$request.get("/department/selectAll").then((t=>{"200"===t.code?this.departmentData=t.data:this.$message.error(t.msg)}))},load(t){t&&(this.pageNum=t),this.$request.get("/doctor/selectPage2",{params:{pageNum:this.pageNum,pageSize:this.pageSize,departmentId:this.departmentId}}).then((t=>{this.tableData=t.data?.list,this.total=t.data?.total}))},reset(){this.departmentId=null,this.load(1)},handleCurrentChange(t){this.load(t)}}},n=s,l=a(3736),o=(0,l.Z)(n,i,r,!1,null,null,null),d=o.exports}}]);
//# sourceMappingURL=397.e8beedb5.js.map