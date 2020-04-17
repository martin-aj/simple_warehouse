# Simple Warehouse API
Provides basic CRUD operations above products and orders.
Orders older than 30 minutes are invalidated and proper amount of products returned to warehouse.

#### Project setup
The steps bellow shows the setup specifically for Intellij IDEA as a recommended IDE for this project.
However, it is possible to use an another IDE, but the support is not guaranteed.

1. Import the project as a Gradle project with use of gradle wrapper task configuration
2. Install Lombok plugin (Preferences --> Plugins --> Browse repositories... --> Search for 'Lombok' --> Install) 

> Note that Annotation Processing in IDEA should be enabled by importing the project using Gradle ('net.ltgt.apt-idea' plugin). 
> If not, you need to enable Annotation Processing manually: Preferences --> Build, Execution, Deployment --> Compiler --> 
> Annotation Processors --> Enable Annotation processing.

#### Swagger
<http://localhost:8080/swagger-ui.html>
