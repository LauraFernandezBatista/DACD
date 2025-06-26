Breve descripción del proyecto y su propuesta de valor. 
Este proyecto implementa un sistema modular y distribuido orientado a la captura, almacenamiento y análisis de eventos informativos (noticias y vídeos) obtenidos de fuentes externas mediante APIs públicas 
como NewsAPI y YouTube Data API. Está diseñado para funcionar tanto en tiempo real como en modo histórico (diferido), permitiendo una monitorización continua y una exploración retrospectiva de la información 
recopilada.
El sistema se compone de varios módulos autónomos pero interconectados, que siguen los principios de desacoplamiento, reutilización y escalabilidad. Estos módulos incluyen:

-Feeder de noticias (news-feeder)
Encargado de consultar la NewsAPI según un término proporcionado por el usuario, y publicar los resultados como eventos estructurados.
-Feeder de vídeos (youtube-feeder)
Funciona de manera análoga al anterior, pero consumiendo datos de YouTube Data API.
-Event Store Builder (eventstore-builder)
Actúa como consumidor de eventos en tiempo real a través de ActiveMQ y los persiste en archivos organizados jerárquicamente según tópico, fuente y fecha, conformando así un eventstore.
-Unidad de negocio (business-unit)
Proporciona una interfaz gráfica para consultar el historial de eventos procesados, accediendo directamente a los archivos del eventstore, y opcionalmente persistiéndolos en un DataMart local (SQLite).

El proyecto aporta valor al usuario final y al sistema en general mediante:
-Centralización de información: unifica la adquisición de datos desde múltiples fuentes heterogéneas en una estructura común y consultable.
-Procesamiento en tiempo real: mediante el uso de mensajería asincrónica (ActiveMQ), se consigue una arquitectura reactiva capaz de recibir eventos de forma inmediata.
-Persistencia histórica: los eventos no solo se procesan en directo, sino que se almacenan de forma ordenada y accesible para futuros análisis o auditorías.
-Interfaz gráfica accesible: permite al usuario final consultar el historial completo de eventos recopilados sin necesidad de conocimientos técnicos, presentando la información en formato cronológico y clasificada por fuente.
-Diseño modular y extensible: la arquitectura facilita la incorporación de nuevas fuentes de datos o tipos de análisis sin modificar el resto de componentes, alineándose con principios de bajo acoplamiento.


Justificación de la elección de APIs y estructura del datamart. 
NewsAPI:
Esta API permite realizar búsquedas personalizadas de artículos de noticias en tiempo real, obteniendo títulos, descripciones, URLs y fechas de publicación. Se eligió por su facilidad de integración, documentación 
clara y gran variedad de medios disponibles.
Su uso aporta al sistema un flujo constante de información periodística estructurada y actualizada, útil para monitorizar temas de actualidad.

YouTube Data API:
Esta API ofrece acceso a metadatos de videos disponibles en YouTube relacionados con una búsqueda específica. Permite obtener títulos, descripciones, URLs, fechas de publicación y canales de origen.
Se integra perfectamente con sistemas de análisis de contenido multimedia y aporta una dimensión audiovisual al sistema, complementando los datos textuales de las noticias.


El módulo business-unit incluye un DataMart embebido en SQLite, diseñado para almacenar los eventos transformados (NewsEvent, VideoEvent) en tablas normalizadas.
El DataMart está diseñado para recibir eventos desde dos fuentes:
-Eventos nuevos recibidos en tiempo real gracias al BrokerSubscriber.
-Eventos históricos cargados desde archivos .events ubicados en el eventstore.
De este modo, el sistema combina lo mejor de dos mundos:
-Persistencia bruta en archivos legibles (eventstore/, como log de eventos).
-Persistencia estructurada en base de datos (datamart, para consultas y análisis).
Esta dualidad responde directamente a la filosofía de la arquitectura Lambda, donde los datos se almacenan tanto en crudo como en forma estructurada para dar soporte a diferentes tipos de procesamiento y análisis.

Instrucciones claras para compilar y ejecutar cada módulo.
Con activemq corriendo:
1. Ejecutar los Feeders (news-feeder y youtube-feeder)
-Accede al módulo correspondiente:
news-feeder/src/main/java/com/newsfeeder/Main.java
youtube-feeder/src/main/java/com/youtube/Main.java
-Ejecuta la clase Main.
-El programa te pedirá por consola que introduzcas un tema de búsqueda (por ejemplo, clima, tecnología, etc.).
-Se publicarán eventos en tiempo real en ActiveMQ.

2.Ejecutar el EventStore Builder
-Ve a: eventstore-builder/src/main/java/com/builder/Main.java
-Ejecuta la clase Main.
-Este módulo se suscribirá a los eventos publicados en ActiveMQ y los almacenará en el directorio eventstore/{topic}/{ss}/{YYYYMMDD}.events.

3.Ejecutar la Business Unit (Interfaz gráfica)
-Accede a: business-unit/src/main/java/com/business/Application.java
-Ejecuta el Main del módulo.
-Se abrirá una interfaz gráfica que muestra el historial de eventos almacenados en eventstore.
Si mientras tienes esta interfaz abierta vuelves a ejecutar algún feeder y el eventstore-builder, podrás actualizar la vista pulsando el botón "Actualizar historial".

Ejemplos de uso (consultas, peticiones REST, etc.). 
-Ejecuta youtube-feeder y escribe por consola el tema: clima.
-A continuación, ejecuta eventstore-builder. Este recibirá y guardará los eventos en archivos.
-Finalmente, ejecuta business-unit. Verás en la interfaz los eventos que has introducido.
Si vuelves a repetir el proceso con otro tema, puedes actualizar la interfaz usando el botón.

Arquitectura de sistema y arquitectura de la aplicación (con diagramas). 
Los diagramas se encuentran en la carpeta "Diagramas"

Principios y patrones de diseño aplicados en cada módulo.
-Arquitectura basada en eventos (Event-driven architecture):
Los feeders publican eventos, y consumidores reaccionan a ellos, favoreciendo desacoplamiento.

-Publisher/Subscriber (Pub/Sub) con ActiveMQ:
Uso de topics para distribuir eventos a múltiples consumidores sin dependencia directa.

-Persistencia separada (Event Store):
Almacenamiento de eventos originales en archivos para recuperación y auditoría.

-DataMart para análisis:
Uso de base de datos relacional para consultas eficientes y estructuradas.

-Patrón Singleton / Threading:
Consumidor durable en segundo hilo para procesamiento en background.

-MVC simplificado en GUI:
Interfaz gráfica separada de la lógica de negocio y acceso a datos.

-Uso de JSON y Gson para serialización:
Facilita transporte y almacenamiento de eventos en formato estándar.
