/**
 * author: 韩山师范学院 2017115132 肖泽锴<br>
 * lastUpdate-Time: May 29th, 2019<br>
 * function:<br>
 * RSA是基于大素数的素性分解问题的难解性的一种公钥密码算法。<br>
 * RSA算法过程：<br>
 * 1.选取两个大素数p和q，p和q保密。<br>
 * 2.计算n=p*q，fn=(p-1)*(q - 1)，n公开，fn保密。<br>
 * 3.随机选取满足大于1小于fn的整数e，满足gcd(e,fn)=1。e是公开的加密密钥。<br>
 * 4.计算d，满足d*e=1(mod fn)，d是保密的解密密。<br>
 * 5.加密变换：对明文m∈Zn，密文为 c = m ^ e(mod n)。<br>
 * 6.解密变换：对密文c∈Zn，明文为 m = c ^ d(mod n)。<br>
 * 本包是我自己根据对RSA算法的理解编写的程序。<br>
 * @version <p>RSA密码器 1.0.0<p><br>
 */
package RSA;