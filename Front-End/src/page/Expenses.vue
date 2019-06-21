<template>
  <div class='expensesComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Expenses'/>

      <div class="expensesBodySection">
        <div class="expensesTableHeader">
          All Expenses
        </div>

        <div class="expensesTableBody">
          <table>
            <thead>
              <tr>
                <th>Date</th>
                <th>Detail</th>
                <th>Price</th>
                <th>Contributors</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for='(expense, index) in dataExpense' :key='index'>
                <td>{{expense.createdDate | dateFormatter}}</td>
                <td>{{expense.title}}</td>
                <td>Rp {{expense.price | thousandSeparators}}</td>
                <td>31 Members</td>
              </tr>
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

  export default {
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    },
    data: function() {
      return {
        dataExpense: []
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
      }
    },
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
    border-radius: 4px;
    padding: 15px;
    width: fit-content;
    margin-left: 15px;
    font-weight: 600;
    box-shadow: 2px 2px 3px rgba(0, 0, 0, .2);
    position: relative;
    z-index: 1;
  }

  .expensesTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-0);
    text-align: center;
    line-height: 35px;
  }

  .expensesTableBody table {
    width: 100%;
  }

  .expensesTableBody tbody {
    height: 63vh;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }

  .expensesTableBody thead tr, .expensesTableBody tbody { display: block; box-sizing: border-box; }
  .expensesTableBody tbody td:nth-child(1), .expensesTableBody thead tr th:nth-child(1) { width: 15vw; }
  .expensesTableBody tbody td:nth-child(2), .expensesTableBody thead tr th:nth-child(2) { width: 325px; text-align: left; }
  .expensesTableBody tbody td:nth-child(3), .expensesTableBody thead tr th:nth-child(3) { width: 15vw; }
  .expensesTableBody tbody td:nth-child(4), .expensesTableBody thead tr th:nth-child(4) { width: 15vw; }
</style>
