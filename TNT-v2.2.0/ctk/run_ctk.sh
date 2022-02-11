#!/bin/bash
java -jar DCSA-Validator-Toolkit.jar $TEST_SUITE
while true
do
	# To stop retry.
	sleep 30
done
exit 0