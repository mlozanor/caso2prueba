import java.io.*;

public class Imagen {
    private byte[] header = new byte[54];
    private byte[][][] imagen;
    private int alto, ancho, padding;
    private String nombre;

    // Constructor para cargar una imagen desde un archivo BMP
    public Imagen(String input) {
        nombre = input;
        try (FileInputStream fis = new FileInputStream(nombre)) {
            fis.read(header);
            ancho = ((header[21] & 0xFF) << 24) | ((header[20] & 0xFF) << 16) | 
                    ((header[19] & 0xFF) << 8) | (header[18] & 0xFF);
            alto = ((header[25] & 0xFF) << 24) | ((header[24] & 0xFF) << 16) | 
                   ((header[23] & 0xFF) << 8) | (header[22] & 0xFF);

            System.out.println("Ancho: " + ancho + " px, Alto: " + alto + " px");
            imagen = new byte[alto][ancho][3];

            int rowSizeSinPadding = ancho * 3;
            padding = (4 - (rowSizeSinPadding % 4)) % 4;

            byte[] pixel = new byte[3];
            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    fis.read(pixel);
                    imagen[i][j][0] = pixel[0]; // B
                    imagen[i][j][1] = pixel[1]; // G
                    imagen[i][j][2] = pixel[2]; // R
                }
                fis.skip(padding); // Saltar el padding si lo hay
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para recuperar la longitud del mensaje escondido
    public int leerLongitud() {
        int longitud = 0;
        for (int i = 0; i < 16; i++) {
            int col = (i % (ancho * 3)) / 3;
            longitud |= (imagen[0][col][i % 3] & 1) << i;
        }
        return longitud;
    }

    // Método para recuperar un mensaje escondido en la imagen
    public void recuperar(char[] mensaje, int longitud) {
        int bytesFila = ancho * 3;
        for (int posCaracter = 0; posCaracter < longitud; posCaracter++) {
            mensaje[posCaracter] = 0;
            for (int i = 0; i < 8; i++) {
                int numBytes = 16 + (posCaracter * 8) + i;
                int fila = numBytes / bytesFila;
                int col = (numBytes % bytesFila) / 3;
                mensaje[posCaracter] |= (imagen[fila][col][numBytes % 3] & 1) << i;
            }
        }
    }
}
