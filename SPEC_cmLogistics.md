# SPEC cmLogistics

## Manifiesto del componente

`cmLogistics` debe completarse desde las carpetas que el proyecto ya dejo preparadas para este componente. Su responsabilidad inmediata es autenticar al tecnico de mantenimiento contra `ServidorCentral`, consultar sus maquinas asignadas, identificar cuales tienen alarmas activas y producir una orden de atencion clara.

Este componente no debe modificar inventario, cerrar alarmas directamente ni ejecutar abastecimientos. Por ahora la implementacion debe concentrarse en completar los paquetes incompletos o vacios de `cmLogistics` y en usar solo las integraciones necesarias para que esos paquetes tengan sentido.

## Estado actual

- `coffeemach/cmLogistics/src/main/java/CmLogistics.java` solo inicializa Ice con `CmLogistic.cfg`.
- `coffeemach/cmLogistics/src/main/resources/CmLogistic.cfg` ya define:
  - `ServerCentral = logistica:tcp -h localhost -p 12345`
  - `MaquinaCafe = abastecer:default -h localhost -p 12346`
- El proxy que debe usarse para consultar el servidor es `ServicioComLogisticaPrx`.
- `ServidorCentral` ya publica el objeto `logistica` mediante `ControlComLogistica`.
- El contrato Ice disponible expone:
  - `inicioSesion(int codigoOperador, string password)`
  - `asignacionMaquina(int codigoOperador)`
  - `asignacionMaquinasDesabastecidas(int codigoOperador)`
- Existen paquetes intencionales para completar la implementacion:
  - `controlAlarma`
  - `gui`
  - `tecnicoMantenimiento`
  - `zonaGeografica`
- `settings.gradle` todavia debe activar el modulo con `include 'cmLogistics'` cuando se empiece la implementacion.

## Alcance temporal

Por ahora esta spec solo debe guiar modificaciones dentro de `coffeemach/cmLogistics` y lo directamente necesario para que esas modificaciones funcionen. El trabajo se concentra en:

- `CmLogistics.java` como punto de entrada.
- `controlAlarma` como paquete de manejo de alarmas.
- `gui` como paquete de interfaz grafica Swing e interaccion con el tecnico.
- `tecnicoMantenimiento` como paquete de sesion y datos del tecnico.
- `zonaGeografica` como paquete de maquinas, ubicaciones y agrupacion territorial.
- `CmLogistic.cfg` solo si se necesita ajustar propiedades usadas por este componente.

Quedan fuera de esta spec, salvo como dependencias existentes:

- Cambiar la base de datos.
- Cambiar `ServidorCentral`.
- Cambiar `coffeeMach`.
- Cambiar `bodegaCentral`.
- Agregar nuevos contratos Ice.

La propiedad `MaquinaCafe` existe en `CmLogistic.cfg`, pero no debe guiar la implementacion inicial de `cmLogistics`: en este alcance, logistica no abastece directamente.

## Paquetes existentes y responsabilidad propuesta

Estas carpetas deben tenerse en cuenta como la estructura base del componente. Aunque hoy esten vacias, no deben ignorarse ni reemplazarse con clases sueltas en la raiz del modulo.

| Paquete | Papel en `cmLogistics` | Clases sugeridas |
| --- | --- | --- |
| `controlAlarma` | Coordinar la consulta, normalizacion y seleccion de alarmas activas asignadas al tecnico. Debe contener la logica de negocio del componente. | `ControlAlarma`, `AlarmaAsignada`, `OrdenAtencion`, `ParserAlarmaAsignada` |
| `gui` | Presentar la interfaz grafica de uso del tecnico siguiendo el estilo Swing ya existente en `coffeeMach/interfazUsuario` y `ServidorCentral/interfaz`. | `InterfazLogistica`, `ControladorLogistica` |
| `tecnicoMantenimiento` | Representar la sesion del tecnico u operador logistico autenticado, sus credenciales validadas y sus maquinas asignadas. | `TecnicoMantenimiento`, `SesionTecnico` |
| `zonaGeografica` | Agrupar y ordenar maquinas por ubicacion o zona para facilitar el recorrido de atencion. Debe partir del campo `ubicacion` recibido desde servidor. | `ZonaGeografica`, `MaquinaAsignada` |

`CmLogistics.java` debe quedar como punto de entrada: inicializa Ice, crea el proxy de `ServerCentral`, instancia las clases de estos paquetes y arranca el flujo de UI. La logica de parseo, sesion, seleccion de alarma y generacion de orden no debe quedarse acumulada en `main`.

## Patron de interfaz requerido

La interfaz de `cmLogistics` debe seguir la misma estructura de las interfaces ya hechas:

- `coffeeMach/src/main/java/interfazUsuario/Interfaz.java`
- `ServidorCentral/src/main/java/interfaz/InterfazRecetas.java`
- `ServidorCentral/src/main/java/interfaz/ControladorRecetas.java`

