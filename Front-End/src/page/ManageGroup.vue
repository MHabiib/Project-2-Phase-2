<template>
  <div class='manageGroupComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Manage Group'/>
    </div>
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';

  export default {
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    }
  }
</script>

<style>
  .manageGroupComponent {
    display: flex;
  }

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }
</style>
