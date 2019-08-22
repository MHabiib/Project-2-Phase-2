<template>
  <div class='fixedPosition'>
    <div class='createNewGroupWindow'>
      <div class='createNewGroupWindowSize'>
        <div class="createNewGroupHeader">
          <div>{{this.headerTitle}}<span v-if="add==false">, Total Members : {{groupMemberList.length}}</span> </div>
          <div class='buttonGroup'>
            <!-- <div class="closeButton" @click="closeGroupDetailWindow">Close</div> -->
            <div v-if='!edit' class="buttonDisband"  @click="disbandGroup">Disband Group</div>
            <div class="editButton" @click="editGroupDetailWindow">{{editBtnName}}</div>
            <div  class="editButton"  @click="closeGroupDetailWindow">{{closeBtnName}}</div>          
          </div>
        </div>
        <div class="createNewGroupBody">
          <div v-if="add==false">
            <div class="labelInput" >
              <label for="namaInput">Created Date</label>
            </div>
            <div class="valueInput">
              <div  class="value">: {{groupDetail.createdDate | dateFormatter}}</div>
            </div>
          </div>
          <div>
            <div class="labelInput" >
              <label for="namaInput">Group Name </label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: {{groupDetail.name}} </div>
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Name' v-model='newGroupDetail.name'/>
            </div>
          </div>
          <div class="myParent">
            <div class="labelInput" >
              <label for="namaInput">Group Admin </label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: {{groupDetail.groupAdmin}}</div>
              <select ref="groupAdmin" v-if="edit" name="selectAdmin" id="selectAdmin" class='selectAdmin' v-model='newGroupDetail.groupAdmin' @change="changeGroupAdmin($event)">
                <option  
                  class='selectAdminOption' value="">      
                  No Group Admin            
                </option>     
                <option  
                  class='selectAdminOption'
                  v-for='(member,index) in groupMemberList' :key='index'
                  :value='member.email'>
                    {{member.name}}
                </option>           
              </select>
            </div>
          </div>
          <div>
            <div class="labelInput" >
              <label for="namaInput">Group Bank Account</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false" class="value">: {{groupDetail.bankAccountNumber}} </div>
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Group Bank Account' v-model='newGroupDetail.bankAccountNumber'  @keypress="checkChar"/>
            </div>
          </div>
          <div>
            <div class="labelInput" >
              <label  for="namaInput">Group Current Period</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false"  class="value">: {{groupDetail.currentPeriod}}</div>
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Current Period (Default: 1)' v-model='newGroupDetail.currentPeriod' @keypress="checkChar"/>
            </div>
          </div>
          <div>
            <div class="labelInput" >
              <label  for="namaInput">Regular Payment</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false"  class="value">: Rp {{ groupDetail.regularPayment | thousandSeparators}}</div>
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Regular Payment' v-model='newGroupDetail.regularPayment' @keypress="checkChar"/>
            </div>
          </div>
          
          <div>
            <div class="labelInput" >
              <label for="namaInput">Balance </label>
            </div>
            <div class="valueInput">
              <div v-if="add==false"  v-show="edit==false"  class="value">: Rp {{groupDetail.groupBalance | thousandSeparators}}</div>
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Balance' v-model='newGroupDetail.groupBalance'  @keypress="checkChar" required/>
            </div>
          </div>
          <div>
            <div class="labelInput" >
              <label  for="namaInput">Used Balance</label>
            </div>
            <div class="valueInput">
              <div v-if="add==false" v-show="edit==false"  class="value">: Rp {{groupDetail.balanceUsed | thousandSeparators}}
              </div>            
              <input v-if="edit" class='singleLineInput' type="text" placeholder='Used Balance' v-model='newGroupDetail.balanceUsed' @keypress="checkChar" required/>
            </div>
            <!-- <div v-if='!edit' class="buttonDisband"  @click="disbandGroup">Disband Group</div> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Helper from '../../Helper';
  import Members from "../page/Members";

  export default {
    components:{
    },
    props: ['groupDetail','editMode','addMode','headerTitle'],
    data: function() {
      return {
        newGroupDetail: {},
        groupMemberList:[],
        disableButton: false,
        dStatus:null,
        namaInput:'',
        filter:'Tzuyu',
        options: ["Tzuyu","Sana","Jihyo"],
        loading:false,
        editButtonNames:{
          "false":{name:'Edit'},
          "true":{name:'Cancel'}
        },
        closeButtonNames:{
          "false":{name:'Close'},
          "true":{name:'Save'}
        },
        edit:null,
        add:null,
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
      console.log("Edit Mode : "+this.editMode)
      this.edit = this.editMode
      this.add = this.addMode
      this.newGroupDetail = Object.assign({},this.groupDetail)
      this.getMember()
    },

    methods: {
      validateInput(){
        if(this.newGroupDetail.name === '' || this.newGroupDetail.name == null) {
          alert('Group name can\'t be null.')
          return false
        }  else if(this.newGroupDetail.bankAccountNumber === '' || this.newGroupDetail.bankAccountNumber == null) {
          alert('Please insert a valid group\'s bank account number')
          return false
        } else {
          if(this.newGroupDetail.groupCurrentPeriod === '' || this.newGroupDetail.groupCurrentPeriod == null) {
              this.newGroupDetail.groupCurrentPeriod=1
          } if(this.newGroupDetail.groupBalance === '' || this.newGroupDetail.groupBalance == null) {
              this.newGroupDetail.groupBalance=0.0
          } if(this.newGroupDetail.regularPayment === '' || this.newGroupDetail.regularPayment == null) {
              this.newGroupDetail.regularPayment=0.0
          }
            return true
          }
      },
      updateGroup(jsonBody){
        fetch(`${Helper.backEndAddress}/api/group/${this.newGroupDetail.idGroup}`,{
          headers: {
            'Content-Type' : 'application/json',
            Authorization: localStorage.getItem('accessToken')},
          method:'PUT',
          body:jsonBody
        }).then(response => {
            if(response.status==401){
              console.log('groupId : '+this.groupId)
              Helper.getNewToken(this.updateGroup.bind(null,jsonBody))
            } else {
                localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
                if(response.ok){
                  response.json().then(
                    res => {
                      this.newGroupDetail = res;
                      alert('group updated!')
                      this.$emit('updateGroupDetailWindow');
                    }
                  )
                }
            }
        })
      },
      disbandGroup(){
        if(this.add) return
        let isConfirmed = confirm('Are you sure to disband this group?\nDisbanding group can\'t be undo.')
        if(!isConfirmed) return;
        fetch(`${Helper.backEndAddress}/api/group/${this.newGroupDetail.idGroup}`,{
          headers: {
            'Content-Type' : 'application/json',
            Authorization: localStorage.getItem('accessToken')},
          method:'DELETE',
        }).then(response => {
            if(response.status==401){
              console.log('groupId : '+this.groupId)
              Helper.getNewToken(this.disbandGroup)
            } else if(response.ok){
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              this.$emit('refreshData')
              alert(`Group successfuly disbanded.\nAny last information regarding group ${this.newGroupDetail.name} has been sent to every member\'s email.`)
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
      createNewGroup(jsonBody){
        fetch(`${Helper.backEndAddress}/api/group`,{
          headers: {
            'Content-Type' : 'application/json',
            Authorization: localStorage.getItem('accessToken')},
          method:'POST',
          body:jsonBody
        }).then(response => {
            if(response.status==401){
              console.log('groupId : '+this.groupId)
              Helper.getNewToken(this.createNewGroup.bind(null,jsonBody))
            } else {
                localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
                if(response.ok){
                  response.json().then(
                    res => {
                      this.newGroupDetail = res;
                      alert('group created!')
                      this.$emit('updateGroupDetailWindow');
                    }
                  )
                }
                else{
                  alert(`Oops! Something wrong happened (${response.status}).`)
                }
            }
        })
      },
      closeGroupDetailWindow() {
        if(this.edit==false) { this.$emit('closeGroupDetailWindow'); return}
        if(!this.validateInput()) return;
        let jsonBody = JSON.stringify(this.newGroupDetail);
        if(this.add==true){
          this.createNewGroup(jsonBody);return
        }
        else{
          let nameChanged = this.newGroupDetail.name != this.groupDetail.name ? true : false 
          if(nameChanged){
            if(!confirm("Changing group name will reset all group's latest activity\nAre you sure to change the group's name?"))            {
              return
            }
          }
          this.updateGroup(jsonBody)

        }//else end here
      },
      editGroupDetailWindow() {
        if(this.addMode) {this.$emit('closeGroupDetailWindow'); return}
        this.edit = !this.edit;
      },      
      changeGroupAdmin(e){
        this.newGroupDetail.groupAdmin = e.target.value;
      },
      getMember() {
        let gName = this.newGroupDetail.name==undefined ? "GROUP_LESS":this.newGroupDetail.name
        console.log("GroupName : "+this.newGroupDetail.name)
        fetch(`${Helper.backEndAddress}/api/group/${gName}/members`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          },
          method: 'GET'
        })
          .then(response => {
            if(response.status==401){
              this.groupMemberList=[]
              Helper.getNewToken(this.getMember)
            }
            else if(response.ok){
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              response.json().then(
                res => {
                  console.log(res)
                  this.groupMemberList = res
                  setTimeout(e=>{this.loading=false},500)
                }
              )
            }
          })
      },    
      checkChar(e) {
        if(e.keyCode >= 48 && e.keyCode <= 57 || e.keyCode === 8) {} else {
          e.preventDefault();
        }
      },

    },
    
  }
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style>
  .fixedPosition {
    position: absolute;
  }
  .red {
    color: RED;
  }
  .createNewGroupWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
  }

  .createNewGroupHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    padding: 16px 20px;
    box-sizing: border-box;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    width: 95%;
    margin: auto;
    position: relative;
    z-index: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 600;
  }

  .createNewGroupWindowSize {
    width: 40%;
    height: fit-content;
  }

  .createNewGroupBody {
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
    font-weight: 400;
    margin-left: 10px;
  }

  .buttonGroup {
    display: flex;
    align-items: center;
  }

  .buttonDisband {
    color: #fff;
    display:inline-block;
    /* width: 25%; */
    text-align:center;
    font-weight:400;
    border-radius: 10px;
    padding: 7px;
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
    text-align: right;
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
