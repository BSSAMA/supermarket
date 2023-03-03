<template>
  <div style="height: 100%" >
    <el-container style="height: 100%; border: 1px solid #eee">
      <el-aside width="200px" height="100%" style="background-color: rgb(238, 241, 246)" class="left">
        <el-menu router >
          <el-submenu v-for="(item,index) in menu" :key="index" :index="index+''" style="text-align: left"> <!-- index取下标 -->
            <template slot="title"><i :class="item.iconCls"></i>{{item.nameZh}}</template>
            <el-menu-item v-for="(item2) in item.children" :key="item2.path" :index="item2.path" :class="$route.path===item2.path?'is-active':''"><i :class="item2.icon"></i>{{item2.nameZh}}</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 14px" class="el-header">
          <h1 style="float: left;line-height: 60px;text-align: center;font-size: 2em">小型超市管理系统</h1>
          <div id="username">
            <el-dropdown>
              <i class="el-icon-setting" style="margin-right: 15px"></i>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="showMyInfo">我的信息</el-dropdown-item>
                <el-dropdown-item @click.native="showModifyPassword">修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="logout">注销</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <span>{{username}}</span>
          </div>
        </el-header>

        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>

    <el-dialog title="修改密码" :visible.sync="modifyPasswordVisible" width="450px" v-if="modifyPasswordVisible">
      <el-form ref="modifyPassword" :model="modifyPassword" :rules="modifyPassword_rules" >
        <el-form-item label="旧密码：" label-width="120px" prop="oldPassword">
          <el-input v-model="modifyPassword.oldPassword" autocomplete="off" class="register_input" type="password"></el-input>
        </el-form-item>
        <el-form-item label="新密码：" label-width="120px" prop="newPassword">
          <el-input v-model="modifyPassword.newPassword" autocomplete="off" class="register_input" type="password"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyPasswordVisible = false">取 消</el-button>
        <el-button type="primary" @click="updatePassword('modifyPassword')" >修 改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="我的信息" :visible.sync="myInfoVisible" width="450px" v-if="myInfoVisible">
      <el-form ref="myInfo" :model="myInfo" :rules="myInfo_rules" >
        <el-form-item label="用户编号：" label-width="120px" prop="aid" style="text-align: left">
          <template>
            <span>{{myInfo.aid}}</span>
          </template>
        </el-form-item>
        <el-form-item label="用户名：" label-width="120px" prop="username" style="text-align: left">
          <template>
            <span>{{myInfo.username}}</span>
          </template>
        </el-form-item>
        <el-form-item label="邮箱：" label-width="120px" prop="email" style="text-align: left">
          <template>
            <span v-if="myInfo.isEdit"><el-input v-model="myInfo.email" autocomplete="off"></el-input></span>
            <span v-else>{{myInfo.email}}</span>
          </template>
        </el-form-item>
        <el-form-item label="电话：" label-width="120px" prop="tel" style="text-align: left">
          <template>
            <span v-if="myInfo.isEdit"><el-input v-model="myInfo.tel" autocomplete="off"></el-input></span>
            <span v-else>{{myInfo.tel}}</span>
          </template>
        </el-form-item>
        <el-form-item label="积分：" label-width="120px" prop="integral" style="text-align: left">
          <template>
            <span>{{myInfo.integral}}</span>
          </template>
        </el-form-item>
      </el-form>

      <template>
        <span v-if="myInfo.isEdit">
        <el-button type="primary" style="width: 89px" @click="cancel">取消</el-button>
          <el-button type="primary" style="width: 89px" @click="save" >提交</el-button>
        </span>
        <span v-else>
        <el-button type="primary" style="width: 89px" @click="edit">修改</el-button>
        </span>
      </template>
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="modifyPasswordVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="updatePassword('modifyPassword')" >修 改</el-button>-->
<!--      </div>-->
    </el-dialog>


  </div>
</template>

<script>

import axios from "axios";

export default {
  computed: {
    menu () {
      return this.$store.state.menu
    }
  },
  methods:{
    logout() {
      this.$confirm('您确定要注销登录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定
        const _this = this;
        axios.get('http://localhost:8181/api/auth/logout').then(function (resp){
          if (resp.data.code === 200){
            window.localStorage.removeItem('username')
            _this.$router.replace({path:'/login'})
            _this.$store.state.menu = []
          }
        })

        this.$message({
          type: 'success',
          message: '注销成功!'
        });
      }).catch(() => {//取消
      });
    },

    showMyInfo(){
      const _this = this
      axios.get('http://localhost:8181/api/account/getmyinfo/'+_this.username).then(function (resp){
        _this.myInfo = resp.data.data
        _this.$set(_this.myInfo, 'isEdit', false)
      })
      this.myInfoVisible = true

    },

    showModifyPassword(){
      this.modifyPassword.newPassword = ''
      this.modifyPassword.oldPassword = ''
      this.modifyPasswordVisible = true
    },
    updatePassword(formName){
      const _this = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('http://localhost:8181/api/account/updatePassword/'+_this.username+'/'+_this.modifyPassword.oldPassword+'/'+_this.modifyPassword.newPassword).then(function (resp){
            if (resp.data.code === 200){
              _this.$message.success(resp.data.reason)
              axios.get('http://localhost:8181/api/auth/logout').then(function (resp){
                if (resp.data.code === 200){
                  window.localStorage.removeItem('username')
                  _this.$router.replace({path:'/login'})
                  _this.$store.state.menu = []
                }
              })
            }else{
              _this.$message.error(resp.data.reason)
            }
          })
        } else {
          return false;
        }
      });
    },
    edit() {
      this.myInfo.isEdit = true
      // 表格数据都是前端处理，需要把旧值存起来，用户点击修改之后修改了原来的数据，但是又点了取消的情况，还需要获取到原来的值
      sessionStorage.setItem('oldEmail', this.myInfo.email)
      sessionStorage.setItem('oldTel', this.myInfo.tel)
    },
    //保存
    save() {
      const _this = this
      axios.post('http://localhost:8181/api/account/updatemyinfo', this.myInfo).then(function (resp) {
        _this.$message.success(resp.data.reason)
      })
      this.myInfo.isEdit = false
    },
    cancel() {
      this.$set(this.myInfo, 'email',sessionStorage.getItem('oldEmail'))
      this.$set(this.myInfo, 'tel',sessionStorage.getItem('oldTel'))
      this.myInfo.isEdit = false
    },

  },
  data() {
    return {
      myInfo: {
        aid:'',
        username:'',
        email:'',
        tel:'',
        integral:'',
      },
      modifyPassword:{
        oldPassword:'',
        newPassword:''
      },
      myInfo_rules:{

      },
      modifyPassword_rules:{
        oldPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ]
      },
      modifyPasswordVisible:false,
      myInfoVisible:false,
      username:''
    }
  },
  created() {
    this.username =window.localStorage.getItem('username')
  }
}
</script>


<style>
#username{
  float: right;
  text-align: center;
  margin-top: 20px;
}

.left{
  overflow-x: hidden;
}
</style>
