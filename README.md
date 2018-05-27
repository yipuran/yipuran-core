# yipuran-core
Java core application framework
#### 目的
頻繁に利用される基盤に近い処理をかき集めたパッケージで再利用が目的
Throwable なラムダ、GenericBuilder　など広範囲ではある。各機能は Javadoc を参照

## Document
Extract doc/yipuran-core-doc.zip and see the Javadoc
or [Wiki Page](../../wiki)

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
        <version>4.0</version>
    </dependency>
</dependencies>

```
