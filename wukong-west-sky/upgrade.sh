#!/bin/bash

###################################################################
# Func Name: upgrade WuKong
# Author wangbao 
# Date Created: 2018/04/25 First release
#-----------------------------------------------------------------
# Modification History
# DATE                        NAME                   DESC
# 2018/04/27                logs add             add some nb script
###################################################################
# check if run as root user
if [ "$(id -u)" -ne 0 ]; then
    echo "you need root privileges to run this script."
    exit 1
fi

test -z "$SERVERROOT" && SERVERROOT=/opt/H3C/athena && export SERVERROOT
test -z "$LOGFILE" && LOGFILE=/var/log/athena/upgrade.log && export LOGFILE 

echo -e "\nstart to upgrade athena, the upgrade shell script at ${CURRENTDIR}"
# current directory
CURRENTDIR=$(cd `dirname $0`;pwd)
# update files to server root dir
rm -f $SERVERROOT/*.sh
rm -rf $SERVERROOT/athena_package
rm -rf $SERVERROOT/upgrade
cp ${CURRENTDIR}/*.sh $SERVERROOT
cp -r ${CURRENTDIR}/athena_package/ $SERVERROOT
cp -r ${CURRENTDIR}/upgrade $SERVERROOT

# the dir which have upgrade scripts
upgradesqldir=${SERVERROOT}/upgrade/mysql-server

# query current installed athena version, then decide to install or upgrade
installed_version=$(docker images | grep 'athena-webapp' | awk {'print $2'})
echo "installed_version is ${installed_version}"

#1.0-E0101 to number like 100101
installed_version_number=$(echo -n "${installed_version}" | sed 's/[^0-9]//g')
echo "installed_version_number is ${installed_version_number}"

# an array like ("1.0-E0102" "1.0-E0102H01" "2.0-E0202")
upgrade_sql_dir_array=$(ls $upgradesqldir | grep '-')

#sort the array
sql_arr_length=${#upgrade_sql_dir_array[@]}
echo "sql_arr_length is ${sql_arr_length}"

for(( i = 0; i < $sql_arr_length; i++ )); do
  for(( j = i+1; j < $sql_arr_length; j++ )); do
    val_i=$(echo -n ${upgrade_sql_dir_array[i]} | sed 's/[^0-9]//g')
    val_j=$(echo -n ${upgrade_sql_dir_array[j]} | sed 's/[^0-9]//g')
    echo "val_i is ${val_i}, and val_j is ${val_j}"
    if [ "$val_i" \> "$val_j" ]; then
      temp=${upgrade_sql_dir_array[i]}
      upgrade_sql_dir_array[i]=${upgrade_sql_dir_array[j]}
      upgrade_sql_dir_array[j]=$temp
    fi
  done
done

for sql_dir in ${upgrade_sql_dir_array[@]};do
  sql_dir_number=$(echo -n "${sql_dir}" | sed 's/[^0-9]//g')
  echo "sql_dir is ${sql_dir} and sql_dir_number is ${sql_dir_number}"
  # compare installed_version and support version, do upgrade only once
  cd ${SERVERROOT}/athena_package/
  if [ "$sql_dir_number" \> "$installed_version_number" ] && [ -f "$upgradesqldir/$sql_dir/upgrade.sql" ]; then
        echo -e "\nstep1: copy upgrade script in $sql_dir  into container\n"
        docker-compose -f docker-compose-base.yml exec mysql-server mkdir -p /usr/local/work/$sql_dir
        docker cp $upgradesqldir/$sql_dir/upgrade.sql mysql-server:/usr/local/work/$sql_dir
        docker cp $upgradesqldir/$sql_dir/upgrade.sh mysql-server:/usr/local/work/$sql_dir

        echo -e "\nstep2: execute script in container\n"
        docker-compose -f docker-compose-base.yml exec mysql-server bash /usr/local/work/$sql_dir/upgrade.sh
    fi
done

echo -e "\nstep3: rebuild athena\n"
docker-compose -f docker-compose.yml down
docker rmi -f $(docker images | grep 'athena' | awk {'print $3'})
docker-compose -f docker-compose.yml up -d

echo -e "\nupgrade successful, please wait a minute, the services is starting...\n"
