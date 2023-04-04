package RSA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * Author: 韩山师范学院 2017115132 肖泽锴<br>
 * lastUpdata-Time: May 29th, 2019<br>
 * function: 对事件做触发处理，与用户形成交互<br>
 * @version Police 1.0.0<br>
 * */
public class Police implements ActionListener{
	/**输入待加密明文的文本区域*/
	JTextArea textArea_plaintext;
	/**输入待解密密文的文本区域*/
	JTextArea textArea_ciphertext;
	/**显示解密后的明文的文本区域*/
	JTextArea textArea_toPlaintext;
	/**输入公钥的文本框*/
	JTextField textField_publicKey;
	/**输入大素数p的文本框*/
	JTextField textField_p;
	/**输入大素数q的文本框*/
	JTextField textField_q;
	/**当前的UI界面*/
	RSA_UI ui;
	/**文件对话框，用于打开欲读取的文件*/
	JFileChooser fileDialog;
	
	/**
	 * @param textArea_plaintext  输入明文的文本区域
	 * @param textArea_ciphertext   输入密文的文本区域
	 * @param textArea_toPlaintext 显示解密后的明文的文本区域
	 * @param textField_p  输入大素数p的文本框
	 * @param textField_q  输入大素数q的文本框
	 * @param textField_publicKey  输入RSA的公共密钥
	 * @param fileDialog  文件对话框，用户选择打开欲读取的文件
	 * @param ui  当前用户交互界面
	 * */
	Police(JTextArea textArea_plaintext, JTextArea textArea_ciphertext, JTextArea textArea_toPlaintext,
			JTextField textField_p, JTextField textField_q, JTextField textField_publicKey, 
			JFileChooser fileDialog, RSA_UI ui){
		this.textArea_plaintext = textArea_plaintext;
		this.textArea_ciphertext = textArea_ciphertext;
		this.textArea_toPlaintext = textArea_toPlaintext;
		this.textField_publicKey = textField_publicKey;
		this.textField_p = textField_p;
		this.textField_q = textField_q;
		this.fileDialog = fileDialog;
		this.ui = ui;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton bn = (JButton)(e.getSource());
		
		if(bn.getText().equals("加 密")) {
			long l = System.currentTimeMillis();
			Date date = new Date(l);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			System.out.println(dateFormat.format(date));
			
			if(textArea_plaintext.getText().equals("")) {
				JOptionPane.showMessageDialog(ui, "请输入待加密的明文!",
						"错误提示框", JOptionPane.WARNING_MESSAGE);
			}
			else if(textField_p.getText().equals("") || textField_q.getText().equals("")){
				JOptionPane.showMessageDialog(ui, "请输入大素数p和q获取公钥!",
						"错误提示框", JOptionPane.WARNING_MESSAGE);
			}
			else if(textField_publicKey.getText().equals("")) {
				JOptionPane.showMessageDialog(ui, "请输入获得的公钥!",
						"错误提示框", JOptionPane.WARNING_MESSAGE);
			}
			else {
				String plainText = textArea_plaintext.getText();
				RSA rsa = new RSA(plainText, RSATool.KU);
				try {
					String ciphertext = rsa.encrypt();
					textArea_ciphertext.setText(ciphertext);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			long l2 = System.currentTimeMillis();
			Date date2 = new Date(l2);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			System.out.println(dateFormat2.format(date2));
		}
		else if(bn.getText().equals("解 密")) {
			if(textArea_ciphertext.getText().equals("")) {
				JOptionPane.showMessageDialog(ui, "请输入待解密的密文!",
						"错误提示框", JOptionPane.WARNING_MESSAGE);
			}
			else if(textField_p.getText().equals("") || textField_q.getText().equals("")){
				JOptionPane.showMessageDialog(ui, "请输入大素数p和q!",
						"错误提示框", JOptionPane.WARNING_MESSAGE);
			}
			else if(textField_publicKey.getText().equals("")) {
				JOptionPane.showMessageDialog(ui, "请输入公钥!",
						"错误提示框", JOptionPane.WARNING_MESSAGE);
			}
			else {
				String ciphertext = textArea_ciphertext.getText();
				RSATool.p = new BigInteger(textField_p.getText());
				RSATool.q = new BigInteger(textField_q.getText());
				int flag = 1;
				
				for(int i = 0; i < 10; i++) {
					if(RSATool.Miller_Rabin(RSATool.p, RSATool.createb(RSATool.p)) == 0) {
						JOptionPane.showMessageDialog(ui, "您输入的大整数p不是素数!",
								"错误提示框", JOptionPane.WARNING_MESSAGE);
						flag++;
						break;
					}
				}
				for(int i = 0; i < 10; i++) {
					if(RSATool.Miller_Rabin(RSATool.q, RSATool.createb(RSATool.q)) == 0 && flag == 1) {
						JOptionPane.showMessageDialog(ui, "您输入的大整数q不是素数!",
									"错误提示框", JOptionPane.WARNING_MESSAGE);
						flag++;
						break;
					}
				}
				BigInteger Ku = new BigInteger(textField_publicKey.getText());
				if(Ku.gcd(RSATool.p.subtract(BigInteger.ONE).multiply
						(RSATool.q.subtract(BigInteger.ONE))).compareTo(BigInteger.ONE) != 0 && flag == 1) {
					JOptionPane.showMessageDialog(ui, "公钥必须与(p-1)*(q-1)互素!",
							"错误提示框", JOptionPane.WARNING_MESSAGE);
					flag++;
				}
				if(flag == 1){
					RSA rsa = new RSA(ciphertext, Ku);
					String plaintext = null;
					
					try {
						plaintext = rsa.decrypt();
						textArea_toPlaintext.setText(plaintext);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		else if(bn.getText().equals("导 入 明 文")){
			int state = fileDialog.showOpenDialog(ui);
			
			if(state == JFileChooser.APPROVE_OPTION) {
				textArea_plaintext.setText(null);
				try {
					File dir = fileDialog.getCurrentDirectory();
					String name = fileDialog.getSelectedFile().getName();
					File file = new File(dir, name);
					FileReader fileReader = new FileReader(file);
					BufferedReader in = new BufferedReader(fileReader);
					String s = null;
					while((s = in.readLine()) != null) {
						textArea_plaintext.append(s + "\n");
					}
					in.close();
					fileReader.close();
				}
				catch(IOException exp) {}
			}
		}
		else if(bn.getText().equals("导 入 密 文")) {
			int state = fileDialog.showOpenDialog(ui);
			
			if(state == JFileChooser.APPROVE_OPTION) {
				textArea_ciphertext.setText(null);
				try {
					File dir = fileDialog.getCurrentDirectory();
					String name = fileDialog.getSelectedFile().getName();
					File file = new File(dir, name);
					FileReader fileReader = new FileReader(file);
					BufferedReader in = new BufferedReader(fileReader);
					String s = null;
					while((s = in.readLine()) != null) {
						textArea_ciphertext.append(s + "\n");
					}
					in.close();
					fileReader.close();
				}
				catch(IOException exp) {}
			}
		}
		else if(bn.getText().equals("生 成 公 钥")) {
			int flag = 1;
			
			if(textField_p.getText().equals("") || textField_q.getText().equals("")){
				JOptionPane.showMessageDialog(ui, "请输入大素数p和q!",
						"错误提示框", JOptionPane.WARNING_MESSAGE);
			}
			else {
				RSATool.p = new BigInteger(textField_p.getText());
				RSATool.q = new BigInteger(textField_q.getText());
				
				for(int i = 0; i < 10; i++) {
					System.out.println();
					if(RSATool.Miller_Rabin(RSATool.p, RSATool.createb(RSATool.p)) == 0) {
						JOptionPane.showMessageDialog(ui, "您输入的大整数p不是素数!",
								"错误提示框", JOptionPane.WARNING_MESSAGE);
						flag++;
						break;
					}
				}
				for(int i = 0; i < 10; i++) {
					if(RSATool.Miller_Rabin(RSATool.q, RSATool.createb(RSATool.q)) == 0 && flag == 1) {
						JOptionPane.showMessageDialog(ui, "您输入的大整数q不是素数!",
									"错误提示框", JOptionPane.WARNING_MESSAGE);
						flag++;
						break;
					}
				}
				if(flag == 1) {
					if(RSATool.p.multiply(RSATool.q).compareTo(RSATool.onehtwoseven) <= 0) {
						JOptionPane.showMessageDialog(ui, "两素数的乘积必须大于127",
								"错误提示框", JOptionPane.WARNING_MESSAGE);
					}
					else {
						BigInteger Ku = RSATool.createKU();
						JOptionPane.showMessageDialog(ui, "加密的公钥为" + Ku,
								"信息提示框", JOptionPane.INFORMATION_MESSAGE);
						textField_publicKey.setText(Ku.toString());
					}
				}
			}
		}
		else {
			textArea_plaintext.setText(null);
			textArea_ciphertext.setText(null);
			textArea_toPlaintext.setText(null);
			textField_p.setText(null);
			textField_q.setText(null);
			textField_publicKey.setText(null);
		}
	}
}
