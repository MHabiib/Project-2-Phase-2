import Vue from 'vue'
import Vuex from 'vuex'
import Helper from '../../Helper'

Vue.use(Vuex)

const LOGIN = "LOGIN";
const LOGIN_SUCCESS = "LOGIN_SUCCESS";
const LOGOUT = "LOGOUT";

const state =  {
  isLoggedIn: !!localStorage.getItem("accessToken"),
  user : {}
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
  },
  SET_USER (state, _user){
    state.user = Object.assign({},_user)
  },
};
const actions = {
  login({ commit }, TOKEN) {
    commit(LOGIN); // show spinner
    return new Promise(resolve => {
      setTimeout(() => {
        localStorage.setItem("accessToken", 'Token '.concat(TOKEN));
        commit(LOGIN_SUCCESS);
        resolve();
      }, 1000);
    });
  },
  logout({ commit }) {
    let jsonBody = JSON.stringify({
      accessToken:localStorage.getItem('accessToken').substring(6),
      refreshToken:localStorage.getItem('refreshToken')
    })
    fetch(`${Helper.backEndAddress}/auth/signout`,{
      method:'PUT',
      headers:{
        'Content-Type':'application/json'
      },
      body:jsonBody
    }).then(response=>{
      if(response.ok){
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('groupName')
        localStorage.removeItem('userEmail')
        localStorage.removeItem('role')
        localStorage.removeItem('groupCreatedDate')
        localStorage.removeItem('imageURL')
      }
      else{
        alert('Failed to log out!Something terrible happened.')
      }
    })
    localStorage.removeItem("accessToken");
    commit(LOGOUT);
  },
  setUser: function(context,_user){
    context.commit("SET_USER",_user)
  }
};
const getters = {
  isLoggedIn: state => {
    return state.isLoggedIn
  },
  user: state =>{
    return state.user
  }
}

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions
})
