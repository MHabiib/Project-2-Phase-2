<template>
  <div class='fixedPosition'>
    <div class='expenseDetailWindow'>
      <div class='expenseWindowSize'>
        <div class="expenseDetailHeader">
          <div>Expense Detail</div>
          <div class="closeButton" @click="closeExpenseDetailWindow">Close</div>
        </div>

        <div class="expenseDetailBody">
          {{expenseDetail.name}}
        </div>
      </div>
    </div>
  </div></template>

<script>
  export default {
    props: ['detailExpenseSelected'],
    methods: {
      closeExpenseDetailWindow() {
        this.$emit('closeExpenseDetailWindow')
      }
    },
    created() {
      this.expenseDetail = this.detailExpenseSelected
    },
    data: function() {
      return {
        expenseDetail: {}
      }
    }
  }
</script>

<style>
  .fixedPosition {
    position: absolute;
  }

  .expenseDetailWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
  }

  .expenseDetailHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    padding: 16px 12px;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    width: 90%;
    margin: auto;
    position: relative;
    z-index: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .expenseWindowSize {
    width: 40%;
    height: 40%;
  }

  .closeButton {
    font-size: 12px;
    cursor: pointer;
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
    overflow: auto;
  }
</style>
