import E from 'wangeditor';
import { Loading } from 'element-ui';
import { Message } from 'element-ui';
import { getToken } from '@/utils/auth'

const { $, BtnMenu, DropListMenu, PanelMenu, DropList, Panel, Tooltip } = E;
let _this = null;
const filePath = process.env.VUE_APP_BASE_API + '/common/upload';
                                           // commom/upload/fastdfs
let loading = null;

const funcs = (e) => {
  return new Promise(((resolve, reject) => {
    let xhr = new XMLHttpRequest();
    xhr.open('post',filePath,true);
    xhr.setRequestHeader('Authorization','Bearer '+getToken())
    xhr.setRequestHeader('x-platform','ADMIN')
    xhr.send(e)
    xhr.onreadystatechange = function(res) {
      if (xhr.readyState==4){
        try{
          let res = JSON.parse(xhr.responseText)
          if (JSON.parse(xhr.responseText).code == 200) {
            resolve(res);
          } else {
            Message.error('上传错误！');
            loading.close();
          }
        }catch (e) {
          Message.error('上传错误！')
          loading.close();
        }
      }
    }
  }))
}

const UploadFile = (file) => {
  return new Promise((resolve, reject) => {
    let data = new FormData();
    data.append('file',file);
    console.log(data.get('file'));
    let reader = new FileReader();
    reader.readAsDataURL(file);
    const audio = document.createElement('audio');
    reader.onload = (e) => {
      audio.src = e.target.result;
      audio.oncanplay = (e) => {
        resolve(data);
      }
    }
  })
}

export default class musicEditor extends BtnMenu {
  constructor(editor) {
    // data-title属性表示当鼠标悬停在该按钮上时提示该按钮的功能简述
    _this = editor;
    const $elem = E.$('<div class="w-e-menu" data-title="音频"><i class="iconfont icon-yinle"></i>' +
      '<input type="file" id="audio" accept="audio/*" style="position:absolute;left:0;width:40px;opacity:0;" /></div>')
    super($elem,editor);
  }

  clickHandler() {
    let editor = this.editor;
    let input = document.getElementById('audio');
    input.removeEventListener('change',()=>console.log('a'));
    input.addEventListener('change',function(e){
      let files = this.files;
      UploadFile(this.files[0]).then((res)=>{
        loading = Loading.service({
          lock: false,
          text: '文件上传中',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        editor.disable();
        funcs(res).then((e)=>{
          editor.enable();
          let html = '<audio src="'+e.url+'" controls style="outline: none"></audio></br>';
          editor.cmd.do('insertHTML',html);
          Message.success('上传成功');
          loading.close();
        })
      })
    })
    input.removeEventListener('change',() => console.log('a'));
    input.outerHTML = input.outerHTML;
    return false;
    // alert('hello!');
  }

  tryChangeActive(){
    // this.active();
  }
}
export {musicEditor};
