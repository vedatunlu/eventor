#!/bin/bash
# Eventry Quick Setup Script

echo "🚀 Setting up Eventry Spring Event Generator..."

# Check Java version
echo "Checking Java version..."
if command -v java &> /dev/null; then
    java_version=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}')
    echo "Java version: $java_version"

    # Extract major version number (works for both 1.8.x and 17.x formats)
    if [[ $java_version == 1.* ]]; then
        major_version=$(echo $java_version | awk -F '.' '{print $2}')
    else
        major_version=$(echo $java_version | awk -F '.' '{print $1}')
    fi

    if [ "$major_version" -lt 17 ]; then
        echo "❌ Error: Java 17+ required for Spring Boot 3+"
        echo "   Current version: $java_version (major: $major_version)"
        echo "💡 Install Java 17+ using:"
        echo "   - SDKMAN: sdk install java 17.0.8-tem"
        echo "   - Homebrew: brew install openjdk@17"
        echo "   - Or download from: https://adoptium.net/"
        exit 1
    fi
    echo "✅ Java version compatible (major: $major_version)"
else
    echo "❌ Error: Java not found in PATH"
    echo "💡 Install Java 17+ and ensure it's in your PATH"
    exit 1
fi

# Check Maven version
echo "Checking Maven version..."
if command -v mvn &> /dev/null; then
    maven_version=$(mvn -version 2>&1 | head -n 1 | awk '{print $3}')
    echo "Maven version: $maven_version"

    # Extract major.minor version for comparison
    maven_major=$(echo $maven_version | awk -F '.' '{print $1}')
    maven_minor=$(echo $maven_version | awk -F '.' '{print $2}')

    if [ "$maven_major" -lt 3 ] || ([ "$maven_major" -eq 3 ] && [ "$maven_minor" -lt 6 ]); then
        echo "❌ Warning: Maven 3.6.3+ recommended. Current: $maven_version"
        echo "💡 Consider upgrading Maven for best compatibility"
    else
        echo "✅ Maven version compatible"
    fi
else
    echo "❌ Error: Maven not found. Please install Maven 3.6.3+"
    echo "💡 Install Maven using:"
    echo "   - SDKMAN: sdk install maven"
    echo "   - Homebrew: brew install maven"
    echo "   - Or download from: https://maven.apache.org/download.cgi"
    exit 1
fi

echo "✅ Prerequisites checked"

# Build and install Eventry
echo "📦 Building and installing Eventry..."
echo "This may take a few minutes for the first run..."

mvn clean install -q

if [ $? -eq 0 ]; then
    echo "✅ Eventry installed successfully!"
    echo ""
    echo "🎯 Next steps:"
    echo "1. Add the plugin to your Spring Boot project's pom.xml:"
    echo "   <plugin>"
    echo "     <groupId>io.eventor</groupId>"
    echo "     <artifactId>eventor-maven-plugin</artifactId>"
    echo "     <version>0.1.0-SNAPSHOT</version>"
    echo "   </plugin>"
    echo ""
    echo "2. Create JSON definitions in src/main/resources/eventry/"
    echo "3. Run: mvn clean compile"
    echo ""
    echo "📖 Full guide: https://github.com/yourorg/eventry/blob/main/SPRING_BOOT_3_INTEGRATION.md"
    echo "🆘 Troubleshooting: https://github.com/yourorg/eventry/blob/main/TROUBLESHOOTING.md"
else
    echo "❌ Build failed. Check the output above for errors."
    echo "🆘 See TROUBLESHOOTING.md for common build issues"
    exit 1
fi
