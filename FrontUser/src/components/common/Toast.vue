<template>
  <Teleport to="body">
    <Transition name="toast">
      <div v-if="visible" class="toast-container" :class="type">
        <div class="toast-content">
          <div class="toast-icon">
            <svg v-if="type === 'success'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="20,6 9,17 4,12"></polyline>
            </svg>
            <svg v-else-if="type === 'error'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"></circle>
              <line x1="15" y1="9" x2="9" y2="15"></line>
              <line x1="9" y1="9" x2="15" y2="15"></line>
            </svg>
            <svg v-else-if="type === 'warning'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
              <line x1="12" y1="9" x2="12" y2="13"></line>
              <line x1="12" y1="17" x2="12.01" y2="17"></line>
            </svg>
            <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"></circle>
              <line x1="12" y1="8" x2="12" y2="12"></line>
              <line x1="12" y1="16" x2="12.01" y2="16"></line>
            </svg>
          </div>
          <div class="toast-message">{{ message }}</div>
          <button v-if="showClose" class="toast-close" @click="close">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';

interface Props {
  message: string;
  type?: 'success' | 'error' | 'warning' | 'info';
  duration?: number;
  showClose?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  type: 'info',
  duration: 3000,
  showClose: true
});

const emit = defineEmits<{
  close: [];
}>();

const visible = ref(false);

const show = () => {
  visible.value = true;
};

const close = () => {
  visible.value = false;
  emit('close');
};

const hideAfterDelay = () => {
  if (props.duration > 0) {
    setTimeout(() => {
      close();
    }, props.duration);
  }
};

onMounted(() => {
  show();
  hideAfterDelay();
});

defineExpose({
  show,
  close
});
</script>

<style scoped>
.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  max-width: 400px;
  min-width: 300px;
}

.toast-content {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4) var(--space-5);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  backdrop-filter: blur(10px);
  border: 1px solid var(--border-light);
  animation: slideInRight 0.3s ease-out;
}

.toast-icon {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
}

.toast-message {
  flex: 1;
  font-size: 0.875rem;
  font-weight: 500;
  line-height: 1.4;
}

.toast-close {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  background: none;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  transition: all var(--transition-fast);
  opacity: 0.6;
}

.toast-close:hover {
  opacity: 1;
  background: rgba(0, 0, 0, 0.1);
}

/* 成功类型 */
.toast-container.success .toast-content {
  background: linear-gradient(135deg, #d4edda, #c3e6cb);
  border-color: #28a745;
  color: #155724;
}

.toast-container.success .toast-icon {
  color: #28a745;
}

/* 错误类型 */
.toast-container.error .toast-content {
  background: linear-gradient(135deg, #f8d7da, #f5c6cb);
  border-color: #dc3545;
  color: #721c24;
}

.toast-container.error .toast-icon {
  color: #dc3545;
}

/* 警告类型 */
.toast-container.warning .toast-content {
  background: linear-gradient(135deg, #fff3cd, #ffeaa7);
  border-color: #ffc107;
  color: #856404;
}

.toast-container.warning .toast-icon {
  color: #ffc107;
}

/* 信息类型 */
.toast-container.info .toast-content {
  background: linear-gradient(135deg, #d1ecf1, #bee5eb);
  border-color: #17a2b8;
  color: #0c5460;
}

.toast-container.info .toast-icon {
  color: #17a2b8;
}

/* 动画 */
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from {
  opacity: 0;
  transform: translateX(100%);
}

.toast-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(100%);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .toast-container {
    top: 10px;
    right: 10px;
    left: 10px;
    max-width: none;
    min-width: auto;
  }
  
  .toast-content {
    padding: var(--space-3) var(--space-4);
  }
}
</style> 