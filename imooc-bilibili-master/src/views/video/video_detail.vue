<template>
	<div class="video-detail-wrap">
		<div class="content-left">
			<!-- <div class="home-header">
				<div class="menu-wrap">
					<div class="menu-item" v-for="(item,index) in headerList" :key="index">{{ item.title }}</div>
				</div>
				<div class="menu-right-wrap">
					<div class="menu-wrap">
						<div class="menu-item" v-for="(item,index) in menuListRight" :key="index">{{ item.title }}</div>
					</div>
				</div>
			</div> -->

			<div class="header-top">
				<Bheader :showSearch="false"></Bheader>
			</div>
			<div class="video-content">
				<div class="content-left">
					<div class="title">{{ videoInfo.video ? videoInfo.video.title : '' }}</div>
					<div class="play-info">68.7万播放 · 1641弹幕 2023-02-02 13:54:26</div>
					<div id="vs" class="vs"></div>
					<div class="forward-wrap">
						<div class="icon-item">
							<img v-if="!likesInfo.like" class="img active" @click="postLikes"
								src="../../assets/image/video/icon_01.png" alt="">
							<img v-else class="img active" @click="postLikes"
								src="../../assets/image/video/icon_01_active.png" alt="">
							{{ likesInfo.count }}
						</div>
						<div class="icon-item">
							<img class="img" v-if="!coins.like" @click="postCoins"
								src="../../assets/image/video/icon_02.png" alt="">
							<img class="img" v-else @click="postCoins" src="../../assets/image/video/icon_02_active.png"
								alt="">
							{{ coins.count || coins.count == 0 ? coins.count : 0 }}
						</div>
						<div class="icon-item">
							<img class="img" v-if="!collections.like" @click="postCollections"
								src="../../assets/image/video/icon_03.png" alt="">
							<img class="img" v-else @click="postCollections"
								src="../../assets/image/video/icon_03_active.png" alt="">
							{{ collections.count }}
						</div>
						<!-- <div class="icon-item">
							<img class="img" src="../../assets/image/video/icon_04.png" alt="">
							365
						</div> -->
					</div>
					<div class="danmu-content">
						<textarea rows="" cols="" class="input" placeholder="发一条弹幕" v-model="text"></textarea>
						<div class="send-btn" @click="socketSend">发送弹幕</div>
					</div>

					<div class="comment-wrap">
						<div class="comment-list">
							<div class="comment-item" v-for="(item, index) in comments" :key="index">
								<div class="comment-in">
									<img class="avatar" src="../../assets/image/home/avatar_users.jpeg" alt="">
									<div class="comment-right">
										<div class="name">{{ item.userInfo.nick }}</div>
										<div class="comment-content">{{ item.comment }}</div>
										<div class="time">{{ item.createTime }} <span class="reply"
												@click="setReplyInfo('root', item)">回复</span></div>
										<div class="clild-comments" v-for="(child, childIndex) in item.childList"
											:key="'child_' + index">
											<img class="child-avatar" src="../../assets/image/home/avatar_users.jpeg"
												alt="">
											<div class="child-user-info">
												<div class="child-comment-info">
													<span class="child-name">{{ child.userInfo.nick }}</span>
													<span class="child-comment"><span class="reply-name">{{ '回复 @：' +
														child.replyUserInfo.nick }}</span>{{ child.comment }}</span>
												</div>
												<div class="child-time">{{ child.createTime }} <span class="child-reply"
														@click="setReplyInfo('root', item)">回复</span></div>
											</div>

										</div>
									</div>
								</div>

								<div class="reply-input" v-if="replyInfo.rootId === item.id">
									<div class="danmu-content">
										<textarea rows="" cols="" class="input"
											:placeholder="'回复 @' + replyInfo.replyUserName + ':'"
											v-model="replyInfo.comment"></textarea>
										<div class="send-btn" @click="postComments('reply')">发表评论</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="danmu-content">
						<textarea rows="" cols="" class="input" placeholder="发一条友善的评论" v-model="comment"></textarea>
						<div class="send-btn" @click="postComments">发表评论</div>
					</div>
					<!-- <img class="comment-img" src="../../assets/image/video/comment.jpg" alt=""> -->
				</div>
				<div class="content-right">
					<div class="danmu-list-wrap">
						<div class="danmu-list-header">
							弹幕列表
						</div>
						<table class="danmu-table" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<th class="time">时间</th>
								<th class="content">弹幕内容</th>
								<th class="date">发送时间</th>
							</tr>
						</table>
						<div class="danmu-list-content">
							<table class="danmu-table" border="0" cellpadding="0" cellspacing="0">

								<tr v-for="(item, index) in danmuList" :key="index" v-if="danmuList.length > 0">
									<td class="time">{{ item.danmuTime }}</td>
									<td class="content">{{ item.content }}</td>
									<td class="date">{{ item.createTime }}</td>
								</tr>
								<!-- 如果没有弹幕数据，可以显示一条提示信息 -->
								<tr v-if="danmuList.length === 0">
									<td colspan="3" class="no-data">暂无弹幕</td>
								</tr>
							</table>
						</div>

					</div>
				</div>
			</div>

		</div>


	</div>
