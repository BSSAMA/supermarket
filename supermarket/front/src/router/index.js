import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../views/layout/Index'
import Login from '../views/login/Login'
import axios from "axios";
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


//转换格式
const formatRoutes = (routes) => {
  let fmtRoutes = []
  routes.forEach(route => {
    if (route.children) {
      route.children = formatRoutes(route.children)
    }

    let fmtRoute = {
      path: route.path,
      component: resolve => {
        require(['../views/layout/' + route.component + '.vue'], resolve)
      },
      name: route.name,
      nameZh: route.nameZh,
      iconCls: route.iconCls,
      children: route.children
    }
    fmtRoutes.push(fmtRoute)
  })
  return fmtRoutes
}

const initMenu = (router, store) => {
  const _this = this
  if (store.state.menu.length > 0) {
    return
  }
  axios.get('http://localhost:8181/api/menu/getmenu').then(resp => {
    if (resp.data.code === 200) {

      var fmtRoutes = formatRoutes(resp.data.data)
      for (let x of fmtRoutes) {
        router.addRoute(x)
      }
      store.commit('initAdminMenu', fmtRoutes)
    }
  })
}

router.beforeEach((to, from, next)=>{
  document.title = '小型超市管理系统'
  if (!to.path.startsWith('/login')){
    axios.get('http://localhost:8181/api/account/islogin').then(function (resp){
      if (resp.data.code !==200){
        if (resp.data.code === 2001)
          next({path:'/login'})
        else {
          alert(resp.data.reason)
          next({path:'/login'})
        }
      }
    })
    initMenu(router, store)
  }else {
    axios.get('http://localhost:8181/api/account/islogin').then(function (resp){
      if (resp.data.code === 200){
        next({path:'/'})
      }
    })
  }
  next()
})


export default router
