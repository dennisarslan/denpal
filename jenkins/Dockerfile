FROM jenkins/jenkins:lts

USER root
VOLUME /var/jenkins_home/.ssh

# Installing bunch of packages in a single Docker filesystem layer and cleaning up after ourselves
# Todo: Actually be more effective with file system layers
RUN apt-get update
RUN apt-get install -y openssh-server net-tools inetutils-ping ansible python-pip rubygems
RUN gem install serverspec
RUN pip install ansible

# Install Docker
RUN apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common

RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -

RUN add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/debian \
   $(lsb_release -cs) \
   stable"

RUN apt-get update
RUN apt-get install -y docker-ce docker-ce-cli containerd.io
RUN rm -rf /var/lib/apt/lists/*

# Installing some nice tools
RUN gem install serverspec
RUN pip install ansible

# Run Jenkins with limited permissions
USER jenkins

#RUN chmod 400 /var/jenkins_home/.ssh
#RUN chmod 600 /var/jenkins_home/.ssh/id_rsa
