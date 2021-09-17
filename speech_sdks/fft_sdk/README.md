## 快速傅里叶变换(FFT)的java实现
快速傅里叶变换 (fast Fourier transform), 即利用计算机计算离散傅里叶变换（DFT)的高效、快速计算方法的统称，简称FFT。
快速傅里叶变换是1965年由J.W.库利和T.W.图基提出的。采用这种算法能使计算机计算离散傅里叶变换所需要的乘法次数大为减少，
特别是被变换的抽样点数N越多，FFT算法计算量的节省就越显著。
计算量小的显著的优点，使得FFT在信号处理技术领域获得了广泛应用，结合高速硬件就能实现对信号的实时处理。
例如，对语音信号的分析和合成，对通信系统中实现全数字化的时分制与频分制(TDM/FDM)的复用转换，在频域对信号滤波以及相关分析，
通过对雷达、声纳、振动信号的频谱分析以提高对目标的搜索和跟踪的分辨率等等，都要用到FFT。


## 运行例子 - FFTExample

核心算法：
```text
	/**
	 * Performs Fast Fourier Transformation in place.
	 */
	public void process(double[] signal) {
		final int numPoints = signal.length;
		// initialize real & imag array
		real = signal;
    	imag = new double[numPoints];

		// perform FFT using the real & imag array
		final double pi = Math.PI;
		final int numStages = (int) (Math.log(numPoints) / Math.log(2));
		final int halfNumPoints = numPoints >> 1;
		int j = halfNumPoints;
		// FFT time domain decomposition carried out by "bit reversal sorting"
		// algorithm
		int k;
		for (int i = 1; i < numPoints - 2; i++) {
			if (i < j) {
				// swap
				double tempReal = real[j];
				double tempImag = imag[j];
				real[j] = real[i];
				imag[j] = imag[i];
				real[i] = tempReal;
				imag[i] = tempImag;
			}
			k = halfNumPoints;
			while (k <= j) {
				j -= k;
				k >>= 1;
			}
			j += k;
		}

		// loop for each stage
		for (int stage = 1; stage <= numStages; stage++) {
			int LE = 1;
			for (int i = 0; i < stage; i++) {
				LE <<= 1;
			}
            final int LE2 = LE >> 1;
			double UR = 1;
			double UI = 0;
			// calculate sine & cosine values
			final double SR =  Math.cos(pi / LE2);
            final double SI = -Math.sin(pi / LE2);
			// loop for each sub DFT
			for (int subDFT = 1; subDFT <= LE2; subDFT++) {
				// loop for each butterfly
				for (int butterfly = subDFT - 1; butterfly <= numPoints - 1; butterfly += LE) {
					int ip = butterfly + LE2;
					// butterfly calculation
					double tempReal = (double) (real[ip] * UR - imag[ip] * UI);
					double tempImag = (double) (real[ip] * UI + imag[ip] * UR);
					real[ip] = real[butterfly] - tempReal;
					imag[ip] = imag[butterfly] - tempImag;
					real[butterfly] += tempReal;
					imag[butterfly] += tempImag;
				}

				double tempUR = UR;
				UR = tempUR * SR - UI * SI;
				UI = tempUR * SI + UI * SR;
			}
		}
	}

````

运行成功后，命令行应该看到下面的信息:
```text
# 复数的实部
Real parts: [0.002560000019002473, 8.446425149266073E-5, ..., 8.446425149266079E-5]
# 复数的虚部
Imaginary parts: [0.0, 8.056758864562464E-4, 4.0390382669844993E-4, ..., -8.056758864562461E-4]

```
