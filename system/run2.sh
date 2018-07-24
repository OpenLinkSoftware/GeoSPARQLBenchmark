#!/bin/sh

cp /sparql-snb/virtuoso-mocha.lic /etc/oplmgr/virtuoso.lic
sudo ln -s /lib/x86_64-linux-gnu/libncurses.so.5.9 /lib/x86_64-linux-gnu/libtermcap.so.2
cd /opt/virtuoso/database
cp /sparql-snb/virtuoso.ini.template virtuoso.ini
rm /opt/virtuoso/database/virtuoso.lck
bash /opt/virtuoso/install/command-oplmgr.sh start
bash virtuoso-start.sh

cd /sparql-snb
java -cp DataStorageBenchmark.jar org.hobbit.core.run.ComponentStarter1 org.hobbit.sparql_snb.systems.VirtuosoSysAda
