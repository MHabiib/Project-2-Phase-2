<template>
  <div>
    <h2>ADD USER</h2>
    <form>
        <div class="Add User">
          Nama: <input type="name" name="name" placeholder="Name" v-model="nameInput" /><br />
          Email: <input type="email" name="email" placeholder="Email" v-model="emailInput" /><br />
          Password <input type="password" name="password" placeholder="Password" v-model="passwordInput" /><br />
          Role: <input type="text" name="role" placeholder="role" v-model="roleInput" /><br />
          groupName: <input type="groupName" name="groupName" placeholder="groupName" v-model="groupNameInput" /><br />
          <button @click.prevent="AddUserHandler">Simpan</button>
     </div>
  </form>
  </div>
</template>

<script>

  export default {
    data: function() {
      return { 
        nameInput:'',
        emailInput: '',
        passwordInput: '',
        roleInput:'',
        groupNameInput:''
      }
    },
    methods: {
        AddUserHandler() {
        let AddUser = {
          name: this.nameInput,
          email: this.emailInput,
          password: this.passwordInput,
          role: this.roleInput,
          groupName:this.groupNameInput
        };
        let formData = new FormData()
        let userJSON = JSON.stringify(AddUser)
        formData.append('user',userJSON)
       fetch(`http://localhost:8088/api/user`, {
          method: "POST",
          headers: {'Authorization': localStorage.getItem('accessToken')},
          body : formData
        })
         .then(res => {
            console.log(res)
            // alert(res)
            if(res.status==401){
              this.getNewToken(this.AddUserHandler) // jika response dari server 401 (karena token expired/invalid), maka pangil request utk ambil token baru
            } 
            localStorage.setItem('accessToken','Token '+res.headers.get("Authorization")) //timpa acessToken di localstorage dengan yang baru setiap res.ok berhasil
            //jika token blum expired, maka timpa token lama dengan token yang baru yang diberi dengan server.
          })
          .catch(err => {alert(`Catch an error: ${err}`)})  
        },
        getNewToken(callback){
          let tokenMap = {
            accessToken:(localStorage.getItem("accessToken")).substring(6),
            refreshToken:localStorage.getItem("refreshToken")
          }
          let bodyJSON = JSON.stringify(tokenMap)
          fetch(`http://localhost:8088/auth/refreshtoken`, {
              method: "POST",
              headers: {"Content-Type" : "application/json"},
              body : bodyJSON
            })
            .then(res => {
                console.log(res)
                // alert(res)
                if(!res.ok){
                  // this.$router.push('/login')
                  console.log('Error failed to get refreshToken')
                }
                else {
                  res.json().then((data)=>{ // parse response dari server ke JSON
                    localStorage.setItem("accessToken","Token "+data.accessToken)//timpa token lama dengan token yang baru
                    localStorage.setItem("refreshToken",data.refreshToken) 
                    callback()//jalankan kembali request yang sempat ditolak karena 401 di atas
                  })
                }
                })
              .catch(err => {alert(`Catch an error: ${err}`)})
        }//end method

    }
    
  }
</script>
