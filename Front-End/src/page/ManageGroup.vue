<template>
  <div class='groupsComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Groups'/>


      <div class="groupsBodySection">
        <div class="groupsTableHeader">
          <div class="groupsTableHeaderTitle">
            All Groups
          </div>

          <div style='display: flex;'>
            <input class='groupTableSearch' type="text" placeholder="Search by anything" v-model='searchQuery'/>

            <div class="groupTableAddNew" @click='openCreateNewGroupWindow'>
              Request Group
            </div>
          </div>
        </div>

        <div class="groupsTableBody">
          <table>
            <thead>
            <tr>
              <th>&nbsp;&nbsp;</th>
              <th>Name</th>
              <th>Admin</th>
              <th>Regular Payment</th>
              <th>Group Balance</th>
              <th>Status</th>
            </tr>
            </thead>

            <tbody id='infiniteScroll'>
            <tr
              class='groupRow'
              v-for='(group, index) in dataGroupShown' :key='"group-"+index'
              @click="openGroupDetailWindow(group.idGroup)"
            >
              <td>{{index+1}}.</td>
              <td>{{group.name}}</td>
              <td>{{group.groupAdmin}}</td>
              <td>Rp {{group.regularPayment | thousandSeparators}}</td>
              <td>Rp {{group.groupBalance | thousandSeparators}}</td>
              <td>{{group.active?"Active" : "Non-Active"}}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <UserContributedWindow
      v-if='showUserContributedWindow'
      @closeContributedWindow="closeUserContributedWindow"
      :groupName="this.selectedGroup"
      :userList="this.selectedUserList"
    />

    <groupDetailWindow
      v-if='showGroupDetailWindow'
      @closeGroupDetailWindow="closeGroupDetailWindow"
      @refreshData="getGroupData"
      :groupId="this.detailGroupSelected"
    />

    <createNewGroupWindow
      v-if='showCreateNewGroupWindow'
      @closeCreateNewGroupWindow='closeCreateNewGroupWindow'
      @refreshData="getGroupData"
    />
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';
  import UserContributedWindow from '../components/userContributedWindow';
  import groupDetailWindow from '../components/GroupDetailWindow';
  import createNewGroupWindow from '../components/createNewExpense';
  import Helper from '../../Helper';

  export default {
    computed: {rightPanelWidth: function() {return (document.documentElement.clientWidth - 280);}},
    data: function() {
      return {
        dataGroup: [],
        dataGroupShown: [],
        selectedUserList: [],
        detailGroupSelected: '',
        selectedGroup: '',
        showUserContributedWindow: false,
        showGroupDetailWindow: false,
        showCreateNewGroupWindow: false,
        searchQuery: ''
      }
    },
    created() {this.getGroupData();},
    methods: {
      getGroupData() {
        fetch(`${Helper.backEndAddress}/api/group`, {
          headers: {Authorization: localStorage.getItem('accessToken')}
        })
          .then(response => {
            if(response.status==401){
              Helper.getNewToken(this.getGroupData)
            }
            else{
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              response.json().then(
                res => {
                  // console.log(res)
                  // this.dataGroup = res
                  this.dataGroupShown = res
                  // this.filterData(this.searchQuery);
                }
              )
            }
          })
      },
      showUserContributed(selectedGroup, userList) {
        this.selectedGroup = selectedGroup;
        this.selectedUserList = userList;
        this.showUserContributedWindow = true;
      },
      closeUserContributedWindow() {this.showUserContributedWindow = false;},
      openGroupDetailWindow(groupId) {
        this.showGroupDetailWindow = true;
        this.detailGroupSelected = groupId;
      },
      closeGroupDetailWindow() {this.showGroupDetailWindow = false;},
      openCreateNewGroupWindow() {this.showCreateNewGroupWindow = true;},
      closeCreateNewGroupWindow() {this.showCreateNewGroupWindow = false;},
      scroll() {
        document.getElementById('infiniteScroll').onscroll = (e) => {
          if(e.target.clientHeight + e.target.scrollTop >= e.target.scrollHeight) {
            console.log('Infinite Triggered')
          }
        };
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
      statusChecker(status) {
        switch(status) {
          case true: return 'Accepted'
          case false: return 'Rejected'
          case null: return 'Waiting'
        }
      },
      filterData(newQuery) {
        let dataFiltered = [];
        const queryBaru = newQuery.toString().toLowerCase();

        this.dataGroup.forEach(element => {
          const dateElement = this.dateFormatter(element.createdDate).toString().toLowerCase();
          const titleElement = element.title.toString().toLowerCase();
          const statusElement = this.statusChecker(element.status).toString().toLowerCase();
          const priceElement = element.price.toString();

          if(
            dateElement.includes(queryBaru) ||
            titleElement.includes(queryBaru) ||
            statusElement.includes(queryBaru) ||
            priceElement.includes(queryBaru)
          ) {
            dataFiltered.push(element)
          }
        })

        this.dataGroupShown = dataFiltered;
        const e = document.getElementById('infiniteScroll');
        if (e.scrollHeight <= e.clientHeight) {
          console.log('Infinite Triggered')
        }
      }
    },
    components: {
      'UserContributedWindow': UserContributedWindow,
      'createNewGroupWindow': createNewGroupWindow,
      groupDetailWindow,

    },
    filters: {
      statusChecker(status) {
        switch(status) {
          case true: return 'Accepted'
          case false: return 'Rejected'
          case null: return 'Waiting'
        }
      }
    },
    mounted() {this.scroll()},
    watch: {
      searchQuery: function (newQuery, oldQuery) {
        if(newQuery === '') {
          this.dataGroupShown = this.dataGroup;
        } else {
          this.filterData(newQuery);
        }
      }
    },
  }
</script>

<style>
  .groupsComponent {display: flex;}

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .groupsBodySection {margin-top: 30px;}

  .groupsTableHeader {
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

  .groupsTableHeaderTitle {
    font-size: 20px;
    font-weight: 600;
  }

  .groupsTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-4);
    text-align: center;
  }

  .groupsTableBody table {width: 100%;}

  .groupsTableBody tbody {
    height: 55vh;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }

  .groupsTableBody tbody tr td {padding-top: 12px; padding-bottom: 12px;}
  .groupsTableBody thead tr, .groupsTableBody tbody {text-align:left;display: block; box-sizing: border-box;}
  .groupsTableBody tbody td:nth-child(1), .groupsTableBody thead tr th:nth-child(1) {
    width: 1.5vw;
    text-align: left;
    padding-left: 10px;
  }
  .groupsTableBody tbody td:nth-child(2), .groupsTableBody thead tr th:nth-child(2) {
    width: 13vw;
    text-align: left;
    padding-left: 10px;
  }

  .groupsTableBody tbody td:nth-child(3), .groupsTableBody thead tr th:nth-child(3) {
    width: 300px;
    text-align: left;
    padding-left: 10px;
  }

  .groupsTableBody tbody td:nth-child(4), .groupsTableBody thead tr th:nth-child(4) {width: 10vw;}
  .groupsTableBody tbody td:nth-child(5), .groupsTableBody thead tr th:nth-child(5) {width: 11vw;}
  .groupsTableBody tbody td:nth-child(6), .groupsTableBody thead tr th:nth-child(6) {width: 10vw;}

  .showMembersButton {cursor: pointer;}

  .showMembersButton:hover {
    text-decoration: underline;
    color: var(--primary-0);
  }

  .groupTableSearch {
    outline: none;
    padding: 8px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
  }

  .groupTableSearch::placeholder {color: var(--primary-1)}

  .groupTableAddNew {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    font-size: 14px;
    margin-left: 10px;
    cursor: pointer;
  }

  .groupTableAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
  }

  .groupTableAddNew:active {background-color: var(--primary-4);}
  .groupRow {cursor: pointer;}
  .groupRow:hover {background-color: white;}
  .groupRow:active {background-color: rgba(255, 255, 255, .5);}
</style>
