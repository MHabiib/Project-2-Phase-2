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
        fetch('http://localhost:8088/api/hello', {
          method: 'GET',
          headers: {
            'Authorization': localStorage.getItem('token')
          }
        })
          .then(response => console.log(response))
      }
    }
  }
</script>

<style>

</style>
