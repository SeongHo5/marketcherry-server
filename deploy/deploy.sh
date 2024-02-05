REPOSITORY=~
PROJECT_NAME=cherrymarket-be

echo "> Copy build file"
cp $REPOSITORY/$PROJECT_NAME/build/libs/*SNAPSHOT.jar $REPOSITORY/

echo "> Check current running application PID"
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo "> Current running application PID: $CURRENT_PID"
if [ -z "$CURRENT_PID" ]; then
	echo "> There is no running application, so it will not be terminated."
else
	echo "> kill -15 $CURRENT_PID"
	kill -15 "$CURRENT_PID"
	sleep 5
fi

echo "> Deploy new application"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep *.jar | tail -n 1)

echo "> Jar Name: $JAR_NAME"
nohup java -jar $REPOSITORY/"$JAR_NAME" --spring.profiles.active=prod 2>&1 &
