# Version 0.1
# 基础镜像
FROM harbor.nti56.com/iwms/openjdk-font:8
# 维护者信息
MAINTAINER NTI rjtest@nti56.com
VOLUME /tmp
# 拷贝相关的jar包
ADD target/*.jar imes.jar
# for openjdk awt font solution  安装fontconfig库，处理NPE问题
# RUN apk update && apk install fontconfig -y && apk install --fix-broken -y
# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
EXPOSE  8081
ENTRYPOINT ["java","-Dspring.profiles.active=test","-Djava.security.egd=file:/dev/./urandom","-jar","/imes.jar",">/log/app.log"]

