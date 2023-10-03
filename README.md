# AREP-PATRONES-ARQUITECTURALES

## Introducción:

El proyecto se enfoca en la implementación de un sistema distribuido que utiliza tecnologías como MongoDB, Docker, EC2 de Amazon Web Services (AWS), y servicios REST para crear una aplicación web llamada APP-LB-RoundRobin. Este sistema permite el almacenamiento y recuperación de cadenas de texto a través de instancias de servicio llamadas LogService, que se ejecutan en contenedores Docker en máquinas virtuales EC2.

## Resumen del Proyecto:

El servicio MongoDB se despliega como una instancia de MongoDB dentro de un contenedor Docker en una máquina virtual de EC2. Este servicio actúa como la base de datos centralizada para almacenar información.

Por otro lado, el servicio LogService se presenta como un servicio REST que acepta cadenas de texto, las almacena en la base de datos MongoDB y responde con un objeto JSON que contiene las 10 últimas cadenas almacenadas junto con sus fechas de almacenamiento. Este servicio se implementa en tres instancias, cada una de las cuales utiliza un algoritmo de balanceo de carga de Round Robin para distribuir la carga de procesamiento y garantizar la eficiencia del sistema.

La aplicación web APP-LB-RoundRobin consta de un cliente web que incluye un campo de entrada y un botón. Cuando el usuario envía un mensaje desde el cliente web, este se comunica con el servicio REST correspondiente en el LogService. Después de procesar el mensaje, el servicio REST utiliza un algoritmo de Round Robin para distribuir la carga entre las tres instancias de LogService. La respuesta JSON obtenida se actualiza en la pantalla del cliente web.

## Imagen de Guía

![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/f304a681-da9a-4939-ba41-495291e79a16)

## AWS EC2

### Pasos:

1) Ingresamos a la carpeta donde tenemos nuestra llave generada:

   ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/177c63ce-4097-4a51-a987-dce7cacd28a9)

2) Nos conectamos:

   ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/1c488a48-b889-428b-a87c-8c5997e0a24c)

3) Puede ser necesario realizar lo siguiente antes:

    a) Instale Docker
      ```bash
      sudo yum update -y
      ```
      
      ```bash
      sudo yum install docker
      ```
    b) Inicie el servicio de docker
      ```bash
      sudo service docker start
      ```
    c) Configure su usuario en el grupo de docker para no tener que ingresar “sudo” cada vez que invoque un comando
      ```bash
      sudo usermod -a -G docker ec2-user
      ```
    d) Desconéctese de la máquina virtual e ingrese nuevamente para que la configuración de grupos de usuarios tenga efecto.

5) Ejecutamos los siguientes comandos en nuestro cmd:

    a) 
    ```bash
    docker network create arep_network
    ```

    ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/16d7a7b0-0eb3-47f4-9388-4603f03e2fad)

    b) 
    ```bash
    docker run -d -p 35000:6000 --name roundrobincontainer --network arep_network danielsperezb/roundrobin
    ```

    ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/251945cd-f5b8-499f-9957-041bb92cb09d)

    c) 
    ```bash
    docker run -d -p 35001:6000 --name firstlogservicecontainer --network arep_network danielsperezb/logservicedate
    ```

    ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/b362597a-a020-421e-b38e-43c1a4f2ba8b)

    d) 
    ```bash
    docker run -d -p 35002:6000 --name secondlogservicecontainer --network arep_network danielsperezb/logservicedate
    ```

    ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/789b2855-613e-4403-917a-94ed71922611)

    e) 
    ```bash
    docker run -d -p 35003:6000 --name thirdlogservicecontainer --network arep_network danielsperezb/logservicedate
    ```

    ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/37119ccd-6543-4032-9e15-d8c1c10d340d)

    f) 
    ```bash
    docker run -d -p 27017:27017 --name db --network arep_network danielsperezb/mongodb
    ```

    ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/34d5420d-3152-43fb-bc9e-3842d8cd1589)

#### Notas Adicionales:

1) Recuerda en la parte de "Security" agregar los puertos necesarios que se van a usar:

    ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/2d04fefd-16bb-47c7-97a5-7037bf872b52)

2) Copiamos la "Public IPv4 DNS"

    ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/25a35dba-cc15-49ea-99c8-cda967d53fcb)

   Y finalmente buscamos en nuestro navegador: ec2-44-207-5-41.compute-1.amazonaws.com:35000

#### Adicionales:

a) Si quieres eliminar los contenedores y imagenes en tu docker en la maquina virtual 
```bash
docker network rm arep_network
```

```bash
docker stop $(docker ps -a -q)
```
```bash
docker rm $(docker ps -a -q)
```
```bash
docker rmi $(docker images -q)
```


## Docker de Manera Local para LogRoundRobin

### Instrucciones para Docker en tu Máquina Local:

1. Abrir Docker en tu máquina local.

2. Ejecutar el siguiente comando dentro del repositorio (carpeta) LogRoundRobin:

    ```bash
    docker-compose up -d
    ```

### Funcionamiento Local sin Docker y Transformación a Docker de Manera Local

Al iniciar este laboratorio, sigue los pasos a continuación:

1. Desde tu IDE preferido, como NetBeans, inicia LogRoundRobin.

2. Abre 3 terminales (cmd) para lanzar 3 instancias de LogService:

    ```bash
    java -cp "target/classes;target/dependency/*" co.edu.escuelaing.logservice.LogService
    ```

    ```bash
    SET PORT=4569
    ```

    ```bash
    java -cp "target/classes;target/dependency/*" co.edu.escuelaing.logservice.LogService
    ```

    ```bash
    SET PORT=4569
    ```

    ```bash
    java -cp "target/classes;target/dependency/*" co.edu.escuelaing.logservice.LogService
    ```

3. Crea el archivo `docker-compose.yml` dentro de LogRoundRobin para evitar ejecuciones repetitivas. También, actualiza la variable `LOG_SERVICES` en la clase `HttpRemoteCaller` de:

    ```java
    private static final String[] LOG_SERVICES = new String[]{
        "http://localhost:4568/logservice",
        "http://localhost:4569/logservice",
        "http://localhost:4570/logservice"
    };
    ```

    a:

    ```java
    private static final String[] LOG_SERVICES = new String[]{
        "http://firstlogservicecontainer:6000/logservice",
        "http://secondlogservicecontainer:6000/logservice",
        "http://thirdlogservicecontainer:6000/logservice"
    };
    ```

    donde ahora se utilizan los contenedores y el puerto de ambiente para su correcto funcionamiento al ejecutar `docker-compose.yml`.

### Creación de Imágenes Docker Hub

Para poder llamarlas en el docker-compose.yml y tambien funcional a la hora de hacerlo en  AWS

![Docker Hub Images](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/21ed7698-193f-437c-b6ba-55a4b47a655b)