Por tanto, `gui` debe implementarse asi:

- Crear una clase visual, por ejemplo `gui.InterfazLogistica`, que extienda `JFrame`.
- Construir los componentes Swing en el constructor usando el estilo actual del proyecto: `JPanel`, `JLabel`, `JButton`, `JTextArea`, `JTextField`, `JScrollPane`, `JComboBox` si aplica, `setBounds(...)` y layout `null`.
- Mantener los controles como atributos privados.
- Exponer los controles necesarios mediante getters, igual que `Interfaz` e `InterfazRecetas`.
- No poner logica de negocio ni llamadas Ice directamente dentro de la clase visual.
- Crear un controlador separado, por ejemplo `gui.ControladorLogistica` o `controlAlarma.ControlAlarma`, que implemente `Runnable`.
- El controlador debe crear la ventana en `run()`, hacer `setLocationRelativeTo(null)`, usar `JFrame.DISPOSE_ON_CLOSE`, llamar `setVisible(true)`, cargar datos iniciales y registrar eventos.
- Los `ActionListener` deben vivir en un metodo tipo `eventos()`.
- La actualizacion de listas y areas de texto debe vivir en un metodo tipo `actualizarVista()`.

La UI minima debe tener campos para codigo de tecnico y contrasena, botones para iniciar sesion/refrescar/generar orden, areas para maquinas asignadas y alarmas activas, y un campo o selector para elegir la alarma.

## Flujo funcional requerido

1. Iniciar `CmLogistics` con `Util.initialize(args, "CmLogistic.cfg", extArgs)`.
2. Crear el proxy:
   - Leer `ServerCentral` desde propiedades Ice.
   - Hacer `ServicioComLogisticaPrx.checkedCast(...)`.
   - Usar proxy `twoway`.
   - Si el proxy falla, mostrar un error claro y terminar sin excepcion no controlada.
3. Mostrar una interfaz Swing de inicio de sesion desde `gui`.
4. Pedir:
   - Codigo de operador.
   - Contrasena.
5. Validar entrada localmente:
   - Codigo numerico y mayor que cero.
   - Contrasena no vacia.
6. Llamar `inicioSesion(codigoOperador, password)`.
7. Si el login falla:
   - Mostrar mensaje de credenciales invalidas.
   - Permitir reintentar o salir.
8. Si el login es correcto:
   - Crear una `SesionTecnico` en `tecnicoMantenimiento`.
   - Consultar `asignacionMaquina(codigoOperador)`.
   - Convertir cada registro en objetos de `zonaGeografica`.
   - Mostrar cada maquina asignada en formato legible.
   - Consultar `asignacionMaquinasDesabastecidas(codigoOperador)`.
9. Enviar las cadenas de alarmas a `controlAlarma.ParserAlarmaAsignada`.
10. Mostrar alarmas activas asignadas desde `gui.InterfazLogistica`.
11. Permitir seleccionar una alarma por numero de lista.
12. Generar una `OrdenAtencion` con:
    - Codigo de maquina.
    - Ubicacion.
    - Fecha inicial de alarma.
    - Codigo de alarma.
    - Descripcion de alarma.
    - Recurso esperado segun la tabla normalizada.
13. Mantener menu operativo:
    - Refrescar alarmas.
    - Ver maquinas asignadas.
    - Generar orden para bodega.
    - Cerrar sesion.

## Diseno interno esperado

`gui` debe depender de casos de uso simples, no de detalles de Ice. La ventana debe pedir datos, mostrar listas y exponer botones/campos mediante getters para que el controlador registre eventos.

`tecnicoMantenimiento` debe encapsular el operador autenticado:

- Codigo de operador.
- Estado de sesion.
- Lista de maquinas asignadas.
- Validacion basica de que existe una sesion activa antes de consultar alarmas.

`zonaGeografica` debe convertir las asignaciones del servidor en objetos propios:

- `idMaquina`.
- `ubicacion`.
- `zona` derivada de la ubicacion si aplica.
- Metodo de ordenamiento o agrupacion para mostrar rutas de atencion.

`controlAlarma` debe concentrar la logica de alarmas:

- Parsear `idMaquina#ubicacion#fechaInicial#idAlarma#descripcion`.
- Marcar registros invalidos sin romper el flujo.
- Traducir `idAlarma` a recurso esperado.
- Construir la orden de atencion que luego podra usar bodega.
- Mantener la regla de que logistica no ejecuta el abastecimiento directamente.

## Formatos de datos

`asignacionMaquina` retorna cadenas con el formato actual:

```text
idMaquina-ubicacion
```

`asignacionMaquinasDesabastecidas` retorna cadenas con el formato actual:

```text
idMaquina#ubicacion#fechaInicial#idAlarma#descripcion
```

