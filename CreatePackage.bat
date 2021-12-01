@echo off
call mvn clean package 
rmdir /S /Q ctk 
mkdir ctk
copy target\DCSA-Validator-Toolkit-*-jar-with-dependencies.jar ctk\
move ctk\DCSA-Validator-Toolkit-*-jar-with-dependencies.jar ctk\DCSA-Validator-Toolkit.jar 
copy Dockerfile ctk\
#copy docker-compose-template.yml ctk\docker-compose.yml
copy docker-compose.yml ctk\docker-compose.yml
copy src\main\resources\config\v2\dataconfig.json ctk\
Xcopy src\main\java\org\dcsa\api\validator\features ctk\src\main\java\org\dcsa\api\validator\features /E /I
Xcopy suitxmls ctk\suitxmls /E /I
