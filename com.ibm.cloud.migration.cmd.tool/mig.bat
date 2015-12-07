@echo off 
REM ###########################################################################
REM #
REM # Licensed Materials - Property of IBM
REM #
REM # (C) COPYRIGHT International Business Machines Corp. 2009
REM #
REM # All Rights Reserved.
REM #
REM # US Government Users Restricted Rights - Use, duplication or
REM # disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
REM #
REM #############################################################################
if not exist "%JAVA_HOME%\bin\java.exe" goto showhelp
	SET CL_CLASSPATH=.;%cmd_path%\lib\aopalliance-1.0.jar;%cmd_path%\lib\appmodel.jar;%cmd_path%\lib\cloudfoundry-client-lib-0.8.7.BUILD-SNAPSHOT-sources.jar;%cmd_path%\lib\cloudfoundry-client-lib-0.8.7.BUILD-SNAPSHOT.jar;%cmd_path%\lib\cmdtool.jar;%cmd_path%\lib\commons-cli-1.2.jar;%cmd_path%\lib\commons-codec-1.4.jar;%cmd_path%\lib\commons-collections-3.2.1.jar;%cmd_path%\lib\commons-httpclient-3.1.jar;%cmd_path%\lib\commons-io-2.1.jar;%cmd_path%\lib\commons-lang-2.4.jar;%cmd_path%\lib\commons-logging-1.0.4.jar;%cmd_path%\lib\composer.jar;%cmd_path%\lib\dom4j-1.6.jar;%cmd_path%\lib\hamcrest-all-1.1.jar;%cmd_path%\lib\hamcrest-core-1.1.jar;%cmd_path%\lib\heroku-api-0.15-SNAPSHOT.jar;%cmd_path%\lib\heroku-http-apache-0.15-SNAPSHOT.jar;%cmd_path%\lib\heroku-json-jackson-0.15-SNAPSHOT.jar;%cmd_path%\lib\httpclient-4.1.2.jar;%cmd_path%\lib\httpcore-4.1.2.jar;%cmd_path%\lib\jackson-core-asl-1.9.10.jar;%cmd_path%\lib\jackson-mapper-asl-1.9.10.jar;%cmd_path%\lib\jaxen-1.1.6.jar;%cmd_path%\lib\jdom-2.0.5.jar;%cmd_path%\lib\json.jar;%cmd_path%\lib\junit-4.10.jar;%cmd_path%\lib\log4j-1.2.14.jar;%cmd_path%\lib\mockito-core-1.9.5.jar;%cmd_path%\lib\objenesis-1.0.jar;%cmd_path%\lib\spring-aop-3.0.7.RELEASE.jar;%cmd_path%\lib\spring-asm-3.0.7.RELEASE.jar;%cmd_path%\lib\spring-beans-3.0.7.RELEASE.jar;%cmd_path%\lib\spring-context-3.0.7.RELEASE.jar;%cmd_path%\lib\spring-context-support-3.0.7.RELEASE.jar;%cmd_path%\lib\spring-core-3.0.7.RELEASE.jar;%cmd_path%\lib\spring-expression-3.0.7.RELEASE.jar;%cmd_path%\lib\spring-security-config-3.1.3.RELEASE.jar;%cmd_path%\lib\spring-security-core-3.1.3.RELEASE.jar;%cmd_path%\lib\spring-security-crypto-3.1.0.RELEASE.jar;%cmd_path%\lib\spring-security-oauth2-1.0.0.RC1.jar;%cmd_path%\lib\spring-security-web-3.1.3.RELEASE.jar;%cmd_path%\lib\spring-test-3.0.7.RELEASE.jar;%cmd_path%\lib\spring-web-3.0.7.RELEASE.jar;%cmd_path%\lib\spring-webmvc-3.0.7.RELEASE.jar;%cmd_path%\lib\velocity-1.6.jar;%cmd_path%\lib\xercesImpl.jar;%cmd_path%\lib\xml-apis.jar;%cmd_path%\lib\zip4j_1.3.1.jar
	call "%JAVA_HOME%\bin\java" -cp %CL_CLASSPATH% com.ibm.cloud.migration.cmd.tool.Command -action %*
	goto end

:showhelp
	echo You don't have JAVA installed or the environment variable JAVA_HOME is not set correctly. Please install Java version version 1.6.x and set the JAVA_HOME environment variable if it's already installed. 
	
:end