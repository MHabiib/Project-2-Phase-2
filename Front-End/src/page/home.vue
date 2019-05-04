<template>
    <div>
        <router-link to="/login">
        Login
        </router-link>
      <button @click.prevent="logout">Logout</button>
      <button @click.prevent="hello">Tes Hello</button>
    </div>
</template>

<script>
  import { mapActions } from 'vuex'

export default {

  methods : {
    ...mapActions([
       'logout'
     ]),
    hello() {
      const auth={
        headers:{'Authorization':localStorage.getItem('token')}}
        //https://stackoverflow.com/questions/3102819/disable-same-origin-policy-in-chrome
      this.axios
        .get('http://localhost:8088/api/hello',auth)
        .then((res) => {
            alert (JSON.stringify(res))
        }).catch((e) => {
          alert(e)
      })
    }
  }
}
</script>

<style>
    
</style>
