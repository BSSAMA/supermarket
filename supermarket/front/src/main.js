import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
import './css/PublicStyle.css'
import './css/TableCss.css'
import axios from "axios";
import 'default-passive-events'

Vue.config.productionTip = false

// //登录请求预处理
// //没有此设置，后端就无法将数据保存到cookie中
axios.defaults.withCredentials = true

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
