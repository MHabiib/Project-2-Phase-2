<template>
  <div class='fixedPosition'>
    <div class='createNewExpenseWindow'>
      <div class='createNewExpenseWindowSize'>
        <div class="createNewExpenseHeader">
          <div>Add New User</div>
          <div class='buttonGroup'>
            <div class="closeButton" @click="closeAddNewUserWindow">Close</div>
            <div class="okButton" @click="createNewUser">OK</div>
          </div>
        </div>

        <div class="createNewExpenseBody">
          <input class='singleLineInput' type="text" placeholder='Nama' v-model='namaInput'/>
          <input class='singleLineInput' type="text" placeholder='Email' v-model='emailInput'/>
          <input class='singleLineInput' type="text" placeholder='Nomor Handphone' v-model='nomorHpInput' @keypress="checkChar"/>
          <div style="display: flex; justify-content: space-between">
            <input style='width: 48%' class='singleLineInput' type="text" placeholder='Nama Bank' v-model='namaBankInput'/>
            <input style='width: 48%' class='singleLineInput' type="text" placeholder='Nomor Rekening' v-model='nomorRekeningInput' @keypress="checkChar"/>
          </div>
          <input class='singleLineInput' type="password" placeholder='Buat password baru' v-model='passwordInput'/>
          
          <div class="createNewExpenseFlexLine">
            <select name="selectGroup" id="selectGroup" class='selectGroup' @change="changeGroup($event)">
              <option value="none" style="display: none">Select Group</option>
              <option v-for="(group, index) in groupList" :key="index" :value="group.name">{{group.name}}</option>
            </select>

            <select name="selectRole" id="selectRole" class='selectRole' @change="changeRole($event)">
              <option value="none" style="display: none">Select Role</option>
              <option value="USER">User</option>
              <option value="ADMIN">Admin</option>
            </select>

            <input type="file" @change="changeFile($event)" class="selectFile"/>
          </div>
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
        namaInput: '',
        emailInput: '',
        nomorHpInput: '',
        groupInput: '',
        roleInput: '',
        fileInput: null,
        passwordInput: '',
        nomorRekeningInput: '',
        namaBankInput: '',
        groupList: []
      }
    },
    methods: {
      closeAddNewUserWindow() {
        this.$emit('closeAddNewUserWindow');
      },
      checkChar(e) {
        if(e.keyCode >= 48 && e.keyCode <= 57 || e.keyCode === 8) {} else {
          e.preventDefault();
        }
      },
      getAllGroup() {
        fetch(`${Helper.backEndAddress}/api/group`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.ok) {
            response.json().then(
              res => {
                this.groupList = res;
              }
            )
          } else {
            alert('Sesi Anda telah berakhir, harap refresh kembali halaman ini.');
            window.location.reload();
          }
        })
        .catch(err => {
          alert('Terjadi kesalahan ketika mengambil data. Harap pastikan koneksi internet Anda tersedia.');
          console.log(err);
        })
      },
      changeGroup(e) {
        this.groupInput = e.target.value;
      },
      changeRole(e) {
        this.roleInput = e.target.value;
      },
      changeFile(e) {
        this.fileInput = e.target.files[0];
      },
      createNewUser() {
        if(this.namaInput === '') {
          alert('Harap masukkan nama user.')
        } else if(this.emailInput === '') {
          alert('Harap masukkan email user.')
        } else if(this.nomorHpInput === '') {
          alert('Harap masukkan nomor handphone user')
        } else if(this.groupInput === '') {
          alert('Harap pilih grup user.')
        } else if(this.fileInput === null) {
          alert('Harap masukkan foto user.')
        } else {
          let formData = new FormData();

          formData.append('user', JSON.stringify({
            name: this.namaInput,
            email: this.emailInput,
            phone: this.nomorHpInput,
            groupName: this.groupInput,
            role: this.roleInput,
            password: this.passwordInput,
            rekening: this.nomorRekeningInput,
            // namaBank: this.namaBankInput
          }))

          formData.append('file', this.fileInput);

          fetch(`${Helper.backEndAddress}/api/user`, {
            method: 'POST',
            headers: {
              Authorization: localStorage.getItem('accessToken')
            },
            body: formData
          })
          .then(response => {
            if(response.ok) {
              alert('User baru berhasil ditambahkan.');
              this.closeAddNewUserWindow();
              this.$emit('refreshData');
            } else {
              console.log(response);
              alert('Terjadi kesalahan. Harap periksa kembali input Anda atau refresh kembali halaman ini.');
            }
          })
          .catch(err => {
            alert('Terjadi kesalahan. Harap periksa koneksi internet Anda.');
            console.log(err);
          })
        }
      },
    },
    created() {
      this.getAllGroup();
    },
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
    height: fit-content;
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

  .selectFile {
    color: var(--primary-0);
    margin-top: 12px;
    width: 80%;
  }

  .createNewExpenseFlexLine {
    display: flex;
    align-items: center;
  }
</style>
