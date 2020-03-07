CURRENTDIR=$(cd `dirname $0`;pwd)
mysql -uroot -p$MYSQL_ROOT_PASSWORD << EOF
source $CURRENTDIR/upgrade.sql
EOF

