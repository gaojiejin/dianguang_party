import Vue from 'vue'
import Vuex from 'vuex'
import user from '@/store/modules/user'
import tab from '@/store/modules/tab'
import createPersistedState from "vuex-persistedstate";
Vue.use(Vuex);

export default new Vuex.Store({
    modules:{
        user,
        tab,
    },
    // 数据持久化
    plugins: [createPersistedState()],
})
