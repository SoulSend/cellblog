<template>
  <div class="not-found">
    <div class="not-found-container">
      <div class="not-found-content">
        <div class="error-code">404</div>
        <h1 class="error-title">页面未找到</h1>
        <p class="error-description">
          抱歉，您访问的页面不存在或已被移除。
        </p>
        <div class="error-actions">
          <button class="action-btn primary" @click="goHome">
            <span class="action-icon">🏠</span>
            <span>返回首页</span>
          </button>
          <button class="action-btn secondary" @click="goBack">
            <span class="action-icon">←</span>
            <span>返回上页</span>
          </button>
        </div>
      </div>
      
      <div class="error-illustration">
        <div class="illustration-container">
          <div class="floating-element" style="--delay: 0s">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"></path>
            </svg>
          </div>
          <div class="floating-element" style="--delay: 0.5s">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
            </svg>
          </div>
          <div class="floating-element" style="--delay: 1s">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
            </svg>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
  router.push('/home');
};

const goBack = () => {
  router.go(-1);
};
</script>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--bg-secondary) 0%, var(--bg-tertiary) 100%);
  padding: var(--space-6);
}

.not-found-container {
  max-width: 800px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-12);
  align-items: center;
}

.not-found-content {
  text-align: left;
}

.error-code {
  font-size: 8rem;
  font-weight: 900;
  color: var(--primary-color);
  line-height: 1;
  margin-bottom: var(--space-4);
  text-shadow: 0 4px 8px rgba(6, 186, 199, 0.3);
}

.error-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-4) 0;
  line-height: 1.2;
}

.error-description {
  font-size: 1.125rem;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 0 0 var(--space-8) 0;
  max-width: 400px;
}

.error-actions {
  display: flex;
  gap: var(--space-4);
  flex-wrap: wrap;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-4) var(--space-6);
  border: none;
  border-radius: var(--radius-lg);
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all var(--transition-fast);
  text-decoration: none;
}

.action-btn.primary {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  color: white;
  box-shadow: var(--shadow-sm);
}

.action-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.action-btn.secondary {
  background: var(--bg-primary);
  color: var(--text-primary);
  border: 2px solid var(--border-color);
}

.action-btn.secondary:hover {
  border-color: var(--primary-color);
  color: var(--primary-color);
  transform: translateY(-2px);
}

.action-icon {
  font-size: 1.125rem;
}

/* 装饰性动画 */
.error-illustration {
  display: flex;
  align-items: center;
  justify-content: center;
}

.illustration-container {
  position: relative;
  width: 300px;
  height: 300px;
}

.floating-element {
  position: absolute;
  font-size: 2rem;
  animation: float 3s ease-in-out infinite;
  animation-delay: var(--delay);
}

.floating-element:nth-child(1) {
  top: 20%;
  left: 20%;
}

.floating-element:nth-child(2) {
  top: 30%;
  right: 30%;
}

.floating-element:nth-child(3) {
  bottom: 30%;
  left: 30%;
}

.floating-element:nth-child(4) {
  bottom: 20%;
  right: 20%;
}

.floating-element svg {
  color: var(--primary-color);
  opacity: 0.8;
  transition: all var(--transition-normal);
}

.floating-element:hover svg {
  opacity: 1;
  transform: scale(1.1);
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(10deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .not-found-container {
    grid-template-columns: 1fr;
    gap: var(--space-8);
    text-align: center;
  }
  
  .error-code {
    font-size: 6rem;
  }
  
  .error-title {
    font-size: 2rem;
  }
  
  .error-description {
    font-size: 1rem;
    max-width: none;
  }
  
  .error-actions {
    justify-content: center;
  }
  
  .illustration-container {
    width: 200px;
    height: 200px;
  }
  
  .floating-element {
    font-size: 1.5rem;
  }
}

@media (max-width: 480px) {
  .not-found {
    padding: var(--space-4);
  }
  
  .error-code {
    font-size: 4rem;
  }
  
  .error-title {
    font-size: 1.5rem;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .action-btn {
    width: 100%;
    max-width: 300px;
    justify-content: center;
  }
}
</style> 