import Vue from 'vue';
import Router from 'vue-router';
import DashboardPage from '../page/Dashboard';
import PaymentPage from '../page/Payment';
import LoginPage from '../page/Login';
import OverviewPage from '../page/Overview';
import ExpensesPage from '../page/Expenses';
import MembersPage from '../page/Members';
import ManageUserPage from '../page/ManageUser';
import ManageGroupPage from '../page/ManageGroup';
import SidebarComponent from '../components/Sidebar';
import HeaderSection from '../components/HeaderSection';
import '../index.css';

Vue.use(Router);
Vue.component('SidebarComponent', SidebarComponent);
Vue.component('HeaderSection', HeaderSection);

Vue.mixin({
  data: function() {
    return {
      usernameGloballyStored: 'Global'
    }
  }
})

const router = new Router({
  routes: [
    {
      path: '/',
      redirect: to => { // Ini pakai function untuk nantinya ngecek udah login atau belum. Kalau udah balikin Dashboard, kalau belum balikin Login
        return '/login'
      },
      // meta: { requiresAuth: true }
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
    },
    {
      path: '/manage-user',
      component: ManageUserPage
    },
    {
      path: '/manage-group',
      component: ManageGroupPage
    }
  ],
  mode: "history"
});

// let isAuthenticated = false;

// router.beforeEach((to, from, next) => {
//   if (to.matched.some(record => record.meta.requiresAuth)) {
//     if (isAuthenticated) {
//       next({
//         path: '/login',
//         query: {
//           redirect: to.fullPath,
//         },
//       });
//     } else {
//       next();
//     }
//   } else {
//     next();
//   }
// })

export default router;
