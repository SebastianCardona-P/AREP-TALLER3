# MicroSpringBoot Framework - AREP Taller 3

Un servidor Web tipo Apache construido en Java que proporciona un framework IoC (Inversión de Control) para la construcción de aplicaciones web a partir de POJOs (Plain Old Java Objects). Este proyecto implementa las capacidades reflexivas de Java para cargar automáticamente beans y derivar aplicaciones web a partir de ellos.

## Descripción del Proyecto

Este framework permite a los desarrolladores crear aplicaciones web modernas utilizando:

- **Servidor Web HTTP** - Capaz de entregar páginas HTML, CSS, JavaScript e imágenes PNG/JPG
- **Framework IoC** - Construcción automática de aplicaciones web a partir de POJOs
- **Capacidades Reflexivas** - Carga automática de beans usando anotaciones
- **Manejo de Solicitudes No Concurrentes** - Procesamiento secuencial de múltiples solicitudes
- **Soporte para Anotaciones** - `@RestController`, `@GetMapping`, `@RequestParam`

## Características Principales

### 1. Framework IoC con Anotaciones

El framework utiliza las capacidades reflexivas de Java para descubrir y cargar automáticamente componentes:

```java
@RestController
public class GreetingController {
    
    @GetMapping("/greeting")
    public static String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
}
```

### 2. Servidor Web HTTP

- Sirve archivos estáticos (HTML, CSS, JS, imágenes)
- Maneja múltiples tipos de contenido con headers apropiados
- Procesamiento de solicitudes HTTP GET
- Respuestas 404 para recursos no encontrados

### 3. Exploración Automática de Componentes

El framework explora automáticamente el classpath buscando clases con `@RestController`:

```java
mvn clean install
// Carga automática - no requiere especificar controladores
java -cp target/classes co.edu.escuelaing.microsptingboot.MicroSptingBoot

// Carga específica - especificar controlador particular
java -cp target/classes co.edu.escuelaing.microsptingboot.MicroSptingBoot co.edu.escuelaing.microsptingboot.controller.GreetingController
```

## Servicios REST Disponibles

El framework incluye los siguientes endpoints de ejemplo:

### GreetingController

- **GET** `/app/greeting?name=valor` - Saludo personalizado
  - Ejemplo: `http://localhost:35000/app/greeting?name=Sebastian`
  - Respuesta: `"Hola Sebastian"`

- **GET** `/app/hello?name=valor&age=valor` - Saludo con edad
  - Ejemplo: `http://localhost:35000/app/hello?name=María&age=25`
  - Respuesta: `"Hola hola María, tienes 25 años"`

- **GET** `/app/status` - Estado del servidor
  - Ejemplo: `http://localhost:35000/app/status`
  - Respuesta: `"El servidor está funcionando correctamente"`

- **GET** `/app/welcome?name=valor&age=valor&city=valor` - Bienvenida completa
  - Ejemplo: `http://localhost:35000/app/welcome?name=Carlos&age=30&city=Bogotá`
  - Respuesta: `"Bienvenido Carlos, tienes 30 años y vives en Bogotá"`

### CalcuteController

- **GET** `/app/calculate/suma?a=valor&b=valor` - Operación de suma
  - Ejemplo: `http://localhost:35000/app/calculate/suma?a=15&b=25`
  - Respuesta: `"La suma de 15 + 25 = 40"`

- **GET** `/app/calculate/resta?a=valor&b=valor` - Operación de resta
  - Ejemplo: `http://localhost:35000/app/calculate/resta?a=50&b=20`
  - Respuesta: `"La resta de 50 - 20 = 30"`

### Importante
Puedes añadir más controladores y servicios siguiendo el mismo patrón, simplemente creando nuevas clases anotadas con `@RestController` y métodos con `@GetMapping`, pues por ahora el framework solo soporta métodos estáticos. con anotaciones Get y devuelve solo valores de tipo String.

## Instalación y Ejecución

### Prerrequisitos

- **Java 21** o superior
- **Apache Maven 3.6** o superior
- **Git** (para clonar el repositorio)

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/SebastianCardona-P/AREP-TALLER3.git
   cd microSptingBoot
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

