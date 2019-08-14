<template>
  <div class='fixedPosition'>
    <div class='paymentDetailWindow'>
      <div class='paymentWindowSize'>
        <div class="paymentDetailHeader">
          <div>Payment Id : {{paymentDetail.idPayment}}</div>
          <div class="closeButton" @click="closePaymentDetailWindow">Close</div>
        </div>

        <div class="paymentDetailBody">
          <div class="paymentDetailFirstRow paymentDetailRow">
            <div class="paymentDetailContainer">
              <div class='paymentDetailLabel'>Nama Pengirim</div>
              <div class="paymentDetailValue">: {{paymentDetail.namaPengirim}}</div>
            </div>

            <div class="paymentDetailDate">
              {{paymentDetail.paymentDate | dateFormatter}}
            </div>
          </div>
          <div class="paymentDetailSecondRow paymentDetailRow ">
            <div class="paymentDetailContainer">
              <div class='paymentDetailLabel'>Bayar Untuk</div>
              <div class="paymentDetailValue">: {{paymentDetail.emailMemberLain==""||paymentDetail.emailMemberLain==null? paymentDetail.email:paymentDetail.emailMemberLain}}</div>
            </div>
          </div>
          <div class="paymentDetailSecondRow paymentDetailRow ">
            <div class="paymentDetailContainer">
              <div class='paymentDetailLabel'>Total Period</div>
              <div class="paymentDetailValue">: {{paymentDetail.periode}} <span style="font-weight:600">( Rp {{paymentDetail.price |thousandSeparators}} )</span></div>
            </div>


          </div>

          <div class="paymentDetailThirdRow paymentDetailRow">
            <div class="paymentDetailContainer">
              <div class='paymentDetailLabel'>Bukti Pembayaran</div>
              <div class="paymentDetailValue">: </div>

            </div>
          </div>
          <div class="paymentDetailFourthRow paymentDetailRow">
              <zoom-on-hover :img-normal="paymentDetail.imageURL" :scale="1.8"></zoom-on-hover>
          </div>

          <div class="paymentDetailFifthRow paymentDetailRow">
            <div class="paymentDetailContainer">
              <div class='paymentDetailLabel'>Status</div>
              <div class="paymentDetailValue">: {{paymentDetail.isRejected | statusChecker}}</div>
            </div>

            <div class="paymentDetailButton" v-if="role !== 'MEMBER' && paymentDetail.isRejected===null" >
              <div
                :class="{disableButton: disableButton ,rejectButton: !disableButton}"
                @click="updatePaymentStatus(paymentDetail.idPayment ,false)"
              >
                Reject
              </div>

              <div
                :class="{disableButton: disableButton ,acceptButton: !disableButton}"
                @click="updatePaymentStatus(paymentDetail.idPayment ,true)"
              >
                Accept
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div></template>

<script>
  import Helper from '../../Helper';
  import ZoomOnHover from '../components/zoomOnHover';

  export default {
    components:{
      'ZoomOnHover':ZoomOnHover,
    },
    props: ['payment'],
    data: function() {
      return {
        paymentDetail: {},
        disableButton: false,
        dStatus:null,
        role:localStorage.getItem('role'),
      }
    },
    methods: {
      closePaymentDetailWindow() {this.$emit('closePaymentDetailWindow')},
      updatePaymentStatus(id, status) {
        if(this.disableButton !== true) {
          this.dStatus = status;
          this.disableButton = true;
          fetch(`${Helper.backEndAddress}/api/payment/managementPayment`, {
            method: 'PUT',
            headers: {
              'Authorization': localStorage.getItem('accessToken'),
              Accept: 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({
              id: id,
              status: status
            })
          })
          .then(response => {
            console.log(id,status)
            if(response.status==401){
              alert('Ups! something wrong happened.\nPlease redo your action')
              Helper.getNewToken(this.updatePaymentStatus.bind(null,{id,status}))
            }
            else if(response.ok) {
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              console.log('payment updated!')
              response.json().then(
                res=>{
                  this.paymentDetail=res;
                  this.$emit('refreshData')
                }
              )              
            } else{
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              console.log('error!')
              alert('Oops! Something wrong happened when processing request.\nPlease try again.')
            }
          })
        }
      },
      checkStatus(status) {if(status !== null) {this.disableButton = true}},
    },
    created() {this.paymentDetail=Object.assign({},this.payment), this.role = localStorage.getItem('role')},
    
    filters: {
      statusChecker(status) {
        switch(status) {
          case true: return 'Rejected, please made another payment'
          case false: return 'Accepted'
          case null: return 'Pending'
        }
      }
    }
  }
</script>

<style>
  .fixedPosition {position: absolute;}

  .paymentDetailWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
    color: var(--primary-3);
  }

  .paymentDetailHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    padding: 16px 20px;
    box-sizing: border-box;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    width: 95%;
    margin: auto;
    position: relative;
    z-index: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 600;
  }

  .paymentWindowSize {
    width: 30vw;
    height: 80vh;
    position: relative;
    top: -10vh;
  }

  .closeButton {
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
  }

  .paymentDetailBody {
    background-color: white;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
    position: relative;
    top: -30px;
    padding: 20px;
    padding-top: 32px;
    height: 100%;
    overflow: hidden;
    font-size: 14px;
  }

  .paymentDetailFirstRow,
  .paymentDetailFifthRow,
  .paymentDetailButton,
  .paymentDetailRequester,
  .paymentDetailSecondRow {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .paymentDetailRow {margin-top: 15px;}

  .paymentDetailContainer {
    display: flex;
    width: 300px;
  }

  .paymentDetailFourthRow {
    border: solid 1px var(--primary-1);
    text-align: center;
    padding: 5px;
    height: 45vh;
    overflow: scroll;
    border-radius: 5px;
  }

  .paymentDetailButton {
    cursor: pointer;
    color: var(--lightColor);
    font-size: 12px;
  }

  .acceptButton, .rejectButton, .disableButton {
    padding: 7px 10px;
    border-radius: 4px;
    width: 50px;
    text-align: center;
    margin-left: 5px;
  }

  .acceptButton {background-color: var(--primary-0);}
  .rejectButton {background-color: var(--warning);}
  .disableButton {background-color: #999;}
  .acceptButton:hover, .rejectButton:hover {opacity: .9;}
  .acceptButton:active, .rejectButton:active {opacity: 1;}

  .paymentDetailLabel {
    width: 125px;
    font-weight: 600;
  }
</style>
