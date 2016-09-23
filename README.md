# Raven
**Initializes class fields with values provided in an external text file.**

<p>
Contains field annotations that points which value/values from text file 
should be used to initialize that field. 
Different instances can be initialized with different text files.
For example it can be used to initialize some algorithm's properties and 
instances which are used by it.
</p>

##Quick start

######1. Annotate your class
<p>
Annotation <code>@PositionInFile</code> denotes that field should be initialized. 
<code>col</code> and <code>row</code> values point to position in a text file.
</p>

```java
public class Network {
    
    @PositionInFile(col = 0, row = 0)
    private int nrOfNodes;
    
    @PositionInFile(col = 1, row = 0)
    private int nrOfEdges;
    
    @PositionInFile(col = 0, row = 1)
    private int capacity;
    
    ...
}
```

######2. Create a text file 

Create a text file `network1.txt`.

```
10 20
```

######3. Initialize your class in a code

```java

import com.github.piotrlechowicz.raven.IntegerFlatFileParser;
...
            
IntegerFlatFileParser parser = new IntegerFlatFileParser();
Network network = parser.parseFile(Network.class, "network1.txt");
```
    
The fields of instance `network` will have following values:
```
nrOfNodes == 10
nrOfEdges == 20
```

##Parsing list of values

It is possible to parse list of values using `@ManyCols` and/or `@ManyRows` annotations.

```java
public class Network {
...
    @PositionInFile(col = 2, row = 3)
    @ManyCols(5)
    List<Integer> edgeDistances;
}
```

It will initialize this field with 5 values starting from 3rd row and 2nd column, and ending at 6th column.

Parsed values are marked bold in a file:

<pre>
1 2 3 4
1 2 4 8
1 3 <b>9 16 25 36 49</b> 64 81
1 1 1 2  2  2  3  3  3
</pre>

TODO: finish description of @ManyCols and @ManyRows

##Parsing different types

If class contains different types of field, two approaches can be used.

######1. Parsing Strings

Parsing all values to some class as Strings and after that converting them as necessary

Create a class:
    
```java
public class NetworkRaw {
    @PositionInFile(col = 0, row = 0)
    String name;
    
    @PositionInFile(col = 0; row = 1)
    String nrOfNodes;
     
    @PositionInFile(col = 1; row = 1)
    String nrOfEdges;
    
    ...
    }
```

Create a text file network1.txt.
    
```
EuroNetwork
10 20
```

And use `StringFlatFileParser(NetworkRaw.class, "network1.txt")` to create instance of `NetworkRaw`. <br/>
Write a factory/provide constructor/etc. which will convert `NetworkRaw` into `Network`.

######2. Parsing Objects

Extend class `FlatFileParser` for `Object` type and implement method `createMatrixOfValues(List<String> rawFileContent): List<List<Object>>`. <br/>

Depending on the row, it should parse value to correct format.
    
```java
public class NetworkFlatFileParser extends FlatFileParser<Object> {
    
    @Override
    protected List<List<Object>> createMatrixOfValues(List<String> rawFileContent) {
        List<List<Object>> result = new ArrayList<>();
        for (int i = 0; i < rawFileContent.size(); i++) {
            String line = rawFileContent.get(i);
            if (i == 1) {
                List<Object> row = new ArrayList<>();
                Scanner scanner = new Scanner(line);
                while(scanner.hasNextInt()) {
                    row.add(scanner.nextInt());
                }
                result.add(row);
            } else {
                String[] split = line.split("\\s+");
                result.add(Arrays.asList(split));
            }
        }
        return result;
    }
}
```