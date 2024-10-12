import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TablaPaginas {
    private int[] marcosPagina;
    private boolean[] bitR;
    private boolean[] bitM;
    private Queue<Integer> colaMarcos;
    private int hits;
    private int fallasPagina;
    private int tamanioPagina;
    private long tiempoHits;
    private long tiempoFallas;

    // Constructor
    public TablaPaginas(int numPaginas, int numMarcos, int tamanioPagina) {
        this.marcosPagina = new int[numMarcos];
        this.bitR = new boolean[numPaginas];
        this.bitM = new boolean[numPaginas];
        this.colaMarcos = new LinkedList<>();
        this.hits = 0;
        this.fallasPagina = 0;
        this.tamanioPagina = tamanioPagina;

        Arrays.fill(marcosPagina, -1);

        for (int i = 0; i < numMarcos; i++) {
            colaMarcos.add(i);
        }
    }

    // Procesar cada referencia y determinar si es un hit o falla
    public synchronized void procesarReferencia(int pagina, boolean escritura) {
        if (estaEnRAM(pagina)) {
            hits++;
            tiempoHits += 25; // Tiempo de acceso en ns
            bitR[pagina] = true;
            if (escritura) {
                bitM[pagina] = true;
            }
        } else {
            fallasPagina++;
            tiempoFallas += 10_000_000; // Tiempo de acceso en ns (10 ms = 10,000,000 ns)
            reemplazarPagina(pagina);
        }
    }

    // Verificar si una página está en un marco de la RAM
    private boolean estaEnRAM(int pagina) {
        for (int marco : marcosPagina) {
            if (marco == pagina) {
                return true;
            }
        }
        return false;
    }

    // Reemplazar una página en un marco usando el algoritmo NRU
    private void reemplazarPagina(int nuevaPagina) {
        int marcoReemplazo = colaMarcos.poll();
        int paginaReemplazo = marcosPagina[marcoReemplazo];

        // Resetear los bits de la página reemplazada si no es -1 (significa que hay una página previa)
        if (paginaReemplazo != -1) {
            bitR[paginaReemplazo] = false;
            bitM[paginaReemplazo] = false;
        }

        // Actualizar la tabla con la nueva página y marcarla como referenciada
        marcosPagina[marcoReemplazo] = nuevaPagina;
        bitR[nuevaPagina] = true;
        colaMarcos.add(marcoReemplazo);
    }

    // Método para cargar y procesar las referencias desde un archivo
    public void cargarReferencias(String archivoRef) {
        try (Scanner scanner = new Scanner(new File(archivoRef))) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
    
                // Ignorar las líneas de metadatos (TP, NF, NC, NP)
                if (linea.startsWith("TP=") || linea.startsWith("NF=") || linea.startsWith("NC=") || linea.startsWith("NP=")) {
                    continue; // Saltar a la siguiente línea si es una línea de metadatos
                }
    
                // Procesar las líneas de referencias (esperando formato "pagina,desplazamiento,R")
                String[] ref = linea.split(",");
                if (ref.length < 3) {
                    System.out.println("Formato incorrecto en la línea: " + linea);
                    continue; // Saltar líneas con formato incorrecto para evitar errores
                }
    
                int pagina = Integer.parseInt(ref[0].trim());
                boolean escritura = "W".equals(ref[2].trim());
                procesarReferencia(pagina, escritura);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir una referencia: " + e.getMessage());
        }
    }
    

    // Método para simular la paginación y mostrar los resultados
    public void simularPaginacion() {
        long tiempoTotal = tiempoHits + tiempoFallas;

        System.out.println("Simulación completa.");
        System.out.println("Tamaño de página: " + tamanioPagina + " bytes");
        System.out.println("Total de Hits: " + hits);
        System.out.println("Total de Fallas de Página: " + fallasPagina);
        System.out.println("Tiempo total de Hits: " + tiempoHits + " ns");
        System.out.println("Tiempo total de Fallas: " + tiempoFallas + " ns");
        System.out.println("Tiempo total de acceso: " + tiempoTotal + " ns (" + tiempoTotal / 1_000_000 + " ms)");
    }
}
