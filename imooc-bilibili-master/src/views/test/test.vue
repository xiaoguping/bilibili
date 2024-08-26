<template>
	<div class="test-wrap">
	</div>
</template>
<script>
	export default {
		name: "",
		components: {
		},
		created() {
			this.test();
		},
		data() {
			return {
			
			}
		},
		methods: {
			async test() {
				let res = await api.test();
				console.log(res);
			}
		},
	}
</script>
<style lang="scss" scoped>
	
</style>