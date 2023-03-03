<template>
  <div style="width: 100%" align="center">
    <div style="width: 1401px">
      <el-form :inline="true" :model="search" class="demo-form-inline" ref="search" style="float: left">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="search.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="search.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="是否可用" prop="enable">
          <el-select v-model="search.enable" placeholder="是否可用">
            <el-option label="是" value="true" key="是"></el-option>
            <el-option label="否" value="false" key="否"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="searchAccount">搜索</el-button>
          <el-button type="primary" style="width: 89px" @click="resetForm('search')">重置</el-button>
        </el-form-item>
      </el-form>

      <el-button type="primary" style="width: 110px;margin-left: -40px" @click="showModifyRole">更改角色</el-button>

      <div class="clear"></div>
      <el-table :data="accounts" border style="width: 1101px" :key="tableKey" >
        <el-table-column align="center" sortable  prop="aid" label="用户编号" width="150px"></el-table-column> <!-- prop与下面的集合里的数据对应起来 -->
        <el-table-column align="center" sortable  prop="username" label="用户名" width="150px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.username" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.username}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="email" label="邮箱" width="200px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.email" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.email}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="tel" label="电话" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.tel" placeholder="请输入内容"></el-input></span>
            <span v-else>{{scope.row.tel}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="integral" label="积分" width="120px"></el-table-column>
        <el-table-column align="center" sortable  prop="role" label="角色" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.isEdit"><el-input size="small" v-model="scope.row.role" placeholder="请输入内容"></el-input></span>
            <span v-else v-for="(item, index) in scope.row.role">{{item}}<br v-if="index !== scope.row.role.length-1"></span>
          </template>
        </el-table-column>
        <el-table-column align="center" sortable  prop="enable" label="是否可用" width="120px">
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.enable"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="true"
                inactive-value="false"
                :disabled="!scope.row.isEdit">
            </el-switch>
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

    <el-dialog title="更改角色" :visible.sync="modifyRoleVisible" width="690px" v-if="modifyRoleVisible" style="text-align: left">
      <el-form ref="modifyRole" :model="modifyRole" :rules="modifyRole_rules" >
        <el-form-item label="用户名：" prop="username" label-width="120px" >
          <el-select v-model="modifyRole.username" placeholder="商品类型" @change="initRoleList()">
            <el-option v-for="item in accounts"  :label="item.username" :value="item.username" :key="item.username"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <el-transfer
          v-model="value"
          :data="data"
          :button-texts="['移除', '添加']"
          :titles="['未赋予角色','赋予的角色']"
          v-if="modifyRole.username !== ''"
      ></el-transfer>

      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyRoleVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateRole('modifyRole')" >确 认</el-button>
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
    searchAccount() {
      const _this = this;
      axios.get('http://localhost:8181/api/account/getaccount/1', {params: this.search}).then(function (resp) {
        if (resp.data.code === 200) {
          _this.accounts = resp.data.data.content
          _this.total = resp.data.data.totalElements
          _this.currentPage = 1
          _this.accounts.forEach(e => {
            axios.get('http://localhost:8181/api/account/getrole/'+e.username).then(function (resp){
              _this.$set(e, 'role' , resp.data.data)
            })
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
      axios.get('http://localhost:8181/api/account/getaccount/'+currentpage,{params: this.search}).then(function (resp) {
        _this.accounts = resp.data.data.content
        _this.accounts.forEach(e => {
          axios.get('http://localhost:8181/api/account/getrole/'+e.username).then(function (resp){
            _this.$set(e, 'role' , resp.data.data)
          })
          _this.$set(e, 'isEdit', false)
        })
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
        axios.get('http://localhost:8181/api/account/deleteaccount/'+row.aid).then(function (resp) {
          if (resp.data.code === 200) {
            _this.$message.success(resp.data.reason)
            if (_this.lastElement)
              if (_this.currentPage !== 1)
                _this.currentPage -= 1
            axios.get('http://localhost:8181/api/account/getaccount/'+_this.currentPage,{params: _this.search}).then(function (resp) {
              if (resp.data.code === 200) {
                _this.accounts = resp.data.data.content
                _this.total = resp.data.data.totalElements
                _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
              }else {
                _this.search.username = ''
                _this.search.email = ''
                _this.search.enable = ''
                axios.get('http://localhost:8181/api/account/getaccount/1',{params: _this.search}).then(function (resp) {
                  if (resp.data.code === 200) {
                    _this.accounts = resp.data.data.content
                    _this.total = resp.data.data.totalElements
                    _this.currentPage = 1
                    _this.pageKey = moment().toString() + Math.random(),
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
      for (let i of this.accounts) {
        if (i.isEdit && i.aid !== row.aid)
          return this.$message.warning("请先保存当前编辑项");
      }

      // 表格数据都是前端处理，需要把旧值存起来，用户点击修改之后修改了原来的数据，但是又点了取消的情况，还需要获取到原来的值
      sessionStorage.setItem('oldUsername', row.username)
      sessionStorage.setItem('oldEmail', row.email)
      sessionStorage.setItem('oldTel', row.tel)
      sessionStorage.setItem('oldEnable', row.enable)
      this.$set(this.accounts[index], 'isEdit', true)
    },
    //保存
    save(row, index) {
      const _this = this
      axios.post('http://localhost:8181/api/account/saveaccount', row).then(function (resp) {
        _this.$message.success(resp.data.reason)
      })
      this.$set(this.accounts[index], 'isEdit', false)
    },
    // 取消商品属性编辑
    cancel(row, index) {
      this.$set(this.accounts[index], 'username',sessionStorage.getItem('oldUsername'))
      this.$set(this.accounts[index], 'email',sessionStorage.getItem('oldEmail'))
      this.$set(this.accounts[index], 'tel',sessionStorage.getItem('oldTel'))
      this.$set(this.accounts[index], 'enable',sessionStorage.getItem('oldEnable'))
      this.$set(this.accounts[index], 'isEdit', false)
    },
    showModifyRole(){
      this.modifyRole.username = ''
      this.modifyRoleVisible = true
    },
    initRoleList(){
      const _this = this
      this.value = []
      axios.get('http://localhost:8181/api/account/getrole/'+_this.modifyRole.username).then(function (resp){
        resp.data.data.forEach(e => {
          _this.value.push(e)
        })
      })
    },
    updateRole(formName){
      const _this = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('http://localhost:8181/api/account/updaterole/'+_this.modifyRole.username,_this.value).then(function (resp){
            if (resp.data.code === 200) {
              _this.$message.success(resp.data.reason)
              _this.modifyRoleVisible = false

              axios.get('http://localhost:8181/api/account/getaccount/1', {params: _this.search}).then(function (resp) {
                _this.accounts = resp.data.data.content
                _this.total = resp.data.data.totalElements
                _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
                _this.currentPage = 1
                _this.accounts.forEach(e => {
                  axios.get('http://localhost:8181/api/account/getrole/'+e.username).then(function (resp){
                    _this.$set(e, 'role' , resp.data.data)
                  })
                  _this.$set(e, 'isEdit', false)
                })
              })
            }
          })
        } else {
          return false;
        }
      });
    }



  },

  data() {
    return {
      data: [],
      value: [],
      total:null,
      modifyRoleVisible:false,
      accounts: null,
      modifyRole:{
        username:''
      },
      currentPage:null,
      lastElement:false,
      tableKey:moment().toString() + Math.random(),
      pageKey:moment().toString() + Math.random(),
      search: {
        username: '',
        email: '',
        enable: '',
      },
      modifyRole_rules:{
        username:[
          { required: true, message: '请输入选择用户名', trigger: 'blur' }
        ]
      }
    }
  },

  created() {
    const _this = this;
    axios.get('http://localhost:8181/api/account/getaccount/1', {params: this.search}).then(function (resp) {
      _this.accounts = resp.data.data.content
      _this.total = resp.data.data.totalElements
      _this.lastElement = (resp.data.data.last && resp.data.data.numberOfElements === 1)
      _this.currentPage = 1
      _this.accounts.forEach(e => {
        axios.get('http://localhost:8181/api/account/getrole/'+e.username).then(function (resp){
          _this.$set(e, 'role' , resp.data.data)
        })
        _this.$set(e, 'isEdit', false)
      })
    })

    axios.get('http://localhost:8181/api/role/getrolelist').then(function (resp){
      resp.data.data.forEach(role => {
        _this.data.push({
          label: role,
          key: role
        })
      })
    })


  },
}
</script>

<style scoped>
</style>
