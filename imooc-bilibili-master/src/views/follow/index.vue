<template>
	<div class="follow-wrap">
		<div class="header-top">
			<Bheader :showSearch="false"></Bheader>
		</div>
		<Title title="我的关注"></Title>
		<div class="img-top"></div>
		<div class="follow-content">
			<div class="group-wrap">
				<div class="group-title">
					<span>我的关注</span>
					<i class="el-icon-plus" @click="showPutGroup = true"></i>
				</div>
				<div class="follow-group-list">
					<div class="follow-group-item" :class="[!showFans && activeIndex === index ? 'active' : '']" v-for="(item, index) in followGroupList" :key="index" @click="tab(item,index)">
						<div class="title">{{ item.name }}</div>
						<div>{{ item.followingUserInfoList.length }}</div>
					</div>
				</div>
				<div class="group-title" >我的粉丝</div>
				<div class="follow-group-list">
					<div class="follow-group-item" :class="[showFans ? 'active' : '']" @click="tabFans">
						<div>我的粉丝</div>
						<div>{{ fans.length }}</div>
					</div>
				</div>
			</div>
			<div class="content-right" v-if="!showFans">
				<div class="follow-content-title" v-if="followGroupList[activeIndex]">
					<div class="title">{{ followGroupList[activeIndex].name }}</div>
				</div>
				<div class="follow-user-list" v-if="followGroupList[activeIndex]">
					<div class="follow-user-item" v-for="(item, index) in followGroupList[activeIndex].followingUserInfoList" :key="index">
						<img src="../../assets/image/home/avatar_users.jpeg" alt="" class="avatar">
						<div class="infos">
							<div class="nick">{{ item.nick }}</div>
							<div class="sign">{{ item.sign ? item.sign : '这个人没有填简介啊~~~' }}</div>
						</div>
						<div class="follow-button-wrap">
							<div class="follow-button">
								已关注
							</div>
							<div class="follow-button" @click="setFollowGroup(item)">
								设置分组
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="content-right" v-else>
				<div class="follow-content-title">
					<div class="title">我的粉丝</div>
				</div>
				<div class="follow-user-list" v-if="fans.length > 0">
					<div class="follow-user-item" v-for="(item, index) in fans" :key="index">
						<img src="../../assets/image/home/avatar_users.jpeg" alt="" class="avatar">
						<div class="infos">
							<div class="nick">{{ item.userInfo.nick }}</div>
							<div class="sign">{{ item.userInfo.sign ? item.sign : '这个人没有填简介啊~~~' }}</div>
						</div>
						<div class="follow-button-wrap">
							<div class="follow-button" :class="[item.userInfo.followed ? '' : 'active' ]" @click="follow(item)">
								{{ item.userInfo.followed ? '已互粉' : '关注' }}
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<el-dialog title="新建分组" :visible.sync="showPutGroup" width="20%">
			<el-input v-model="groupParams.name" placeholder="分组名称"></el-input>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="showPutGroup = false">取 消</el-button>
		    <el-button type="primary" @click="putGroup(false)">新 建</el-button>
		  </div>
		</el-dialog>
		<el-dialog title="设置分组" :visible.sync="showSetGroup" width="20%">
			<div class="dialog-title">请为 <span>{{ currentUserItem.nick }}</span> 选择分组</div>
			<div class="set-group-list">
				<el-radio-group class="group-radio-wrap" v-model="setGroupId" v-if="followGroupList.length>0">
					<el-radio 
						class="group-radio-item" 
						v-for="(item,index) in followGroupList" 
						:key="item.id" :label="item.id" 
						:disabled="followGroupList[activeIndex] && followGroupList[activeIndex].id === item.id">
						{{ item.name }}
					</el-radio>
				  </el-radio-group>
			</div>
			<div class="new-group-wrap">
				<el-input v-model="groupParams.name" placeholder="分组名称"></el-input>
				<el-button type="text" @click="putGroup()" :disabled="!groupParams.name">新建分组</el-button>
			</div>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="showSetGroup = false">取 消</el-button>
		    <el-button type="primary" @click="setGroup">保 存</el-button>
		  </div>
		</el-dialog>
		
	</div>
