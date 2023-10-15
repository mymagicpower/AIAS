package me.aias.example.utils;

public class IntegerConversion {
	public static int convertTwoBytesToInt1(byte b1, byte b2) // signed
	{
		return (b2 << 8) | (b1 & 0xFF);
	}

	public static int convertFourBytesToInt1(byte b1, byte b2, byte b3, byte b4) {
		return (b4 << 24) | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
	}

	public static int convertTwoBytesToInt2(byte b1, byte b2) // unsigned
	{
		return (b2 & 0xFF) << 8 | (b1 & 0xFF);
	}

	public static long convertFourBytesToInt2(byte b1, byte b2, byte b3, byte b4) {
		return (long) (b4 & 0xFF) << 24 | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
	}

	public static void main(String[] args) {
		byte b1 = (byte) 0xfe;
		byte b2 = (byte) 0xff;
		byte b3 = (byte) 0xFF;
		byte b4 = (byte) 0xFF;
		float s = (float) (convertTwoBytesToInt1(b1, b2) * ( (float)1.0 / Float.valueOf(1 << ((8 * 2) - 1))));
		System.out.print(s);
		//System.out.printf("%,14d%n", convertTwoBytesToInt2(b1, b2));

		//System.out.printf("%,14d%n", convertFourBytesToInt1(b1, b2, b3, b4));
		//System.out.printf("%,14d%n", convertFourBytesToInt2(b1, b2, b3, b4));
	}
}