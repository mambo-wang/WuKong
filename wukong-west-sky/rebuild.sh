# rebuild athena  with jenkins
# w14014
# History:
# 2018/04/24 w14014 First release

PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

CURRENTDIR=$(cd `dirname $0`;pwd)
echo -e "\nstart to rebuild athena, the rebuild shell script at ${CURRENTDIR}"
echo -e "\nstep1: docker-compose down\n"
cd ${CURRENTDIR}/athena_package
docker-compose -f docker-compose.yml down

echo -e "\nstep2: docker rmi images\n"
docker rmi -f $(docker images | grep 'athena' | awk {'print $3'})

echo -e "\nstep3: docker-compose up\n"
docker-compose -f docker-compose.yml up -d

echo -e "\nrebuild athena success, wait a minute , the service is starting...\n"
