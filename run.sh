#!/bin/bash
 
 echo $API_ROOT_URI
 echo $TEST_DATA_FILE
# while true
#do
	#echo "Press [CTRL+C] to stop.."
#	sleep 1
#done
java -jar DCSA-Validator-Toolkit.jar suitxmls/$TEST_SUITE
echo $?

