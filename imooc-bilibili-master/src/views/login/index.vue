<template>
	<div class="register-wrap">
		<div class="register-title-wrap">
			<span>登录</span>
		</div>
		<div class="register-content">
			<el-input class="input-item" type="text" placeholder="手机号/邮箱" v-model="params.phone"></el-input>
			<el-input class="input-item" type="password" placeholder="密码" v-model="params.password"></el-input>
			<el-button class="button-register" @click="login" type="primary">登录</el-button>
		</div>
	</div>
</template>
<script>
	import api from '../../fetch/api/register.js';
	import auth from '../../fetch/api/auth.js';
	export default {
		name: "",
		components: {},
		data() {
			return {
				params: {
					phone: '',
					password: '',
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
			async login() {
				let param = {};
				let _params = JSON.parse(JSON.stringify(this.params));
				if (_params.phone.indexOf('@') > -1) {
					param.email = _params.phone;
				} else {
					param.phone = _params.phone;
				}
				console.log(this.key);
				let psd = this.$getRsaCode(_params.password, this.key);
				if (!psd) {
					this.$message({
						type:'error',
						message: '公钥获取失败，请重试'
					})
					this.getKey();
					return false;
				}
				param.password = psd;
				let res;
				if (this.$store.state.chapter === 'login') {
					res = await api.login(param);
					if (res && res.code === '0') {
						this.$message({
							showClose: true,
							message: '登录成功',
							type: 'success'
						});
						// localStorage.setItem('token', res.data);
						this.$store.commit('setToken',res.data);
						localStorage.setItem('token',res.data);
						this.getAuthList();
						this.$router.go(-1);
					}
				} else if (this.$store.state.chapter === 'login-v2') {
					res = await api.loginV2(param);
					if (res && res.code === '0') {
						this.$message({
							showClose: true,
							message: '登录成功',
							type: 'success'
						});
						localStorage.setItem('token', res.data.accessToken);
						localStorage.setItem('refreshToken', res.data.refreshToken);
						this.getAuthList();
						this.$router.go(-1);
					}
				}
				
			},
			async getAuthList() {
				let res = await auth.getAuthList();
				if (res && res.code === '0') {
					this.$store.commit('setAuthList', res.data);
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
