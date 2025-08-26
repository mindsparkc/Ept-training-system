<!-- 组合题组件 -->
<template>
	<view style="padding: 15px;">

		<item-question-stem type="组合" :content="group.content" score="100">
		</item-question-stem>
		<text>请仔细阅读题目，完成下面的问题</text>
		<view style="padding: 10px 15px;">


			<view v-for="(item,index) in group.questionGroup" :key="index">
				<!-- 单选题 -->
				<view v-if="item.questionType == 1">
					<item-question-stem type="单选" :content="item.content" score="100">
					</item-question-stem>

					<item-question-choice :choicesList="item.question" :type="item.questionType"></item-question-choice>
					
					<item-question-analysis></item-question-analysis>
				</view>
				<!-- 复选题 -->
				<view v-if="item.questionType == 2">
					<item-question-stem type="复选" :content="item.content" score="100">
					</item-question-stem>

					<item-question-choice :choicesList="item.question" :type="item.questionType"></item-question-choice>
				</view>
				<!-- 判断题 -->

				<view v-if="item.questionType == 3">
					<item-question-stem type="判断" :content="item.content" score="100">
					</item-question-stem>

					<item-question-choice :choicesList="item.question" :type="item.questionType"></item-question-choice>
				</view>
				<!-- 填空题 -->
				<view v-if="item.questionType == 4">
					<item-question-stem type="填空" :content="item.content" score="100">
					</item-question-stem>
					<item-question-completion :completionValues="item.question" @completionInput="completionInput">
					</item-question-completion>
				</view>
				<!-- 问答题 -->
				<view v-if="item.questionType == 5">
					<item-question-stem type="问答" :content="item.content" score="100">
					</item-question-stem>
					<item-quesiton-subjective @input="input"></item-quesiton-subjective>
				</view>

			</view>
		</view>

	</view>
</template>

<script>
	export default {
		name: "item-question-group",

		props: {
			questionGroup: {
				type: Object,
				default: _ => {
					return null
				}
			},
		},


		data() {
			return {
				group: {
					content: "<p>我是一个组合题目的题干我是一个组合题目的题干我是一个组合题目的题干我是一个组合题目的题干</p>",
					questionType: 6,
					questionGroup: [{
							content: "<p>我是单选题题目</p>",
							questionType: 1,
							question: [{
									"content": "",
									"image": "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-1acbc9a0-a6e8-45cc-a231-308cbd47885e/99275460-5819-44ab-a1b4-d9ac665e1a01.png",
									"key": "A",
									"isRight": "A"
								}, {
									"content": "桌子上",
									"key": "B",
									"isRight": "A"
								},
								{
									"content": "地板砖上",
									"key": "C",
									"isRight": "A"
								}
							]

						},
						{
							content: "<p>我是填空题题目1拉all啊啊啦啦啦啦啦啦啦</p>",
							questionType: 4,
							question: [{
								value: ''
							}, {
								value: ''
							}, {
								value: ''
							}]

						}


					]
				}


			};
		},
		methods: {
			input(e) {
				console.log("输入框输入文字", e);
			},
			completionInput(e) {
				console.log("填空题", e);
			}
		},

	}
</script>

<style>

</style>
