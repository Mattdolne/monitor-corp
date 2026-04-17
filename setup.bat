@echo off
title Instalador Monitor CORP - Estacio Resende
color 0b

:: 1. TRAVA DE DIRETÓRIO: Força o script a rodar na mesma pasta onde o .bat está localizado
cd /d "%~dp0"

set "DESTINO=C:\MonitorCorp"

echo ======================================================
echo   INSTALANDO MONITOR CORP - AGENTE DE CONFORMIDADE
echo ======================================================
echo.

if not exist "%DESTINO%" (
    echo [+] Criando diretorio em %DESTINO%...
    mkdir "%DESTINO%"
)

:: 2. COPIA COM TRATAMENTO DE ERRO
echo [+] Copiando o executavel...
copy /Y "MonitorCorp.exe" "%DESTINO%\" >nul
if %errorlevel% neq 0 goto :ERRO_EXE

echo [+] Copiando o JRE (isto pode demorar alguns segundos)...
xcopy /E /I /Y "jre" "%DESTINO%\jre" >nul
if %errorlevel% neq 0 goto :ERRO_JRE

echo [+] Configurando inicializacao automatica no Registro (HKCU)...
reg add "HKLM\Software\Microsoft\Windows\CurrentVersion\Run" /v "MonitorCORP" /t REG_SZ /d "\"%DESTINO%\MonitorCorp.exe\"" /f >nul

echo [+] Criando atalho na Area de Trabalho...
set "SCRIPT_ATALHO=%TEMP%\criar_atalho.vbs"
echo Set oWS = WScript.CreateObject("WScript.Shell") > "%SCRIPT_ATALHO%"
echo sLinkFile = oWS.SpecialFolders("Desktop") ^& "\%NOME_ATALHO%" >> "%SCRIPT_ATALHO%"
echo Set oLink = oWS.CreateObject("WScript.Shell").CreateShortcut(sLinkFile) >> "%SCRIPT_ATALHO%"
echo oLink.TargetPath = "%DESTINO%\MonitorCorp.exe" >> "%SCRIPT_ATALHO%"
echo oLink.WorkingDirectory = "%DESTINO%" >> "%SCRIPT_ATALHO%"
echo oLink.Description = "Monitor de Conformidade Corporativa" >> "%SCRIPT_ATALHO%"
echo oLink.Save >> "%SCRIPT_ATALHO%"
cscript /nologo "%SCRIPT_ATALHO%"
del "%SCRIPT_ATALHO%"

echo [+] Iniciando o agente...
start "" "%DESTINO%\MonitorCorp.exe"

echo.
color 0a
echo ======================================================
echo   INSTALACAO CONCLUIDA COM SUCESSO!
echo ======================================================
pause
exit

:: ========================================================
:: ROTINAS DE TRATAMENTO DE ERRO (ABORTAM A INSTALAÇÃO)
:: ========================================================

:ERRO_EXE
echo.
color 0c
echo [X] ERRO FATAL: MonitorCorp.exe nao encontrado!
echo Certifique-se de que o executavel esta na mesma pasta deste instalador.
echo Instalacao abortada.
pause
exit

:ERRO_JRE
echo.
color 0c
echo [X] ERRO FATAL: Pasta 'jre' nao encontrada!
echo O programa precisa da pasta com o motor Java portátil ao lado do executavel.
echo Instalacao abortada.
pause
exit