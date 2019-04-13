import Vue from 'vue'
import Router from 'vue-router'
import Home from '../page/home'
import Login from '../page/login'

Vue.use(Router)
import store from '../store/store.js'

let router = new Router({
    routes: [
        {
            path: '/',
            component: Home
        },
        {
            path: '/login',
            component: Login
        }]
}
)