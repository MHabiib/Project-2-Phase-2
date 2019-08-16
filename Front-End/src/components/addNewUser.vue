<template>
  <div class='fixedPosition'>
    <div class='createNewUserWindow'>
      <div class='createNewUserWindowSize'>
        <div class="createNewUserHeader">
          <div>{{this.headerTitle}}</div>
          <div class='buttonGroup'>
            <div v-if='!edit' class="buttonDisbandUser"  @click="deleteUser(userDetail.idUser)">Resign User</div>
            <div class="editButton" @click="editUserDetailWindow">{{editBtnName}}</div>
            <div  class="editButton"  @click="closeUserDetailWindow">{{closeBtnName}}</div>   
          </div>
        </div>

        <div class="createNewUserBody">
          <div v-if="add==false">
            <div class="labelInput" >
              <label for="namaInput">Created Date</label>
            </div>
            <div class="valueInput">
              <div  class="value">: {{userDetail.joinDate | dateFormatter}}</div>
            </div>
          </div>
          <div>
            <div class="labelInput" >
              <label for="namaInput">Name</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value pp">: {{userDetail.name}} </div>
              <div v-if="add==false" v-show="!edit" class="value pp" style="width:20%;display:">
                  <!-- <img v-if="!ppAvailable" src="../assets/profile.png" alt="pp" class="profilePicture"> -->
                  <img v-if="ppAvailable" :src="userDetail.imageURL" alt="pp" class="profilePicture">
              </div>
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Name' v-model='newUserDetail.name'/>
            </div>
          </div>
          <div>
            <div class="labelInput" >
              <label for="emailInput" >Email</label>
            </div>
            <div  class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: {{userDetail.email}} </div>
              <input v-if="edit && add" class='singleLineInput' type="text" placeholder='Email' v-model='newUserDetail.email'/>
            </div>
          </div>
          <div>
            <div class="labelInput" >
              <label for="emailInput">Phone</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: {{userDetail.phone}} </div>
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Phone Number' v-model='newUserDetail.phone' @keypress="checkChar"/>
            </div>
          </div>
          <div >
            <div class="labelInput" >
              <label for="emailInput">Balance / Contribution</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: <span class="inlineInput">Rp {{parseFloat(userDetail.balance).toFixed(2) | thousandSeparators}}</span>/ Rp {{parseFloat(userDetail.balanceUsed).toFixed(2) | thousandSeparators}}</div>
              <input v-if="edit" class='singleLineInput inlineInput' type="text" placeholder='Balance' v-model='newUserDetail.balance' @keypress="checkChar"/>
              <input v-if="edit" class='singleLineInput inlineInput' type="text" placeholder='Contribution' v-model='newUserDetail.balanceUsed' @keypress="checkChar"/>
            </div>
          </div>
          <div >
            <div class="labelInput" >
              <label for="emailInput">Period Paid / Missed</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: <span class="inlineInput">{{userDetail.totalPeriodPayed}}</span>/ {{userDetail.periodeTertinggal>0?userDetail.periodeTertinggal:0}}</div>
              <input v-if="edit" class='singleLineInput inlineInput' type="number" placeholder='Period Paid' v-model='newUserDetail.totalPeriodPayed' @keypress="checkChar"/>
              <input v-if="edit" class='singleLineInput inlineInput' type="number" placeholder='Period Missed' v-model='newUserDetail.periodeTertinggal' @keypress="checkChar"/>
            </div>
          </div>
          <div >
            <div class="labelInput" >
              <label for="emailInput">Group Name / Role</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: <span class="inlineInput"> {{userDetail.groupName}}</span>/ {{userDetail.role}}</div>
                <select v-if="edit" name="selectGroup" id="selectGroup" v-model="newUserDetail.groupName" class='selectGroup' @change="changeGroup($event)">
                  <option value="" >No Group</option>
                  <option v-for="(group, index) in groupList" :key="index" :value="group.name">{{group.name}}</option>
                </select>
                <select v-if="edit" name="selectRole" id="selectRole" v-model="newUserDetail.role" class='selectRole' @change="changeRole($event)">
                  <!-- <option value="">Select Role</option> -->
                  <option value="MEMBER">Member</option>
                  <option value="GROUP_ADMIN">Group Admin</option>
                  <option class="sa" value="SUPER_ADMIN">Super Admin</option>
                </select>
            </div>
          </div>
          <div  v-if="groupAdminGroupChanged || groupAdminChanged" >
            <!-- <div v-if="add==false && groupMemberList.length == 0" style="text-align:center" >
              <div v-if='edit' class="buttonDisbandGroup"  @click="deleteUser(userDetail.idUser)">Disband Group</div>
            </div> -->
            <div v-if="add==false && groupMemberList.length > 0">
              <div class="labelInput" >
                <label for="emailInput">New Group Admin</label>
              </div>
              <div class="valueInput">
                <div v-if="add==false" v-show="edit==false" class="value">: <span class="inlineInput"> {{userDetail.groupName}}</span>/ {{userDetail.role}}</div>
                <select v-if="edit" name="selectAdmin" id="selectAdmin" class='selectAdmin' v-model='newGroupAdmin' @change="changeGroupAdmin($event)">  
                  <option  
                    class='selectAdminOption'
                    v-for='(member,index) in groupMemberList' :key='index'
                    :value='member.email'>
                      {{member.name}}
                  </option>  
                </select>    
              </div>
            </div>
          </div>
          <div v-if="edit">
            <div class="labelInput" >
              <label for="emailInput">Password</label>
            </div>
            <div class="valueInput">
              <input class='singleLineInput inlineInput' type="password" placeholder='New Password' v-model='newPassword'/>
              <input class='singleLineInput inlineInput' type="password" placeholder='Repeat Password' v-model='repeatPassword'/>
            </div>
          </div>
          <div v-if="add" class="fileInput" >
            <div class="labelInput" >
              <label for="emailInput">Profile Picture</label>
            </div>
            <div  class="valueInput">
              <input 
                accept="image/*" 
                type="file" 
                @change="changeFile($event)" 
                class="selectFile"
                placeholder="Image file"/>
            </div>            
          </div>
        </div>     
      </div>
    </div>
  </div>