3. **Ejecutar la aplicación**

   **Opción 1: Usando Maven**
   ```bash
   mvn clean compile exec:java
   ```

   **Opción 2: Usando Java directamente**
   ```bash
   java -cp .\target\classes\ co.edu.escuelaing.microsptingboot.MicroSptingBoot
   ```

   **Opción 3: Cargar controlador específico**
   ```bash
   java -cp .\target\classes\ co.edu.escuelaing.microsptingboot.MicroSptingBoot co.edu.escuelaing.microsptingboot.controller.GreetingController
   ```

4. **Verificar el funcionamiento**
   
   Abrir el navegador y visitar:
   ```
   http://localhost:35000
   ```

## Arquitectura del Sistema

### Estructura del Proyecto

```
microSptingBoot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── co/edu/escuelaing/microsptingboot/
│   │   │       ├── MicroSptingBoot.java           # Clase principal
│   │   │       ├── annotations/                   # Anotaciones del framework
│   │   │       │   ├── RestController.java
│   │   │       │   ├── GetMapping.java
│   │   │       │   └── RequestParam.java
│   │   │       ├── controller/                    # Controladores de ejemplo
│   │   │       │   ├── GreetingController.java
│   │   │       │   └── CalcuteController.java
│   │   │       └── httpServer/                    # Núcleo del servidor
│   │   │           ├── HttpServer.java
│   │   │           ├── HttpRequest.java
│   │   │           └── HttpResponse.java
│   │   └── resources/                             # Archivos estáticos
│   │       ├── index.html
│   │       ├── styles/
│   │       ├── scripts/
│   │       └── images/
│   └── test/                                      # Pruebas unitarias
│       └── java/
├── pom.xml                                        # Configuración Maven
└── README.md                                      # Documentación
```

### Componentes Principales

#### 1. HttpServer
- **Gestión de sockets** - Acepta y maneja conexiones de clientes
- **Enrutamiento de solicitudes** - Dirige requests a handlers apropiados
- **Servicio de archivos estáticos** - Sirve HTML, CSS, JS e imágenes
- **Registro de servicios** - Administra mappings de servicios REST
- **Exploración reflexiva** - Descubre automáticamente controladores

#### 2. Sistema de Anotaciones
- **@RestController** - Marca clases como controladores REST
- **@GetMapping** - Define endpoints HTTP GET con rutas específicas
- **@RequestParam** - Extrae parámetros de query con valores por defecto

#### 3. HttpRequest
- **Extracción de parámetros** - Parsea parámetros URL usando `getValue()`
- **Procesamiento URI** - Maneja paths y query strings
- **Mapeo de parámetros** - Convierte query strings a pares clave-valor

#### 4. Framework IoC
- **Carga automática de beans** - Descubre clases con `@RestController`
- **Inyección de dependencias** - Resuelve parámetros con `@RequestParam`
- **Gestión del ciclo de vida** - Administra instancias de componentes

## Uso para Desarrolladores Externos

### Creando un Nuevo Controlador

1. **Crear la clase del controlador**
   ```java
   package co.edu.escuelaing.microsptingboot.controller;

   import co.edu.escuelaing.microsptingboot.annotations.*;

   @RestController
   public class MyController {
       
       @GetMapping("/myservice")
       public static String myService(
           @RequestParam(value = "param1", defaultValue = "default") String param1,
           @RequestParam(value = "param2", defaultValue = "0") String param2
       ) {
           return "Response: " + param1 + " - " + param2;
       }
   }
   ```

2. **Compilar y ejecutar**
   ```bash
   mvn clean compile
   java -cp target/classes co.edu.escuelaing.microsptingboot.MicroSptingBoot
   ```

3. **Probar el servicio**
   ```
   http://localhost:35000/app/myservice?param1=test&param2=123
   ```

### Configuración de Archivos Estáticos

```java
// En el método main o durante la inicialización
HttpServer.staticfiles("/public");  // Servir desde target/classes/public/
```

### Manejo de Parámetros

```java
@GetMapping("/calculate")
public static String calculate(
    @RequestParam(value = "operation", defaultValue = "sum") String op,
    @RequestParam(value = "a", defaultValue = "0") String a,
    @RequestParam(value = "b", defaultValue = "0") String b
) {
    // Lógica de negocio aquí
    return "Result: " + result;
}
```

