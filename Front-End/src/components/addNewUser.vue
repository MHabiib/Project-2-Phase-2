<template>
  <div class='fixedPosition'>
    <div class='createNewExpenseWindow'>
      <div class='createNewExpenseWindowSize'>
        <div class="createNewExpenseHeader">
          <div>Add New User</div>
          <div class='buttonGroup'>
            <div class="closeButton" @click="closeAddNewUserWindow">Close</div>
            <div class="okButton">OK</div>
          </div>
        </div>

        <div class="createNewExpenseBody">
          <input class='singleLineInput' type="text" placeholder='Nama' v-model='namaInput'/>
          <input class='singleLineInput' type="text" placeholder='Email' v-model='emailInput'/>
          <input class='singleLineInput' type="text" placeholder='Nomor Handphone' v-model='nomorHpInput' @keypress="checkChar"/>
          
          <select name="selectGroup" id="selectGroup" class='selectGroup'>
            <option value="none" style="display: none">Select Group</option>
            <option value="group">group</option>
          </select>

          <select name="selectRole" id="selectRole" class='selectRole'>
            <option value="none" style="display: none">Select Role</option>
            <option value="group">role</option>
          </select>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data: function() {
      return {
        namaInput: '',
        emailInput: '',
        nomorHpInput: ''
      }
    },
    methods: {
      closeAddNewUserWindow() {
        this.$emit('closeAddNewUserWindow')
      },
      checkChar(e) {
        if(e.keyCode >= 48 && e.keyCode <= 57 || e.keyCode === 8) {} else {
          e.preventDefault();
        }
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
    height: 25%;
    position: relative;
    top: -60px;
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

  .closeButton {
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
  }

  .okButton {
    background-color: #fff;
    padding: 7px;
    color: var(--primary-0);
    border-radius: 50%;
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
    margin-left: 10px;
  }

  .buttonGroup {
    display: flex;
    align-items: center;
  }

  .singleLineInput {
    outline: none;
    padding: 10px;
    border: solid 1px var(--primary-1);
    color: var(--primary-4);
    width: 100%;
    margin-top: 10px;
    box-sizing: border-box;
    border-radius: 5px;
  }

  .singleLineInput::placeholder {
    color: var(--primary-1);
  }
  
  .selectGroup, .selectRole {
    outline: none;
    border: none;
    padding: 10px !important;
    color: var(--primary-4);
    width: 100px;
    height: 30px;
    background-color: var(--lightColor);
    margin-top: 12px;
    margin-right: 12px;
  }
</style>
