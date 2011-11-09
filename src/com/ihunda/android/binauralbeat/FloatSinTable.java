package com.ihunda.android.binauralbeat;

/**
 * Fast smaller precision sin
 */

public class FloatSinTable
{
	static float[] table=null;
	static float step;
	static float invStep;
	static int size=0;

	public FloatSinTable()
	{
		this(720);
	}

	public FloatSinTable(int n)
	{
		if (size==0)
		{
			build(n);
		}
	}

	/**
	 * build sin table with size elements
	 * @param size
	 */
	final private void build(int pSize)
	{
		size=pSize;
		table = new float[size];
		step = (float) (2d * Math.PI / size);
		invStep = 1.0f / step;
		for (int i=0; i<size;i++) {
			table[i] = (float)Math.sin(step*((float)i));
		}
	}

	/**
	 * calculates fast sin, but with low precision.
	 * @param a angle in radians
	 * @return sin of angle a
	 */
	final public float sinFast(float a)
	{
		/* we need speed !

if (a == Double.NaN) return Double.NaN;
if (a == 0.0d) return 0.0d;
		 */

		int index = (int)(a/step);
		index = index % size;
		return table[index];
	}
	
	/**
	 * calculates fast sin, but with low precision.
	 * @param a angle already converted to integer
	 * @return sin of angle a
	 */
	final public float sinFastInt(int a)
	{
		return table[a % size];
	}
}
