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
import tugasWeb from '../page/tugasWeb';
import '../index.css';

Vue.use(Router);
Vue.component('SidebarComponent', SidebarComponent);
Vue.component('HeaderSection', HeaderSection);

Vue.mixin({
  data: function() {
    return {

    }
  },
  filters: {
    thousandSeparators: function(numbers) {
      if(numbers === undefined) {
        return '';
      } else {
        let result = '';
        let counter = 0;
        for (let i = numbers.toString().length ; i >= 0 ; i--){
            if(counter % 3 === 0 && counter !== 0 && counter !== numbers.toString().length){
                result = '.' + numbers.toString().substr(i, 1) + result;
            } else {
                result = numbers.toString().substr(i, 1) + result;
            }
            counter += 1;
        }
        return result;
      }
    },
    dateFormatter(dateToFormat) {
      const monthToString = (month)=> {
        switch(month) {
          case 0: return 'January'
          case 1: return 'February'
          case 2: return 'March'
          case 3: return 'April'
          case 4: return 'May'
          case 5: return 'June'
          case 6: return 'July'
          case 7: return 'August'
          case 8: return 'September'
          case 9: return 'October'
          case 10: return 'November'
          case 11: return 'December'
        }
      }

      const dateObjected = new Date(dateToFormat);
      return `${dateObjected.getDate()} ${monthToString(dateObjected.getMonth())} ${dateObjected.getFullYear()}`
    }
  },
})

const router = new Router({
  routes: [
    {
      path: '/',
      redirect: to => {
        if(localStorage.getItem('token')) {
          return '/dashboard'
        } else {
          return '/login'
        }
      }
    },
    {
      path: '/tugasweb',
      component: tugasWeb,
      meta: { requiresAuth: true }
    },
    {
      path: '/dashboard',
      component: DashboardPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/payment',
      component: PaymentPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      component: LoginPage
    },
    {
      path: '/overview',
      component: OverviewPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/expenses',
      component: ExpensesPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/members',
      component: MembersPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/manage-user',
      component: ManageUserPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/manage-group',
      component: ManageGroupPage,
      meta: { requiresAuth: true }
    }
  ],
  mode: "history"
});

router.beforeEach((to, from, next) => {
  if(to.matched.some(record => record.meta.requiresAuth)) {
    if (localStorage.getItem('accessToken') === null) {
      next({
        path: '/login'
      })
    }
    else {
      // let user = JSON.parse(localStorage.getItem('user'));
      // if(to.matched.some(record => record.meta.is_admin)) {
      //   if(user.is_admin == 1){
      //     next()
      //   }
      //   else{
      //     next({ name: 'userboard'})
      //   }
      // } else {
      //   next()
      // }
      next();
    }
  } else {
    next()
  }
})

export default router;
