<template>
	<div class="personal-wrap">
		<div class="header-top">
			<Bheader></Bheader>
		</div>
		<Title title="个人中心"></Title>
		<div class="personal-content">
			<div class="menu-left">
				<div class="left-title">个人中心</div>
				<div class="menu-list">
					<div class="menu-item" 
						:class="activeIndex === index ? 'active' : '' " 
						v-for="(item,index) in menuList" 
						:key="item.code" 
						@click="tab(item,index)">
						{{ item.title }}
					</div>
				</div>
			</div>
			<div class="content-right">
				<div class="right-title">
					{{ menuList[activeIndex].title }}
				</div>
				<div class="info-content">
					<component :is="activeView"></component>
					<!-- <Profile></Profile> -->
				</div>
			</div>
		</div>
	</div>
</template>
<script>
	import Bheader from '../../components/common/B_header.vue';
	import Title from '../../components/common/Title.vue';
	import Profile from '../../components/personal/User_profile.vue';
	import Account from '../../components/personal/Account.vue';
	export default {
		name: "",
		components: {
			Title,
			Profile,
			Account,
			Bheader
		},
		computed:{
			token() {
				return this.$store.state.token
			}
		},
		watch:{
			token(newToken){
				if(newToken === null){
					this.$router.push('/')
				}
			}
		},
		data() {
			return {
				activeIndex: 0,
				menuList: [
					{
						title: '我的信息',
						code: 'info',
						component: 'Profile'
					},
					// {
					// 	title: '我的头像',
					// 	code: 'avatar',
					// 	component: 'Avatar'
					// },
					{
						title: '账号安全',
						code: 'account',
						component: 'Account'
					},
				],
				activeView: 'Profile'
				
			}
		},
		methods: {
			tab(item,index) {
				this.activeIndex = index;
				this.activeView = item.component;
			}
		},
	}
</script>
<style lang="scss" scoped>
	.personal-wrap {
		width: 100%;
		.header-top {
			width: 100%;
			background: url(../../assets/image/header/header_bg.jpeg) no-repeat;
			background-size: cover;
			margin-bottom: 40px;
		}
		.personal-content {
			width: 980px;
			margin: 0 auto;
			display: flex;
			text-align: center;
			line-height: 50px;
			.menu-left {
				width: 150px;
				font-size: 14px;
				background-color: #FAFAFA;
				border: 1px solid #dddddd;
				.left-title {
					width: 100%;
					height: 50px;
					font-size: 18px;
					border-bottom: 1px solid #dddddd;
				}
				.menu-list {
					width: 100%;
					.menu-item {
						width: 100%;
						height: 100%;
						cursor: pointer;
						color: #333;
						&:hover {
							background-color: #dddddd;
							color: #333;
						}
					}
					.active {
						background-color: #00a1d7 !important;
						color: #fff !important;
					}
				}
			}
			.content-right {
				width: calc(100% - 150px);
				border: 1px solid #dddddd;
				border-left: 0;
				text-align: left;
				.right-title {
					width: 100%;
					background-color: #fafafa;
					border-bottom: 1px solid #dddddd;
					height: 50px;
					padding-left: 20px;
					font-size: 14px;
				}
				
				.info-content {
					width: 100%;
					padding: 40px;
					/deep/ .el-form-item__label {
						width: 100px;
						margin-right: 20px;
					}
					/deep/ .el-input {
						width: 400px;
					}
				}
			}
			
		}
	}
</style>