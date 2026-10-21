@echo off
title JanjiTemu - Frontend (React Vite)
cd frontend
echo ===================================================
echo Menjalankan Frontend React Vite
echo ===================================================
if not exist node_modules (
    echo Menginstall dependencies npm terlebih dahulu...
    call npm install
)
call npm run dev
pause
