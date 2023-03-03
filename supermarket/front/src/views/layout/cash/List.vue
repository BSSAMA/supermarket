<template>
  <div style="width: 100%" align="center">
    <div style="width: 1401px">
      <el-form :inline="true" :model="input" :rules="input_rules" class="demo-form-inline" ref="input" style="float: left">
        <el-form-item label="商品编号" prop="cid">
          <el-input v-model="input.cid" placeholder="商品编号" @blur="initBatchNumber()" ></el-input>
        </el-form-item>
        <el-form-item label="生产批号" prop="batchNumber">
          <el-select v-model="input.batchNumber" placeholder="生产批号"  >
            <el-option v-for="(item) in batchNumber"  :label="item" :value="item" :key="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="number">
          <el-input v-model="input.number" placeholder="数量" type="number" min="0"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="addCommodity('input')">添加</el-button>
          <el-button type="primary" style="width: 89px" @click="resetForm('input')">重置</el-button>
        </el-form-item>
      </el-form>




      <div style="width: 781px">
      <el-table :data="sales" border style="width: 781px" show-summary :summary-method="getSummaries">
        <el-table-column align="center" sortable  prop="cid" label="商品编码" width="150px"></el-table-column> <!-- prop与下面的集合里的数据对应起来 -->
        <el-table-column align="center" sortable  prop="batchNumber" label="生产批号" width="150px"></el-table-column>
        <el-table-column align="center" sortable  prop="number" label="数量（个）" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.number" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.number}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="price" label="单价（元）" width="120px"></el-table-column>
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

        <el-button type="primary" @click="createSales('cash')" style="float: right;margin-top: 10px;margin-right: 20px">结算</el-button>
        <el-form :inline="true" :model="cash" :rules="tel_rules" class="demo-form-inline" ref="cash" style="float: right;margin-top: 10px">
          <el-form-item label="会员电话" prop="tel">
            <el-input v-model="cash.tel" placeholder="会员电话"></el-input>
          </el-form-item>
        </el-form>
      </div>


    </div>
  </div>
</template>

<script>
import axios from "axios";
import moment from "moment";

export default {
  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    addCommodity(formName) {
      const _this = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          for (let i = 0;i < _this.sales.length;i++){
            if (_this.input.cid === _this.sales[i].cid && _this.input.batchNumber === _this.sales[i].batchNumber) {
              _this.sales[i].number += Number(_this.input.number)
              _this.sales[i].sumPrice = Number(_this.sales[i].number) * Number(_this.sales[i].price)
              return
            }
          }
          axios.get('http://localhost:8181/api/commodity/getcommoditybycidandbatchnumber', {params: this.input}).then(function (resp) {
            _this.$set(resp.data.data, 'isEdit', false)
            _this.sales.unshift(resp.data.data);
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
        this.sales.splice(index,1)
      }).catch(() => {//取消
      });
    },
    //编辑
    edit(row, index) {
      for (let i of this.sales) {
        if (i.isEdit && i.cid !== row.cid && i.batchNumber !== row.batchNumber)
          return this.$message.warning("请先保存当前编辑项");
      }

      // 表格数据都是前端处理，需要把旧值存起来，用户点击修改之后修改了原来的数据，但是又点了取消的情况，还需要获取到原来的值
      sessionStorage.setItem('oldNumber', row.number)
      this.$set(this.sales[index], 'isEdit', true)
    },
    //保存
    save(row, index) {
      this.sales[index].sumPrice = this.sales[index].number * this.sales[index].price
      this.$set(this.sales[index], 'isEdit', false)
    },
    // 取消商品属性编辑
    cancel(row, index) {
      this.$set(this.sales[index], 'number',sessionStorage.getItem('oldNumber'))
      this.$set(this.sales[index], 'isEdit', false)
    },
    createSales(formName){
      const _this = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          _this.$confirm('用户是否已经付款?', '提示', {
            confirmButtonText: '是',
            cancelButtonText: '否',
            type: 'warning'
          }).then(() => {//确定
            axios.post('http://localhost:8181/api/sales/createsales/'+(_this.cash.tel?_this.cash.tel:'null'), this.sales).then(function (resp) {
              _this.$message.success(resp.data.reason)
              _this.sales=[]
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
    initBatchNumber(){
      if (this.input.cid === sessionStorage.getItem('inputCid'))
        return
      sessionStorage.setItem('inputCid', this.input.cid)
      this.input.batchNumber = null
      this.batchNumber = null
    }


  },

  data() {
    var telVerify = (rule, value, callback) => {
      if (value !== '')
        axios.get('http://localhost:8181/api/account/telexist/'+this.cash.tel).then(function (resp) {
        if (resp.data.code !== 200)
          callback(new Error(resp.data.reason));
        else
          callback()
        })
      else
        callback()
    };
    var cidVerify = (rule, value, callback) => {
      const _this = this
      axios.get('http://localhost:8181/api/commodity/cidexist/'+this.input.cid).then(function (resp) {
        if (resp.data.code !== 200)
          callback(new Error(resp.data.reason));
        else {
          axios.get('http://localhost:8181/api/commodity/getbatchnumberbycid/' + _this.input.cid).then(function (resp) {
            _this.batchNumber = resp.data.data
          })
          callback();
        }
      })
    };
    var numberVerify = (rule, value, callback) => {
      const _this = this
      if (this.input.batchNumber === '' || this.input.batchNumber === null) {
        callback(new Error("请先填写前两项！"))
      }
      else {
        axios.get('http://localhost:8181/api/commodity/getcommoditynumber/' + this.input.cid + '/' + this.input.batchNumber).then(function (resp) {
          if (value > resp.data.data)
            callback(new Error("数量大于库存！库存数量为：" + resp.data.data + "！"))
          else
            callback()
        })
      }
    };

    return {
      sales: [],
      input: {
        cid: '',
        batchNumber: '',
        number :''
      },
      cash:{
        tel:''
      },
      batchNumber: null,
      tel_rules: {
        tel: [
          { required: false, message: "请输入会员手机号", trigger: "blur" },
          // 这个只能验证手机号
          // { pattern:/^0{0,1}(13[0-9]|15[7-9]|153|156|18[7-9])[0-9]{8}$/, message: "请输入合法手机号", trigger: "blur" }
          { pattern:/^((0\d{2,3}-\d{7,8})|(1[35847]\d{9}))$/, message: "请输入合法手机号", trigger: "blur" },
          { validator: telVerify, trigger: 'blur' }
        ]
      },
      input_rules: {
        cid: [
          { required: true, message: '请输入商品编号', trigger: 'blur' },
          { min: 13, max: 13, message: '长度为 13 个字符', trigger: 'blur' },
          { validator: cidVerify, trigger: 'blur' }
        ],
        batchNumber:[
          { required: true, message: '请选择生产批号', trigger: 'blur' }
        ],
        number: [
          { required: true, message: '请输入数量', trigger: 'blur' },
          { pattern:/^[1-9]\d*$/, message: "数量只能为正整数", trigger: "blur" },
          { validator: numberVerify, trigger: 'blur' }
        ]
      },
    }
  },

  created() {
    const _this = this;
    this.sales = [{cid:'6928804011142',batchNumber:'20210820',number:4,price:2,sumPrice:8},
      {cid:'6935498308793',batchNumber: '20210726',number: 2,price:4.5,sumPrice:9}]
  }
}
</script>

<style scoped>

</style>
