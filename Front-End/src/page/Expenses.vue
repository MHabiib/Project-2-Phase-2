<template>
  <div class='expensesComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Expenses'/>

      <div class="expensesBodySection">
        <div class="expensesTableHeader">
          <div class="expensesTableHeaderTitle">
            All Expenses
          </div>

          <div style='display: flex;'>
            <input class='expenseTableSearch' type="text" placeholder="Search by anything" v-model='searchQuery'/>

            <div class="expenseTableAddNew" @click='openCreateNewExpenseWindow'>
              Request Expense
            </div>
          </div>
        </div>

        <div class="expensesTableBody">
          <table>
            <thead>
              <tr>
                <th>Date</th>
                <th>Description</th>
                <th>Status</th>
                <th>Price</th>
                <th>Contributors</th>
              </tr>
            </thead>

            <tbody id='infiniteScroll'>
              <tr
                class='expenseRow'
                v-for='(expense, index) in dataExpenseShown' :key='index'
                @click="openExpenseDetailWindow(expense.idExpense)"
              >
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
      @refreshData="getExpenseData"
      :expenseId="this.detailExpenseSelected"
    />

    <createNewExpenseWindow
      v-if='showCreateNewExpenseWindow'
      @closeCreateNewExpenseWindow='closeCreateNewExpenseWindow'
      @refreshData="getExpenseData"
    />
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';
  import UserContributedWindow from '../components/userContributedWindow';
  import expenseDetailWindow from '../components/expenseDetailWindow';
  import createNewExpenseWindow from '../components/createNewExpense';

  export default {
    computed: {rightPanelWidth: function() {return (document.documentElement.clientWidth - 280);}},
    data: function() {
      return {
        dataExpense: [],
        dataExpenseShown: [],
        selectedUserList: [],
        detailExpenseSelected: '',
        selectedExpense: '',
        showUserContributedWindow: false,
        showExpenseDetailWindow: false,
        showCreateNewExpenseWindow: false,
        searchQuery: ''
      }
    },
    created() {this.getExpenseData();},
    methods: {
      getExpenseData() {
        fetch(`http://localhost:8088/api/expense/group?email=${localStorage.getItem('userEmail')}`, {
          headers: {Authorization: localStorage.getItem('accessToken')}
        })
        .then(response => {
          response.json().then(
            res => {
              this.dataExpense = res;
              this.filterData(this.searchQuery);
            }
          )
        })
      },
      showUserContributed(selectedExpense, userList) {
        this.selectedExpense = selectedExpense;
        this.selectedUserList = userList;
        this.showUserContributedWindow = true;
      },
      closeUserContributedWindow() {this.showUserContributedWindow = false;},
      openExpenseDetailWindow(expenseId) {
        this.showExpenseDetailWindow = true;
        this.detailExpenseSelected = expenseId;
      },
      closeExpenseDetailWindow() {this.showExpenseDetailWindow = false;},
      openCreateNewExpenseWindow() {this.showCreateNewExpenseWindow = true;},
      closeCreateNewExpenseWindow() {this.showCreateNewExpenseWindow = false;},
      scroll() {
        document.getElementById('infiniteScroll').onscroll = (e) => {
          if(e.target.clientHeight + e.target.scrollTop >= e.target.scrollHeight) {
            console.log('Infinite Triggered')
          }
        };
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
      statusChecker(status) {
        switch(status) {
          case true: return 'Accepted'
          case false: return 'Rejected'
          case null: return 'Waiting'
        }
      },
      filterData(newQuery) {
        let dataFiltered = [];
        const queryBaru = newQuery.toString().toLowerCase();

        this.dataExpense.forEach(element => {
          const dateElement = this.dateFormatter(element.createdDate).toString().toLowerCase();
          const titleElement = element.title.toString().toLowerCase();
          const statusElement = this.statusChecker(element.status).toString().toLowerCase();
          const priceElement = element.price.toString();

          if(
            dateElement.includes(queryBaru) ||
            titleElement.includes(queryBaru) ||
            statusElement.includes(queryBaru) ||
            priceElement.includes(queryBaru)
          ) {
            dataFiltered.push(element)
          }
        })

        this.dataExpenseShown = dataFiltered;
        const e = document.getElementById('infiniteScroll');
        if (e.scrollHeight <= e.clientHeight) {
          console.log('Infinite Triggered')
        }
      }
    },
    components: {
      'UserContributedWindow': UserContributedWindow,
      'expenseDetailWindow': expenseDetailWindow,
      'createNewExpenseWindow': createNewExpenseWindow
    },
    filters: {
      statusChecker(status) {
        switch(status) {
          case true: return 'Accepted'
          case false: return 'Rejected'
          case null: return 'Waiting'
        }
      }
    },
    mounted() {this.scroll()},
    watch: {
      searchQuery: function (newQuery, oldQuery) {
        if(newQuery === '') {
          this.dataExpenseShown = this.dataExpense;
        } else {
          this.filterData(newQuery);
        }
      }
    },
  }
</script>

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
    height: 63vh;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }

  .expensesTableBody tbody tr td {padding-top: 12px; padding-bottom: 12px;}
  .expensesTableBody thead tr, .expensesTableBody tbody {display: block; box-sizing: border-box;}

  .expensesTableBody tbody td:nth-child(1), .expensesTableBody thead tr th:nth-child(1) {
    width: 13vw;
    text-align: left;
    padding-left: 10px;
  }

  .expensesTableBody tbody td:nth-child(2), .expensesTableBody thead tr th:nth-child(2) {
    width: 300px;
    text-align: left;
    padding-left: 10px;
  }

  .expensesTableBody tbody td:nth-child(3), .expensesTableBody thead tr th:nth-child(3) {width: 10vw;}
  .expensesTableBody tbody td:nth-child(4), .expensesTableBody thead tr th:nth-child(4) {width: 11vw;}
  .expensesTableBody tbody td:nth-child(5), .expensesTableBody thead tr th:nth-child(5) {width: 10vw;}

  .showMembersButton {cursor: pointer;}

  .showMembersButton:hover {
    text-decoration: underline;
    color: var(--primary-0);
  }

  .expenseTableSearch {
    outline: none;
    padding: 8px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
  }

  .expenseTableSearch::placeholder {color: var(--primary-1)}

  .expenseTableAddNew {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    font-size: 14px;
    margin-left: 10px;
  }

  .expenseTableAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
    cursor: pointer;
  }

  .expenseTableAddNew:active {background-color: var(--primary-4);}
  .expenseRow {cursor: pointer;}
  .expenseRow:hover {background-color: white;}
  .expenseRow:active {background-color: rgba(255, 255, 255, .5);}
</style>
