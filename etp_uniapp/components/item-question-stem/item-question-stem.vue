<!-- 题干  题目类型   富文本显示题干内容 题目分数 -->
<template>
	<view style="color: #333333;font-size: 16px;margin-top: 15px;">
		<mp-html :content="contentFormat"></mp-html>

	</view>
</template>

<script>
	export default {
		name: "item-question-stem",

		props: {

			type: {
				type: String,
				default: "",
				required: true
			},
			content: {
				type: String,
				default: "",
				required: true
			},

			score: {
				type: Number,
				default: 0,
				required: true
			},
		


		},
		computed: {
			contentFormat() {
				let type = this.type;
				let score = this.score;
				let typeHtml =
					`<span style='font-size: 12px;color: #FFFFFF;background: linear-gradient( 135deg, #037eff 10%, #736EFE 100%);padding: 3px 8px;border-top-left-radius: 10px;border-top-right-radius: 8px;border-bottom-right-radius: 8px;line-height:30px;box-shadow: rgba(136, 165, 191, 0.48) 5px 2px 10px 0px, rgba(255, 255, 255, 0.8) -5px -2px 10px 0px;'>${type}题</span><span style='font-size: 12px;color: #909299;margin-right:5px;margin-left:5px'>(${score}分)</span>`;

				//若题干首行为p标签 则将该p标签设定为行内元素 防止换行
				let con = this.content.substring(0, 2)
			
				if (con == '<p') {
					// console.log(typeHtml.concat(this.content.replace(con, "<p style='display:inline'>")));
					return typeHtml.concat(this.content.replace(con, "<p style='display:inline'"));
				} else {
					return typeHtml.concat(this.content)
				}


			}
		},

		data() {
			return {};
		}
	}
</script>

<style>

</style>
