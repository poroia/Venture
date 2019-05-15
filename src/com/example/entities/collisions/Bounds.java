package com.example.entities.collisions;

/**
 * The bounds used for collision detection.
 * 
 * @author poroia
 */
public class Bounds
{
	/**
	 * The different types of Bounds
	 */
	public static enum Type
	{
		CIRC,
		RECT
	}

	/**
	 * The x-coordinate of this Bounds.
	 */
	private float	x;

	/**
	 * The y-coordinate of this Bounds.
	 */
	private float	y;

	/**
	 * The width of this Bounds.
	 */
	private float	width;

	/**
	 * The height of this Bounds.
	 */
	private float	height;

	/**
	 * The bounds type specified in this class's enum Type.
	 */
	private Type	type;

	/**
	 * Constructs a Bound with a specified type.
	 * 
	 * @param type
	 *            The bounds type
	 */
	public Bounds(Type type)
	{
		this.type = type;
	}

	/**
	 * Constructs a CIRC type Bounds.
	 * 
	 * @param x
	 *            The x-coordinates
	 * @param y
	 *            The y-coordinates
	 * @param size
	 *            The size
	 */
	public Bounds(float x, float y, float size)
	{
		setCirc(x, y, size);
		this.type = Type.CIRC;
	}

	/**
	 * Constructs a RECT type Bounds.
	 * 
	 * @param x
	 *            The x-coordinates
	 * @param y
	 *            The y-coordinates
	 * @param width
	 *            The width
	 * @param height
	 *            The height
	 */
	public Bounds(float x, float y, float width, float height)
	{
		setRect(x, y, width, height);
		this.type = Type.RECT;
	}

	/**
	 * Determine if an intersection occurs between two bounds. Able to account for
	 * different bound types.
	 * 
	 * @param other
	 *            The other bounds used in the calculation.
	 * @return if intersects
	 */
	public boolean intersects(Bounds other)
	{
		if (checkType(this, Type.CIRC)) {

			if (checkType(other, Type.CIRC)) {
				return circIntersectsCirc(other);
			}

			else if (checkType(other, Type.RECT)) {
				return circIntersectsRect(other);
			}
		}

		else if (checkType(this, Type.RECT)) {

			if (checkType(other, Type.RECT)) {
				return rectIntersectsRect(other);
			}

			else if (checkType(other, Type.CIRC)) {
				return rectIntersectsCirc(other);
			}
		}

		return false;
	}

	/**
	 * Determine if an intersection occurs between two circs.
	 * 
	 * @param other
	 *            The other Bounds (assumed to be a circ)
	 * @return if intersects
	 */
	private boolean circIntersectsCirc(Bounds circ)
	{
		float distX = (this.getX() + this.getRadius()) - (circ.getX() + circ.getRadius());
		float distY = (this.getY() + this.getRadius()) - (circ.getY() + circ.getRadius());
		float distRad = this.getRadius() + circ.getRadius();
		return distX * distX + distY * distY <= distRad * distRad;
	}

	/**
	 * Determine if an intersection occurs between two rects.
	 * 
	 * @param other
	 *            The other Bounds (assumed to be a rect)
	 * @return if intersects
	 */
	private boolean rectIntersectsRect(Bounds rect)
	{
		return this.getX() < rect.getX() + rect.getWidth() && this.getX() + this.getWidth() > rect.getX()
				&& this.getY() < rect.getY() + rect.getHeight() && this.getY() + this.getHeight() > rect.getY();
	}

	/**
	 * Determine if an intersection occurs between this circ and a rect.
	 * 
	 * @param other
	 *            The other rect (assumed)
	 * @return if intersects
	 */
	private boolean circIntersectsRect(Bounds rect)
	{
		float closestX = clip(this.getCenterX(), rect.getX(), rect.getX() + rect.getWidth());
		float closestY = clip(this.getCenterY(), rect.getY(), rect.getY() + rect.getHeight());

		float distX = this.getCenterX() - closestX;
		float distY = this.getCenterY() - closestY;
		float distRad = this.getRadius();
		return distX * distX + distY * distY <= distRad * distRad;
	}

