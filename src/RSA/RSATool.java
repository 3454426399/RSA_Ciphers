package RSA;

import java.math.BigInteger;
import java.util.Random;

/**
 * Author: ��ɽʦ��ѧԺ 2017115132 Ф����<br>
 * lastUpdata-Time: May 29th, 2019<br>
 * function: ʵ��RSA���ܽ��ܹ����е��߼�������̣��������Լ�⣬������ģ�����㣬����Ԫ��<br>
 * @version RSATool 1.0.0<br>
 * */
public class RSATool {
	/**RSA�Ĵ�����p*/
	static BigInteger p;
	/**RSA�Ĵ�����q*/
	static BigInteger q;
	/**RSA�Ĺ�����ԿKU*/
	static BigInteger KU;
	/**RSA�ı�����ԿKR*/
	static BigInteger KR;
	/**���������� -1*/
	final static BigInteger negativeOne = new BigInteger("-1");
	/**���������� 2*/
	final static BigInteger two = new BigInteger("2");
	/**���������� 127*/
	final static BigInteger onehtwoseven = new BigInteger("127");
	
	/**
	 * ��������Miller_Rabin<br>
	 * ���ܣ��ж�һ���������Ƿ�����������ͨ����⣬������Ǻ����ĸ��ʲ�����1/4������ͨ����ε�����ȷ�ϸ����Ƿ�Ϊ����<br>
	 * @param number ���жϵĴ�����
	 * @param b �������1����number - 1 ���ص�����
	 * @return int
	 * */
	public static int Miller_Rabin(BigInteger number, BigInteger b) {
		BigInteger s = BigInteger.ZERO, t = BigInteger.ZERO;
		BigInteger n = number.subtract(BigInteger.ONE);
		
		/*�Ѵ����� n - 1 ת��Ϊ2 ^ s * t*/
		while((n.mod(two).compareTo(BigInteger.ZERO) == 0) == true) {
			s = s.add(BigInteger.ONE);
			n = n.divide(two);
		}
		t = n;
		BigInteger flag = modularExponentiation(b, t, number);
		/*��� b ^ t ģ number ����1����-1��number - 1�����������������*/
		if(flag.compareTo(BigInteger.ONE) == 0 || 
				flag.compareTo(number.subtract(BigInteger.ONE)) == 0) {
			return 1;
		}
		else {
			/*(b ^ t) ^ 2 ^ m����1��numberΪ���������ж���m����(1�� 2...s - 1)ֻҪһ����������*/
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
	 * ��������Euclid<br>
	 * ���ܣ���һ��������ģ��һ���������ĳ˷���Ԫ<br>
	 * @param number ������Ĵ�����<br>
	 * @param modulus ģ��<br>
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
	 * ��������createKU<br>
	 * ���ܣ�������ɹ�����ԿKU <br>
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
		/*һֱѭ��ֱ���ҵ�һ���� 1 �� fn���� fn ���ص�����KUΪֹ*/
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
	 * ��������createb<br>
	 * ���ܣ��������1-(n - 1)��Χ�ڵ�����b�������Լ��<br>
	 * @param number ������������Ͻ�
	 * @return BigInteger
	 * */
	public static BigInteger createb(BigInteger number) {
		BigInteger n = number;
		BigInteger flag = BigInteger.ONE;
		int times = 0;
		Random rnd = new Random();
		BigInteger b;
		
		/*�ҵ�ʹ������nС��2^times��ָ��times*/
		while(flag.compareTo(n) == -1) {
			flag = flag.multiply(RSATool.two);
			times++;
		}
		for( ; ;) {
			b = new BigInteger(times, rnd);    /*�������0-2^times֮�������*/
			
			/*����������������������0��С��n-1ʱ���ɹ�������ѭ��*/
			if(b.compareTo(BigInteger.ZERO) > 0 && b.compareTo(n) == -1) {
				break;
			}
		}
		return b;
	}
	
	/**
	 * ��������modularExponentiation<br>
	 * ���ܣ�����ģ�ظ�ƽ���㷨��ʵ�ִ�����ģ������<br>
	 * @param number ģ������ĵ���
	 * @param index ģ�������ָ��
	 * @param mod ģ�������ģ��
	 * @return BigInteger
	 * */
	public static BigInteger modularExponentiation(BigInteger number, BigInteger index, BigInteger mod) {
		byte bit [] = get2048bit(index);    //��ָ��indexת��Ϊ2048���صĶ�������
		BigInteger s = BigInteger.ONE;
		
		for(int i = bit.length - 1; i >= 0; i--) {
			if(bit [i] == 1) {
				//���ö�����λΪ1������϶�Ӧ��ʮ�������ٳ���ģ������
				s = s.multiply(number).remainder(mod);
			}
			number = number.pow(2).mod(mod);    //����ƽ������ģ������
		}
		return s;
	}
	
	/**
	 * ��������get2048bit
	 * ���ܣ��Ѵ�����ת��Ϊ��Ӧ��2048���ص�ԭ��
	 * @param number ��ת���Ĵ�����
	 * @return byte []
	 * */
	public static byte [] get2048bit(BigInteger number) {
		byte bit [] = new byte [0x800];
		BigInteger n = number;
		
		/*������С��0������-1��ת��Ϊ����*/
		if(n.compareTo(BigInteger.ZERO) < 0) {
			number = number.multiply(negativeOne);
		}
		for(int i = bit.length - 1; i >= 0; i--) {
			if(number.equals(BigInteger.ZERO) == false) {
				bit [i] = (byte) (number.mod(two) == BigInteger.ZERO ? 0 : 1);
				number = number.divide(two);
			}
		}
		/*��Ϊ���������������λΪ1*/
		if(n.compareTo(BigInteger.ZERO) < 0) {
			bit [0] = 1;
		}
		return bit;
	}
}