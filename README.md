# Creación de un servidor Apache Kafka en Azure

En esta guía explicaremos como implementar un servidor de Apache Kafka con procesos en background en una máquina virtual en la nube de Azure 

## Creación de una máquina virtual en Azure

Evite crear una VM con menos de 2gb de RAM 

- Ingresar a `https://portal.azure.com`
- Clic en `Crear un recurso`
- Crear Máquinas Virtuales
- Escoger alguna imágen de Linux y el tamaño de la máquina virtual. (Recomendable Ubuntu o Debian)
- Tipo de autenticación: Clave pública SSH
- Puertos de entrada público: Permitir los puertos seleccionados(Seleccione SSH,HTTP y HTTPS)
- Crear recurso
- Descargar la clave privada generada y guardarla en una dirección conocida.

## Configuración de la máquina virtual

- Accede al recurso creado
- Configuración -> Redes -> Agregar regla de puerto de entrada  
  Prioridad = 100   
  Nombre = kafka_port  
  Puerto = 9092,2181  
  Protocolo = Cualquiera  
  Origen = Cualquiera  
  Destino = Cualquiera  
  Acción = Permitir  
- Configuración -> Redes -> Agregar regla de puerto de salida  
  Prioridad = 100   
  Nombre = port_kafka  
  Puerto = 9092,2181  
  Protocolo = Cualquiera  
  Origen = Cualquiera  
  Destino = Cualquiera  
  Acción = Permitir  
- Buscar en tus recursos un recurso de tipo "Interfaz de Red"
- Dentro del recurso: Configuraciones de IP -> Habilitar Reenvío IP
- Ingresar a la configuración IP en la mísma sección(Posible nombre: ipconfig1).
- Dirección IP pública : Asociar
- Asignación : Estática
- Regresar a su recurso Maquina virtual
- Iniciar (Si no lo está)
- Configuración -> Conectar -> SSH
- Abrir consola de powershell en su local
- Pegar el comando que le sugiere Azure. En la ruta de acceso de clave privada,colocar la ruta de su clave privada descargada previamente.
- Aceptar (Escribir yes) luego de ejecutar el comando si se lo pide
## Instalación de JDK y Kafka

Una vez accedido a su máquina virtual descargará en esta las dependencias necesarias. Asegurese que su máquina virtual tenga 2gb de ram a más para evitar desborde de memoria.

Ejecutaremos los siguientes comandos: 
- `sudo apt-get update`
- `sudo apt-get install --fix-missing`
- `sudo apt install default-jre`
- `sudo apt install default-jdk`
- Verifique que se instaló con `java -version`
- Dirigase desde su computadora a la página de descarga de Kafka y copie el enlace(Por ejemplo este: https://dlcdn.apache.org/kafka/3.0.0/kafka_2.13-3.0.0.tgz) que otorga Apache 
- En la máquina virtual ejecute `wget https://dlcdn.apache.org/kafka/3.0.0/kafka_2.13-3.0.0.tgz`
- `tar -xvf kafka_2.13-2.6.0.tgz`

## Configuración y levantamiento del servidor de Kafka 
Crearemos los procesos para tener el servidor de Kafka

- `cd kafka_2.13-2.6.0`
- `cd config`
- `nano server.properties`
- Descomentar `listeners=PLAINTEXT://:9092`
- Ingresar `0.0.0.0` antes del `:9092`
- Descomentar `advertised.listeners=PLAINTEXT://your.host.name:9092`
- Ingresar la IP de tu máquina virtual en reemplazo de `your.host.name`
- Guardar cambios con `Ctrl + O`
- Presione enter (No presione nada antes ni después.Podría cambiar el nombre del archivo)
- Salga con `Ctrl + X`
- Puede ingresar a la página de Kafka y en el apartado de Quickstart verá los comandos de inicio rápido. Los ejecutará
- Primero retrocederá una jerarquia `cd ..`
- Ejecute `bin/zookeeper-server-start.sh config/zookeeper.properties`
- Una vez ejecutandose presione `Ctrl + Z` y escriba `bg` para pasarlo a segundo plano
- Ejecute `bin/kafka-server-start.sh config/server.properties`
- Una vez ejecutandose presione `Ctrl + Z` y escriba `bg` para pasarlo a segundo plano
- Ya tiene un servidor de Kafka ejecutandose

## Creación de tópicos

Creará su tópico. Tendrá también un código de prueba en Java que le permitirá visualizar y ejecutar su productor y consumidor 
`bin/kafka-topics.sh --create --partitions 1 --replication-factor 1 --topic <nombre de su tópico> --bootstrap-server <ip de su VM>:9092`

Usted ya puede cerrar su conexión con `exit`.

## Ejecución del código de prueba

En el código,tanto para el Productor como el Consumidor deberá modificar la IP y el tópico donde intenta enviar y recibir información,dentro del objeto Properties.

Buena suerte.
Cualquier consulta envieme un mensaje al correo manuttupa.ligas@gmail.com

Nota: Las configuraciones de red tanto en la máquina virtual y en el servidor son globales para evitar alargar el tutorial, pero si usted conoce su IP puede limitar el acceso. 
