<template>
  <div class='fixedPosition'>
    <div class='createNewExpenseWindow'>
      <div class='createNewExpenseWindowSize'>
        <div class="createNewExpenseHeader">
          <div>Request Expense</div>
          <div class='buttonGroup'>
            <div class="closeButton" @click="closeCreateNewExpenseWindow">Close</div>
            <div class="okButton" @click="createNewExpense">OK</div>
          </div>
        </div>

        <div class="createNewExpenseBody">
          <input class='singleLineInput price' type="text" placeholder='Estimated Cost' v-model='biaya' @keypress="checkChar" @change="checkChange"/>
          <input class='singleLineInput' type="text" placeholder='Expense Title' v-model='namaPengeluaran'/>
          <textarea class='multiLineInput' type="text" placeholder='Expense Details...' v-model='deskripsiPengeluaran'/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Helper from '../../Helper';

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
      closeCreateNewExpenseWindow() {
        this.$emit('closeCreateNewExpenseWindow')
      },
      checkChar(e) {
        if(e.keyCode >= 48 && e.keyCode <= 57 || e.keyCode === 8) {} else {
          e.preventDefault();
        }
      },
      checkChange(e) {
        let biayaWithOutDot = '';
        this.biaya.toString().split('').forEach(element => {
          if(element !== '.') {biayaWithOutDot += element}
        })
        this.biaya = this.thousandSeparators(biayaWithOutDot);
      },
      thousandSeparators: function(numbers) {
        if(numbers === undefined) {
          return '';
        } else {
          let result = '';
          let counter = 0;
          for (let i = numbers.toString().length ; i >= 0 ; i--){
              if(counter % 3 === 0 && counter !== 0 && counter !== numbers.toString().length){
                  result = '.' + numbers.toString().substr(i, 1) + result;
              } else {
                  result = numbers.toString().substr(i, 1) + result;
              }
              counter += 1;
          }
          return result;
        }
      },
      createNewExpense() {
        if(!this.validateInput()) return;
        let biayaWithOutDot = '';
        this.biaya.toString().split('').forEach(element => {
          if(element !== '.') {biayaWithOutDot += element}
        })

        fetch(`${Helper.backEndAddress}/api/expense`, {
          method: 'POST',
          headers: {
            Authorization: localStorage.getItem('accessToken'),
            Accept: 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            "groupName": localStorage.getItem('groupName'),
            "detail": this.deskripsiPengeluaran,
            "price": biayaWithOutDot,
            "title": this.namaPengeluaran,
            "requester": localStorage.getItem('userEmail')
          })
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.createNewExpense)
          }
          else if(response.ok) {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            console.log('asd')
            alert('Request expense succeed!\nWaiting for admin group confirmation.')
            console.log('dsa')
            this.$emit('refreshData');
            this.$emit('closeCreateNewExpenseWindow');
          } else {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            alert(`Oops! Something wrong happened ${response.status}`);
          }
        })
        .catch(err => {
          alert('Terjadi kesalahan. Harap periksa kembali koneksi internet Anda.');
          console.log(err);
        })
      },
      validateInput(){
        if(this.biaya === ''){
          alert('Please input the estimated cost.')
          return false
        } else if(this.namaPengeluaran === '') {
          alert('Please input the expense title.')
          return false
        } else if(this.deskripsiPengeluaran === '') {
          alert('Please input the expense details.')
          return false
        } else {
          return true
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
    height: 30%;
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

  .singleLineInput, .multiLineInput {
    outline: none;
    padding: 10px;
    border: solid 1px var(--primary-1);
    color: var(--primary-4);
    width: 100%;
    margin-top: 10px;
    box-sizing: border-box;
    border-radius: 5px;
  }

  .singleLineInput::placeholder, .multiLineInput::placeholder {
    color: var(--primary-1);
  }

  .multiLineInput {
    height: 50%;
    resize: none;
  }
</style>
