<template>
	<view>
		<!-- 	<item-question-stem type="单选" content="<p>我是一个题目拉all啊啊啦啦啦啦啦啦啦</p>" score="100">
		</item-question-stem>

	<item-question-choice :choicesList="choices" type="1"></item-question-choice>

		<item-quesiton-subjective @input="input"></item-quesiton-subjective>
		<item-question-completion :completionValues="completionValues" @completionInput="completionInput">
		</item-question-completion> -->



		<!-- <item-question-group></item-question-group> -->


		<item-question-mix :question="question" :isShowAnswer="false" @answer="getAnswer()"></item-question-mix>



	</view>
</template>

<script>
	export default {
		data() {
			return {
				completionValues: [{
					value: ''
				}, {
					value: ''
				}, {
					value: ''
				}],
				choices: [{
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
				],
				question: {
					"paperUnitName": "完形填空",
					"paperUnitTotal": 5,
					"paperUnitSort": 1,
					"quId": "题目ID",
					"parentId": "父题目ID",
					"version": "版本号（Linux时间戳）",
					"content": "我是一个组合题的题干我是一个组合题的题干我是一个组合题的题干我是一个组合题的题干我是一个组合题的题干我是一个组合题的题干我是一个组合题的题干我是一个组合题的题干我是一个组合题的题干",
					"display": "题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部",
					"type": "6",
					"level": "非常困难",
					"analysis": "我是一个合格的题目解析",
					"tarinFlag": "是否练习（1是 0否）",
					"examFlag": "是否考试（1是 0否）",
					"delFlag": "删除",
					"create_By": "创建者",
					"createTime": "",
					"updateBy": "更新者",
					"updateTime": "",
					"remark": "",
					"score": "20",
					"answeredFlag": false,
					"answered": "学员问答题答案",
					"categoryIds": "[1,2,3]", //题目归属的分类， 仅在展示题目详情时有效 
					"answers": [{
							"answerId": "参考答案ID",
							"quId": "题目ID",
							"content": "['我是第一个空的答案','我是第一个空的第二种答案']",
							"rightFlag": false,
							"analysis": "答案解析",
							"sort": 1,
							"selectFlag": false,
							"answered": ""
						},
						{
							"answered": "",
							"answerId": "参考答案ID",
							"quId": "题目ID",
							"content": "['我是第2个空的答案','我是第2个空的第二种答案']",
							"rightFlag": false,
							"analysis": "答案解析ssssssssssssssssssssss",
							"sort": 1,
							"selectFlag": false
						},
						{
							"answered": "",
							"answerId": "参考答案ID",
							"quId": "题目ID",
							"content": "['我是第3个空的答案','我是第一个空的第3种答案']",
							"rightFlag": true,
							"analysis": "答案解析sssssssss",
							"sort": 1,
							"selectFlag": false
						}
					],
					"children": [{
						"quId": "题目ID",
						"parentId": "父题目ID",
						"version": "版本号（Linux时间戳）",
						"content": "我是题干我是题干我是题干我是题干我是题干我是题干",
						"display": "题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部",
						"type": "1",
						"level": "容易",
						"analysis": "我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析",
						"tarinFlag": "是否练习（1是 0否）",
						"examFlag": "是否考试（1是 0否）",
						"delFlag": "删除",
						"create_By": "创建者",
						"createTime": "",
						"updateBy": "更新者",
						"updateTime": "",
						"remark": "",
						"score": "10",
						"answeredFlag": "true/false 学员是否答题",
						"answered": "学员问答题答案",
						"answers": [{
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '',
								"image":"https://vkceyugu.cdn.bspapp.com/VKCEYUGU-1acbc9a0-a6e8-45cc-a231-308cbd47885e/99275460-5819-44ab-a1b4-d9ac665e1a01.png",
								"rightFlag": false,
								"analysis": "答案解析",
								"sort": 1,
								"selectFlag": false,
								"answered": ""
							},
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '我是选项B我是选项B我是选项B我是选项B我是选项B我是选项B我是选项B我是选项B我是选项B我是选项B我是选项B我是选项B',
								"rightFlag": false,
								"analysis": "答案解析ssssssssssssssssssssss",
								"sort": 1,
								"selectFlag": false
							},
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '我是选项C',
								"rightFlag": true,
								"analysis": "答案解析sssssssss",
								"sort": 1,
								"selectFlag": false
							}
						],
					},
					{
						"quId": "题目ID",
						"parentId": "父题目ID",
						"version": "版本号（Linux时间戳）",
						"content": "我是题干我是题干我是题干我是题干我是题干我是题干",
						"display": "题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部",
						"type": "2",
						"level": "容易",
						"analysis": "我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析",
						"tarinFlag": "是否练习（1是 0否）",
						"examFlag": "是否考试（1是 0否）",
						"delFlag": "删除",
						"create_By": "创建者",
						"createTime": "",
						"updateBy": "更新者",
						"updateTime": "",
						"remark": "",
						"score": "10",
						"answeredFlag": "true/false 学员是否答题",
						"answered": "学员问答题答案",
						"answers": [{
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '我是选项A',
								"rightFlag": true,
								"analysis": "答案解析",
								"sort": 1,
								"selectFlag": false,
								"answered": ""
							},
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '我是选项B',
								"rightFlag": false,
								"analysis": "答案解析ssssssssssssssssssssss",
								"sort": 1,
								"selectFlag": false
							},
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '我是选项C',
								"rightFlag": true,
								"analysis": "答案解析sssssssss",
								"sort": 1,
								"selectFlag": false
							},
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '我是选项D',
								"rightFlag": true,
								"analysis": "答案解析sssssssss",
								"sort": 1,
								"selectFlag": false
							}
						],
					},
					{
						"quId": "题目ID",
						"parentId": "父题目ID",
						"version": "版本号（Linux时间戳）",
						"content": "<p><span style='font-family: verdana, sans-serif;font-size: 16px;font-weight: bold;font-style: italic;text-decoration: line-through;font-variant: small-caps;'>我是一个富文本的题干我是一个富文本的题干我是一个富文本的题干我是一个富文本的题干我是一个富文本的题干我是一个富文本的题干我是一个富文本的题干我是一个富文本的题干我是一个富文本的题干我是一个富文本的题干我是一个富文本的题干</span></p>",
						"display": "题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部",
						"type": "3",
						"level": "困难",
						"analysis": "我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析",
						"tarinFlag": "是否练习（1是 0否）",
						"examFlag": "是否考试（1是 0否）",
						"delFlag": "删除",
						"create_By": "创建者",
						"createTime": "",
						"updateBy": "更新者",
						"updateTime": "",
						"remark": "",
						"score": "10",
						"answeredFlag": "true/false 学员是否答题",
						"answered": "学员问答题答案",
						"answers": [{
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '正确',
								"rightFlag": false,
								"analysis": "答案解析",
								"sort": 1,
								"selectFlag": false,
								"answered": ""
							},
							
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '错误',
								"rightFlag": true,
								"analysis": "答案解析sssssssss",
								"sort": 1,
								"selectFlag": false
							}
						],
					},
					{
						"quId": "题目ID",
						"parentId": "父题目ID",
						"version": "版本号（Linux时间戳）",
						"content": "我是题干我是题干我是题干我是题干我是题干我是题干",
						"display": "题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部",
						"type": "4",
						"level": "容易",
						"analysis": "我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析",
						"tarinFlag": "是否练习（1是 0否）",
						"examFlag": "是否考试（1是 0否）",
						"delFlag": "删除",
						"create_By": "创建者",
						"createTime": "",
						"updateBy": "更新者",
						"updateTime": "",
						"remark": "",
						"score": "10",
						"answeredFlag": "true/false 学员是否答题",
						"answered": "学员问答题答案",
						"answers": [{
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '["我是第一个空的答案","我是第一个空的第二种答案"]',
								"rightFlag": false,
								"analysis": "答案解析",
								"sort": 1,
								"selectFlag": false,
								"answered": ""
							},
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '["我是第二个空的答案","我是第二个空的第二种答案"]',
								"rightFlag": false,
								"analysis": "答案解析ssssssssssssssssssssss",
								"sort": 1,
								"selectFlag": false
							},
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '["我是第三个空的答案","我是第三个空的第二种答案"]',
								"rightFlag": true,
								"analysis": "答案解析sssssssss",
								"sort": 1,
								"selectFlag": false
							}
						],
					},
					{
						"quId": "题目ID",
						"parentId": "父题目ID",
						"version": "版本号（Linux时间戳）",
						"content": "我是题干我是题干我是题干我是题干我是题干我是题干",
						"display": "题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部",
						"type": "5",
						"level": "困难",
						"analysis": "我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析我是题目解析",
						"tarinFlag": "是否练习（1是 0否）",
						"examFlag": "是否考试（1是 0否）",
						"delFlag": "删除",
						"create_By": "创建者",
						"createTime": "",
						"updateBy": "更新者",
						"updateTime": "",
						"remark": "",
						"score": "10",
						"answeredFlag": "true/false 学员是否答题",
						"answered": "学员问答题答案",
						"answers": [{
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '["我是第一个空的答案","我是第一个空的第二种答案"]',
								"rightFlag": false,
								"analysis": "答案解析",
								"sort": 1,
								"selectFlag": false,
								"answered": ""
							},
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '["我是第一个空的答案","我是第一个空的第二种答案"]',
								"rightFlag": false,
								"analysis": "答案解析ssssssssssssssssssssss",
								"sort": 1,
								"selectFlag": false
							},
							{
								"answered": "",
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": '["我是第一个空的答案","我是第一个空的第二种答案"]',
								"rightFlag": true,
								"analysis": "答案解析sssssssss",
								"sort": 1,
								"selectFlag": false
							}
						],
					},
					
					]
				},





			}
		},
		onLoad() {
			this.tranPaperUnitData()
		},
		methods: {
			
			getAnswer(e){
				console.log("我是答案哦");
			},
			
			input(e) {
				console.log("输入框输入文字", e);
			},
			completionInput(e) {
				console.log("填空题", e);
			},

			//初始化试题数据 将试题单元整合成一个数组列表方便展示  其中记录每个单元的题目总数以及该题目在该单元的顺序方便展示 "paperUnitName": "完形填空",
			//"paperUnitTotal": 5,
			//	"paperUnitSort": 1,
			tranPaperUnitData(e) {

				let paperUnit = [{
					"paperUnitId": "试卷单元编号",
					"paperId": "所属试卷编号",
					"display": "显示名称（默认为type的对应命名）",
					"quType": "题目类型",
					"totalScore": "总分值",
					"eachScore": "每题的默认分值",
					"quOrderFlag": "题目排序策略（0正序 1随机）",
					"answerOrderFlag": "候选答案排序策略（0正序 1随机）",
					"sort": "排序",
					"questions": [{
						"quId": "题目ID",
						"parentId": "父题目ID",
						"version": "版本号（Linux时间戳）",
						"content": "题干内容",
						"display": "题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部",
						"type": "题目类型：1-单选题；2-复选题；3-判断题；4-填空题；5-问答题；6-组合题。取字典 question_type",
						"level": "容易",
						"analysis": "题目解析",
						"tarinFlag": "是否练习（1是 0否）",
						"examFlag": "是否考试（1是 0否）",
						"delFlag": "删除",
						"create_By": "创建者",
						"createTime": "",
						"updateBy": "更新者",
						"updateTime": "",
						"remark": "",
						"score": "10",
						"answeredFlag": "true/false 学员是否答题",
						"answered": "学员问答题答案",
						"categoryIds": "[1,2,3]",
						 "answers": [{
							"answerId": "参考答案ID",
							"quId": "题目ID",
							"content": "答案内容",
							"rightFlag": "是否正确（1是 0否）",
							"analysis": "答案解析",
							"sort": 1,
							"selectFlag": "学员是否选中 0未选中 1选中"
						}],
						"children": [{
							"quId": "题目ID",
							"parentId": "父题目ID",
							"version": "版本号（Linux时间戳）",
							"content": "题干内容",
							"display": "题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部",
							"type": "1",
							"level": "容易",
							"analysis": "题目解析",
							"tarinFlag": "是否练习（1是 0否）",
							"examFlag": "是否考试（1是 0否）",
							"delFlag": "删除",
							"create_By": "创建者",
							"createTime": "",
							"updateBy": "更新者",
							"updateTime": "",
							"remark": "",
							"score": "10",
							"answeredFlag": "true/false 学员是否答题",
							"answered": "学员问答题答案",
							"answers": [{
								"answerId": "参考答案ID",
								"quId": "题目ID",
								"content": "答案内容",
								"rightFlag": "是否正确（1是 0否）",
								"analysis": "答案解析",
								"sort": 1,
								"selectFlag": "学员是否选中 0未选中 1选中"
							}],
						}]
					}]
				}];

				let allQuestion = []

				for (let i = 0; i < paperUnit.length; i++) {

					let paperUnitName = paperUnit[i].display;
					let paperUnitTotal = paperUnit[i].questions.length;
					let questions = paperUnit[i].questions;
					for (let j = 0; j < questions.length; j++) {
						questions[j].paperUnitName = paperUnitName;
						questions[j].paperUnitTotal = paperUnitTotal;
						questions[j].paperUnitSort = j+1;
					}

					allQuestion = allQuestion.concat(questions)
				}

				// console.log(JSON.stringify(allQuestion));


			}





		}
	}
</script>

<style>

</style>
