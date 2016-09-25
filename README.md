# Raven
**Initializes class fields with values provided in an external text file.**

<p>
Contains field annotations that points which value/values from text file 
should be used to initialize that field. 
Different instances can be initialized with different text files.
For example it can be used to initialize some algorithm's properties and 
instances which are used by it.
</p>

##How to use

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

The fields of instance `network` will have following values:
```
name == "euro-network"
nrOfNodes == 10
nrOfEdges == 20
```

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
 and ending at 6th column.</p>

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

##Using parsers

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

```java
@Parsable(parser = BooleanParser.class)
Boolean booleanValue;
```

######Creating new parsers
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