#!/bin/bash

baseDir=`pwd`

#-------------------------------------------------------------------------
#--tag
#-------------------------------------------------------------------------
etp_server_tag="PROD-SPRINT-V0.1.3-20220711-1"
etp_web_manager_tag="PROD-SPRINT-V0.1.3-20220711-1"
etp_web_student_tag="PROD-SPRINT-V0.1.3-20220711-1"
ParseFiles_tag="PROD-SPRINT-V0.1.3-20220711-1"

echo "┌──────────────────[信息] ────────────────────┐"
echo "当前目录 $baseDir"
echo "."
echo "执行该脚本前请确保脚本所在目录下无任何子文件夹或文件"
echo "."
echo "正在执行打包脚本，该脚本将会打包以下内容："
echo "    1. etp_server %etp_server_tag%"
echo "    2. etp_web_student %PROD-SPRINT-V0.1.3-20220711-0%"
echo "    3. etp_web_manager %etp_web_manager_tag%"
echo "    4. ParseFiles %ParseFiles_tag%"
echo "└─────────────────────────────────────────────┘"

echo -ne "\r"

# "输入 Y 继续执行，否则取消执行"
echo "请输入（y/N）确认或取消操作："
read continue
if [ "$continue" != "Y" ]; then
    echo 本次执行已终止
    pause
    exit
fi

echo -ne "\r"

#-------------------------------------------------------------------------
#--builded 打包好的文件存放在该目录下
#-------------------------------------------------------------------------
mkdir -p ./builded/etp_server/config
mkdir -p ./builded/etp_web_manager
mkdir -p ./builded/etp_web_student
mkdir -p ./builded/ParseFiles

#-------------------------------------------------------------------------
#--etp_server
#-------------------------------------------------------------------------
echo "倒计时结束后，将开始下载源码文件并开始编译（etp_server %etp_server_tag%）"
for SEC in {10..1}     #从10到1循环
do
echo -n "After ${SEC}s is end!"  #-n表⽰显⽰的内容不换⾏
echo -ne "\r"   #-e表⽰后⾯的⾏刷新前⼀⾏"\r"表⽰将光标移⾄⾏⾸
sleep 1  #每秒钟显⽰⼀次
done

mkdir etp_server
cd etp_server
git init
git remote add origin http://10.153.3.240:3000/wanglijie/etp_server.git
git fetch origin +refs/tags/$etp_server_tag
git checkout -qf FETCH_HEAD
mvn clean package -P prod
cp -r .\sinosoft-admin\target\config\ ..\builded\etp_server\config
cp -r .\sinosoft-admin\target\etp*.jar ..\builded\etp_server
cp -r .\bin\etp_server.sh ..\builded\etp_server
cp -r .\bin\etp_server.bat ..\builded\etp_server
cd ..

#-------------------------------------------------------------------------
#--etp_web_manager
#-------------------------------------------------------------------------
echo "倒计时结束后，将开始下载源码文件并开始编译（etp_web_manager %etp_web_manager_tag%）"
for SEC in {10..1}     #从10到1循环
do
echo -n "After ${SEC}s is end!"  #-n表⽰显⽰的内容不换⾏
echo -ne "\r"   #-e表⽰后⾯的⾏刷新前⼀⾏"\r"表⽰将光标移⾄⾏⾸
sleep 1  #每秒钟显⽰⼀次
done
mkdir etp_web_manager
cd etp_web_manager
git init
git remote add origin http://10.153.3.240:3000/wanglijie/etp_web_manager.git
git fetch origin +refs/tags/$etp_web_manager_tag
git checkout -qf FETCH_HEAD
npm install
npm run build:prod
cp -r .\dist ..\builded\etp_web_manager
cd ..

#-------------------------------------------------------------------------
#--etp_web_student
#-------------------------------------------------------------------------
echo "倒计时结束后，将开始下载源码文件并开始编译（etp_web_student %etp_web_student_tag%）"
for SEC in {10..1}     #从10到1循环
do
echo -n "After ${SEC}s is end!"  #-n表⽰显⽰的内容不换⾏
echo -ne "\r"   #-e表⽰后⾯的⾏刷新前⼀⾏"\r"表⽰将光标移⾄⾏⾸
sleep 1  #每秒钟显⽰⼀次
done

mkdir etp_web_student
cd etp_web_student
git init
git remote add origin http://10.153.3.240:3000/wanglijie/etp_web_student.git
git fetch origin +refs/tags/$etp_web_student_tag
git checkout -qf FETCH_HEAD
npm install
npm run build:prod
cp -r .\dist ..\builded\etp_web_student
cd ..

#-------------------------------------------------------------------------
#--ParseFiles
#-------------------------------------------------------------------------
echo "倒计时结束后，将开始下载源码文件并开始编译（ParseFiles %ParseFiles_tag%）"
for SEC in {10..1}     #从10到1循环
do
echo -n "After ${SEC}s is end!"  #-n表⽰显⽰的内容不换⾏
echo -ne "\r"   #-e表⽰后⾯的⾏刷新前⼀⾏"\r"表⽰将光标移⾄⾏⾸
sleep 1  #每秒钟显⽰⼀次
done

mkdir ParseFiles
cd ParseFiles
git init
git remote add origin http://10.153.3.240:3000/wanglijie/ParseFiles.git
git fetch origin +refs/tags/$ParseFiles_tag
git checkout -qf FETCH_HEAD
mvn clean package -P prod
cp -r .\src\main\resources\application-template.yml ..\builded\ParseFiles\application.yml
cp -r .\target\ParseFiles.jar ..\builded\ParseFiles
cp -r .\bin\start_parseFiles.sh ..\builded\ParseFiles
cd ..

pause