</template>

<script>
  import Helper from '../../Helper';

  export default {
    props: ['userDetail','editMode','addMode','headerTitle'],
    data: function() {
      return {
        editButtonNames:{
          "false":{name:'Edit'},
          "true":{name:'Cancel'}
        },
        closeButtonNames:{
          "false":{name:'Close'},
          "true":{name:'Save'}
        },
        edit:false,
        add:false,
        updateProfile:false,
        newUserDetail:{},
        fileInput: null,
        newPassword:'',
        repeatPassword:'',
        groupList: [],
        groupMemberList:[],
        groupAdminGroupChanged : false,
        groupAdminChanged: false,
        newGroupAdmin:'',
        reg: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/
      }
    },    
    computed:{
      editBtnName: function(){
        return this.editButtonNames[this.edit].name
      },
      closeBtnName: function(){
        return this.closeButtonNames[this.edit].name
      },
      ppAvailable(){
        if(this.newUserDetail.imageURL==='' || this.newUserDetail.imageURL== null || this.newUserDetail.imageURL=="null"){
          return false
        }
        return true
      }
    },
    created() {
      this.newUserDetail = Object.assign({},this.userDetail)
      this.edit=this.editMode
      this.add=this.addMode
      this.getAllGroup();
      if(!this.add) this.getMember();
      console.log(this.groupMemberList.length)
    },
    watch:{
      groupAdminGroupChanged: function(oldVal,newVal){
        this.groupAdminGroupChanged = this.newUserDetail.groupName === this.userDetail.groupName ? false : this.userDetail.role === 'GROUP_ADMIN'&& this.userDetail.groupName !== 'GROUP_LESS'
      },
      groupAdminChanged: function(oldVal,newVal){
        this.groupAdminChanged =  this.userDetail.role === 'GROUP_ADMIN' && this.userDetail.groupName !== 'GROUP_LESS'? this.newUserDetail.role !== this.userDetail.role : false
      }
    },
    methods: {
      closeAddNewUserWindow() {
        this.$emit('closeAddNewUserWindow');
      },
      checkChar(e) {
        if(e.keyCode >= 48 && e.keyCode <= 57 || e.keyCode === 8) {} else {
          e.preventDefault();
        }
      },
      getAllGroup() {
        fetch(`${Helper.backEndAddress}/api/group`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.getAllGroup)
          }
          else if(response.ok) {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                this.groupList = res;
              }
            )
            if(!this.add) this.getMember();

          } else {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            alert('Oops! Something wrong happened.');
          }
        })
        .catch(err => {
          alert('Terjadi kesalahan ketika mengambil data. Harap pastikan koneksi internet Anda tersedia.');
          console.log(err);
        })
      },
      getMember() {
        // let gName = this.newGroupDetail.name==undefined ? "GROUP_LESS":this.newGroupDetail.name
        // console.log("GroupName : "+this.userDetail.groupName)
        fetch(`${Helper.backEndAddress}/api/group/${this.userDetail.groupName}/members`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          },
          method: 'GET'
        })
          .then(response => {
            if(response.status==401){
              this.groupMemberList=[]
              Helper.getNewToken(this.getMember)
            } else{
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              if(response.ok){
                response.json().then(
                  res => {
                    // someArray = someArray.filter(x => x.name !== 'Kristian')
                    let array = res
                    array = array.filter(user => user.email !== this.userDetail.email)
                    console.log('Calon Admin Baru : '+array)
                    this.groupMemberList = array
                  }
                )
              }
            }            
          })
      },
      changeGroup(e) {
        this.newUserDetail.groupName = e.target.value;
        this.groupAdminGroupChanged = !this.groupAdminGroupChanged;
      },
      changeGroupAdmin(e){
        this.newGroupAdmin= e.target.value;
      },
      changeRole(e) {
        this.newUserDetail.role = e.target.value;
        this.groupAdminChanged = !this.groupAdminChanged;

      },
      changeFile(e) {
         if(e.target.files[0].size > 5000000) {
          alert('Max Image size is 5 MB. Please upload a lower resolution image.');
          this.fileInput = null;
          e.target.value = null;
        } else {
        this.fileInput = e.target.files[0];
        }
      },
      createNewUser(formData) { 
        // if(!this.validateInput()) return;

        fetch(`${Helper.backEndAddress}/api/user`, {
          method: 'POST',
          headers: {
            Authorization: localStorage.getItem('accessToken')
          },
          body: formData
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.createNewUser.bind(null,formData))
          } else if(response.ok) {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            alert('Succeed to add new user.');
            this.closeAddNewUserWindow();
            this.$emit('refreshData');       
          } else {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            console.log(response);
            alert(`Oops! Something wrong happened ${response.status}`);
          }
        })
        // .catch(err => {
        //   alert('Terjadi kesalahan. Harap periksa koneksi internet Anda.');
        //   console.log(err);
        // })      
      },
      updateUser(jsonBody){
        // if(!this.validateInput()) return;
        fetch(`${Helper.backEndAddress}/api/user/managementUser/${this.newUserDetail.idUser}?newGroupAdmin=${this.newGroupAdmin}`,{
          headers: {
            'Content-Type':'application/json',
            Authorization: localStorage.getItem('accessToken')},
          method:'PUT',
          body:jsonBody
        }).then(response => {
            if(response.status==401){
              console.log('groupId : '+this.groupId)
              Helper.getNewToken(this.updateUser.bind(null,jsonBody))
            } else {
                localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
                if(response.ok){
                  response.json().then(
                    res => {
                      this.newUserDetail = res;
                      alert('user updated!')
                      this.closeAddNewUserWindow();
                      this.$emit('refreshData');               
                      }
                  )
                }
                if(response.status==500){response.json().then( res => {
                  alert(res.message)
                  })
                }
            }
        })
      },
      deleteUser(idUser){
        if(this.add) return
        let isConfirmed = confirm('Are you sure to resign this user?\nResigning user can\'t be undo.')
        if(!isConfirmed) return;
        fetch(`${Helper.backEndAddress}/api/user/${idUser}`, {
          method: 'DELETE',
          headers: {
            Authorization: localStorage.getItem('accessToken')
          },
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.deleteUser.bind(null,idUser))
          } else if(response.ok) {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            alert(`User resigned succeed.Any last information regarding ${this.newUserDetail.name} has been sent to ${this.newUserDetail.email}`);
            this.closeAddNewUserWindow()
            this.$emit('refreshData')                 
          } else if(response.status==500){
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then( res => {
                alert(res.message)
              })
            }
        })
        .catch(err => {
          alert('Terjadi kesalahan. Harap periksa koneksi internet Anda.');
          console.log(err);
        })      
      },
      disbandPreviousGroup(){
        let previousGroup = this.groupList.find( x => x.name === this.userDetail.groupName)
        console.log("Previous Group Name / Id: "+ previousGroup.name + " / " + previousGroup.idGroup)
        if(this.add) return
        let isConfirmed = confirm('Are you sure to disband this group?\nDisbanding group can\'t be undo.')
        if(!isConfirmed) return;
        fetch(`${Helper.backEndAddress}/api/group/${previousGroup.idGroup}`,{
          headers: {
            'Content-Type' : 'application/json',
            Authorization: localStorage.getItem('accessToken')},
          method:'DELETE',
        }).then(response => {
            if(response.status==401){
              console.log('groupId : '+this.groupId)
              Helper.getNewToken(this.disbandPreviousGroup)
            } else if(response.ok){
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              this.updateUser(JSON.stringify(this.newUserDetail)) // now change the user group after disband group succeed
              this.$emit('refreshData')
              alert(`Group successfuly disbanded.\nAny last information regarding group ${this.previousGroup.name} has been sent to every member\'s email.`)
            } else if(response.status == 500){
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              response.json().then(res=>{
                let message = ''
                res.forEach((element,index) => {
                  // message+=index+1+". " +element.name+"\n"
                  message+=`${index+1}. ${element.name} (${element.periodeTertinggal} period(s))\n`
                });
                alert(`Failed to disband group, ${res.length} member(s) haven't completed their payment\n${message}`)
              })
            }
        })
      },
      closeUserDetailWindow() { // CLOSE BUTTON FUNCTION CALLED
        if(this.edit==false) { this.$emit('closeAddNewUserWindow'); return}
        if(!this.validateInput()) return;
        let formData = new FormData();
        formData.append('user', JSON.stringify(this.newUserDetail))
        formData.append('file', this.fileInput);

        if(this.add==true){
          this.createNewUser(formData);return
        }
        else{
          let groupAdminChanged = this.newUserDetail.role != this.userDetail.role ? this.newUserDetail.role === "GROUP_ADMIN" : false 
          if(groupAdminChanged){
            if(!confirm("Are you sure to change this user's  role? *previous group admin will be demoted to member.")) {
              return
            }
          }
          if(this.userDetail.groupName !== 'GROUP_LESS' && this.newUserDetail.groupName!==this.userDetail.groupName && this.groupMemberList.length==0){
            if(confirm(`Are you sure to change this user's  group? *this action will disband group ${this.userDetail.groupName} because ${this.userDetail.groupName} will have 0 member`)) {
              this.disbandPreviousGroup()
            }
            return 
          }
          this.updateUser(JSON.stringify(this.newUserDetail))
        }//else end here
      },
      editUserDetailWindow() {
        if(this.addMode) {this.$emit('closeAddNewUserWindow'); return}
        this.edit = !this.edit;
      }, 
      validateInput(){
        
        let isEmailValid = this.reg.test(this.newUserDetail.email)
        if(this.add){
          if(this.newPassword!==this.repeatPassword || this.newPassword==='') {
            alert('Please input the password again.')
            return false;
          } 
          this.newUserDetail.password=this.newPassword
        }
        if(this.newUserDetail.name === '') {
          alert('Name can\'t be null.')
          return false;
        } else if(this.newUserDetail.email === '' || isEmailValid==false) {
          alert('Please input valid email.')
          return false;
        } else if(this.newUserDetail.phone === '') {
          alert('Please input phone number.')
          return false;
        // } else if((this.groupAdminGroupChanged === true || this.groupAdminChanged === true)  && this.newGroupAdmin ==='') {
        } else if(((this.groupAdminGroupChanged === true || this.groupAdminChanged === true)  && this.newGroupAdmin ==='') && this.groupMemberList.length>0) {
          alert('Please input new Group Admin.')
          return false;
        } else {
          if(this.newUserDetail.balance === ''){
            this.newUserDetail.balance = 0
          } if(this.newUserDetail.contribution === ''){
            this.newUserDetail.contribution = 0
          } if(this.newUserDetail.totalPeriodPayed === ''){
            this.newUserDetail.totalPeriodPayed = 0
          } if(this.newUserDetail.periodeTertinggal === ''){
            this.newUserDetail.periodeTertinggal = 0
          } if(this.newUserDetail.role === ''){
            this.newUserDetail.role = 'MEMBER'           
          } else if((this.newPassword!=='' && this.newPassword.length>=5) && (this.newPassword === this.repeatPassword)){
            this.newUserDetail.password = this.newPassword          
          } else if(this.newPassword!=='' && this.newPassword.length<=5 || (this.newPassword !== this.repeatPassword)){
            alert('please input a valid password')
            return false
          }
          this.newUserDetail.password = this.userDetail.password == this.newUserDetail.password ? "" : this.newPassword 
          return true
        }
        // return (this.email == "")? "" : (this.reg.test(this.email)) ? 'has-success' : 'has-error';
      },
    },
  }
