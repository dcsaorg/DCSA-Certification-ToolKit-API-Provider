ECHO ON
set arg1="%~1"
newman run %arg1% -r htmlextra
PAUSE