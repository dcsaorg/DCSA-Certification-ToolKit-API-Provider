@echo off
call mvn clean package
set version=2.2.0
set package=TNT-v%version%
rmdir /S /Q %package%
mkdir %package%\ctk
mkdir %package%\documents
copy target\DCSA-Validator-Toolkit-*-jar-with-dependencies.jar %package%\ctk\
move %package%\ctk\DCSA-Validator-Toolkit-*-jar-with-dependencies.jar %package%\ctk\DCSA-Validator-Toolkit.jar
copy Dockerfile %package%\
:: copy docker-compose-template.yml %package%\docker-compose.yml
copy docker-compose.yml %package%\docker-compose.yml
copy src\main\resources\config\v2\dataconfig.json %package%\ctk\
Xcopy src\main\java\org\dcsa\api\validator\features %package%\ctk\src\main\java\org\dcsa\api\validator\features /E /I
Xcopy suitexmls\ %package%\ctk\suitexmls\ /E /I
copy suitexmls\TNT-TestSuite.xml %package%\ctk\TestSuite.xml