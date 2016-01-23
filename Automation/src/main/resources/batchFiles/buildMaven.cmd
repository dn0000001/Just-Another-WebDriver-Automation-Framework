cmd /c mvn -Dmaven.repo.local=../repository -o -B -f pom.xml clean
cmd /c mvn -Dmaven.repo.local=../repository -o -B -f pom.xml compile

:: Plugin is not using given output folder always outputs to root of target
cmd /c mvn -B -f pom.xml dependency:copy-dependencies
pause