</template>
<script>
	import Title from '../../components/common/Title.vue';
	import api from '../../fetch/api/users.js';
	import Bheader from '../../components/common/B_header.vue';
	export default {
		name: "",
		components: {
			Title,
			Bheader
		},
		created() {
			this.getFollows();
			// this.getFollowGroups();
			this.getFans();
		},
		computed: {
			concatGroups () {
				let groups = this.followGroupList.concat(this.groups);
				let _groups = groups.filter((item) => {
					return item.id;
				})
				return _groups
			}
		},
		data() {
			return {
				activeIndex: 0,
				followGroupList: [],
				followList: [],
				showPutGroup: false,
				showSetGroup: false,
				groupParams: {
					name: ''
				},
				currentUserItem: {},
				groups: [],
				setGroupId: '',
				fans:[],
				showFans: false
			}
		},
		methods: {
			tabFans() {
				this.showFans = true;
				this.getFans();
			},
			async getFans () {
				let res = await api.getUserFans();
				if (res && res.code === '0') {
					this.fans = res.data;
				}
			},
			async getFollowGroups () {
				let res = await api.getFollowGroup();
				if (res && res.code === '0') {
					this.groups = res.data;
				}
			},
			tab(item,index) {
				this.showFans = false;
				this.activeIndex = index;
				this.getFollows();
			},
			async getFollows () {
				let res = await api.getFollows();
				if (res && res.code === '0') {
					this.followGroupList = res.data;
				}
			},
			async putGroup (bool) {
				if (!this.groupParams.name) {
					this.$message({
						type: 'error',
						message: '请填写分组名'
					})
					return false;
				}
				let res = await api.putFollowGroup(this.groupParams);
				if (res && res.code === '0') {
					this.groupParams.name = '';
					if (!bool) {
						this.showPutGroup = false;
					}
					this.getFollows();
					this.getFollowGroups();
				}
			},
			setFollowGroup (item) {
				this.currentUserItem = item;
				this.showSetGroup = true;
			},
			getGroupList (groupArr) {
				let followGroupArr = this.followGroupList.filter((item) => {
					return item.id;
				})
				let groups = followGroupArr.concat(groupArr);
				return groups
			},
			async setGroup () {
				let params = {
					groupId: this.setGroupId,
					followingId: this.currentUserItem.userId
				}
				let res = await api.follow(params);
				if (res && res.code === '0') {
					this.showSetGroup = false;
					this.getFollows();
				}
			},
			async follow (item) {
				let params = {
					followingId: item.userInfo.userId
				}
				if (!item.userInfo.followed) {
					let res = await api.follow(params);
					if (res && res.code === '0') {
						this.$message({
							type: 'success',
							message: '关注成功'
						})
						this.getFans();
					}
				}
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
			width: 1280px;
			height: 200px;
			display: block;
			margin: 0 auto;
			background: url(../../assets/image/header/header_bg.jpeg) no-repeat center center;
			background-size: cover;
		}
		.follow-content {
			width: 1280px;
			margin: 0 auto;
			border-radius: 5px;
			background-color: #fff;
			border: 1px solid #eee;
			margin-top: 20px;
			height: 600px;
			display: flex;
			
			.group-wrap {
				width: 210px;
				font-size: 14px;
				.group-title {
					padding: 20px 0 0 20px;
					color: #99a2aa;
					display: flex;
					justify-content: space-between;
					padding-right: 20px;
					.el-icon-plus {
						cursor: pointer;
					}
				}
				.follow-group-list {
					width: 100%;
					padding: 10px 0 20px;
					border-bottom: 1px solid #eee;
					.follow-group-item {
						height: 44px;
						width: 100%;
						padding-left: 40px;
						padding-right: 20px;
						color: #333;
						display: flex;
						align-items: center;
						justify-content: space-between;
						cursor: pointer;
						&:hover {
							background-color: #f4f5f7;
						}
						&:hover .title {
							color: #00A2D6;
						}
					}
					
					.active {
						background-color: #00A2D6 !important;
						color: #fff !important;
						.title {
							color: #fff !important;
						}
					}
				}
			}
			
			.content-right {
				width: calc(100% - 210px);
				border-left: 1px solid #eee;
				padding: 0 20px;
				.follow-content-title {
					width: 100%;
					padding: 20px 0;
					font-size: 18px;
					color: #333;
					border-bottom: 1px solid #eee;
					
				}
				.follow-user-list {
					width: 100%;
					.follow-user-item {
						width: 100%;
						padding: 20px 0;
						display: flex;
						border-bottom: 1px solid #eee;
						.avatar {
							width: 60px;
							height: 60px;
							border-radius: 50%;
							display: block;
						}
						
						.infos {
							min-width: 200px;
							max-width: 500px;
							display: flex;
							flex-direction: column;
							justify-content: space-between;
							padding: 8px 0;
							margin-left: 20px;
							.nick {
								color: rgb(251, 114, 153);
								font-size: 16px;
								font-weight: 500;
							}
							.sign {
								color: #6d757a;
								font-size: 12px;
							}
						}
						
						.follow-button-wrap {
							margin-left: auto;
							display: flex;
							align-items: center;
							padding-right: 20px;
							
							.follow-button {
								width: 64px;
								height: 22px;
								border-radius: 5px;
								font-size: 14px;
								background-color: #e5e9ef;
								color: #6d757a;
								text-align: center;
								line-height: 22px;
								cursor: pointer;
								margin-left: 10px;
							}
							.active {
								color: #fff;
								background-color: #00A2D6;
							}
						}
					}
					
				}
				
				
				
			}
			
			
		}
	}
</style>