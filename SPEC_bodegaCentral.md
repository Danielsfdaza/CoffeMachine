# SPEC bodegaCentral

## Manifiesto del componente

`bodegaCentral` debe completarse desde las carpetas y clases incompletas que el proyecto ya dejo preparadas. Su responsabilidad inmediata es recibir una orden de atencion, determinar que recurso se necesita, validar existencias internas y preparar la operacion de abastecimiento o mantenimiento.

Este componente no debe consultar directamente alarmas del servidor central. La implementacion debe concentrarse por ahora en `bodega`, `mantenimientoExistencias`, `guiInventario`, `BodegaCentral.java` y la relacion minima con la maquina de cafe cuando sea necesario ejecutar la atencion.

## Estado actual

- `coffeemach/bodegaCentral/src/main/java/BodegaCentral.java` solo imprime `Sin Implementacion`.
- `coffeemach/bodegaCentral/src/main/java/bodega/Bodega.java` define operaciones sin implementacion.
- `coffeemach/bodegaCentral/src/main/java/mantenimientoExistencias/Inventario.java` define operaciones sin implementacion.
- `coffeemach/bodegaCentral/src/main/java/guiInventario/Interfaz.java` solo declara referencias a `Inventario` y `Bodega`.
- Existen paquetes intencionales para completar la implementacion:
  - `bodega`
  - `mantenimientoExistencias`
  - `guiInventario`
- No existe `BodegaCentral.cfg`.
- `settings.gradle` todavia debe activar el modulo con `include 'bodegaCentral'` cuando se empiece la implementacion.

## Alcance temporal

Por ahora esta spec solo debe guiar modificaciones dentro de `coffeemach/bodegaCentral` y lo directamente necesario para que esas modificaciones funcionen. El trabajo se concentra en:

- `BodegaCentral.java` como punto de entrada.
- `bodega` como paquete de operaciones de bodega.
- `mantenimientoExistencias` como paquete de inventario.
- `guiInventario` como paquete de interfaz grafica Swing e interaccion con el usuario.
- `BodegaCentral.cfg` si se necesita configurar el proxy de la maquina.

Quedan fuera de esta spec, salvo como dependencias existentes:

- Cambiar la base de datos.
- Cambiar `ServidorCentral`.
- Cambiar `cmLogistics`.
- Cambiar `coffeeMach`, excepto asumir que ya existe el servicio remoto `abastecer`.
- Agregar nuevos contratos Ice.

## Paquetes existentes y responsabilidad propuesta

Estas carpetas deben tenerse en cuenta como la estructura base del componente. Aunque algunas clases sean interfaces incompletas o no tengan comportamiento, deben guiar la implementacion en lugar de crear todo en `BodegaCentral.java`.

| Paquete | Papel en `bodegaCentral` | Clases sugeridas |
| --- | --- | --- |
| `bodega` | Coordinar la atencion de una orden: validar el tipo de alarma, pedir existencias al inventario, separar recursos y ejecutar la accion de bodega. | `BodegaCentralService`, `OrdenBodega`, `ResultadoAtencion`, implementacion de `Bodega` |
| `mantenimientoExistencias` | Administrar el inventario interno de bodega: monedas, ingredientes, suministros y kits de reparacion. Debe decidir si hay existencias suficientes y descontarlas. | `InventarioBodega`, `Existencia`, `TipoRecurso`, implementacion de `Inventario` |
| `guiInventario` | Presentar la interfaz grafica de bodega siguiendo el estilo Swing ya existente en `coffeeMach/interfazUsuario` y `ServidorCentral/interfaz`. | Completar `Interfaz`, agregar `ControladorInventario` si hace falta |

`BodegaCentral.java` debe quedar como punto de entrada: inicializa Ice, crea el proxy `ServicioAbastecimientoPrx`, instancia inventario, bodega y UI, y arranca el flujo. La logica de inventario, validacion de alarmas y atencion de orden no debe quedarse acumulada en `main`.

## Patron de interfaz requerido

La interfaz de `bodegaCentral` debe seguir la misma estructura de las interfaces ya hechas:

- `coffeeMach/src/main/java/interfazUsuario/Interfaz.java`
- `ServidorCentral/src/main/java/interfaz/InterfazRecetas.java`
- `ServidorCentral/src/main/java/interfaz/ControladorRecetas.java`

Por tanto, `guiInventario` debe implementarse asi:

- Completar `guiInventario.Interfaz` como clase visual Swing; debe extender `JFrame`.
- Construir los componentes en el constructor usando el estilo actual del proyecto: `JPanel`, `JLabel`, `JButton`, `JTextArea`, `JTextField`, `JScrollPane`, `JComboBox` si aplica, `setBounds(...)` y layout `null`.
- Mantener los controles como atributos privados.
- Exponer los controles necesarios mediante getters, igual que `Interfaz` e `InterfazRecetas`.
- No poner logica de inventario, validacion de alarmas ni llamadas Ice directamente dentro de la clase visual.
- Crear un controlador separado, por ejemplo `guiInventario.ControladorInventario`, que implemente `Runnable`.
- El controlador debe crear la ventana en `run()`, hacer `setLocationRelativeTo(null)`, usar `JFrame.DISPOSE_ON_CLOSE`, llamar `setVisible(true)`, cargar datos iniciales y registrar eventos.
- Los `ActionListener` deben vivir en un metodo tipo `eventos()`.
- La actualizacion de inventario, resultado de atencion y mensajes debe vivir en un metodo tipo `actualizarVista()`.

