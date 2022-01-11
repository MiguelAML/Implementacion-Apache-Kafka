package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) {
        //Facilitador de logs en consola
        final Logger logger = LoggerFactory.getLogger(Consumer.class);
        //Propiedades finales de la clase
        final String bootstrapServers = "52.188.206.15:9092";
        final String consumerGroupID = "java-group-consumer";
        //Creación de objeto properties para Consumer
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,consumerGroupID);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        //Creación de objeto Consumer
        final KafkaConsumer<String,String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        //Subscripción de consumer a los tópicos. Recordar la teoría de que un consumidor en un grupo no puede consumir de un tópico que ya
        //es consumido por un consumidor de su mismo grupo
        kafkaConsumer.subscribe(Arrays.asList("sample-topic"));

        //Es un oyente que cada segundo(1000Milisegundos) recupera los mensajes
        //La mejor práctica es tener este código en un HILO. Revisar código de UFISI
        while(true){
            ConsumerRecords<String,String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
            for(ConsumerRecord record : consumerRecords){
                logger.info("Received new record: \n"+
                        "Key: "+record.key()+", "+
                        "Value: "+record.value()+", "+
                        "Topic: "+record.topic()+", "+
                        "Partition: "+record.partition()+", "+
                        "Offset: "+record.offset()+", "+ "\n");
            }
        }
    }
}
