<template>
  <div class='fixedPosition'>
    <div class='userContributedWindow'>
      <div class='windowSize'>
        <div class="userContributedHeader">
          <div>Contributors in {{expenseName}}</div>
          <div class="closeButton" @click="closeContributedWindow">Close</div>
        </div>

        <div class="userContributedBody">
          <div v-for='(dataUser, index) in dataUserContributed' :key='index' class='userContributedBodyContent'>
            <div class='userContributedBodyContentLeft'>
              <img :src="dataUser.imageURL" :alt="dataUser.name"/>
            </div>

            <div class="userContributedBodyContentRight">
              <div class="userContributedName">{{dataUser.name}}</div>
              <div class="userContributedEmail">{{dataUser.email}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    props: ['expenseName', 'userList'],
    data: function() {
      return {
        dataUserContributed: []
      }
    },
    created() {
      this.dataUserContributed = this.userList;
    },
    methods: {
      closeContributedWindow() {
        this.$emit('closeContributedWindow')
      }
    }
  }
</script>

<style>
  .fixedPosition {
    position: absolute;
  }

  .userContributedWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
  }

  .userContributedHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    padding: 16px 12px;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    width: 90%;
    margin: auto;
    position: relative;
    z-index: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .windowSize {
    width: 35%;
    height: 50%;
  }

  .userContributedBody {
    background-color: white;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
    position: relative;
    top: -30px;
    padding: 20px;
    padding-top: 32px;
    height: 100%;
    overflow: auto;
  }

  .userContributedBodyContent {
    display: flex;
    margin-top: 15px;
    align-items: center;
  }

  .userContributedBodyContentLeft img {
    border-radius: 50%;
    width: 50px;
  }

  .userContributedBodyContentLeft {
    margin-right: 15px;
  }

  .userContributedName {
    color: var(--primary-3);
    font-weight: 600;
  }

  .userContributedEmail {
    font-size: 13px;
    color: var(--primary-2);
  }

  .closeButton {
    font-size: 12px;
    cursor: pointer;
  }
</style>
