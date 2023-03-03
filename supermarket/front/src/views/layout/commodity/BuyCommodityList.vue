<template>
  <div style="width: 100%" align="center">
    <div style="width: 1401px">
      <el-form :inline="true" :model="search" class="demo-form-inline" ref="search" style="float: left">
        <el-form-item label="进货单编号" prop="bid">
          <el-input v-model="search.bid" placeholder="进货单编号"></el-input>
        </el-form-item>
        <el-form-item label="商品编号" prop="cid">
          <el-input v-model="search.cid" placeholder="商品编号"></el-input>
        </el-form-item>
        <el-form-item label="生产批号" prop="batchNumber">
          <el-input v-model="search.batchNumber" placeholder="生产批号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="searchBuycommodity">搜索</el-button>
          <el-button type="primary" style="width: 89px" @click="resetForm('search')">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="clear"></div>
      <el-table :data="buyCommodity" border style="width: 1221px" :key="tableKey">
        <el-table-column align="center" sortable  prop="bid" label="进货单编号" width="150px"></el-table-column> <!-- prop与下面的集合里的数据对应起来 -->
        <el-table-column align="center" sortable  prop="cid" label="商品编号" width="150px"></el-table-column>
        <el-table-column align="center" sortable  prop="batchNumber" label="生产批号" width="200px"></el-table-column>
        <el-table-column align="center" sortable  prop="dateProduction" label="生产日期" width="120px"></el-table-column>
        <el-table-column align="center" sortable  prop="expirationDate" label="保质期" width="120px"></el-table-column>
        <el-table-column align="center" sortable  prop="buyingPrice" label="进价" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.buyingPrice" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.buyingPrice}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="number" label="数量" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.number" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.number}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="sumPrice" label="金额" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.sumPrice" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.sumPrice}}</span>
          </template>
        </el-table-column>
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

      <el-pagination background layout="prev, pager, next" :key="pageKey" :page-size=6 :total="total" @current-change="page"> <!-- page-size 每页数据数量,total数据总数,@current-change点击事件 page方法名 --></el-pagination>
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
    searchBuycommodity() {
      const _this = this;
      axios.get('http://localhost:8181/api/buycommodity/getbuycommodity/1', {params: this.search}).then(function (resp) {
        if (resp.data.code === 200) {
          _this.buyCommodity = resp.data.data.content
          _this.total = resp.data.data.totalElements
          _this.currentPage = 1
          _this.pageKey = moment().toString() + Math.random()
          _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
        }else {
          _this.$message.warning(resp.data.reason)
        }
      })
    },
    page(currentpage){
      const _this = this;
      axios.get('http://localhost:8181/api/buycommodity/getbuycommodity/'+currentpage,{params: this.search}).then(function (resp) {
        _this.buyCommodity = resp.data.data.content
        _this.total = resp.data.data.totalElements
        _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
      })
      this.currentPage = currentpage
    },
    Delete(row, index){
      this.$confirm('您确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定
        const _this = this
        axios.get('http://localhost:8181/api/buycommodity/deletebuycommodity', {params: row}).then(function (resp) {
          if (resp.data.code === 200) {
            _this.$message.success(resp.data.reason)
            if (_this.lastElement)
              if (_this.currentPage !== 1)
                _this.currentPage -= 1
            axios.get('http://localhost:8181/api/buycommodity/getbuycommodity/'+_this.currentPage,{params: _this.search}).then(function (resp) {
              if (resp.data.code === 200) {
                _this.buyCommodity = resp.data.data.content
                _this.total = resp.data.data.totalElements
                _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
              }else {
                _this.search.bid = ''
                _this.search.cid = ''
                _this.search.batchNumber = ''
                axios.get('http://localhost:8181/api/buycommodity/getbuycommodity/1',{params: _this.search}).then(function (resp) {
                  if (resp.data.code === 200) {
                    _this.buyCommodity = resp.data.data.content
                    _this.total = resp.data.data.totalElements
                    _this.currentPage = 1
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
      for (let i of this.buyCommodity) {
        if (i.isEdit && (i.bid !== row.bid || i.cid !== row.cid || i.batchNumber !== row.batchNumber))
          return this.$message.warning("请先保存当前编辑项");
      }

      // 表格数据都是前端处理，需要把旧值存起来，用户点击修改之后修改了原来的数据，但是又点了取消的情况，还需要获取到原来的值
      sessionStorage.setItem('oldBuyingPrice', row.buyingPrice)
      sessionStorage.setItem('oldSumPrice', row.sumPrice)
      sessionStorage.setItem('oldNumber', row.number)
      this.$set(this.buyCommodity[index], 'isEdit', true)
    },
    //保存
    save(row, index) {
      const _this = this
      axios.post('http://localhost:8181/api/buycommodity/savebuycommodity', row).then(function (resp) {
        _this.$message.success(resp.data.reason)
      })
      this.$set(this.buyCommodity[index], 'isEdit', false)
    },
    // 取消商品属性编辑
    cancel(row, index) {
      this.$set(this.buyCommodity[index], 'buyingPrice',sessionStorage.getItem('oldBuyingPrice'))
      this.$set(this.buyCommodity[index], 'sumPrice',sessionStorage.getItem('oldSumPrice'))
      this.$set(this.buyCommodity[index], 'number',sessionStorage.getItem('oldNumber'))
      this.$set(this.buyCommodity[index], 'isEdit', false)
    }

  },

  data() {
    return {
      total:null,
      buyCommodity: null,
      currentPage:null,
      lastElement:false,
      tableKey:moment().toString() + Math.random(),
      pageKey:moment().toString() + Math.random(),
      search: {
        bid: '',
        cid: '',
        batchNumber: ''
      }
    }
  },

  created() {
    const _this = this;
    axios.get('http://localhost:8181/api/buycommodity/getbuycommodity/1', {params: this.search}).then(function (resp) {
      _this.buyCommodity = resp.data.data.content
      _this.total = resp.data.data.totalElements
      _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
      _this.currentPage = 1
      _this.buyCommodity.forEach(e => {
        _this.$set(e, 'isEdit', false)
      })
    })
  },
}
</script>

<style scoped>

</style>
