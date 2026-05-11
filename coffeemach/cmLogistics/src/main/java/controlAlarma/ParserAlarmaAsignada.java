package controlAlarma;

import java.util.ArrayList;
import java.util.List;

public class ParserAlarmaAsignada {

    public List<AlarmaAsignada> parsear(List<String> registros) {
        List<AlarmaAsignada> alarmas = new ArrayList<>();
        if (registros == null) {
            return alarmas;
        }

        for (String registro : registros) {
            alarmas.add(parsear(registro));
        }
        return alarmas;
    }

    public AlarmaAsignada parsear(String registro) {
        if (registro == null || registro.trim().isEmpty()) {
            return AlarmaAsignada.invalida("Registro vacio");
        }

        String[] partes = registro.split("#", -1);
        if (partes.length != 5) {
            return AlarmaAsignada.invalida("Formato invalido: " + registro);
        }

        try {
            int idMaquina = Integer.parseInt(partes[0].trim());
            String ubicacion = partes[1].trim();
            String fechaInicial = partes[2].trim();
            int idAlarma = Integer.parseInt(partes[3].trim());
            String descripcion = partes[4].trim();

            if (idMaquina <= 0 || idAlarma <= 0 || ubicacion.isEmpty()) {
                return AlarmaAsignada.invalida("Datos incompletos: " + registro);
            }

            return new AlarmaAsignada(idMaquina, ubicacion, fechaInicial,
                    idAlarma, descripcion);
        } catch (NumberFormatException e) {
            return AlarmaAsignada.invalida("Codigo numerico invalido: " + registro);
        }
    }
}
