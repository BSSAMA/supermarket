import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    menu: []
  },
  mutations: {
    initAdminMenu (state, menus) {
      state.menu = menus
    },
    logout(state){
      state.menu = []
    }
  },
  actions: {
  },
  modules: {
  }
})
