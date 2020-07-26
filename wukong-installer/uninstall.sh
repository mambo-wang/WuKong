#!/bin/bash

##############################################################
# Program to uninstall athena
# Func Name: uninstall Athena
# Author: wangbao
# Date Created: 2018/4/18 
#------------------------------------------------------------
# Modification History
# DATE                 NAME                    DESC
# 2018/04/27          improve              add some nb script
##############################################################

# check if run as root user
if [ "$(id -u)" -ne 0 ]; then
    echo "you need root privileges to run this script."
    exit 1
fi

# Make sure this shell can run by itself
test -z "$SERVERROOT" && SERVERROOT=/opt/H3C/athena && export SERVERROOT

# uninstall docker-ce
read -p "are you sure to uninstall athena? Please input (yes/no): " yn

if [ "$yn" != "yes" ]; then 
    echo -e "\nuninstall program cancel!\n"
    exit 0
fi

rm -rf /opt/H3C/athena

#do uninstall
echo -e "\nstep 1 : stop containers\n"
athena_containers=$(docker ps -a | grep 'athena' | awk {'print $1'})
for container in $athena_containers
do
    # stop container
    docker stop $container
    # remove container
    docker rm $container
    # remove container files
    rm -rf /var/lib/docker/volumes/$container
done

mysql_container=$(docker ps -a | grep 'mysql-server' | awk {'print $1'})
test -n "$mysql_container" && docker stop $mysql_container
test -n "$mysql_container" && docker rm $mysql_container

echo -e "\nstep 2 : remove docker images\n"
athena_images=$(docker images | grep 'athena' | awk {'print $3'})
for image in $athena_images
do
    docker rmi -f $image
done

mysql_image=$(docker images | grep 'mysql-server' | awk {'print $3'})
test -n "$mysql_image" && docker rmi -f $mysql_image
echo -e "\nuninstall success!"
