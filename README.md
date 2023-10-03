# AREP-PATRONES-ARQUITECTURALES

## Introducción:

El proyecto se enfoca en la implementación de un sistema distribuido que utiliza tecnologías como MongoDB, Docker, EC2 de Amazon Web Services (AWS), y servicios REST para crear una aplicación web llamada APP-LB-RoundRobin. Este sistema permite el almacenamiento y recuperación de cadenas de texto a través de instancias de servicio llamadas LogService, que se ejecutan en contenedores Docker en máquinas virtuales EC2.

## Resumen del Proyecto:

El servicio MongoDB se despliega como una instancia de MongoDB dentro de un contenedor Docker en una máquina virtual de EC2. Este servicio actúa como la base de datos centralizada para almacenar información.

Por otro lado, el servicio LogService se presenta como un servicio REST que acepta cadenas de texto, las almacena en la base de datos MongoDB y responde con un objeto JSON que contiene las 10 últimas cadenas almacenadas junto con sus fechas de almacenamiento. Este servicio se implementa en tres instancias, cada una de las cuales utiliza un algoritmo de balanceo de carga de Round Robin para distribuir la carga de procesamiento y garantizar la eficiencia del sistema.

La aplicación web APP-LB-RoundRobin consta de un cliente web que incluye un campo de entrada y un botón. Cuando el usuario envía un mensaje desde el cliente web, este se comunica con el servicio REST correspondiente en el LogService. Después de procesar el mensaje, el servicio REST utiliza un algoritmo de Round Robin para distribuir la carga entre las tres instancias de LogService. La respuesta JSON obtenida se actualiza en la pantalla del cliente web.

## Imagen de Guia

![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/f304a681-da9a-4939-ba41-495291e79a16)

## AWS EC2

### Pasos:

1) Ingresamos a la carpeta donde tenemos nuestra llave generada:

   ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/177c63ce-4097-4a51-a987-dce7cacd28a9)

2) Nos conectamos:

   ![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/1c488a48-b889-428b-a87c-8c5997e0a24c)

3) Puede ser necesario realizar lo siguiente antes:

    a) Instale Docker
      sudo yum update -y
      sudo yum install docker
    b) Inicie el servicio de docker
      sudo service docker start
    c) Configure su usuario en el grupo de docker para no tener que ingresar “sudo” cada vez que invoca un comando
      sudo usermod -a -G docker ec2-user

     d) Desconectes de la máquina virtual e ingrese nuevamente para que la configuración de grupos de usuarios tenga efecto.

5) Ejecutamos los siguientes comandos en nuestro cmd:

a) docker network create arep_network 

![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/16d7a7b0-0eb3-47f4-9388-4603f03e2fad)

b) docker run -d -p 35000:6000 --name roundrobincontainer --network arep_network danielsperezb/roundrobin

![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/251945cd-f5b8-499f-9957-041bb92cb09d)

c) docker run -d -p 35001:6000 --name firstlogservicecontainer --network arep_network danielsperezb/logservicedate

![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/b362597a-a020-421e-b38e-43c1a4f2ba8b)

d) docker run -d -p 35002:6000 --name secondlogservicecontainer --network arep_network danielsperezb/logservicedate

![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/789b2855-613e-4403-917a-94ed71922611)

e) docker run -d -p 35003:6000 --name thirdlogservicecontainer --network arep_network danielsperezb/logservicedate

![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/37119ccd-6543-4032-9e15-d8c1c10d340d)

f) docker run -d -p 27017:27017 --name db --network arep_network danielsperezb/mongodb

![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/34d5420d-3152-43fb-bc9e-3842d8cd1589)

