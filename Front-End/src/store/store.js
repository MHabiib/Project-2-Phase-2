import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

const LOGIN = "LOGIN";
const LOGIN_SUCCESS = "LOGIN_SUCCESS";
const LOGOUT = "LOGOUT";

const state =  {
  isLoggedIn: !!localStorage.getItem("token")
}

const mutations =  {
  [LOGIN] (state) {
    state.pending = true;
  },
  [LOGIN_SUCCESS] (state) {
    state.isLoggedIn = true;
    state.pending = false;
  },
  [LOGOUT](state) {
    state.isLoggedIn = false;
  }
};
const actions = {
  login({ commit }, TOKEN) {
    commit(LOGIN); // show spinner
    return new Promise(resolve => {
      setTimeout(() => {
        localStorage.setItem("token", 'Token '.concat(TOKEN));
        commit(LOGIN_SUCCESS);
        resolve();
      }, 1000);
    });
  },
  logout({ commit }) {
    localStorage.removeItem("token");
    commit(LOGOUT);
  }
};
const getters = {
  isLoggedIn: state => {
    return state.isLoggedIn
  }
}

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions
})
