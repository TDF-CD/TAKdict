# TAK-CoT 

## Environment setup

### 1. Install Java 18

To install Java on your machine, please follow the linked instructions:

https://studyopedia.com/java/install-java-windows/

### 2. Install IntelliJ

Download and install IntelliJ from here:

https://www.jetbrains.com/idea/download/#section=windows

You can use any other preferred code editor instead.

### 3. Install GIT

Download GIT from here:
https://git-scm.com/downloads
and follow the linked instructions:
https://github.com/git-guides/install-git

### 4. Clone the ATAK-CoT repo

At first, the SSH connection with GitLab account should be configured. To achieve that, generate the SSH key pair and insert the public key in your GitLab account. Please find the detailed description here:

https://www.theserverside.com/blog/Coffee-Talk-Java-News-Stories-and-Opinions/How-to-configure-GitLab-SSH-keys-for-secure-Git-connections

Once successfully connected, open GIT Bash. Navigate to the desired location and enter the command below to clone the repo:
```
git clone git@github.com:TDF-CD/TAKdict.git
```

### 5. Run CsvMapper

Compile CsvMapper class using the command:
```
javac CsvMapper.java
```

#### 5.1. With the default delimiter - `;`

6 arguments are required:
- path (with the filename) to the CSV file
- path (with the filename) to the public key file
- path (with the filename) to the signature file
- searched term
- source column number (that the provided term is searched in)
- destination column (that the result is returned from)
7th argument is optional - delimiter.

Example:
```
java -cp target/tak-1.0-SNAPSHOT.jar org.wot.tak.Main src/main/resources/APP6B_to_CoT.csv src/main/resources/pub.rsa src/main/resources/APP6B_to_CoT.csv.sig SUG-IRSR-- 0 2
```

#### 5.2. With the custom delimiter

Provide CSV delimiter as the 5th optional argument.

Example:
```
java -cp target/tak-1.0-SNAPSHOT.jar org.wot.tak.Main src/main/resources/APP6B_to_CoT.csv src/main/resources/pub.rsa src/main/resources/APP6B_to_CoT.csv.sig SUG-IRSR-- 0 2 ","
```

## Licence

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 