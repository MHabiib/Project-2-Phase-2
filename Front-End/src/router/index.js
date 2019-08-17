import Vue from 'vue';
import Router from 'vue-router';
import DashboardPage from '../page/Dashboard';
import LoginPage from '../page/Login';
import OverviewPage from '../page/Overview';
import ExpensesPage from '../page/Expenses';
import MembersPage from '../page/Members';
import ManageUserPage from '../page/ManageUser';
import ManageGroupPage from '../page/ManageGroup';
import SidebarComponent from '../components/Sidebar';
import HeaderSection from '../components/HeaderSection';
import PaymentHistoryPage from '../page/PaymentHistory';
import ManagePaymentPage from '../page/ManagePayment';
import ProfilePage from '../page/Profile'
import '../index.css';
import * as moment from 'moment';


Vue.use(Router);
Vue.component('SidebarComponent', SidebarComponent);
Vue.component('HeaderSection', HeaderSection);

Vue.mixin({
  data: function() {
    return {
      groupName: localStorage.groupName,
      myRole: localStorage.role
    }
  },
  computed:{

  },
  methods:{

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
    fromNow(date) {
      return moment(date).fromNow()
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
        // if(localStorage.getItem('token')) {
        if(localStorage.getItem('accessToken')) {
          return '/dashboard'
        } else {
          return '/login'
        }
      }
    },
    {
      path: '/dashboard',
      component: DashboardPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/paymenthistory',
      component: PaymentHistoryPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/managepayment',
      component: ManagePaymentPage,
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      component: ProfilePage,   
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
      meta: { requiresAuth: true, adminOnly: true }
    },
    {
      path: '/manage-group',
      component: ManageGroupPage,
      meta: { requiresAuth: true, adminOnly: true }
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
    } else {
      if(to.matched.some(record => record.meta.adminOnly)) {
        if(localStorage.getItem('role') !== 'SUPER_ADMIN') {
          alert('Menu ini hanya boleh diakses oleh super admin.');
          next({
            path: '/dashboard'
          })
        } else {
          next();
        }
      } else {
        next();
      }
    }
  } else {
    next()
  }
})

export default router;
