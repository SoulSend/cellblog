// main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { useAuthStore } from './stores/index';

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

const app = createApp(App)
const pinia = createPinia();

app.use(pinia);

const authStore = useAuthStore();
const token = localStorage.getItem('token_CellBlog');
if (token) {
  authStore.login(token);
  authStore.fetchUser();
}
app.use(ElementPlus);
app.use(router)

app.mount('#app')