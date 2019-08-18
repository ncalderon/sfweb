#!/usr/bin/env bash
#scp -o "StrictHostKeyChecking no" -i ./id_rsa ./target/sf-web-client-0.0.1-SNAPSHOT.war root@45.77.160.68:/opt/sf/sf.war
#ssh -F ./config calderon 'systemctl stop sf-web
#mv /opt/sf/sf-web.war /opt/sf/backup/sf-web.war
#mv /opt/sf/sf.war /opt/sf/sf-web.war
#systemctl start sf-web'
#./mvnw -Pprod package -DskipTests=true -Dmaven.javadoc.skip=true -B -V -q com.google.cloud.tools:jib-maven-plugin:dockerBuild
#docker save -o sfweb sfweb
#scp sfweb calderon:~/docker-images/sfweb
#ls -la ~/.ssh
#cd ~/.ssh
#cat config
./mvnw -Pprod package -DskipTests=true -Dmaven.javadoc.skip=true -B -V -q com.google.cloud.tools:jib-maven-plugin:dockerBuild
docker save -o sfweb sfweb
sshpass -f ~/.ssh/sshpass scp sfweb travisci@calderonlabs.com:~/docker-images/sfweb/
sshpass -f ~/.ssh/sshpass ssh travisci@calderonlabs.com 'docker load -i ~/docker-images/sfweb/sfweb
docker-compose -f ~/docker-images/sfweb/app.yml up -d --force-recreate
docker rmi $(docker images -f "dangling=true" -q)
'
#ssh -o "StrictHostKeyChecking no" -i ~/.ssh/id_rsa travisci@calderonlabs.com 'docker ps'
#ssh calderon 'docker load -i ~/docker-images/sfweb/sfweb
#docker-compose -f ~/docker-images/sfweb/app.yml up -d --force-recreate'
exit 0
