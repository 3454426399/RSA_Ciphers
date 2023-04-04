package RSA;

import java.math.BigInteger;
import java.util.Random;

/**
 * Author: 韩山师范学院 2017115132 肖泽锴<br>
 * lastUpdata-Time: May 29th, 2019<br>
 * function: 实现RSA加密解密过程中的逻辑处理过程，包括素性检测，大数的模幂运算，求逆元等<br>
 * @version RSATool 1.0.0<br>
 * */
public class RSATool {
	/**RSA的大素数p*/
	static BigInteger p;
	/**RSA的大素数q*/
	static BigInteger q;
	/**RSA的公开密钥KU*/
	static BigInteger KU;
	/**RSA的保密密钥KR*/
	static BigInteger KR;
	/**大整数常量 -1*/
	final static BigInteger negativeOne = new BigInteger("-1");
	/**大整数常量 2*/
	final static BigInteger two = new BigInteger("2");
	/**大整数常量 127*/
	final static BigInteger onehtwoseven = new BigInteger("127");
	
	/**
	 * 方法名：Miller_Rabin<br>
	 * 功能：判断一个大整数是否是素数，若通过检测，则该数是合数的概率不超过1/4，可以通过多次调用来确认该数是否为素数<br>
	 * @param number 待判断的大整数
	 * @param b 满足大于1且与number - 1 互素的整数
	 * @return int
	 * */
	public static int Miller_Rabin(BigInteger number, BigInteger b) {
		BigInteger s = BigInteger.ZERO, t = BigInteger.ZERO;
		BigInteger n = number.subtract(BigInteger.ONE);
		
		/*把大整数 n - 1 转化为2 ^ s * t*/
		while((n.mod(two).compareTo(BigInteger.ZERO) == 0) == true) {
			s = s.add(BigInteger.ONE);
			n = n.divide(two);
		}
		t = n;
		BigInteger flag = modularExponentiation(b, t, number);
		/*如果 b ^ t 模 number 等于1或者-1即number - 1，则该数可能是素数*/
		if(flag.compareTo(BigInteger.ONE) == 0 || 
				flag.compareTo(number.subtract(BigInteger.ONE)) == 0) {
			return 1;
		}
		else {
			/*(b ^ t) ^ 2 ^ m等于1则number为素数，其中对于m属于(1， 2...s - 1)只要一个成立即可*/
			for(BigInteger i = BigInteger.ONE; i.compareTo(s.subtract(BigInteger.ONE)) <= 0; i = i.add(BigInteger.ONE)) {
				flag = modularExponentiation(flag, two, number);
				if(flag.compareTo(number.subtract(BigInteger.ONE)) == 0) {
					return 1;
				}
			}
			return 0;
		}
	}
	
	/**
	 * 方法名：Euclid<br>
	 * 功能：求一个大整数模另一个大整数的乘法逆元<br>
	 * @param number 待求逆的大整数<br>
	 * @param modulus 模数<br>
	 * @return BigInteger<br>
	 * */
	public static BigInteger Euclid(BigInteger number, BigInteger modulus) {
		BigInteger m = modulus, n = number, b_1 = BigInteger.ZERO, b_2 = BigInteger.ONE;
		BigInteger q = m.divide(n), r = m.subtract(q.multiply(n));
		
		while((r.compareTo(BigInteger.ZERO) != 0) == true) {
			m = n;
			n = r;
			BigInteger t = b_2;
			b_2 = b_1.subtract(q.multiply(b_2));
			b_1 = t;
			q = m.divide(n);
			r = m.subtract(q.multiply(n));
		}
		return b_2.add(modulus).mod(modulus);
	}
	
	/**
	 * 方法名：createKU<br>
	 * 功能：随机生成公共密钥KU <br>
	 * @return BigInteger<br>
	 * */
	public static BigInteger createKU() {
		BigInteger fn = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		BigInteger flag = BigInteger.ONE;
		int times = 0;
		Random rnd = new Random();
		
		while(flag.compareTo(fn) == -1) {
			flag = flag.multiply(RSATool.two);
			times++;
		}
		/*一直循环直到找到一个在 1 至 fn且与 fn 互素的整数KU为止*/
		for( ; ;) {
			KU = new BigInteger(times, rnd);
			
			if(KU.compareTo(BigInteger.ONE) > 0 && KU.compareTo(fn.subtract(BigInteger.ONE)) < 0 &&
					KU.gcd(fn).compareTo(BigInteger.ONE) == 0) {
				break;
			}
		}
		return KU;
	}
	
	/**
	 * 方法名：createb<br>
	 * 功能：随机产生1-(n - 1)范围内的整数b用于素性检测<br>
	 * @param number 产生随机数的上界
	 * @return BigInteger
	 * */
	public static BigInteger createb(BigInteger number) {
		BigInteger n = number;
		BigInteger flag = BigInteger.ONE;
		int times = 0;
		Random rnd = new Random();
		BigInteger b;
		
		/*找到使得整数n小于2^times的指数times*/
		while(flag.compareTo(n) == -1) {
			flag = flag.multiply(RSATool.two);
			times++;
		}
		for( ; ;) {
			b = new BigInteger(times, rnd);    /*随机产生0-2^times之间的整数*/
			
			/*当随机产生的整数满足大于0且小于n-1时，成功，跳出循环*/
			if(b.compareTo(BigInteger.ZERO) > 0 && b.compareTo(n) == -1) {
				break;
			}
		}
		return b;
	}
	
	/**
	 * 方法名：modularExponentiation<br>
	 * 功能：利用模重复平方算法来实现大数的模幂运算<br>
	 * @param number 模幂运算的底数
	 * @param index 模幂运算的指数
	 * @param mod 模幂运算的模数
	 * @return BigInteger
	 * */
	public static BigInteger modularExponentiation(BigInteger number, BigInteger index, BigInteger mod) {
		byte bit [] = get2048bit(index);    //把指数index转化为2048比特的二进制数
		BigInteger s = BigInteger.ONE;
		
		for(int i = bit.length - 1; i >= 0; i--) {
			if(bit [i] == 1) {
				//若该二进制位为1，则乘上对应的十进制数再除以模数求余
				s = s.multiply(number).remainder(mod);
			}
			number = number.pow(2).mod(mod);    //不断平方除以模数求余
		}
		return s;
	}
	
	/**
	 * 方法名：get2048bit
	 * 功能：把大整数转化为对应的2048比特的原码
	 * @param number 待转化的大整数
	 * @return byte []
	 * */
	public static byte [] get2048bit(BigInteger number) {
		byte bit [] = new byte [0x800];
		BigInteger n = number;
		
		/*若该数小于0，乘以-1先转化为正的*/
		if(n.compareTo(BigInteger.ZERO) < 0) {
			number = number.multiply(negativeOne);
		}
		for(int i = bit.length - 1; i >= 0; i--) {
			if(number.equals(BigInteger.ZERO) == false) {
				bit [i] = (byte) (number.mod(two) == BigInteger.ZERO ? 0 : 1);
				number = number.divide(two);
			}
		}
		/*若为负数，二进制最高位为1*/
		if(n.compareTo(BigInteger.ZERO) < 0) {
			bit [0] = 1;
		}
		return bit;
	}
}