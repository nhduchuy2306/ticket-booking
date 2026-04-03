FROM jenkins/jenkins:lts-jdk21

USER root

# Install tools needed by pipeline steps: Git + Docker CLI + Compose + curl
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        ca-certificates \
        curl \
        git \
        maven \
        docker.io \
    && rm -rf /var/lib/apt/lists/*

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

USER jenkins

