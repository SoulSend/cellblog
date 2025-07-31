// stores/authStore.ts
import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: false,
    user: {
      id: '',
      account: '',
      nickname: '',
      avatar: '',
    },
    token: ''
  }),
  getters: {
    getUser: (state) => state.user,
    userIsAuthenticated: (state) => state.isAuthenticated,
  },
  actions: {
    login(token: string) {
      this.isAuthenticated = true;
      this.token = token;
      localStorage.setItem('token_CellBlog', token);
    },
    logout() {
      this.isAuthenticated = false;
      this.user = {
        id: '',
        account: '',
        nickname: '',
        avatar: '',
      };
      this.token = '';
      localStorage.removeItem('token_CellBlog');
    },
    async setUser(user: any) {
      this.user = user;
      localStorage.setItem('user_CellBlog', JSON.stringify(user));
    },
    async fetchUser() {
      const userJson = localStorage.getItem('user_CellBlog');
      if (userJson) {
        this.user = JSON.parse(userJson);
        this.isAuthenticated = true;
      }
    }
  }
});