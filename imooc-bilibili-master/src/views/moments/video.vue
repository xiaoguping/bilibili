<template>
	<div class="post-video-wrap">
		<div class="header-top">
			<Bheader :showSearch="false"></Bheader>
		</div>
		<Title title="视频投稿"></Title>
		<div class="post-content">
			
		</div>
	</div>
</template>
<script>
	import Bheader from '../../components/common/B_header.vue';
	import Title from '../../components/common/Title.vue';
	export default {
		name: "",
		components: {
			Bheader,
			Title
		},
		data() {
			return {
				
			}
		}
	}
</script>
<style lang="scss" scoped>
	.post-video-wrap {
		width: 100%;
		.header-top {
			width: 100%;
			background: url(../../assets/image/header/header_bg.jpeg) no-repeat;
			background-size: cover;
			margin-bottom: 40px;
		}
		.post-content {
			width: 1440px;
			margin: 0 auto;
			
		}
	}
</style>