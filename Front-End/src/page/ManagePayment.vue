<template>
  <div class='paymentsComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection :headerTitle="'Group '+groupName+'\'s Members\' Payment'"/>

      <div class="paymentsBodySection">
        <div class="paymentsTableHeader">
          <div class="paymentsTableHeaderTitle">
            Manage Payments
          </div>

          <div class="myParent" style='display: flex;'>
            <input class='paymentTableSearch' type="text" :placeholder="'Search by '+this.searchPlaceHolder" v-model='searchQuery'/>
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

        <div class="managePaymentsTableBody">
          <table>
            <thead>
              <tr>
                <th>&nbsp;&nbsp;</th>
                <th>PaymentDate</th>
                <th>Email</th>
                <th>Status</th>
                <th>Periode</th>
                <th>Price</th>
              </tr>
            </thead>
            <tbody id='infiniteScroll'>
              <tr
                class='paymentRow'
                v-for='(payment, index) in paymentList' :key='index'
                @click="openPaymentDetailWindow(payment)">
                    <td>{{index+1}}.</td>
                    <td>{{payment.paymentDate | dateFormatter}}</td>
                    <td>{{payment.email}}</td>
                    <td>{{payment.isRejected | statusChecker}}</td>
                    <td>{{payment.periode}}</td>
                    <td>Rp {{payment.price | thousandSeparators}}</td>
              </tr>
               <div style="text-align:center">
                <div v-show="loading" class="lds-ring"><div></div><div></div><div></div><div></div></div>
              </div>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <PaymentDetailWindow
      v-if='showPaymentDetailWindow'
      @closePaymentDetailWindow="closePaymentDetailWindow"
      @refreshData="searchData(0)"
      :payment="this.detailPaymentSelected"
    />

  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';
  import PaymentDetailWindow from '../components/PaymentDetailWindow';
  import Helper from '../../Helper';
  import Multiselect from 'vue-multiselect'
  import { clearTimeout } from 'timers';

  export default {
    components: {
      'PaymentDetailWindow': PaymentDetailWindow,
      'multiselect': Multiselect,
    },
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);     
       },
      searchPlaceHolder:function(){
        return this.filter == "date before"  || this.filter == "date after"  ? "(dd-MMMM-yyyy)" : this.filter
      }
    },
    data: function() {
      return {
        dataPayment:{},
        paymentList:[],
        detailPaymentSelected: '',
        showPaymentDetailWindow: false,
        searchQuery: '',
        filter:'email',
        options:['email','status','date before','date after','periode <','periode >'],
        loading:false,
      }
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
    created() {this.searchData(0);},
    methods: {
      searchData(page) {
        this.loading=true;
        console.log(this.filter+" : " +this.searchQuery)
        fetch(`${Helper.backEndAddress}/api/payment/search?query=${this.filter}:${this.searchQuery}&page=${page}`, {
          headers: {Authorization: localStorage.getItem('accessToken')}
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.searchData.bind(null,0))
          }
          else{
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                this.paymentList=res.content
                this.dataPayment = res;
                setTimeout(e=>{this.loading=false},500)

                // this.loading=false
              }
            )
          }         
        })
      },
      getPendingPayment(page) {
        this.loading=true;
        console.log(this.filter+" : " +this.searchQuery)

        // fetch(`${Helper.backEndAddress}/api/payment?isPaid=false&page=${page}`, {
        fetch(`${Helper.backEndAddress}/api/payment/search?query=${this.filter}:${this.searchQuery}&page=${page}`, {
          headers: {Authorization: localStorage.getItem('accessToken')}
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.getPendingPayment.bind(null,this.dataPayment.pageable.pageNumber+1))
          }
          else{
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                this.paymentList=this.paymentList.concat(res.content)
                this.dataPayment = res;
                setTimeout(e=>{this.loading=false},500)
                // this.loading=false;
              }
            )
          }         
        })
      },
      openPaymentDetailWindow(payment) {
        this.showPaymentDetailWindow = true;
        this.detailPaymentSelected = payment;
      },
      closePaymentDetailWindow() {this.showPaymentDetailWindow = false;},


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
          case null: return 'Pending'
        }
      },
      scroll() {
        const paymentTable = document.querySelector('#infiniteScroll');
        paymentTable.addEventListener('scroll', e => {
          // console.log(paymentTable.scrollTop + paymentTable.clientHeight+" : "+paymentTable.scrollHeight)
          if((paymentTable.scrollTop + paymentTable.clientHeight)+1>= paymentTable.scrollHeight) {
            console.log('infinite scroll triggered!')
            if(this.dataPayment.last!=true & this.loading==false){   
              this.loading=true;
              this.getPendingPayment(this.dataPayment.pageable.pageNumber+1)
              console.log('get more data!')
            }
          }
        })
      },
      searchPayment(){

      }
    },

    filters: {
      statusChecker(status) {
        switch(status) {
          case true: return 'Rejected'
          case false: return 'Accepted'
          case null: return 'Pending'
        }
      }
    },    
  }
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style>
  .paymentsComponent {display: flex;}

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
    font-weight: 600;

    width: 90%;
    margin: auto;
    position: relative;
    z-index: 1;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);

  }

  .paymentsTableHeaderTitle {
    font-size: 20px;
    font-weight: 600;
  }

  .managePaymentsTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-4);
    text-align: center;
  }

  .managePaymentsTableBody table {width: 100%;}

  .managePaymentsTableBody tbody {
    height: 55vh;
    max-height:400px;
    overflow-y: auto;
    overflow-x: auto;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }

  .managePaymentsTableBody tbody tr td {text-align:left;padding-top: 12px; padding-bottom: 12px;}
  .managePaymentsTableBody thead tr, .managePaymentsTableBody tbody {text-align:left;display: block; box-sizing: border-box;}

  .managePaymentsTableBody tbody td:nth-child(1), .managePaymentsTableBody thead tr th:nth-child(1) {
    width: 1.5vw;
    text-align: left;
    padding-left: 10px;
  }

  .managePaymentsTableBody tbody td:nth-child(2), .managePaymentsTableBody thead tr th:nth-child(2) {
    width: 10vw;
    text-align: left;
    padding-left: 10px;
  }

  .managePaymentsTableBody tbody td:nth-child(3), .managePaymentsTableBody thead tr th:nth-child(3) {width: 20vw;}
  .managePaymentsTableBody tbody td:nth-child(4), .managePaymentsTableBody thead tr th:nth-child(4) {width: 11vw;}
  .managePaymentsTableBody tbody td:nth-child(5), .managePaymentsTableBody thead tr th:nth-child(5) {width: 10vw;}

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
    height: 40px;
    box-sizing: border-box;
  }

  .paymentTableSearch::placeholder {color: var(--primary-1)}
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
  .paymentTableAddNew {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    font-size: 14px;
    margin-left: 10px;
    cursor: pointer;
  }

  .paymentTableAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
  }

  .paymentTableAddNew:active {background-color: var(--primary-4);}
  .paymentRow {cursor: pointer;}
  .paymentRow:hover {background-color: white;}
  .paymentRow:active {background-color: rgba(255, 255, 255, .5);}
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
