<template>
	<div class="follow-wrap">
		<div class="header-top">
			<Bheader :showSearch="false"></Bheader>
		</div>
		<Title title="动态"></Title>
		<div class="img-top"></div>
		<div class="follow-content">
			<div class="put-moments">
				<el-button type="primary" @click="selectVideo">选择视频</el-button>
				<div class="progress-wrap">
					<el-progress :text-inside="true" :stroke-width="26" :percentage="percent" style="width: 80%;">
					</el-progress>
				</div>
				<input type="file" class="file-input" @change="postFile" ref="fileInput">
				<div class="form">
					<el-form :model="postParams">
						<el-form-item label="标题*" width="180">
							<el-input v-model="postParams.title"></el-input>
						</el-form-item>
						<el-form-item label="类型*" width="180">
							<el-radio v-model="postParams.type" label="0">自制</el-radio>
							<el-radio v-model="postParams.type" label="1">转载</el-radio>
						</el-form-item>
					</el-form>
				</div>

				<!-- <el-input class="moment-input" type="textarea" placeholder="有什么想和大家分享的？"></el-input> -->
				<div class="button-wrap">
					<el-button type="primary" class="button-put" @click="postVideos(postParams)">发布</el-button>
				</div>
			</div>
			<div class="moments-list" v-for="item in moments" :key="item.id">
				<span>contentId: {{item.contentId}}</span><span>userId: {{item.userId}}</span><span>type:
					{{item.type}}</span>
			</div>
		</div>

	</div>
</template>
<script>
	import Title from '../../components/common/Title.vue';
	import api from '../../fetch/api/users.js';
	import uploadApi from '../../fetch/api/upload.js';
	import Bheader from '../../components/common/B_header.vue';
	import {
		fileSlices
	} from '../../assets/js/file_slices.js';
	export default {
		name: "",
		components: {
			Title,
			Bheader
		},
		computed:{
			token(){
				return this.$store.state.token;
			}
		},
		watch:{
			token(newToken){
				console.log(newToken)
				if(newToken === null){
					this.$router.push('/')
				}
			}
		},
		created() {
			this.getUserMoments();
		},
		data() {
			return {
				params: {
					type: "0",
					"contentId": 1
				},
				moments: [],
				percent: 0,
				showProgress: true,
				chunksInfo: null,
				sliceTotal: 0,
				postParams: {
					title: '',
					url: '',
					thumbnail: "https://dhealth.oss-cn-beijing.aliyuncs.com/010_uv.png",
					description: '',
					type: '0',
					area: '0',
					duration: '',
					videoTagList: [{
							"tagId": 1
						},
						{
							"tagId": 2
						}
					],
				}
			}
		},
		methods: {
			selectVideo() {
				this.$refs.fileInput.click();
			},
			postFile(e) {
				let file = e.target.files[0];
				let _this = this;
				fileSlices(file, 1024 * 1024 * 1)
					.then(async (res) => {
						console.log(res);
						if (!_this.chunksInfo) {
							_this.chunksInfo = res;
						}
						_this.current = 0;
						upload(_this.current);
						async function upload(current) {
							if (current < res.files.length) {
								let formData = new FormData();
								console.log(res.files[current]);
								formData.append('slice', res.files[current]);
								formData.append('fileMd5', res.md5);
								formData.append('sliceNo', current + 1);
								formData.append('totalSliceNo', res.total);
								// console.log(formData);
								let uploadRes = await uploadApi.upload(formData, _this.onProgress)
								if (uploadRes && uploadRes.code === '0') {
									if (current === res.files.length - 1) {
										_this.$message({
											type: 'success',
											message: '上传成功'
										})
										_this.postParams.url = uploadRes.data;
										// _this.postVideos(uploadRes.data);
									}
									_this.current++;
									upload(_this.current);
								}
							}

						}
					})

			},
			onProgress(current, total) {
				if (this.current === 0) {
					this.sliceTotal = total;
				}
				let _current = this.current * this.sliceTotal + current;
				this.percent = parseInt(_current / this.chunksInfo.totalSize * 100);
			},
			async postVideos(params) {
				if (!params.url) {
					this.$message({
						type: 'error',
						message: '还未上传视频'
					})
					return false;
				}
				// let params = {
				// 	url: params.url,
				// 	thumbnail: "https://dhealth.oss-cn-beijing.aliyuncs.com/010_uv.png",
				// 	title: params.title,
				// 	type: params.type,
				// 	"duration": "",
				// 	"area": params.area,
				// 	"videoTagList": [{
				// 			"tagId": 1
				// 		},
				// 		{
				// 			"tagId": 2
				// 		}
				// 	],
				// 	"description": params.description
				// }
				let res = await api.postVideos(params);
				if (res && res.code === '0') {
					this.postParamsReset();
					this.$message({
						type: 'success',
						message: '投稿成功'
					})
					this.$router.push('/dist/home-page');
				}
			},
			postParamsReset () {
				this.postParams = {
					title: '',
					url: '',
					thumbnail: "https://dhealth.oss-cn-beijing.aliyuncs.com/010_uv.png",
					description: '',
					type: '0',
					area: '0',
					duration: '',
					videoTagList: [{
							"tagId": 1
						},
						{
							"tagId": 2
						}
					],
				}
			},
			async change(e) {
				let files = e.currentTarget.files;
				for (let i = 0; i < files.length; i++) {
					let formData = new FormData();
					formData.append("file", files[i]);
					formData.append("fileMd5", 'asdasdadasdasda11123sasdsadfff');
					formData.append("sliceNo", i + 1);
					formData.append("totalSliceNo", files.length);
					let res = await uploadApi.upload(formData);


				}
			},
			async getUserMoments() {
				let res = await api.getUserMoments();
				if (res && res.code === '0') {
					this.moments = res.data;
				}
			},
			async postUserMoments() {
				let res = await api.postUserMoments(this.params);
			}
		},
	}
