# Implementacion de cmLogistics

## Resumen

Se completo el componente `coffeemach/cmLogistics` siguiendo `SPEC_cmLogistics.md`. La implementacion convierte el modulo en una aplicacion Swing para tecnicos de logistica que se conecta a `ServidorCentral`, autentica al operador, consulta maquinas asignadas, lista alarmas activas y genera una orden de atencion para bodega.

El componente no modifica inventario, no cierra alarmas y no llama a `ServicioAbastecimientoPrx`, aunque `CmLogistic.cfg` tenga configurada la propiedad `MaquinaCafe`.

## Cambios principales

- `coffeemach/settings.gradle` ahora incluye `cmLogistics`.
- `CmLogistics.java` dejo de ser un `main` vacio y ahora inicializa Ice con `CmLogistic.cfg`.
- Se crea el proxy `ServicioComLogisticaPrx` desde la propiedad `ServerCentral`.
- Si `ServerCentral` no esta disponible, el componente termina con mensaje claro.
- Se agrego una interfaz Swing en `gui.InterfazLogistica`.
- Se agrego un controlador separado en `gui.ControladorLogistica`, siguiendo el patron de `ControladorRecetas`.
- Se completaron los paquetes vacios definidos por el proyecto:
  - `controlAlarma`
  - `gui`
  - `tecnicoMantenimiento`
  - `zonaGeografica`

## Flujo implementado

1. `CmLogistics.main` inicializa Ice.
2. Lee `ServerCentral` desde `CmLogistic.cfg`.
3. Crea un proxy `ServicioComLogisticaPrx` en modo `twoway`.
4. Instancia `ControladorLogistica`.
5. El controlador abre `InterfazLogistica`.
6. El tecnico ingresa codigo y password.
7. El controlador llama `inicioSesion(codigo, password)`.
8. Si el login es valido, consulta:
   - `asignacionMaquina(codigo)`
   - `asignacionMaquinasDesabastecidas(codigo)`
9. Las asignaciones se convierten a objetos `MaquinaAsignada`.
10. Las alarmas se parsean como `AlarmaAsignada`.
11. El tecnico selecciona una alarma por numero.
12. Se genera una `OrdenAtencion` con maquina, alarma, ubicacion, fecha, descripcion y recurso esperado.

## Paquetes y clases

`controlAlarma`

- `ControlAlarma`: coordina consulta de alarmas y generacion de orden.
- `ParserAlarmaAsignada`: interpreta registros del servidor con formato `idMaquina#ubicacion#fechaInicial#idAlarma#descripcion`.
- `AlarmaAsignada`: representa una alarma valida o un registro invalido.
- `OrdenAtencion`: contiene los datos que se entregan a bodega.

`gui`

- `InterfazLogistica`: ventana Swing que extiende `JFrame`.
- `ControladorLogistica`: registra eventos, llama al servidor y actualiza la vista.

`tecnicoMantenimiento`

- `TecnicoMantenimiento`: modelo basico de tecnico autenticado.
- `SesionTecnico`: estado de sesion, tecnico activo y maquinas asignadas.

`zonaGeografica`

- `MaquinaAsignada`: representa una maquina recibida desde el servidor.
- `ZonaGeografica`: convierte registros `idMaquina-ubicacion` en objetos ordenables.

## Interfaz grafica

La UI sigue el estilo existente del proyecto:

- Clase visual que extiende `JFrame`.
- Layout `null` con `setBounds`.
- Controles Swing privados.
- Getters para que el controlador registre eventos.
- Logica de negocio fuera de la vista.

La ventana incluye:

- Campo de codigo de tecnico.
- Campo de password.
- Botones de iniciar sesion, refrescar, generar orden y cerrar sesion.
- Area de maquinas asignadas.
- Area de alarmas activas.
- Campo para seleccionar el numero de alarma.
- Area de orden generada.
- Area de mensajes.

## Validaciones y errores

- Codigo de tecnico debe ser numerico y mayor que cero.
- Password no puede estar vacio.
- No se consulta informacion sin sesion activa.
- Un login invalido no muestra maquinas ni alarmas.
- Si una alarma llega mal formada, se muestra como registro invalido.
- Si el servidor falla durante login o consulta, el error se muestra en la interfaz.
- Si se cierra la ventana, se ejecuta `communicator.shutdown()` para no dejar el proceso colgado.

## Traduccion de alarmas

`ControlAlarma.recursoParaAlarma` traduce los IDs conocidos:

| ID | Recurso esperado |
| --- | --- |
| `1` | Kit de reparacion |
| `2` | Monedas de 100 |
| `4` | Monedas de 200 |
| `6` | Monedas de 500 |
| `8` | Agua |
| `9` | Cafe |
| `10` | Azucar |
| `11` | Vaso |

Los IDs no conocidos se muestran como `Tipo desconocido`.

## Ejecucion

Desde `coffeemach/`, usar JDK 11 para Gradle:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-11.0.30.7-hotspot'
.\gradlew.bat build
```

Despues de compilar:

```powershell
java -jar cmLogistics\build\libs\cmLogistics.jar
```

Para uso real, `ServidorCentral` debe estar corriendo y publicar la identidad `logistica` en el endpoint configurado por `CmLogistic.cfg`.

## Validacion realizada

Se ejecuto:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-11.0.30.7-hotspot'
.\gradlew.bat build
.\gradlew.bat test
```

Ambos comandos pasaron. El proyecto no tiene tests fuente, por eso Gradle reporta `NO-SOURCE` en tareas de test.

## Limitaciones actuales

- La orden se muestra en pantalla para ser usada por bodega; no se persiste ni se envia automaticamente.
- No se modifica `ServidorCentral`, `coffeeMach`, base de datos ni Slice.
- El componente depende de que el servidor retorne los formatos actuales:
  - `idMaquina-ubicacion`
  - `idMaquina#ubicacion#fechaInicial#idAlarma#descripcion`

