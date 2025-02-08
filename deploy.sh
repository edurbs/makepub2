rm target/*.jar
./mvnw clean package -Pproduction
docker build -t makepub:latest .
docker tag makepub:latest edurbs/makepub:latest
docker push edurbs/makepub:latest
ssh root@192.168.21.201 '/root/deployMakepub.sh'