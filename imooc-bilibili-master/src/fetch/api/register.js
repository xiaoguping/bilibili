import Api from '../http/base.js';

export default {
  //获取公钥
  async getKey (param) {
    const methodType = 'GET';
    const url = '/rsa-pks';
    return Api(methodType, url, param);
  },
  //用户注册
  async register (param) {
    const methodType = 'POST';
    const url = '/users';
    return Api(methodType, url, param);
  },
  //用户登录
  async login (param) {
    const methodType = 'POST';
    const url = '/user-tokens';
    return Api(methodType, url, param);
  },
  //用户登录2.0
  async loginV2 (param) {
    const methodType = 'POST';
    const url = '/user-dts';
    return Api(methodType, url, param);
  },
  //退出登录
  async logout (param) {
    const methodType = 'DELETE';
    const url = '/refresh-tokens';
    return Api(methodType, url, param);
  },
  //用户信息
  async userInfo (param) {
    const methodType = 'GET';
    const url = '/users';
    return Api(methodType, url, param);
  },
	//更新用户信息
	async putProfile (param) {
	  const methodType = 'PUT';
	  const url = '/user-infos';
	  return Api(methodType, url, param);
	},
	//更新用户账号信息
	async putAccount (param) {
	  const methodType = 'PUT';
	  const url = '/users';
	  return Api(methodType, url, param);
	},
	//查询用户
	async queryUsers (param) {
	  const methodType = 'GET';
	  const url = '/user-infos';
	  return Api(methodType, url, param);
	},

};
