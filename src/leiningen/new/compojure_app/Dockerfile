
FROM debian:7.5
MAINTAINER Sébastien Arnaud, sarnaud@opinionlab.com

#
RUN apt-get update
RUN DEBIAN_FRONTEND=noninteractive apt-get -y upgrade

# Install Java & Wget
# RUN DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-7-jre-headless wget

# Setup Oracle
RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee /etc/apt/sources.list.d/webupd8team-java.list
RUN echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886
RUN apt-get update
RUN echo "oracle-java7-installer  shared/accepted-oracle-license-v1-1 boolean true" | debconf-set-selections
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y oracle-java7-installer

# Install build essentials & deps to build customer nginx exec
RUN apt-get install -y wget build-essential zlib1g-dev libpcre3-dev

# Download nginx source, compile and install with clojure plugin
RUN mkdir /src
RUN cd /src && wget http://nginx.org/download/nginx-1.6.0.tar.gz && tar xzf nginx-1.6.0.tar.gz
RUN cd /src/nginx-1.6.0 && wget --no-check-certificate https://github.com/nginx-clojure/nginx-clojure/archive/v0.2.3.tar.gz && tar xzf v0.2.3.tar.gz && mv nginx-clojure-0.2.3 nginx-clojure
RUN cd /src/nginx-1.6.0 && ./configure --add-module=nginx-clojure/src/c && make && make install

# Download latest leiningen version
RUN cd /usr/local/bin && wget --no-check-certificate https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein && chmod +x lein

# Setup env var to prevent warning
ENV LEIN_ROOT true

# Self-install Leiningen
RUN HTTP_CLIENT="wget --no-check-certificate -O" lein

# Compile and install nginx-clojure
RUN cd /src/nginx-1.6.0/nginx-clojure && lein jar
RUN mkdir /usr/local/nginx/jars && cp /src/nginx-1.6.0/nginx-clojure/target/nginx-clojure-0.2.3.jar /usr/local/nginx/

#ADD private/clj-nginx.conf /src/clj-nginx.conf
#RUN cd /src; sed -i "/http {/r clj-nginx.conf" /usr/local/nginx/conf/nginx.conf
#RUN cat /usr/local/nginx/conf/nginx.conf

ADD private/nginx.conf /usr/local/nginx/conf/nginx.conf
ADD target/{{name}}-0.1.0-SNAPSHOT-standalone.jar /usr/local/nginx/jars/{{name}}-0.1.0.jar


EXPOSE 80

CMD ["/usr/local/nginx/sbin/nginx"]
