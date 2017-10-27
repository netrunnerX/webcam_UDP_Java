package webcam;

import java.awt.image.BufferedImage;
import java.io.*;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.imageio.ImageIO;

public class ClienteWebcam {

    private static InetAddress maquina;
    private static int puerto = 6699;
	public static void main(String[] args) {
		Webcam cam = null;
		DatagramSocket dataSocket = null;

		try {
                    dataSocket = new DatagramSocket();
                    maquina = InetAddress.getByName("localhost");
                    enviarCamUDP(dataSocket, cam);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
		finally {
			if (cam != null) {
				cam.close();
			}
			Webcam.shutdown();
                        if (dataSocket != null) {
                            dataSocket.close();
                        }
			System.out.println("Terminado");
		}
                
	}
        
        public static void enviarCamUDP(DatagramSocket dataSocket, Webcam cam) 
        throws IOException {
            cam = Webcam.getDefault();
            cam.setViewSize(WebcamResolution.VGA.getSize());
            cam.open();
            
            System.out.println("Enviando imagenes webcam al servidor");
            while (true) {
                BufferedImage frame = cam.getImage(); //Obtiene imagen de la webcam
                
                //Convierte la imagen a JPEG y la pasa a un array de bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(frame, "jpeg", baos);
                byte[] bytes = baos.toByteArray();
                
                //Crea un paquete UDP y lo envia al receptor
                DatagramPacket paquete = new DatagramPacket(bytes, bytes.length,
                maquina, puerto);
                dataSocket.send(paquete);
            }
        }

}
