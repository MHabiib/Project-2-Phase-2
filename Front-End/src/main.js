import Vue from 'vue';
import App from './app';
import router from './router';
import axios from 'axios';
import store from './store/store';
import byPass from './page/Dashboard.vue';

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
  store,
  render: h => h(App)
})
