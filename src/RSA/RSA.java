package RSA;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * author: 韩山师范学院 2017115132 肖泽锴<br>
 * lastUpdata-Time: May 29th, 2019<br>
 * function: 这个类主要提供加密、解密的功能实现<br>
 * @version RSA 1.0.0<br>
 * */
public class RSA {
	/**待加密/解密的明文/密文*/
	String message;
	/**加密或解密后的结果存储，初始值为空*/
	String result = "";
	/**RSA密码的公钥*/
	BigInteger Ku;
	/**RSA密码的私钥*/
	BigInteger Kr;
	
	/**
	 * @param message 明文或密文段
	 * @param Ku RSA的公钥
	 * */
	RSA(String message, BigInteger Ku){
		this.message = message;
		this.Ku = Ku;
	}
	
	/**
	 * 方法名: encrypt<br>
	 * 功能: 完成RSA公钥密码算法的加密，密文为二进制流<br>
	 * @return String
	 * @throws UnsupportedEncodingException 不支持编码异常
	 * */
	public String encrypt() throws UnsupportedEncodingException {
		byte[] bit = message.getBytes("utf-8");    //把明文段转化为utf-8编码
		BigInteger ms [] = new BigInteger [bit.length];
		BigInteger n = RSATool.p.multiply(RSATool.q);
		
		for(int i = 0; i < bit.length; i++) {
			ms [i] = new BigInteger(String.valueOf(bit [i]));
		}
		/*对每个编码得到的整数进行加密转换*/
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
	 * 方法名：decrypt<br>
	 * 功能：完成RSA公钥密码算法的解密<br>
	 * @return String
	 * @throws UnsupportedEncodingException 不支持编码异常
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
		/*把每2048比特的密文通过解密变换转化为原来的明文*/
		for(int i = 0; i <= message.length() - 0X800; i = i + 0x800) {
			String s = message.substring(i, i + 0x800);
			c = s.toCharArray();
			
			/*计算2048比特二进制对应的十进制数*/
			for(int j = c.length - 1; j >= 1; j--) {
				if(c [j] == '1') {
					ms [k] = ms [k].add(flag);
				}
				flag = flag.multiply(RSATool.two);
			}
			/*若为负数，乘以-1*/
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
