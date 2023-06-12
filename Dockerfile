FROM azul/zulu-openjdk:17-latest
LABEL maintainer="contact@devopscube.com"
COPY build/distributions/sol-listener-1.0.tar /
RUN tar -xvf sol-listener-1.0.tar
CMD ["sol-listener-1.0/bin/sol-listener"]