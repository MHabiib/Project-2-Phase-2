<template>
  <div class="headerSection">
    <div class="pageTitle">
      {{ headerTitle }}
    </div>

    <div class="headerMenu">
      <!-- <a href="#"> -->
        <span  v-show='!isRead' class="badge">{{isNotReadTotal}}</span>
        <img src="../assets/notifications.png" alt="Notifications" width='20px' height='70%' style="margin-right:20px" 
            @click="showPersonalNotification">
      <!-- </a> -->
      <img src="../assets/profile.png" alt="Notifications" width='23px' height='70%'>
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
  export default {
    props: ['headerTitle'],
    data: function() {
      return {
        clicked:0,
        isRead : true,
        isNotReadTotal:0,
        showNotification: false,
        isNotifNull:true,
        email:'',
        groupName:'',
        notificationList:[],
        ess:null,
        showNotifNumber:false
      }
    },
    created(){
      this.streamPersonalNotification()
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
      // isNotReadTotal: function(oldVal,newVal){
      //   if(this.isNotReadTotal==0){
      //     this.isRead=!this.isRead
      //   }
      // }
    },
    methods: {
      streamPersonalNotification(){
        let myEvent = localStorage.getItem('userEmail')+'personal'

        this.ess = new EventSource('http://localhost:8088/notification/personal?ref='+localStorage.getItem('userEmail'))
        
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
        if(this.isRead==false){
          this.isRead = !this.isRead
          fetch('http://localhost:8088/notification?ref='+localStorage.getItem('userEmail'),{
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
      clearAllNotifications(){
        this.notificationList=[]
        fetch('http://localhost:8088/notification?ref='+localStorage.getItem('userEmail'),{
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

  .notificationContainer {
    position: absolute;
    top: 60px;
    right: 75px;
    background-color: var(--lightColor);
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    z-index: 10;
  }

  .notificationItems {
    padding: 15px 20px;
    border-bottom: 1px solid var(--primary-1);
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .notificationItems:last-child {
    border-bottom: none;
  }

  .notificationText {
    font-size: 14px;
    margin-right: 30px;
    color: var(--darkColor);
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
