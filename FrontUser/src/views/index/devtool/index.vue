<template>
  <div class="devtool-container">
    <List :category="props.category" :key="props.category"></List>
  </div>
</template>

<script setup lang="js">
import List from '@/components/articlelist/index.vue'

const props = defineProps({
  category: {
    type: String,
    default: '开发工具'
  }
})
</script>

<style scoped>
.devtool-container {
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