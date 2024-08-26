<template>
	<div class="register-wrap">
		<div class="register-title-wrap">
			<span>注册</span>
		</div>
		<div class="register-content">
			<el-input class="input-item" type="tel" placeholder="填写登陆手机号" v-model="params.phone"></el-input>
			<el-input class="input-item" type="password" placeholder="密码" v-model="params.password"></el-input>
			<el-input class="input-item" type="text" placeholder="昵称" v-model="params.nickName"></el-input>
			<el-button class="button-register" @click="register" type="primary">注册</el-button>
		</div>
	</div>
</template>
<script>
	import api from '../../fetch/api/register.js';
	export default {
		name: "",
		components: {},
		data() {
			return {
				params: {
					nickName: '',
					password: '',
					phone: '',
				},
				key: ''
			}
		},
		created() {
			this.getKey();
		},
		methods: {
			async getKey() {
				let res = await api.getKey();
				if (res && res.code === '0') {
					this.key = res.data;
				}
			},
			async register() {
				let _params = JSON.parse(JSON.stringify(this.params));
				console.log(this.key);
				let psd = this.$getRsaCode(_params.password, this.key);
				_params.password = psd;
				let res = await api.register(_params);
				if (res && res.code === '0') {
					this.$message({
						showClose: true,
						message: '注册成功',
						type: 'success'
					});
					//注册成功后，请求登陆
					let resToken = await api.login(_params)
					if(resToken && resToken.code === '0'){
						localStorage.setItem('token',resToken.data);
						this.$store.commit('setToken',resToken.data);
					}
					this.$router.go(-1);
				}
			}
		},
	}
</script>
<style lang="scss" scoped>
	.register-wrap {
		width: 980px;
		margin: 0 auto;
		padding-top: 50px;

		.register-title-wrap {
			width: 100%;
			height: 28px;
			border-bottom: 1px solid #ddd;
			margin-bottom: 28px;
			text-align: center;

			span {
				line-height: 56px;
				margin: 0 auto;
				padding: 0 20px;
				font-size: 40px;
				background: #fff;
				text-align: center;
			}
		}

		.register-content {
			width: 400px;
			margin: 0 auto;
			padding-top: 60px;

			.input-item {
				margin-bottom: 40px;
				background-color: #fff;
			}

			.button-register {
				width: 100%;
				background-color: #00a1d7;
			}
		}
	}
</style>
