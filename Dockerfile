# First stage, build the custom JRE
FROM eclipse-temurin:21-jdk-alpine AS jre-builder

# Install binutils, required by jlink
RUN apk update &&  \
    apk add binutils

# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /optimized-jdk-21

# Second stage, Use the custom JRE and build the app image
FROM alpine:latest

# Needed to fix 'Fontconfig warning: ignoring C.UTF-8: not a valid language tag'
ENV LANG en_US.UTF-8

# JRE fails to load fonts if there are no standard fonts in the image; DejaVu is a good choice,
# see https://github.com/docker-library/openjdk/issues/73#issuecomment-207816707
RUN apk add --no-cache fontconfig ttf-liberation

ENV JAVA_HOME=/opt/jdk/jdk-21
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=jre-builder /optimized-jdk-21 $JAVA_HOME

# Add app user
ARG APPLICATION_USER=spring

# Create a user to run the application, don't run as root
RUN addgroup --system $APPLICATION_USER &&  adduser --system $APPLICATION_USER --ingroup $APPLICATION_USER

# Create the application directory
RUN mkdir /app && chown -R $APPLICATION_USER /app

COPY --chown=$APPLICATION_USER:$APPLICATION_USER target/*.jar /app/app.jar

WORKDIR /app

USER $APPLICATION_USER

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]