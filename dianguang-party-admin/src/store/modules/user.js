import Cookie from 'js-cookie'

export default {
    state: {
        user: '',
        token: '',
        branchIdToName: [],
    },
    getters: {
        // 定义一个 getter 来根据 branch_id 获取 branch_name
        getBranchName: (state) => (branchId) => {
            const branch = state.branchIdToName.find(branch => branch.branch_id === branchId);
            return branch ? branch.branch_name : null;
        },
        getBranchId: (state) => (branchName) => {
            const branch = state.branchIdToName.find(branch => branch.branch_name === branchName);
            return branch ? branch.branch_id : null;
        },
        getToken(state) {
            return state.token || Cookie.get('token')
        },
        getUser(state) {
            return state.user || Cookie.get('user')
        },
    },
    mutations: {
        setToken(state, val) {
            state.token = val
            Cookie.set('token', val)
        },
        clearToken(state) {
            state.token = ''
            Cookie.remove('token')
        },

        setUser(state, val) {
            state.user = val
            Cookie.set('user', val)
        },

        clearUser(state) {
            state.user = ''
            Cookie.remove('user')
        },


    }
}
