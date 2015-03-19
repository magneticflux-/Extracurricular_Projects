package com.electronauts.virtualrobot;

public class Motor
{
	private final MotorData	motorData;
	private double			x, y, rpm, radius;
	private Wheel			wheel;

	public Motor(final MotorData motorData, final Wheel wheel, final double x, final double y)
	{
		this.motorData = motorData;
		this.wheel = wheel;
		this.x = x;
		this.y = y;
		this.rpm = 0;
	}

	public Motor(final MotorData motorData, final Wheel wheel, final double rpm, final double x, final double y)
	{
		this.motorData = motorData;
		this.wheel = wheel;
		this.x = x;
		this.y = y;
		this.rpm = rpm;
	}

	public MotorData getMotorData()
	{
		return this.motorData;
	}

	public double getRadius()
	{
		return this.radius;
	}

	public double getRPM()
	{
		return this.rpm;
	}

	public double getVelocity()
	{
		return this.getRPM() * this.getWheel().getCircumference();
	}

	public Wheel getWheel()
	{
		return this.wheel;
	}

	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}

	public void setRadius(final double radius)
	{
		this.radius = radius;
	}

	public void setRPM(final double rpm)
	{
		this.rpm = rpm;
	}

	public void setWheel(final Wheel wheel)
	{
		this.wheel = wheel;
	}

	public void setX(final double x)
	{
		this.x = x;
	}

	public void setY(final double y)
	{
		this.y = y;
	}
}
