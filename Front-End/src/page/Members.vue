<template>
  <div class='membersComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection :headerTitle="'Group '+groupName+'\'s Members'"/>

      <div class="membersBodySection">
        <div class="membersTableHeader">
          <div class="membersTableHeaderTitle">
            All Members
          </div>

         <div class="myParent" style='display: flex;'>
            <input class='membersTableSearch' type="text" :placeholder="'Query by '+filter " v-model='searchQuery'/>
            <!-- <div class="refreshBtn"  @click='searchData(0)'>
              <img src="../assets/magnifier.png" width="18px" alt="Search">
            </div> -->          
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
         </div>
        </div>

        <div class="membersTableBody">
          <table>
            <thead>
              <tr>
                <th>&nbsp;&nbsp;</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Join Date</th>
              </tr>
            </thead>
            <tbody id='infiniteScroll'>
              <tr v-for="(member, index) in memberList" :key="index">
                <td>{{index+1}}.</td>
                <td>{{member.name}}</td>
                <td>{{member.email}}</td>
                <td>{{member.phone}}</td>
                <td>{{member.role}}</td>
                <td>{{member.joinDate | dateFormatter}}</td>
              </tr>
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
  import Multiselect from 'vue-multiselect'
  export default {
    components:{
      Multiselect
    },
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    },
    data: function() {
      return {
        memberList:[],
        dataMember:{},
        searchQuery: '',
        showInviteMemberWindow: false,
        loading:false,
        groupName:localStorage.groupName,       
        filter:'name',
        options:['name','email','role']
      }
    },
    created() {
      this.searchData(0)
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
        fetch(`${Helper.backEndAddress}/api/user/search?query=${this.filter}:${this.searchQuery}&membersOnly=true&page=${page}`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            this.userList=[]
            Helper.getNewToken(this.searchData.bind(null,0))
          }
          else if(response.ok){
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
        fetch(`${Helper.backEndAddress}/api/user/search?query=${this.filter}:${this.searchQuery}&membersOnly=true&page=${page}`, {
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

      dateFormatter(dateToFormat) {
        const monthToString = (month)=> {
          switch(month) {
            case 0: return 'January'
            case 1: return 'February'
            case 2: return 'March'
            case 3: return 'April'
            case 4: return 'May'
            case 5: return 'June'
            case 6: return 'July'
            case 7: return 'August'
            case 8: return 'September'
            case 9: return 'October'
            case 10: return 'November'
            case 11: return 'December'
          }
        }
        const dateObjected = new Date(dateToFormat);
        return `${dateObjected.getDate()} ${monthToString(dateObjected.getMonth())} ${dateObjected.getFullYear()}`
      },
    },


  }
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
<style>
  .membersComponent {
    display: flex;
  }
  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }
  .membersBodySection {
    margin-top: 30px;
  }
  .membersTableHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    border-radius: 4px;
    padding: 15px;
    width: fit-content;
    margin-left: 15px;
    font-weight: 600;
    box-shadow: 2px 2px 3px rgba(0, 0, 0, .2);
    position: relative;
    z-index: 1;
  }
  .membersTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-4);
    text-align: left;
  }
  .membersTableBody table {
    width: 100%;
  }
  .membersTableBody tbody {
    height: 55vh;
    max-height:400px;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }
  .membersTableBody tbody tr td {
    padding-top: 20px;
  }
  .membersTableBody thead tr, .membersTableBody tbody { display: block; box-sizing: border-box; }
  .membersTableBody tbody td:nth-child(1), .membersTableBody thead tr th:nth-child(1) {width: 1.5vw;}
  .membersTableBody tbody td:nth-child(2), .membersTableBody thead tr th:nth-child(2) {width: 14vw;}
  .membersTableBody tbody td:nth-child(3), .membersTableBody thead tr th:nth-child(3) {width: 18vw;}
  .membersTableBody tbody td:nth-child(4), .membersTableBody thead tr th:nth-child(4) {width: 12vw;}
  .membersTableBody tbody td:nth-child(5), .membersTableBody thead tr th:nth-child(5) {width: 12vw;}
  .membersTableHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    display: flex;
    justify-content: space-between;
    padding: 15px 25px;
    border-radius: 5px;
    align-items: center;
    width: 90%;
    margin: auto;
    position: relative;
    z-index: 1;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
  }
  .membersTableHeaderTitle {
    font-size: 20px;
    font-weight: 600;
  }
  .membersTableSearch {
    outline: none;
    padding: 8px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
    height: 40px;
    box-sizing: border-box;
  }
  .membersTableSearch::placeholder {color: var(--primary-1)}
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