## Java Implementation of Fast Fourier Transform (FFT)

Fast Fourier Transform (FFT) is a term used to describe the efficient and fast computation method of discrete Fourier transform (DFT) by utilizing a computer. The algorithm was proposed by J.W. Cooley and T.W. Tukey in 1965. The adoption of this algorithm reduces the number of multiplication operations required for a computer to calculate DFT significantly. This is particularly significant when the number of sampled points N increases, as the savings in computational resources are more pronounced.

The significant advantage of FFT in terms of reduced computational requirements has made it widely used in the field of signal processing. Coupled with high-speed hardware, it can be used to achieve real-time signal processing. FFT can be used for various applications such as analyzing and synthesizing voice signals, achieving full digitalization of time division multiplexing (TDM)/frequency division multiplexing (FDM) in communication systems, filtering signals in the frequency domain, and analyzing the spectrum of radar, sonar, and vibration signals to improve the resolution of target search and tracking.

## Running Example - FFTExample
```text
...
	    String audioFilePath = "src/test/resources/audio.wav";
        //-1 value implies the method to use default sample rate
        int defaultSampleRate = -1;
        //-1 value implies the method to process complete audio duration
        int defaultAudioDuration = -1;

        JLibrosa jLibrosa = new JLibrosa();

        // To read the magnitude values of audio files - equivalent to librosa.load('../audio.wav', sr=None)
        float audioFeatureValues[] = jLibrosa.loadAndRead(audioFilePath, defaultSampleRate, defaultAudioDuration);

        double[] arr = IntStream.range(0, audioFeatureValues.length).mapToDouble(i -> audioFeatureValues[i]).toArray();

        double[] fft = FFT.fft(arr);
        float[][] complex = FFT.rfft(fft);

        System.out.println("Real parts: " + Arrays.toString(complex[0]));
        System.out.println("Imaginary parts: " + Arrays.toString(complex[1]));
...
````

After running the example successfully, the command line should display the following information:
```text
#Real parts of complex numbers
Real parts: [-1.8461201, -1.1128254, 0.58502156, 2.6774616, -1.7226994, ..., 0.15794027]
#Imaginary parts of complex numbers
Imaginary parts: [0.0, 1.2845019, 2.8104274, -1.3958083, 1.2868061, ..., -0.3447435, 0.0]

```
