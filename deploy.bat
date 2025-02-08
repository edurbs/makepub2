del build\libs\*.jar
call gradlew.bat bootjar
docker build -t makepub:latest .
docker tag makepub:latest edurbs/makepub:latest
docker push edurbs/makepub:latest
c:\Windows\System32\OpenSSH\ssh root@192.168.21.201 '/root/deployMakepub.sh'