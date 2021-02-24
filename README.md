# Project Name 
ENB - E-Noticing System API
# Description
This is an API implementation for the Electronic Noticing System web APP. <br/>
*Java Spring Boot* Framework is used.

# Documentation
Documentation can be found [here](https://tibebu-es.github.io/enb-api/)

# Usage


## Application Properties
### Database Parameters - postgresql is used
spring.jpa.hibernate.ddl-auto=update <br/>
spring.datasource.url=jdbc:postgresql://localhost:5432/<database name>  <br/>
spring.datasource.username=<user name>  <br/>
spring.datasource.password=<password>  <br/>
spring.jpa.show-sql=true  <br/>
spring.jpa.properties..hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect  <br/>

### File Uploading Properties
#### MULTIPART (MultipartProperties)
##### Enable multipart uploads
spring.servlet.multipart.enabled=true
##### Threshold after which files are written to disk.
spring.servlet.multipart.file-size-threshold=2KB
##### Max file size.
spring.servlet.multipart.max-file-size=200MB
##### Max Request Size
spring.servlet.multipart.max-request-size=215MB
##### File Storage Properties
file.upload-dir=./uploads

# Features
* Get list of notices
* Get a notice by ID
* Get Today's notices
* Create new Notice
* Update and delete an existing notice
* Get IDs of notice contents of an existing notice
* Add Notice Contents to an existing Notice
* Delete Notice Contents of an  existing Notice
