<template>
  <div class='fixedPosition'>
    <div class='expenseDetailWindow'>
      <div class='expenseWindowSize'>
        <div class="expenseDetailHeader">
          <div>Expense Detail</div>
          <div class="closeButton" @click="closeExpenseDetailWindow">Close</div>
        </div>

        <div class="expenseDetailBody">
          <div class="expenseDetailFirstRow expenseDetailRow">
            <div class="expenseDetailContainer">
              <div class='expenseDetailLabel'>Requested By</div>
              <div class="expenseDetailValue">: {{newExpenseDetail.requester}}</div>
            </div>

            <div class="expenseDetailDate">
              {{newExpenseDetail.createdDate | dateFormatter}}
            </div>
          </div>

          <div class="expenseDetailSecondRow expenseDetailRow">
            <div class="expenseDetailContainer">
              <div class='expenseDetailLabel'>Cost</div>
              <div class="expenseDetailValue">: Rp {{newExpenseDetail.price | thousandSeparators}}</div>
            </div>
          </div>

          <div class="expenseDetailThirdRow expenseDetailRow">
            <div class="expenseDetailContainer">
              <div class='expenseDetailLabel'>Title</div>
              <div class="expenseDetailValue">: {{newExpenseDetail.title}}</div>
            </div>
          </div>

          <div class="expenseDetailFourthRow expenseDetailRow">
            {{newExpenseDetail.detail}}
          </div>

          <div class="expenseDetailFifthRow expenseDetailRow">
            <div class="expenseDetailContainer">
              <div class='expenseDetailLabel'>Status</div>
              <div class="expenseDetailValue">: {{newExpenseDetail.status | statusChecker}}</div>
            </div>

            <div class="expenseDetailButton" v-if="role !== 'MEMBER' && newExpenseDetail.status===null">
              <div
                :class="{disableButton: disableButton ,rejectButton: !disableButton}"
                @click="updateExpenseStatus(newExpenseDetail.idExpense ,false)"
              >
                Reject
              </div>

              <div
                :class="{disableButton: disableButton ,acceptButton: !disableButton}"
                @click="updateExpenseStatus(newExpenseDetail.idExpense ,true)"
              >
                Accept
              </div>
              <div v-show="loading" style="text-align:center">
                <div class="loading-center"><div></div><div></div><div></div><div></div></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div></template>

<script>
  import Helper from '../../Helper';

  const trueOrFalse =[false,true]

  export default {
    props: ['expenseDetail'],
    data: function() {
      return {
        disableButton: false,
        dStatus:null,
        role:localStorage.getItem('role'),
        newExpenseDetail:{},
        loading:false,
      }
    },
    methods: {
      closeExpenseDetailWindow() {this.$emit('closeExpenseDetailWindow')},
      updateExpenseStatus(id, status) {
        if(this.disableButton !== true) {
          this.dStatus = status;
          this.disableButton = true;
          this.loading = true
          fetch(`${Helper.backEndAddress}/api/expense/managementExpense`, {
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
            if(response.status==401 || response.status==403){
              alert('Oops!Something wrong happened, please redo your action.')
              Helper.getNewToken(this.updateExpenseStatus.bind(null,this.expenseDetail.idExpense,this.dStatus))
            }
            else if(response.status==400 ){
              alert('Oops! Your group balance not enough!')
              Helper.getNewToken(this.updateExpenseStatus.bind(null,this.expenseDetail.idExpense,this.dStatus))
            }
            else if(response.ok) {
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              this.getExpenseData(this.newExpenseDetail.idExpense);
              this.$emit('refreshData')
            }
            else{
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              alert('Oops! Something wrong happened.\n Please try again.')
              this.$emit('refreshData')
            }
            this.loading=false
          })
        }
      },
      checkStatus(status) {if(status !== null) {this.disableButton = true}},
      getExpenseData(id) {
        fetch(`${Helper.backEndAddress}/api/expense/${id}`, {
          headers: {Authorization: localStorage.getItem('accessToken')}
        })
        .then(response => {
          if(response.status==401 || response.status==403){
            console.log('IdExpense : '+this.newExpenseDetail.idExpense)
            Helper.getNewToken(this.getExpenseData.bind(null,this.newExpenseDetail.idExpense))
          }  else {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            if(response.ok){
              response.json().then(
                res => {
                  this.newExpenseDetail = res;
                  this.checkStatus(res.status);
                }
              )
            }
          }
        })
      }
    },

    created() {
      this.newExpenseDetail=Object.assign({},this.expenseDetail)
      this.role = localStorage.getItem('role')
    },
    
    filters: {
      statusChecker(status) {
        switch(status) {
          case true: return 'Accepted'
          case false: return 'Rejected'
          case null: return 'Waiting Response'
        }
      }/*,
      roleChecker(role){
        switch (role) {
          case localStorage.getItem('role')==='SUPER_ADMIN':return 'SUPER_ADMIN'
          case localStorage.getItem('role')==='GROUP_ADMIN':return 'GROUP_ADMIN'
          case localStorage.getItem('role')==='MEMBER':return 'MEMBER'
        }
      }*/
    }
  }
</script>

<style>
  .fixedPosition {position: absolute;}

  .expenseDetailWindow {
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

  .expenseDetailHeader {
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

  .expenseWindowSize {
    width: 40%;
    height: 40%;
    position: relative;
    top: -50px;
  }

  .closeButton {
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
  }

  .expenseDetailBody {
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

  .expenseDetailFirstRow,
  .expenseDetailFifthRow,
  .expenseDetailButton,
  .expenseDetailRequester,
  .expenseDetailSecondRow {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .expenseDetailRow {margin-top: 15px;}

  .expenseDetailContainer {
    display: flex;
    width: 300px;
  }

  .expenseDetailFourthRow {
    border: solid 1px var(--primary-1);
    padding: 10px;
    height: 100px;
    overflow: scroll;
    border-radius: 5px;
  }

  .expenseDetailButton {
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

  .expenseDetailLabel {
    width: 105px;
    font-weight: 600;
  }
  .loading-center {
    display: block;
    position: absolute;
    top:43%;
    right:43%;
    width: 64px;
    height: 64px;
  }
  .loading-center div {
    box-sizing: border-box;
    display: flex;
    position: absolute;
    width: 51px;
    height: 51px;
    margin: 6px;
    border: 6px solid var(--primary-2);
    border-radius: 50%;
    animation: loading-center 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
    border-color: var(--primary-2) transparent transparent transparent;
  }
  .loading-center div:nth-child(1) {
    animation-delay: -0.45s;
  }
  .loading-center div:nth-child(2) {
    animation-delay: -0.3s;
  }
  .loading-center div:nth-child(3) {
    animation-delay: -0.15s;
  }
  @keyframes loading-center {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }


</style>
