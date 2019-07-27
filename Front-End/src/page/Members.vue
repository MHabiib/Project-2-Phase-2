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
            <div class="loading fade-enter-active fade-leave-active" v-show="loading">
              <span class="fa fa-spinner fa-spin"></span> Loading
            </div>
            <tbody id='infiniteScroll'>
              <tr v-for="(member, index) in memberList" :key="index">
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
  import Helper from '../../Helper';
  export default {
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    },
    data: function() {
      return {
        // membersData: [],
        // dataMembersShown: [],
        memberList:[],
        dataMember:{},
        searchQuery: '',
        showInviteMemberWindow: false,
        loading:false,
      }
    },
    created() {
      this.getMembersData(0);
    },
    mounted(){
      this.scroll();
    },
    methods: {
      getMembersData(page) {
        fetch(`${Helper.backEndAddress}/api/group/membersByEmail?email=${localStorage.getItem('userEmail')}&filter=${'role'}&page=${page}`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
        .then(response => {
          if(response.status==401){
            this.memberList=[]
            Helper.getNewToken(this.getMembersData.bind(null,0))
          }
          else{
            localStorage.setItem('accessToken','Token '+ response.headers.get('Authorization'))
            response.json().then(
              res => {
                this.memberList=this.memberList.concat(res.content)
                this.dataMember=res
                this.loading=false
                  // console.log(this.dataUser)        
              }
            )}
        })
      },
      scroll() {
        const paymentTable = document.querySelector('#infiniteScroll');
        paymentTable.addEventListener('scroll', e => {
          // console.log(paymentTable.scrollTop + paymentTable.clientHeight+" : "+paymentTable.scrollHeight)
          if((paymentTable.scrollTop + paymentTable.clientHeight)+1>= paymentTable.scrollHeight) {
            console.log('infinite scroll triggered!')
            if(this.dataMember.last!=true){             
              this.loading=true;
              this.getMembersData(this.dataMember.pageable.pageNumber+1)
              console.log('get more data!')
              // setTimeout(e=>{this.loading=false},300)
            }
          }
        })
      },
      // filterData(newQuery) {
      //   let dataFiltered = [];
      //   const queryBaru = newQuery.toString().toLowerCase();
      //   this.membersData.forEach(element => {
      //     const joinDateElement = this.dateFormatter(element.joinDate).toString().toLowerCase();
      //     const nameElement = element.name.toString().toLowerCase();
      //     const emailElement = element.email.toString().toLowerCase();
      //     const phoneElement = element.phone.toString();
      //     const roleElement = element.role.toString().toLowerCase();
      //     if(
      //       joinDateElement.includes(queryBaru) ||
      //       nameElement.includes(queryBaru) ||
      //       emailElement.includes(queryBaru) ||
      //       phoneElement.includes(queryBaru) ||
      //       roleElement.includes(queryBaru)
      //     ) {
      //       dataFiltered.push(element)
      //     }
      //   })
      //   this.dataMembersShown = dataFiltered;
      //   const e = document.getElementById('infiniteScroll');
      //   if (e.scrollHeight <= e.clientHeight) {
      //     console.log('Infinite Triggered')
      //   }
      // },
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

    // watch: {
    //   searchQuery: function (newQuery, oldQuery) {
    //     if(newQuery === '') {
    //       this.dataMembersShown = this.membersData;
    //     } else {
    //       this.filterData(newQuery);
    //     }
    //   }
    // },
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
    height: 55vh;
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
  .loading {
    text-align: center;
    position: absolute;
    color: #fff;
    z-index: 9;
    background: var(--primary-0);
    opacity: 0.8;
    padding: 8px 18px;
    border-radius: 5px;
    left: calc(50% - 45px);
    top: calc(50% - 18px);
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity .5s
  }
  .fade-enter, .fade-leave-to {
    opacity: 0
  }
</style>