<template>
  <div class='paymentComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection :headerTitle="'Group '+groupName+'\'s Members\' Payment'"/>
      <div style="margin:10px 5px;color: var(--primary-0)">First payment start from : {{monthList[groupCreated.month]}} {{groupCreated.year}}</div>
      <div class="paymentsBodySection">
        
        <div class="paymentsTableHeader">
          <div class="paymentsTableHeaderTitle">
            Payment Table <span class="nextPrev" @click="year-=1">&#8249;</span>   {{checkYear}}   <span class="nextPrev" @click="year+=1">&#8250;</span>
          </div>

          <div class="myParent" style='display: flex;'>
            <input class='paymentTableSearch' type="text" :placeholder="'Search by '+filter " v-model='searchQuery'/>
            <!-- <div class="refreshBtn"  @click='searchData(0)'>
              <img src="../assets/magnifier.png" width="18px" alt="Search">
            </div> -->
            <div class="dropdownMenu" >
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
        
        <div class="paymentsTableBody" >
          <table >
            <thead>
              <tr>
                <th style="width:1.5vw">&nbsp;&nbsp;</th>
                <th>Name</th>
                <th v-if="myRole==='SUPER_ADMIN'">Group</th>
                <th  v-for='(month,index) in monthList' :key='"month-"+index'>
                  {{month.substring(0,3)}}
                </th>
              </tr>
            </thead>
            <tbody id='infiniteScroll'>
              <tr
                class='paymentRow'
                v-for='(user, i) in userList' :key='"no-"+i'          
              >
                <td style="width:1.5vw">{{i+1}}.</td>
                <td >{{user.name}}</td>
                <td v-if="myRole==='SUPER_ADMIN'">{{user.groupName}}</td>
                <td v-for='j in (getThisYearTotalPeriodPayed(user.totalPeriodPayed,year)<0?0:getThisYearTotalPeriodPayed(user.totalPeriodPayed,year))' :key='"ceklis-"+j'   > 
                  &#10004;
                </td>
                </tr>
                <div v-show="loading" class="lds-ring"><div></div><div></div><div></div><div></div></div>
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
  import UserContributedWindow from '../components/userContributedWindow';
  import payNowWindow from '../components/payNow';
  import Helper from '../../Helper';
  import Multiselect from 'vue-multiselect'

  export default {
    components:{
      'payNowWindow': payNowWindow,
      'multiselect': Multiselect,

    },
    computed: {
      rightPanelWidth: function() {return (document.documentElement.clientWidth - 280);},
      checkYear: function(){
        if(this.year < this.groupCreated.year){
          this.year=this.groupCreated.year
        }
        return this.year
      },      
    },
    data: function() {
      return {
        searchMode:false,
        groupName:localStorage.groupName,
        monthList:[],
        start:'',
        dataUser:{},
        year:0,
        userList:[],
        showPayNowWindow: false,
        loading:false,
        searchQuery:'',
        groupCreated:{},
        filter:'period',
        options:['name','group','period'],
        groupOnly:false,
        showAll:false
      }
    },

    created() {
      this.searchData(0)
      this.showAll = this.$myRole === 'SUPER_ADMIN'
      this.monthList=["January","February","March","April","May","June","July","August","September","October","November","December"]
      this.groupCreated.month=new Date(parseInt(localStorage.groupCreatedDate)).getMonth()
      this.groupCreated.year=new Date(parseInt(localStorage.groupCreatedDate)).getFullYear()
      this.year=new Date().getFullYear()
      },

    mounted(){
      this.scroll()
    },
    watch:{      
      filter : function (newQuery, oldQuery) {
        this.searchData(0)  
      },
      searchQuery: function(oldVal,newVal){
        this.searchData(0)
      },

    },
    methods: {
      searchData(page){
        this.loading=true
        fetch(`${Helper.backEndAddress}/api/user/search?query=${this.filter}:${this.searchQuery}&membersOnly=${this.showAll}&page=${page}`, {              
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
                this.userList=res.content 
                this.userList = this.userList.filter(user => user.groupName !== 'GROUP_LESS')
                this.dataUser=res
                setTimeout(e=>{this.loading=false},500)
              }
            )
          }
        })
      },
      getThisYearTotalPeriodPayed: function(totalPeriodPayed,year){
        let yearDiff = year - this.groupCreated.year
        totalPeriodPayed= totalPeriodPayed - (yearDiff*12)
        let result = totalPeriodPayed + this.groupCreated.month
        return result > 12 ? 12 : result
      },
      openPayNowWindow() {this.showPayNowWindow = true},
      closePayNowWindow() {this.showPayNowWindow = false},
      getMembersData(page) {
        this.loading=true
        // fetch(`${Helper.backEndAddress}/api/group/membersByEmail?email=${localStorage.getItem('userEmail')}&page=${page}&filter=${'totalPeriodPayed'}`, {
        fetch(`${Helper.backEndAddress}/api/user/search?query=${this.filter}:${this.searchQuery}&membersOnly=${this.showAll}&page=${page}`, {              
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            // this.userList.length=0
            Helper.getNewToken(this.getMembersData.bind(null,this.dataUser.pageable.pageNumber+1))
          }
          else if(response.ok){
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                this.userList=this.userList.concat(res.content)
                this.userList = this.userList.filter(user => user.groupName !== 'GROUP_LESS')

                this.dataUser=res
                setTimeout(e=>{this.loading=false},500)

              }
            )
          }
        })
      },
      scroll() {
        const paymentTable = document.querySelector('#infiniteScroll');
        paymentTable.addEventListener('scroll', e => {
          // console.log(paymentTable.scrollTop + paymentTable.clientHeight+" : "+paymentTable.scrollHeight)
          if((paymentTable.scrollTop + paymentTable.clientHeight)+1>= paymentTable.scrollHeight) {
            console.log('infinite scroll triggered!')
            if(this.dataUser.last!=true & this.loading==false){   
              this.getMembersData(this.dataUser.pageable.pageNumber+1)
              console.log('get more data!')
            }
          }
        })
      },
    }
  }
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style>
  .paymentComponent {display: flex;}

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .paymentsBodySection {margin-top: 30px;}

  .paymentsTableHeader {
    background-color: var(--primary-0);

    color: var(--lightColor);
    display: flex;
    justify-content: space-between;
    padding: 15px 25px;
    border-radius: 5px;
    align-items: center;
    width: 90%;
    font-weight: 600;
    margin: auto;
    position: relative;
    z-index: 1;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
  }

  .paymentsTableHeaderTitle {
    font-size: 20px;
    font-weight: 600;
  }

  .paymentsTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-4);
    text-align: center;
  }

  .paymentsTableBody table {width: 100%;}

  .paymentsTableBody tbody {
    height: 55vh;
    /* overflow-y: auto;
    overflow-x:hidden; */
    overflow: auto;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }

  .paymentsTableBody tbody tr td {padding-top: 12px; padding-bottom: 12px;}
  .paymentsTableBody thead , .paymentsTableBody tbody {display: block; box-sizing: border-box;}

  .paymentsTableBody tbody td, .paymentsTableBody thead tr th {
    width: 4vw;
    text-align: center;
    /* padding-left: 5px; */
  }
  .paymentsTableBody tbody{
    max-height:400px
  }
  .paymentsTableBody tbody td:nth-child(2), td:nth-child(1), .paymentsTableBody thead tr th:nth-child(2), th:nth-child(1) {
    width: 15vw;
    text-align: left;  
  }
  /* .paymentsTableBody tbody td:nth-child(4), .paymentsTableBody thead tr th:nth-child(4) {width: 11vw;}
  .paymentsTableBody tbody td:nth-child(5), .paymentsTableBody thead tr th:nth-child(5) {width: 10vw;} */

  .showMembersButton {cursor: pointer;}

  .showMembersButton:hover {
    text-decoration: underline;
    color: var(--primary-0);
  }

  .paymentTableSearch {
    outline: none;
    padding: 8px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
  }

  .paymentTableSearch::placeholder {color: var(--primary-1)}

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
    cursor: pointer;
  }

  .refreshBtn:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
  }

  .refreshBtn:active {background-color: var(--primary-4);}
  /* .paymentRow {cursor: pointer;} */
  .paymentRow:hover {background-color: white;}
  /* .paymentRow:active {background-color: rgba(255, 255, 255, .5);} */

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


  .nextPrev{
    font-size: 28px;
    text-decoration: none;
    padding:1px 8px; 
    border-radius: 28%;
    display: inline-block;
  }

  .nextPrev:hover{
    cursor: pointer;
    background-color: var(--lightColor); 
    color: var(--primary-0);
  }
  .nextPrev:active{
    cursor: pointer;

    background-color: var(--primary);
    color: var(--lightColor);
  }

</style>