	/**
	 * Determine if an intersection occurs between this rect and a circ.
	 * 
	 * @param other
	 *            The other circ (assumed)
	 * @return if intersects
	 */
	private boolean rectIntersectsCirc(Bounds circ)
	{
		float closestX = clip(circ.getCenterX(), this.getX(), this.getX() + this.getWidth());
		float closestY = clip(circ.getCenterY(), this.getY(), this.getY() + this.getHeight());

		float distX = circ.getCenterX() - closestX;
		float distY = circ.getCenterY() - closestY;
		float distRad = circ.getRadius();
		return distX * distX + distY * distY <= distRad * distRad;
	}

	/**
	 * Used to force a value within a certain range by clipping it when it is too
	 * high or low.
	 * 
	 * @param value
	 *            The input value
	 * @param min
	 *            The minimum wanted
	 * @param max
	 *            The maximum wanted
	 * @return clippedValue
	 */
	private float clip(float value, float min, float max)
	{
		if (value < min) return min;
		if (value > max) return max;
		return value;
	}

	/**
	 * Checks if the bounds is a certain specified type.
	 * 
	 * @param bounds
	 *            The bounds to be checked
	 * @param type
	 *            The type for comparison
	 * @return if same type
	 */
	public boolean checkType(Bounds bounds, Type type)
	{
		return bounds.getType() == type;
	}

	//
	// SETTER AND GETTER METHODS
	//

	/**
	 * Set Bounds for CIRC.
	 *
	 * @param x
	 *            The x-coordinates
	 * @param y
	 *            The y-coordinates
	 * @param size
	 *            The size
	 */
	public void setCirc(float x, float y, float size)
	{
		setX(x);
		setY(y);
		setSize(size);
	}

	/**
	 * Set Bounds for RECT.
	 * 
	 * @param x
	 *            The x-coordinates
	 * @param y
	 *            The y-coordinates
	 * @param width
	 *            The width
	 * @param height
	 *            The height
	 */
	public void setRect(float x, float y, float width, float height)
	{
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	/**
	 * Sets the x-coordinates.
	 * 
	 * @param x
	 *            The x-coordinates
	 */
	public void setX(float x)
	{
		this.x = x;
	}

	/**
	 * Sets the y-coordinates.
	 * 
	 * @param y
	 *            The y-coordinates
	 */
	public void setY(float y)
	{
		this.y = y;
	}

	/**
	 * Sets the size of this Bounds.
	 * 
	 * @param size
	 *            The size
	 */
	public void setSize(float size)
	{
		setWidth(size);
		setHeight(size);
	}

	/**
	 * Sets the width.
	 * 
	 * @param width
	 *            The width
	 */
	public void setWidth(float width)
	{
		this.width = width;
	}

	/**
	 * Sets the height.
	 * 
	 * @param height
	 *            The height
	 */
	public void setHeight(float height)
	{
		this.height = height;
	}

	/**
	 * Returns the x-coordinate.
	 * 
	 * @return x
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * Returns the x-coordinate for the center.
	 * 
	 * @return centerX
	 */
	public float getCenterX()
	{
		return x + width / 2;
	}

	/**
	 * Returns the y-coordinate.
	 * 
	 * @return y
	 */
	public float getY()
	{
		return y;
	}

	/**
	 * Returns the y-coordinate for the center.
	 * 
	 * @return centerY
	 */
	public float getCenterY()
	{
		return y + height / 2;
	}

	/**
	 * Returns the width.
	 * 
	 * @return width
	 */
	public float getWidth()
	{
		return width;
	}

	/**
	 * Returns the height.
	 * 
	 * @return height
	 */
	public float getHeight()
	{
		return height;
	}

	/**
	 * Returns the radius, assuming width is equal to height and the type is a
	 * circle.
	 * 
	 * @return
	 */
	public float getRadius()
	{
		return width / 2;
	}

	/**
	 * Returns the bounds type.
	 * 
	 * @return type
	 */
	public Type getType()
	{
		return type;
	}
}