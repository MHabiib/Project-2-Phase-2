<template>
  <div class='fixedPosition'>
    <div class='createNewExpenseWindow'>
      <div class='createNewExpenseWindowSize'>
        <div class="createNewExpenseHeader">
          <div>Invite Members</div>
          <div class="okButton" @click="closeInviteWindow">Done</div>
        </div>

        <div class="createNewExpenseBody">

        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data: function() {
      return {
        biaya: '',
        namaPengeluaran: '',
        deskripsiPengeluaran: ''
      }
    },
    created() {

    },
    methods: {
      closeInviteWindow() {
        this.$emit('closeInviteWindow')
      },
      createNewExpense() {
      }
    }
  }
</script>

<style>
  .fixedPosition {
    position: absolute;
  }

  .createNewExpenseWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
  }

  .createNewExpenseHeader {
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
    font-weight: 600;
  }

  .createNewExpenseWindowSize {
    width: 35%;
    height: 30%;
  }

  .createNewExpenseBody {
    background-color: white;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
    position: relative;
    top: -30px;
    padding: 20px;
    padding-top: 32px;
    height: 100%;
  }

  .okButton {
    background-color: #fff;
    padding: 5px 10px;
    color: var(--primary-0);
    border-radius: 5px;
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
    margin-left: 10px;
  }

  .okButton:hover {background-color: var(--lightColor)}
  .okButton:active {background-color: #fff}
</style>
