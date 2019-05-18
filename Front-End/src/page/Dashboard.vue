<template>
    <div class='dashboardContainer'>
      <SidebarComponent />

      <div class="rightPanel" :style="{ width: rightPanelWidth + 'px' }">
        <HeaderSection headerTitle='Dashboard' />

        <div class="dashboardFirstRow">
          <div>

          </div>

          <div>

          </div>

          <div>

          </div>
        </div>
      </div>

      <!-- <button @click.prevent="hello">Tes Hello</button> -->
    </div>
</template>

<script>
  import HeaderSection from '../components/HeaderSection';

  export default {
    // data: function() {
    //   return {

    //   }
    // },
    methods : {
      // hello() {
      //   fetch('http://localhost:8088/api/hello',{
      //     headers: {'Authorization': localStorage.getItem('token')}
      //   })
      //   .then(response => {
      //     response.json().then(
      //       res => {
      //         if(response.status === 500) {
      //           this.$router.push('/login')
      //         } else {
      //           console.log(res);
      //         }
      //       }
      //     )
      //   })
      // }
    },
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    },
    components: {
      HeaderSection
    }
  }
</script>

<style>
  .dashboardContainer {
    display: flex;
  }

  .rightPanel {
    padding: 20px 40px;
    box-sizing: border-box;
  }

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
  }
</style>