</template>
<script>
import Player from 'xgplayer';
import api from '../../fetch/api/home_page.js';
import likesApi from '../../fetch/api/likes.js';
import Bheader from '../../components/common/B_header.vue';
export default {
	name: "",
	components: {
		Bheader
	},
	computed:{
		token(){
			return this.$store.state.token;
		}
	},
	data() {
		return {
			videoId: 11,
			player: null,
			danmuId: 0,
			startTime: 1000,
			text: '',
			comment: '',
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
			danmuList: [],
			socketObj: null,
			videoInfo: {},
			likesInfo: {
				count: 0,
				like: false
			},
			collections: {
				count: 0,
				like: false,
			},
			coins: {
				count: 0,
				like: false
			},
			comments: [],
			replyInfo: {
				videoId: null,
				rootId: null,
				replyUserId: null,
				replyUserName: '',
				comment: ''
			}
		}
	},
	created() {
		this.initWebSocket();
		
		let id = this.$route.query.videoId;
		if (id) {
			this.videoId = parseInt(id);
		}
		this.getDanmus();
		this.getVideoDetail();
		this.getLikes();
		this.getCollections();
		this.getCoins();
		this.getComments();
		
		
	},
	mounted() {
		
	},
	beforeDestroy() {
		if (this.socketObj) {
			this.socketObj.close();
			this.socketObj = null;
		}
	},
	methods: {
		async getDanmus() {
			let res = await api.getDanmus({ videoId: this.videoId });
			console.log("弹幕数据");
			console.log(res.data)
			if (res && res.code === '0') {
				this.danmuList = res.data;
			}
		},
		
		setReplyInfo(type, data, child) {
			this.replyInfo.videoId = this.videoId;
			if (type === 'root') {
				this.replyInfo.rootId = data.id;
				this.replyInfo.replyUserId = data.userId;
				this.replyInfo.replyUserName = data.userInfo.nick;
			} else {
				this.replyInfo.rootId = data.id;
				this.replyInfo.replyUserId = child.userId;
				this.replyInfo.replyUserName = child.userInfo.nick;
			}
		},
		async postComments(type) {

			//判断是否登陆
			if(!this.token){
				this.$message({
					type: "error",
					message: "请先登陆"
				})
				return;
			}
			let params;
			if (type === 'reply') {
				params = this.replyInfo;
			} else {
				params = {
					videoId: this.videoId,
					comment: this.comment
				}
			}
			let res = await api.postComments(params);
			if (res && res.code === '0') {
				this.getComments();
				this.$message({
					type: 'success',
					message: '发表成功'
				})
				this.comment = ''
				this.replyInfo.comment = '';
				this.replyInfo.rootId = null;
				this.replyInfo.replyUserId = null;
				this.replyInfo.replyUserName = '';
			}

		},
		async getComments() {
			let res = await api.getComments({
				size: 10,
				no: 1,
				videoId: this.videoId
			});
			if (res && res.code === '0') {
				this.comments = res.data.list;
			}
		},
		async getCoins() {
			let res = await api.getCoins({
				videoId: this.videoId
			});
			if (res && res.code === '0') {
				this.coins = res.data;
			}
		},
		async postCoins() {
			//判断是否登陆
			if(!this.token){
				this.$message({
					type: "error",
					message: "请先登陆"
				})
				return;
			}
			let res = await api.postCoins({
				videoId: this.videoId,
				amount: 1
			});
			if (res && res.code === '0') {
				this.getCoins();
				this.$message({
					type: 'success',
					message: '投币成功'
				})
			} 
		},
		async getCollections() {
			let res = await api.getCollections({
				videoId: this.videoId
			});
			if (res && res.code === '0') {
				this.collections = res.data;
			}
		},
		async postCollections() {
			//判断是否登陆
			if(!this.token){
				this.$message({
					type: "error",
					message: "请先登陆"
				})
				return;
			}
			if (!this.collections.like) {
				let res = await api.postCollections({
					videoId: this.videoId,
					groupId: 1
				});
				if (res && res.code === '0') {
					this.getCollections();
					this.$message({
						type: 'success',
						message: '收藏成功'
					})
				}
			} else {
				let res = await api.deleteCollections({
					videoId: this.videoId
				});
				if (res && res.code === '0') {
					this.getCollections();
					this.$message({
						type: 'success',
						message: '已取消收藏'
					})
				}
			}

		},
		async getLikes() {
			let res = await api.getLikes({
				videoId: this.videoId
			});
			if (res && res.code === '0') {
				this.likesInfo = res.data;
			}
		},
		async postLikes() {
			//判断是否登陆
			if(!this.token){
				this.$message({
					type: "error",
					message: "请先登陆"
				})
				return;
			}

			if (!this.likesInfo.like) {
				let res = await likesApi.postLikes({
					videoId: this.videoId
				});
				if (res && res.code === '0') {
					this.getLikes();
					this.$message({
						type: 'success',
						message: '点赞成功'
					})
				}
			} else {
				let res = await api.deleteLikes({
					videoId: this.videoId
				});
				if (res && res.code === '0') {
					this.getLikes();
					this.$message({
						type: 'success',
						message: '已取消点赞'
					})
				}
			}

		},
		async getVideoDetail() {
			let res = await api.getVideoDetail({
				videoId: this.videoId
			});
			if (res && res.code === '0') {
				this.videoInfo = res.data;
				let videoUrl = `${process.env.VUE_APP_BASE_URL}/video-slices?url=${res.data.video.url}`;
				console.log("打印视频地址");
				console.log(videoUrl);
				this.initPlayer(videoUrl);
			}
		},
		initWebSocket() {
			let token = localStorage.getItem('token');
			let socketUrl = process.env.VUE_APP_BASE_URL;
			socketUrl = socketUrl.replace("https", "ws").replace("http", "ws");
			socketUrl = `${socketUrl}/imserver/${token}`;
			this.socketObj = new WebSocket(socketUrl);
			console.log(socketUrl)
			this.setSocketLinister();
		},
		setSocketLinister() {
			this.socketObj.onopen = this.websocketOpen;
			this.socketObj.onerror = this.websocketError;
			this.socketObj.onmessage = this.websocketMessage;
		},
		websocketOpen(res) {
			//链接成功
			console.log(res);
		},
		websocketError(err) {
			//链接错误
			console.log(err);
		},
		websocketMessage(message) {
			//接收消息
			console.log(message)
			if (message.data === '500') {
				console.log('网络服务异常');
			} else if (message.data === '0') {
				console.log('连接成功');
			} else {
				let data = JSON.parse(message.data);
				if (data && data.videoId == this.videoId) {
					this.sendDanmu(data.content);
				}
			}

		},
		initPlayer(videoUrl) {
			let danmuArr = this.initDanmu();
			
			this.player = new Player({
				id: 'vs',
				url: videoUrl,
				autoplay: true,
				volume: 0.3,
				danmu: {
					comments: danmuArr,
					area: {
						start: 0,
						end: 1
					}
				},
				height: 600,
				width: 800,
				whitelist: ['']
			});
		},
		
		initDanmu() {
			
			let danmuArr = [];
			if (this.danmuList.length === 0) {
        		return danmuArr;
    		}

			for (let i = 0; i < 300; i++) {
				this.danmuId++;
				this.startTime += (this.random(0, 1) * 500);
				let danmuObj = {
					duration: 15000,
					id: this.danmuId,
					start: this.startTime,
					txt: this.danmuList[i % this.danmuList.length].content,
					style: { //弹幕自定义样式
						color: '#ff9500',
						fontSize: '20px',
						border: 'solid 1px #ff9500',
						borderRadius: '50px',
						padding: '5px 11px',
						backgroundColor: 'rgba(255, 255, 255, 0.1)'
					}
				}
				danmuObj.duration = (this.random(5, 15) * 1000);
				danmuArr.push(danmuObj);
			}
			return danmuArr;
		},
		async socketSend() {
			if (!this.text) {
				this.$message({
					type: "error",
					message:"请输入弹幕内容"
				})
				return false;
			}
			let params = {
				videoId: this.videoId,
				content: this.text,
			}
			//增加弹幕当前时间
			if (this.player) {
				params.danmuTIme = this.player.currentTime;
			}
			this.socketObj.send(JSON.stringify(params));


			if(localStorage.getItem("token")){
				await this.wait(30);
				this.getDanmus();
			}
			 
		},
		wait(ms) {
        	return new Promise(resolve => setTimeout(resolve, ms));
    	},
		sendDanmu(text) {
			this.danmuId++;
			let start = this.player.currentTime;
			start = start.toFixed(3) * 1000;
			let random = this.random(5, 15);
			let duration = random * 1000;
			this.player.danmu.sendComment({ //发送弹幕
				duration: duration,
				id: this.danmuId,
				start: start,
				txt: text,
				style: {
					color: '#f00',
					fontSize: '20px',
					border: 'solid 1px #f00',
					borderRadius: '50px',
					padding: '5px 11px',
					backgroundColor: 'rgba(255, 255, 255, 0.1)'
				}
			});
			this.text = '';
		},
		random(min, max) {
			return Math.round((Math.random() * (max - min) + min) * 10) / 10;
		}
	}
}
</script>
<style lang="scss" scoped>
.danmu-content {
	margin-bottom: 20px;
	width: 800PX;
	height: 65px;
	display: flex;
	justify-content: space-between;
	margin-top: 20px;

	.input {
		width: 90%;
		height: 100%;
		resize: none;
		font-size: 12px;
		box-sizing: border-box;
		background-color: #f4f5f7;
		border: 1px solid #e5e9ef;
		overflow: auto;
		border-radius: 4px;
		color: #555;
		padding: 5px 10px;
		line-height: normal;
		outline: none;
	}

	.send-btn {
		width: 70px;
		height: 100%;
		background-color: #00a1d6;
		font-size: 14px;
		color: #fff;
		border-radius: 4px;
		padding: 12px 15px;
		text-align: center;
		vertical-align: center;
		cursor: pointer;
		// display: flex;
		// justify-content: center;
		// align-items: center;
	}


}

