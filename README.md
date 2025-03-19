# Getting started with Stainless

Stainless is a verification framework for Scala programs. Given a program written 
in Scala, Stainless verifies it statically (i.e., without running the program), by checking if the program is conform to a specification.
It also checks whether the program does not contain runtime errors and if it terminates.

## Installation

### Java Development Kit
In order to run stainless, as well as the programs you will verify, you will need **Java 17**.

If you already have Java installed you can check your Java version using 
```shell
> java -version
openjdk version "17.0.9" 2023-10-17
OpenJDK Runtime Environment Temurin-17.0.9+9 (build 17.0.9+9)
OpenJDK 64-Bit Server VM Temurin-17.0.9+9 (build 17.0.9+9, mixed mode, sharing)
> javac -version
javac 17.0.9
```
The exact version might vary, but the major version should be 17.

On some Linux distributions, a command exists to change your "active" JDK if multiple ones are installed on your machine. Examples include:
- **Debian-based:** `update-alternatives -config java`;
- **[ArchLinux-based](https://wiki.archlinux.org/title/Java#Switching_between_JVM):** `archlinux-java set java-17-openjdk`.

On macOS, [SDKMAN!](https://sdkman.io/) allows you to easily install and switch between multiple JDK versions.
- To install SDKMAN!, you can refer to the [installation instructions](https://sdkman.io/install).
- You can see the list of available versions with `sdk list java`.
- You can install a specific version with `sdk install java 17.0.14-tem` (any version works as long as it is java 17).
- If you want to use a specific version for the current terminal session, you can use `sdk use java 17.0.14-tem`.
- If you want to use a specific version as the default, you can use `sdk default java 17.0.14-tem`.


### Scala and sbt
You will also need a way to compile scala programs. For that you can install sbt, a build tool for Scala projects. You can find the installation instructions [here](https://www.scala-sbt.org/download). Once you have downloaded sbt, you can test your installation by running
```shell
> sbt -version 
sbt runner version: 1.10.11
```

Common IDEs for working with Scala are Visual Studio Code with the Scala Metals extension and IntelliJ IDEA with the Scala plugin. 

### Stainless
 Let's now install Stainless!

1. You can download the latest Stainless release from the [releases page](https://github.com/epfl-lara/stainless/releases) on GitHub. Make sure to pick the appropriate ZIP for your operating system
2. Unzip the the file you just downloaded to a directory.
3. (Recommended) Add this directory to your PATH. This will let you invoke Stainless via the stainless command instead of its fully qualified path.

[This video](https://mediaspace.epfl.ch/media/01-21%2C%20Stainless%20Tutorial%201_4/0_h1bv5a7v) shows a step-by-step installation of Stainless in case you need more guidance. 

You can check if Stainless is correctly installed by running the following command in your terminal (you may need to add `.sh` or `.bat` after `stainless` depending on your operating system):
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

We are now going to introduce you to the basics of Stainless. If you do not find this tutorial satisfactory, or if it does not suit your needs, other resources are available online.
A basic tutorial on stainless can be found on [Stainless documentation website](https://epfl-lara.github.io/stainless/tutorial.html). Some additional resources can be found on the [official repository page](https://github.com/epfl-lara/stainless/#further-documentation-and-learning-materials). 
If you are looking for concrete verified examples, [here](https://github.com/epfl-lara/stainless/blob/main/frontends/benchmarks/verification/valid/) are some verified examples used as benchmarks for Stainless. For more involved examples, you can check out [Bolts](https://github.com/epfl-lara/bolts), a repository featuring case studies of verified programs using Stainless.

In order to follow the tutorial, start by cloning this repository.

### Preconditions and postconditions

 As mentioned earlier, Stainless accepts Scala programs as input. Properties that the user wants to verify are expressed as annotations in the code. Note that these annotations will be converted to runtime assertions when executed. Hence, annotated programs can still be compiled and run as regular Scala programs. Let's see how this works with a simple example. Open the file [`Tutorial.scala`](src/main/scala/Tutorial.scala) under `src/main/scala`. We start by defining a function that computes the square of an integer.

```scala
def square(x: BigInt): BigInt = {
  x * x
}
```

A property we know about squaring is that the result is always non-negative. We can express this property as a postcondition of the `square` function. To do this, we add an `ensuring` clause at the end of the function definition. Inside the `ensuring` clause, we write the property we want to verify. The property is a function that takes the result of the function as input and returns a boolean.

```scala
def square(x: BigInt): BigInt = {
  x * x
}.ensuring(res => res >= 0)
```

If you run stainless on this file, you should see the following output where most of the text is colored in green.

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
The summary shows that the postcondition of the `square` function has been proven valid. You can see other information such as the time taken to verify the property, the solver used and the line number where the property is defined.
Note that the type used in this function is `BigInt` instead of `Int`. `Int` are also supported by Stainless but you would need to prove that the result of the square function does not overflow.

Suppose that we know that the input of your square function is always at least 2.
We could prove additional properties such as `res > x`.
```scala
// Trust me: x >= 2
def square(x: BigInt): BigInt = {
  x * x
}.ensuring(res => res >= 0 && res > x)
```
Unfortunately, the snippet is not going to be verified by Stainless, since the property does not hold for all inputs and Stainless has no way of knowing what you know about the input. To fix this, we can add preconditions to the function. Preconditions are expressed using the `require` keyword, and are properties of the input that must hold whenever the function is called. 
```scala
def square(x: BigInt): BigInt = {
  require(x >= 2)
  x * x
}.ensuring(res => res >= 0 && res > x)
```
Stainless should now be able to verify the postcondition of the `square` function. If you call the function in some other part of the code with an input that does not satisfy the precondition, Stainless will complain saying that the precondition is not satisfied.

```scala
def square(x: BigInt): BigInt = {
  require(x >= 2)
  x * x
}.ensuring(res => res >= 0 && res > x)

// This will not verify
val squareOfOne = square(1)
```

### Recursion and termination

If you are familiar with inductive reasoning, you probably know that proving properties about recursive functions can be done by structural induction. Stainless supports this kind of reasoning and in easy cases does not require manual assistance. Let's start by defining a recursive function that computes the factorial of an integer.

```scala
def factorial(n: BigInt): BigInt = {
  if n == 0 then 1 else factorial(n - 1) * n
}
```
If you run Stainless on this piece of code, you see a lot of warnings and a red line indicating that a measure is missing for `factorial`. A measure is a non-negative integer value that decreases with each recursive call. If a measure exists for the function, it is guaranteed to terminate. When verifying a program, Stainless tries to prove that every function always terminate. To do this, it tries to infer likely measures for recursive functions. Our factorial function can loop indefinitely if the input is negative. We therefore need to fix this by adding a precondition to the function.

```scala
// Stainless automatically finds an appropriate measure: n
def factorial(n: BigInt): BigInt = {
  require(n >= 0)
  if n == 0 then 1 else factorial(n - 1) * n
}
```

Note that if the measure is too involved for Stainless to infer, you can specify it manually.

```scala
// imports the decreases annotation
import stainless.lang.*

def factorial(n: BigInt): BigInt = {
  decreases(n)
  require(n >= 0)
  if n == 0 then 1 else factorial(n - 1) * n
}
```

If we want to prove that the factorial of a natural number is always positive, we generally write an induction proof.
$$\begin{align*}
&\text{Base case:} \quad \quad\quad \text{factorial}(0) = 1 \\
&\text{Inductive step:} \quad \text{factorial}(n) = \text{factorial}(n-1) \times n \geq 1 \quad (\text{by IH and the fact that } n \geq 1)
\end{align*}$$
In easy case Stainless can automatically guess the induction step and proves the property.
```scala
def factorial(n: BigInt): BigInt = {
  require(n >= 0)
  if n == 0 then 1 else factorial(n - 1) * n
}.ensuring(res => res >= 1)
```

Sometimes, some properties cannot be expressed by solely using preconditions and postconditions. In such cases, you can define external functions that will act as theorems.
For instance, let's prove that the factorial function is increasing.
```scala
def factorialIncreasing(n: BigInt, m: BigInt): Unit = {
  require(n >= 0)
  require(m >= 0)
}.ensuring(factorial(n + m) >= factorial(n))
```
In Scala `Unit` is the equivalent of `void` in Java. It is used to indicate that the function does not return anything (in reality it implicitly returns the only `Unit` value `()`). The ensuring clause contains the theorem we want to prove. Since the function `factorialIncreasing` is not recursive (because it does not do anything), Stainless does not know that it should use induction to prove the theorem. 
In fact, if you run Stainless on this file, you will see that it will fail to prove the theorem. If the theorem is easy enough, you can add an `@induct` annotation to the parameter you want to induct on. Stainless will try to prove the theorem by induction on the annotated parameter.
```scala
// imports the @induct annotation
import stainless.annotation.*

def factorialIncreasing(@induct n: BigInt, m: BigInt): Unit = {
  require(n >= 0)
  require(m >= 0)
}.ensuring(factorial(n + m) >= factorial(n))
```
The theorem can now be used in other parts of the program to prove more complex properties. To do that, just call the theorem you just proved inside the body of the one you are currently proving. Essentially, the body of a theorem is a sketch of its proof!
```scala
def factorialIncreasing2(m: BigInt, n: BigInt): Unit = {
  require(0 <= m && m <= n)
  factorialIncreasing(m, n - m)
}.ensuring(factorial(m) <= factorial(n))
```
The above theorem could still be proved without the use of `factorialIncreasing`. However, it would be too complex to be proved by just using the `@induct` annotation. We would be forced to write a proper
proof by induction.

```scala
def factorialIncreasing(m: BigInt, n: BigInt): Unit = {
  require(0 <= m && m <= n)
  if m == 0 then () else factorialIncreasing(m - 1, n - 1)
}.ensuring(factorial(m) <= factorial(n))
```

### CLI arguments, annotations and afterthoughts

Command-line arguments can be passed to Stainless to get the most out of the verification process. For instance, tweaking the timeout value can significantly affect the verification time. For small and independent files, a timeout of 2 seconds is usually enough. When dealing with larger programs or more complex theorems, you might want to increase the timeout value. Some datatypes like `Int`(where overflow is a concern) tend to be more difficult to verify than their `BigInt` counterparts. For this tutorial, the timeout was set to 2 seconds. Similarly, you can choose to enabling caching to store the results of previous verification attempts. This will speed up the verification process for verification conditions that have not changed between 2 runs. On the other hand, the cache can grow quite large and needs to be cleaned up from time to time.
For this tutorial, caching was disabled. Finally some flags enhance user experience. The compact flag reduces the amount of information displayed in the terminal to only failed verification conditions. The watch flag reruns Stainless every time a file is saved.

Command line arguments do not need to be passed every time you run Stainless. You can create a configuration file called `stainless.conf` in the directory where you run Stainless (in this case the root of the repository). For example, the following configuration file sets the timeout to 2 seconds, enables caching, compact information mode and watches for changes in the files.

```shell
timeout=2
vc-cache=true
compact=true
watch=true
```

For other command-line options and their descriptions, you can run `stainless --help`.

Stainless features a wide range of annotations to make the verification process more flexible. The `@extern`annotation ignores the content of a function. It the function has a postcondition, it is assumed to be true. This is useful when some parts of the code are not supported by Stainless or are not meant to be verified. This can also be used for designing axioms. The `@opaque`annotation hides the content of a function. From the outside, only the postcondition and the preconditions of the function will be visible. This can be used to speed up the verification process or to make the rest of the program independent of the implementation of that function. Be aware that if a function is opaque, its postconditions generally need to be stronger (because they fully need to characterize the function's behavior). Other annotations are described in the [official documentation](https://epfl-lara.github.io/stainless/library.html#annotations).

