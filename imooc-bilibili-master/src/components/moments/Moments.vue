<template>
	<div class="moments-wrap">
		<div class="moments-list">
			<div class="moments-item" v-for="item in moments" :key="item.id">
				<span>contentId: {{item.contentId}}</span><span>userId: {{item.userId}}</span><span>type: {{item.type}}</span>
			</div>
		</div>
	</div>
</template>
<script>
	import api from '../../fetch/api/users.js';
	export default {
		name: "",
		components: {
		},
		data() {
			return {
				moments:[]
			}
		},
		created() {
			this.getUserMoments();
		},
		methods: {
			async getUserMoments () {
				let res = await api.getUserMoments();
				if (res && res.code === '0') {
					this.moments = res.data;
				}
			},
		},
	}
</script>
<style lang="scss" scoped>
	.moments-wrap {
		width: 300px;
		padding: 20px;
		box-shadow: 0 0 10px 5px rgba($color: #000000, $alpha: .1);
		background-color: #fff;
		border-radius: 10px;
		.moments-list {
			width: 100%;
			.moments-item {
				width: 100%;
				display: flex;
				justify-content: space-between;
				padding: 10px 0;
				color: #333;
				border-bottom: 1px solid #f4f5f7;
			}
		}
	}
</style>