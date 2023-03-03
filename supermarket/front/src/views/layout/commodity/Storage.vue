<template>
  <div style="width: 100%" align="center">
    <div style="width: 1401px">
      <el-form :inline="true" :model="input" :rules="input_rules" class="demo-form-inline" ref="input" style="float: left">
        <el-form-item label="商品编号" prop="cid">
          <el-input v-model="input.cid" placeholder="商品编号"></el-input>
        </el-form-item>
        <el-form-item label="生产批号" prop="batchNumber">
          <el-input v-model="input.batchNumber" placeholder="生产批号"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="number">
          <el-input v-model="input.number" placeholder="数量" type="number" min="0"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="addCommodity('input')">添加</el-button>
          <el-button type="primary" style="width: 89px" @click="resetForm('input')">重置</el-button>
        </el-form-item>
      </el-form>

      <div style="width: 1021px">
        <el-table :data="buys" border style="width: 1021px" show-summary :summary-method="getSummaries" >
          <el-table-column align="center" sortable  prop="cid" label="商品编码" width="150px"></el-table-column> <!-- prop与下面的集合里的数据对应起来 -->
          <el-table-column align="center" sortable  prop="batchNumber" label="生产批号" width="150px"></el-table-column>
          <el-table-column align="center" sortable  prop="dateProduction" label="生产日期" width="120px"></el-table-column>
          <el-table-column align="center" sortable  prop="expirationDate" label="保质期" width="120px"></el-table-column>
          <el-table-column align="center" sortable  prop="number" label="数量（个）" width="120px">
            <template slot-scope="scope">
              <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.number" placeholder="请输入内容"></el-input></span>
              <span v-else>{{scope.row.number}}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" sortable  prop="buyingPrice" label="进价（元）" width="120px"></el-table-column>
          <el-table-column align="center" sortable  prop="sumPrice" label="金额（元）" width="120px"></el-table-column>
          <el-table-column align="center" label="操作" width="120px">
            <template slot-scope="scope">
            <span v-if="!scope.row.isEdit">
              <el-button type="primary" icon="el-icon-edit" size="small" @click="edit(scope.row,scope.$index)" ></el-button>
              <el-button type="primary" icon="el-icon-delete"  size="small" @click="Delete(scope.row,scope.$index)" ></el-button>
            </span>
              <span v-else>
              <el-button type="primary" icon="el-icon-circle-check" size="small" @click="save(scope.row,scope.$index)" ></el-button>
              <el-button type="primary" icon="el-icon-circle-close"  size="small" @click="cancel(scope.row,scope.$index)" ></el-button>
            </span>
            </template>
          </el-table-column>

        </el-table>

        <el-button type="primary" @click="createBuyCommodityList('cash')" style="float: right;margin-top: 10px;margin-right: 20px">结算</el-button>
        <el-form :inline="true" :model="cash" :rules="pid_rules" class="demo-form-inline" ref="cash" style="float: right;margin-top: 10px">
          <el-form-item label="供应商编号" prop="pid">
            <el-input v-model="cash.pid" placeholder="供应商编号"></el-input>
          </el-form-item>
        </el-form>

      </div>


    </div>

    <el-dialog title="创建商品信息" :visible.sync="dialogFormVisible" width="450px" v-if="dialogFormVisible">
      <el-form ref="createCommodity" :model="createCommodity" :rules="create_rules" >
        <el-form-item label="商品编号：" prop="cid" :label-width="formLabelWidth" >
          <el-input v-model="createCommodity.cid" placeholder="商品编号" autocomplete="off" class="create_input" readonly ></el-input>
        </el-form-item>
        <el-form-item label="商品名称：" :label-width="formLabelWidth" prop="name">
          <el-input v-model="createCommodity.name" placeholder="商品名称" autocomplete="off" class="create_input"></el-input>
        </el-form-item>
        <el-form-item label="商品品牌：" :label-width="formLabelWidth" prop="brand">
          <el-input v-model="createCommodity.brand" placeholder="商品品牌" autocomplete="off" class="create_input" ></el-input>
        </el-form-item>
        <el-form-item label="生产批号：" :label-width="formLabelWidth" prop="batchNumber">
          <el-input v-model="createCommodity.batchNumber" placeholder="生产批号" autocomplete="off" class="create_input" readonly></el-input>
        </el-form-item>
        <el-form-item label="生产日期：" :label-width="formLabelWidth" prop="dateProduction" v-if="createCommodity.batchNumber!=='00000000'" >
          <el-input v-model="createCommodity.dateProduction" placeholder="生产日期" autocomplete="off" type="date" class="create_input" readonly></el-input>
        </el-form-item>
        <el-form-item label="保质期：" :label-width="formLabelWidth" prop="expirationDate" v-if="createCommodity.batchNumber!=='00000000'">
          <el-input  v-model="createCommodity.expirationDate" placeholder="保质期" autocomplete="off" class="create_input"></el-input>
        </el-form-item>
        <el-form-item label="商品进价：" :label-width="formLabelWidth" prop="buyingPrice">
          <el-input  v-model="createCommodity.buyingPrice" placeholder="商品进价"  autocomplete="off" class="create_input"></el-input>
        </el-form-item>
        <el-form-item label="售卖单价：" :label-width="formLabelWidth" prop="price">
          <el-input  v-model="createCommodity.price" placeholder="售卖单价" autocomplete="off" class="create_input"></el-input>
        </el-form-item>
        <el-form-item label="商品类型：" :label-width="formLabelWidth" prop="type">
          <el-input  v-model="createCommodity.type" placeholder="商品类型" autocomplete="off" class="create_input "></el-input>
        </el-form-item>
        <el-form-item label="所在位置：" :label-width="formLabelWidth" prop="location">
          <el-input  v-model="createCommodity.location" placeholder="所在位置" autocomplete="off" class="create_input"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="commodityCreate('createCommodity')" >创 建</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import axios from "axios";
