<template>
  <div class='fixedPosition'>
    <div class='groupDetailWindow'>
      <div class='groupWindowSize'>
        <div class="groupDetailHeader">
          <div>Group Id : {{groupDetail.idGroup}}</div>
          <div class="closeButton" @click="closeGroupDetailWindow">Close</div>
        </div>

        <div class="groupDetailBody">
          <div class="groupDetailFirstRow groupDetailRow">
            <div class="groupDetailContainer">
              <div class='groupDetailLabel'>Group Name</div>
              <div class="groupDetailValue">: {{groupDetail.name}}</div>
            </div>
          </div>
          <div class="groupDetailSecondRow groupDetailRow ">
            <div class="groupDetailContainer">
              <div class='groupDetailLabel'>Admin</div>
              <div class="groupDetailValue">: {{groupDetail.groupAdmin}}</div>
            </div>
          </div>
          <div class="groupDetailSecondRow groupDetailRow ">
            <div class="groupDetailContainer">
              <div class='groupDetailLabel'>Group Balance</div>
              <div class="groupDetailValue">: <span style="font-weight:600">Rp {{groupDetail.groupBalance |thousandSeparators}} </span></div>
            </div>
          </div>
          <div class="groupDetailSecondRow groupDetailRow ">
            <div class="groupDetailContainer">
              <div class='groupDetailLabel'>Used Balance</div>
              <div class="groupDetailValue">: <span style="font-weight:600">Rp {{groupDetail.balanceUsed |thousandSeparators}} </span></div>
            </div>
          </div>

          <div class="groupDetailFourthRow groupDetailRow">
            <ul>
              <li v-for="member in memberList" :key="member">{{member}}</li>
            </ul>
              
<!--            <zoom-on-hover :img-normal="groupDetail.imageURL" :scale="1.8"></zoom-on-hover>-->
            
            <!-- <vue-responsive-image
              :width-on-screen="50"
              :width-on-screen-tablet="75"
              :width-on-screen-smartphone="100"
              :image-url="''+groupDetail.imageURL"
              :image-ratio="4/3"
              :alt="''+groupDetail.imagePath"
              :mode="'all'"
            ></vue-responsive-image> -->
            <!-- <img :src="groupDetail.imageURL" :alt="groupDetail.imagePath"/> -->
          </div>

          <div class="groupDetailFifthRow groupDetailRow">
            <div class="groupDetailContainer">
              <div class='groupDetailLabel'>Status</div>
              <div class="groupDetailValue">: {{groupDetail.active? "Active": "Non-Active" }}</div>
            </div>

            <div class="groupDetailButton" v-if="groupDetail.active">
              <div
                :class="{disableButton: disableButton ,rejectButton: !disableButton}"
                @click="disbandGroup(groupDetail.idGroup)"
              >
                Disband Group
              </div>


            </div>
          </div>
        </div>
      </div>
    </div>
  </div></template>

<script>
  import Helper from '../../Helper';
  import Members from "../page/Members";

  export default {
    props: ['groupId'],
    data: function() {
      return {
        groupDetail: {},
        disableButton: false,
        dStatus:null,
        memberList: ["andri","habib","harvine"]
      }
    },
    methods: {
      closeGroupDetailWindow() {this.$emit('closeGroupDetailWindow')},

      getGroupData(id) {
        fetch(`${Helper.backEndAddress}/api/group/${id}`, {
          headers: {Authorization: localStorage.getItem('accessToken')}
        })
          .then(response => {
            if(response.status==401){
              console.log('groupId : '+this.groupId)
              Helper.getNewToken(this.getGroupData.bind(null,this.groupId))
            }  else {

              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              response.json().then(
                res => {
                  this.groupDetail = res;
                  console.log(this.groupDetail)
                  this.checkStatus(res.isRejected);
                }
              )
            }
          })
      },
      getMember(id) {

        fetch(`${Helper.backEndAddress}/api/group/members/${localStorage.getItem('groupName')}`, {//search query >??
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
          .then(response => {
            if(response.status==401){
              this.userList=[]
              Helper.getNewToken(this.searchData.bind(null,0))
            }
            else if(response.ok){
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              response.json().then(
                res => {
                  console.log(res)
                  this.memberList = res
                  // this.memberList=res.content
                  // this.dataMember=res
                  setTimeout(e=>{this.loading=false},500)

                  // this.loading=false
                }
              )
            }
          })
      },
      disbandGroup(id) {
        fetch(`//API DISBAND GROUP`, {
          headers: {
            Authorization: localStorage.getItem('accessToken')
          }
        })
          .then(response => {
            if(response.status==401){
              this.userList=[]
              Helper.getNewToken(this.searchData.bind(null,0))
            }
            else if(response.ok){
              localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
              response.json().then(
                res => {
                  this.getGroupData()
                  // this.memberList=res.content
                  // this.dataMember=res
                  setTimeout(e=>{this.loading=false},500)

                  // this.loading=false
                }
              )
            }
          })
      },

    },
    created() {this.getGroupData(this.groupId);this.getMember(this.groupId);},

    filters: {
      statusChecker(status) {
        switch(status) {
          case true: return 'Rejected, please made another group.'
          case false: return 'Accepted.'
          case null: return 'Pending'

        }
      }
    }
  }
</script>

<style>
  .fixedPosition {position: absolute;}

  .groupDetailWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
    color: var(--primary-3);
  }

  .groupDetailHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    padding: 16px 20px;
    box-sizing: border-box;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    width: 95%;
    margin: auto;
    position: relative;
    z-index: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 600;
  }

  .groupWindowSize {
    width: 30vw;
    height: 80vh;
    position: relative;
    top: -10vh;
  }

  .closeButton {
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
  }

  .groupDetailBody {
    background-color: white;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
    position: relative;
    top: -30px;
    padding: 20px;
    padding-top: 32px;
    height: 100%;
    overflow: hidden;
    font-size: 14px;
  }

  .groupDetailFirstRow,
  .groupDetailFifthRow,
  .groupDetailButton,
  .groupDetailRequester,
  .groupDetailSecondRow {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .groupDetailRow {margin-top: 15px;}

  .groupDetailContainer {
    display: flex;
    width: 300px;
  }

  .groupDetailFourthRow {
    border: solid 1px var(--primary-1);
    text-align: center;
    padding: 5px;
    height: 45vh;
    overflow: scroll;
    border-radius: 5px;
  }

  .groupDetailButton {
    cursor: pointer;
    color: var(--lightColor);
    font-size: 12px;
  }

  .acceptButton, .rejectButton, .disableButton {
    padding: 7px 10px;
    border-radius: 4px;
    width: 50px;
    text-align: center;
    margin-left: 5px;
  }

  .acceptButton {background-color: var(--primary-0);}
  .rejectButton {background-color: var(--warning);}
  .disableButton {background-color: #999;}
  .acceptButton:hover, .rejectButton:hover {opacity: .9;}
  .acceptButton:active, .rejectButton:active {opacity: 1;}

  .groupDetailLabel {
    width: 125px;
    font-weight: 600;
  }
</style>
