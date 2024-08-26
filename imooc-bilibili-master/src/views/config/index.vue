<template>
	<div class="config-wrap">
		<el-input class="input" placeholder="配置接口地址" v-model="baseApi"></el-input>
		<el-button type="primary" @click="saveApi">保存</el-button>
	</div>
</template>
<script>
export default {
	name: "",
	components: {
	},
	data() {
		return {
			baseApi: 'http://localhost:8081'
		}
	},
	created() {
		let baseApi = localStorage.getItem('baseApi');
		if (baseApi) {
			this.baseApi = baseApi;
		}
	},
	methods: {
		saveApi() {
			if (!this.baseApi) {
				this.$message({
					type: 'error',
					message: '还未配置接口地址'
				})
				return false;
			}
			localStorage.setItem('baseApi', this.baseApi);
			this.$router.push('/chapter')

		}
	},
}
</script>
<style lang="scss" scoped>
.config-wrap {
	width: 100%;
	padding: 50px;
	text-align: center;

	.input {
		width: 400px;
	}
}
</style>