import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
		showSignin: false,
		showInfo: false,
		showMoments: false,
		authData: {},
		chapter: 'login',
		token: localStorage.getItem('token')
  },
  mutations: {
		setToken(state, data){
			state.token = data
		},
		setShowSignin (state, data) {
			state.showSignin = data;
		},
		setShowInfo (state, data) {
			state.showInfo = data;
		},
		setMoments (state, data) {
			state.showMoments = data;
		},
		setChapter (state, data) {
			state.chapter = data;
		},
		setAuthList (state, data) {
			let operateAuthList = data.roleElementOperationList.map((item) => {
				return item.authElementOperation.elementCode;
			})
			let displayAuthList = data.roleMenuList.map((item) => {
				return item.authMenu.elementCode;
			})
			let _data = {
				operateAuthList,
				displayAuthList
			}
			state.authData = _data;
		}
  },
  actions: {
  },
  modules: {
  }
})
