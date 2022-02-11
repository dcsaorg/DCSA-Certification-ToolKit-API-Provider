@echo off
call mvn clean package
set version=2.2.0
set package=TNT-v%version%
rmdir /S /Q %package%
mkdir %package%\ctk
mkdir %package%\documents
mkdir %package%\ctk\testdata
copy target\DCSA-Validator-Toolkit-*-jar-with-dependencies.jar %package%\ctk\
move %package%\ctk\DCSA-Validator-Toolkit-*-jar-with-dependencies.jar %package%\ctk\DCSA-Validator-Toolkit.jar
copy Dockerfile %package%\
copy docker-compose-tnt.yml %package%\docker-compose-tnt.yml
copy docker-compose-notification.yml %package%\docker-compose-notification.yml
copy src\main\resources\config\v2\testdata.json %package%\ctk\testdata
Xcopy src\main\java\org\dcsa\api\validator\features %package%\ctk\src\main\java\org\dcsa\api\validator\features /E /I
Xcopy suitexmls\ %package%\ctk\suitexmls\ /E /I
copy suitexmls\TNT-TestSuite.xml %package%\ctk\
copy suitexmls\TNT-Notification-TestSuite.xml %package%\ctk\
copy run_ctk.sh %package%\ctk\
echo package is ready..
pause