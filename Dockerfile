FROM tomcat:11.0
RUN rm -rf /usr/local/tomcat/webapps/*
COPY NPP-QUEST.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080