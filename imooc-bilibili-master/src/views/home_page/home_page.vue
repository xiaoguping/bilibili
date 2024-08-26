<template>
	<div class="home-wrap">
		<div class="home-header">
			<Bheader></Bheader>
		</div>
		<div class="nav-wrap">
			<div class="nav-content">
				<div class="tab-wrap">
					<div class="tab-item" v-for="(item, index) in tabList" :key="index">
						<div class="icon" :style="{ background: item.background }">
							<img :src="item.icon" alt="">
						</div>
						<div class="title">{{ item.title }}</div>
					</div>
				</div>
				<div class="gird-menu">
					<div class="category-item" v-for="(item, index) in categoryList" :key="index">
						{{ item.title }}
					</div>
				</div>
			</div>
		</div>
		<div class="video-wrap">
			<div class="video-item">
				<img class="img" src="../../assets/image/home/video_bg_01.jpg" alt="">
			</div>
			<div class="video-item">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
			</div>
			<div class="video-item">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
			</div>
			<div class="video-item">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
			</div>
			<div class="video-item">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
			</div>
			<div class="video-item">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
				<img class="img" src="../../assets/image/home/video_bg_02.png" alt="">
			</div>
		</div>
		<!-- 视频列表 -->
		<div class="area-wrap">
			<div class="area-list" v-if="videoData['0']">
				<div class="area-item" v-for="item in videoData['0']" @click="goDetail(item)">
					<img class="item-img" src="../../assets/image/home/video_bg_02.png" alt="">
					<div class="item-title">{{ item.title }}</div>
					<div class="item-tag"></div>
				</div>
			</div>
			<div class="area-list" v-if="videoData['1']">
				<div class="area-item" v-for="item in videoData['1']" @click="goDetail(item)">
					<img class="item-img" src="../../assets/image/home/video_bg_02.png" alt="">
					<div class="item-title">{{ item.title }}</div>
					<div class="item-tag"></div>
				</div>
			</div>
			<div class="area-list" v-if="videoData['2']">
				<div class="area-item" v-for="item in videoData['2']" @click="goDetail(item)">
					<img class="item-img" src="../../assets/image/home/video_bg_02.png" alt="">
					<div class="item-title">{{ item.title }}</div>
					<div class="item-tag"></div>
				</div>
			</div>
		</div>

	</div>
</template>

<script>
import Bheader from '../../components/common/B_header.vue';
import api from '../../fetch/api/auth.js';
import homeApi from '../../fetch/api/home_page.js';

