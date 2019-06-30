<template>
  <div class='membersComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Members'/>

      <div class="membersBodySection">
        <div class="membersTableHeader">
          <div class="membersTableHeaderTitle">
            All Members
          </div>

          <input class='membersTableSearch' type="text" placeholder="Search by anything" v-model='searchQuery'/>
        </div>

        <div class="membersTableBody">
          <table>
            <thead>
              <tr>
                <th>Join Date</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
              </tr>
            </thead>

            <tbody id='infiniteScroll'>
              <tr v-for="(member, index) in dataMembersShown" :key="index">
                <td>{{member.joinDate | dateFormatter}}</td>
                <td>{{member.name}}</td>
                <td>{{member.email}}</td>
                <td>{{member.phone}}</td>
                <td>{{member.role}}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';

  export default {
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    },
    created() {
      this.getMembersData();
    },
    methods: {
      getMembersData() {
        fetch(`http://localhost:8088/api/group/membersByEmail?email=${localStorage.getItem('userEmail')}`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          response.json().then(
            res => {
              this.membersData = res;
              this.dataMembersShown = res;
            }
          )
        })
      },
      filterData(newQuery) {
        let dataFiltered = [];
        const queryBaru = newQuery.toString().toLowerCase();

        this.membersData.forEach(element => {
          const joinDateElement = this.dateFormatter(element.joinDate).toString().toLowerCase();
          const nameElement = element.name.toString().toLowerCase();
          const emailElement = element.email.toString().toLowerCase();
          const phoneElement = element.phone.toString();
          const roleElement = element.role.toString().toLowerCase();

          if(
            joinDateElement.includes(queryBaru) ||
            nameElement.includes(queryBaru) ||
            emailElement.includes(queryBaru) ||
            phoneElement.includes(queryBaru) ||
            roleElement.includes(queryBaru)
          ) {
            dataFiltered.push(element)
          }
        })

        this.dataMembersShown = dataFiltered;
        const e = document.getElementById('infiniteScroll');
        if (e.scrollHeight <= e.clientHeight) {
          console.log('Infinite Triggered')
        }
      },
      dateFormatter(dateToFormat) {
        const monthToString = (month)=> {
          switch(month) {
            case 0: return 'January'
            case 1: return 'February'
            case 2: return 'March'
            case 3: return 'April'
            case 4: return 'May'
            case 5: return 'June'
            case 6: return 'July'
            case 7: return 'August'
            case 8: return 'September'
            case 9: return 'October'
            case 10: return 'November'
            case 11: return 'December'
          }
        }

        const dateObjected = new Date(dateToFormat);
        return `${dateObjected.getDate()} ${monthToString(dateObjected.getMonth())} ${dateObjected.getFullYear()}`
      },
    },
    data: function() {
      return {
        membersData: [],
        dataMembersShown: [],
        searchQuery: '',
        showInviteMemberWindow: false,
      }
    },
    watch: {
      searchQuery: function (newQuery, oldQuery) {
        if(newQuery === '') {
          this.dataMembersShown = this.membersData;
        } else {
          this.filterData(newQuery);
        }
      }
    },
  }
</script>

<style>
  .membersComponent {
    display: flex;
  }

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .membersBodySection {
    margin-top: 30px;
  }

  .membersTableHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    border-radius: 4px;
    padding: 15px;
    width: fit-content;
    margin-left: 15px;
    font-weight: 600;
    box-shadow: 2px 2px 3px rgba(0, 0, 0, .2);
    position: relative;
    z-index: 1;
  }

  .membersTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-4);
    text-align: left;
  }

  .membersTableBody table {
    width: 100%;
  }

  .membersTableBody tbody {
    height: 63vh;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }

  .membersTableBody tbody tr td {
    padding-top: 20px;
  }

  .membersTableBody thead tr, .membersTableBody tbody { display: block; box-sizing: border-box; }
  .membersTableBody tbody td:nth-child(1), .membersTableBody thead tr th:nth-child(1) {width: 13vw;}
  .membersTableBody tbody td:nth-child(2), .membersTableBody thead tr th:nth-child(2) {width: 14vw;}
  .membersTableBody tbody td:nth-child(3), .membersTableBody thead tr th:nth-child(3) {width: 22vw;}
  .membersTableBody tbody td:nth-child(4), .membersTableBody thead tr th:nth-child(4) {width: 10vw;}

  .membersTableHeader {
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

  .membersTableHeaderTitle {
    font-size: 20px;
    font-weight: 600;
  }

  .membersTableSearch {
    outline: none;
    padding: 8px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
    height: 37px;
    box-sizing: border-box;
  }

  .membersTableSearch::placeholder {color: var(--primary-1)}

  .membersTableAddNew {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    font-size: 14px;
    margin-left: 10px;
  }

  .membersTableAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
    cursor: pointer;
  }

  .membersTableAddNew:active {background-color: var(--primary-4);}
</style>
