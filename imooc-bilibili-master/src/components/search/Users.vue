<template>
	<div class="user-query-list-wrap">
		<div class="user-list-wrap" v-if="list.length > 0">
			<div class="user-item" v-for="(item,index) in list" :key="index">
				<img class="avatar" src="../../assets/image/home/avatar_users.jpeg" alt="">
				<div class="infos">
					<div class="infos-top">
						<div class="nick">{{ item.nick }}</div>
						<div class="follow" :class="[item.followed ? 'disable' : '']" @click="follow(item.userId)">{{ item.followed ? '已关注' : '关注'}}</div>
					</div>
					<div class="infos-sign">{{ item.sign }}</div>
				</div>
			</div>
		</div>
		<div v-else>
			<div class="no-data">暂无数据</div>
		</div>
	</div>
</template>
<script>
	import api from '../../fetch/api/users.js';
	export default {
		name: "query_list_users",
		components: {
		},
		props: {
			list: {
				type: Array,
				default: () => {
					return [];
				}
			},
		},
		data() {
			return {
				
			}
		},
		methods: {
			async follow(id) {
				let params = {
					followingId: id
				}
				let res = await api.follow(params);
				if (res && res.code === '0') {
					this.$message({
						type: 'success',
						message: '关注成功'
					})
					this.$emit('success','users');
				}
			}
		},
	}
</script>
<style lang="scss" scoped>
	.user-query-list-wrap {
		width: 100%;
		
		.user-list-wrap {
			width: 100%;
			.user-item {
				width: 100%;
				display: flex;
				padding: 20px 0;
				align-items: center;
				border-bottom: 1px solid #e5e9ef;
				.avatar {
					width: 90px;
					height: 90px;
					border-radius: 50%;
				}
				.infos {
					width: calc(100% - 90px);
					padding-left: 10px;
					height: 90px;
					display: flex;
					flex-direction: column;
					justify-content: space-between;
					padding: 10px 0;
					padding-left: 20px;
					.infos-top {
						display: flex;
						align-items: center;
						.nick {
							font-weight: 500;
						}
						.follow {
							width: 64px;
							height: 24px;
							border-radius: 5px;
							background-color: #00a1d6;
							color: #fff;
							text-align: center;
							line-height: 24px;
							font-size: 12px;
							margin-left: 20px;
							cursor: pointer;
						}
						
						.disable {
							background-color: #e5e9ef;
							color: #6d757a;
						}
					}
					
					.infos-sign {
						font-size: 14px;
						color: #6d757a;
					}
				}
			}
		}
	}
</style>