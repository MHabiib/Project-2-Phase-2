<template>
  <div class='expensesComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Expenses'/>
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
  .expensesComponent {
    display: flex;
  }

  .rightPanel {
    padding: 20px 20px 20px 30px;
    box-sizing: border-box;
  }
</style>
