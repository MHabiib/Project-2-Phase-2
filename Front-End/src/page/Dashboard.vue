<template>
  <div class='dashboardContainer'>
    <SidebarComponent />

    <div class='rightPanel'>
      <HeaderSection headerTitle='Dashboard' />

      <div class='dashboardFirstRow'>
        <div class='cardStyleOne'>
          <div class  ='cardStyleOneHeader'>
            <p> Rp {{dashboardData.expenseByValue | thousandSeparators}}</p>
          </div>

          <div class='cardStyleOneBody'>

            <div class='cardStyleOneTitle'>Total Debit This Month</div>
            <div class='cardStyleOneDescription'>Toal Debit Last Month Rp {{dashboardData.expenseByValueBefore | thousandSeparators}}</div>
          </div>
        </div>

        <div class='cardStyleOne'>
          <div class='cardStyleOneHeader'>
            <p>{{dashboardData.expenseByQuantity | thousandSeparators}}</p>
          </div>

          <div class='cardStyleOneBody'>
            <div class='cardStyleOneTitle'>Total Expense This Month</div>
            <div class='cardStyleOneDescription'>Total Expense Last Month {{dashboardData.expenseByQuantityBefore | thousandSeparators}}</div>
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

            <div class='secondRowBodyLower' >
                <img src='../assets/last-expense.png' alt='Last Update Icon' width='14px'/>
                <span v-show="isLastAvailable"> 
                  Last expense (<span class='lastExpense'>{{lastExpense.title}}</span>) {{lastExpense.lastModifiedAt | fromNow}}. 
                </span> 
                <span v-show="!isLastAvailable"> 
                 There isn't any expense yet. 
                </span> 
            </div>
          </div>
        </div>
      </div>

      <div class="dashboardThirdRow">
        <div class="thirdRowHeader">
          Your Payment in <span class="nextPrev" @click="year>groupCreated.year?year-=1:year;getThisYearTotalPeriodPayed(dashboardData.yourPayment,year)
">&#8249;</span>{{checkYear}}<span class="nextPrev" @click="year+=1;getThisYearTotalPeriodPayed(dashboardData.yourPayment,year)">&#8250;</span>
        </div>

        <div class="thirdRowBody">
          <div 
              class="thirdRowContent paidMonth"
              v-for='(monthPaid,index) in ((monthPaid))' :key='index'>{{monthPaid}}
          </div>
        
          <div 
              class="thirdRowContent"
              v-for='(monthNotYetPaid,idx) in ((monthNotYetPaid))' :key='"Not"+idx'>{{monthNotYetPaid}}
          </div>
        
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
      :dataPayNow='this.dataPayNow'
    />
  </div>
</template>

<script>
  import createNewExpenseWindow from '../components/createNewExpense';
  import payNowWindow from '../components/payNow';
  import Helper from '../../Helper';

  export default {
    data: function() {
      return {
        monthPaid:[],
        monthNotYetPaid:[],
        dashboardData: {},
        showCreateNewExpenseWindow: false,
        showPayNowWindow: false,
        dataPayNow: {},
        year:0,
        groupCreated:{},
        lastExpense:{},
        isLastAvailable :false,
        email:localStorage.getItem('userEmail')
      }
    },
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      },
      checkYear: function(){
        if(this.year <= this.groupCreated.year){
          this.year=this.groupCreated.year
        }
        // console.log(this.year)
        return this.year
      },
    },
    created() {
      this.getDashboardData();
      this.getLastExpense();
      this.groupCreated.month=new Date(parseInt(localStorage.groupCreatedDate)).getMonth()
      this.groupCreated.year=new Date(parseInt(localStorage.groupCreatedDate)).getFullYear()
      this.year=new Date().getFullYear()
    },
    methods: {
      getLastExpense(){
        fetch(`${Helper.backEndAddress}/api/expense/last`, {
          headers: {
            'Authorization': localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.getLastExpense)
          }
          else{
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            if(response.ok){
              response.json().then(
                res => {
                  console.log("Last Expense : "+res)
                  this.lastExpense=res
                  this.isLastAvailable = true
                }
              )
            }  
          }      
        })
      },
      getThisYearTotalPeriodPayed: function(totalPeriodPayed,year){
        let monthList=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Des"]
        this.monthPaid=[]
        let yearDiff = year - this.groupCreated.year
        totalPeriodPayed= totalPeriodPayed - (yearDiff*12)
        let result = totalPeriodPayed + this.groupCreated.month
        result = result > 12 ? 12 : result
        result = result > 0 ? result :  0
        this.monthPaid=monthList
        console.log(this.year)
        this.monthNotYetPaid=(this.monthPaid.splice(result))
      },
      getDashboardData() {
        fetch(`${Helper.backEndAddress}/api/dashboard?email=${this.email}`, {
          headers: {
            'Authorization': localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.getDashboardData)
          }
          else{
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
            res => {
              console.log(res);
              this.dashboardData = res;
              //bagi 2 monthList, menjadi 1. array yang berisi bulan yg telah dibayar, 2. aray berisi bulan belum bayar
              this.getThisYearTotalPeriodPayed(res.yourPayment,this.year)
              this.dataPayNow = {
                lastPayment: (res.yourPayment+this.groupCreated.month)%12-1,
                nomorRekening: res.adminAccountNumber,
                namaAdmin: res.adminName,
                regularPayment: res.regularPayment
              }
            }
           )
          }
          
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
    width: 80%;
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

  .cardStyleOneHeader > p {
    margin: 0 auto;
    font-size: 24pt;
    font-weight: bold;
  }
  .cardStyleOneHeader {
    background-color: var(--primary-0);
    text-align: center;
    align-items: center;
    color: white;
    width: 93%;
    height: 70%;
    border-radius: 10px;
    margin: auto;
    display: flex;
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
    font-weight: 700;
    font-style: italic;
  }
  .nextPrev{
    font-size: 22px;
    text-decoration: none;
    padding:0 6px; 
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
