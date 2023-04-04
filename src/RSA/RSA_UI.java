package RSA;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 * Author: 韩山师范学院 2017115132 肖泽锴<br>
 * lastUpdata-Time: May 28th, 2019<br>
 * function: 制作RSA密码器的用户交互界面<br>
 * @version RSA_UI 1.0.0<br>
 * */
@SuppressWarnings("serial")
public class RSA_UI extends JFrame {
	private JPanel contentPane;
	private JTextField textField_p;
	private JTextField textField_q;
	private JTextField textField_KU;
	private JFileChooser fileDialog;
	/**
	 * Launch the application.
	 * @param args 主函数的参数输入
	 */
	public static void main(String args []) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RSA_UI frame = new RSA_UI();
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
	public RSA_UI() {
		setTitle(" RSA密码器（韩山师范学院 2017115132 肖泽锴）");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 10, 1300, 1000);
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 80, 1282, 158);
		contentPane.add(scrollPane);
		
		JTextArea textArea_plaintext = new JTextArea();
		textArea_plaintext.setFont(new Font("宋体", Font.PLAIN, 20));
		textArea_plaintext.setLineWrap(true);
		scrollPane.setViewportView(textArea_plaintext);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 564, 1282, 158);
		contentPane.add(scrollPane_1);
		
		JTextArea textArea_toPlaintext = new JTextArea();
		textArea_toPlaintext.setLineWrap(true);
		textArea_toPlaintext.setFont(new Font("宋体", Font.PLAIN, 20));
		scrollPane_1.setViewportView(textArea_toPlaintext);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 321, 1282, 168);
		contentPane.add(scrollPane_2);
		
		JTextArea textArea_ciphertext = new JTextArea();
		textArea_ciphertext.setFont(new Font("宋体", Font.PLAIN, 20));
		textArea_ciphertext.setLineWrap(true);
		scrollPane_2.setViewportView(textArea_ciphertext);
		
		JLabel lblNewLabel = new JLabel("\u660E \u6587");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 20));
		lblNewLabel.setBounds(595, 36, 72, 18);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("\u5BC6 \u6587");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(595, 279, 72, 18);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("\u89E3 \u5BC6 \u540E \u7684 \u660E \u6587");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(539, 520, 189, 18);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("\u8F93\u5165\u5927\u7D20\u6570 p\uFF1A");
		lblNewLabel_3.setFont(new Font("宋体", Font.PLAIN, 20));
		lblNewLabel_3.setBounds(67, 758, 149, 18);
		contentPane.add(lblNewLabel_3);
		
		textField_p = new JTextField();
		textField_p.setFont(new Font("宋体", Font.PLAIN, 20));
		textField_p.setBounds(216, 751, 342, 34);
		contentPane.add(textField_p);
		textField_p.setColumns(10);
		
		textField_q = new JTextField();
		textField_q.setFont(new Font("宋体", Font.PLAIN, 20));
		textField_q.setColumns(10);
		textField_q.setBounds(843, 751, 342, 34);
		contentPane.add(textField_q);
		
		textField_KU = new JTextField();
		textField_KU.setFont(new Font("宋体", Font.PLAIN, 20));
		textField_KU.setColumns(10);
		textField_KU.setBounds(508, 808, 342, 32);
		contentPane.add(textField_KU);
		
		fileDialog = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("记事本文件", "txt");
		fileDialog.setFileFilter(filter);
		
		Police police = new Police(textArea_plaintext, textArea_ciphertext, textArea_toPlaintext
				, textField_p, textField_q, textField_KU, fileDialog, this);
		JButton button_encrypt = new JButton("\u52A0 \u5BC6");
		button_encrypt.setForeground(Color.RED);
		button_encrypt.setFont(new Font("宋体", Font.PLAIN, 18));
		button_encrypt.setBounds(67, 867, 138, 34);
		contentPane.add(button_encrypt);
		button_encrypt.addActionListener(police);
		
		JButton button_decrypt = new JButton("\u89E3 \u5BC6");
		button_decrypt.setForeground(Color.GREEN);
		button_decrypt.setFont(new Font("宋体", Font.PLAIN, 18));
		button_decrypt.setBounds(438, 869, 138, 34);
		contentPane.add(button_decrypt);
		button_decrypt.addActionListener(police);
		
		JButton button_clear = new JButton("\u6E05 \u7A7A");
		button_clear.setForeground(Color.RED);
		button_clear.setFont(new Font("宋体", Font.PLAIN, 18));
		button_clear.setBounds(1081, 867, 138, 34);
		contentPane.add(button_clear);
		button_clear.addActionListener(police);
		
		JButton button_input_plaintext = new JButton("\u5BFC \u5165 \u660E \u6587");
		button_input_plaintext.setForeground(Color.GREEN);
		button_input_plaintext.setFont(new Font("宋体", Font.PLAIN, 18));
		button_input_plaintext.setBounds(696, 28, 138, 34);
		contentPane.add(button_input_plaintext);
		button_input_plaintext.addActionListener(police);
		
		JButton button_input_ciphertext = new JButton("\u5BFC \u5165 \u5BC6 \u6587");
		button_input_ciphertext.setForeground(Color.GREEN);
		button_input_ciphertext.setFont(new Font("宋体", Font.PLAIN, 18));
		button_input_ciphertext.setBounds(696, 277, 138, 34);
		contentPane.add(button_input_ciphertext);
		button_input_ciphertext.addActionListener(police);
		
		JLabel lblNewLabel_4 = new JLabel("\u8F93\u5165\u5927\u7D20\u6570 q:");
		lblNewLabel_4.setFont(new Font("宋体", Font.PLAIN, 20));
		lblNewLabel_4.setBounds(696, 758, 149, 18);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("\u516C \u94A5\uFF1A");
		lblNewLabel_5.setFont(new Font("宋体", Font.PLAIN, 20));
		lblNewLabel_5.setBounds(422, 817, 72, 18);
		contentPane.add(lblNewLabel_5);
		
		JButton button_create_KU = new JButton("\u751F \u6210 \u516C \u94A5");
		button_create_KU.setForeground(Color.GREEN);
		button_create_KU.setFont(new Font("宋体", Font.PLAIN, 18));
		button_create_KU.setBounds(765, 862, 138, 34);
		contentPane.add(button_create_KU);
		button_create_KU.addActionListener(police);
	}
}
