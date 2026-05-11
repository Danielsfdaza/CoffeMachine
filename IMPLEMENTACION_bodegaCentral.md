# Implementacion de bodegaCentral

## Resumen

Se completo el componente `coffeemach/bodegaCentral` siguiendo `SPEC_bodegaCentral.md`. La implementacion convierte el modulo en una aplicacion Swing para registrar ordenes de atencion, validar inventario interno y ejecutar el abastecimiento remoto contra la maquina de cafe cuando el proxy `MaquinaCafe` esta disponible.

El componente no consulta alarmas en `ServidorCentral`, no modifica base de datos y no agrega contratos Ice nuevos. Consume el contrato existente `ServicioAbastecimientoPrx.abastecer(codMaquina, idAlarma)`.

## Cambios principales

- `coffeemach/settings.gradle` ahora incluye `bodegaCentral`.
- `BodegaCentral.java` dejo de imprimir `Sin Implementacion` y ahora inicializa Ice, inventario, servicio de bodega y controlador Swing.
- Se agrego `BodegaCentral.cfg` con el proxy:

```text
MaquinaCafe = abastecer:default -h localhost -p 12346
```

- Se completo la estructura de paquetes prevista por el proyecto:
  - `bodega`
  - `mantenimientoExistencias`
  - `guiInventario`

## Flujo implementado

1. `BodegaCentral.main` inicializa Ice con `BodegaCentral.cfg`.
2. Intenta crear `ServicioAbastecimientoPrx` desde la propiedad `MaquinaCafe`.
3. Si la maquina no esta disponible, la aplicacion sigue funcionando para separar existencias.
4. Se instancia `InventarioBodega`.
5. Se instancia `BodegaCentralService` con inventario y proxy opcional a maquina.
6. Se abre la ventana `guiInventario.Interfaz`.
7. El usuario ingresa codigo de maquina e ID de alarma.
8. El controlador construye una `OrdenBodega`.
9. `BodegaCentralService` valida que la alarma sea soportada.
10. Se valida inventario suficiente.
11. Si hay proxy a maquina, se llama `abastecer(codMaquina, idAlarma)`.
12. Si la operacion es exitosa, se descuenta inventario y se muestra el resultado.

## Paquetes y clases

`bodega`

- `Bodega`: interfaz base del componente, extendida con operaciones utiles para atender ordenes.
- `BodegaCentralService`: implementacion principal de la logica de atencion.
- `OrdenBodega`: datos de entrada de una orden: maquina, alarma, ubicacion y descripcion.
- `ResultadoAtencion`: resultado mostrado a la interfaz, con estado, mensaje, recurso y cantidad.

`mantenimientoExistencias`

- `Inventario`: interfaz base, extendida con consulta, descuento y traduccion de alarmas.
- `InventarioBodega`: inventario en memoria con recursos iniciales.
- `Existencia`: recurso individual con cantidad y operaciones de abastecer/descontar.
- `TipoRecurso`: constantes de recursos soportados.

`guiInventario`

- `Interfaz`: ventana Swing que extiende `JFrame`.
- `ControladorInventario`: controlador separado con `run()`, `eventos()` y `actualizarVista()`.

## Interfaz grafica

La UI sigue el estilo existente del proyecto:

- Clase visual que extiende `JFrame`.
- Layout `null` con `setBounds`.
- Controles Swing privados.
- Getters para que el controlador registre eventos.
- Logica de bodega e inventario fuera de la vista.

La ventana incluye:

- Campo de codigo de maquina.
- Campo de ID de alarma.
- Campos opcionales de ubicacion y descripcion.
- Boton para ejecutar atencion.
- Area de inventario.
- Area de resultado.
- Botones para reabastecer inventario local de monedas, ingredientes y suministros.

## Inventario implementado

`InventarioBodega` inicia con:

| Recurso | Cantidad inicial |
| --- | --- |
| Monedas de 100 | 200 |
| Monedas de 200 | 200 |
| Monedas de 500 | 200 |
| Agua | 20 |
| Cafe | 20 |
| Azucar | 20 |
| Vaso | 100 |
| Kit de reparacion | 10 |

Tambien permite abastecer inventario local:

- Monedas: suma 100 unidades a cada denominacion.
- Ingredientes: suma agua, cafe, azucar y vasos.
- Suministros: suma kits de reparacion.

## Traduccion de alarmas

`InventarioBodega.recursoParaAlarma` traduce los IDs soportados:

| ID | Recurso |
| --- | --- |
| `1` | Kit de reparacion |
| `2` | Monedas de 100 |
| `4` | Monedas de 200 |
| `6` | Monedas de 500 |
| `8` | Agua |
| `9` | Cafe |
| `10` | Azucar |
| `11` | Vaso |

Los IDs no soportados rechazan la orden sin descontar inventario ni llamar a la maquina.

## Validaciones y errores

- Codigo de maquina debe ser numerico y mayor que cero.
- ID de alarma debe ser numerico y mayor que cero.
- Alarmas no soportadas se rechazan.
- Si no hay existencias suficientes, no se llama a la maquina.
- Si la maquina no responde, la orden queda pendiente y no se descuenta inventario.
- Si no hay proxy a maquina, se permite separar existencias localmente y se informa en la interfaz.
- Al cerrar la ventana se ejecuta `communicator.shutdown()` para no dejar el proceso colgado.

## Ejecucion

Desde `coffeemach/`, usar JDK 11 para Gradle:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-11.0.30.7-hotspot'
.\gradlew.bat build
```

Despues de compilar:

```powershell
java -jar bodegaCentral\build\libs\bodegaCentral.jar
```

Para ejecutar abastecimiento remoto real, `coffeeMach` debe estar corriendo y publicar la identidad `abastecer` en el endpoint configurado por `BodegaCentral.cfg`.

## Validacion realizada

Se ejecuto:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-11.0.30.7-hotspot'
.\gradlew.bat build
.\gradlew.bat test
```

Ambos comandos pasaron. El proyecto no tiene tests fuente, por eso Gradle reporta `NO-SOURCE` en tareas de test.

## Limitaciones actuales

- El inventario es en memoria y se reinicia al cerrar la aplicacion.
- No se registra historico de ordenes.
- No se consulta directamente `ServidorCentral`.
- No se corrigen mapeos o cierre de alarmas dentro de `coffeeMach` o `ServidorCentral`.
- El flujo depende de que el usuario copie desde `cmLogistics` los datos de maquina e ID de alarma.

