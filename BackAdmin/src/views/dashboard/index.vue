<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
        <h1 class="greeting">欢迎回来，{{ name }}</h1>
        <p class="description">这是你的管理面板，你可以在这里管理所有内容。</p>
      </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  &-header {
    text-align: center;
    margin-bottom: 20px;
  }
}
</style>