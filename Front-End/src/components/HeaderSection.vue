<template>
  <div class="headerSection">
    <div class="pageTitle">
      {{ headerTitle }}
    </div>

    <div class="headerMenu">
      <img src="../assets/notifications.png" alt="Notifications" width='20px' height='70%' @click="showNotification = !showNotification">
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
    </div>
  </div>
</template>

<script>
  export default {
    props: ['headerTitle'],
    data: function() {
      return {
        showNotification: false,
        isNotifNull:true,
        email:'',
        groupName:'',
        notificationList:[],
        ess:null,
      }
    },
    created(){
      //this.streamPersonalNotification()
    },
    destroyed(){
      this.ess.close()
    },
    watch:{
      notificationList: function(oldVal,newVal){
        if(this.notificationList.length>0) {this.isNotifNull=false}
        console.log('Watcher triggered!')
      }
    },
    computed:{
      updateNotif(){
        return this.notificationList.length
      }
    },
    methods: {
      streamPersonalNotification(){
        this.ess = new EventSource('http://localhost:8088/notification/personal?ref='+localStorage.getItem('userEmail'))
        
        this.ess.addEventListener('start', event => {
          this.notificationList = JSON.parse(event.data)
          console.log('PersonalNotification stream started')
          console.log('P_Notification : '+this.notificationList.length)
          console.log('=================================')
        })

        this.ess.addEventListener(localStorage.getItem('userEmail'), event =>{
          this.notificationList = JSON.parse(event.data)
          console.log('P_Notification Updates: '+this.notificationList.length)
        })

        this.ess.onerror = function(){
          // this.ess.close()
          console.log("P_Notification stream errored")
        }
      }
    },
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
</style>
