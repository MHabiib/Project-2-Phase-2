<template>
  <div class='sidebarComponent'>
    <div>
      <div class="logoBlibli">
        <img src="../assets/logo-blibli.png" alt="Logo Blibli" class="logoBlibli">
      </div>

      <div class="menuContainer">
        <div class="menuParent dashBoard" :class='{ activeMenu: isDashboard }'>
          <div v-show="isDashboard" class='littleBlue'></div>
          <div class="menuWrapper" @click="changeView('/dashboard')">
            <img src="../assets/dashboard-icon.png" alt="Dashboard" class='menuIcon'>
            Dashboard
          </div>
        </div>

        <div class="menuParent">
          <div class="menuWrapper" @click="groupDetailExpand = !groupDetailExpand">
            <img src="../assets/detail-icon.png" alt="Dashboard" class='menuIcon'>
            Group Detail
          </div>

          <div class="spacer" v-show="groupDetailExpand"></div>

          <div
            class="menuChild"
            v-show="groupDetailExpand"
            :class='{ activeSmallMenu: isOverview }'
            @click='changeView("/overview")'
          >
            <div :class='{litteSmallBlue: isOverview}'></div>
            <div class="menuChildText">Overview</div>
            <div />
          </div>

          <div
            class="menuChild"
            v-show="groupDetailExpand"
            :class='{ activeSmallMenu: isExpenses }'
            @click='changeView("/expenses")'
          >
            <div :class='{litteSmallBlue: isExpenses}'></div>
            <div class="menuChildText">Expenses</div>
            <div />
          </div>

          <div
            class="menuChild"
            v-show="groupDetailExpand"
            :class='{ activeSmallMenu: isMembers }'
            @click='changeView("/members")'
          >
            <div :class='{litteSmallBlue: isMembers}'></div>
            <div class="menuChildText">Members</div>
            <div />
          </div>
        </div>




        <div class="menuParent" :class='{ activeMenu: isPayment }'>
          <div v-show="isPayment" class='littleBlue'></div>
          <div class="menuWrapper" @click="paymentExpand = !paymentExpand">
            <img src="../assets/invoice-icon.png" alt="Dashboard" class='menuIcon'>
            Payment
          </div>
          <div class="spacer" v-show="paymentExpand"></div>
          <div
            class="menuChild"
            v-show="paymentExpand"
            :class='{ activeSmallMenu: isPaymentHistory }'
            @click='changeView("/paymenthistory")'
          >
            <div :class='{litteSmallBlue: isPaymentHistory}'></div>
            <div class="menuChildText">History</div>
            <div />
          </div>
          <div
            class="menuChild"
            v-show="paymentExpand"
            :class='{ activeSmallMenu: isManagePayment }'
            @click='changeView("/managepayment")'
          >
            <div :class='{litteSmallBlue: isManagePayment}'></div>
            <div class="menuChildText">Manage</div>
            <div />
          </div>



        </div>

        <div class="menuParent management" v-if='isAdmin'>
          <div class="menuWrapper" @click="managementExpand = !managementExpand">
            <img src="../assets/management-icon.png" alt="Dashboard" class='menuIcon'>
            Management
          </div>

          <div class="spacer" v-show="managementExpand"></div>

          <div
            class="menuChild"
            v-show="managementExpand"
            :class='{ activeSmallMenu: isManageUser }'
            @click='changeView("/manage-user")'
          >
            <div :class='{litteSmallBlue: isManageUser}'></div>
            <div class="menuChildText">User</div>
            <div />
          </div>

          <div
            class="menuChild"
            v-show="managementExpand"
            :class='{ activeSmallMenu: isManageGroup }'
            @click='changeView("/manage-group")'
          >
            <div :class='{litteSmallBlue: isManageGroup}'></div>
            <div class="menuChildText">Group</div>
            <div />
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
  import Helper from '../../Helper'

  export default {
    data() {
      return {
        groupDetailExpand: true,
        managementExpand: true,
        paymentExpand : true,
        isPaymentHistory:window.location.href === 'http://localhost:3000/paymenthistory',
        isManagePayment:window.location.href === 'http://localhost:3000/managepayment',
        isDashboard: window.location.href === 'http://localhost:3000/dashboard',
        isPayment: window.location.href === 'http://localhost:3000/payment',
        isOverview: window.location.href === 'http://localhost:3000/overview',
        isExpenses: window.location.href === 'http://localhost:3000/expenses',
        isMembers: window.location.href === 'http://localhost:3000/members',
        isManageGroup: window.location.href === 'http://localhost:3000/manage-group',
        isManageUser: window.location.href === 'http://localhost:3000/manage-user',
        isAdmin: localStorage.getItem('role') === 'SUPER_ADMIN'
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
      },
    },
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
    width: 100%;
    text-align: center;
    margin: auto;
    height: 31px;
    display: flex;
    justify-content: space-between;
    align-items: center;
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

  .activeSmallMenu {
    background-color: white;
  }

  .litteSmallBlue {
    background-color: var(--primary-0);
    height: 40px;
    width: 7px;
  }

  .menuChildText {
    width: 50px;
    text-align: left;
  }
</style>
