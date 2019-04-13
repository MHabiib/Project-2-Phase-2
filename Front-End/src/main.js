import Vue from 'vue'
import App from './app'
import Store from './store/store.js'
import router from './router'
import axios from 'axios'

Object.defineProperties(Vue.prototype, {
  axios: {
    get () {
      return axios
    }
  }
})



Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  Store,
  render: h => h(App)
})
