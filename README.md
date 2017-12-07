# 2D MMORPG Client

[![Build Status](https://travis-ci.org/2D-MMORPG/client.svg?branch=master)](https://travis-ci.org/2D-MMORPG/client)
[![Waffle.io - Columns and their card count](https://badge.waffle.io/2D-MMORPG/planning.svg?columns=all)](https://waffle.io/2D-MMORPG/planning) 
\
2D MMORPG client

## HowTo: Translate

First download [PoEdit](https://poedit.net/).\
https://github.com/awkay/easy-i18n/wiki/Gettext-tutorial
https://github.com/awkay/easy-i18n/wiki/Getting-Started

## Run Sonarqube

```bash
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=2d-mmorpg -Dsonar.login=<SONAR_TOKEN>
```

Replace "<SONAR_TOKEN>" with sonarcloud token.