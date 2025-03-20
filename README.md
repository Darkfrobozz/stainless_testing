# Getting started with Stainless

Stainless is a verification framework for Scala programs. Given a program written 
in Scala, Stainless verifies it statically (i.e., without running the program), by checking if the program is conform to a specification.
It also checks whether the program does not contain runtime errors and if it terminates.

## Installation

### Java Development Kit
To run Stainless and verify your programs, you need **Java 17**. If you already have Java installed, you can check your version using: 
```shell
> java -version
openjdk version "17.0.9" 2023-10-17
OpenJDK Runtime Environment Temurin-17.0.9+9 (build 17.0.9+9)
OpenJDK 64-Bit Server VM Temurin-17.0.9+9 (build 17.0.9+9, mixed mode, sharing)
> javac -version
javac 17.0.9
```
The exact version may vary, but the major version should be 17.

On some Linux distributions, you can switch between multiple installed JDKs using a system command. Examples include:
- **Debian-based:** `update-alternatives -config java`;
- **[ArchLinux-based](https://wiki.archlinux.org/title/Java#Switching_between_JVM):** `archlinux-java set java-17-openjdk`.

On macOS, [SDKMAN!](https://sdkman.io/) makes it easy to install and switch between multiple JDK versions:
- To install SDKMAN!, you can refer to the [installation instructions](https://sdkman.io/install).
- You can see the list of available versions with `sdk list java`.
- You can install a specific version with `sdk install java 17.0.14-tem` (any version works as long as it is java 17).
- If you want to use a specific version for the current terminal session, you can use `sdk use java 17.0.14-tem`.
- If you want to use a specific version as the default, you can use `sdk default java 17.0.14-tem`.


### Scala and sbt
You also need a way to compile scala programs. The recommended approach is to install **sbt**, a build tool for Scala projects. You can find the installation instructions [here](https://www.scala-sbt.org/download). Once sbt is installed, verify it by running:
```shell
> sbt -version 
sbt runner version: 1.10.11
```

Common IDEs for working with Scala include **Visual Studio Code** with the **Scala Metals** extension and **IntelliJ IDEA** with the **Scala plugin**. 

### Stainless
 Let's now install Stainless!

1. Download the latest Stainless release from the [releases page](https://github.com/epfl-lara/stainless/releases) on GitHub. Make sure to select the appropriate ZIP file for your operating system
2. Unzip the the file you just downloaded into a directory of your choice.
3. *(Recommended)* Add this directory to your PATH. This allows you to run Stainless using the stainless command instead of specifying its fully path.

For a step-by-step guide, you can watch [this video](https://mediaspace.epfl.ch/media/01-21%2C%20Stainless%20Tutorial%201_4/0_h1bv5a7v). 

To verify that Stainless is installed correctly, run the following command in your terminal (you may need to add `.sh` or `.bat` after `stainless`, depending on your operating system):
```shell
> stainless --version
[  Info  ] Stainless verification tool (https://github.com/epfl-lara/stainless)
[  Info  ]   Version: 0.9.9.0
[  Info  ]   Built at: 2024-12-09 13:31:50.314+0000
[  Info  ]   Stainless Scala version: 3.5.2
[  Info  ] Inox solver (https://github.com/epfl-lara/inox)
[  Info  ] Version: 1.1.5-182-g854bcdf
[  Info  ] Bundled Scala compiler: 3.5.2
```

## Tutorial

Now, let's introduce the basics of Stainless. If this tutorial does not fully meet your needs, additional resources are available online.
- A basic tutorial can be found on [Stainless documentation website](https://epfl-lara.github.io/stainless/tutorial.html). 
- More documentation and material are available on the [official repository page](https://github.com/epfl-lara/stainless/#further-documentation-and-learning-materials). 
- If you are looking for concrete verified examples, you can find a collection of benchmark programs [here](https://github.com/epfl-lara/stainless/blob/main/frontends/benchmarks/verification/valid/). 
- For more advanced case studies of verified programs using Stainless, check out the [Bolts repository](https://github.com/epfl-lara/bolts).

To follow this tutorial, start by cloning this repository.

### Preconditions and Postconditions

 As mentioned earlier, Stainless takes Scala programs as input. Properties that you want to verify are expressed as **annotations** in the code. These annotations are converted into runtime assertions when the program is executed, meaning that annotated programs can still be compiled and run like regular Scala programs. 
 Let's see how this works with a simple example. Open the file [`Tutorial.scala`](src/main/scala/Tutorial.scala) under `src/main/scala`. We start by defining a function that computes the square of an integer.

```scala
def square(x: BigInt): BigInt = {
  x * x
}
```

A property of squaring is that the result is always non-negative. We can express this property as a **postcondition** using an `ensuring` clause at the end of the function definition. This clause specified a property that the function's result must satisfy. This property is written as a lambda that takes the result of the function as input and returns a boolean.

```scala
def square(x: BigInt): BigInt = {
  x * x
}.ensuring(res => res >= 0)
```

If you run stainless on this file, you should see the following output, where most of the text is colored green.

```shell
> stainless src/main/scala/Tutorial.scala
[  Info  ] Finished compiling                                       
[  Info  ] Preprocessing finished                                   
[  Info  ] Finished lowering the symbols                            
[  Info  ] Finished generating VCs                                  
[  Info  ] Starting verification...
[  Info  ]  Verified: 1 / 1                     
[  Info  ]   ┌───────────────────┐
[  Info  ] ╔═╡ stainless summary ╞═════════════════════════════════════════════════════════════════════════════════╗
[  Info  ] ║ └───────────────────┘                                                                                 ║
[  Info  ] ║ src/main/scala/Tutorial.scala:1:5:            square   postcondition      valid   U:smt-cvc5     0.1  ║
[  Info  ] ╟┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄╢
[  Info  ] ║ total: 1    valid: 1    (0 from cache, 0 trivial) invalid: 0    unknown: 0    time:    0.08           ║
[  Info  ] ╚═══════════════════════════════════════════════════════════════════════════════════════════════════════╝
```
The summary shows that the postcondition of the `square` function has been proven valid. You can see other details, such as the time taken to verify the property, the solver used, and the line number where the property is defined.

Note that the function uses `BigInt` instead of `Int`. While `Int` is also supported by Stainless, you would need to prove that the result of the `square` function does not overflow.

Now, suppose we know that the input to the `square` function is always at least 2.
In that case, we could attempt to prove additional properties such as `res > x`.
```scala
// Trust me: x >= 2
def square(x: BigInt): BigInt = {
  x * x
}.ensuring(res => res >= 0 && res > x)
```
Unfortunately, Stainless will not verify this snippet because the property does not hold for all inputs, and Stainless has no way of knowing what you assume about the input. To fix this issue, we can explicitly specify assumptions about the input using **preconditions**, expressed with the `require` keyword. Preconditions define conditions that must hold whenever the function is called. 
```scala
def square(x: BigInt): BigInt = {
  require(x >= 2)
  x * x
}.ensuring(res => res >= 0 && res > x)
```
Stainless should now be able to verify the postcondition of the `square` function. If you call the function elsewhere in your code with an input that does not satisfy the precondition, Stainless will complain, stating that the precondition is not met.

```scala
def square(x: BigInt): BigInt = {
  require(x >= 2)
  x * x
}.ensuring(res => res >= 0 && res > x)

// This will not verify
val squareOfOne = square(1)
```

### Recursion and termination

If you are familiar with inductive reasoning, you probably know that proving properties about recursive functions often relies on structural induction. Stainless supports this kind of reasoning and, in simple cases, can handle it automatically. Let's define a recursive function that computes the factorial of an integer.

```scala
def factorial(n: BigInt): BigInt = {
  if n == 0 then 1 else factorial(n - 1) * n
}
```
If you run Stainless on this code, you will see several warnings, along with a red line indicating that a measure is missing for `factorial`. A **termination measure** is a non-negative integer value that decreases with each recursive call. If such a measure exists, the function is guaranteed to terminate. When verifying a program, Stainless attempts to prove that every function terminates. To do this, it tries to infer likely measures for recursive functions. Our `factorial` function can loop indefinitely if given a negative input. To prevent this, we need to add a precondition to ensure that the function is only called with non-negative inputs.

```scala
// Stainless automatically finds an appropriate measure: n
def factorial(n: BigInt): BigInt = {
  require(n >= 0)
  if n == 0 then 1 else factorial(n - 1) * n
}
```

If the measure is too complex for Stainless to infer automatically, you can specify it manually using the `decreases` annotation.

```scala
// imports the decreases annotation
import stainless.lang.*

def factorial(n: BigInt): BigInt = {
  decreases(n)
  require(n >= 0)
  if n == 0 then 1 else factorial(n - 1) * n
}
```

To prove that the factorial of a natural number is always positive, we typically write an induction proof.
$$\begin{align*}
&\text{Base case:} \quad \quad\quad \text{factorial}(0) = 1 \\
&\text{Inductive step:} \quad \text{factorial}(n) = \text{factorial}(n-1) \times n \geq 1 \quad (\text{by IH and the fact that } n \geq 1)
\end{align*}$$
For simple cases, Stainless can automatically infer the induction step and verify the property.
```scala
def factorial(n: BigInt): BigInt = {
  require(n >= 0)
  if n == 0 then 1 else factorial(n - 1) * n
}.ensuring(res => res >= 1)
```

### External theorems
Sometimes, some properties cannot be expressed using just preconditions and postconditions. In such cases, we define **external functions** that serve as theorems.
For example, let's prove that the factorial function is increasing.
```scala
def factorialIncreasing(n: BigInt, m: BigInt): Unit = {
  require(n >= 0)
  require(m >= 0)
}.ensuring(factorial(n + m) >= factorial(n))
```
In Scala `Unit` is similar to `void` in Java - it indicates that the function does not return anything (though it implicitly returns `()`). The ensuring clause specifies the theorem we want to prove. Since the function `factorialIncreasing` is not recursive (because its body is empty), Stainless does not automatically apply induction. Running Stainless on this code will result in a verification failure. If the theorem is easy enough, you can add an `@induct` annotation to the parameter you want to induct on. Stainless will try to prove the theorem by induction on the annotated parameter.
```scala
// Import the @induct annotation
import stainless.annotation.*

def factorialIncreasing(@induct n: BigInt, m: BigInt): Unit = {
  require(n >= 0)
  require(m >= 0)
}.ensuring(factorial(n + m) >= factorial(n))
```
The theorem can now be used to prove more complex properties by calling it within other proofs. To do so, call the theorem you just proved inside the body of the one you are currently proving. The body of a theorem essentially serves as a sketch of its proof:
```scala
def factorialIncreasing2(m: BigInt, n: BigInt): Unit = {
  require(0 <= m && m <= n)
  factorialIncreasing(m, n - m)
}.ensuring(factorial(m) <= factorial(n))
```
While this theorem could be proven without using `factorialIncreasing`, the proof would be too complex for Stainless to infer automatically using just `@induct`. In that case, we would need to write an explicit inductive proof.
```scala
def factorialIncreasing(m: BigInt, n: BigInt): Unit = {
  require(0 <= m && m <= n)
  if m == 0 then () else factorialIncreasing(m - 1, n - 1)
}.ensuring(factorial(m) <= factorial(n))
```

### CLI arguments and annotations

Command-line arguments can be passed to Stainless to get the most out of the verification process. For example, tweaking the **timeout** can significantly impact the verification time. For small, independent files, 2 seconds is usually sufficient. For larger programs or complex theorems, you may need to increase the timeout. Some datatypes like `Int`(which has overflow concerns) tend to be harder to verify than their `BigInt` counterparts. For this tutorial, the timeout was set to 2 seconds. 

Similarly, you can choose to enable caching to store the results of previous verification attempts. This speeds up verification for conditions that have not changed between two runs. However, the cache can grow quite large and needs to be cleaned up periodically.
For this tutorial, caching was disabled. 

Finally, some flags enhance the user experience. The compact flag reduces terminal output to display only failed verification conditions, while the watch flag reruns Stainless every time a file is saved.

Command line arguments do not need to be passed manually each time you run Stainless. Instead, ou can create a configuration file called `stainless.conf` in the directory where you run Stainless (typically the root of the repository). The following example configuration file sets the timeout to 2 seconds, enables caching, activates compact information mode and watches for file changes.

```shell
timeout=2
vc-cache=true
compact=true
watch=true
```

For other command-line options and their descriptions, you can run `stainless --help`.

Stainless features a wide range of annotations to make the verification process more flexible. 
- The `@extern`annotation ignores the content of a function. It the function has a postcondition, Stainless assumes it to be true. This is useful for parts of the code that are not supported by Stainless or are not meant to be verified. It can also be used to define axioms. 
- The `@opaque`annotation hides the content of a function. From the outside, only its preconditions and preconditions are  visible. This can be speed up the verification process or make the rest of the program independent of the function's implementation. Be aware that when a function is opaque, its postconditions typically need to be stronger, as they must fully describe its behavior. 

For a full list of available annotations, refer to the [official documentation](https://epfl-lara.github.io/stainless/library.html#annotations).

