<template>
  <div class='manageUserComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Manage User'/>

      <div class="managementTable">
        <div class="managementTableHead">
          <div class="managementTableHeadTitle">
            All User
          </div>

          <div style='display: flex;'>
            <input class='managementTableHeadSearch' type="text" placeholder="Search by anything" />
            <div class="managementTableHeadAddNew" @click='openAddNewUserWindow'>Add New User</div>
          </div>
        </div>

        <div class="managementTableBody">
          <table>
            <thead>
              <tr>
                <th>Group</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for='(user, index) in dataUser' :key='index'>
                <td>{{user.groupName}}</td>
                <td>{{user.name}}</td>
                <td>{{user.email}}</td>
                <td>{{user.phone}}</td>
                <td>{{user.role}}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <AddNewUserWindow
      v-if='showAddNewUserWindow'
      @closeAddNewUserWindow='closeAddNewUserWindow'
    />
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';
  import { backEndAddress } from '../../Helper';
  import AddNewUserWindow from '../components/addNewUser';

  export default {
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    },
    data: function() {
      return {
        dataUser: [],
        showAddNewUserWindow: false
      }
    },
    methods: {
      getAllUsers() {
        fetch(`${backEndAddress}/user`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.ok) {
            response.json().then(
              res => {
                this.dataUser = res;
              }
            )
          } else {
            alert('Sesi Anda telah berakhir, silahkan refresh halaman ini.');
          }
        })
        .catch(err => {
          console.log(err);
          alert('Terjadi kesalahan ketika mengambil data. Silahkan periksa koneksi Anda.')
        })
      },
      closeAddNewUserWindow() {
        this.showAddNewUserWindow = false;
      },
      openAddNewUserWindow() {
        this.showAddNewUserWindow = true;
      }
    },
    created() {
      this.getAllUsers();
    },
    components: {
      'AddNewUserWindow': AddNewUserWindow
    }
  }
</script>

<style>
  .manageUserComponent {
    display: flex;
  }

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .managementTable {
    margin-top: 20px;
    height: 80vh;
  }

  .managementTableHead {
    background-color: var(--primary-0);
    color: var(--lightColor);
    display: flex;
    justify-content: space-between;
    padding: 15px 25px;
    border-radius: 5px;
    align-items: center;
    width: 90%;
    margin: auto;
    position: relative;
    z-index: 1;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
  }

  .managementTableHeadTitle {
    font-size: 20px;
    font-weight: 600;
  }

  .managementTableHeadSearch {
    outline: none;
    padding: 12px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
  }

  .managementTableHeadSearch::placeholder {
    color: var(--primary-1)
  }

  .managementTableHeadSort {
    outline: none;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
    width: 100px;
    height: 29px;
  }

  .managementTableBody {
    background-color: var(--lightColor);
    color: var(--primary-4);
    position: relative;
    top: -45px;
    border-radius: 5px;
    height: 70vh;
    padding: 60px 20px 20px 20px;
  }

  .managementTableHeadAddNew {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    cursor: pointer;
    font-size: 14px;
    margin-left: 10px;
  }

  .managementTableHeadAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
  }

  .managementTableHeadAddNew:active {
    background-color: var(--primary-4);
  }

  .managementTableBody table {
    width: 100%;
    text-align: left;
  }

  .managementTableBody tbody {
    height: 68vh;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
    line-height: 42px;
  }

  .manageUserComponent .managementTableBody thead tr, .manageUserComponent .managementTableBody tbody { display: block; box-sizing: border-box; }
  .manageUserComponent .managementTableBody tbody td:nth-child(1), .manageUserComponent .managementTableBody thead tr th:nth-child(1) { width: 10vw; }
  .manageUserComponent .managementTableBody tbody td:nth-child(2), .manageUserComponent .managementTableBody thead tr th:nth-child(2) { width: 15vw; }
  .manageUserComponent .managementTableBody tbody td:nth-child(3), .manageUserComponent .managementTableBody thead tr th:nth-child(3) { width: 22vw; }
  .manageUserComponent .managementTableBody tbody td:nth-child(4), .manageUserComponent .managementTableBody thead tr th:nth-child(4) { width: 12vw; }
  /* .manageUserComponent .managementTableBody tbody td:nth-child(5), .manageUserComponent .managementTableBody thead tr th:nth-child(5) { width: 2vw; text-align: 'center' } */
</style>
