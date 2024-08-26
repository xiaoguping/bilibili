<template>
	<div class="user-profile-wrap">
		<el-form :model="formData">
			<el-form-item label="昵称:">
				<el-input v-model="formData.nick"></el-input>
			</el-form-item>
			<el-form-item label="性别:">
				<div class="gender-wrap">
					<div class="gender-option" v-for="item in genderList" :key="item.vallue"
						:class="[formData.gender === item.value ? 'active' : '']" @click="formData.gender = item.value">
						{{ item.name }}
					</div>
				</div>
			</el-form-item>
			<el-form-item label="出生日期:">
				<el-date-picker v-model="formData.birth" value-format="yyyy-MM-dd" type="date" placeholder="选择日期">
				</el-date-picker>
			</el-form-item>
			<el-form-item label="我的签名:">
				<el-input type="textarea" placeholder="设置您的签名- ( ゜- ゜)つロ" v-model="formData.sign"></el-input>
			</el-form-item>
		</el-form>
		<div class="button-submit">
			<el-button type="primary" @click="putProfile">
				保存
			</el-button>
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
				formData: {
					nick: '',
					email: '',
					phone: '',
					gender: '0',
					sign: '',
					birth: ''
				},
				genderList: [{
						name: '男',
						value: '0'
					},
					{
						name: '女',
						value: '1'
					},
					{
						name: '保密',
						value: '2'
					},
				]
			}
		},
		created() {
			this.getUserInfo();
		},
		methods: {
			async getUserInfo() {
				let res = await api.userInfo();
				if (res && res.code === '0') {
					res.data.userInfo.email = res.data.email;
					res.data.userInfo.phone = res.data.phone;
					this.formData = res.data.userInfo;
				}
			},
			async putProfile () {
				let res = await api.putProfile(this.formData);
				if (res && res.code === '0') {
					this.$message({
						type: 'success',
						message: '保存成功'
					})
					this.getUserInfo();
				}
			}
		},
	}
</script>
<style lang="scss" scoped>
	.user-profile-wrap {
		width: 100%;
		padding: 40px;
		// text-align: center;
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
			width: 100%;
			text-align: center;
		}
	}
</style>
