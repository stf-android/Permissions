# Permissions
[详细介绍，请移步](https://www.jianshu.com/p/891207f69ebe)

远程依赖
1.gradle
```

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

dependencies {
	        implementation 'com.github.stf-android:Permissions:-SNAPSHOT'
	}
```
2.maven
```
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

     <dependency>
	    <groupId>com.github.stf-android</groupId>
	    <artifactId>Permissions</artifactId>
	    <version>-SNAPSHOT</version>
	</dependency>
```
