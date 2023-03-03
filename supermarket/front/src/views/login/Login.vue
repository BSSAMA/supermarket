<template>
<div id="login">
  <h2 id="login_title">账号登录</h2>
  <el-form ref="login" :model="login"  label-width="80px" :rules="login_rules">
    <el-form-item prop="username" ><el-input placeholder="请输入用户名" v-model="login.username" prefix-icon="el-icon-user-solid"  class="login_input"></el-input></el-form-item>
    <el-form-item prop="password" ><el-input placeholder="请输入密码" v-model="login.password" prefix-icon="el-icon-lock" show-password class="login_input"></el-input></el-form-item>
    <el-form-item prop="captcha" >
      <el-input placeholder="请输入验证码" v-model="login.captcha" class="login_input" style="width: 210px" ></el-input>
      <img :src="'data:image/gif;base64,'+ captcha" alt="更换验证码" @click="getVerify()" style="padding-right: 30px;padding-top: 3px;float: right" />
    </el-form-item>
    <el-button type="primary" id="login_button" @click="userLogin('login')">登录</el-button>
  </el-form>

    <el-button type="text" @click="showRegister">立即注册</el-button>

    <el-dialog title="注册账号" :visible.sync="dialogFormVisible" width="450px" v-if="dialogFormVisible">
      <el-form ref="register" :model="register" :rules="register_rules" >
        <el-form-item label="用户名：" prop="username" :label-width="formLabelWidth" >
          <el-input v-model="register.username" autocomplete="off" class="register_input"></el-input>
        </el-form-item>
        <el-form-item label="密码：" :label-width="formLabelWidth" prop="password">
          <el-input v-model="register.password" autocomplete="off" class="register_input" type="password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码：" :label-width="formLabelWidth" prop="checkPass">
          <el-input v-model="register.checkPass" autocomplete="off" class="register_input" type="password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱：" :label-width="formLabelWidth" prop="email">
          <el-input v-model="register.email" autocomplete="off" class="register_input"></el-input>
        </el-form-item>
        <el-form-item label="电话：" :label-width="formLabelWidth" prop="tel">
          <el-input v-model="register.tel" autocomplete="off" class="register_input"></el-input>
        </el-form-item>
        <el-form-item label="验证码：" :label-width="formLabelWidth" prop="verify">
          <el-input  v-model="register.verify" autocomplete="off" style="width: 120px" class="register_input verifyInput"></el-input>
          <el-button size="medium" style="float: left;margin-left: 8px;margin-top: 2px" @click="sendVerify">获取验证码</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="userRegister('register')">注 册</el-button>
      </div>
    </el-dialog>

</div>
</template>

<script>
import axios from "axios";

