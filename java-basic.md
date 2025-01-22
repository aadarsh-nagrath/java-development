Program, process, thread, objects, class,
Printing args that String[] args one
System.out.println(args[0]); or [1]
loops , i++ and ++i difference

1:34:49

Advanced -
ArrayList, List,
Map, HashMap,
Optional, Streams, Filters
Interface, Generics,

In Java, an interface is a reference type that is similar to a class but is used to define a contract that a class must adhere to. It specifies what a class must do, but not how it does it. Interfaces are used to achieve abstraction and multiple inheritance in Java.

Set<> for loop map EntrySet()
Immutable list usage : -> Block List

Generics
----------------------------------------------------
### **Generics in Java**

Generics in Java allow you to write **type-safe** and **reusable code** by enabling classes, interfaces, and methods to operate on **typed parameters**. With generics, you can define a class or method once and use it with different data types without sacrificing type safety.

---

### **Why Use Generics?**
1. **Type Safety**:
   - Detect errors at compile time instead of runtime.
   - Avoids `ClassCastException` by ensuring type compatibility.
   
2. **Reusability**:
   - Write a generic class or method once and use it for any data type.
   
3. **Readability and Maintainability**:
   - Eliminates the need for explicit casting, making code easier to read.

---

### **Basic Syntax**
#### **Generic Class**
```java
// Define a generic class with a type parameter T
class Box<T> {
    private T item;

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}

// Use the generic class
public class Main {
    public static void main(String[] args) {
        Box<String> stringBox = new Box<>();
        stringBox.setItem("Hello");
        System.out.println(stringBox.getItem()); // Output: Hello

        Box<Integer> integerBox = new Box<>();
        integerBox.setItem(42);
        System.out.println(integerBox.getItem()); // Output: 42
    }
}
```

---

#### **Generic Method**
A method can also be defined as generic by declaring the type parameter before the return type.

```java
public class Main {
    // Generic method
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] intArray = {1, 2, 3, 4};
        String[] stringArray = {"Hello", "Generics"};

        printArray(intArray);    // Output: 1 2 3 4
        printArray(stringArray); // Output: Hello Generics
    }
}
```

---

#### **Bounded Type Parameters**
You can restrict the types that can be used as type parameters with **bounds**.

1. **Upper Bound** (`extends`):
   - Restricts the type parameter to be a specific type or its subclasses.
   - Syntax: `<T extends Type>`

```java
class NumberBox<T extends Number> {
    private T num;

    public NumberBox(T num) {
        this.num = num;
    }

    public double square() {
        return num.doubleValue() * num.doubleValue();
    }
}

public class Main {
    public static void main(String[] args) {
        NumberBox<Integer> intBox = new NumberBox<>(4);
        System.out.println(intBox.square()); // Output: 16.0

        NumberBox<Double> doubleBox = new NumberBox<>(3.5);
        System.out.println(doubleBox.square()); // Output: 12.25
    }
}
```

2. **Multiple Bounds**:
   - A type parameter can implement multiple interfaces or extend a class and implement interfaces.
   - Syntax: `<T extends Class & Interface1 & Interface2>`

```java
class Bounded<T extends Number & Comparable<T>> {
    private T value;

    public Bounded(T value) {
        this.value = value;
    }

    public boolean isGreaterThan(T other) {
        return value.compareTo(other) > 0;
    }
}
```

---

#### **Wildcard in Generics**
Wildcards (`?`) allow more flexibility when working with generics by enabling unknown types.

1. **Unbounded Wildcard**:
   - `<?>`: Accepts any type.
   ```java
   public static void printList(List<?> list) {
       for (Object obj : list) {
           System.out.println(obj);
       }
   }
   ```

2. **Upper-Bounded Wildcard**:
   - `<? extends Type>`: Accepts any type that is a subclass of `Type`.
   ```java
   public static double sum(List<? extends Number> list) {
       double total = 0;
       for (Number num : list) {
           total += num.doubleValue();
       }
       return total;
   }
   ```

3. **Lower-Bounded Wildcard**:
   - `<? super Type>`: Accepts any type that is a superclass of `Type`.
   ```java
   public static void addToList(List<? super Integer> list) {
       list.add(42);
   }
   ```

---

### **Generic Interfaces**
You can also define generic interfaces.

```java
interface Pair<K, V> {
    K getKey();
    V getValue();
}

class KeyValue<K, V> implements Pair<K, V> {
    private K key;
    private V value;

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

public class Main {
    public static void main(String[] args) {
        Pair<String, Integer> pair = new KeyValue<>("Age", 25);
        System.out.println(pair.getKey() + ": " + pair.getValue()); // Output: Age: 25
    }
}
```

---

### **Advantages of Generics**
1. **Compile-Time Safety**: Detects type errors early during compilation.
2. **Eliminates Casting**: No need for explicit type casting.
3. **Code Reusability**: Write generic code that works with any type.
4. **Improved Performance**: Avoids the overhead of boxing/unboxing in collections.

---

### **Limitations of Generics**
1. **Type Erasure**:
   - Generic type information is erased during runtime, which means you cannot check the type at runtime.
   ```java
   List<String> list = new ArrayList<>();
   if (list instanceof ArrayList<String>) { // Compile-time error
       // Cannot check generic types at runtime.
   }
   ```

2. **Cannot Create Instances of Generic Types**:
   - You cannot do this:
   ```java
   T obj = new T(); // Not allowed
   ```

3. **Static Context**:
   - Generic type parameters cannot be used in static fields or methods.


