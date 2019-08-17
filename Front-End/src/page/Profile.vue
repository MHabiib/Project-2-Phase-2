<template>
  <div class='profileComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='My Profile'/>
      <div class="profileBodySection">
        <div class="profileTableHeader">
          <div class="profileTableHeaderTitle">
            User Id : {{currentUser.idUser}}
          </div>
          <div class="myParent" style='display: flex;'>
            <div class="refreshBtn" @click='getUserData(email)'>
              <img src="../assets/sinchronize-256.png" width="16px" alt="Refresh">
            </div>
            <div v-if="editMode" class="cancel" @click='cancelChanges()'>
              Cancel
            </div>
            <div v-if="editMode" class="save" @click='saveChanges()'>
              &nbsp;&nbsp;Save&nbsp;&nbsp;
            </div>
            <div v-show="!editMode" class="profileButton" @click='updateProfile()'>
              Update Profile
            </div>
          </div>
        </div>

        <div class="profileTableBody">
          <table>
            <div  class="borderBottom">
              <tr>
                <th >Group Info</th>
              </tr>
            </div>
            <tbody id='infiniteScroll'>
              <tr>
                <!-- <th>&nbsp;&nbsp;</th> -->
                <td>Group Name</td>
                <td>: {{currentUser.groupName}}</td>
                <td>, joined since {{currentUser.joinDate | dateFormatter}}</td>
                <!-- <th>Status</th> -->
              </tr>
              <tr>
                <td>My Balance / Contribution</td>
                <td>: {{currentUser.balance}}</td>
                <td>/ {{currentUser.balanceUsed}}                  
                </td>
              </tr>
              <tr>
                <td>Period Paid / Period Missed</td>
                <td>: {{currentUser.totalPeriodPayed}}</td>
                <td>/ {{currentUser.periodeTertinggal}}</td>
              </tr>
              <div class="borderBottom">
                <tr>
                  <th >Personal Info</th>
                </tr>
              </div>
              <tr>
                <td>Email</td>
                <td>: {{currentUser.email}}</td>
             </tr>
             <tr>
                <td>Name</td>
                <td v-show="!editMode">: {{currentUser.name}}</td>
                <td v-if="editMode">
                  <input class='updateInput' type="text" placeholder='Name' v-model='newUser.name'/>
                </td>
             </tr>
             <tr>
                <td>Phone</td>
                <td v-show="!editMode">: {{currentUser.phone}}</td>
                <td v-if="editMode"> 
                  <input class='updateInput' type="text" placeholder='Phone' v-model='newUser.phone'/>
                <td><img v-if="ppAvailable" :src="currentUser.imageURL" alt="pp" class="profilePicture"></td>
             </tr>
              <tr v-show="!editMode">
                <td>Role</td>
                <td>: {{currentUser.role}}</td>
              </tr>
             <div v-if="editMode">
              <tr>
                <td>Password</td>
                <td >
                  <input class='updateInput' type="password" placeholder='New Password' v-model='newPassword'/>                  
                </td>                
                <td>
                  <input 
                    accept="image/*" 
                    type="file" 
                    @change="changeFile($event)" 
                    class="selectPicture"
                    placeholder="Image file"/>
                </td>
             </tr>
             <tr>
               <td>&nbsp;</td>
               <td>
                  <input class='updateInput' type="password" placeholder='Repeat Password' v-model='repeatPassword'/>
                </td>
             </tr>
             </div>
              <div style="text-align:center">
                <div v-show="loading" class="lds-ring"><div></div><div></div><div></div><div></div></div>
              </div>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';
  import Helper from '../../Helper';


  export default {
    data: function() {
      return {
        email:localStorage.getItem('userEmail'),
        currentUser : {},
        newUser:{},
        fileInput:null,
        loading:false,
        editMode:false,
        showBulkInsertWindow: false,
        newPassword:'',
        repeatPassword:'',
      }
    },
    created(){
      this.currentUser = Object.assign({},this.$store.getters.user)
      this.newUser = Object.assign({},this.currentUser)
      if(this.currentUser.name ==null || this.currentUser.name == undefined){
        this.getUserData(this.email)
      }
      // console.log('Current User :: '+ this.currentUser.name)
    },
    computed: {
      rightPanelWidth: function() {return (document.documentElement.clientWidth - 280);
      },
      ppAvailable(){
        if(this.currentUser.imageURL==='' || this.currentUser.imageURL== null || this.currentUser.imageURL=="null"){
          return false
        }
        return true
      }
    },

    methods: {
      changeFile(e) {
         if(e.target.files[0].size > 5000000) {
          alert('Max Image size is 5 MB. Please upload a lower resolution image.');
          this.fileInput = null;
          e.target.value = null;
        } else {
        this.fileInput = e.target.files[0];
        }
      },
      getUserData(email){
        fetch(`${Helper.backEndAddress}/api/user/email?email=${email}`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          },
          method: 'GET'
        })
          .then(response => {
            if(response.status==401){
              this.currentUser=[]
              Helper.getNewToken(this.getUserData.bind(null,email))
            } else{
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              if(response.ok){
                response.json().then(
                  res => {
                    // console.log(res)
                    this.currentUser = res
                    this.newUser = Object.assign({},res)
                  }
                )
              }
              else{
                alert(`Oops! Something wrong happened (${response.status}).`)
              }
            }            
          })
      },
      updateProfile(){
        this.editMode = true
      },
      cancelChanges(){
        this.editMode = false
      },
      saveChanges(){
        if(!this.validateInput()) return;
        let formData = new FormData()
        formData.append('user',JSON.stringify(this.newUser))
        formData.append('file',this.fileInput)
        fetch(`${Helper.backEndAddress}/api/user/${this.newUser.idUser}`, {
          method: 'PUT',
          headers: {
            Authorization: localStorage.getItem('accessToken')
          },
          body: formData
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.saveChanges.bind(null,formData))
          } else if(response.ok) {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                // console.log(res)
                this.editMode=false
                alert('Profile Updated!')
                this.currentUser = res
                this.newUser = Object.assign({},res)
              }
            )           
          } else {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            // console.log(response);
            alert(`Oops! Something wrong happened ${response.status}.`);
          }
        })
      }, 
      validateInput(){
        if(this.newUser.name === '') {
          alert('Name can\'t be null.')
          return false;
        } else if(this.newUser.phone === '') {
          alert('Please input phone number.')
          return false;
        } else if((this.newPassword!=='' && this.newPassword.length>=5) && (this.newPassword === this.repeatPassword)){
            this.newUser.password = this.newPassword      
            return true     
        } else if(this.newPassword!=='' && this.newPassword.length<=5 || (this.newPassword !== this.repeatPassword)){
            alert('please input a valid password')
            return false
        } else {
          this.newUser.password = this.currentUser.password === this.newUser.password ? "" : this.newPassword 
          return true
        }
        // return (this.email == "")? "" : (this.reg.test(this.email)) ? 'has-success' : 'has-error';
      },    
    }
  }
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
<style>
  .profileComponent {display: flex;}

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .profileBodySection {margin-top: 30px;}

  .profileTableHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    display: flex;
    justify-content: space-between;
    padding: 15px 25px;
    border-radius: 5px;
    align-items: center;
    font-weight: 600;
    width: 90%;
    margin: auto;
    position: relative;
    z-index: 1;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
  }

  .profileTableHeaderTitle {
    font-size: 20px;
    font-weight: 600;
  }

  .profileTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-4);
    text-align: center;
  }

  .profileTableBody table {width: 100%;}

  .profileTableBody tbody {
    height: 55vh;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
  }

  .profileTableBody tbody tr td {
    padding-top:6px;
    padding-bottom: 12px; 
    /* padding-bottom: 12px; */
    font-size: 16px;
    }
  .profileTableBody thead tr, .profileTableBody tbody {text-align:left;display: block; box-sizing: border-box;}
  .profileTableBody tbody td:nth-child(1){
    width: 15vw;
    text-align: left;
    padding-left: 20px;
  }
  .profileTableBody tbody td:nth-child(2), .profileTableBody thead tr th:nth-child(2) {
    width: 17vw;
    text-align: left;
    padding-left: 10px;
  }

  .profileTableBody tbody td:nth-child(3), .profileTableBody thead tr th:nth-child(3) {
    width: 17vw;
    text-align: left;
    padding-left: 10px;
  }

  .borderBottom {
    padding-bottom:10px; 
    border-bottom: solid 1px var(--primary-1) !important;

  }
  /* .profileTableBody tbody td:nth-child(6), .profileTableBody thead tr th:nth-child(6) {width: 10vw;} */

  .showMembersButton {cursor: pointer;}

  .showMembersButton:hover {
    text-decoration: underline;
    color: var(--primary-0);
  }

  .profileTabeSearch {
    outline: none;
    padding: 8px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
  }

  .profileTabeSearch::placeholder {color: var(--primary-1)}

