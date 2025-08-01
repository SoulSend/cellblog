<template>
  <div class="frontend-container">
    <List :category="props.category" :key="props.category"></List>
  </div>
</template>

<script setup lang="js">
import List from '@/components/articlelist/index.vue'
import { computed, ref, onMounted, watch } from 'vue'
import axios from '@/axios'

// 接收路由参数
const props = defineProps({
  category: {
    type: String,
    default: '前端'
  }
})

const categories = ref([])

// 根据分类名称获取分类ID
const categoryId = computed(() => {
  const foundCategory = categories.value.find(cat => 
    cat.categoryName.toLowerCase() === props.category?.toLowerCase()
  )
  return foundCategory ? Number(foundCategory.id) : 1
})

onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8888/categorys/detail')
    categories.value = response.data.data
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
})

// 监听分类变化，重新获取文章列表
watch(() => props.category, (newCategory, oldCategory) => {
  if (newCategory !== oldCategory) {
    // 分类变化时，List组件会通过key的变化重新渲染
    console.log('Category changed from', oldCategory, 'to', newCategory)
  }
})
</script>

<style scoped>
.frontend-container {
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