export default {
	name: 'home',
	data() {
		return {
			baseApi: 'http://localhost:8081',
			token: localStorage.getItem('token'),
			active: true,
			showSignin: false,
			showInfo: false,
			headerList: [{
				'title': '主站'
			},
			{
				'title': '番剧'
			},
			{
				'title': '游戏中心'
			},
			{
				'title': '直播'
			},
			{
				'title': '会员购'
			},
			{
				'title': '漫画'
			},
			{
				'title': '赛事'
			},

			],
			menuListRight: [{
				'title': '大会员'
			},
			{
				'title': '消息'
			},
			{
				'title': '动态'
			},
			{
				'title': '收藏'
			},
			{
				'title': '历史'
			},
			{
				'title': '创作中心'
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
			categoryList: [
				{
					title: '直播',
					code: '0'
				},
				{
					title: '电影',
					code: '1'
				},
				{
					title: '电视剧',
					code: '2'
				},
				{
					title: '纪录片',
					code: '3'
				},
				{
					title: '游戏',
					code: '3'
				},
				{
					title: '音乐',
					code: '3'
				},
				{
					title: '影视',
					code: '3'
				},
				{
					title: '知识',
					code: '3'
				},
				{
					title: '资讯',
					code: '3'
				},
				{
					title: '生活',
					code: '3'
				},
				{
					title: '时尚',
					code: '3'
				},
				{
					title: '动物圈',
					code: '3'
				},
				{
					title: '专栏',
					code: '3'
				},
				{
					title: '小黑屋',
					code: '3'
				},
				{
					title: 'VLOG',
					code: '3'
				},
				{
					title: '番剧',
					code: '3'
				},
				{
					title: '国创',
					code: '3'
				},
				{
					title: '综艺',
					code: '3'
				},
				{
					title: '动画',
					code: '3'
				},
				{
					title: '鬼畜',
					code: '3'
				},
				{
					title: '舞蹈',
					code: '3'
				},
				{
					title: '娱乐',
					code: '3'
				},
				{
					title: '科技',
					code: '3'
				},
				{
					title: '美食',
					code: '3'
				},
				{
					title: '汽车',
					code: '3'
				},
				{
					title: '运动',
					code: '3'
				},
				{
					title: '课堂',
					code: '3'
				},
				{
					title: '活动中心',
					code: '3'
				},
				{
					title: '新歌热榜',
					code: '3'
				},
				{
					title: '更多',
					code: '3'
				},
			],
			userInfoMenu: [
				{
					title: '个人中心',
					code: 'personal',
					icon: require('../../assets/image/home/personal.png'),
					route: '/personal'
				},
			],
			userInfo: {},
			videoData: {}
		}
	},
	created() {
		this.getList('0');
		this.getList('1');
		this.getList('2');
		localStorage.setItem('baseApi', this.baseApi);
		let token = localStorage.getItem('token');
		let module = this.$store.state.chapter;
		if (token && module === 'auth') {
			this.getAuthList();
		}
	},
	components: {
		Bheader
	},
	methods: {
		async getList(area) {
			let params = {
				size: 10,
				no: 1,
				area: area
			}
			let res = await homeApi.getVideoList(params);
			if (res && res.code === '0') {
				let _data = JSON.parse(JSON.stringify(this.videoData));
				_data[area] = res.data.list;
				this.videoData = _data;
				console.log(this.videoData);
			}
		},
		async getAuthList() {
			let res = await api.getAuthList();
			if (res && res.code === '0') {
				this.$store.commit('setAuthList', res.data);
			}
		},
		logout() {
			localStorage.removeItem('token');
			this.token = "";
		},
		goDetail(item) {
			// debugger;
			if (item) {
				console.log(item.id); // 到这里 item.id = 32 是没问题
				let id = item.id;
				this.$router.push({
					path: '/dist/video-detail',
					query: {
						videoId: id
					}
				});
			} else {
				this.$router.push('/dist/video-detail');
			}
			// const route = this.$router.resolve('/video-detail');
			// window.open(route.href, '_blank');
		},

	}
}
</script>

<style lang="scss" scoped>
.home-wrap {
	width: 100%;

	.home-header {

		width: 100%;
		height: 188px;
		background: url(../../assets/image/header/header_bg.jpeg) no-repeat left top;
		background-size: 100%;

		.home-header-wrap {
			width: 100%;
			padding: 10px 24px;
			display: flex;

			.menu-wrap {
				width: fit-content;
				height: 32px;
				display: flex;
			}

			.menu-right-wrap {
				margin-left: auto;
			}

			.menu-wrap,
			.menu-right-wrap {
				display: flex;
				height: 100%;
				align-items: center;

				.head-wrap {
					position: relative;

					.animation {
						animation: notice 2s infinite;
					}

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
				}
			}
		}

	}


	.nav-wrap {
		width: 1630px;
		margin: 0 auto;

		.nav-content {
			width: 100%;
			height: 108px;
			padding: 20px 0;
			display: flex;

			.tab-wrap {
				width: fit-content;
				height: 100%;
				display: flex;

				.tab-item {
					width: 66px;
					height: 100%;
					display: flex;
					flex-direction: column;
					align-items: center;

					.icon {
						width: 36px;
						height: 36px;
						display: block;
						border-radius: 50%;
						overflow: hidden;
						margin-bottom: 4px;
						display: flex;
						justify-content: center;
						align-items: center;

						& img {
							width: 80%;
							display: block;
						}
					}

					.title {
						color: #212121;
						font-size: 14px;
					}

				}
			}

			.gird-menu {
				display: grid;
				grid-template-rows: repeat(2, 32px);
				grid-template-columns: repeat(15, 98px);
				grid-gap: 10px;

				.category-item {
					width: 100%;
					height: 100%;
					border-radius: 5px;
					text-align: center;
					line-height: 32px;
					font-size: 14px;
					font-weight: 400;
					color: #333;
					background-color: #F5F7F8;
					cursor: pointer;

					&:hover {
						background-color: #E7EAEB;
					}
				}
			}


		}
	}

	.video-wrap {
		width: 100%;
		height: 242px;
		width: 1630px;
		margin: 0 auto;
		display: flex;
		justify-content: space-between;
		overflow: hidden;

		.video-item {
			width: fit-content;
			height: 100%;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			cursor: pointer;

			.img {
				width: 206px;
				// height: 116px;
				display: block;
			}



			&:first-child {
				.img {
					width: 550px;
					// height: 100%;
					display: block;
				}
			}
		}
	}


	.area-wrap {
		width: 1630px;
		margin: 0 auto;
		margin-top: 20px;

		.area-list {
			width: 100%;
			display: flex;
			flex-wrap: wrap;

			.area-item {
				width: 206px;
				margin-right: 10px;
				cursor: pointer;

				.item-img {
					width: 100%;
					display: block;
				}
			}
		}

	}


}
</style>
