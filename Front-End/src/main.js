import Vue from 'vue'
import App from './app'
import Store from './store/store.js'
import router from './router'

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  components: { app },
  template: '<app/>'
})