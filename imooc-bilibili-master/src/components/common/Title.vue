<template>
	<div class="register-title-wrap">
		<span>{{ title }}</span>
	</div>
</template>
<script>
	export default {
		name: "",
		props: [
			'title'
		],
		data() {
			return {
				
			}
		}
	}
</script>
<style lang="scss" scoped>
	.register-title-wrap {
		width: 100%;
		height: 28px;
		border-bottom: 1px solid #ddd;
		margin-bottom: 60px;
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
</style>