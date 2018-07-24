#!/bin/sh

sudo ln -s /usr/local/virtuoso-opensource/bin/isql-v /usr/local/virtuoso-opensource/bin/isql

rm virtuoso_run.log 2> /dev/null
cp virtuoso_vos.ini.template virtuoso.ini

echo $(date +%H:%M:%S.%N | cut -b1-12)" : Starting OpenLink Virtuoso Universal Server..."
virtuoso-t -f > /sparql-snb/virtuoso_run.log 2>&1 &
seconds_passed=0

# wait until virtuoso is ready
echo $(date +%H:%M:%S.%N | cut -b1-12)" : Waiting until Virtuoso Server is online..."
until grep -m 1 "Server online at 1111" /sparql-snb/virtuoso_run.log
do
  sleep 1
  seconds_passed=$((seconds_passed+1))
  echo $seconds_passed >> out.txt
  if [ $seconds_passed -gt 120 ]; then
    echo $(date +%H:%M:%S.%N | cut -b1-12)" : Could not start Virtuoso Server. Timeout: [2 min]"
    break
  fi
done
echo $(date +%H:%M:%S.%N | cut -b1-12)" : Virtuoso Server started successfully."

export LC_ALL=C.UTF-8

# run the system adapter
echo $(date +%H:%M:%S.%N | cut -b1-12)" : Running the System adapter..."
java -cp DataStorageBenchmark.jar org.hobbit.core.run.ComponentStarter1 org.hobbit.sparql_snb.systems.VirtuosoSysAda
