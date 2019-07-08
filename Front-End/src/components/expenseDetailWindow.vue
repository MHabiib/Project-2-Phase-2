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
              <div class="expenseDetailValue">: {{expenseDetail.requester}}</div>
            </div>

            <div class="expenseDetailDate">
              {{expenseDetail.createdDate | dateFormatter}}
            </div>
          </div>

          <div class="expenseDetailSecondRow expenseDetailRow">
            <div class="expenseDetailContainer">
              <div class='expenseDetailLabel'>Cost</div>
              <div class="expenseDetailValue">: Rp {{expenseDetail.price | thousandSeparators}}</div>
            </div>
          </div>

          <div class="expenseDetailThirdRow expenseDetailRow">
            <div class="expenseDetailContainer">
              <div class='expenseDetailLabel'>Title</div>
              <div class="expenseDetailValue">: {{expenseDetail.title}}</div>
            </div>
          </div>

          <div class="expenseDetailFourthRow expenseDetailRow">
            {{expenseDetail.detail}}
          </div>

          <div class="expenseDetailFifthRow expenseDetailRow">
            <div class="expenseDetailContainer">
              <div class='expenseDetailLabel'>Status</div>
              <div class="expenseDetailValue">: {{expenseDetail.status | statusChecker}}</div>
            </div>

            <div class="expenseDetailButton">
              <div
                :class="{disableButton: disableButton ,rejectButton: !disableButton}"
                @click="updateExpenseStatus(expenseDetail.idExpense ,false)"
              >
                Reject
              </div>

              <div
                :class="{disableButton: disableButton ,acceptButton: !disableButton}"
                @click="updateExpenseStatus(expenseDetail.idExpense ,true)"
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
  export default {
    props: ['expenseId'],
    methods: {
      closeExpenseDetailWindow() {this.$emit('closeExpenseDetailWindow')},
      updateExpenseStatus(id, status) {
        if(this.disableButton !== true) {
          this.disableButton = true;
          fetch(`http://localhost:8088/api/expense/managementExpense`, {
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
            if(response.ok) {
              this.getExpenseData(this.expenseId);
              this.$emit('refreshData')
            }
          })
        }
      },
      checkStatus(status) {if(status !== null) {this.disableButton = true}},
      getExpenseData(id) {
        fetch(`http://localhost:8088/api/expense/${id}`, {
          headers: {Authorization: localStorage.getItem('accessToken')}
        })
        .then(response => {
          response.json().then(
            res => {
              this.expenseDetail = res;
              this.checkStatus(res.status);
            }
          )
        })
      }
    },
    created() {this.getExpenseData(this.expenseId);},
    data: function() {
      return {
        expenseDetail: {},
        disableButton: false
      }
    },
    filters: {
      statusChecker(status) {
        switch(status) {
          case true: return 'Accepted'
          case false: return 'Rejected'
          case null: return 'Waiting Response'
        }
      }
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
</style>