La UI minima debe tener campos para codigo de maquina e ID de alarma, botones para registrar/ejecutar atencion, areas para inventario y resultado, y controles para consultar existencias.

## Flujo funcional requerido

1. Iniciar `BodegaCentral`.
2. Si se va a ejecutar abastecimiento remoto, cargar configuracion Ice propia:
   - Archivo: `coffeemach/bodegaCentral/src/main/resources/BodegaCentral.cfg`
   - Propiedad minima: `MaquinaCafe = abastecer:default -h localhost -p 12346`
3. Si se usa Ice, crear proxy a la maquina:
   - Leer `MaquinaCafe` desde propiedades Ice.
   - Hacer `ServicioAbastecimientoPrx.checkedCast(...)`.
   - Usar proxy `twoway`.
4. Inicializar `mantenimientoExistencias.Inventario` con las existencias minimas.
5. Inicializar una implementacion de `bodega.Bodega` con:
   - Inventario.
   - Proxy de la maquina si se ejecutara abastecimiento remoto.
   - Tabla normalizada de alarmas.
6. Mostrar una interfaz Swing de bodega desde `guiInventario`.
7. Pedir los datos de la orden:
   - Codigo de maquina.
   - ID de alarma.
   - Opcionalmente ubicacion y descripcion para mostrar confirmacion.
8. Validar entrada:
   - Codigo de maquina numerico y mayor que cero.
   - ID de alarma numerico y soportado.
9. Determinar recurso requerido con la tabla normalizada de alarmas desde `bodega`.
10. Consultar inventario interno desde `mantenimientoExistencias`.
11. Si no hay existencias suficientes:
   - No llamar a la maquina.
   - Mostrar motivo de rechazo.
   - Mantener la orden pendiente.
12. Si hay existencias:
   - Separar existencias para la orden.
   - Descontar el inventario interno.
   - Ejecutar la accion de bodega; si el proxy esta disponible, llamar `abastecer(codMaquina, idAlarma)` en la maquina.
13. Si la accion fue exitosa:
    - Registrar en la interfaz que la atencion fue ejecutada.
    - Mostrar recurso usado y existencia restante.
14. Si la llamada remota falla:
    - Informar error.
    - Dejar claro si el inventario debe revertirse o si la orden queda en revision.

## Diseno interno esperado

`guiInventario` debe depender de operaciones de bodega, no de detalles de Ice. La ventana debe pedir los datos de la orden, mostrar inventario, mostrar errores y presentar el resultado de la atencion.

`mantenimientoExistencias` debe encapsular el estado de inventario:

- Monedas disponibles por denominacion.
- Ingredientes disponibles por nombre.
- Kits de reparacion disponibles.
- Validacion de cantidad suficiente.
- Separacion o descuento de recursos.
- Consulta de saldo restante.

`bodega` debe concentrar la logica operativa:

- Construir una `OrdenBodega` desde `codMaquina` e `idAlarma`.
- Traducir `idAlarma` al recurso requerido.
- Pedir al inventario que reserve o descuente el recurso.
- Llamar a `ServicioAbastecimientoPrx.abastecer` solo si se esta ejecutando el abastecimiento remoto.
- Construir un `ResultadoAtencion` con exito, rechazo o error remoto.

La implementacion concreta de `Bodega` no debe leer directamente desde consola. La UI entrega datos; bodega decide si se puede atender; inventario administra existencias.

## Inventario minimo

El inventario interno minimo debe cubrir:

| Recurso | Uso | Cantidad sugerida por atencion |
| --- | --- | --- |
| Monedas de 100 | Alarma `2` | Lote de reposicion |
| Monedas de 200 | Alarma `4` | Lote de reposicion |
| Monedas de 500 | Alarma `6` | Lote de reposicion |
| Agua | Alarma `8` | Llenado de ingrediente |
| Cafe | Alarma `9` | Llenado de ingrediente |
| Azucar | Alarma `10` | Llenado de ingrediente |
| Vaso | Alarma `11` | Llenado de ingrediente |
| Kit de reparacion | Alarma `1` | Un kit |

Las cantidades exactas pueden ser constantes internas de bodega en la primera version. No se requiere persistencia en base de datos para esta fase, salvo que el equipo decida extender el alcance.

## Tabla normalizada de alarmas

Estos IDs se documentan aqui para que `bodega` traduzca una orden en un recurso interno. La spec no ordena cambiar otros componentes para normalizarlos en esta etapa.

