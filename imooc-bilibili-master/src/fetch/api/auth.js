import Api from '../http/base.js';

export default {
	//获取权限列表
	async getAuthList (param) {
	  const methodType = 'GET';
	  const url = '/user-authorities';
	  return Api(methodType, url, param);
	},

};
