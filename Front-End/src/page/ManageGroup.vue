<template>
  <div class='groupsComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Manage Group'/>
      <div class="groupsBodySection">
        <div class="groupsTableHeader">
          <div class="groupsTableHeaderTitle">
            All Groups
          </div>
          <div class="myParent" style='display: flex;'>
            <input class='groupsTabeSearch' type="text" :placeholder="'Search by '+this.searchPlaceHolder" v-model='searchQuery'/>
            <div class="dropdownMenu" >
                <multiselect 
                  v-model="filter" 
                  :allow-empty="false" 
                  :options="options" 
                  :searchable="false" 
                  :close-on-select="true" 
                  :show-labels="false" 
                  placeholder="Pick a value">
                </multiselect>
            </div>
            <div class="refreshBtn" @click='searchData(0)'>
              <img src="../assets/sinchronize-256.png" width="16px" alt="Refresh">
            </div>
            <div class="groupsTabeAddNew" @click='openCreateNewGroupWindow'>
              Add New Group
            </div>
            <div class="groupsTabeAddNew" @click='openBulkInsertWindow'>
              Bulk Insert
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
              <th>Balance</th>
              <th>Regular Payment</th>
              <th>Created Date</th>
              <!-- <th>Status</th> -->
            </tr>
            </thead>

            <tbody id='infiniteScroll'>
              <tr
                class='groupRow'
                v-for='(group, index) in groupList' :key='"group-"+index'
                @click="openGroupDetailWindow(group)"
              >
                <td>{{index+1}}.</td>
                <td>{{group.name}}</td>
                <td>{{group.groupAdmin}}</td>
                <td>Rp {{group.groupBalance | thousandSeparators}}</td>
                <td>Rp {{group.regularPayment| thousandSeparators}}</td>
                <td>{{group.createdDate | dateFormatter}}</td>

                <!-- <td>{{group.active ? "Active" : "Non-Active"}}</td> -->
              </tr>
              <div style="text-align:center">
                <div v-show="loading" class="lds-ring"><div></div><div></div><div></div><div></div></div>
              </div>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <createNewGroupWindow
      v-if='showGroupDetailWindow'
      @updateGroupDetailWindow="updateGroupDetailWindow"
      @closeGroupDetailWindow="closeGroupDetailWindow"
      @refreshData="searchData(0)"
      :groupDetail="this.detailGroupSelected"
      :headerTitle ="this.headerTitle"
      :editMode ="this.editMode"
      :addMode = this.addMode
    />
    <bulkInsertWindow
    v-if="showBulkInsertWindow"
    @updateBulkInsertWindow="searchData(0)"
    @closeBulkInsertWindow="closeBulkInsertWindow"
    />
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';
  import createNewGroupWindow from '../components/CreateNewGroupWindow';
  import bulkInsertWindow from '../components/BulkInsertWindow'; 

  import Multiselect from 'vue-multiselect'
  import Helper from '../../Helper';
  

  export default {
    components: {
      'multiselect': Multiselect,
      bulkInsertWindow,
      createNewGroupWindow,
    },
    computed: {
      rightPanelWidth: function() {return (document.documentElement.clientWidth - 280);
      },
      searchPlaceHolder:function(){
        return this.filter == "date before"  || this.filter == "date after"  ? "(dd-MMMM-yyyy)" : this.filter
      }    
    },
    data: function() {
      return {
        dataGroup: {},
        groupList:[],
        detailGroupSelected: '',
        showGroupDetailWindow: false,
        searchQuery: '',
        filter:'name',
        options:['name','groupAdmin','date before','date after','balance <','balance >'],
        loading:false,
        editMode:false,
        addMode:false,
        showBulkInsertWindow: false,
        headerTitle:'',
      }
    },
    mounted() {this.scroll()},
    watch: {
      filter : function (newQuery, oldQuery) {
        this.searchData(0)  
      },
      searchQuery: function (newQuery, oldQuery) {
        this.searchData(0)  
      }
    },
    created() {this.searchData(0);},
    methods: {
      searchData(page) {
        this.loading=true;
        console.log(this.filter+" : " +this.searchQuery)
        fetch(`${Helper.backEndAddress}/api/group/search?query=${this.filter}:${this.searchQuery}&page=${page}`, {
          headers: {Authorization: localStorage.getItem('accessToken')}
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.searchData.bind(null,0))
          }
          else{
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                this.groupList=res.content
                this.dataGroup = res;
                setTimeout(e=>{this.loading=false},500)

                // this.loading=false
              }
            )
          }         
        })
      },
      getGroupData(page) {
        this.loading=true;
        console.log(this.filter+" : " +this.searchQuery)

        // fetch(`${Helper.backEndAddress}/api/payment?isPaid=false&page=${page}`, {
        fetch(`${Helper.backEndAddress}/api/group/search?query=${this.filter}:${this.searchQuery}&page=${page}`, {
          headers: {Authorization: localStorage.getItem('accessToken')}
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.getPendingPayment.bind(null,this.dataGroup.pageable.pageNumber+1))
          }
          else{
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                this.groupList=this.groupList.concat(res.content)
                this.dataGroup = res;
                setTimeout(e=>{this.loading=false},500)
                // this.loading=false;
              }
            )
          }         
        })
      },
      closeUserContributedWindow() {this.showUserContributedWindow = false;},
      openGroupDetailWindow(groupDetail) {
        this.detailGroupSelected = groupDetail;
        this.headerTitle = `Group ${groupDetail.name}`;
        this.editMode = false;
        this.addMode = false;
        this.showGroupDetailWindow = true;

      },
      closeGroupDetailWindow() {this.showGroupDetailWindow = false;},
      updateGroupDetailWindow(){
        this.showGroupDetailWindow = false
        this.searchData(0)
      },
      openCreateNewGroupWindow() {
        this.detailGroupSelected = {};
        this.headerTitle = `Add New Group`
        this.editMode = true;
        this.addMode = true;
        this.showGroupDetailWindow = true;},
      closeCreateNewGroupWindow() {
        this.showGroupDetailWindow = false;},
      openBulkInsertWindow(){
        this.showBulkInsertWindow = true;
      },
      closeBulkInsertWindow(){
        this.showBulkInsertWindow = false;
      },
      scroll() {
        const paymentTable = document.querySelector('#infiniteScroll');
        paymentTable.addEventListener('scroll', e => {
          // console.log(paymentTable.scrollTop + paymentTable.clientHeight+" : "+paymentTable.scrollHeight)
          if((paymentTable.scrollTop + paymentTable.clientHeight)+1>= paymentTable.scrollHeight) {
            console.log('infinite scroll triggered!')
            if(this.dataGroup.last!=true & this.loading==false){   
              this.loading=true;
              this.getGroupData(this.dataGroup.pageable.pageNumber+1)
              console.log('get more data!')
            }
          }
        })
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
  }
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
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
    font-weight: 600;
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
    max-height: 400px;
    overflow-y: auto;
    overflow-x: auto;
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
    width: 12vw;
    text-align: left;
    padding-left: 10px;
  }

  .groupsTableBody tbody td:nth-child(3), .groupsTableBody thead tr th:nth-child(3) {
    width: 15vw;
    text-align: left;
    padding-left: 10px;
  }

  .groupsTableBody tbody td:nth-child(4), .groupsTableBody thead tr th:nth-child(4) {width: 14vw;}
  .groupsTableBody tbody td:nth-child(5), .groupsTableBody thead tr th:nth-child(5) {width: 12vw;}
  /* .groupsTableBody tbody td:nth-child(6), .groupsTableBody thead tr th:nth-child(6) {width: 10vw;} */

  .showMembersButton {cursor: pointer;}

  .showMembersButton:hover {
    text-decoration: underline;
    color: var(--primary-0);
  }

  .groupsTabeSearch {
    outline: none;
    padding: 8px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
  }

  .groupsTabeSearch::placeholder {color: var(--primary-1)}

