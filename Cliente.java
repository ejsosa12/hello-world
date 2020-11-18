import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;	
import java.util.*;
import java.util.regex.*;

public class Cliente extends JFrame implements Runnable{

	HashMap<String, String> servidores = new HashMap<String, String>();

	JPanel panel;
	Color color = new Color(146, 135, 135);
	JTextField usuarioServidorLoggin;
	JTextField contrasenaLoggin;
	JButton boton;
	JLabel etiqueta_advertencias;

	public Cliente() {

		servidores.put("miserver", "127.0.0.1");
		servidores.put("laptop", "192.168.1.6");
		servidores.put("mario", "25.97.10.142");

		setSize(500,500);
		setVisible(true);
		
		setTitle("Cliente");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		agregarPanel();
		colocarEtiquetas();
		agregarCajaDeTexto();
		colocarBoton();
		
		setLocationRelativeTo(null);
		setSize(500,500);
		setVisible(true);

		Thread hilo = new Thread(this);
		hilo.start();


	}

	private void agregarPanel() { 

		panel = new JPanel();

		panel.setLayout(null);
		this.getContentPane().add(panel);		
		panel.setBackground(color);
		
		
	}

	private void colocarEtiquetas() {
		
		JLabel etiqueta = new JLabel("Cliente");
		JLabel etiqueta_usarioServidorLoggin = new JLabel("Usario/Servidor");	
		JLabel etiqueta_contrasena = new JLabel("Contraseña");
		etiqueta_advertencias = new JLabel();

		etiqueta.setBounds(210, -30, 100, 130);
		etiqueta_usarioServidorLoggin.setBounds(48, 5, 109, 130);
		etiqueta_contrasena.setBounds(40, 5, 109, 130);
		etiqueta_advertencias.setBounds(50, 200, 95, 200);
		etiqueta.setFont(new Font("arial", 0, 20));
		etiqueta.setForeground(Color.WHITE);

		etiqueta_advertencias.setText("Advertencia");

		//panel.add(etiqueta_contrasena);

		panel.add(etiqueta);
		panel.add(etiqueta_usarioServidorLoggin);
		panel.add(etiqueta_advertencias);

	}

	private void agregarCajaDeTexto() {

		usuarioServidorLoggin = new JTextField();
		contrasenaLoggin  = new JTextField();

		usuarioServidorLoggin.setBounds(185, 60, 120, 25);
		contrasenaLoggin.setBounds(185, 80, 120, 25);

		panel.add(contrasenaLoggin);
		panel.add(usuarioServidorLoggin);

	}

	private void colocarBoton() {

		EnviarDataUserServidor evento = new EnviarDataUserServidor();

		boton = new JButton();
		boton.setText("Ingresar");
		boton.setBounds(185, 100, 120, 25);

		boton.addActionListener(evento);
		panel.add(boton);
		

	}


	@Override
	public void run() {

		try{

			ServerSocket servidor_cliente = new ServerSocket(1400);

			BufferedReader in;

			boolean flag = true;
			while(flag){

				Socket cliente = servidor_cliente.accept();

				in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

				String mensaje_error = in.readLine();


				etiqueta_advertencias.setText(mensaje_error);

				if(mensaje_error.equals("OK LOGGIN")){

					cliente.close();
					servidor_cliente.close();
					setVisible(false);
					flag = false;
					dispose();



				}
					

			}

		} catch(Exception runE){

			System.out.println(runE.getMessage());

		}

	}


	private class EnviarDataUserServidor implements ActionListener {

		String ip;
		String usuario;
		String servidor;
		BufferedReader in;
		PrintWriter out;

	
		@Override
		public void actionPerformed(ActionEvent e) {

			
			String usuarioServidor_Envio = usuarioServidorLoggin.getText();


			Boolean hizoMatch = Pattern.matches("\\w+[@]\\w+", usuarioServidor_Envio);

			
			if (hizoMatch == true) {

				
				String[] partes = usuarioServidor_Envio.split("@");	

					usuario = partes[0]; // 123
					servidor = partes[1];

					
					boolean resultado = servidores.containsKey(servidor);
					

					if(resultado == true) {

						try {

							ip = servidores.get(servidor);
							
							Socket sc = new Socket(ip, 1400);

							in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
							out = new PrintWriter(sc.getOutputStream(), true);

							out.print(usuario + "-" + contrasenaLoggin.getText());
							out.close();			

							usuarioServidorLoggin.setText(null);
							contrasenaLoggin.setText(null);

							sc.close();


						} catch (UnknownHostException uknE) {

							System.out.println("Error: Servidor no encontrado");

						} catch (IOException ioE){

							System.out.println(ioE.getMessage());
							String expt = ioE.getMessage();
							System.out.println(expt);
							
							if(expt == null){

								etiqueta_advertencias.setText("ERROR");

							}
						
						} 

					} else {

						System.out.println("Error: Servidor no encontrado.");
						System.out.println(servidores.get(servidor));
						usuarioServidorLoggin.setText(null);
						contrasenaLoggin.setText(null);
						etiqueta_advertencias.setText("Error: Servidor no encontrado."); 
					}

				} else {

					System.out.println("Error: Usuario/Servidor no escrito correctamente");
					usuarioServidorLoggin.setText(null);
					contrasenaLoggin.setText(null);

				}	


			}

		}

	}