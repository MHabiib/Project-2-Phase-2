import Vue from 'vue';
import Router from 'vue-router';
import DashboardPage from '../page/Dashboard';
import PaymentPage from '../page/Payment';
import LoginPage from '../page/Login';
import OverviewPage from '../page/Overview';
import ExpensesPage from '../page/Expenses';
import MembersPage from '../page/Members';
import SidebarComponent from '../components/Sidebar';
import HeaderSection from '../components/HeaderSection';
import '../index.css';

Vue.use(Router);
Vue.component('SidebarComponent', SidebarComponent);
Vue.component('HeaderSection', HeaderSection);

export default new Router({
  routes: [
    {
      path: '/',
      redirect: to => { // Ini pakai function untuk nantinya ngecek udah login atau belum. Kalau udah balikin Dashboard, kalau belum balikin Login
        return '/login'
      }
    },
    {
      path: '/dashboard',
      component: DashboardPage
    },
    {
      path: '/payment',
      component: PaymentPage
    },
    {
      path: '/login',
      component: LoginPage
    },
    {
      path: '/overview',
      component: OverviewPage
    },
    {
      path: '/expenses',
      component: ExpensesPage
    },
    {
      path: '/members',
      component: MembersPage
    }
  ],
  mode: "history"
});