#app .myParent .dropdownMenu .multiselect__tags,.multiselect__single, .multiselect__element{
    /* background-color: var(--lightColor); */
    font-size: 14px;
    color: var(--primary-0) ;
    border-radius: 5px;
  }

  .dropdownMenu{
    margin-left: 10px;
    position: relative;
    width: 10vw;
  }
  .dropdownMenu:hover{
    cursor:pointer;
  }
  .refreshBtn {
    background-color: #fff;
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    font-size: 14px;
    margin-left: 10px;
  }
  .refreshBtn:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
    cursor: pointer;
  }
  .refreshBtn:active {background-color: var(--primary-4);}
  .groupsTabeAddNew {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 500;
    border-radius: 5px;
    font-size: 14px;
    margin-left: 10px;
    cursor: pointer;
  }

  .groupsTabeAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
  }

  .groupsTabeAddNew:active {background-color: var(--primary-4);}
  .groupRow {cursor: pointer;}
  .groupRow:hover {background-color: white;}
  .groupRow:active {background-color: rgba(255, 255, 255, .5);}
   .lds-ring {
    display: inline-block;
    position: relative;
    width: 64px;
    height: 64px;
  }
  .lds-ring div {
    box-sizing: border-box;
    display: flex;
    position: absolute;
    width: 51px;
    height: 51px;
    margin: 6px;
    border: 6px solid #fff;
    border-radius: 50%;
    animation: lds-ring 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
    border-color: #fff transparent transparent transparent;
  }
  .lds-ring div:nth-child(1) {
    animation-delay: -0.45s;
  }
  .lds-ring div:nth-child(2) {
    animation-delay: -0.3s;
  }
  .lds-ring div:nth-child(3) {
    animation-delay: -0.15s;
  }
  @keyframes lds-ring {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
</style>