.video-detail-wrap {
	width: 100%;

	.content-left {
		.header-top {
			width: 100%;
			background: url(../../assets/image/header/header_bg.jpeg) no-repeat;
			background-size: cover;
			margin-bottom: 40px;
		}
	}

	.home-header {
		width: 100%;
		background: #fff;
		background-size: 100% 100%;
		padding: 10px 24px;
		display: flex;
		align-items: center;
		box-shadow: 0 2px 10px 0 rgba(0, 0, 0, .1);

		.menu-wrap {
			width: fit-content;
			height: 32px;
			display: flex;
			align-items: center;

		}

		.menu-right-wrap {
			margin-left: auto;
		}

		.menu-wrap,
		.menu-right-wrap {
			.menu-item {
				height: 100%;
				width: fit-content;
				font-size: 14px;
				color: #212121;
				margin-right: 12px;
				display: flex;
				align-items: center;
			}
		}
	}



	.video-content {
		max-width: 1984px;
		min-width: 988px;
		margin: 0 auto;
		// display: -ms-flexbox;
		padding: 0 68px;
		padding-top: 27px;
		width: fit-content;
		display: flex;

		.content-left {
			width: 800PX;



			.title {
				font-size: 18px;
				font-weight: 500;
				color: #212121;
				margin-bottom: 8px;
			}

			.play-info {
				font-size: 12px;
				color: #999;
				margin-bottom: 10px;
			}

			.vs {
				width: 1200px;

			}

			.forward-wrap {
				width: 800PX;
				display: flex;
				margin-top: 20px;
				padding-bottom: 10px;
				border-bottom: 1px solid #e5e9f0;

				.icon-item {
					display: flex;
					align-items: center;
					font-size: 14px;
					color: #505050;
					margin-right: 30px;

					.img {
						width: 30px;
						height: 30px;
						display: block;
						margin-right: 10px;
						cursor: pointer;
					}

					.active {
						// background-image: url($img), linear-gradient(#f00, #f00);

						// -webkit-filter: drop-shadow(8px 8px 10px red); /* Chrome, Safari, Opera */
						//     filter: drop-shadow(8px 8px 10px red);
					}
				}
			}



			.comment-img {
				width: 100%;
				display: block;
				margin-top: 60px;
			}

			.comment-wrap {
				width: 100%;

				.comment-list {
					width: 100%;

					.comment-item {
						padding: 20px 0;
						border-top: 1PX solid #eeeeee;
						border-bottom: 1PX solid #eeeeee;

						.comment-in {
							display: flex;

							.avatar {
								width: 60px;
								height: 60px;
								display: block;
								border-radius: 50%;
							}

							.comment-content {
								margin: 10px 0;
							}

							.comment-right {
								margin-left: 20px;

								.name {
									font-size: 14px;
									color: #74797A;
								}

								.time {
									font-size: 12px;
									color: #666666;

									.reply {
										margin-left: 10px;
										cursor: pointer;
										border-radius: 5px;
										padding: 2px 3px;

										&:hover {
											color: #fff;
											background-color: #666666;
										}
									}
								}

								.clild-comments {
									margin-top: 20px;
									display: flex;
									padding: 10px 0;

									.child-avatar {
										width: 30px;
										height: 30px;
										display: block;
										border-radius: 50%;
									}

									.child-user-info {
										margin-left: 10px;

										.child-comment-info {
											padding-top: 5px;
											display: flex;
											align-items: center;

											.child-name {
												font-size: 14px;
												color: #74797A;
											}

											.child-comment {
												margin-left: 10px;

												.reply-name {
													margin-right: 5px;
												}
											}
										}

										.child-time {
											font-size: 12px;
											color: #666666;

											.child-reply {
												margin-left: 10px;
												cursor: pointer;
												border-radius: 5px;
												padding: 2px 3px;

												&:hover {
													color: #fff;
													background-color: #666666;
												}
											}
										}

									}

								}
							}
						}

					}
				}
			}

		}

		.content-right {
			width: 350px;
			padding-top: 60px;
			margin-left: 40px;

			.danmu-list-wrap {
				width: 100%;

				.danmu-list-header {
					width: 100%;
					height: 46px;
					background-color: #f4f4f4;
					display: flex;
					color: #222;
					font-size: 16px;
					border-radius: 2px;
					padding: 0 10px 0 16px;
					align-items: center;
				}

				.danmu-list-content {
					width: 100%;
					height: 500PX;
					overflow-y: scroll;

					.danmu-table {
						width: 100%;
						padding-left: 40px;

						tr {
							th {
								font-size: 12px;
								color: #6d757a;
								font-weight: 100;
								text-align: left;
								padding: 10px 0;
							}

							td {
								font-size: 12px;
								color: #222;
							}

							.time {
								width: 20%;
							}

							.content {
								// width: 60%;
							}

							.date {
								width: 40%;
							}
						}
					}
				}

				.danmu-table {
					width: 100%;
					padding-left: 40px;

					tr {
						th {
							font-size: 12px;
							color: #6d757a;
							font-weight: 100;
							text-align: left;
							padding: 10px 0;
						}

						td {
							font-size: 12px;
							color: #222;
						}

						.time {
							width: 20%;
						}

						.content {
							// width: 60%;
						}

						.date {
							width: 30%;
						}
					}
				}
			}

		}


	}









}
</style>
