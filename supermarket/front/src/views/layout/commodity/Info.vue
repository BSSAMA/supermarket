<template>
  <div style="width: 100%" align="center">
    <div style="width: 1401px">
      <el-form :inline="true" :model="search" class="demo-form-inline" ref="search" style="float: left">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="search.name" placeholder="商品名称"></el-input>
        </el-form-item>
        <el-form-item label="商品类型" prop="type">
          <el-select v-model="search.type" placeholder="商品类型">
            <el-option v-for="(item) in type"  :label="item" :value="item" :key="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="searchCommodity">搜索</el-button>
          <el-button type="primary" style="width: 89px" @click="resetForm('search')">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="clear"></div>
      <div :style="tableWidth()"  >
      <el-table :data="commodities" border :row-class-name="tableRowClassName" :key="tableKey" >
        <el-table-column align="center" sortable  prop="cid" label="商品编码" width="123px" key="cid" ></el-table-column> <!-- prop与下面的集合里的数据对应起来 -->
        <el-table-column align="center" sortable  prop="name" label="商品名称" width="110px" key="name" ></el-table-column>
        <el-table-column align="center" sortable  prop="brand" label="商品品牌" width="110px" key="brand"></el-table-column>
        <el-table-column align="center" sortable  prop="batchNumber" label="生产批号" width="110px" key="barchNumber" ></el-table-column>
        <el-table-column align="center" sortable  prop="dateProduction" label="生产日期" width="115px" key="dateProduction" ></el-table-column>
        <el-table-column align="center" sortable  prop="expirationDate" label="保质期(月)" width="115px" key="expirationDate" ></el-table-column>
        <el-table-column align="center" sortable  prop="buyingPrice" label="商品进价(元)" width="125px" key="buyingPrice" v-if="buyingPriceExist()">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.buyingPrice" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.buyingPrice}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="price" label="售卖单价(元)" width="125px" key="price" >
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.price" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.price}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="type" label="商品类型" width="115px" key="type" >
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.type" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.type}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="location" label="所在位置" width="115px" key="location" >
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.location" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.location}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="number" label="商品数量" width="115px" key="number" >
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.number" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.number}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="120px" v-if="buyingPriceExist()" key="operation" >
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
      <el-pagination background layout="prev, pager, next" :key="pageKey" :page-size=6 :total="total" @current-change="page"> <!-- page-size 每页数据数量,total数据总数,@current-change点击事件 page方法名 --></el-pagination>
    </div>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import moment from "moment";

