# yipuran-core
Java core application framework
#### 目的
頻繁に利用される基盤に近い処理をかき集めたパッケージで再利用が目的
Throwable なラムダ、GenericBuilder　など広範囲ではある。各機能は Javadoc を参照

**（注意）バージョン 4.19 より、CSVの処理は、[yipuran-csv](https://github.com/yipuran/yipuran-csv) に分岐し、yipuran-core からは消滅します。**

## Document
[Wiki Page](../../wiki)

## Setup pom.xml
```
<repositories>
   <repository>
      <id>yipuran-core</id>
      <url>https://raw.github.com/yipuran/yipuran-core/mvn-repo</url>
   </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.yipuran.core</groupId>
        <artifactId>yipuran-core</artifactId>
        <version>4.22</version>
    </dependency>
</dependencies>

```


## Setup gradle
```
repositories {
    mavebCentral()
    maven { url 'https://raw.github.com/yipuran/yipuran-core/mvn-repo'  }
}

dependencied {
    compile 'org.yipuran.core:yipuran-core:4.22'
}
```
