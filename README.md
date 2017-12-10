# 2D MMORPG Client

[![Build Status](https://travis-ci.org/2D-MMORPG/client.svg?branch=master)](https://travis-ci.org/2D-MMORPG/client)
[![Waffle.io - Columns and their card count](https://badge.waffle.io/2D-MMORPG/planning.svg?columns=all)](https://waffle.io/2D-MMORPG/planning) 
[![Lines of Code](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-client&metric=ncloc)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-client) 
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.jukusoft.mmo%3Ammorpg-client)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-client) 
[![Coverage](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-client&metric=coverage)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-client) 
[![Technical Debt Rating](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-client&metric=sqale_debt_ratio)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-client) 
[![Security Rating](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-client&metric=new_security_rating)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-client) 
\
2D MMORPG client

## Supported platforms

  - Windows
  - Linux

**Mac OS isn't supported officially!**

## HowTo: Translate

First download [PoEdit](https://poedit.net/).\
https://github.com/awkay/easy-i18n/wiki/Gettext-tutorial
https://github.com/awkay/easy-i18n/wiki/Getting-Started

## Run Sonarqube

```bash
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=2d-mmorpg -Dsonar.login=<SONAR_TOKEN>
```

Replace "<SONAR_TOKEN>" with sonarcloud token.

## Sonarqube Badges

You can find sonarqube badges here:\
https://github.com/QualInsight/qualinsight-plugins-sonarqube-badges/wiki/Measure-badges