| ID | Recurso o accion | Resultado esperado |
| --- | --- | --- |
| `1` | Kit de reparacion | Resolver mal funcionamiento / mantenimiento |
| `2` | Monedas de 100 | Reponer deposito de monedas de 100 |
| `4` | Monedas de 200 | Reponer deposito de monedas de 200 |
| `6` | Monedas de 500 | Reponer deposito de monedas de 500 |
| `8` | Agua | Recargar ingrediente Agua |
| `9` | Cafe | Recargar ingrediente Cafe |
| `10` | Azucar | Recargar ingrediente Azucar |
| `11` | Vaso | Recargar ingrediente Vaso |

Bodega debe enviar el `idAlarma` recibido y no inventar nuevos codigos. La limpieza de alarmas criticas locales en la maquina queda fuera del alcance de esta spec.

## Comportamiento ante errores

- Si `MaquinaCafe` no esta disponible, mostrar que la maquina no responde y no perder la orden.
- Si el proxy Ice no puede crearse, terminar limpiamente con mensaje claro.
- Si el ID de alarma no esta soportado, no descontar inventario ni llamar a la maquina.
- Si no hay existencias suficientes, rechazar la atencion y mostrar recurso faltante.
- Si la llamada remota falla despues de descontar inventario, registrar que la orden queda en revision manual.
- No debe lanzarse `UnsupportedOperationException` desde el flujo principal.

## Dependencias tecnicas de otros componentes

- `coffeeMach` debe estar disponible si se ejecuta el abastecimiento remoto.
- `coffeeMach` debe publicar la identidad `abastecer`, porque `bodegaCentral` solo consumira ese servicio existente.
- `settings.gradle` debe incluir `bodegaCentral` antes de compilar este modulo con Gradle.
- La spec no ordena modificar servidor, base de datos, SQL, `cmLogistics` ni `coffeeMach` en esta etapa.

## Paso a paso de implementacion futura

1. Activar `include 'bodegaCentral'` en `coffeemach/settings.gradle`.
2. Crear `coffeemach/bodegaCentral/src/main/resources/BodegaCentral.cfg`.
3. Implementar arranque Ice en `BodegaCentral.java`.
4. Crear proxy `ServicioAbastecimientoPrx` usando la propiedad `MaquinaCafe` solo para ejecutar abastecimiento remoto.
5. Completar `mantenimientoExistencias` con la implementacion concreta del inventario de bodega.
6. Completar `bodega` con la implementacion concreta de `Bodega`, `OrdenBodega` y `ResultadoAtencion`.
7. Completar `guiInventario.Interfaz` como `JFrame` y agregar un controlador de eventos que use `Bodega` e `Inventario`.
8. Implementar controles de interfaz para:
   - Ver inventario.
   - Registrar orden de logistica.
   - Ejecutar abastecimiento.
   - Salir.
9. Implementar lectura y validacion de campos para `codMaquina` e `idAlarma` en el controlador de `guiInventario`.
10. Implementar validacion de recurso segun tabla de alarmas en `bodega`.
11. Implementar descuento de inventario en `mantenimientoExistencias`.
12. Invocar `abastecer(codMaquina, idAlarma)` desde la implementacion de `Bodega` cuando la atencion requiera comunicarse con la maquina.
13. Registrar resultado de la operacion en la UI.
14. Ejecutar build y pruebas.
15. Probar manualmente con una orden generada por `cmLogistics`.

## Criterios de aceptacion

- El modulo compila al activarse en Gradle.
- La aplicacion inicia con `BodegaCentral.cfg`.
- La interfaz Swing permite registrar una orden con `codMaquina` e `idAlarma`.
- Un ID de alarma soportado se traduce al recurso correcto.
- Una atencion sin inventario suficiente no llama a la maquina.
- Una atencion con inventario suficiente descuenta existencias y, si corresponde, llama a `ServicioAbastecimientoPrx.abastecer`.
- La interfaz informa resultado, recurso usado y saldo de inventario.
- La implementacion usa los paquetes existentes `bodega`, `mantenimientoExistencias` y `guiInventario` con responsabilidades separadas.
- El componente no consulta directamente alarmas del servidor central en el flujo recomendado.

## Pruebas recomendadas

- Ejecutar desde `coffeemach/`:

```powershell
.\gradlew.bat build
.\gradlew.bat test
```

- Prueba manual minima:
  - Preparar PostgreSQL con los scripts de `scripts/postgres`.
  - Ejecutar `ServidorCentral`.
  - Ejecutar `coffeeMach`.
  - Obtener desde `cmLogistics` una orden de alarma.
  - Ejecutar `bodegaCentral`.
  - Ingresar `codMaquina` e `idAlarma`.
  - Confirmar que bodega descuenta inventario.
  - Confirmar que `coffeeMach` recibe `abastecer`.
  - Confirmar que el servidor deja la alarma atendida con `fecha_final`.
