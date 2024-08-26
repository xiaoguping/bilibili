<template>
	<div class="user-profile-wrap">
		<el-form :model="formData">
			<el-form-item label="手机号:">
				<el-input v-model="formData.phone"></el-input>
			</el-form-item>
			<el-form-item label="邮箱:">
				<el-input v-model="formData.email"></el-input>
			</el-form-item>
			<el-form-item label="新密码:">
				<el-input type="password" v-model="password_new"></el-input>
			</el-form-item>
			<el-form-item label="确认密码:">
				<el-input type="password" v-model="password_confirm"></el-input>
			</el-form-item>
		</el-form>
		<el-button type="primary" @click="putAccount" class="button-submit">
			保存
		</el-button>
	</div>
</template>
<script>
	import api from '../../fetch/api/register.js';
	export default {
		name: "",
		components: {},
		data() {
			return {
				formData: {
					email: '',
					phone: '',
				},
				password_confirm: '',
				password_new: '',
				key: ''
			}
		},
		created() {
			this.getUserInfo();
			this.getKey();
		},
		methods: {
			async getKey() {
				let res = await api.getKey();
				if (res && res.code === '0') {
					this.key = res.data;
				}
			},
			async getUserInfo() {
				let res = await api.userInfo();
				if (res && res.code === '0') {
					this.formData = res.data;
				}
			},
			async putAccount () {

				let _formData = JSON.parse(JSON.stringify(this.formData));
				//检查密码和确认密码是否匹配
				if (this.password_new !== this.password_confirm) {
					this.$message({
						type: 'error',
						message: '密码和确认密码不匹配'
					});
					return; // 停止提交
				}
				if (this.password_new) {
					_formData.password = this.$getRsaCode(this.password_new, this.key);
				}else{
					_formData.password = ''
				}
				
				let res = await api.putAccount(_formData);
				if (res && res.code === '0') {
					this.$message({
						type: 'success',
						message: '保存成功'
					})
					if (this.password) {
						localStorage.removeItem('token');
						this.$router.replace('/dist/login');
					} else {
						this.getUserInfo();
					}
				}
			}
		},
	}
</script>
<style lang="scss" scoped>
	.user-profile-wrap {
		width: 100%;
		padding: 40px;
		/deep/ .el-form-item__label {
			width: 100px;
			margin-right: 20px;
		}

		/deep/ .el-input {
			width: 400px;
		}

		/deep/ .el-textarea {
			width: 400px;
		}

		.gender-wrap {
			width: 400px;
			display: flex;

			.gender-option {
				cursor: pointer;
				height: 26px;
				line-height: 26px;
				width: fit-content;
				padding: 0 10px;
				text-align: center;
				background-color: #f4f4f4;
				border-radius: 5px;
				border: 1px solid #bfcbd9;
				margin-right: 20px;
			}

			.active {
				border: 1px solid #20a0ff;
				background-color: #20a0ff;
				color: #fff;
			}
		}
		.button-submit {
			display: block;
			margin: 0 auto;
		}
	}
</style>
