mBuild:
	mvn clean -Dmaven.test.skip  package -e
mDocker:
	docker-compose -up -d --build