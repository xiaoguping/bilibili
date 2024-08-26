import Api from '../http/formDataApi.js';

export default {
	//点赞
	async postLikes (param) {
	  const methodType = 'POST';
	  const url = '/video-likes';
	  return Api(methodType, url, param);
	},

};
