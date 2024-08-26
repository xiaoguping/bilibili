import Api from '../http/file.js';

export default {
  //上传视频
  async upload (param,onProgress) {
    const methodType = 'PUT';
    const url = '/file-slices';
    return Api(methodType, url, param, onProgress);
  },
};