export default {
  methods:{
    showRegister(){
      this.register.username = ''
      this.register.password = ''
      this.register.checkPass = ''
      this.register.email = ''
      this.register.tel = ''
      this.register.verify = ''
      this.dialogFormVisible = true
    },
    sendVerify(){
      const _this = this
      axios.get('http://localhost:8181/api/auth/verifycode/'+this.register.email).then(function (resp){
        if (resp.data.code === 200){
          _this.$message.success(resp.data.reason)
        }else {
          _this.$message.error(resp.data.reason);
        }
      })
    },
    userRegister(formName){
      const _this = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('http://localhost:8181/api/auth/register/'+this.register.verify,this.register).then(function(resp){
            if (resp.data.code === 200){
              _this.$message.success(resp.data.reason)
              _this.dialogFormVisible = false
            }else {
              _this.$message.error(resp.data.reason);
            }
          })
        } else {
          return false;
        }
      });
    },
    userLogin(formName){
      const _this = this;

      _this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('http://localhost:8181/api/auth/checkcaptcha/'+sessionStorage.getItem('imgUUID')+'/'+this.login.captcha).then(function(resp){
              if (resp.data.code !== 200) {
                _this.$message.error(resp.data.reason)
                _this.getVerify()
              }
              else {
                axios.post('http://localhost:8181/api/auth/login','username='+_this.login.username+'&password='+_this.login.password).then(function (resp){
                  if (resp.data.code === 200){
                    _this.$message.success(resp.data.reason)
                    sessionStorage.removeItem('imgUUID')
                    localStorage.setItem('username' , resp.data.data)
                    _this.$router.replace({path:'/'})
                  }else {
                    _this.$message.error(resp.data.reason);
                    _this.getVerify()
                  }
                })
              }
          })
        } else {
          return false;
        }
      });
    },

    getVerify(){
      const _this = this
      axios.get('http://localhost:8181/api/auth/getcaptcha/'+sessionStorage.getItem('imgUUID')).then(function(resp){
        if (resp.data.code === 200){
          if (sessionStorage.getItem('imgUUID') == null)
            sessionStorage.setItem('imgUUID',resp.data.data.imgUUID)
          _this.captcha = resp.data.data.img
        }
      })
    }
  },

  data() {
    var validatePass = (rule, value, callback) => {
      if (value !== '') {
        if (this.register.checkPass !== '') {
          this.$refs.register.validateField('checkPass');
        }
        callback();
      }
    };
    var validateCheckPass = (rule, value, callback) => {
      if (value !== this.register.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    var validateVerify = (rule, value, callback) => {
      var numReg = /^[0-9]*$/
      var numRe = new RegExp(numReg)
      if (!numRe.test(value)) {
        callback(new Error('验证码为数字'));
      }else {
        if (value < 100000 || value > 999999) {
          callback(new Error('验证码长度为6位数'));
        } else {
          callback();
        }
      }
    };
    return {
      login: {
        username:'admin',
        password:'123456',
        captcha:''
      },
      captcha: null,
      dialogFormVisible: false,
      register: {
        username: '',
        password: '',
        checkPass: '',
        email: '',
        tel: '',
        verify: ''
      },
      formLabelWidth: '120px',
      register_rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur' }
        ],
        password:[
          { required: true, message: '请输入密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validateCheckPass }
        ],
        tel: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          // 这个只能验证手机号
          // { pattern:/^0{0,1}(13[0-9]|15[7-9]|153|156|18[7-9])[0-9]{8}$/, message: "请输入合法手机号", trigger: "blur" }
          { pattern:/^((0\d{2,3}-\d{7,8})|(1[35847]\d{9}))$/, message: "请输入合法手机号/电话号", trigger: "blur" }
        ],
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { pattern:/^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g, message: "请输入正确的邮箱", trigger: "blur"}
        ],
        verify: [
          { required: true, message: '请输入邮箱验证码', trigger: 'blur' },
          { validator: validateVerify, trigger: 'blur' }
        ]
      },
      login_rules :{
        username:[
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password:[
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        captcha:[
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    const _this = this
    axios.get('http://localhost:8181/api/auth/getcaptcha/'+sessionStorage.getItem('imgUUID')).then(function(resp){
      if (resp.data.code === 200){
        if (sessionStorage.getItem('imgUUID') == null)
          sessionStorage.setItem('imgUUID',resp.data.data.imgUUID)
        _this.captcha = resp.data.data.img
      }
    })
  }
}
</script>

<style scoped>
#login{
  text-align: center;
  position: absolute;
  width: 400px;
  left: 50%;
  margin-left: -200px;
  top: 20%;
  padding-bottom: 20px;
  background-color: skyblue;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.login_input{
  margin: 5px 0 0 -80px;
  width: 340px;
}
#login_title{
  padding: 40px 0 25px 0;
  color: #21293a;
  font-size: 20px;
  line-height: 28px;
  text-align: center;
}
#login_button{
  margin-top: 20px;
  width: 340px;
}
.register_input{
  width: 240px;
  float: left;
}
</style>