</script>

<style>
  .fixedPosition {
    position: absolute;
  }
  .sa {
    font-weight: 800;
  }
  .createNewUserWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
  }

  .createNewUserHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    padding: 16px 12px;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    width: 90%;
    margin: auto;
    position: relative;
    z-index: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 600;
  }

  .createNewUserWindowSize {
    width: 40%;
    height: fit-content;
  }

  .createNewUserBody {
    background-color: white;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
    position: relative;
    top: -30px;
    padding: 20px;
    padding-top: 32px;
    height: 100%;
  }

.closeButton {
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
  }
  .editButton{
    font-size: 12px;
    padding: 7px; 
    /* color: var(--primary-0); */
    border-radius: 50%;
    cursor: pointer;
    font-weight: 400;
    margin-left: 10px;
  }
  .editButton:hover{
    color: pointer;
    background-color: #fff;  
    color: var(--primary-0);
  }

  .okButton {
    background-color: #fff;
    padding: 7px;
    color: var(--primary-0);
    border-radius: 50%;
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
    margin-left: 10px;
  }

  .buttonGroup {
    display: flex;
    align-items: center;
  }

  .buttonDisbandGroup, .buttonDisbandUser {
    color: red;
    display:inline-block;
    /* width: 25%; */
    text-align:center;
    font-weight:400;
    border-radius: 10px;
    padding: 7px;
    font-size: 12px;    
  }
  .buttonDisbandUser{
    color: white !important;
  }
  .buttonDisbandGroup {
    display:inline-block;
    /* width: 25%; */
    margin-top:10px;
    text-align:center;
    font-weight:400;
    border-radius: 10px;
    padding: 7px;
    font-size: 12px;    
  }
  .buttonDisbandGroup:hover, .buttonDisbandUser:hover{
    cursor: pointer;
    color: pointer;
    background-color: red;  
    color: white;
  }
  .labelInput{
    text-align: left;
    margin-left: 10px;
    display: inline-block;
    color: var(--primary-4);
    width: 30%;
    font-size: 14px;
  }
  .valueInput{
    text-align: left;
    display: inline-block;
    width: 66%;
  }
  .singleLineInput {
    outline: none;
    padding: 5px;
    width: 100%;
    border: solid 1px var(--primary-1);
    border-top: none;
    border-left: none;
    border-right: none;
    color: var(--primary-2);
    margin-top: 15px;
    box-sizing: border-box;
    border-radius: 2px;
  }
  .inlineInput{
    width: 49% !important;
    display: inline-block;
  }

  .value{
    text-align: left;
    font-size:14px;
    margin-top: 15px;
    width: 100%;
    outline: none;
    padding: 5px;
    color: var(--primary-4);
  }
  .pp{
    text-align: left;
    width: 45% !important;
    display: inline-block;
  }
  .profilePicture {
    width:100px;
    height:120px;
    border-radius:50%;
    display:flex;
    position:absolute;
    right:10%;
    top:20%;
    /* border: solid 2px var(--primary-1); */
  }
  .singleLineInput::placeholder {
    color: var(--primary-1);
  }
  
  .selectGroup, .selectRole {
    outline: none;
    border: none;
    padding: 10px !important;
    color: var(--primary-4);
    width: 49%;
    height: 40px;
    background-color: var(--lightColor);
    border-radius: 5px;
    margin-top: 12px;
    /* margin-right: 12px; */
  }
  .selectGroup:hover, .selectRole:hover {
    cursor: pointer;
  }
  .fileInput{
      text-align: center;
  }
  .selectFile {
    outline: none;
    border: none;
    padding: 10px !important;
    color: var(--primary-4);
    /* width: %; */
    /* height: 40px; */
    background-color: var(--lightColor);
    margin-top: 15px;
    margin-right: 12px;
    border-radius: 10px;
  }

  .selectFile:hover{
    cursor: pointer;
    color:white;
    background-color: var(--primary-1);
  }
  .selectFileOption{
    padding: 10px;
    color: var(--primary-3);
  }
  .createNewUserFlexLine {
    display: flex;
    align-items: center;
  }
  .selectAdmin {
    outline: none;
    border: none;
    padding: 10px !important;
    color: var(--primary-4);
    width: 100%;
    /* height: 40px; */
    background-color: var(--lightColor);
    margin-top: 15px;
    margin-right: 12px;
    border-radius: 5px;
  }

  .selectAdmin:hover, .selectAdminOption:hover{
    cursor: pointer;
  }
  .selectAdminOption{
    padding: 10px;
    color: var(--primary-3);
  }
</style>
