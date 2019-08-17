<template>
  <div class='expensesComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection :headerTitle="'Group '+groupName+'\'s Expenses'"/>

      <div class="expensesBodySection">
        <div class="expensesTableHeader">
          <div class="expensesTableHeaderTitle">
            All Expenses
          </div>

          <div class="myParent" style='display: flex;'>
          <input class='expensesTableSearch'  type="text" :placeholder="'Query by '+this.searchPlaceHolder " v-model='searchQuery'/>
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

        <div class="expensesTableBody">
          <table>
            <thead>
              <tr>
                <th>&nbsp;&nbsp;</th>
                <th>Date</th>
                <th>Title</th>
                <th>Status</th>
                <th>Price</th>
                <th>Contributors</th>
              </tr>
            </thead>

            <tbody id='infiniteScroll'>
              <tr
                class='expenseRow'
                v-for='(expense, index) in expenseList' :key='"expense-"+index'
                @click="openExpenseDetailWindow(expense)"
              >
                <td>{{index+1}}.</td>
                <td>{{expense.createdDate | dateFormatter}}</td>
                <td>{{expense.title}}</td>
                <td>{{expense.status | statusChecker}}</td>
                <td>Rp {{expense.price | thousandSeparators}}</td>
                <td
                  class='showMembersButton'
                  @click.stop="showUserContributed(expense.title, expense.userContributed)"
                >
                  {{expense.userContributed.length}} Members
                </td>
              </tr>
              <div class="loading">
                <div v-show="loading"  class="lds-ring"><div></div><div></div><div></div><div></div></div>
              </div>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <UserContributedWindow
      v-if='showUserContributedWindow'
      @closeContributedWindow="closeUserContributedWindow"
      :expenseName="this.selectedExpense"
      :userList="this.selectedUserList"
    />

    <expenseDetailWindow
      v-if='showExpenseDetailWindow'
      @closeExpenseDetailWindow="closeExpenseDetailWindow"
      @refreshData="searchData(0)"
      :expenseDetail=  this.detailExpenseSelected
    />

    <createNewExpenseWindow
      v-if='showCreateNewExpenseWindow'
      @closeCreateNewExpenseWindow='closeCreateNewExpenseWindow'
      @refreshData="searchData(0)"
    />
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';
  import UserContributedWindow from '../components/userContributedWindow';
  import expenseDetailWindow from '../components/expenseDetailWindow';
  import createNewExpenseWindow from '../components/createNewExpense';
  import Helper from '../../Helper';
  import Multiselect from 'vue-multiselect';
  import { setTimeout } from 'timers';

  export default {
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      },
      searchPlaceHolder:function(){
        return this.filter == "date before"  || this.filter == "date after"  ? "(dd-MMMM-yyyy)" : this.filter
      }
    },
    components: {
      'multiselect': Multiselect,
      'UserContributedWindow': UserContributedWindow,
      'expenseDetailWindow': expenseDetailWindow,
      'createNewExpenseWindow': createNewExpenseWindow
    },
    data: function() {
      return {
        dataExpense: [],
        expenseList: [],
        selectedUserList: [],
        detailExpenseSelected: '',
        selectedExpense: '',
        showUserContributedWindow: false,
        showExpenseDetailWindow: false,
        showCreateNewExpenseWindow: false,
        searchQuery: '',
        filter:'title',
        options:['title','status','date before','date after','price <','price >'],
        groupName:localStorage.groupName,
        disable:false,
        loading:false,
        createExpenseMode:null,
      }
    },
    created() {
      this.searchData(0);
    },
    mounted() {this.scroll()},
    watch: {
      filter : function (newQuery, oldQuery) {
        this.searchData(0)  
      },
      searchQuery: function (newQuery, oldQuery) {
        this.searchData(0)  
      }
    },
    methods: {
      searchData(page){
        console.log(this.disable)
        // if(this.disable==true) return
        this.loading=true
        console.log(this.filter +" : "+this.searchQuery)
        // fetch(`${Helper.backEndAddress}/api/expense/search?${this.filter}=${this.searchQuery}&groupName=${this.groupName}&page=${page}`, {
        fetch(`${Helper.backEndAddress}/api/expense/search?query=${this.filter}:${this.searchQuery}&page=${page}`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            this.disable=true
            this.userList=[]
            Helper.getNewToken(this.searchData.bind(null,0))
          }
          else if(response.ok){
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                this.expenseList=res.content 
                this.dataExpense=res
                setTimeout(e=>{this.loading=false},500)
                this.disable=false
                // this.loading=false
              }
            )
          }
        })
      },
      getExpenseData(page) {
        this.loading=true
        this.disable=true
        // fetch(`${Helper.backEndAddress}/api/expense/search?${this.filter}=${this.searchQuery}&groupName=${this.groupName}&page=${page}`, {                    
        fetch(`${Helper.backEndAddress}/api/expense/search?query=${this.filter}:${this.searchQuery}&page=${page}`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            this.disable=true
            // this.memberList.length=0
            Helper.getNewToken(this.getExpenseData.bind(null,this.dataExpense.pageable.pageNumber+1))
          }
          else{
            localStorage.setItem('accessToken','Token '+ response.headers.get('Authorization'))
            response.json().then(
              res => {
                this.expenseList=this.expenseList.concat(res.content)
                this.dataExpense=res      
                setTimeout(e=>{this.loading=false},500)
                this.disable=false
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
            if(this.dataExpense.last!=true & this.loading==false){   
              this.loading=true;
              this.getExpenseData(this.dataExpense.pageable.pageNumber+1)
              console.log('get more data!')
            }
          }
        })
      },
      showUserContributed(selectedExpense, userList) {
        this.selectedExpense = selectedExpense;
        this.selectedUserList = userList;
        this.showUserContributedWindow = true;
      },
      closeUserContributedWindow() {this.showUserContributedWindow = false;},
      openExpenseDetailWindow(expense) {
        this.createExpenseMode = true;
        this.showExpenseDetailWindow = true;
        this.detailExpenseSelected = expense;
      },
      closeExpenseDetailWindow() {this.showExpenseDetailWindow = false;},
      openCreateNewExpenseWindow() {this.showCreateNewExpenseWindow = true;},
      closeCreateNewExpenseWindow() {this.showCreateNewExpenseWindow = false;},
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
    statusChecker(status) {
      switch(status) {
        case true: return 'Accepted'
        case false: return 'Rejected'
        case null: return 'Waiting'
      }
    },
  },
  filters:{
    statusChecker(status) {
      switch(status) {
        case true: return 'Accepted'
        case false: return 'Rejected'
        case null: return 'Waiting'
      }
    },
  }

}
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style> 
  .expensesComponent {display: flex;}

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .expensesBodySection {margin-top: 30px;}

  .expensesTableHeader {
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

  .expensesTableHeaderTitle {
    font-size: 20px;
    font-weight: 600;
  }

  .expensesTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-4);
    text-align: center;
  }

  .expensesTableBody table {width: 100%;}

  .expensesTableBody tbody {
    height: 55vh;
    max-height:400px;
    overflow-y: auto;
    overflow-x: auto;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }

  .expensesTableBody tbody tr td {padding-top: 12px; padding-bottom: 12px;}
  .expensesTableBody thead tr, .expensesTableBody tbody {text-align:left;display: block; box-sizing: border-box;}
  .expensesTableBody tbody td:nth-child(1), .expensesTableBody thead tr th:nth-child(1) {
    width: 1.5vw;
    text-align: left;
    padding-left: 10px;
  }
  .expensesTableBody tbody td:nth-child(2), .expensesTableBody thead tr th:nth-child(2) {
    width: 13vw;
    text-align: left;
    padding-left: 10px;
  }

  .expensesTableBody tbody td:nth-child(3), .expensesTableBody thead tr th:nth-child(3) {
    width: 300px;
    text-align: left;
    padding-left: 10px;
  }

  .expensesTableBody tbody td:nth-child(4), .expensesTableBody thead tr th:nth-child(4) {width: 10vw;}
  .expensesTableBody tbody td:nth-child(5), .expensesTableBody thead tr th:nth-child(5) {width: 11vw;}
  .expensesTableBody tbody td:nth-child(6), .expensesTableBody thead tr th:nth-child(6) {width: 10vw;}

  .showMembersButton {cursor: pointer;}

  .showMembersButton:hover {
    text-decoration: underline;
    color: var(--primary-0);
  }

  .expensesTableSearch {
    outline: none;
    padding: 8px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
    height: 40px;
    box-sizing: border-box;
  }

  .expensesTableSearch::placeholder {color: var(--primary-1)}
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
  .expenseTableAddNew {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    font-size: 14px;
    margin-left: 10px;
    cursor: pointer;
  }

  .expenseTableAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
  }

  .expenseTableAddNew:active {background-color: var(--primary-4);}
  .expenseRow {cursor: pointer;}
  .expenseRow:hover {background-color: white;}
  .expenseRow:active {background-color: rgba(255, 255, 255, .5);}
  .loading{
    text-align: center;
    position: relative;
  }
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
