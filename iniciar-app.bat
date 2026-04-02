@echo off
setlocal
cd /d "%~dp0"

where mvn >nul 2>nul
if errorlevel 1 (
  echo Maven nao encontrado no PATH.
  echo Instale o Maven e tente novamente.
  pause
  exit /b 1
)

echo Iniciando aplicacao JavaFX...
mvn -q javafx:run

if errorlevel 1 (
  echo.
  echo Falha ao iniciar. Verifique se o JDK 21 esta instalado.
)
pause
