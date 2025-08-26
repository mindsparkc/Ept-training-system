
export default {
	
	clickEvent:(eventType,id)=>{
		
		let params={
			eventType:eventType,
			id:id,
			streamName:'stream'
		}
		
		uni.$u.api.clickEvent(params).then(res => {
			if (res.data) {
				console.log(res);
			}
		
		
		}).catch(err => {
			console.log(err.status);
		});
		
		
	},
	
	//驻留事件 eventType:事件类型 id：课件id pt:课件入口类型 pid课件入口id(课程或者任务) offset:office课件记录页码 、视频类 记录播放偏移时长
	stayEvent:(eventType,id,pt,pid,offset)=>{
		let params={
			eventType:eventType,
			id:id,
			pt:pt,
			pid:pid,
			offset:offset,
			streamName:'stream'
		}
		
		uni.$u.api.stayEvent(params).then(res => {
			if (res.data) {
				console.log(res);
			}
		
		
		}).catch(err => {
			console.log(err.errMsg);
		});
	},
	
	
	//课件形式解析
	transfType(type) {
		
		switch (type) {
			case '1':
				return 'OFFICE';
			case '2':
				return '视频';
			case '3':
				return '文章';
			case '4':
				return '其他';
		
			default:
				break;
		}
	}
	
	
	
}
