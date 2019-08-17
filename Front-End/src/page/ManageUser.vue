<template>
  <div class='manageUserComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Manage User'/>

      <div class="managementTable">
        <div class="managementTableHead">
          <div class="managementTableHeadTitle">
            All User
          </div>

          <div class="myParent" style='display: flex;'>
            <input class='managementTableHeadSearch' type="text" :placeholder="'Search by '+ searchPlaceHolder " v-model='searchQuery'/>
            <div   class="dropdownMenu" >
                <multiselect 
                  v-model="filter" 
                  :allow-empty="false" 
                  :options="options" 
                  :searchable="false" 
                  :close-on-select="true" 
                  :show-labels="false" 
                  placeholder="Pick a value">
                </multiselect>
            </div>    
            <div class="refreshBtn" @click='searchData(0)'>
              <!-- Refresh -->
              <img src="../assets/sinchronize-256.png" width="16px" alt="Refresh">
            </div>
            <div class="refreshBtn" @click='createUser'>Add New User</div>

          </div>
        </div>

        <div class="managementTableBody">
          <table>
            <thead>
              <tr>
                <th>&nbsp;&nbsp;</th>
                <th>Group</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
              </tr>
            </thead>

            <tbody id='infiniteScroll'>
              <tr v-for='(user, index) in memberList' :key='index' class="userRow" @click="editUser(user)">
                <td>{{index+1}}</td>
                <td>{{user.groupName}}</td>
                <td>{{user.name}}</td>
                <td>{{user.email}}</td>
                <td>{{user.phone}}</td>
                <td>{{user.role}}</td>
              </tr>
              <div style="text-align:center">
                <div v-show="loading" class="lds-ring"><div></div><div></div><div></div><div></div></div>
              </div>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <AddNewUserWindow
      v-if='showAddNewUserWindow'
      @closeAddNewUserWindow='closeAddNewUserWindow'
      @refreshData="searchData(0)"
      :headerTitle = this.headerTitle
      :editMode="this.editMode"
      :addMode = this.addMode
      :userDetail="this.userDetailSelected"
    />
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';
  import {backEndAddress} from '../../Helper';
  import AddNewUserWindow from '../components/addNewUser';
  import Multiselect from 'vue-multiselect';
  import Helper from '../../Helper';

  export default {
    components: {
      'AddNewUserWindow': AddNewUserWindow,
      Multiselect
    },
    data: function() {
      return {
        memberList:[],
        headerTitle:'',
        dataMember:{},
        searchQuery: '',
        loading:false,
        showAddNewUserWindow: false,
        searchQuery: '',
        editMode: false,
        addMode: false,
        userDetailSelected: '',
        groupName:'',
        filter:'name',
        options:['name','email','role','group']
      }
    },
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }, 
      searchPlaceHolder:function(){
        return this.filter == "date before"  || this.filter == "date after"  ? "(dd-MMMM-yyyy)" : this.filter
      }
    },
    created() {
      // this.getAllUsers();
      this.searchData(0)
      this.groupName=localStorage.getItem('groupName')

    },
    mounted(){
      this.scroll();
    },
    watch:{
      filter : function (newQuery, oldQuery) {
        this.searchData(0)  
      },
      searchQuery: function(oldVal,newVal){
        this.searchData(0)
      }
    },
    methods: {
      searchData(page){
        this.loading=true
        console.log(this.filter)
        // fetch(`${Helper.backEndAddress}/api/user/search?${this.filter}=${this.searchQuery}&page=${page}`, {
        fetch(`${Helper.backEndAddress}/api/user/search?query=${this.filter}:${this.searchQuery}&page=${page}`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            this.userList=[]
            Helper.getNewToken(this.searchData.bind(null,0))
          } else if(response.status==403){
            Helper.getNewToken()
            alert('403 : Unauthorized access.')
            this.$router.push('/dashboard')
          } else if(response.ok){
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                this.memberList=res.content 
                this.dataMember=res
                setTimeout(e=>{this.loading=false},500)
                // this.loading=false
              }
            )
          }
        })
      },
      getMembersData(page) {
        this.loading=true
        // fetch(`${Helper.backEndAddress}/api/user/search?${this.filter}=${this.searchQuery}&page=${page}`, {    
        fetch(`${Helper.backEndAddress}/api/user/search?query=${this.filter}:${this.searchQuery}&page=${page}`, {              
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            // this.memberList.length=0
            Helper.getNewToken(this.getMembersData.bind(null,this.dataMember.pageable.pageNumber+1))
          }
          else{
            localStorage.setItem('accessToken','Token '+ response.headers.get('Authorization'))
            response.json().then(
              res => {
                this.memberList=this.memberList.concat(res.content)
                this.dataMember=res      
                setTimeout(e=>{this.loading=false},500)
              }
            )}
        })
      },
      closeAddNewUserWindow() {
        this.showAddNewUserWindow = false;
      },
      filterData(newQuery) {
        let dataFiltered = [];
        const queryBaru = newQuery.toString().toLowerCase();

        this.dataUser.forEach(element => {
          const groupElement = element.groupName.toString().toLowerCase();
          const nameElement = element.name.toString().toLowerCase();
          const emailElement = element.email.toString().toLowerCase();
          const phoneElement = element.phone.toString();
          const roleElement = element.role.toString().toLowerCase();

          if(
            groupElement.includes(queryBaru) ||
            nameElement.includes(queryBaru) ||
            emailElement.includes(queryBaru) ||
            phoneElement.includes(queryBaru) ||
            roleElement.includes(queryBaru)
          ) {
            dataFiltered.push(element)
          }
        })

        this.dataUserShown = dataFiltered;
        const e = document.getElementById('infiniteScroll');
        if (e.scrollHeight <= e.clientHeight) {
          console.log('Infinite Triggered')
        }
      },
      editUser(userDetail) {
        this.headerTitle = `User Id : ${userDetail.idUser}`;
        this.editMode = false;
        this.userDetailSelected = userDetail;
        this.addMode = false;
        this.showAddNewUserWindow = true;
      },
      createUser() {
        this.userDetailSelected={}
        this.editMode = true;
        this.addMode = true;
        this.showAddNewUserWindow = true;
        this.headerTitle = `Add New User`;

      },
      scroll() {
        const paymentTable = document.querySelector('#infiniteScroll');
        paymentTable.addEventListener('scroll', e => {
          // console.log(paymentTable.scrollTop + paymentTable.clientHeight+" : "+paymentTable.scrollHeight)
          if((paymentTable.scrollTop + paymentTable.clientHeight)+1>= paymentTable.scrollHeight) {
            console.log('infinite scroll triggered!')
            if(this.dataMember.last!=true & this.loading==false){   
              this.loading=true;
              this.getMembersData(this.dataMember.pageable.pageNumber+1)
              console.log('get more data!')
            }
          }
        })
      },
    },
  }
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style>
  .manageUserComponent {
    display: flex;
  }

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .managementTable {
    margin-top: 20px;
    height: 80vh;

  }

  .managementTableHead {
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

  .managementTableHeadTitle {
    font-size: 20px;
    font-weight: 600;
  }

  .managementTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-4);
    text-align: center;
  }
  
  .managementTableHeadSearch {
    outline: none;
    padding: 12px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
  }

  .managementTableHeadSearch::placeholder {
    color: var(--primary-1)
  }

  .managementTableHeadSort {
    outline: none;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
    width: 100px;
    height: 29px;
  }


  .managementTableHeadAddNew {
    background-color: #fff;
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    cursor: pointer;
    font-size: 14px;
    margin-left: 10px;
  }

  .managementTableHeadAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
  }

  .managementTableHeadAddNew:active {
    background-color: var(--primary-4);
  }

  .managementTableBody table {
    width: 100%;
    text-align: left;
  }

  .managementTableBody tbody {
    height: 55vh;
    /* overflow-y: auto;
    overflow-x: hidden; */
    overflow : auto;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
    line-height: 42px;
  }
  .managementTableBody tbody#infiniteScroll {
    max-height:400px
  }
  .manageUserComponent .managementTableBody thead tr, .manageUserComponent .managementTableBody tbody { display: block; box-sizing: border-box; }
  .manageUserComponent .managementTableBody tbody td:nth-child(1), .manageUserComponent .managementTableBody thead tr th:nth-child(1) { width: 1.5vw; padding-left: 12px}
  .manageUserComponent .managementTableBody tbody td:nth-child(2), .manageUserComponent .managementTableBody thead tr th:nth-child(2) { width: 10vw; padding-left: 12px}
  .manageUserComponent .managementTableBody tbody td:nth-child(3), .manageUserComponent .managementTableBody thead tr th:nth-child(3) { width: 15vw; padding-left: 12px}
  .manageUserComponent .managementTableBody tbody td:nth-child(4), .manageUserComponent .managementTableBody thead tr th:nth-child(4) { width: 18vw; padding-left: 12px}
  .manageUserComponent .managementTableBody tbody td:nth-child(5), .manageUserComponent .managementTableBody thead tr th:nth-child(5) { width: 12vw; padding-left: 12px}
  .manageUserComponent .managementTableBody tbody td:nth-child(6), .manageUserComponent .managementTableBody thead tr th:nth-child(6) { width: 5vw; text-align: 'center'; padding-left: 12px }

  .userRow:hover {
    background-color: white;
    cursor: pointer;
  }

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
