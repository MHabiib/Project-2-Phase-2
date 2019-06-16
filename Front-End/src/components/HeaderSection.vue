<template>
  <div class="headerSection">
    <div class="pageTitle">
      {{ headerTitle }}
    </div>

    <div class="headerMenu">
      <img src="../assets/notifications.png" alt="Notifications" width='20px' height='70%'>
      <img src="../assets/profile.png" alt="Notifications" width='23px' height='70%'>
    </div>
  </div>
</template>

<script>
  export default {
    props: ['headerTitle']
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
</style>
