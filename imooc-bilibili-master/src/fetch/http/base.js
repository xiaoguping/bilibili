import axios from 'axios';
import router from '../../router/index.js';
// import {
// 	baseUrl
// } from '../../../config/env.js';
import {
	Message,
	Loading
} from 'element-ui';
let loading;

function startLoading() {
	loading = Loading.service({
		lock: true,
		text: '加载中……',
		background: 'rgba(0, 0, 0, 0.7)'
	});
}

function endLoading() {
	loading.close();
}
async function Api(methodType, url, params) {
	let baseUrl = process.env.VUE_APP_BASE_URL;
	startLoading();
	let axiosOption = {
		baseURL: baseUrl,
		timeout: 30000,
		responseType: 'json',
		transformRequest: [function(data) {
			// 对 data 进行任意转换处理
			return JSON.stringify(data);
		}],

	}



	let http = axios.create(axiosOption);

	// 返回状态判断(添加响应拦截器)
	http.interceptors.response.use(
		res => {
			endLoading();
			// axios.defaults.headers.common.ut = utils.getLS('ut')?utils.getLS('ut'):"";
			return res;
		},
		error => {
			endLoading();
			Message({
				showClose: true,
				message: '网络请求失败',
				type: 'error'
			});
			// if (
			// 	(error.response && error.response.status === 404) ||
			// 	(error.response && error.response.status === 403) ||
			// 	(error.response && error.response.status === 500) ||
			// 	(error.response && error.response.status === 505)
			// ) {

			// }
			// window.vueObject.$message('网络超时或异常请重试');
			return Promise.reject(error);
		}
	);

	let key = url.match(/{(\S*)}/);
	if (key) {
		key = key[1];
		url = url.match(/(\S*){/)[1];
		url += params[key];
		delete params[key];
	}
	let headers = {
		Accept: 'application/json',
		'Content-Type': 'application/json;charset=UTF-8',
	}
	if (localStorage.getItem('token')) {
		headers.token = localStorage.getItem('token')
	}
	if (localStorage.getItem('refreshToken')) {
		headers.refreshToken = localStorage.getItem('refreshToken')
	}
	let paramData = {
		method: methodType,
		url: url,
		headers: headers
	}
	if (methodType === 'GET' || methodType === 'DELETE') {
		paramData.params = params;
	} else {
		paramData.data = params;
	}
	let resData = await http(paramData);
	switch (resData.data.code) {
		case '0':
			return resData.data;
			break;
		case '555': //用户token过期 清除token 返回登录页面
			if (localStorage.getItem('refreshToken')) {
				axios.post(baseUrl + '/access-tokens', {}, {
					headers: {
						refreshToken: localStorage.getItem('refreshToken')
					}
				}).then((res) => {
					if (res.data && res.data.code === '0') {
						localStorage.setItem('token', res.data.data);
					} else if (res.data.code === '555') {
						localStorage.removeItem('token');
						router.push({
							path: '/dist/login'
						});
					}
				}).catch((err) => {
				})
			}
			if(!localStorage.getItem('refreshToken')){
				localStorage.removeItem('token');
				this.token = undefined
				Message({
					showClose: true,
					message: "登陆失效，请重新登陆",
					type: 'error'
				});
			}
			break;
		case '556': //toekn 异常返回登录页面重新获取token
			localStorage.removeItem('token');
			router.push({
				path: '/dist/login'
			});
			break;
		default:
			break;
	}
	if (resData.data.code === '0') {
		return resData.data;
	} else if (resData.data.code === '500') {
		Message({
			showClose: true,
			message: resData.data.msg,
			type: 'error'
		});
	}

}
export default Api;
