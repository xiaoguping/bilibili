<template>
	<div id="app" @click="closeDialog">
		<keep-alive>
			<router-view v-if="$route.meta.keepAlive"></router-view>
		</keep-alive>
		<router-view v-if="!$route.meta.keepAlive"></router-view>
	</div>
</template>
<script>
	import api from './fetch/api/auth.js';
	export default {
		name: 'app',
		created() {
			let token = localStorage.getItem('token');
			if (token) {
				// this.getAuthList();
			}
		},
		methods: {
			closeDialog() {
				this.$store.commit('setShowSignin', false);
				this.$store.commit('setShowInfo', false);
				this.$store.commit('setMoments', false);
			},
			async getAuthList() {
				let res = await api.getAuthList();
				if (res && res.code === '0') {
					this.$store.commit('setAuthList', res.data);
				}
			}
		},
	}
</script>
<style lang="scss">
	#app {
		font-family: Avenir, Helvetica, Arial, sans-serif;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
		color: #2c3e50;
	}
</style>
