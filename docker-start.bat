@echo off
echo Iniciando JTech TaskList...

docker-compose down
docker-compose up --build -d

timeout /t 20 /nobreak > nul

echo.
echo Aplicacao iniciada:
echo Frontend: http://localhost:5173
echo Backend: http://localhost:8080
echo Swagger: http://localhost:8080/swagger-ui/index.html
echo.

pause