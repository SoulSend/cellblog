import { createVNode, render } from 'vue';
import Toast from '@/components/common/Toast.vue';

interface ToastOptions {
  message: string;
  type?: 'success' | 'error' | 'warning' | 'info';
  duration?: number;
  showClose?: boolean;
}

class ToastManager {
  private container: HTMLElement | null = null;

  private createContainer() {
    if (!this.container) {
      this.container = document.createElement('div');
      this.container.id = 'toast-container';
      document.body.appendChild(this.container);
    }
    return this.container;
  }

  private show(options: ToastOptions) {
    const container = this.createContainer();
    
    const vnode = createVNode(Toast, {
      ...options,
      onClose: () => {
        render(null, container);
      }
    });
    
    render(vnode, container);
  }

  success(message: string, duration = 3000) {
    this.show({ message, type: 'success', duration });
  }

  error(message: string, duration = 4000) {
    this.show({ message, type: 'error', duration });
  }

  warning(message: string, duration = 3500) {
    this.show({ message, type: 'warning', duration });
  }

  info(message: string, duration = 3000) {
    this.show({ message, type: 'info', duration });
  }
}

export const toast = new ToastManager(); 