import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;

public class Cliente_Contactos extends JFrame {
	

	JPanel panel;
	JTextArea areaTexto;	

	public Cliente_Contactos() {

		panel = new JPanel();

		add(panel);
		panel.setLayout( new BorderLayout());

		areaTexto = new JTextArea();

		panel.add(areaTexto, BorderLayout.CENTER);

		

		

	}

}