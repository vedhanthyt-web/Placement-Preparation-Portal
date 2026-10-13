#!/bin/bash
# ===================================================================
# Placement Preparation Portal - Quick Start Launcher
# ===================================================================

echo "=========================================================="
echo " Starting Placement Preparation Portal (Spring Boot)"
echo "=========================================================="

# Check for custom local tool paths if system Java is not in PATH
if ! command -v java &> /dev/null; then
    if [ -d "$HOME/.tools" ]; then
        ZULU_DIR=$(ls -d $HOME/.tools/zulu17* 2>/dev/null | head -n 1)
        if [ -n "$ZULU_DIR" ]; then
            export JAVA_HOME="$ZULU_DIR"
            export PATH="$JAVA_HOME/bin:$HOME/.tools/apache-maven-3.9.6/bin:$PATH"
        fi
    fi
fi

if ! command -v java &> /dev/null; then
    echo "Error: Java 17 or higher is required to run this project."
    echo "Please install Java 17 and try again."
    exit 1
fi

echo "Using Java: $(java -version 2>&1 | head -n 1)"

# Check if JAR exists
if [ ! -f "target/placement-portal-1.0.0.jar" ]; then
    echo "Building application JAR..."
    mvn package -DskipTests
fi

echo "Launching application on http://localhost:8080 ..."
java -jar target/placement-portal-1.0.0.jar
