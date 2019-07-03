<template>
  <div class='dashboardContainer'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Dashboard' />

      <div class='dashboardFirstRow'>
        <div class='cardStyleOne'>
          <div class  ='cardStyleOneHeader'>

          </div>

          <div class='cardStyleOneBody'>
            <div class='cardStyleOneTitle'>Total Expenses by Value</div>
            <div class='cardStyleOneDescription'>Increased 25% compared to last month</div>
          </div>
        </div>

        <div class='cardStyleOne'>
          <div class='cardStyleOneHeader'>

          </div>

          <div class='cardStyleOneBody'>
            <div class='cardStyleOneTitle'>Total Expenses by Quantity</div>
            <div class='cardStyleOneDescription'>Increased 30% compared to last month</div>
          </div>
        </div>

        <div class='bigButtonContainer'>
          <div class="bigButton bigButtonKiri" @click='openPayNowWindow'>
            <div>
              <img src="../assets/pay-icon.png" alt="Pay Now">
              <div class="bigButtonText">Pay Now</div>
            </div>
          </div>

          <div class="bigButton bigButtonKanan" @click="openCreateNewExpenseWindow">
            <div>
              <img src="../assets/expense.png" alt="Pay Now">
              <div class="bigButtonText">Request Expense</div>
            </div>
          </div>
        </div>
      </div>

      <div class="dashboardSecondRow">
        <div class="secondRowCardContainer">
          <div class="secondRowHeader">
            <img src="../assets/balance.png" alt="" width="55%">
          </div>

          <div class="secondRowBody">
            <div class='secondRowBodyUpper'>
              <div class="secondRowBodyUpperTitle">
                Total Balance
              </div>

              <div class="secondRowBodyUpperValue">
                Rp {{dashboardData.groupBalance | thousandSeparators}}
              </div>
            </div>

            <div class='secondRowBodyLower'>
              <img src='../assets/time.png' alt='Last Update Icon' width='14px'/> Last updated 3 hours ago.
            </div>
          </div>
        </div>

        <div class="secondRowCardContainer">
          <div class="secondRowHeader">
            <img src="../assets/members.png" alt="" width="55%">
          </div>

          <div class="secondRowBody">
            <div class='secondRowBodyUpper'>
              <div class="secondRowBodyUpperTitle">
                Total Members
              </div>

              <div class="secondRowBodyUpperValue">
                {{dashboardData.totalMembers}}
              </div>
            </div>

            <div class='secondRowBodyLower'>
              <img src='../assets/time.png' alt='Last Update Icon' width='14px'/> Last updated 3 hours ago.
            </div>
          </div>
        </div>

        <div class="secondRowCardContainer">
          <div class="secondRowHeader">
            <img src="../assets/pending.png" alt="" width="55%">
          </div>

          <div class="secondRowBody">
            <div class='secondRowBodyUpper'>
              <div class="secondRowBodyUpperTitle">
                Pending Payment
              </div>

              <div class="secondRowBodyUpperValue">
                Rp {{dashboardData.pendingPayment | thousandSeparators}}
              </div>
            </div>

            <div class='secondRowBodyLower'>
              <img src='../assets/time.png' alt='Last Update Icon' width='14px'/> Last updated 3 hours ago.
            </div>
          </div>
        </div>

        <div class="secondRowCardContainer">
          <div class="secondRowHeader">
            <img src="../assets/contribution.png" alt="" width="55%">
          </div>

          <div class="secondRowBody">
            <div class='secondRowBodyUpper'>
              <div class="secondRowBodyUpperTitle">
                Your Contribution
              </div>

              <div class="secondRowBodyUpperValue">
                Rp {{dashboardData.yourContribution | thousandSeparators}}
              </div>
            </div>

            <div class='secondRowBodyLower'>
              <img src='../assets/last-expense.png' alt='Last Update Icon' width='14px'/> Last expense is&nbsp;<span class='lastExpense'>Beli Meja</span>.
            </div>
          </div>
        </div>
      </div>

      <div class="dashboardThirdRow">
        <div class="thirdRowHeader">
          Your Payment - 2019
        </div>

        <div class="thirdRowBody">
          <div class="thirdRowContent paidMonth">Jan</div>
          <div class="thirdRowContent paidMonth">Feb</div>
          <div class="thirdRowContent paidMonth">Mar</div>
          <div class="thirdRowContent paidMonth">Apr</div>
          <div class="thirdRowContent paidMonth">May</div>
          <div class="thirdRowContent">Jun</div>
          <div class="thirdRowContent">Jul</div>
          <div class="thirdRowContent">Aug</div>
          <div class="thirdRowContent">Sep</div>
          <div class="thirdRowContent">Oct</div>
          <div class="thirdRowContent">Nov</div>
          <div class="thirdRowContent">Dec</div>
        </div>
      </div>
    </div>

    <createNewExpenseWindow
      v-if='showCreateNewExpenseWindow'
      @closeCreateNewExpenseWindow='closeCreateNewExpenseWindow'
      @refreshData="getExpenseData"
    />

    <payNowWindow
      v-if='showPayNowWindow'
      @closePayNowWindow='closePayNowWindow'
    />
  </div>
</template>