El parser de `cmLogistics` debe aceptar exactamente cinco partes separadas por `#`. Si un registro llega incompleto o mal formado, debe marcarse como invalido en pantalla y no debe impedir mostrar los demas registros.

## Tabla normalizada de alarmas

Estos IDs se documentan aqui solo para que `controlAlarma` traduzca la alarma en una orden entendible. `cmLogistics` no debe modificar el origen de estos IDs ni corregir otros componentes.

| ID | Recurso o accion | Tipo de atencion |
| --- | --- | --- |
| `1` | Kit de reparacion | Mal funcionamiento / mantenimiento |
| `2` | Monedas de 100 | Reabastecimiento de monedas |
| `4` | Monedas de 200 | Reabastecimiento de monedas |
| `6` | Monedas de 500 | Reabastecimiento de monedas |
| `8` | Agua | Reabastecimiento de ingrediente |
| `9` | Cafe | Reabastecimiento de ingrediente |
| `10` | Azucar | Reabastecimiento de ingrediente |
| `11` | Vaso | Reabastecimiento de ingrediente |

Logistica solo debe reportar el `idAlarma` que recibe del servidor y mostrar el recurso asociado si lo conoce. La resolucion real de alarmas criticas o de inventario queda fuera de `cmLogistics`.

## Comportamiento ante errores

- Si `ServerCentral` no esta disponible, mostrar que el servidor central no responde y terminar limpiamente.
- Si el login falla, no consultar asignaciones.
- Si no hay maquinas asignadas, mostrar una lista vacia explicita.
- Si no hay alarmas activas asignadas, mostrar `sin alarmas asignadas`.
- Si una alarma tiene ID desconocido, permitir generar orden como `tipo desconocido`, pero advertir que bodega podria no ejecutarla.
- Si una llamada Ice falla durante una consulta, mantener la sesion y permitir reintentar.

## Dependencias tecnicas de otros componentes

- `ServidorCentral` debe estar en ejecucion y publicar la identidad `logistica`.
- `ServidorCentral` debe retornar alarmas activas desde `asignacionMaquinasDesabastecidas`.
- `settings.gradle` debe incluir `cmLogistics` antes de compilar este modulo con Gradle.
- La spec no ordena modificar servidor, base de datos, SQL ni maquina de cafe en esta etapa.

## Paso a paso de implementacion futura

1. Activar `include 'cmLogistics'` en `coffeemach/settings.gradle`.
2. Completar `tecnicoMantenimiento` con el modelo de tecnico y sesion autenticada.
3. Completar `zonaGeografica` con el modelo de maquina asignada y agrupacion por ubicacion.
4. Completar `controlAlarma` con el modelo de alarma asignada, parser, traduccion de IDs y orden de atencion.
5. Completar `gui` con una interfaz Swing y un controlador de eventos que usen las clases anteriores.
6. Implementar la conexion a `ServicioComLogisticaPrx` desde `CmLogistics`.
7. Hacer que `CmLogistics` inyecte el proxy de servidor central en el controlador o caso de uso principal.
8. Implementar login con reintento usando `tecnicoMantenimiento`.
9. Implementar consulta y visualizacion de maquinas asignadas usando `zonaGeografica`.
10. Implementar consulta y visualizacion de alarmas activas usando `controlAlarma`.
11. Implementar generacion de orden para bodega.
12. Validar registros mal formados sin detener el proceso.
13. Ejecutar build y pruebas.
14. Probar manualmente contra `ServidorCentral` con operador `1 / 1123`.

## Criterios de aceptacion

- El modulo compila al activarse en Gradle.
- La interfaz Swing permite iniciar sesion contra `ServidorCentral`.
- Un login invalido no muestra informacion de maquinas.
- Un login valido muestra maquinas asignadas.
- Las alarmas activas se muestran con maquina, ubicacion, fecha, ID y descripcion.
- Una alarma seleccionada produce una orden completa de atencion.
- La implementacion usa los paquetes existentes `controlAlarma`, `gui`, `tecnicoMantenimiento` y `zonaGeografica` con responsabilidades separadas.
- El componente no modifica inventario ni cierra alarmas.
- El componente no llama `ServicioAbastecimientoPrx` aunque `CmLogistic.cfg` tenga una propiedad `MaquinaCafe`.
- Las fallas Ice se informan en la interfaz sin terminar con stack trace visible al usuario final.

## Pruebas recomendadas

- Ejecutar desde `coffeemach/`:

```powershell
.\gradlew.bat build
.\gradlew.bat test
```

- Prueba manual minima:
  - Preparar PostgreSQL con los scripts de `scripts/postgres`.
  - Ejecutar `ServidorCentral`.
  - Ejecutar `cmLogistics`.
  - Autenticar operador `1 / 1123`.
  - Verificar que aparezcan maquinas asignadas.
  - Verificar que aparezcan alarmas activas de las maquinas del operador.
  - Generar una orden para bodega y comprobar que contiene `codMaquina` e `idAlarma`.
