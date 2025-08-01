<template>
  <div class="rearend-container">
    <List :category="props.category" :key="props.category"></List>
  </div>
</template>

<script setup lang="js">
import List from '@/components/articlelist/index.vue'

const props = defineProps({
  category: {
    type: String,
    default: '后端'
  }
})
</script>

<style scoped>
.rearend-container {
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