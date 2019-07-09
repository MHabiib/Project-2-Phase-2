<template>
  <div class="loginWrapper">
    <div class="loginComponent">
      <div class="loginTitle">
        Login
      </div>

      <form @submit="loginHandler">
        <div class="loginBody">
          <input type="email" name="email" placeholder="Email" v-model="emailInput" /><br />
          <input type="password" name="password" placeholder="Password" v-model="passwordInput" /><br />
          <button @click.prevent="loginHandler">Go</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
  document.title = 'Login | Team Cash Flow Management';

  export default {
    data: function() {
      return {
        emailInput: '',
        passwordInput: ''
      }
    },
    methods: {
      loginHandler() {
        let dataLogin = {
          email: this.emailInput,
          password: this.passwordInput
        };

        this.axios
          .post('http://localhost:8088/auth/signin', dataLogin)
          .then(res => {
            // this.$store.dispatch("login", res.data);
            localStorage.setItem('accessToken', `Token ${res.data.token}`);
            localStorage.setItem('refreshToken', res.data.refreshToken);
            localStorage.setItem('userEmail', this.emailInput);
            localStorage.setItem('groupName', res.data.groupName )
          })
          .then(()=> {
            alert('Login Berhasil');
            this.$router.push('/dashboard');
            
            // this.$router.push('/tugasweb');// hanya sebuah tugas web (coba-coba punya) #abaikanSaja
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
