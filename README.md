## Descripción breve del proyecto
Esta aplicación permite gestionar convenios de pago para clientes. 
Incluye la funcionalidad de calcular pagos programados basados en un monto total, un monto inicial, 
una periodicidad y un día de pago preferido. 
Los datos se almacenan en una base de datos MySQL

## Instrucciones para ejecutar la aplicación
1. **Requisitos previos**:
    - Java 17 o superior.
    - Maven 3.8 o superior.
2. **Clonar el repositorio**:
3. IDE IJ
4. Configurar la base de datos MySQL:
   - Crear una base de datos llamada `convenio_pagos`.
   - Actualizar las credenciales de la base de datos en el archivo `src/main/resources/application.properties`.
5. Configurar plugin lombok en IDE IJ
 ```bash
 git clone <URL_DEL_REPOSITORIO>
 cd convenios-periodicos-aldia

Compilar el proyecto:  
mvn clean package install
Ejecutar la aplicación:  
mvn spring-boot:run
Acceder a la aplicación:  
La API estará disponible en: http://localhost:8080/convenio-pagos

##Request
{
    "idCliente": 1,
    "montoTotal": 1020.00,
    "montoInicial": 270.00,
    "fechaPrimerPago": "2025-10-07",
    "diaPago": "LUNES",
    "periodicidad": "SEMANAL",
    "montoPorPeriodo": 250.00
}

##Reponse
{
    "idCliente": 1,
    "montoTotal": 1020.00,
    "montoInicial": 270.00,
    "fechaPrimerPago": "2025-10-07",
    "diaPago": "LUNES",
    "periodicidad": "SEMANAL",
    "montoPorPeriodo": 250.00
}