<template>
  <div class="frontend-container">
    <List :categoryId="categoryId"></List>
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
    console.log('Categories loaded:', categories.value)
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
})

watch(() => props.category, (newCategory) => {
  console.log('Category changed to:', newCategory)
  console.log('Current categoryId:', categoryId.value)
})
</script>

<style scoped>
.frontend-container {
  width: 100%;
}
</style>