// 页面路径：store/index.js 
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);//vue的插件机制

//Vuex.Store 构造器选项
const store = new Vuex.Store({
	state:{
		user: [],
		token: '',
		job_id: '',
		role: '',
		phone: '',
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
		}
    }
})
export default store
