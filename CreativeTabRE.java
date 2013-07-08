package realisticelectricity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabRE extends CreativeTabs
{
	public CreativeTabRE() {
		super("tabRealisticElectricity");
	}
	
	@SideOnly(Side.CLIENT)
    public String getTabLabel(){
        return "Realistic Electricity";
    }

	@SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel(){
        return this.getTabLabel();
    }
}
