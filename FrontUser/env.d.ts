/// <reference types="vite/client" />
declare module '*vue' {
    import { ComponentOptions } from "vue";
    const ComponentOptions: ComponentOptions
    export default ComponentOptions
}
declare module "vuex" {
    export * from "vuex/types/index.d.ts";
    export * from "vuex/types/helpers.d.ts";
    export * from "vuex/types/logger.d.ts";
    export * from "vuex/types/vue.d.ts";
  
}