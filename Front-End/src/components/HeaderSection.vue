<template>
  <div class="headerSection">
    <div class="pageTitle">
      {{ headerTitle }}
    </div>

    <div class="headerMenu">
      <!-- <a href="#"> -->
        <span  v-show='!isRead' class="badge">{{isNotReadTotal}}</span>
        <img src="../assets/notifications.png" alt="Notifications" width='22px' height='80%' style="margin-right:20px" 
            @click="showPersonalNotification">
      <!-- </a> -->
      <span  @click="showMyProfile">
        <img src="../assets/profile.png" alt="pp" width='30px' height='90%'>
      </span>
      <!-- <span v-if="ppAvailable==true"  @click="showMyProfile"> 
        <img :src="imageURL" style="border-radius:50%" alt="pp" width='35px' height='100%'>
      </span> -->
    </div>
    <div class="profileContainer"   v-show="showProfile">
      <div class="row">
        <div class="profileItems">
          <div class="profileText">
            Logged in as <span style="font-weight:600;">{{email}}</span>
          </div>
        </div>
      </div>
      <div class="row" @click="openProfilePage">
        <div class="profileItems"> 
          <div class="profileText">
            My Profile
          </div>
        </div>
      </div>
      <!--<div class="row">
        <div class="profileItems">
          <div class="profileText">
            Log out
          </div>
        </div>
      </div>-->
    </div>
    <div class="notificationContainer" v-show="showNotification">
      <div class="notificationItems" v-show="isNotifNull">
          <div class="notificationText">
            There is no notification at this moment
          </div>
          <div class="notificationTime"></div>
        </div>
      <div 
        v-for='(notif,index) in notificationList' :key='index'
          >
        <div class="notificationItems">
          <div class="notificationText">
            {{notif.message}}
          </div>
          <div class="notificationTime">
            {{new Date(notif.timestamp).toLocaleString()}}
          </div>
        </div>
      </div>
      <div class="notificationItems" v-show="!isNotifNull">
          <div class="notificationText">
          </div>
          <div class="notificationTime clearAll" @click="clearAllNotifications">
            Clear All
          </div>
        </div>
    </div>
  </div>
</template>

<script>
  import AddNewUserWindow from '../components/addNewUser';
  import Helper from '../../Helper';



  export default {
    props: ['headerTitle'],
    data: function() {
      return {
        showUpdateProfileWindow:false,
        updateProfileMode:false,
        editMode:false,
        isRead : true,
        isNotReadTotal:0,
        showNotification: false,
        isNotifNull:true,
        imageURL: localStorage.getItem('imageURL'),
        groupName:'',
        notificationList:[],
        ess:null,
        email:  localStorage.getItem('userEmail'),
        showNotifNumber:false,
        showProfile:false,
      }
    },
    created(){
      this.streamPersonalNotification()      
    },
    computed:{
      ppAvailable(){
        if(this.imageURL==='' || this.imageURL== null || this.imageURL=="null"){
          return false
        }
        return true
      }
    },
    destroyed(){
      this.ess.close()
    },
    watch:{
      notificationList: function(oldVal,newVal){
        this.isNotReadTotal = 0
        if(this.notificationList.length>0) {this.isNotifNull=false}
        else this.isNotifNull=true
        this.notificationList.forEach(notif => {
          if(!notif.isRead){this.isNotReadTotal +=1 }
        })
        this.isRead = this.isNotReadTotal>0 ? false : true
        console.log('Personal Watcher triggered!')
      },
    },
    methods: {    
      openProfilePage(){
        this.$router.push('/profile')
      },
      streamPersonalNotification(){
        // let myEvent = localStorage.getItem('userEmail')+'personal'
        let myEvent = this.email+'personal'
        this.ess = new EventSource('http://localhost:8088/notification/personal?ref='+this.email)
        
        this.ess.addEventListener('start', event => {
          this.notificationList = JSON.parse(event.data)
          console.log(this.notificationList)
          console.log('PersonalNotification stream started')
          console.log('P_Notification : '+this.notificationList.length)
          console.log('=================================')
        })
        this.ess.addEventListener(myEvent, event =>{
          this.notificationList = JSON.parse(event.data)
          console.log('P_Notification Updates: '+this.notificationList.length)
        })

        this.ess.onerror = function(){
          // this.ess.close()
          console.log("P_Notification stream errored")
        }
      },
      showPersonalNotification(){
        this.showNotification = !this.showNotification
        this.showProfile = false
        if(this.isRead==false){
          this.isRead = !this.isRead
          fetch('http://localhost:8088/notification?ref='+this.email,{
            method: 'PUT'
          }).then(response => {
            if(response.ok){
              console.log('notification isRead updated!')
              this.isNotReadTotal=0;
            }else{
              console.log('failed to update notification.')
            }
          })
        }
      },
      showMyProfile(){
        this.showProfile = !this.showProfile
        this.showNotification = false
      },
      clearAllNotifications(){
        this.notificationList=[]
        fetch('http://localhost:8088/notification?ref='+this.email,{
          method: 'DELETE'
        }).then(response => {
          if(response.ok){
            console.log('notification cleared!')
          }else{
            console.log('failed to clear notification.')
          }
        })
      }
    }//methods end here
  }
</script>

<style>
  .headerSection {
    display: flex;
    justify-content: space-between;
  }

  .pageTitle {
    color: var(--primary-0);
    font-size: 30px;
    font-weight: 200;
  }

  .headerMenu {
    display: flex;
    align-items: center;
  }

  .headerMenu img {
    margin-right: 12px;
    cursor: pointer;
  }
  .profileContainer{
    position: absolute;
    top: 8%;
    right: 2%;
    width: 25vw;
    /* background-color: var(--lightColor); */
    background-color: rgba(242,244,246,0.9);
    border-radius:5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    z-index: 10;
    text-align: center !important;
  }
  .notificationContainer {
        color: var(--primary-4);

    position: absolute;
    top: 60px;
    right: 75px;
    /* background-color: var(--lightColor); */
    background-color: rgba(242,244,246,0.9);

    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    z-index: 10;
    border-radius:10px;
  }

  .notificationItems {
    padding: 10px 15px;
    border-bottom: 1px solid var(--primary-1);
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .profileItems {
    padding: 8px 12px;
    text-align: center;
    justify-content: center;
    margin-left: 35px;
    margin-right:35px;
    /* border-radius: 5px; */
    border-bottom: 1px solid var(--primary-1) !important;
  }

  .notificationItems:last-child, .profileItems:last-child {
    border-bottom: none;
  }

  .notificationText, .profileText {
    font-size: 14px;
    margin-right: 30px;
  }
  
  .profileText{
    width:100%;
    text-align: center;
    margin-right: 0 ;
  }
  .row {
    color: var(--primary-4);
  }
  .row:hover {
    cursor: pointer;
    border-radius: 5px;
    color:  #fff !important;
    background-color: #4c8bd2;
  }
  .notificationTime {
    font-size: 11px;
    color: var(--primary-2);
  }
  .clearAll{  
    font-size:12px;
    color: var(--primary-2);

  }
  .clearAll:hover {
    /* background-color: var(--primary-2); */
    cursor: pointer;
    font-size:14px;
    font-weight: 600;
  }
  .badge {
  position: relative;
  top: -12px;
  right: -35px;
  padding: 1px 5px;
  border-radius: 50%;
  text-decoration: none;
  background: red;
  color: white;
  }
</style>
