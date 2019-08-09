<template>
  <div class='fixedPosition'>
    <div class='createNewExpenseWindow'>
      <div class='createNewExpenseWindowSize'>
        <div class="createNewExpenseHeader">
          <div>{{this.headerTitle}}</div>
          <div class='buttonGroup'>
            <div class="editButton" @click="editUserDetailWindow">{{editBtnName}}</div>
            <div  class="editButton"  @click="closeUserDetailWindow">{{closeBtnName}}</div>   
          </div>
        </div>

        <div class="createNewExpenseBody">
          <div>
            <div class="labelInput" >
              <label for="namaInput">Name</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: {{userDetail.name}} </div>
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Nama' v-model='newUserDetail.name'/>
            </div>
          </div>
          <div>
            <div class="labelInput" >
              <label for="emailInput">Email</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: {{userDetail.email}} </div>
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Email' v-model='newUserDetail.email'/>
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
              <input v-if="edit" class='singleLineInput inlineInput' type="text" placeholder='Period Paid' v-model='newUserDetail.totalPeriodPayed' @keypress="checkChar"/>
              <input v-if="edit" class='singleLineInput inlineInput' type="text" placeholder='Period Missed' v-model='newUserDetail.periodeTertinggal' @keypress="checkChar"/>
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
          <div v-if="edit">
            <div class="labelInput" >
              <label for="emailInput">Password</label>
            </div>
            <div class="valueInput">
              <input class='singleLineInput inlineInput' type="password" placeholder='New Password' v-model='newPassword'/>
              <input class='singleLineInput inlineInput' type="password" placeholder='Repeat Password' v-model='repeatPassword'/>
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
        newUserDetail:{},
        repeatPassword:'',
        fileInput: null,
        newPassword:'',
        groupList: [],
        reg: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/
      }
    },    
    computed:{
      editBtnName: function(){
        return this.editButtonNames[this.edit].name
      },
      closeBtnName: function(){
        return this.closeButtonNames[this.edit].name
      }
    },
    created() {
      this.edit=this.editMode
      this.add=this.addMode
      this.getAllGroup();
      this.newUserDetail = Object.assign({},this.userDetail)
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
          } else {
            alert('Sesi Anda telah berakhir, harap refresh kembali halaman ini.');
            window.location.reload();
          }
        })
        .catch(err => {
          alert('Terjadi kesalahan ketika mengambil data. Harap pastikan koneksi internet Anda tersedia.');
          console.log(err);
        })
      },
      changeGroup(e) {
        this.newUserDetail.groupName = e.target.value;
      },
      changeRole(e) {
        this.newUserDetail.role = e.target.value;
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
        if(!this.validateInput()) return;

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
            response.json().then(
              res => {
                this.newGroupDetail = res;
                alert('Succeed to add new user.');
                this.closeAddNewUserWindow();
                this.$emit('refreshData');            
                }
            )
          } else {
            console.log(response);
            alert('Oops! Something wrong happened. Please make sure the data is valid');
          }
        })
        .catch(err => {
          alert('Terjadi kesalahan. Harap periksa koneksi internet Anda.');
          console.log(err);
        })      
      },
      updateUser(jsonBody){
        if(!this.validateInput()) return;

        fetch(`${Helper.backEndAddress}/api/user/managementUser/${this.newUserDetail.idUser}`,{
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
      closeUserDetailWindow() {
        if(this.edit==false) { this.$emit('closeAddNewUserWindow'); return}
        let formData = new FormData();

        formData.append('user', JSON.stringify(this.newUserDetail))

        formData.append('file', this.fileInput);
        if(this.add==true){
          this.createNewUser(formData);return
        }
        else{
          let groupAdminChanged = this.newUserDetail.role != this.userDetail.role ? true : false 
          if(groupAdminChanged){
            if(!confirm("Are you sure to change this user's  role?")) {
              return
            }
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
          } 
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
  .createNewExpenseWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
  }

  .createNewExpenseHeader {
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

  .createNewExpenseWindowSize {
    width: 35%;
    height: fit-content;
  }

  .createNewExpenseBody {
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

  .buttonDisband {
    color: red;
    display:inline-block;
    width: 25%;
    text-align:center;
    border-radius: 10px;
    padding: 2px 0;
    font-size: 12px;    
  }
  .buttonDisband:hover{
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
    width: 49%;
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
  .selectFile {
    color: var(--primary-0);
    margin-top: 12px;
    margin-left: -15px;
    width: 70%;
  }

  .createNewExpenseFlexLine {
    display: flex;
    align-items: center;
  }
</style>
