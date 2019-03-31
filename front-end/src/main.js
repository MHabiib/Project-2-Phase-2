import Vue from 'vue';
// eslint-disable-next-line
import Login from './login.vue';
import Dashboard from './dashboard.vue';
import SidebarDashboard from './components/sidebar.vue';
import './index.css';

Vue.config.productionTip = false;
Vue.component('sidebarDashboard', SidebarDashboard)

new Vue({
  render: h => h(Dashboard)
}).$mount('#app')