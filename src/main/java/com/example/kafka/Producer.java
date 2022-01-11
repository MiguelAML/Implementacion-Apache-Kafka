package com.example.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {
    public static void main(String[] args) {
        //Facilitador de logs en consola
        final Logger logger = LoggerFactory.getLogger(Producer.class);

        //Crear objeto Propiedades para el objeto Productor
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "52.188.206.15:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //Crear objeto Productor
        final KafkaProducer<String,String> kafkaProducer = new KafkaProducer<String, String>(properties);
        //Crear objeto RecordProductor
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>("sample-topic","Lapiz001",new Producto("Lapíz",10D).toString());
        //Enviar data
        kafkaProducer.send(producerRecord, new Callback() {
            //Enviamos el record y Kafka nos devuelve los metadatos de la inserción,o el error ocurrido en caso no haya sido posible enviarse.
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e==null){
                    //Se escribió el mensaje en Kafka
                    logger.info("\nReceived record metada \n"+
                            "Topic: "+recordMetadata.topic()+ ", Partition: "+recordMetadata.partition()+", "+
                            "Offset: "+recordMetadata.offset()+" @ Timestamp: "+recordMetadata.timestamp()+"\n");
                }else{
                    //No se escribió el mensaje en Kafka
                    logger.error("Error Ocurred",e);
                }
            }
        });
        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
