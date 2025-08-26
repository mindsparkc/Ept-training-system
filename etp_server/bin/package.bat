@echo off
:: 命令行标题将显示为“自定义标题”
title 培训与考试系统打包

call :ColorText 0b "red"
set baseDir=%CD%

::-------------------------------------------------------------------------
::--tag
::-------------------------------------------------------------------------
set etp_server_tag=PROD-SPRINT-V0.1.3-20220711-1
set etp_web_manager_tag=PROD-SPRINT-V0.1.3-20220711-1
set etp_web_student_tag=PROD-SPRINT-V0.1.3-20220711-1
set ParseFiles_tag=PROD-SPRINT-V0.1.3-20220711-1

echo ┌──────────────────[信息] ────────────────────┐
echo 当前目录 %baseDir%
echo .
echo 执行该脚本前请确保脚本所在目录下无任何子文件夹或文件
echo .
echo 正在执行打包脚本，该脚本将会打包以下内容：
echo     1. etp_server %etp_server_tag%
echo     2. etp_web_student %PROD-SPRINT-V0.1.3-20220711-0%
echo     3. etp_web_manager %etp_web_manager_tag%
echo     4. ParseFiles %ParseFiles_tag%
echo └─────────────────────────────────────────────┘


:: 输入 Y 继续执行，否则取消执行
set continue=
set /p continue= 请输入（y/N）确认或取消操作：
if /i %continue% NEQ Y (
    echo 本次执行已终止
    pause
    exit
)
echo.

echo 》》 倒计时结束后将进行编译环境检测
timeout /t 15 /nobreak 2>nul
call :CheckEnv "git --version"
call :CheckEnv "mvn --version"
echo.
echo 《《 编译环境检测通过
echo.

::-------------------------------------------------------------------------
::--builded 打包好的文件存放在该目录下
::-------------------------------------------------------------------------
md .\builded\etp_server\config
md .\builded\etp_web_manager
md .\builded\etp_web_student
md .\builded\ParseFiles

::-------------------------------------------------------------------------
::--etp_server
::-------------------------------------------------------------------------
echo 倒计时结束后，将开始下载源码文件并开始编译（etp_server %etp_server_tag%）
timeout /t 10 /nobreak 2>nul

md etp_server
cd etp_server
call git init
call git remote add origin http://10.153.3.240:3000/wanglijie/etp_server.git
call git fetch origin +refs/tags/%etp_server_tag%
call git checkout -qf FETCH_HEAD
call mvn clean package -P prod
xcopy /B /Y .\ruoyi-admin\target\config\ ..\builded\etp_server\config
xcopy /B /Y .\ruoyi-admin\target\etp*.jar ..\builded\etp_server
xcopy /B /Y .\bin\etp_server.sh ..\builded\etp_server
xcopy /B /Y .\bin\etp_server.bat ..\builded\etp_server
cd ..

::-------------------------------------------------------------------------
::--etp_web_manager
::-------------------------------------------------------------------------
echo 倒计时结束后，将开始下载源码文件并开始编译（etp_web_manager %etp_web_manager_tag%）
timeout /t 10 /nobreak 2>nul

md etp_web_manager
cd etp_web_manager
call git init
call git remote add origin http://10.153.3.240:3000/wanglijie/etp_web_manager.git
call git fetch origin +refs/tags/%etp_web_manager_tag%
call git checkout -qf FETCH_HEAD
call npm install
call npm run build:prod
xcopy /E /Y /Q .\dist ..\builded\etp_web_manager
cd ..

::-------------------------------------------------------------------------
::--etp_web_student
::-------------------------------------------------------------------------
echo 倒计时结束后，将开始下载源码文件并开始编译（etp_web_student %etp_web_student_tag%）
timeout /t 10 /nobreak 2>nul

md etp_web_student
cd etp_web_student

call git init
call git remote add origin http://10.153.3.240:3000/wanglijie/etp_web_student.git
call git fetch origin +refs/tags/%etp_web_student_tag%
call git checkout -qf FETCH_HEAD
call npm install
call npm run build:prod
xcopy /E /Y /Q .\dist ..\builded\etp_web_student
cd ..

::-------------------------------------------------------------------------
::--ParseFiles
::-------------------------------------------------------------------------
echo 倒计时结束后，将开始下载源码文件并开始编译（ParseFiles %ParseFiles_tag%）
timeout /t 10 /nobreak 2>nul

md ParseFiles
cd ParseFiles
call git init
call git remote add origin http://10.153.3.240:3000/wanglijie/ParseFiles.git
call git fetch origin +refs/tags/%ParseFiles_tag%
call git checkout -qf FETCH_HEAD
call mvn clean package -P prod
xcopy /B /Y .\src\main\resources\application-template.yml ..\builded\ParseFiles\application.yml
xcopy /B /Y .\target\ParseFiles.jar ..\builded\ParseFiles
xcopy /B /Y .\bin\start_parseFiles.sh ..\builded\ParseFiles
cd ..

pause&&goto:eof


::-------------------------------------------------------------------------
::--以下为函数定义
::-------------------------------------------------------------------------
::检测环境完整性
:CheckEnv
    call %~1
    if %errorlevel% == 0 (
        echo successfully
    ) else (
        echo failed
    )
goto:eof

:: 设置文字颜色
:ColorText
echo off
<nul set /p ".=%DEL%" > "%~2"
findstr /v /a:%1 /R "^$" "%~2" nul
del "%~2" > nul 2>&1
goto :eof


