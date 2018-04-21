#!/bin/bash

PROJECT_HOME=$(pwd)
PROJECT_NAME="server-node"
LAUNCH_DIRECTORY="/var"
NGINX_DIRECTORY_ENABLED="/etc/nginx/sites-enabled"
NGINX_DIRECTORY_AVAILABLE="/etc/nginx/sites-available"
NGINX_FILE_NAME="server-node"
SUPERVISOR_DIRECTORY="/etc/supervisor/conf.d"
SUPERVISOR_FILE_NAME="server-node.conf"

function help {
	echo "Choose one of the following: {publish|clean|run|install}"
	exit 1
}

function install {
	sudo apt-get install supervisor
	sudo apt-get install nginx
}

function clean {
	echo "Cleaning .."
  sudo service supervisor stop &&
  sudo rm -r -f ${SUPERVISOR_DIRECTORY}/${SUPERVISOR_FILE_NAME} &&
  sudo service supervisor start &&
  sudo rm -r -f $LAUNCH_DIRECTORY/$PROJECT_NAME &&
	sudo rm -r -f $NGINX_DIRECTORY_AVAILABLE/$NGINX_FILE_NAME &&
  sudo rm -r -f $NGINX_DIRECTORY_ENABLED/$NGINX_FILE_NAME &&
	sudo service nginx restart
}

function publish {
	clean
	echo "Publishing .."
  cd ${PROJECT_HOME} &&
  cd .. &&
  # Launching
  sudo cp -r ${PROJECT_NAME} ${LAUNCH_DIRECTORY} &&
  cd $LAUNCH_DIRECTORY/$PROJECT_NAME &&
  sudo npm install &&
	sudo cp ${SUPERVISOR_FILE_NAME} ${SUPERVISOR_DIRECTORY} &&
  sudo service supervisor stop &&
  sudo service supervisor start &&
  # Nginx
  sudo cp ${NGINX_FILE_NAME} ${NGINX_DIRECTORY_AVAILABLE} &&
  sudo ln -s ${NGINX_DIRECTORY_AVAILABLE}/${NGINX_FILE_NAME} ${NGINX_DIRECTORY_ENABLED}/${NGINX_FILE_NAME} &&
  sudo service nginx restart
}

function run {
  echo "Running .."
  clean
  cd ${PROJECT_HOME} &&
  npm install &&
  node index.js
}

if [ $# -eq 0 ]
then
	help
fi

for cmd in $@
do

case "$cmd" in

	publish)
		publish
	;;

	clean)
		clean
	;;

	run)
		run
	;;

	install)
		install
	;;

	*)
		help
	;;
esac

done