## Pruebas Automatizadas

El proyecto incluye una suite completa de pruebas que valida:

### Cobertura de Pruebas

- ✅ **GreetingControllerTest** - Pruebas de todos los métodos del controlador de saludos
- ✅ **CalcuteControllerTest** - Pruebas de operaciones matemáticas y manejo de errores
- ✅ **HttpServerTest** - Pruebas de lectura de archivos estáticos y funcionalidad del servidor
- ✅ **HttpRequestTest** - Pruebas de extracción de parámetros de query
- ✅ **AnnotationsTest** - Pruebas de funcionamiento de anotaciones del framework
- ✅ **MicroSptingBootIntegrationTest** - Pruebas de integración extremo a extremo

### Ejecutar las Pruebas

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar pruebas específicas
mvn test -Dtest=GreetingControllerTest

# Ver reporte de cobertura
# Los reportes se generan en target/surefire-reports/
```

### Funcionalidades Probadas

- Carga automática de controladores con `@RestController`
- Registro correcto de endpoints con `@GetMapping`
- Extracción de parámetros con `@RequestParam` y valores por defecto
- Servicio de archivos estáticos (HTML, CSS, JS, imágenes)
- Manejo de errores y casos límite
- Procesamiento de múltiples solicitudes no concurrentes

## Tipos de Contenido Soportados

| Extensión | Content-Type | Descripción |
|-----------|--------------|-------------|
| `.html` | `text/html` | Documentos HTML |
| `.css` | `text/css` | Hojas de estilo |
| `.js` | `text/javascript` | Archivos JavaScript |
| `.png` | `image/png` | Imágenes PNG |
| `.jpg/.jpeg` | `image/jpg` | Imágenes JPEG |
| `.ico` | `image/ico` | Archivos de iconos |

## Características del Framework IoC

### Capacidades Reflexivas

- **Exploración automática del classpath** - Encuentra todas las clases anotadas
- **Análisis de anotaciones en runtime** - Procesa `@RestController`, `@GetMapping`, `@RequestParam`
- **Invocación dinámica de métodos** - Ejecuta servicios usando reflection
- **Resolución de parámetros** - Mapea automáticamente query parameters a parámetros de método

### Gestión de Beans

- **Carga automática** - No requiere configuración XML o manual
- **Registro dinámico** - Servicios disponibles inmediatamente después del descubrimiento
- **Soporte para POJOs** - Cualquier clase Java puede convertirse en controlador

## Limitaciones Actuales

- **Procesamiento no concurrente** - Maneja una solicitud a la vez
- **Solo métodos estáticos** - Los métodos de controlador deben ser static
- **Parámetros tipo String** - Solo soporta parámetros de entrada tipo String
- **Respuestas tipo String** - Solo retorna contenido tipo String

## Construcción y Deployment

### Construcción

```bash
# Limpiar y compilar
mvn clean compile

# Ejecutar pruebas
mvn test

# Crear package
mvn package
```

### Deployment

```bash
# Ejecutar aplicación
java -cp target/classes co.edu.escuelaing.microsptingboot.MicroSptingBoot

# O con controlador específico
java -cp target/classes co.edu.escuelaing.microsptingboot.MicroSptingBoot co.edu.escuelaing.microsptingboot.controller.GreetingController
```

## Tecnologías Utilizadas

- **[Java 21](https://openjdk.org/projects/jdk/21/)** - Lenguaje de programación y runtime
- **[Maven](https://maven.apache.org/)** - Gestión de dependencias y build
- **[JUnit 5](https://junit.org/junit5/)** - Framework de testing
- **[Mockito](https://mockito.org/)** - Mocking para pruebas
- **Java Reflection API** - Capacidades reflexivas
- **Java Socket API** - Comunicación de red
- **Java NIO** - Operaciones del sistema de archivos

## Autor

- **Sebastian Cardona** - *Desarrollo inicial* - [SebastianCardona-P](https://github.com/SebastianCardona-P)

## Agradecimientos

- Curso AREP en la Escuela Colombiana de Ingeniería Julio Garavito
- Especificación HTTP/1.1 (RFC 2616)
- Patrones de diseño de frameworks web modernos
- Principios de Inversión de Control (IoC)

---

