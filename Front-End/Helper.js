let getNewToken = function(callback){
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
          alert('your session is expired\nPlease re-login.')
          window.location='/login'
          console.log('Error failed to get refreshToken')
        }
        else {                
          res.json().then((data)=>{ // parse response dari server ke JSON
            localStorage.setItem("accessToken","Token "+data.accessToken)//timpa token lama dengan token yang baru
            localStorage.setItem("refreshToken",data.refreshToken) 
            console.log('HelperJS: succeed to get new token!')
            this.loading = false
            callback()//jalankan kembali request yang sempat ditolak karena 401 di atas
          })
        }
        })
      .catch(err => {alert(`Catch an error: ${err}`)})
}

exports.getNewToken = getNewToken
exports.backEndAddress = 'http://localhost:8088'