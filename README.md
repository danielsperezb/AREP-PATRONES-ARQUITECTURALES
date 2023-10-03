# AREP-PATRONES-ARQUITECTURALES

## Introducción:

El proyecto se enfoca en la implementación de un sistema distribuido que utiliza tecnologías como MongoDB, Docker, EC2 de Amazon Web Services (AWS), y servicios REST para crear una aplicación web llamada APP-LB-RoundRobin. Este sistema permite el almacenamiento y recuperación de cadenas de texto a través de instancias de servicio llamadas LogService, que se ejecutan en contenedores Docker en máquinas virtuales EC2.

## Resumen del Proyecto:

El servicio MongoDB se despliega como una instancia de MongoDB dentro de un contenedor Docker en una máquina virtual de EC2. Este servicio actúa como la base de datos centralizada para almacenar información.

Por otro lado, el servicio LogService se presenta como un servicio REST que acepta cadenas de texto, las almacena en la base de datos MongoDB y responde con un objeto JSON que contiene las 10 últimas cadenas almacenadas junto con sus fechas de almacenamiento. Este servicio se implementa en tres instancias, cada una de las cuales utiliza un algoritmo de balanceo de carga de Round Robin para distribuir la carga de procesamiento y garantizar la eficiencia del sistema.

La aplicación web APP-LB-RoundRobin consta de un cliente web que incluye un campo de entrada y un botón. Cuando el usuario envía un mensaje desde el cliente web, este se comunica con el servicio REST correspondiente en el LogService. Después de procesar el mensaje, el servicio REST utiliza un algoritmo de Round Robin para distribuir la carga entre las tres instancias de LogService. La respuesta JSON obtenida se actualiza en la pantalla del cliente web.

## Imagen de Guia

![image](https://github.com/danielsperezb/AREP-PATRONES-ARQUITECTURALES/assets/101849347/f304a681-da9a-4939-ba41-495291e79a16)

