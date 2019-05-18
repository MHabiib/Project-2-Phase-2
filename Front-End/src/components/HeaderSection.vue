<template>
  <div class="headerSection">
    <div class="pageTitle">
      {{ headerTitle }}
    </div>

    <div class="headerMenu">
      <div class="headerMenuItem">Notification</div>
      <div class="headerMenuItem">Profile</div>
    </div>
  </div>
</template>

<script>
  export default {
    props: ['headerTitle']
  }
</script>
