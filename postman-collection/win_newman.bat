@echo on
set "arg1=%~1"
set "arg2=%~2"
echo "arg1 = %arg1%"
echo "arg2 = %arg2%"
newman run %arg1% -r htmlextra %*
pause