<template>
	<div class="b-header-wrap">
		<div class="home-header-wrap">
			<div class="menu-wrap">
				<div class="menu-item" v-for="(item,index) in headerList" :key="index" @click="menuClick(item)">
					{{ item.title }}
				</div>
			</div>
			<div class="search-row-wrap" v-if="showSearch">
				<div class="search-row">
					<input class="input" v-model="queryContent" type="text">
					<i @click="query" class="icon el-icon-search"></i>
				</div>
			</div>

			<div class="menu-right-wrap">
				<div v-if="!token" class="head-wrap">

					<div class="head-img" @mouseover="active = false" @mouseout="active = true"
						:class="[ chapter === 'login' && active ? 'animation' : '' ]"
						@click.stop="$store.commit('setShowSignin', true)">
						登录
					</div>
					<div class="signin" @click.stop v-if="showSignin">
						<div class="title">登录以后你可以:</div>
						<div class="text-row">
							<div>免费看高清视频</div>
							<div>多端同步播放记录</div>
						</div>
						<div class="text-row">
							<div>发表弹幕/评论</div>
							<div>热门番剧影视看不停</div>
						</div>
						<el-button class="button-login" type="primary" @click="$router.push('/dist/login')">立即登录
						</el-button>
						<div class="signin-wrap">
							首次使用？<span @click="$router.push('/dist/register')">点我注册</span>
						</div>
					</div>
				</div>
				<div v-else class="user-info-wrap">
					<div class="avatar" @click.stop="$store.commit('setShowInfo', true)">
						<img src="../../assets/image/home/avatar.png" alt="">
					</div>
					<div v-if="showInfo" class="user-info-content" @click.stop>
						<div class="nick-title">
							{{ userInfo.userInfo ? userInfo.userInfo.nick : '萌新' }}
						</div>
						<div class="follow-wrap">
							<div class="follow-item" :class="[chapter === 'follow' && active ? 'animation' : '' ]"
								@mouseover="active = false" @mouseout="active = true" @click="$router.push('/dist/follows')">
								<!-- <div class="num">1</div> -->
								<div class="title">关注</div>
							</div>
							<div class="follow-item" :class="[chapter === 'moments' && active ? 'animation' : '' ]"
								@mouseover="active = false" @mouseout="active = true" @click="moment">
								<!-- <div class="num">1</div> -->
								<div class="title">动态</div>
							</div>
						</div>
						<div class="menu-item" v-for="item in userInfoMenu" :key="item.code"
							@click="$router.push(item.route)">
							<img :src="item.icon" alt="" class="icon">
							<div class="title">{{ item.title }}</div>
							<img class="arrow" src="../../assets/image/common/arrow_right.png" alt="">
						</div>
						<div class="logout" @click="logout">
							退出登录
						</div>
					</div>
				</div>
				<div class="menu-row-wrap">
					<div class="menu-item" v-for="(item,index) in menuListRight" @mouseover="moseover(item)"
						@click="menuClick(item)" :key="index">
						<span>{{ item.title }}</span>
						<Moments v-if="momentsFlag(item)" class="moments-pop" @click.stop />
					</div>
				</div>
				<div class="contribution-wrap" @click="contribution">
					<div class="contribution-button">
						投稿
					</div>
					<div class="area-wrap" v-if="showContribution">
						<div class="area-item" v-for="item in areas" :key="item.id" @click.stop="postMoments(item)">
							<img class="icon" :src="item.icon" alt="">
							<div class="title">{{ item.title }}</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
	import {
		mapState
	} from 'vuex';
	import api from '../../fetch/api/users.js';
	import registerApi from '../../fetch/api/register.js';
	import Moments from '../moments/Moments.vue';

	export default {
		name: "",
		components: {
			Moments
		},
		props: {
			showSearch: {
				type: Boolean,
				default: true
			},
		},
		computed: {
			...mapState({
				showSignin: state => state.showSignin,
				showInfo: state => state.showInfo,
				showMoments: state => state.showMoments,
				operateAuth: state => state.authData.operateAuthList,
				chapter: state => state.chapter,
				token: state => state.token
			}),
			authClass(code) {
				return (code) => {
					console.log(this.operateAuth);
					console.log(code);
					let res = '';
					if (!code) {
						res = ''
					} else {
						if (!this.operateAuth) {
							res = 'disable';
						} else {
							if (this.operateAuth.indexOf(code) === -1) {
								res = 'disable';
							}
						}

					}
					console.log(res);
					return res;
				}
			},
			momentsFlag(item) {
				return (item) => {
					let res = false;
					if (localStorage.getItem('token') && item.code === 'moments' && this.showMoments) {
						res = true;
					}
					return res;
				}
			}
		},
		created() {
			this.getUserInfo();
		},
		data() {
			return {
				areas: [{
						id: 0,
						title: '视频投稿',
						icon: require('../../assets/image/moment/video.png'),
						authCode: 'postVideoButton'
					},
					{
						id: 1,
						title: '直播投稿',
						icon: require('../../assets/image/moment/stream.png')
					},
					{
						id: 2,
						title: '专栏投稿',
						icon: require('../../assets/image/moment/area.png')
					}
				],
				active: true,
				headerList: [
					{
						code:  'home',
						title: '主站',
						route: '/dist/home-page',
					},
					{
						title: '番剧',
					},
					{
						title: '游戏中心'
					},
					{
						title: '直播'
					},
					{
						title: '会员购'
					},
					{
						title: '漫画'
					},
					{
						title: '赛事'
					},

				],
				menuListRight: [{
						title: '大会员'
					},
					{
						title: '消息'
					},
					{
						code: 'moments',
						title: '动态',
					},
					{
						title: '收藏'
					},
					{
						title: '历史'
					},
					{
						title: '创作中心'
					},
				],
				tabList: [{
						title: '动态',
						icon: require('../../assets/image/home/dynamic.png'),
						background: '#F28D8B'
					},
					{
						title: '热门',
						icon: require('../../assets/image/home/hot.png'),
						background: '#F28D8B'
					},
					{
						title: '频道',
						icon: require('../../assets/image/home/channel.png'),
						background: '#5ACA73'
					},
				],
				userInfoMenu: [{
					title: '个人中心',
					code: 'personal',
					icon: require('../../assets/image/home/personal.png'),
					route: '/dist/personal'
				}, ],
				userInfo: {},
				queryContent: '',
				showContribution: false,
				videoUrl: ''
			}
		},
		methods: {
			
			moment(){
				
				console.log(localStorage.getItem("token"))
				if(localStorage.getItem("token")){
					this.$router.push('/dist/moments')
				}else{
					this.$message({
						type: "error",
						message: "请先登陆"
					})
				}
				
			},
			
			async postMoments(item) {
				// if (this.$store.state.chapter !== 'auth') {
				// 	let checkRes = this.checkAuth(item.authCode);
				// 	if (!checkRes) {
				// 		this.$message({
				// 			type: 'error',
				// 			message: '您的权限不足'
				// 		})
				// 		return false;
				// 	}
				// }
				if (item.authCode === 'postVideoButton') {
					//视频投稿 上传视频
					// console.log(this.$refs.fileInput[0]);
					this.$router.push('/dist/moments');
					// this.$refs.fileInput[0].click();
				} else {
					let params = {
						type: item.id,
						contentId: '1'
					}
					let res = await api.postUserMoments(params);
					if (res && res.code === '0') {
						this.$message({
							type: 'success',
							message: '发布成功'
						})
					}
				}

			},
			checkAuth(code) {
				let res = true;
				if (!code) {
					res = true
				} else {
					if (!this.operateAuth) {
						res = false;
					} else {
						if (this.operateAuth.indexOf(code) === -1) {
							res = false;
						}
					}

				}
				return res;
			},
			contribution() {
				//判断是否登陆
				if(!this.token){
					this.$message({
						type: "error",
						message: "请先登陆"
					})
					return;
				}
				this.showContribution = !this.showContribution;
			},
			async getUserInfo() {
				
				if (localStorage.getItem("token")) {
					let res = await api.userInfo();
					if (res && res.code === '0') {
						this.userInfo = res.data;
						this.token = localStorage.getItem("token")
					}
				}
			},
			menuClick(item) {
				if(item.code === 'moments'){
					if(localStorage.getItem("token")){
						this.$router.push('/dist/moments')
					}else{
						this.$message({
							type: "error",
							message: "请先登陆"
						})
					}
				}
				if(item.code === 'home'){
					this.$router.push('/')
				}
			},
			moseover(item) {
				if (item.code === 'moments') {
					this.$store.commit('setMoments', true);
				}
			},
			async logout() {
				if (this.$store.state.chapter === 'login') {
					localStorage.removeItem('token');
					this.$store.commit('setToken',null);
					this.$message({
						type: "success",
						message: "退出登陆"
					})
				} else {
					let res = await registerApi.logout();
					if (res && res.code === '0') {
						localStorage.removeItem('token');
						localStorage.removeItem('refreshToken');
						this.token = "";
					}
				}
			},
			query() {
				this.$router.push({
					path: '/dist/search-result',
					query: {
						content: this.queryContent
					}
				});
			}
		},
	}
