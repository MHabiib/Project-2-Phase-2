<template>
  <div class="loginWrapper" :style="{'background-image': 'url(' + require('../assets/bg.jpg') + ')'}">
    <div class="loginComponent">
      <div class="loginTitle">
        Login
      </div>

      <form @submit="loginHandler">
        <div class="loginBody">
          <input type="email" name="email" placeholder="Email" v-model="email" /><br />
          <input type="password" name="password" placeholder="Password" v-model="password" /><br />
          <button @click.prevent="loginHandler">Go</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
  document.title = 'Login | Team Cash Flow Management';
  import Helper from '../../Helper';
  import axios from 'axios';

  export default {
    data: function() {
      return {
        email: '',
        password: '',
        message: '',
        response:{},
      }
    },
    methods: {
      loginHandler() {
        if(this.email === '' || this.password === ''){
          this.message = 'Please input email/password.'
          alert(this.message)
          return;
        }

        let dataLogin = {
          email: this.email,
          password: this.password
        };

        axios
          .post(`${Helper.backEndAddress}/auth/signin`, dataLogin)
          .then(res => {
            this.response = res;
            console.log(this.response)
            // this.$store.dispatch("login", res.data);
            localStorage.setItem('accessToken', `Token ${res.data.accessToken}`);
            localStorage.setItem('refreshToken', res.data.refreshToken);
            localStorage.setItem('userEmail', this.email);
            localStorage.setItem('groupName', res.data.groupName);
            localStorage.setItem('role', res.data.role);
            localStorage.setItem('groupCreatedDate', parseInt(res.data.groupCreatedDate));
            localStorage.setItem('imageURL', res.data.imageURL)
            this.$store.dispatch('setUser',res.data.currentUser)
            this.message='Welcome '+ this.$store.getters.user.name
            alert(this.message)
            
          })
          .then(()=> {
            this.$router.push('/dashboard');
          })
          .catch(err => {alert(`Catch an error: ${err}`)})
      }
    }
  }
</script>

<style>
  .loginWrapper {
    background-color: #F2F4F6;
    font-family: 'Helvetica Neue';
    height: 100vh;
    display: flex;
  }

  .loginComponent {
    width: 300px;
    margin: auto;
  }

  .loginTitle {
    background-color: #4C8CD2;
    color: #fff;
    font-weight: 400;
    padding: 14px 20px;
    border-radius: 7px;
    text-align: center;
    position: relative;
    z-index: 1;
    width: 75%;
    margin: auto;
    box-shadow: 0 2px 6px #bbb;
  }

  .loginBody {
    background-color: #fff;
    border-radius: 7px;
    text-align: center;
    position: relative;
    top: -30px;
    z-index: 0;
    padding: 35px 0 18px 0;
    box-shadow: 0 2px 6px #bbb;
  }

  .loginBody input {
    width: 200px;
    border: none;
    outline: none;
    background-color: #f2f4f6;
    padding: 10px;
    box-sizing: border-box;
    border-radius: 5px;
    margin-top: 15px;
    text-align: center;
  }

  .loginBody button {
    margin-top: 15px;
    padding: 10px 20px;
    background-color: #4c8cd2;
    color: #fff;
    border-radius: 5px;
    outline: none;
    border: none;
    box-shadow: 0 2px 6px #bbb;
  }

  .loginBody button:hover {
    background-color: #70A5E1;
    cursor: pointer;
  }
</style>
