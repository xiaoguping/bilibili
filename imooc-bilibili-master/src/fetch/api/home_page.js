import Api from '../http/base.js';

export default {
	//获取权限列表
	async getVideoDetail (param) {
	  const methodType = 'GET';
	  const url = '/video-details';
	  return Api(methodType, url, param);
	},
	//获取视频列表
	async getVideoList (param) {
	  const methodType = 'GET';
	  const url = '/videos';
	  return Api(methodType, url, param);
	},

	//取消点赞
	async deleteLikes (param) {
	  const methodType = 'DELETE';
	  const url = '/video-likes';
	  return Api(methodType, url, param);
	},
	// 查询点赞数量
	async getLikes (param) {
	  const methodType = 'GET';
	  const url = '/video-likes';
	  return Api(methodType, url, param);
	},
	// 查询收藏数量
	async getCollections (param) {
	  const methodType = 'GET';
	  const url = '/video-collections';
	  return Api(methodType, url, param);
	},
	// 收藏
	async postCollections (param) {
	  const methodType = 'POST';
	  const url = '/video-collections';
	  return Api(methodType, url, param);
	},
	// 取消收藏
	async deleteCollections (param) {
	  const methodType = 'DELETE';
	  const url = '/video-collections';
	  return Api(methodType, url, param);
	},

	// 查询投币数量
	async getCoins (param) {
	  const methodType = 'GET';
	  const url = '/video-coins';
	  return Api(methodType, url, param);
	},
	// 投币
	async postCoins (param) {
	  const methodType = 'POST';
	  const url = '/video-coins';
	  return Api(methodType, url, param);
	},
	// 获取评论
	async getComments (param) {
	  const methodType = 'GET';
	  const url = '/video-comments';
	  return Api(methodType, url, param);
	},
	// 发送评论
	async postComments (param) {
	  const methodType = 'POST';
	  const url = '/video-comments';
	  return Api(methodType, url, param);
	},
	// 获取弹幕列表
	async getDanmus (param) {
	  const methodType = 'GET';
	  const url = '/danmus';
	  return Api(methodType, url, param);
	},

};