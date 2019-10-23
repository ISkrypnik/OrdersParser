# Orders parser application
An application is a service that can parse data from CSV and JSON input files to internal Order entity and then convert it to
output DTO Order with status of parsing, line number of original Order and file name from where it was parsed.

#### Input files
Input files path should be passed through program arguments.
In case of unsupported extension program will be terminated.

#### File extensions
To extend supported extensions OrdersFileReader should be implemented in new extension reader class. Aslo new extension
name should be added to SupportedFileExtensions enumeration.

#### Logging
By default logs write to path _C:/logs/application.log_. It can be changed in logback.xml file.

#### Output
Output is a DTO in JSON format. For current realisation output prints in console.

#### Build
Application builds by "mvn clean build" command inside of a project folder. After build JAR file can be found in target
directory.

#### Run
Application runs by "java -jar OrdersParser-1.0.jar filename". Last arg is file name. There can be several file names
to read. Should be listed with a space. 