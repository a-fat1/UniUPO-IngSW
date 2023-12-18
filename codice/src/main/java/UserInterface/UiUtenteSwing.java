package UserInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class UiUtenteSwing extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_1;
	private String pulsantiRegistrazione[];
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UiUtenteSwing frame = new UiUtenteSwing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UiUtenteSwing() {
		/*
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new GridLayout(0, 2, 50, 5));
		
		JLabel lblNewLabel = new JLabel("Nome");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblNewLabel);
		
		textField_1 = new JTextField();
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblCognome = new JLabel("Cognome");
		lblCognome.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblCognome);
		
		textField = new JTextField();
		getContentPane().add(textField);
		textField.setColumns(10);
		pulsantiRegistrazione = new String[2];
		pulsantiRegistrazione[0] = "Cancella";
		pulsantiRegistrazione[1] = "avanti";
		
		JOptionPane.showOptionDialog(null, getContentPane(), "Registrazione (clicca su X o cancella per uscire)", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, pulsantiRegistrazione, "Registrati");
		*/
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(new GridLayout(0, 2, 50, 5));
		
		JLabel lblNewLabel = new JLabel("Nome");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel);
		
		textField_1 = new JTextField();
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblCognome = new JLabel("Cognome");
		lblCognome.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblCognome);
		
		textField = new JTextField();
		contentPane.add(textField);
		textField.setColumns(10);
		pulsantiRegistrazione = new String[2];
		pulsantiRegistrazione[0] = "Cancella";
		pulsantiRegistrazione[1] = "avanti";
		
		JOptionPane.showOptionDialog(null, contentPane, "Registrazione (clicca su X o cancella per uscire)", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, pulsantiRegistrazione, "Registrati");
	}

}
