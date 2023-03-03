<template>
  <div style="width: 100%" align="center">
    <div style="width: 1401px">
      <el-form :inline="true" :model="search" class="demo-form-inline" ref="search" style="float: left">
        <el-form-item label="供应商名称" prop="name">
          <el-input v-model="search.name" placeholder="供应商名称"></el-input>
        </el-form-item>
        <el-form-item label="供应商地址" prop="address">
          <el-input v-model="search.address" placeholder="供应商地址"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="searchProvider">搜索</el-button>
          <el-button type="primary" style="width: 89px" @click="resetForm('search')">重置</el-button>
        </el-form-item>
      </el-form>
      <el-button type="primary" style="width: 110px;margin-left: 145px" @click="showCreateProvider">添加供应商</el-button>


      <el-table :data="providers" border style="width: 1061px" :key="tableKey" >
        <el-table-column align="center" sortable  prop="pid" label="供应商编号" width="150px"></el-table-column> <!-- prop与下面的集合里的数据对应起来 -->
        <el-table-column align="center" sortable  prop="name" label="供应商名称" width="150px"></el-table-column>
        <el-table-column align="center" sortable  prop="address" label="供应商地址" width="400px"></el-table-column>
        <el-table-column align="center" sortable  prop="headName" label="负责人姓名" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.headName" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.headName}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="tel" label="联系电话" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.tel" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.tel}}</span>
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

    <el-dialog title="添加供应商" :visible.sync="dialogFormVisible" width="450px" v-if="dialogFormVisible">
      <el-form ref="createProvider" :model="createProvider" :rules="createProvider_rules" >
        <el-form-item label="供应商名称：" prop="name" :label-width="formLabelWidth" >
          <el-input v-model="createProvider.name" autocomplete="off" class="createProvider_input"></el-input>
        </el-form-item>
        <el-form-item label="地址：" :label-width="formLabelWidth" prop="address">
          <el-input v-model="createProvider.address" autocomplete="off" class="createProvider_input" ></el-input>
        </el-form-item>
        <el-form-item label="负责人姓名：" :label-width="formLabelWidth" prop="headName">
          <el-input v-model="createProvider.headName" autocomplete="off" class="createProvider_input" ></el-input>
        </el-form-item>
        <el-form-item label="负责人电话：" :label-width="formLabelWidth" prop="tel">
          <el-input v-model="createProvider.tel" autocomplete="off" class="createProvider_input"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addProvider('createProvider')">添 加</el-button>
      </div>
    </el-dialog>

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
    searchProvider() {
      const _this = this;
      axios.get('http://localhost:8181/api/provider/getprovider/1', {params: this.search}).then(function (resp) {
        if (resp.data.code === 200) {
          _this.providers = resp.data.data.content
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
      axios.get('http://localhost:8181/api/provider/getprovider/'+currentpage,{params: this.search}).then(function (resp) {
        _this.providers = resp.data.data.content
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
        axios.get('http://localhost:8181/api/provider/deleteprovider', {params: row}).then(function (resp) {
          if (resp.data.code === 200) {
            _this.$message.success(resp.data.reason)
            if (_this.lastElement)
              if (_this.currentPage !== 1)
                _this.currentPage -= 1
            axios.get('http://localhost:8181/api/provider/getprovider/'+_this.currentPage,{params: _this.search}).then(function (resp) {
              if (resp.data.code === 200) {
                _this.providers = resp.data.data.content
                _this.total = resp.data.data.totalElements
                _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
              }else {
                _this.search.name = ''
                _this.search.type = ''
                axios.get('http://localhost:8181/api/provider/getprovider/1',{params: _this.search}).then(function (resp) {
                  if (resp.data.code === 200) {
                    _this.providers = resp.data.data.content
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
      for (let i of this.providers) {
        if (i.isEdit && i.pid !== row.pid)
          return this.$message.warning("请先保存当前编辑项");
      }

      // 表格数据都是前端处理，需要把旧值存起来，用户点击修改之后修改了原来的数据，但是又点了取消的情况，还需要获取到原来的值
      sessionStorage.setItem('oldHeadName', row.headName)
      sessionStorage.setItem('oldTel', row.tel)
      this.$set(this.providers[index], 'isEdit', true)
    },
    //保存
    save(row, index) {
      const _this = this
      axios.post('http://localhost:8181/api/provider/saveprovider', row).then(function (resp) {
        _this.$message.success(resp.data.reason)
      })
      this.$set(this.providers[index], 'isEdit', false)
    },
    // 取消商品属性编辑
    cancel(row, index) {
      this.$set(this.providers[index], 'headName',sessionStorage.getItem('oldHeadName'))
      this.$set(this.providers[index], 'tel',sessionStorage.getItem('oldTel'))
      this.$set(this.providers[index], 'isEdit', false)
    },
    showCreateProvider(){
      this.createProvider.name = ''
      this.createProvider.address = ''
      this.createProvider.headName = ''
      this.createProvider.tel = ''
      this.dialogFormVisible = true
    },
    addProvider(formName){
      const _this = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('http://localhost:8181/api/provider/saveprovider', this.createProvider).then(function (resp) {
            if (resp.status === 200) {
              _this.$message.success(resp.data.reason)
              _this.dialogFormVisible = false
              _this.resetForm('search')
              axios.get('http://localhost:8181/api/provider/getprovider/1', {params: _this.search}).then(function (resp) {
                _this.providers = resp.data.data.content
                _this.total = resp.data.data.totalElements
                _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
                _this.currentPage = 1
                _this.providers.forEach(e => {
                  _this.$set(e, 'isEdit', false)
                })
                _this.tableKey = moment().toString() + Math.random()
              })
            }
            else
              _this.$message.error("添加失败！")
          })
        } else {
          return false;
        }
      });
    },

  },

  data() {
    return {
      total:null,
      dialogFormVisible:false,
      formLabelWidth: '120px',
      createProvider: {
        name: '',
        address: '',
        headName: '',
        tel: ''
      },
      providers: null,
      currentPage:null,
      lastElement:false,
      tableKey:moment().toString() + Math.random(),
      pageKey:moment().toString() + Math.random(),
      search: {
        name: '',
        address: ''
      },
      createProvider_rules: {
        name: [
          { required: true, message: '请输入供应商名称', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        address:[
          { required: true, message: '请输入地址', trigger: 'blur' }
        ],
        headName: [
          { required: true, message: '请再次输入负责人姓名', trigger: 'blur' },
          { min: 2, max: 6, message: '长度在 2 到 6 个字符', trigger: 'blur' }
        ],
        tel: [
          { required: true, message: "请输入负责人手机号", trigger: "blur" },
          // 这个只能验证手机号
          // { pattern:/^0{0,1}(13[0-9]|15[7-9]|153|156|18[7-9])[0-9]{8}$/, message: "请输入合法手机号", trigger: "blur" }
          { pattern:/^((0\d{2,3}-\d{7,8})|(1[35847]\d{9}))$/, message: "请输入合法手机号/电话号", trigger: "blur" }
        ]
      },
    }
  },

  created() {
    const _this = this;
    axios.get('http://localhost:8181/api/provider/getprovider/1', {params: this.search}).then(function (resp) {
      _this.providers = resp.data.data.content
      _this.total = resp.data.data.totalElements
      _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
      _this.currentPage = 1
      _this.providers.forEach(e => {
        _this.$set(e, 'isEdit', false)
      })
    })
  },
}
</script>

<style scoped>
.createProvider_input{
  width: 240px;
  float: left;
}
</style>