</script>
<style lang="scss" scoped>
	/deep/.el-radio-group {
		display: flex;
		flex-direction: column;
	}

	/deep/ .el-radio {
		padding: 10px 0;
	}

	.follow-wrap {
		width: 100%;



		.dialog-title {
			font-weight: 500;
			color: #99a2aa;
			font-size: 12px;

			span {
				color: #333;
			}


		}

		.set-group-list {
			width: 100%;

		}

		.new-group-wrap {
			width: 100%;
			display: flex;
			margin-top: 20px;

			/deep/ .el-button {
				margin-left: 10px;
			}
		}

		.header-top {
			width: 100%;
			background: url(../../assets/image/header/header_bg.jpeg) no-repeat;
			background-size: cover;
			margin-bottom: 40px;
		}

		.img-top {
			width: 980px;
			height: 200px;
			display: block;
			margin: 0 auto;
			background: url(../../assets/image/header/header_bg.jpeg) no-repeat center center;
			background-size: cover;
		}

		.follow-content {
			width: 980px;
			margin: 0 auto;
			border-radius: 5px;
			background-color: #fff;
			margin-top: 20px;
			height: 600px;

			.put-moments {
				width: 100%;
				padding: 16px;
				box-shadow: 0 0 15px 4px rgba($color: #000000, $alpha: 0.1);
				border-radius: 10px;
				height: fit-content;

				.form {
					width: 100%;
					margin-top: 20px;

					/deep/ .el-input {
						width: 500px;
					}

					/deep/ .el-form-item__label {
						width: 180px;
						text-align: left;
					}
				}

				.progress-wrap {
					margin-top: 20px;
					width: 100%;
				}

				.file-input {
					display: none;
				}

				.moment-input {
					width: 100%;
				}

				.button-wrap {
					width: 100%;
					display: flex;
					justify-content: flex-end;
					padding: 10px 0;

					.button-put {
						background-color: #00a1d6;
						color: #fff;
					}
				}
			}

			.moments-list {
				width: 100%;
				box-shadow: 0 0 10px 4px rgba($color: #000000, $alpha: 0.1);
				padding: 16px;
				height: fit-content;
				border-radius: 10px;
				margin-top: 20px;

				span {
					margin-right: 10px;
				}
			}
		}
	}
</style>
