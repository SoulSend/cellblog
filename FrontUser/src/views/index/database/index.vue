<template>
  <div class="database-container">
    <List :category="props.category" :key="props.category"></List>
  </div>
</template>

<script setup lang="js">
import List from '@/components/articlelist/index.vue'

const props = defineProps({
  category: {
    type: String,
    default: '数据库'
  }
})
</script>

<style scoped>
.database-container {
  width: 100%;
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>