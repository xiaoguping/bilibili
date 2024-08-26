<template>
	<div class="mask-test-wrap">
<!-- 		<svg>
		    <defs>    
		        <mask id="mask" maskContentUnits="objectBoundingBox">
		            <ellipse cx=".5" cy=".5" rx=".4" ry=".2" fill="white"></ellipse>
		            <rect x=".3" y=".1" width=".4" height=".8" rx=".1" ry=".1" fill="white"></rect>
		        </mask>
		    </defs>    
		</svg> -->
		<div class="video-content">
			<video width="100%" ref="video" src="../assets/video/model.mp4" controls></video>
			<div class="play" v-if="showBtn" @click="play">play</div>
			<div class="mask" :style="{ maskImage: `url(${mask})` }">
				<div class="text">
					啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊
				</div>
				
			</div>
		</div>
		
		<!-- <img src="../assets/image/video/vd.png" class="mask" alt=""> -->
		<!-- <img id="mask" src="../assets/image/video/icon_01.png" alt=""> -->
		<!-- <img src="../assets/image/header/header_bg.jpeg" alt=""> -->
		<!-- <div class="mask"></div> -->
	</div>
	
</template>
<script>
	export default {
		name: "",
		components: {
			
		},
		data() {
			return {
				mask: require('../assets/image/mask/0.png'),
				maskTimmer: null,
				showBtn: true
			}
		},
		created() {
			// this.playMask();
		},
		methods: {
			play () {
				let video = this.$refs.video;
				this.showBtn = false;
				video.play();
				this.playMask();
			},
			playMask() {
				let start = 0;
				let total = 33;
				
				this.maskTimmer = setInterval(() => {
					if (start < 89) {
						start ++;
						this.mask = require(`../assets/image/mask/${start}.png`);
					} else {
						clearInterval(this.maskTimmer);
						this.maskTimmer = null;
					}
				}, 275);
			}
		},
	}
</script>
<style lang="scss" scoped>
	.mask-test-wrap {
		width: 100%;
		// height: 400px;
		.video-content {
			width: 1000px;
			position: relative;
			
			.play {
				width: 100px;
				height: 100px;
				border-radius: 50%;
				border: 3px solid #fff;
				display: flex;
				justify-content: center;
				align-items: center;
				position: absolute;
				left: 50%;
				top: 50%;
				margin-left: -50px;
				margin-top: -50px;
				font-size: 40px;
				color: #fff;
				cursor: pointer;
				z-index: 999;
			}
			.mask {
				width: 100%;
				height: 562px;
				// mask-image: url(../assets/image/mask/J2s2tGIfNNGAIEvYAAAbqeT6NS4232.png);
				mask-type: luminance;
				mask-mode: luminance, alpha;
				mask-size: 1000px 500px;
				position: absolute;
				left: 0;
				top: 0;
				.text {
					color: #fff;
				}
				
				// mask-image: url(#mask);
				// mask-size: 50%;
				// mask-image: url(../assets/image/video/icon_01.png);
			}
		}
		
		
	}
</style>