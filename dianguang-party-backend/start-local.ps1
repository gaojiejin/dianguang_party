Set-Location $PSScriptRoot
mvn -gs scripts/maven-settings.xml -s scripts/maven-settings.xml spring-boot:run
