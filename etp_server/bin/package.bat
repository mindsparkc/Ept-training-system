@echo off
:: �����б��⽫��ʾΪ���Զ�����⡱
title ��ѵ�뿼��ϵͳ���

call :ColorText 0b "red"
set baseDir=%CD%

::-------------------------------------------------------------------------
::--tag
::-------------------------------------------------------------------------
set etp_server_tag=PROD-SPRINT-V0.1.3-20220711-1
set etp_web_manager_tag=PROD-SPRINT-V0.1.3-20220711-1
set etp_web_student_tag=PROD-SPRINT-V0.1.3-20220711-1
set ParseFiles_tag=PROD-SPRINT-V0.1.3-20220711-1

echo ��������������������������������������[��Ϣ] ������������������������������������������
echo ��ǰĿ¼ %baseDir%
echo .
echo ִ�иýű�ǰ��ȷ���ű�����Ŀ¼�����κ����ļ��л��ļ�
echo .
echo ����ִ�д���ű����ýű��������������ݣ�
echo     1. etp_server %etp_server_tag%
echo     2. etp_web_student %PROD-SPRINT-V0.1.3-20220711-0%
echo     3. etp_web_manager %etp_web_manager_tag%
echo     4. ParseFiles %ParseFiles_tag%
echo ����������������������������������������������������������������������������������������������


:: ���� Y ����ִ�У�����ȡ��ִ��
set continue=
set /p continue= �����루y/N��ȷ�ϻ�ȡ��������
if /i %continue% NEQ Y (
    echo ����ִ������ֹ
    pause
    exit
)
echo.

echo ���� ����ʱ�����󽫽��б��뻷�����
timeout /t 15 /nobreak 2>nul
call :CheckEnv "git --version"
call :CheckEnv "mvn --version"
echo.
echo ���� ���뻷�����ͨ��
echo.

::-------------------------------------------------------------------------
::--builded ����õ��ļ�����ڸ�Ŀ¼��
::-------------------------------------------------------------------------
md .\builded\etp_server\config
md .\builded\etp_web_manager
md .\builded\etp_web_student
md .\builded\ParseFiles

::-------------------------------------------------------------------------
::--etp_server
::-------------------------------------------------------------------------
echo ����ʱ�����󣬽���ʼ����Դ���ļ�����ʼ���루etp_server %etp_server_tag%��
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
echo ����ʱ�����󣬽���ʼ����Դ���ļ�����ʼ���루etp_web_manager %etp_web_manager_tag%��
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
echo ����ʱ�����󣬽���ʼ����Դ���ļ�����ʼ���루etp_web_student %etp_web_student_tag%��
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
echo ����ʱ�����󣬽���ʼ����Դ���ļ�����ʼ���루ParseFiles %ParseFiles_tag%��
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
::--����Ϊ��������
::-------------------------------------------------------------------------
::��⻷��������
:CheckEnv
    call %~1
    if %errorlevel% == 0 (
        echo successfully
    ) else (
        echo failed
    )
goto:eof

:: ����������ɫ
:ColorText
echo off
<nul set /p ".=%DEL%" > "%~2"
findstr /v /a:%1 /R "^$" "%~2" nul
del "%~2" > nul 2>&1
goto :eof


