# Raven
**Raven initializes class's fields with values provided in an external text file.**

<p>
Java tool which allows to initialize class with values stored in a text file.
Values in a text file are refered from classes based on field annotations. 
It is useful where there is necessity to parse huge number of files with different structure.
</p>

* [Getting started](#getting-started)
* [Text files](#text-files)
* [Parsing list of values](#parsing-list-of-values)
* [Parsers](#parsers)
* [Using parsers for other types than Strings](#using-parsers-for-other-types-than-strings)
* [Motivation](#motivation)
* [Installation](#installation)

## Getting started

######1. Annotate your class
<p>
Annotation <code>@Parsable</code> denotes that field should be initialized. 
<code>col</code> and <code>row</code> values point to position in a text file.
<code>parser</code> is necessary if field has to be parsed from String to other type.
</p>

```java
public class Network {
    
	@Parsable(col = 0, row = 0)
	private String name;
	
    @Parsable(col = 0, row = 1, parser = IntegerParser.class)
    private int nrOfNodes;
    
    @Parsable(col = 1, row = 1, parser = IntegerParser.class)
    private int nrOfEdges;
    
    ...
}
```

######2. Create a text file 

Create a text file `network1.txt`.

```
euro-network
10 20
```

######3. Create/initialize your class in a code

To create class in code use `Raven#create(path : String) : T` method.

```java

import com.github.piotrlechowicz.raven.Raven;
...
            
Raven<Network> raven = new Raven(Network.class);
Network network = raven.create("network1.txt");
```

To initialize already create class use `Raven#initialize(instance : T, path : String) : T` method.
    
```java
Network network = new Network();
...
Raven<Network> raven = new Raven(Network.class);
raven.initialize(network, "network1.txt");
```

In both cases the fields of instance `network` will have following values:
```
name : "euro-network"
nrOfNodes : 10
nrOfEdges : 20
```

## Text files

<p>
Text files is considered as two dimensional grid, where columns are separated with space sign
and rows are separated with return sign. The indices are counted from top left corner, starting
from value 0.
</p>

If text file has following form:

<pre>
00 01 02 03 04
10 11 12 13 14
20 21 22
30 31 32
</pre>

<p>
then coordinates (2nd row, 1st col) refers to value 21. 
File can contain different number of columns in each row.
</p>

##Parsing list of values

It is possible to parse list of values using `@ManyCols` and/or `@ManyRows` annotations.

######1. Parsing many columns

To parse many columns use `@ManyCols` annotation.
```java
public class Network {
...
    @Parsable(col = 2, row = 3, parser = IntegerParser.class)
    @ManyCols(5)
    List<Integer> edgeDistances;
}
```
<p>
It will initialize this field with 5 values starting from 3rd row and 2nd column,
and ending at 6th column.
</p>

Parsed values are marked bold in a file:

<pre>
1 2 3 4
1 2 4 8
1 3 <b>9 16 25 36 49</b> 64 81
1 1 1 2  2  2  3  3  3
</pre>

######2. Parsing many rows
<p>
To parse many rows use `@ManyRows` annotation. 
If no argument is passed to `@ManyCols` or `@ManyRows`,
it means that all values till last row/column should be taken.
</p>

```java
public class Demands {
	@Parsable(row = 1, col = 1, coverter = IntegerParser.class)
	@ManyRows
	private List<Integer> destinationNodes;
}
```
<p>
It will initialize field with list of integers, 
starting from 1st row and 1st column, and ending on last row.</p>
</p>
Parsed values are marked bold in a file:

<pre>
1 5 20
2 <b>4</b> 18
2 <b>6</b> 10
1 <b>2</b> 15
9 <b>8</b> 4
</pre>

######3. Parsing matrix
<p>
It is possible to initialize matrix of values (<code>List&lt;List&lt;?&gt;&gt;</code>) using at the same time
<code>@ManyCols</code> and <code>@ManyRows</code> annotations.
</p>

## Using parsers for other types than Strings

<p>
Fields of different type than String can be parsed with parsers. 
There is provided set of basic parsers in 
<code>com.github.piotrlechowicz.raven.parsers</code> package:
</p>

 * IntegerParser
 * FloatParser
 * DoubleParser
 * BooleanParser
 * ByteParser

<p>
To use certain parser, it has to be specified in <code>@Parsable</code> annotation 
by setting <code>parser</code> value. For example to use <code>BooleanParser</code> 
field should be annotated in the following way:
</p>

```java
@Parsable(parser = BooleanParser.class)
Boolean booleanValue;
```

###### Creating new parsers
<p>
New parsers can be created to parse other types. 
In order to do this <code>Parser&lt;T&gt;</code> interface has to be extended.
It contains abstract method <code>parse(input : String) : T</code> 
which determines how the input String will be parsed to desired value <code>T</code>. 
</p>

```java
public class MyParser implements Parser<String> {
    @Override
    public String parse(String input) {
        return input.substring(0, 2); 
    }
}
```

## Motivation

<p>
Creation of algorithm with huge number of input files, configurations and properties can 
be a little overwhelming. Raven was created as a tool to minimize necessary effort to
writing text file parsers. It provides easy to use mechanism with annotations.
</p>

## Installation

<p>
The project can be downloaded from Maven repository. 
In your pom.xml file you have to add the following repository and dependency.
</p>

```xml
<repositories>
    <repository>
        <id>armadillo</id>
        <name>GitHub Armadillo Repository</name>
        <url>https://github.com/piotrlechowicz/armadillo</url>
    </repository>
</repositories>
...
<dependencies>
    <dependency>
        <groupId>com.github.piotrlechowicz</groupId>
        <artifactId>raven</artifactId>
        <version>1.1</version>
    </dependency>
</dependencies>
``` 
