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

          <input class='expenseTableSearch' type="text" placeholder="Search by anything" />

          <select class='expenseTableSort' name="sortBy" id="sortBy">
            <option style='display: none' value="none">Sort By</option>
            <option value="date">Date</option>
            <option value="price">Price</option>
          </select>

          <div class="expenseTableAddNew">
            Add New Expense
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

            <tbody>
              <tr
                class='expenseRow'
                v-for='(expense, index) in dataExpense' :key='index'
                @click="openExpenseDetailWindow(expense)"
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
      :detailExpenseSelected="this.detailExpenseSelected"
    />
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';
  import UserContributedWindow from '../components/userContributedWindow';
  import expenseDetailWindow from '../components/expenseDetailWindow';

  export default {
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    },
    data: function() {
      return {
        dataExpense: [],
        showUserContributedWindow: false,
        selectedExpense: '',
        selectedUserList: [],
        showExpenseDetailWindow: false,
        detailExpenseSelected: {},
        dummyData: [

        ]
      }
    },
    created() {
      this.getExpenseData();
    },
    methods: {
      getExpenseData() {
        fetch(`http://localhost:8088/api/expense/group?email=${localStorage.getItem('userEmail')}`, {
          headers: {
            Authorization: localStorage.getItem('token')
          }
        })
        .then(response => response.json())
        .then(res => {
          this.dataExpense = res
        })
      },
      showUserContributed(selectedExpense, userList) {
        this.selectedExpense = selectedExpense;
        this.selectedUserList = userList;
        this.showUserContributedWindow = true;
      },
      closeUserContributedWindow() {
        this.showUserContributedWindow = false;
      },
      openExpenseDetailWindow(expense) {
        this.showExpenseDetailWindow = true;
        this.detailExpenseSelected = expense;
      },
      closeExpenseDetailWindow() {
        this.showExpenseDetailWindow = false;
      }
    },
    components: {
      'UserContributedWindow': UserContributedWindow,
      'expenseDetailWindow': expenseDetailWindow
    },
    filters: {
      statusChecker(status) {
        switch(status) {
          case true: return 'Accepted'
          case false: return 'Rejected'
          case null: return 'Waiting'
        }
      }
    }
  }
</script>

<style>
  .expensesComponent {
    display: flex;
  }

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .expensesBodySection {
    margin-top: 30px;
  }

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
    color: var(--primary-3);
    text-align: center;
    line-height: 35px;
  }

  .expensesTableBody table {
    width: 100%;
  }

  .expensesTableBody tbody {
    height: 60vh;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }

  .expensesTableBody thead tr, .expensesTableBody tbody {display: block; box-sizing: border-box;}
  .expensesTableBody tbody td:nth-child(1), .expensesTableBody thead tr th:nth-child(1) {width: 13vw; text-align: left; padding-left: 10px;}
  .expensesTableBody tbody td:nth-child(2), .expensesTableBody thead tr th:nth-child(2) {width: 300px; text-align: left; padding-left: 10px;}
  .expensesTableBody tbody td:nth-child(3), .expensesTableBody thead tr th:nth-child(3) {width: 10vw;}
  .expensesTableBody tbody td:nth-child(4), .expensesTableBody thead tr th:nth-child(4) {width: 11vw;}
  .expensesTableBody tbody td:nth-child(5), .expensesTableBody thead tr th:nth-child(5) {width: 10vw;}

  .showMembersButton {
    cursor: pointer;
  }

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

  .expenseTableSearch::placeholder {
    color: var(--primary-1)
  }

  .expenseTableSort {
    outline: none;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
    width: 100px;
    height: 29px;
  }

  .expenseTableAddNew {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 600;
    border-radius: 5px;
  }

  .expenseTableAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
    cursor: pointer;
  }

  .expenseTableAddNew:active {
    background-color: var(--primary-4);
  }

  .expenseRow {
    cursor: pointer;
  }

  .expenseRow:hover {
    background-color: white;
  }

  .expenseRow:active {
    background-color: rgba(255, 255, 255, .5);
  }
</style>
