#!/bin/bash
BUILD_JAR=$(ls /home/ubuntu/WanderHub_Backend/build/libs/server-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)

echo "> 현 재 시 간: $(date) " >> /home/ubuntu/wanderHub-back/log/deploy.log
echo "> build 파일명: $JAR_NAME " >> /home/ubuntu/wanderHub-back/log/deploy.log
echo "> build 파일 복사" >> /home/ubuntu/wanderHub-back/log/deploy.log
DEPLOY_PATH=/home/ubuntu/wanderHub-back/
cp $BUILD_JAR $DEPLOY_PATH


echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/wanderHub-back/log/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/wanderHub-back/log/deploy.log
else
  echo "> kill -9 $CURRENT_PID" >> /home/ubuntu/wanderHub-back/log/deploy.log
  sudo kill -9 $CURRENT_PID
  sleep 5
fi


echo "> DEPLOY_JAR 배포 " >> /home/ubuntu/wanderHub-back/log/deploy.log

sudo nohup java -jar -Dspring.profiles.active=prod $BUILD_JAR >> /home/ubuntu/wanderHub-back/log/deploy.log 2>/home/ubuntu/wanderHub-back/log/deploy_err.log &