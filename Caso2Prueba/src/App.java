import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Imagen imagen = null;
        TablaPaginas tablaPaginas = null;

        boolean salir = false;
        String archivoReferencias = ""; // Variable para almacenar la ruta del archivo de referencias
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

                    String rutaBase = System.getProperty("user.dir") + "/Caso2Prueba/Archivos/";
                    String nombreArchivo;

                    if (imagenSeleccionada == 1) {
                        nombreArchivo = "caso2-parrots_mod.bmp";
                    } else if (imagenSeleccionada == 2) {
                        nombreArchivo = "caso2-parrots.bmp";
                    } else {
                        System.out.println("Selección no válida. Debe elegir 1 o 2.");
                        break;
                    }

                    // Crear la instancia de Imagen con la ruta completa del archivo
                    imagen = new Imagen(rutaBase + nombreArchivo);
                    archivoReferencias = rutaBase + "referencias.txt";
                    imagen.generarReferencias(archivoReferencias, tamanioPagina);
                    System.out.println("Referencias generadas y guardadas en: " + archivoReferencias);
                    break;

                case 2:
                    if (imagen == null || archivoReferencias.isEmpty()) {
                        System.out.println("Debe cargar una imagen y generar las referencias primero.");
                        break;
                    }

                    System.out.println("Ingrese el número de marcos de página:");
                    int numMarcos = scanner.nextInt();
                    System.out.println("Ingrese el tamaño de página (en bytes) que se usó para generar las referencias:");
                    int tamanioPaginaParaSimulacion = scanner.nextInt();

                    tablaPaginas = new TablaPaginas(1000, numMarcos, tamanioPaginaParaSimulacion);
                    tablaPaginas.cargarReferencias(archivoReferencias); // Usar la misma ruta del archivo generado
                    tablaPaginas.simularPaginacion();
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
