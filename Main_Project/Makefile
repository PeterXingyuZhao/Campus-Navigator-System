runServer: WebApp.class
	java WebApp
runTests: FrontendTests.class
	java -jar ../junit5.jar —class-path=. —select-class=FrontendTests	
WebApp.class: WebApp.java
	javac WebApp.java
FrontendTests.class: FrontendTests.java
	javac -cp .:../junit5.jar FrontendTests.java
clean: rm -f *.class
