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
     // let token = localStorage.getItem("token")
      var header = {
        'Authorization':localStorage.getItem("token")
      }
      console.log(header)
      console.log(localStorage.getItem(("token")))
      this.axios
        .get('http://localhost:8088/api/hello', null,{headers:{header}})
        .then((res) => {
            alert (res)
        }).catch((e) => {
          alert(e)
      })
    }


  }

}
</script>

<style>
    
</style>
