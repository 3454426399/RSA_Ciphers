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
 * Author: ��ɽʦ��ѧԺ 2017115132 Ф����<br>
 * lastUpdata-Time: May 29th, 2019<br>
 * function: ���¼��������������û��γɽ���<br>
 * @version Police 1.0.0<br>
 * */
public class Police implements ActionListener{
	/**������������ĵ��ı�����*/
	JTextArea textArea_plaintext;
	/**������������ĵ��ı�����*/
	JTextArea textArea_ciphertext;
	/**��ʾ���ܺ�����ĵ��ı�����*/
	JTextArea textArea_toPlaintext;
	/**���빫Կ���ı���*/
	JTextField textField_publicKey;
	/**���������p���ı���*/
	JTextField textField_p;
	/**���������q���ı���*/
	JTextField textField_q;
	/**��ǰ��UI����*/
	RSA_UI ui;
	/**�ļ��Ի������ڴ�����ȡ���ļ�*/
	JFileChooser fileDialog;
	
	/**
	 * @param textArea_plaintext  �������ĵ��ı�����
	 * @param textArea_ciphertext   �������ĵ��ı�����
	 * @param textArea_toPlaintext ��ʾ���ܺ�����ĵ��ı�����
	 * @param textField_p  ���������p���ı���
	 * @param textField_q  ���������q���ı���
	 * @param textField_publicKey  ����RSA�Ĺ�����Կ
	 * @param fileDialog  �ļ��Ի����û�ѡ�������ȡ���ļ�
	 * @param ui  ��ǰ�û���������
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
		
		if(bn.getText().equals("�� ��")) {
			long l = System.currentTimeMillis();
			Date date = new Date(l);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			System.out.println(dateFormat.format(date));
			
			if(textArea_plaintext.getText().equals("")) {
				JOptionPane.showMessageDialog(ui, "����������ܵ�����!",
						"������ʾ��", JOptionPane.WARNING_MESSAGE);
			}
			else if(textField_p.getText().equals("") || textField_q.getText().equals("")){
				JOptionPane.showMessageDialog(ui, "�����������p��q��ȡ��Կ!",
						"������ʾ��", JOptionPane.WARNING_MESSAGE);
			}
			else if(textField_publicKey.getText().equals("")) {
				JOptionPane.showMessageDialog(ui, "�������õĹ�Կ!",
						"������ʾ��", JOptionPane.WARNING_MESSAGE);
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
		else if(bn.getText().equals("�� ��")) {
			if(textArea_ciphertext.getText().equals("")) {
				JOptionPane.showMessageDialog(ui, "����������ܵ�����!",
						"������ʾ��", JOptionPane.WARNING_MESSAGE);
			}
			else if(textField_p.getText().equals("") || textField_q.getText().equals("")){
				JOptionPane.showMessageDialog(ui, "�����������p��q!",
						"������ʾ��", JOptionPane.WARNING_MESSAGE);
			}
			else if(textField_publicKey.getText().equals("")) {
				JOptionPane.showMessageDialog(ui, "�����빫Կ!",
						"������ʾ��", JOptionPane.WARNING_MESSAGE);
			}
			else {
				String ciphertext = textArea_ciphertext.getText();
				RSATool.p = new BigInteger(textField_p.getText());
				RSATool.q = new BigInteger(textField_q.getText());
				int flag = 1;
				
				for(int i = 0; i < 10; i++) {
					if(RSATool.Miller_Rabin(RSATool.p, RSATool.createb(RSATool.p)) == 0) {
						JOptionPane.showMessageDialog(ui, "������Ĵ�����p��������!",
								"������ʾ��", JOptionPane.WARNING_MESSAGE);
						flag++;
						break;
					}
				}
				for(int i = 0; i < 10; i++) {
					if(RSATool.Miller_Rabin(RSATool.q, RSATool.createb(RSATool.q)) == 0 && flag == 1) {
						JOptionPane.showMessageDialog(ui, "������Ĵ�����q��������!",
									"������ʾ��", JOptionPane.WARNING_MESSAGE);
						flag++;
						break;
					}
				}
				BigInteger Ku = new BigInteger(textField_publicKey.getText());
				if(Ku.gcd(RSATool.p.subtract(BigInteger.ONE).multiply
						(RSATool.q.subtract(BigInteger.ONE))).compareTo(BigInteger.ONE) != 0 && flag == 1) {
					JOptionPane.showMessageDialog(ui, "��Կ������(p-1)*(q-1)����!",
							"������ʾ��", JOptionPane.WARNING_MESSAGE);
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
		else if(bn.getText().equals("�� �� �� ��")){
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
		else if(bn.getText().equals("�� �� �� ��")) {
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
		else if(bn.getText().equals("�� �� �� Կ")) {
			int flag = 1;
			
			if(textField_p.getText().equals("") || textField_q.getText().equals("")){
				JOptionPane.showMessageDialog(ui, "�����������p��q!",
						"������ʾ��", JOptionPane.WARNING_MESSAGE);
			}
			else {
				RSATool.p = new BigInteger(textField_p.getText());
				RSATool.q = new BigInteger(textField_q.getText());
				
				for(int i = 0; i < 10; i++) {
					System.out.println();
					if(RSATool.Miller_Rabin(RSATool.p, RSATool.createb(RSATool.p)) == 0) {
						JOptionPane.showMessageDialog(ui, "������Ĵ�����p��������!",
								"������ʾ��", JOptionPane.WARNING_MESSAGE);
						flag++;
						break;
					}
				}
				for(int i = 0; i < 10; i++) {
					if(RSATool.Miller_Rabin(RSATool.q, RSATool.createb(RSATool.q)) == 0 && flag == 1) {
						JOptionPane.showMessageDialog(ui, "������Ĵ�����q��������!",
									"������ʾ��", JOptionPane.WARNING_MESSAGE);
						flag++;
						break;
					}
				}
				if(flag == 1) {
					if(RSATool.p.multiply(RSATool.q).compareTo(RSATool.onehtwoseven) <= 0) {
						JOptionPane.showMessageDialog(ui, "�������ĳ˻��������127",
								"������ʾ��", JOptionPane.WARNING_MESSAGE);
					}
					else {
						BigInteger Ku = RSATool.createKU();
						JOptionPane.showMessageDialog(ui, "���ܵĹ�ԿΪ" + Ku,
								"��Ϣ��ʾ��", JOptionPane.INFORMATION_MESSAGE);
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