#app .myParent .dropdownMenu .multiselect__tags,.multiselect__single, .multiselect__element{
    /* background-color: var(--lightColor); */
    font-size: 14px;
    color: var(--primary-0) ;
    border-radius: 5px;
  }

  .dropdownMenu{
    margin-left: 10px;
    position: relative;
    width: 10vw;
  }
  .dropdownMenu:hover{
    cursor:pointer;
  }
  .refreshBtn {
    background-color: #fff;
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    font-size: 14px;
    margin-left: 10px;
  }
  .refreshBtn:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
    cursor: pointer;
  }
  .refreshBtn:active {background-color: var(--primary-4);}
  .profileButton ,.cancel ,.save {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    font-size: 14px;
    margin-left: 10px;
    cursor: pointer;
  }

  .profileButton:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
  }

  .cancel:hover{
    color: #fff;
    background-color: red;
  }
  .save:hover{
    color:#fff;
    background-color: var(--primary-3);

  }
  .selectPicture {
    outline: none;
    border: none;
    padding: 10px !important;
    color: var(--primary-4);
    /* width: %; */
    /* height: 40px; */
    background-color: var(--lightColor);
    position: absolute;
    border-radius: 10px;
    /* margin-left:10px; */
    bottom:20%;
  }
  .selectPicture:hover{
    cursor: pointer;
    color:white;
    background-color: var(--primary-1);
  }
  .profilePicture {
    width:90px;
    height:100px;
    border-radius:50%;
    display:flex;
    position:absolute;
    margin-left:10px;
    /* left:1px; */
    top:52%;
    border: solid 1px var(--primary-1);
  }
  .updateInput {
    background-color: var(--lightColor);
    outline: none;
    /* padding: 5px; */
    width: 100%;
    border: solid 1px var(--primary-1);
    border-top: none;
    border-left: none;
    border-right: none;
    color: var(--primary-3);
    font-size: 16px;
    /* box-sizing: border-box; */
    border-radius: 2px;
    /* display: inline-block; */
    /* position: relative; */
    bottom: 8px;
    margin-right:-20px;

  }
  .updateInput::placeholder {
    color: var(--primary-1);
  }
  .profileButton:active {background-color: var(--primary-4);}
  .groupRow {cursor: pointer;}
  .groupRow:hover {background-color: white;}
  .groupRow:active {background-color: rgba(255, 255, 255, .5);}
   .lds-ring {
    display: inline-block;
    position: relative;
    width: 64px;
    height: 64px;
  }
  .lds-ring div {
    box-sizing: border-box;
    display: flex;
    position: absolute;
    width: 51px;
    height: 51px;
    margin: 6px;
    border: 6px solid #fff;
    border-radius: 50%;
    animation: lds-ring 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
    border-color: #fff transparent transparent transparent;
  }
  .lds-ring div:nth-child(1) {
    animation-delay: -0.45s;
  }
  .lds-ring div:nth-child(2) {
    animation-delay: -0.3s;
  }
  .lds-ring div:nth-child(3) {
    animation-delay: -0.15s;
  }
  @keyframes lds-ring {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
</style>
