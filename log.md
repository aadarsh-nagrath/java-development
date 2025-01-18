![{EA5F65E9-3BB4-4CA2-AE12-08B13ACA9257}](https://github.com/user-attachments/assets/978951bf-e211-49df-9ec7-3a4b0b39a0dd)
## JAVA 
Why java - oops, JAVA 8, possibility to get into depth modules features. Compiled and interpreted language
---

### **What is JVM?**
The JVM is a software-based engine that:
- Executes Java bytecode, which is the intermediate, platform-independent representation of a Java program.
- Provides a runtime environment with functionalities like memory management, garbage collection, and security.
- Acts as an abstraction layer between Java applications and the underlying operating system and hardware.

### **Components of the JVM**
1. **Class Loader Subsystem**  
   - Loads `.class` files (compiled Java bytecode) into memory.
   - Verifies the bytecode for security and correctness.
   - Links and initializes classes before execution.

2. **Runtime Memory Areas**  
   JVM divides memory into several areas:
   - **Heap**: Stores objects and class instances.  
   - **Method Area (or Metaspace in Java 8 and later)**: Stores metadata, static variables, and runtime constant pools.  
   - **Stack**: Stores method execution details, including local variables and partial results. Each thread gets its own stack.  
   - **PC Register**: Holds the address of the currently executing instruction for each thread.  
   - **Native Method Stack**: For executing native (non-Java) methods.

3. **Execution Engine**  
   - **Interpreter**: Interprets bytecode line-by-line.  
   - **Just-In-Time (JIT) Compiler**: Converts bytecode into native machine code for faster execution. Frequently used code paths are optimized by the JIT.  
   - **Garbage Collector (GC)**: Automatically manages memory by reclaiming unused objects.

4. **Java Native Interface (JNI)**  
   Enables interaction with native libraries (e.g., C, C++), providing access to system resources and non-Java functionality.

5. **Native Method Libraries**  
   External libraries that provide platform-specific functionality.

---

### **How the JVM Works**
1. **Compilation**  
   - Java source code (`.java`) is compiled by the Java Compiler (`javac`) into bytecode (`.class`).  
   - This bytecode is platform-independent.

2. **Loading**  
   - Bytecode is loaded into the JVM by the Class Loader.

3. **Bytecode Verification**  
   - The Class Loader verifies the bytecode to ensure that it's secure and doesn't violate any Java language specifications.

4. **Execution**  
   - The Execution Engine interprets the bytecode or compiles it into native code using the JIT compiler.
   - The compiled code is cached for reuse to speed up execution.

5. **Memory Management**  
   - The Garbage Collector identifies and removes objects that are no longer in use to free memory.

6. **Thread Management**  
   - The JVM handles multithreading, enabling concurrent execution of multiple threads.

---

### **Key Features of JVM**
- **Platform Independence**: Java bytecode runs on any device with a compatible JVM.
- **Automatic Memory Management**: The Garbage Collector minimizes memory leaks and manages object lifecycles.
- **Security**: The JVM verifies bytecode to prevent malicious operations.
- **Performance Optimization**: JIT compilation and advanced GC algorithms improve runtime performance.

![{51AAF114-6A37-4842-8DC7-B6E1DAF0DE78}](https://github.com/user-attachments/assets/2a017c5e-d6ca-47e6-8893-ae58e8c0bdfd)
---

### **1. Java Virtual Machine (JVM)**
- **Purpose of JVM:**
  - The JVM allows Java programs to be platform-independent by compiling Java source code into bytecode.
  - It executes the bytecode on any system with a JVM, ensuring cross-platform compatibility.
  
- **JVM Structure:**
  - **Class Loader Subsystem:** Responsible for loading, verifying, and managing class files during runtime.
  - **Runtime Data Areas:** Includes several memory areas where data is stored during program execution, such as:
    - **Heap Memory:** Stores objects and arrays.
    - **Stack Memory:** Stores method calls and local variables.
    - **Method Area:** Stores class definitions.
  - **Execution Engine:** The part of the JVM that actually runs bytecode. It converts bytecode into machine code, either through interpretation or Just-in-Time (JIT) compilation.

- **Garbage Collection:**
  - **Automatic Memory Management:** JVM has a built-in garbage collector that automatically handles memory allocation and deallocation.
  - The garbage collector identifies and removes objects that are no longer in use, helping prevent memory leaks.

---

### **2. Java Internals**
- **Java's Bytecode:**
  - When you compile Java code, it's translated into bytecode, not machine code.
  - Bytecode is platform-independent and can be executed by any JVM regardless of the operating system.
  
- **JIT Compiler:**
  - The Just-In-Time (JIT) compiler converts bytecode into native machine code at runtime, which improves performance by optimizing code execution.

- **JVM vs JRE vs JDK:**
  - **JVM (Java Virtual Machine):** Executes Java bytecode and is platform-dependent.
  - **JRE (Java Runtime Environment):** Includes the JVM and necessary libraries to run Java applications but does not include tools for development.
  - **JDK (Java Development Kit):** Contains the JRE along with development tools like the Java compiler (`javac`), debuggers, and other utilities needed to develop Java applications.

---

### **3. Java Development Kit (JDK) and Its Components**
- **JDK Structure:**
  - **Compiler (`javac`):** Transforms Java source code into bytecode.
  - **JVM:** Executes bytecode.
  - **Standard Libraries:** The JDK includes extensive libraries for common programming tasks such as file I/O, networking, data handling, and more.
  - **Development Tools:** Includes utilities like `javadoc`, `jdb` (Java debugger), and `javap` (class file disassembler).
  
- **JDK vs JRE:**
  - **JDK** includes everything in the **JRE**, plus additional tools for development.
  - The **JRE** is required to run Java applications but does not have the tools necessary to write or compile code.

---

### **4. Java's Internal Workings (Bytecode Execution)**
- **Class Loading Process:**
  - **Bootstrap Class Loader:** Loads core Java libraries (like `rt.jar`).
  - **Extension Class Loader:** Loads extension libraries like Java’s XML parsers.
  - **Application Class Loader:** Loads application-specific classes and packages.

- **Method Invocation:**
  - When a method is called, the JVM locates the method in the method area, loads it, and executes it using the execution engine.

- **Memory Management:**
  - **Stack:** Stores method calls and local variables during execution.
  - **Heap:** Stores objects and arrays.
  - The **Garbage Collector** removes unused objects from the heap, freeing up memory automatically.

---

### **5. JVM Performance Optimization**
- **HotSpot JVM:**
  - **HotSpot:** A common JVM implementation that optimizes performance by using techniques like JIT compilation and adaptive optimization.
  
- **JVM Options:**
  - The JVM provides various flags and configurations to tune performance, such as garbage collection behavior, heap size, and thread management.


![{AC58F3D2-9090-47B8-99FA-DC2FB37B5336}](https://github.com/user-attachments/assets/5b8c186a-619f-40ba-8d78-205aaca98179)


32 min - code, data types, wrappers, objects
