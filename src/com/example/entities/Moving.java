package com.example.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import com.example.entities.animations.Render;
import com.example.entities.collisions.Bounds;
import com.example.libs.Reference;
import com.example.libs.ReferenceRender;
import com.example.libs.Vector2D;

/**
 * A Moving entity that has models real world physics using velocity and
 * acceleration, resulting in smooth motion.
 * 
 * @author poroia
 *
 */
public class Moving extends Entity
{
	//
	// FIELDS
	//

	/**
	 * A vector representing this Entity's velocity.
	 */
	private Vector2D	velocity;

	/**
	 * A vector representing this Entity's acceleration.
	 */
	private Vector2D	acceleration;

	/**
	 * The target velocity in floating-point precision. It is used to cap the
	 * velocity of this Player to prevent it from going too fast.
	 */
	private float		targetSpd;

	/**
	 * The self acceleration capabilities upon movement in floating-point precision.
	 */
	private float		thrust;

	//
	// CONSTRUCTORS
	//

	/**
	 * Constructs an Moving object that can move.
	 * 
	 * @param position
	 *            The vector position of this Entity.
	 * @param width
	 *            The width of this Entity.
	 * @param height
	 *            The height of this Entity.
	 * @param targetSpd
	 *            The target/capped speed of this Entity.
	 * @param thrust
	 *            The thrust capabilities.
	 * @param mainRender
	 *            The main image to be rendered.
	 */
	public Moving(Vector2D position, float width, float height, float targetSpd, float thrust, Render mainRender, ID id)
	{
		super(position, width, height, mainRender, id);
		velocity = new Vector2D();
		acceleration = new Vector2D();
		setTargetSpd(targetSpd);
		setThrust(thrust);
	}

	//
	// GAMELOOP METHODS
	//

	/**
	 * Updates the motion of Moving and the mainRender's rotation
	 */
	@Override
	public void update()
	{
		super.update();

		calcAcceleration();
		setVelocity(Vector2D.add(getVelocity(), getAcceleration()));

		calcVelocity();
		setPosition(Vector2D.add(getPosition(), getVelocity()));
	}

	/**
	 * Renders the mainRender's image and interpolates.
	 */
	@Override
	public void render(Graphics2D g, float interpolation)
	{
		int x = ReferenceRender.getInterpolatedX(getPosition(), velocity, interpolation);
		int y = ReferenceRender.getInterpolatedY(getPosition(), velocity, interpolation);
		getMainRender().render(g, x, y);

		if (Reference.DEBUG) renderBounds(g, interpolation);
	}

	//
	// GENERAL METHODS
	//

	/**
	 * Returns the Bounds represented by a rectangle.
	 * 
	 * @return rect
	 */
	public Bounds getRect()
	{
		return new Bounds(getPosition(), getWidth(), getHeight());
	}

	/**
	 * Returns the Bounds represented by a circle.
	 * 
	 * @return circ
	 */
	public Bounds getCirc()
	{
		return new Bounds(getPosition(), getWidth());
	}

	/**
	 * Returns the Bounds represented by many circles specified. Should be overriden
	 * for complex functionality.
	 * 
	 * @return complex
	 */
	public Bounds[] getComplex()
	{
		Bounds[] complex = new Bounds[1];
		complex[0] = getCirc();
		return complex;
	}

	//
	// HELPER METHODS
	//

	/**
	 * Calculates the acceleration of this Moving entity.
	 */
	protected void calcAcceleration()
	{
		float aLength = getAcceleration().getExactLength();
		if (aLength > 1) { // limit to circular movement
			getAcceleration().quickNormalize(aLength);
		}
		setAcceleration(Vector2D.getScaled(getAcceleration(), getThrust()));
	}

	/**
	 * Calculates the velocity of this Moving entity.
	 */
	protected void calcVelocity()
	{}

	/**
	 * FOR DEBUG PURPOSES ONLY.
	 * 
	 * @param g
	 *            Graphics2D
	 * @param interpolation
	 *            Interpolation for smoother rendering.
	 */
	public void renderBounds(Graphics2D g, float interpolation)
	{
		renderRect(g, interpolation);
		renderCirc(g, interpolation);
		renderComplex(g, interpolation);
	}

	/**
	 * FOR DEBUG PURPOSES ONLY.
	 * 
	 * @param g
	 *            Graphics2D
	 * @param interpolation
	 *            Interpolation for smoother rendering.
	 */
	public void renderRect(Graphics2D g, float interpolation)
	{
		g.setColor(new Color(128, 128, 128)); // rectBounds
		ReferenceRender.drawInterpolatedRect(g, getRect(), velocity, interpolation);
		ReferenceRender.drawInterpolatedString(g, "rectBounds", getRect(), velocity, interpolation);
	}

	/**
	 * FOR DEBUG PURPOSES ONLY.
	 * 
	 * @param g
	 *            Graphics2D
	 * @param interpolation
	 *            Interpolation for smoother rendering.
	 */
	public void renderCirc(Graphics2D g, float interpolation)
	{
		g.setColor(new Color(192, 192, 192)); // circBounds
		ReferenceRender.drawInterpolatedCirc(g, getCirc(), velocity, interpolation);
		ReferenceRender.drawInterpolatedString(g, "circBounds", getCirc(), velocity, interpolation);
	}

	/**
	 * FOR DEBUG PURPOSES ONLY.
	 * 
	 * @param g
	 *            Graphics2D
	 * @param interpolation
	 *            Interpolation for smoother rendering.
	 */
	public void renderComplex(Graphics2D g, float interpolation)
	{
		g.setColor(new Color(255, 255, 255));
		g.setStroke(new BasicStroke(2));
		ReferenceRender.drawInterpolatedComplex(g, getComplex(), velocity, interpolation);
	}

	//
	// SETTER AND GETTER METHODS
	//

	/**
	 * Sets the velocity vector.
	 * 
	 * @param velocity
	 *            The velocity vector
	 */
	public void setVelocity(Vector2D velocity)
	{
		this.velocity = velocity;
	}

	/**
	 * Sets the acceleration vector.
	 * 
	 * @param acceleration
	 *            The acceleration vector
	 */
	public void setAcceleration(Vector2D acceleration)
	{
		this.acceleration = acceleration;
	}

	/**
	 * Sets the target speed.
	 * 
	 * @param targetSpd
	 *            The target or capped speed.
	 */
	public void setTargetSpd(float targetSpd)
	{
		this.targetSpd = targetSpd;
	}

	/**
	 * Sets the thrust.
	 * 
	 * @param thrust
	 *            The thrust capabilities.
	 */
	public void setThrust(float thrust)
	{
		this.thrust = thrust;
	}

	/**
	 * Returns the velocity vector.
	 * 
	 * @return velocity
	 */
	public Vector2D getVelocity()
	{
		return velocity;
	}

	/**
	 * Returns the acceleration vector.
	 * 
	 * @return acceleration
	 */
	public Vector2D getAcceleration()
	{
		return acceleration;
	}

	/**
	 * Returns the target speed.
	 * 
	 * @return targetSpd
	 */
	public float getTargetSpd()
	{
		return targetSpd;
	}

	/**
	 * Returns the thrust capabilities.
	 * 
	 * @return thrust
	 */
	public float getThrust()
	{
		return thrust;
	}
}
