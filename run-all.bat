@echo off
title JanjiTemu - Launcher
echo ===================================================
echo Membuka Backend Java dan Frontend React...
echo ===================================================

start "JanjiTemu Backend (Spring Boot)" cmd /c "run-backend.bat"
timeout /t 3 /nobreak >nul
start "JanjiTemu Frontend (React)" cmd /c "run-frontend.bat"

echo.
echo Keduanya sedang berjalan di jendela terminal masing-masing!
echo Backend API : http://localhost:5000
echo Frontend Web: http://localhost:5173
echo.
pause
