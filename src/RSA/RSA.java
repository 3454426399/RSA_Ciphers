package RSA;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * author: ��ɽʦ��ѧԺ 2017115132 Ф����<br>
 * lastUpdata-Time: May 29th, 2019<br>
 * function: �������Ҫ�ṩ���ܡ����ܵĹ���ʵ��<br>
 * @version RSA 1.0.0<br>
 * */
public class RSA {
	/**������/���ܵ�����/����*/
	String message;
	/**���ܻ���ܺ�Ľ���洢����ʼֵΪ��*/
	String result = "";
	/**RSA����Ĺ�Կ*/
	BigInteger Ku;
	/**RSA�����˽Կ*/
	BigInteger Kr;
	
	/**
	 * @param message ���Ļ����Ķ�
	 * @param Ku RSA�Ĺ�Կ
	 * */
	RSA(String message, BigInteger Ku){
		this.message = message;
		this.Ku = Ku;
	}
	
	/**
	 * ������: encrypt<br>
	 * ����: ���RSA��Կ�����㷨�ļ��ܣ�����Ϊ��������<br>
	 * @return String
	 * @throws UnsupportedEncodingException ��֧�ֱ����쳣
	 * */
	public String encrypt() throws UnsupportedEncodingException {
		byte[] bit = message.getBytes("utf-8");    //�����Ķ�ת��Ϊutf-8����
		BigInteger ms [] = new BigInteger [bit.length];
		BigInteger n = RSATool.p.multiply(RSATool.q);
		
		for(int i = 0; i < bit.length; i++) {
			ms [i] = new BigInteger(String.valueOf(bit [i]));
		}
		/*��ÿ������õ����������м���ת��*/
		for(int i = 0; i < ms.length; i++) {
			ms [i] = RSATool.modularExponentiation(ms [i], Ku, n);
			byte b [] = RSATool.get2048bit(ms [i]);
			char c [] = new char [b.length];
			
			for(int j = 0; j < b.length; j++) {
				c [j] = (char) (b [j] + 0x30);
			}
			result = result + new String(c);
		}
		return result.substring(0, result.length());
	}
	
	/**
	 * ��������decrypt<br>
	 * ���ܣ����RSA��Կ�����㷨�Ľ���<br>
	 * @return String
	 * @throws UnsupportedEncodingException ��֧�ֱ����쳣
	 * */
	public String decrypt() throws UnsupportedEncodingException {
		char c [];
		BigInteger ms [] = new BigInteger [message.length() / 0x800];
		byte b [] = new byte [ms.length];
		BigInteger flag = BigInteger.ONE;
		BigInteger n = RSATool.p.multiply(RSATool.q);
		BigInteger fn = RSATool.p.subtract(BigInteger.ONE).multiply(RSATool.q.subtract(BigInteger.ONE));
		int k = 0;
		Kr = RSATool.Euclid(Ku, fn);
		
		for(int i = 0; i < ms.length; i++) {
			ms [i] = BigInteger.ZERO;
		}
		/*��ÿ2048���ص�����ͨ�����ܱ任ת��Ϊԭ��������*/
		for(int i = 0; i <= message.length() - 0X800; i = i + 0x800) {
			String s = message.substring(i, i + 0x800);
			c = s.toCharArray();
			
			/*����2048���ض����ƶ�Ӧ��ʮ������*/
			for(int j = c.length - 1; j >= 1; j--) {
				if(c [j] == '1') {
					ms [k] = ms [k].add(flag);
				}
				flag = flag.multiply(RSATool.two);
			}
			/*��Ϊ����������-1*/
			if(c [0] == '1') {
				ms [k] = ms [k].multiply(RSATool.negativeOne);
			}
			String s1 = RSATool.modularExponentiation(ms [k], Kr, n).toString();
			b [k] = Byte.valueOf(s1);
			k++;
			flag = BigInteger.ONE;
		}
		result = new String(b,"utf-8");
		return result;
	}
}
