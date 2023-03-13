::ECHO OFF
::set arg1=%1
::newman run .\DCSA-TNT.postman_collection.json
::newman run .\DCSA-TNT.postman_collection.json
::newman run .\%arg1%
newman run .\DCSA-TNT.postman_collection.json -r htmlextra
PAUSE