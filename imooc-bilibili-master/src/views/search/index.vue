<template>
	<div class="search-wrap">
		<div class="header-top">
			<Bheader :showSearch="false"></Bheader>
		</div>
		
		<div class="search-category">
			<div class="query-wrap">
				<el-input v-model="queryParams.keyword"></el-input>
				<el-button type="primary" @click="search">搜索</el-button>
			</div>
			<el-tabs v-model="activeName">
				<el-tab-pane v-for="item in category" :label="item.title" :name="item.code" :key="item.code">
					<component :is="item.component" :list="listData" @success="success"></component>
				</el-tab-pane>
			</el-tabs>
		</div>
	</div>
</template>
<script>
	import Bheader from '../../components/common/B_header.vue';
	import Users from '../../components/search/Users.vue';
	import api from '../../fetch/api/users.js';
	export default {
		name: "",
		components: {
			Bheader,
			Users
		},
		created() {
			let queryContent = this.$route.query.content;
			this.queryParams.keyword = queryContent;
			this.search();
		},
		data() {
			return {
				activeName: 'users',
				category: [
					{
						code: 'users',
						title: '用户',
						component: 'Users'
					},
					{
						code: 'synthesize',
						title: '综合',
						component: ''
					},
				],
				queryParams: {
					no: 1,
					size: 10,
					keyword: ''
				},
				listData: []
			}
		},
		methods: {
			async search() {
				let res = await api.queryUsers(this.queryParams);
				if (res && res.code === '0') {
					console.log(res)
					this.listData = res.data;
				}
			},
			success (category) {
				switch (category){
					case 'users':
					this.search();
						break;
					default:
						break;
				}
			}
		},
	}
</script>
<style lang="scss" scoped>
	.search-wrap {
		width: 100%;
		.header-top {
			width: 100%;
			background: url(../../assets/image/header/header_bg.jpeg) no-repeat;
			background-size: cover;
			margin-bottom: 40px;
		}

		.search-category {
			width: 980px;
			margin: 0 auto;
			
			
			.query-wrap {
				width: 100%;
				display: flex;
				justify-content: center;
				/deep/ .el-input {
					width: 400px;
				}
				/deep/ .el-button {
					margin-left: 10px;
				}
			}
		}
	}
</style>
