# fhh-bot
========================== 

!!! может быть проблема с путем /var/jenkins_home - нужно поменять его владельца sudo chown -R jenkins:jenkins /var/jenkins_home

!!! у агента jenkins/ssh-agent и сервера jenkins/jenkins:lts разные домашние пути  /home/jenkins и /var/jenkins_home

========================== мастер

docker run -p 8080:8080 -p 50000:50000 -d -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts

 в настройках агента на сервере выставить:
 креды пользователя - jenkins, секретный ключ вязять у пользователя на сервере
 порт - 2200
 путь рабочего каталога - /home/jenkins
 путь для jvm - /opt/java/openjdk/bin/java

========================== агент

docker run -d --name=jenkins-agent-ssh --publish 2200:22 -e "JENKINS_AGENT_SSH_PUBKEY=<public_key_from_server>" \
 -e "JENKINS_URL=http://00.000.000.000:8080" \
 -e "JENKINS_AGENT_NAME=ssh-agent" \
 -v /var/run/docker.sock:/var/run/docker.sock \
 -v jenkins_home:/home/jenkins \
 jenkins/ssh-agent