import moment from "moment";

export default {
  methods: {
    initCreateCommodity(){
      this.createCommodity.cid = this.input.cid
      this.createCommodity.batchNumber = this.input.batchNumber
      this.createCommodity.name = ''
      this.createCommodity.brand = ''
      this.createCommodity.dateProduction = this.createCommodity.batchNumber==="00000000"?null:moment(this.createCommodity.batchNumber, "YYYYMMDD").format("YYYY-MM-DD")
      this.createCommodity.expirationDate = ''
      this.createCommodity.buyingPrice = ''
      this.createCommodity.price = ''
      this.createCommodity.type = ''
      this.createCommodity.location = ''
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    addCommodity(formName) {
      const _this = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          for (let i = 0;i < _this.buys.length;i++){
            if (_this.input.cid === _this.buys[i].cid && _this.input.batchNumber === _this.buys[i].batchNumber) {
              _this.buys[i].number += Number(_this.input.number)
              _this.buys[i].sumPrice = Number(_this.buys[i].number) * Number(_this.buys[i].buyingPrice)
              return
            }
          }
          // _this.sales.unshift();
          axios.get('http://localhost:8181/api/commodity/getcommoditybycidandbatchnumber2', {params: this.input}).then(function (resp) {
            if (resp.data.code === 200) {
              _this.$set(resp.data.data, 'isEdit', false)
              _this.buys.unshift(resp.data.data)
              _this.resetForm('input')
            }
            else {
              _this.$confirm(resp.data.reason+'是否创建商品信息？', '提示', {
                confirmButtonText: '创建',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {//确定
                _this.initCreateCommodity()
                _this.dialogFormVisible = true
              }).catch(() => {//取消
              });
            }
          })
        } else {
          return false;
        }
      });
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        if(column.property ==='sumPrice'){
          sums[index]= values.reduce((prev, curr)=>{
            const value =Number(curr);
            if(!isNaN(value)){
              return prev + curr;
            }else{
              return prev;
            }
          },0);
          sums[index] += ' 元';
        }
      });

      return sums;
    },
    Delete(row, index) {
      this.$confirm('是否移除此商品?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定
        this.buys.splice(index,1)

      }).catch(() => {//取消
      });
    },
    //编辑
    edit(row, index) {
      for (let i of this.buys) {
        if (i.isEdit && (i.cid !== row.cid || i.batchNumber !== row.batchNumber))
          return this.$message.warning("请先保存当前编辑项");
      }

      // 表格数据都是前端处理，需要把旧值存起来，用户点击修改之后修改了原来的数据，但是又点了取消的情况，还需要获取到原来的值
      sessionStorage.setItem('oldNumber', row.number)
      sessionStorage.setItem('oldBuyingPrice', row.buyingPrice)
      this.$set(this.buys[index], 'isEdit', true)
    },
    //保存
    save(row, index) {
      this.buys[index].sumPrice = this.buys[index].number * this.buys[index].buyingPrice
      this.$set(this.buys[index], 'isEdit', false)
    },
    // 取消商品属性编辑
    cancel(row, index) {
      this.$set(this.buys[index], 'number',sessionStorage.getItem('oldNumber'))
      this.$set(this.buys[index], 'buyingPrice',sessionStorage.getItem('oldBuyingPrice'))
      this.$set(this.buys[index], 'isEdit', false)
    },
    commodityCreate(formName){
      const _this = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('http://localhost:8181/api/commodity/savecommodity',this.createCommodity).then(function(resp){
            if (resp.data.code === 200){
              _this.$message.success(resp.data.reason)
              _this.dialogFormVisible = false
              _this.addCommodity('input')
            }else {
              _this.$message.error(resp.data.reason);
            }
          })
        } else {
          return false;
        }
      });
    },
    createBuyCommodityList(formName){
      const _this = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          _this.$confirm('是否结算?', '提示', {
            confirmButtonText: '是',
            cancelButtonText: '否',
            type: 'warning'
          }).then(() => {//确定
            axios.post('http://localhost:8181/api/buycommodity/createbuycommodity/'+_this.cash.pid, this.buys).then(function (resp) {
              _this.$message.success(resp.data.reason)
              _this.buys=[]
              _this.resetForm('input')
              _this.resetForm('cash')
            })
          }).catch(() => {//取消
          });
        } else {
          return false;
        }
      });
    },

  },

  data() {
    var pidVerify = (rule, value, callback) => {
      const _this = this
      axios.get('http://localhost:8181/api/provider/pidexist/'+this.cash.pid).then(function (resp) {
        if (resp.data.code !== 200)
          callback(new Error(resp.data.reason));
        else {
          callback();
        }
      })
    };

    return {
      dialogFormVisible:false,
      formLabelWidth: '120px',
      createCommodity:{
        cid: '',
        name: '',
        brand: '',
        batchNumber: '',
        dateProduction: '',
        expirationDate: '',
        buyingPrice:'',
        price:'',
        type:'',
        location:''
      },
      buys: [],
      input: {
        cid: '',
        batchNumber: '',
        number :''
      },
      cash:{
        pid:''
      },
      pid_rules: {
        pid: [
          { required: true, message: "请输入供应商编号", trigger: "blur" },
          { validator: pidVerify, trigger: 'blur' }
        ]
      },
      input_rules: {
        cid: [
          { required: true, message: '请输入商品编号', trigger: 'blur' },
          { min: 13, max: 13, message: '长度为 13 个字符', trigger: 'blur' }
        ],
        batchNumber:[
          { required: true, message: '请输入生产批号,例:20220802', trigger: 'blur' },
          { pattern:/(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)|[0]{8}/, message: "格式为年月日，例：20220802", trigger: "blur" }
        ],
        number: [
          { required: true, message: '请输入数量', trigger: 'blur' },
          { pattern:/^[1-9]\d*$/, message: "数量只能为正整数", trigger: "blur" }
        ]
      },
      create_rules:{
        cid: [
          { required: true, message: '请输入商品编号', trigger: 'blur' },
          { min: 13, max: 13, message: '长度为 13 个字符', trigger: 'blur' }
        ],
        batchNumber:[
          { required: true, message: '请输入生产批号,例:20220802', trigger: 'blur' },
          { pattern:/(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)|[0]{8}/, message: "格式为年月日，例：20220802", trigger: "blur" }
        ],
        dateProduction:[
          { required: true, message: '请输入生产日期', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入商品名称', trigger: 'blur' },
          { max:20, message: '长度不能超过20个字符', trigger: 'blur' }
        ],
        brand: [
          { required: true, message: '请输入商品品牌', trigger: 'blur' },
          { max:20, message: '长度不能超过20个字符', trigger: 'blur' }
        ],
        expirationDate: [
          { required: true, message: '请输入保质期', trigger: 'blur' },
          { pattern:/^[1-9]\d*$/, message: '只能为正整数', trigger: 'blur' }
        ],
        buyingPrice:[
          { required: true, message: '请输入商品进价', trigger: 'blur' },
          { pattern:/(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,2})?$)/, message: "只能为大于0的数字", trigger: "blur" }
        ],
        price:[
          { required: true, message: '请输商品单价', trigger: 'blur' },
          { pattern:/(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,2})?$)/, message: "只能为大于0的数字", trigger: "blur" }
        ],
        type:[
          { required: true, message: '请输入商品类型', trigger: 'blur' },
          { max:10, message: '长度不能超过10个字符', trigger: 'blur' }
        ],
        location:[
          { required: true, message: '请输入所在位置', trigger: 'blur' },
          { pattern:/^[A-Z][-][1-9][0-9]*[-][1-9][0-9]*/, message: "格式错误，例：A-1-1", trigger: "blur" }
        ]
      }
    }
  },

  created() {
    const _this = this;
    this.buys = [{cid:'6928804011142',batchNumber:'20220202',dateProduction:'2022-02-02',expirationDate:12,number:10,buyingPrice:1.7,sumPrice:17},
      {cid:'6935498308793',batchNumber: '20210726',dateProduction:'2021-07-26',expirationDate:12,number: 10,buyingPrice:4.3,sumPrice:43}]
  }
}
</script>

<style scoped>
.create_input{
  width: 240px;
  float: left;
}
</style>
