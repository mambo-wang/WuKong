#!/bin/bash

###########################################################
# Program : wukong
# Func Name: Install or upgrade Athena
# USER: wangbao
# Date Created: 2018/04/18 First release
# ---------------------------------------------------------
# Modification History
# DATE           NAME          DESC
# 2018/04/27   improve       add some nb script
# 2018/05/14   fit           fit ubuntu12.04  
###########################################################

# check if run as root user
if [ "$(id -u)" -ne 0 ]; then
    echo "you need root privileges to run this script."
    exit 1
fi

# test if execute this by bash, not support sh install.sh
declare -a test_bash_array >/dev/null 2>&1
if [ "$?" -ne 0 ]; then
    echo "Can not run this file by 'sh', you need to run it by:"
    echo "    chmod +x $0 && ./$0"
    echo "  or"
    echo "    bash $0"
    exit 1
fi

# current directory
CURRENTDIR=$(cd `dirname $0`;pwd)

# query current installed athena version, then decide to install or upgrade
declare currentversion=$(docker images | grep athena-webapp | awk {'print $2'})

if [ -n "${currentversion}" ];then
    echo -e "current version is ${currentversion}\n" | tee -a ${LOGFILE}
    echo "need upgrade" | tee -a ${LOGFILE}
    bash ${CURRENTDIR}/upgrade.sh
    exit 0
fi

test -z "$SERVERROOT" && SERVERROOT=/opt/H3C/athena && export SERVERROOT && mkdir -p /opt/H3C/athena
test -z "$LOGFILE" && LOGFILE=/var/log/athena/install.log && export LOGFILE && mkdir -p /var/log/athena
# copy files to server root dir
cp ${CURRENTDIR}/*.sh $SERVERROOT
cp -r ${CURRENTDIR}/properties $SERVERROOT
cp -r ${CURRENTDIR}/athena_package/ $SERVERROOT

# if currentversion is empty , the athena is never be installed 
echo "the athena is nerver be installed, need install" | tee -a ${LOGFILE}
echo -e "\nstart to install athena, the install shell script at ${CURRENTDIR}" | tee -a ${LOGFILE}

service docker status > /dev/null 2>&1
    
if [ "$?" -ne 0 ]; then
    echo -e "\nstep1: install docker-ce\n" | tee -a ${LOGFILE}
    cd ${CURRENTDIR}/docker-install/
    bash install.sh
    cd ${CURRENTDIR}
    echo -e "\ninstall docker-ce success: $(docker -v)" | tee -a ${LOGFILE}
fi

echo -e "\nstep2: install docker-compose\n" | tee -a ${LOGFILE}
bash ${CURRENTDIR}/install_docker_compose/install.sh
echo -e "\ninstall docker-compose success: $(docker-compose --version)" | tee -a ${LOGFILE}

echo -e "\nstep3: install base docker images\n" | tee -a ${LOGFILE}
for tar in $(ls ${CURRENTDIR}/from_tar)
do
    docker load --input ${CURRENTDIR}/from_tar/$tar | tee -a ${LOGFILE}
done
echo -e "\ninstall base image success: $(docker images)" | tee -a ${LOGFILE}

echo -e "\nstep4: install athena v1.0\n" | tee -a ${LOGFILE}
cd ${SERVERROOT}/athena_package/
docker-compose -f docker-compose-base.yml up -d | tee -a ${LOGFILE}
docker-compose -f docker-compose.yml up -d | tee -a ${LOGFILE}
docker-compose -f docker-compose-nextcloud.yml up -d | tee -a ${LOGFILE}
echo -e "\ninstall athena success, please wait a minute , the containers are starting...\n" | tee -a ${LOGFILE}