Here's a detailed explanation of **`Optional`**, **`Streams`**, **`Filters`**, and **`Interfaces`** in Java:

---
### Optional, Streams, Filters, and Interfaces in Java
---

## **1. Optional**
### **What is Optional?**
`Optional` is a container object introduced in **Java 8**. It is used to represent a value that might be **present** or **absent**. This avoids using `null` directly and prevents `NullPointerException`.

### **Creating an Optional**
- **Empty Optional**:
  ```java
  Optional<String> empty = Optional.empty();
  ```

- **Optional with a Value**:
  ```java
  Optional<String> optional = Optional.of("Hello");
  ```

- **Optional that Can Be Empty**:
  ```java
  Optional<String> optional = Optional.ofNullable(null); // Can hold null
  ```

---

### **Common Methods of Optional**
1. **Check if Value is Present**:
   ```java
   if (optional.isPresent()) {
       System.out.println(optional.get()); // Prints "Hello"
   }
   ```

2. **Get the Value**:
   ```java
   String value = optional.orElse("Default Value");
   System.out.println(value); // Outputs the value or "Default Value" if empty
   ```

3. **Perform Action if Value is Present**:
   ```java
   optional.ifPresent(value -> System.out.println("Value: " + value));
   ```

4. **Throw Exception if Empty**:
   ```java
   String value = optional.orElseThrow(() -> new IllegalArgumentException("Value is missing"));
   ```

---

### **Use Case**
Avoiding `null` checks:
```java
public Optional<String> findNameById(int id) {
    if (id == 1) {
        return Optional.of("John");
    }
    return Optional.empty();
}
```

---

## **2. Streams**
### **What is a Stream?**
`Stream` is a sequence of elements supporting **functional-style operations** like filtering, mapping, and reducing. Streams do not store data; they operate on the source (e.g., a collection, array).

---

### **Creating a Stream**
1. From a Collection:
   ```java
   List<String> list = Arrays.asList("Java", "Python", "C++");
   Stream<String> stream = list.stream();
   ```

2. From an Array:
   ```java
   Stream<Integer> stream = Arrays.stream(new Integer[]{1, 2, 3});
   ```

3. Using `Stream.of`:
   ```java
   Stream<String> stream = Stream.of("A", "B", "C");
   ```

---

### **Common Stream Operations**
1. **Filter**:
   Filters elements based on a condition.
   ```java
   List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
   List<Integer> evenNumbers = numbers.stream()
                                      .filter(n -> n % 2 == 0)
                                      .collect(Collectors.toList());
   System.out.println(evenNumbers); // Output: [2, 4]
   ```

2. **Map**:
   Transforms each element in the stream.
   ```java
   List<String> names = Arrays.asList("john", "jane");
   List<String> upperCaseNames = names.stream()
                                      .map(String::toUpperCase)
                                      .collect(Collectors.toList());
   System.out.println(upperCaseNames); // Output: [JOHN, JANE]
   ```

3. **Reduce**:
   Aggregates the elements into a single result.
   ```java
   List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
   int sum = numbers.stream()
                    .reduce(0, Integer::sum);
   System.out.println(sum); // Output: 10
   ```

4. **Sorted**:
   Sorts the stream elements.
   ```java
   List<Integer> sorted = numbers.stream()
                                 .sorted()
                                 .collect(Collectors.toList());
   ```

5. **Collect**:
   Converts the stream into a collection or another format.
   ```java
   Set<String> uniqueNames = names.stream()
                                  .collect(Collectors.toSet());
   ```

---

## **3. Filter**
### **What is Filter?**
`filter` is an intermediate operation in **Streams** that processes elements based on a **predicate** (a boolean-valued function). Only elements that pass the condition are included in the resulting stream.

---

### **Example**
```java
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("John", "Jane", "Jack", "Jill");

        // Filter names starting with 'J'
        List<String> filteredNames = names.stream()
                                          .filter(name -> name.startsWith("J"))
                                          .collect(Collectors.toList());

        System.out.println(filteredNames); // Output: [John, Jane, Jack, Jill]
    }
}
```

---

## **4. Interface**
### **What is an Interface?**
An interface in Java is a **blueprint** for a class. It can contain:
- **Abstract methods** (until Java 7).
- **Default and static methods** (from Java 8).
- **Private methods** (from Java 9).

An interface specifies what a class must do but **not how** to do it.

---

### **Defining an Interface**
```java
interface Animal {
    void makeSound(); // Abstract method
}

class Dog implements Animal {
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal dog = new Dog();
        dog.makeSound(); // Output: Woof!
    }
}
```

---

### **Default and Static Methods**
1. **Default Methods**:
   Provide implementation in the interface itself.
   ```java
   interface Animal {
       void makeSound();

       default void eat() {
           System.out.println("This animal eats food.");
       }
   }
   ```

2. **Static Methods**:
   Can be called on the interface.
   ```java
   interface Utility {
       static void print(String message) {
           System.out.println(message);
       }
   }

   public class Main {
       public static void main(String[] args) {
           Utility.print("Hello!"); // Output: Hello!
       }
   }
   ```

---

### **Functional Interfaces**
An interface with **only one abstract method** is called a functional interface. These are used with **lambda expressions**.

Example:
```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);
}

public class Main {
    public static void main(String[] args) {
        Calculator calc = (a, b) -> a + b;
        System.out.println(calc.add(5, 3)); // Output: 8
    }
}
```

---

### **Key Features of Interface**
- Multiple inheritance: A class can implement multiple interfaces.
- Cannot have instance fields (only constants).
- All methods are implicitly public and abstract unless explicitly defined otherwise.
