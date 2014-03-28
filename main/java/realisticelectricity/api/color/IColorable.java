package realisticelectricity.api.color;

/**
 *  A colorable wire will implement this interface.
 *  If your wire is not colorable, simply have the setColor
 *  method do nothing.
 *  
 * @author darkfusion58
 *
 */
public interface IColorable {
	/**
	 *  
	 * @param The color to set the wire to.
	 *
	 */
	public abstract void setColor(byte red, byte green, byte blue);
}
