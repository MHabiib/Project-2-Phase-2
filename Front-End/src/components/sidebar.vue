<template>
  <div class='sidebarComponent'>
    <div>
      <div class="logoBlibli">
        <img src="../assets/logo-blibli.png" alt="Logo Blibli" class="logoBlibli">
      </div>

      <div class="menuContainer">
        <div class="menuParent" :class='{ activeMenu: isDashboard }'>
          <div v-show="isDashboard" class='littleBlue'></div>
          <div class="menuWrapper" @click="changeView('/dashboard')">
            <img src="../assets/dashboard-icon.png" alt="Dashboard" class='menuIcon'>
            Dashboard
          </div>
        </div>

        <div class="menuParent">
          <div class="menuWrapper" @click="groupDetailExpand = !groupDetailExpand">
            <img src="../assets/invoice-icon.png" alt="Dashboard" class='menuIcon'>
            Group Detail
          </div>

          <div class="spacer" v-show="groupDetailExpand"></div>

          <div class="menuChild" v-show="groupDetailExpand">
            Overview
          </div>

          <div class="menuChild" v-show="groupDetailExpand">
            Expenses
          </div>

          <div class="menuChild" v-show="groupDetailExpand">
            Members
          </div>
        </div>

        <div class="menuParent" :class='{ activeMenu: isPayment }'>
          <div v-show="isPayment" class='littleBlue'></div>
          <div class="menuWrapper" @click="changeView('/payment')">
            <img src="../assets/invoice-icon.png" alt="Dashboard" class='menuIcon'>
            Payment
          </div>
        </div>
      </div>
    </div>

    <div class="logoutButton" @click='logout(); backToLogin()'>
      Logout
    </div>
  </div>
</template>

<script>
  document.title = 'Dashboard | Team Cash Flow Management'
  import { mapActions } from 'vuex';

  export default {
    data() {
      return {
        groupDetailExpand: true,
        isDashboard: window.location.href === 'http://localhost:3000/dashboard',
        isPayment: window.location.href === 'http://localhost:3000/payment',
      }
    },
    methods: {
      ...mapActions([
        'logout'
      ]),
      backToLogin() {
        this.$router.push('/login')
      },
      changeView(view) {
        this.$router.push(view)
      }
    }
  }
</script>

<style>
  .sidebarComponent {
    width: 270px;
    padding: 35px 0 20px 0;
    background-color: var(--lightColor);
    min-height: 92vh;
    box-shadow: 5px 0 10px rgba(0, 0, 0, 0.16);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .logoBlibli {
    width: 178px;
    margin: auto;
    margin-bottom: 20px;
  }

  .menuParent {
    padding: 17px 0;
    box-sizing: border-box;
    cursor: pointer;
  }

  .menuIcon {
    width: 30px;
    margin-right: 10px;
  }

  .menuWrapper {
    display: flex;
    align-items: center;
    width: 160px;
    margin: auto;
    font-size: 14px;
    color: var(--darkColor);
    font-weight: 500;
  }

  .menuChild {
    color: var(--darkColor);
    font-size: 14px;
    padding: 12px 0;
    width: 50px;
    margin: auto;
  }

  .activeMenu {
    background-color: white;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16);
  }

  .spacer {
    margin: 15px;
  }

  .logoutButton {
    background-color: var(--darkColor);
    text-align: center;
    color: var(--lightColor);
    padding: 10px;
    width: 80%;
    margin-left: auto;
    margin-right: auto;
    border-radius: 4px;
    cursor: pointer;
  }

  .littleBlue {
    background-color: var(--primary-0);
    height: 64px;
    width: 9px;
    position: absolute;
    margin-top: -17px;
  }
</style>
