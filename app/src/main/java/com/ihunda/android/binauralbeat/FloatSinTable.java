package com.ihunda.android.binauralbeat;

/**
 * Fast smaller precision sin
 */

public class FloatSinTable
{
	static float[] tableSin=null;
	static float[] tableCos=null;
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
		tableSin = new float[size];
		step = (float) (2d * Math.PI / size);
		invStep = 1.0f / step;
		for (int i=0; i<size;i++) {
			tableSin[i] = (float)Math.sin(step*((float)i));
		}
		
		tableCos = new float[size];
		for (int i=0; i<size;i++) {
			tableCos[i] = (float)Math.cos(step*((float)i));
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
		return tableSin[index];
	}
	
	/**
	 * calculates fast sin, but with low precision.
	 * @param a angle already converted to integer
	 * @return sin of angle a
	 */
	final public float sinFastInt(int a)
	{
		return tableSin[a % size];
	}
	
	/**
	 * calculates fast cos, but with low precision.
	 * @param a angle in radians
	 * @return cos of angle a
	 */
	final public float cosFast(float a)
	{
		/* we need speed !

if (a == Double.NaN) return Double.NaN;
if (a == 0.0d) return 0.0d;
		 */

		int index = (int)(a/step);
		index = index % size;
		return tableCos[index];
	}
	
	/**
	 * calculates fast cos, but with low precision.
	 * @param a angle already converted to integer
	 * @return cos of angle a
	 */
	final public float cosFastInt(int a)
	{
		return tableCos[a % size];
	}
}