<script>
  import createNewExpenseWindow from '../components/createNewExpense';
  import payNowWindow from '../components/payNow';

  export default {
    data: function() {
      return {
        dashboardData: {},
        showCreateNewExpenseWindow: false,
        showPayNowWindow: false
      }
    },
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    },
    created() {
      this.getDashboardData();
    },
    methods: {
      getDashboardData() {
        fetch(`http://localhost:8088/api/dashboard?email=${localStorage.getItem('userEmail')}`, {
          headers: {
            'Authorization': localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          response.json().then(
            res => {
              this.dashboardData = res;
            }
          )
        })
      },
      openCreateNewExpenseWindow() {this.showCreateNewExpenseWindow = true;},
      closeCreateNewExpenseWindow() {this.showCreateNewExpenseWindow = false;},
      openPayNowWindow() {this.showPayNowWindow = true},
      closePayNowWindow() {this.showPayNowWindow = false}
    },
    components: {
      'createNewExpenseWindow': createNewExpenseWindow,
      'payNowWindow': payNowWindow
    }
  }
</script>

<style>
  .dashboardContainer {
    display: flex;
  }

  .rightPanel {
    padding: 20px 20px 20px 30px;
    box-sizing: border-box;
  }

  .dashboardFirstRow {
    display: flex;
    justify-content: space-between;
    height: 250px;
    margin-top: 25px;
  }

  .cardStyleOne {
    width: 300px;
  }

  .cardStyleOneHeader {
    background-color: var(--primary-0);
    width: 93%;
    height: 70%;
    border-radius: 10px;
    margin: auto;
    box-shadow: 2px 2px 6px rgba(0, 0, 0, .2);
  }

  .cardStyleOneBody {
    background-color: var(--lightColor);
    position: relative;
    top: -65%;
    padding: 15px;
    padding-top: 58%;
    z-index: -1;
    border-radius: 10px;
    box-shadow: 2px 2px 6px rgba(0, 0, 0, .2);
  }

  .cardStyleOneTitle {
    color: var(--primary-0);
    font-weight: 500;
    margin-bottom: 3px;
    font-size: 14px;
  }

  .cardStyleOneDescription {
    font-weight: 300;
    font-size: 12px;
    color: var(--darkColor);
  }

  .bigButtonContainer {
    display: flex;
    width: 270px;
    border-radius: 10px;
    overflow: hidden;
    height: 95%;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .25);
  }

  .bigButton {
    width: 50%;
    text-align: center;
    padding: 0 20px;
    display: flex;
    align-items: center;
    cursor: pointer;
    font-weight: 600;
  }

  .bigButtonKiri {
    background-color: var(--lightColor);
    color: var(--primary-0);
  }

  .bigButtonKanan {
    background-color: var(--primary-0);
    color: #F2F4F6;
  }

  .bigButtonKanan:hover {
    background-color: rgba(76,139,210,.9);
  }

  .bigButtonKanan:active {
    background-color: var(--primary-0);
  }

  .bigButtonKiri:hover {
    background-color: rgba(242,244,246,.6);
  }

  .bigButtonKiri:active {
    background-color: var(--lightColor);
  }

  .bigButton img {
    width: 70%;
    margin-bottom: 5px;
  }

  .dashboardSecondRow {
    display: flex;
    justify-content: space-between;
    height: 115px;
    margin-top: 12px;
    margin-bottom: 47px;
  }

  .secondRowCardContainer {
    width: 23.5%;
  }

  .secondRowHeader {
    background-color: var(--primary-0);
    height: 80px;
    width: 80px;
    border-radius: 10px;
    margin-left: 10px;
    box-shadow: 1px 1px 4px rgba(0, 0, 0, .3);
    display: flex;
    align-items: center;
  }

  .secondRowHeader img {
    margin: auto;
  }

  .secondRowBody {
    background-color: var(--lightColor);
    height: 100px;
    position: relative;
    top: -70px;
    z-index: -1;
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    text-align: right;
    padding: 10px;
  }

  .secondRowBodyUpper {
    height: 45px;
    padding-top: 26px;
  }

  .secondRowBodyUpperTitle {
    color: var(--primary-0);
    font-weight: 300;
    font-size: 12px;
  }

  .secondRowBodyUpperValue {
    font-weight: bold;
    color: var(--primary-0);
  }

  .secondRowBodyLower {
    border-top: solid 1px white;
    font-size: 12px;
    color: var(--primary-0);
    padding: 10px 10px 10px 5px;
    text-align: left;
    display: flex;
    align-items: center;
  }

  .secondRowBodyLower img {
    margin-right: 5px;
  }

  .lastExpense {
    color: #FC1818;
  }

  .thirdRowBody {
    display: flex;
    justify-content: space-evenly;
    align-items: center;
  }

  .thirdRowHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    width: fit-content;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 12px 20px;
    margin-left: 10px;
    font-weight: 600;
  }

  .thirdRowBody {
    background-color: var(--lightColor);
    font-size: 20px;
    font-weight: 600;
    color: var(--primary-0);
    padding: 60px 0 40px 0;
    border-radius: 10px;
    position: relative;
    top: -30px;
    z-index: -1;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
  }

  .paidMonth {
    text-decoration: line-through;
  }
</style>
