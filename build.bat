@echo off
if not exist "build" mkdir build
javac -d build schema\*.java server\*.java client\*.java
echo Compilation complete.