export default {
  methods: {
    tableRowClassName({row, rowIndex}) {
      var DP = moment(row.dateProduction,"YYYY-MM-DD").add(row.expirationDate,'M')
      var now = moment()
      if (moment(now).isAfter(DP)){
        return 'danger';
      }else if (moment(now.add(10, 'd')).isAfter(DP)){
        return 'warning';
      }else return '';
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    searchCommodity() {
      const _this = this;
      axios.get('http://localhost:8181/api/commodity/getcommodity/1', {params: this.search}).then(function (resp) {
        if (resp.data.code === 200) {
          _this.commodities = resp.data.data.content
          _this.total = resp.data.data.totalElements
          _this.currentPage = 1
          _this.commodities.forEach(e => {
            if (e.number === undefined)
              e.number = 0
            _this.$set(e, 'isEdit', false)
          })
          _this.pageKey = moment().toString() + Math.random()
          _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
        }else {
          _this.$message.warning(resp.data.reason)
        }
      })
    },
    page(currentpage){
      const _this = this;
      axios.get('http://localhost:8181/api/commodity/getcommodity/'+currentpage,{params: this.search}).then(function (resp) {
        _this.commodities = resp.data.data.content
        _this.total = resp.data.data.totalElements
        _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
        _this.commodities.forEach(e => {
          if (e.number === undefined)
            e.number = 0
          _this.$set(e, 'isEdit', false)
        })
      })
      this.currentPage = currentpage
    },
    buyingPriceExist(){
      if (this.commodities != null)
        return this.commodities[0].hasOwnProperty("buyingPrice");
    },
    tableWidth(){
      if (!this.buyingPriceExist())
        return 'width:1154px'
      return  'width:1399px'
    },
    Delete(row, index){
      this.$confirm('您确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定
        const _this = this
        axios.get('http://localhost:8181/api/commodity/deletecommodity', {params: row}).then(function (resp) {
          if (resp.data.code === 200) {
            _this.$message.success(resp.data.reason)
            if (_this.lastElement)
              if (_this.currentPage !== 1)
                _this.currentPage -= 1
            axios.get('http://localhost:8181/api/commodity/getcommodity/'+_this.currentPage,{params: _this.search}).then(function (resp) {
              if (resp.data.code === 200) {
                _this.commodities = resp.data.data.content
                _this.total = resp.data.data.totalElements
                _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
                _this.commodities.forEach(e => {
                  if (e.number === undefined)
                    e.number = 0
                  _this.$set(e, 'isEdit', false)
                })
              }else {
                _this.search.name = ''
                _this.search.type = ''
                axios.get('http://localhost:8181/api/commodity/getcommodity/1',{params: _this.search}).then(function (resp) {
                  if (resp.data.code === 200) {
                    _this.commodities = resp.data.data.content
                    _this.total = resp.data.data.totalElements
                    _this.currentPage = 1
                    _this.commodities.forEach(e => {
                      if (e.number === undefined)
                        e.number = 0
                      _this.$set(e, 'isEdit', false)
                    })
                    _this.pageKey = moment().toString() + Math.random()
                    _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
                  }
                })
              }
            })
          }
        })
        this.tableKey = moment().toString() + Math.random()

      }).catch(() => {//取消
      });


    },
    //编辑
    edit(row, index) {
      for (let i of this.commodities) {
        if (i.isEdit && (i.cid !== row.cid || i.batchNumber !== row.batchNumber))
          return this.$message.warning("请先保存当前编辑项");
      }

      // 表格数据都是前端处理，需要把旧值存起来，用户点击修改之后修改了原来的数据，但是又点了取消的情况，还需要获取到原来的值
      sessionStorage.setItem('oldBuyingPrice', row.buyingPrice)
      sessionStorage.setItem('oldPrice', row.price)
      sessionStorage.setItem('oldType', row.type)
      sessionStorage.setItem('oldLocation', row.location)
      sessionStorage.setItem('oldNumber', row.number)
      this.$set(this.commodities[index], 'isEdit', true)
    },
    //保存
    save(row, index) {
      const _this = this
      axios.post('http://localhost:8181/api/commodity/savecommodity', row).then(function (resp) {
        _this.$message.success(resp.data.reason)
      })
      this.$set(this.commodities[index], 'isEdit', false)
    },
    // 取消商品属性编辑
    cancel(row, index) {
      this.$set(this.commodities[index], 'buyingPrice',sessionStorage.getItem('oldBuyingPrice'))
      this.$set(this.commodities[index], 'price',sessionStorage.getItem('oldPrice'))
      this.$set(this.commodities[index], 'type',sessionStorage.getItem('oldType'))
      this.$set(this.commodities[index], 'location',sessionStorage.getItem('oldLocation'))
      this.$set(this.commodities[index], 'number',sessionStorage.getItem('oldNumber'))
      this.$set(this.commodities[index], 'isEdit', false)
    }


  },

  data() {
    return {
      total:null,
      commodities: null,
      currentPage:null,
      lastElement:false,
      tableKey:moment().toString() + Math.random(),
      pageKey:moment().toString() + Math.random(),
      search: {
        name: '',
        type: ''
      },
      type: null
    }
  },

  created() {
    const _this = this;
    axios.get('http://localhost:8181/api/commodity/getcommodity/1', {params: this.search}).then(function (resp) {
      _this.commodities = resp.data.data.content
      _this.total = resp.data.data.totalElements
      _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
      _this.currentPage = 1
      _this.commodities.forEach(e => {
        if (e.number === undefined)
          e.number = 0
        _this.$set(e, 'isEdit', false)
      })
    })



    axios.get('http://localhost:8181/api/commodity/getcommoditytype').then(function (resp) {
      _this.type = resp.data.data
    })
  },
}
</script>

<style scoped>

</style>
