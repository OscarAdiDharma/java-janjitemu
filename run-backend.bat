@echo off
title JanjiTemu - Backend (Java Spring Boot)
cd backend
set JAVA_HOME=C:\Program Files\Java
echo ===================================================
echo Menjalankan Backend Java Spring Boot di Port 5000
echo ===================================================
call .\mvnw.cmd spring-boot:run
pause
