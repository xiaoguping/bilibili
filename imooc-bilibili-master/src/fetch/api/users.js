import Api from '../http/base.js';

export default {
	//用户信息
	async userInfo(param) {
		const methodType = 'GET';
		const url = '/users';
		return Api(methodType, url, param);
	},
	//更新用户信息
	async putProfile(param) {
		const methodType = 'PUT';
		const url = '/user-infos';
		return Api(methodType, url, param);
	},
	//更新用户账号信息
	async putAccount(param) {
		const methodType = 'PUT';
		const url = '/users';
		return Api(methodType, url, param);
	},
	//查询用户
	async queryUsers(param) {
		const methodType = 'GET';
		const url = '/contents';
		return Api(methodType, url, param);
	},
	//关注
	async follow(param) {
		const methodType = 'POST';
		const url = '/user-followings';
		return Api(methodType, url, param);
	},
	//关注列表
	async getFollows(param) {
		const methodType = 'GET';
		const url = '/user-followings';
		return Api(methodType, url, param);
	},
	//新建关注分组
	async putFollowGroup(param) {
		const methodType = 'POST';
		const url = '/user-following-groups';
		return Api(methodType, url, param);
	},

	//查询用户当前分组
	async getFollowGroup(param) {
		const methodType = 'GET';
		const url = '/user-following-groups';
		return Api(methodType, url, param);
	},
	//查询用户粉丝列表
	async getUserFans(param) {
		const methodType = 'GET';
		const url = '/user-fans';
		return Api(methodType, url, param);
	},
	//获取用户动态列表
	async getUserMoments(param) {
		const methodType = 'GET';
		const url = '/user-subscribed-moments';
		return Api(methodType, url, param);
	},
	//添加用户动态
	async postUserMoments(param) {
		const methodType = 'POST';
		const url = '/user-moments';
		return Api(methodType, url, param);
	},
	//视频投稿
	async postVideos(param) {
		const methodType = 'POST';
		const url = '/videos';
		return Api(methodType, url, param);
	},

};
