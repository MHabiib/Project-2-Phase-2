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

      <div class="notificationItems">
        <div class="notificationText">
          You don't have any notifications yet.
        </div>

        <div class="notificationTime">
          a moment ago
        </div>
      </div>

      <div class="notificationItems">
        <div class="notificationText">
          Second row of notification.
        </div>

        <div class="notificationTime">
          a week ago
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
        email:'',
        groupName:'',
        notificationList:[],
        newNotificationList:[]
      }
    },
  
    created(){
        this.streamPersonalNotification()
      }
    ,
    methods: {
        streamPersonalNotification(){
          let es = new EventSource('http://localhost:8088/notification/personal?ref='+localStorage.getItem('userEmail'))
          
          es.addEventListener('notification',event=>{
          this.notificationList = JSON.parse(event.data)
          console.log('Notification : '+this.notificationList.length)
          })
          es.addEventListener('update',event =>{
          this.newNotificationList = (JSON.parse(event.data))
          console.log('Update notification : ' + this.newNotificationList.length)
          })       
          es.onerror = function(){
             es.close()
             console.log(es)
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
