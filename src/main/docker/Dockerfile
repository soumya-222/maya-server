FROM itom-docker.orgartifactory.swinfra.net:443/shared/opensuse-base:15.5-002

USER root
LABEL authors="Rajiv"

#ENV http_proxy="${HTTP_PROXY:-http://web-proxy.us.softwaregrp.net:8080}" https_proxy="${HTTPS_PROXY:-http://web-proxy.us.softwaregrp.net:8080}"

ENV MAYA_HOME="/usr/local/opentext/maya" \
	AWS_BILLS="/mnt/aws-bills" \
	ACCOUNT_CONFIG="/mnt/account-config" \
	ACCOUNT_DATA="/mnt/account-data" \
	RECOMMENDATION_CONFIG="/mnt/recommendation-config" \
	RECOMMENDATION_DATA="/mnt/recommendation-data" \
	AZURE_BILLS="/mnt/azure-bills" \
	MAYA_LOGS="/tmp/maya/logs" \
	MAYA_BYOD_AWS="/mnt/byod/aws" \
	MAYA_BYOD_AZURE="/mnt/byod/azure" \
	MAYA_BYOD_GCP="/mnt/byod/gcp"

RUN mkdir -p ${MAYA_HOME} && \
	mkdir -p ${MAYA_HOME}/resources && \
    mkdir -p ${AWS_BILLS} && \
	mkdir -p ${ACCOUNT_CONFIG} && \
	mkdir -p ${ACCOUNT_DATA} && \
	mkdir -p ${RECOMMENDATION_CONFIG} && \
	mkdir -p ${RECOMMENDATION_DATA} && \
	mkdir -p ${AZURE_BILLS} && \
    mkdir -p ${MAYA_BYOD_AWS} && \
    mkdir -p ${MAYA_BYOD_AZURE} && \
    mkdir -p ${MAYA_BYOD_GCP} && \
	mkdir -p ${MAYA_LOGS}

# Adding AZUL Repo
RUN curl -O https://cdn.azul.com/zulu/bin/zulu-repo-1.0.0-1.noarch.rpm; \
    rpm --import https://www.azul.com/wp-content/uploads/2021/05/0xB1998361219BD9C9.txt; \
    zypper -qn install zulu-repo-1.0.0-1.noarch.rpm

# Installing Java Dependencies
RUN zypper -qn install zulu17-jdk zip; \
    java -version

ENV JAVA_HOME="/usr/lib/jvm/zulu17"

COPY @project.build.finalName@.jar ${MAYA_HOME}/maya-server.jar

RUN chmod +x ${MAYA_HOME}/*

# Resetting ENV
ENV http_proxy="" https_proxy=""

EXPOSE 9994
WORKDIR ${MAYA_HOME}
ENTRYPOINT ["java","-jar","maya-server.jar"]
