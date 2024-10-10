import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Imagen imagen = null;
        TablaPaginas tablaPaginas = null;

        boolean salir = false;
        while (!salir) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Generación de las referencias");
            System.out.println("2. Calcular datos de hits y fallas de página");
            System.out.println("3. Salir");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                System.out.println("Ingrese el tamaño de página (en bytes):");
                int tamanioPagina = scanner.nextInt();
                System.out.println("Seleccione la imagen:");
                System.out.println("1. caso2-parrots_mod.bmp");
                System.out.println("2. caso2-parrots.bmp");
                int imagenSeleccionada = scanner.nextInt();

                String nombreArchivo;
                if (imagenSeleccionada == 1) {
                    nombreArchivo = "caso2-parrots_mod.bmp";
                } else if (imagenSeleccionada == 2) {
                    nombreArchivo = "caso2-parrots.bmp";
                } else {
                    System.out.println("Selección no válida. Debe elegir 1 o 2.");
                    break;
                }
                String rutaBase = System.getProperty("user.dir") + "/Caso2Prueba/Archivos/";
                imagen = new Imagen(rutaBase + nombreArchivo);
                System.out.println("Imagen cargada exitosamente: " + nombreArchivo);
                // Aquí se podrían generar las referencias.
                break;

                case 2:
                    System.out.println("Ingrese el número de marcos de página:");
                    int numMarcos = scanner.nextInt();
                    if (imagen == null) {
                        System.out.println("Debe cargar una imagen primero.");
                        break;
                    }
                    tablaPaginas = new TablaPaginas(1000, numMarcos);
                    // Simulación de referencias y conteo de hits/fallas
                    System.out.println("Hits: " + tablaPaginas.getHits());
                    System.out.println("Fallas de página: " + tablaPaginas.getFallasPagina());
                    break;
                case 3:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }

        scanner.close();
    }
}
