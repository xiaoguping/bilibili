<template>
	<div class="module-wrap">
		<div class="module-list-wrap">
			<Title class="catalog-title" title="章节目录"></Title>
			<div class="chapter-list" v-for="(item,index) in chapterList" :key="index">
				<div class="chapter-title">{{ item.title }}</div>
				<div class="module-list" v-for="moduleItem in item.modules" :key="moduleItem.module">
					<div class="module-title" @click="setModule(moduleItem)">{{ moduleItem.title }}</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
	import Title from '../../components/common/Title.vue';
	export default {
		name: "",
		components: {
			Title
		},
		data() {
			return {
				chapterList: [
					{
						chapter: 'three',
						title: '第3章 从用户功能体验后端经典开发模式',
						modules: [
							{
								module: 'login',
								title: '实现用户登录与注册，基于JWT的用户token验证',
								router: '/home-page'
							},
							{
								module: 'follow',
								title: '用户关注与粉丝',
								router: '/home-page'
							},
							{
								module: 'moments',
								title: '动态提醒实现方式',
								router: '/home-page'
							},
							{
								module: 'auth',
								title: '用户权限控制',
								router: '/home-page'
							},
							{
								module: 'login-v2',
								title: '用户登录信息验证与刷新',
								router: '/home-page'
							},
						]
					},
					{
						chapter: 'four',
						title: '第4章 打造高性能的视频与弹幕系统',
						modules: [
							{
								module: 'page-list',
								title: '首页分页、详情、点赞、投币、收藏、评论',
								router: '/home-page'
							},
						]
					},
				]
			}
		},
		methods: {
			setModule(item) {
				this.$router.push(item.router);
				this.$store.commit('setChapter',item.module);
			}
		},
	}
</script>
<style lang="scss" scoped>
	.module-wrap {
		width: 100%;
		padding-top: 60px;
		.module-list-wrap {
			width: 980px;
			margin: 0 auto;
			.chapter-list {
				width: 100%;
				margin-top: 40px;
				.chapter-title {
					font-size: 18px;
					font-weight: 500;
					margin-bottom: 20px;
				}
				.module-list {
					width: 100%;
					padding-left: 20px;
					.module-title {
						font-size: 16px;
						font-weight: 400;
						margin-bottom: 15px;
						cursor: pointer;
						&:hover {
							color: #00a1d7;
						}
					}
				}
			}
		}
	}
</style>
