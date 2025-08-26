/**
 * 根据文件扩展名获取文件类型
 * */
export function getExt(url) {
  // 后缀获取
  let suffix = "";
  // 获取类型结果
  let result = "";
  try {
    const flieArr = url.split(".");
    suffix = flieArr[flieArr.length - 1];
  } catch (err) {
    suffix = "";
  }
  // fileName无后缀返回 false
  if (!suffix) { return -1; }
  suffix = suffix.toLocaleLowerCase();
  // 图片格式
  const imglist = ["png", "jpg", "jpeg", "bmp", "gif"];
  // 进行图片匹配
  result = imglist.find(item => item === suffix);
  if (result) {
    return 5;
  }
  // 匹配txt
  const txtlist = ["txt"];
  result = txtlist.find(item => item === suffix);
  if (result) {
    return 5;
  }
  // 匹配 excel
  const excelist = ["xls", "xlsx"];
  result = excelist.find(item => item === suffix);
  if (result) {
    return 1;
  }
  // 匹配 word
  const wordlist = ["doc", "docx"];
  result = wordlist.find(item => item === suffix);
  if (result) {
    return 1;
  }
  // 匹配 pdf
  const pdflist = ["pdf"];
  result = pdflist.find(item => item === suffix);
  if (result) {
    return 1;
  }
  // 匹配 ppt
  const pptlist = ["ppt", "pptx"];
  result = pptlist.find(item => item === suffix);
  if (result) {
    return 1;
  }
  // 匹配 视频
  const videolist = ["mp4", "m2v", "mkv", "rmvb", "wmv", "avi", "flv", "mov", "m4v"];
  result = videolist.find(item => item === suffix);
  if (result) {
    return 2;
  }
  // 匹配 音频
  const radiolist = ["mp3", "wav", "wmv"];
  result = radiolist.find(item => item === suffix);
  if (result) {
    return 5;
  }
  // 其他 文件类型
  return 5;
}