</script>
<style lang="scss" scoped>
	@keyframes notice {
		0% {
			transform: scale(1);
		}

		50% {
			transform: scale(1.4);
		}

		100% {
			transform: scale(1);
		}
	}

	.animation {
		animation: notice 2s infinite;
	}

	.b-header-wrap {
		width: 100%;

		.home-header-wrap {
			width: 100%;
			padding: 10px 24px;
			display: flex;
			align-items: center;

			.menu-wrap {
				width: fit-content;
				height: 32px;
				display: flex;
				flex: 4;
			}

			.search-row-wrap {
				flex: 8;
				display: flex;
				justify-content: center;

				.search-row {
					width: 500px;
					height: 36px;
					padding-right: 50px;
					background-color: #fff;
					border-radius: 6px;
					overflow: hidden;
					position: relative;

					& .input {
						width: 100%;
						height: 100%;
						border: 0;
						padding-left: 10px;
						outline: none;
					}

					.icon {
						position: absolute;
						right: 10px;
						top: 50%;
						font-size: 20px;
						margin-top: -10px;
						cursor: pointer;

					}
				}
			}


			.menu-wrap {
				justify-content: flex-start;
			}

			.menu-wrap,
			.menu-right-wrap {

				height: 100%;

				.menu-row-wrap {
					display: flex;
					height: 100%;
				}

				.head-wrap {
					position: relative;



					.head-img {
						width: 36px;
						height: 36px;
						border-radius: 50%;
						background-color: #fff;
						margin-right: 30px;
						display: flex;
						align-items: center;
						justify-content: center;
						font-size: 14px;
						color: #00a1d6;
						font-weight: 600;
						cursor: pointer;
					}


					.signin {
						position: absolute;
						width: 350px;
						z-index: 99;
						background-color: #fff;
						top: 50px;
						left: -150px;
						transition: all .3s;
						box-shadow: 0px 0px 15px 4px rgba($color: #000000, $alpha: .1);
						padding: 20px;
						font-size: 14px;
						color: #333;

						div {
							width: fit-content;
						}

						.title {
							margin-bottom: 20px;
						}

						.text-row {
							width: 100%;
							display: flex;
							margin-bottom: 20px;

							div {
								width: 50%;
								text-align: left;
							}
						}

						.button-login {
							width: 100%;
							background-color: #00a1d7;
							margin-bottom: 20px;
						}

						.signin-wrap {
							width: 100%;
							text-align: center;

							span {
								color: #00a1d6;
								cursor: pointer;
							}
						}


					}
				}


				.user-info-wrap {
					margin-right: 30px;
					position: relative;

					.avatar {
						width: 36px;
						height: 36px;
						border-radius: 50%;
						overflow: hidden;
						border: 2px solid #fff;
						background-color: #fff;
						cursor: pointer;

						& img {
							display: block;
							width: 100%;
							height: 100%;
						}
					}

					.user-info-content {
						width: 250px;
						border-radius: 10px;
						background-color: #fff;
						// height: 500px;
						position: absolute;
						top: 50px;
						left: -110px;
						padding: 20px;
						box-shadow: 0px 0px 15px 4px rgba($color: #000000, $alpha: .1);
						z-index: 99;

						.nick-title {
							width: 100%;
							font-size: 20px;
							text-align: center;
							padding: 10px 0;
						}

						.follow-wrap {
							width: 100%;
							display: flex;
							justify-content: space-between;
							font-size: 14px;
							color: #333;
							padding: 0 10px;
							margin-bottom: 10px;

							.follow-item {
								width: fit-content;
								display: flex;
								flex-direction: column;
								align-items: center;
								cursor: pointer;
							}
						}

						.menu-item {
							width: 100%;
							background-color: #fff;
							display: flex;
							border-radius: 10px;
							height: 48px;
							align-items: center;
							padding: 5px;
							cursor: pointer;


							&:hover {
								background-color: #bfbfbf;
							}

							.icon {
								height: 20px;
								display: block;
							}

							.title {
								font-size: 14px;
								font-weight: 400;
								color: #333;
								margin-left: 10px;
							}

							.arrow {
								margin-left: auto;
								height: 18px;
								display: block;
							}
						}

						.logout {
							width: 100%;
							text-align: center;
							cursor: pointer;
							margin-top: 20px;
						}
					}

				}


				.menu-item {
					height: 100%;
					width: fit-content;
					font-size: 14px;
					color: #fff;
					margin-right: 12px;
					font-weight: 400;
					cursor: pointer;
					font-weight: 500;
					position: relative;

					.moments-pop {
						position: absolute;
						top: 50px;
						left: -80px;
					}
				}
			}

			.contribution-wrap {
				position: relative;

				.contribution-button {
					width: 100px;
					height: 36px;
					background-color: #fb7299;
					color: #fff;
					text-align: center;
					line-height: 36px;
					border-radius: 5px;
					cursor: pointer;
				}

				.area-wrap {
					width: fit-content;
					display: flex;
					padding: 16px;
					background-color: #fff;
					border-radius: 10px;
					position: absolute;
					left: -250px;
					top: 50px;
					box-shadow: 0 0 15px 4px rgba($color: #000, $alpha: 0.1);
					cursor: pointer;

					.area-item {
						width: 100px;
						display: flex;
						flex-direction: column;
						justify-content: center;
						align-items: center;

						.icon {
							width: 30px;
							height: 30px;
							display: block;
						}

						.title {
							font-size: 16px;
						}

						.file-input {
							// opacity: 0;
							display: none;
						}
					}

					.disable {
						color: #999999;
					}
				}
			}

			.menu-right-wrap {
				display: flex;
				justify-content: flex-end;
				align-items: center;
				flex: 4;
			}
		}
	}
</style>
