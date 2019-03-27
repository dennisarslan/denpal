docker kill $(docker ps | grep denkins | awk '{ print $1 }')
docker run -d -p 8080:8080 -p 50000:50000 -v /tmp/jenkins_lts:/var/jenkins_home denkins:latest
