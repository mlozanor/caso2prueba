import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;

public class TablaPaginas {
    private int[] tablaPaginas;
    private int[] marcosPagina;
    private boolean[] bitR;
    private Queue<Integer> colaMarcos;
    private int hits;
    private int fallasPagina;

    public TablaPaginas(int numPaginas, int numMarcos) {
        tablaPaginas = new int[numPaginas];
        marcosPagina = new int[numMarcos];
        bitR = new boolean[numPaginas];
        colaMarcos = new LinkedList<>();
        hits = 0;
        fallasPagina = 0;

        // Inicializar tabla de páginas y marcos
        Arrays.fill(tablaPaginas, -1);
        Arrays.fill(marcosPagina, -1);
        Arrays.fill(bitR, false);

        for (int i = 0; i < numMarcos; i++) {
            colaMarcos.add(i);
        }
    }

    public synchronized void procesarReferencia(int pagina) {
        if (estaEnRAM(pagina)) {
            hits++;
            bitR[pagina] = true;
        } else {
            fallasPagina++;
            reemplazarPagina(pagina);
        }
    }

    private boolean estaEnRAM(int pagina) {
        for (int marco : marcosPagina) {
            if (marco == pagina) {
                return true;
            }
        }
        return false;
    }

    private void reemplazarPagina(int nuevaPagina) {
        int marcoReemplazo = colaMarcos.poll();
        marcosPagina[marcoReemplazo] = nuevaPagina;
        bitR[nuevaPagina] = true;
        colaMarcos.add(marcoReemplazo);
    }

    public int getHits() {
        return hits;
    }

    public int getFallasPagina() {
        return fallasPagina;
    }

    public synchronized void actualizarBits() {
        for (int i = 0; i < bitR.length; i++) {
            bitR[i] = false; // Simula el envejecimiento de los bits R
        }
    }
}
