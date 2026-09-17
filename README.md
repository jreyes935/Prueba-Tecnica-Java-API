# Prueba Tecnica Java API

Requiere JDK 17 o superior compatible con Spring Boot 3.5. El proyecto ha sido probado en JDK 23.0.2

#   Ejecutar el proyecto

Desde la carpeta principal del repositorio:

    cd payments-api
    ./mvnw spring-boot:run

La API se ejecuta en http://localhost:8080.

#   Ejecutar los tests

Desde la carpeta payments-api

    ./mvnw test

#   Endpoints disponibles

Metodo                      Endpoints                                   Funcion

POST                        /api/payments                               Crear un pago
GET                         /api/payments/{id}                          Consultar un pago
PATCH                       /api/payments/{id}/status                   Actualizar el estado
GET                         /api/merchants/{merchantId}/payments        Listar pagos, con filtro
                                                                            opcional status
GET                         /api/merchants/{merchantId}/summary         Obtener el resumen
                                                                            del comercio

#   Ejemplos de requests

-   Crear un pago

    curl -i -X POST http://localhost:8080/api/payments \
    -H "Content-Type: application/json" \
    -d '{
        "merchantId": "MERCHANT-001",
        "amount": 150.50,
        "currency": "PAB",
        "description": "Compra#1234",
        "customerEmail": "customer@example.com"
    }'

Remplazar el ID_DEL_PAGO en los siguientes ejemplos por el ID recibido al crear el pago

-   Consultar un pago

    curl -i http://localhost:8080/api/payments/ID_DEL_PAGO

-   Actualizar el estado

    curl -i -X PATCH http://localhost:8080/api/payments/ID_DEL_PAGO/status \
    -H "Content-Type: application/json" \
    -d '{"status": "APPROVED"}'

-   Listar los pagos de un comercio

    curl -i http://localhost:8080/api/merchants/MERCHANT-001/payments

-   Listar los pagos de un comercio con filtro por estado

    curl -i "http://localhost:8080/api/merchants/MERCHANT-001/payments?status=APPROVED"

-   Consultar el resumen del comercio

    curl -i http://localhost:8080/api/merchants/MERCHANT-001/summary

#   Decisiones tecnicas
    -   Arquitectura por capas: controller, service, repository, entity, dto y exception.
    -   DTO para separar los datos de entrada y salida de la entidad persistida.
    -   Spring Data JPA y H2 en memoria para facilitar la ejecucion.
    -   BigDecimal para dinero, con precision de 19 y escala de 2 en la base de datos.
    -   UUID para generar los identificadores.
    -   Regla de transicion en el servicio, solo se permite pasar de PENDING a APPROVED, DECLINED o CANCELLED.
    -   Validacion de entradas y manejo centralizado de errores con respuestas 400, 404, 409 y 500 segun corresponda.

#   Supuestos de implementacion
    -   El correo del cliente es obligatorio y debe tener formato valido.
    -   Los importes admiten hasta 17 digitos enteros y 2 decimales.
    -   Las fechas utilizan la hora local del servidor, sin informacion de zona horaria.
    -   Los pagos se pierden al detener la aplicacion porque H2 funciona en memoria
    -   Un comercio sin pagos devuelve una lista vacia y un resumen con ceros.
    -   El total de pagos incluye los pagos pendientes y la suma de los pagos incluye unicamente los 
        aprobados.
    -   Se asume que todos los pagos del comercio estan en la misma moneda. La aplicacion no 
        comprueba esta condicion ni convierte importes entre monedas