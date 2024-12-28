/*    */ package mods.helpfulvillagers.main;
/*    */ 
/*    */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*    */ import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommonProxy
/*    */   implements IGuiHandler
/*    */ {
/*    */   public void registerRenderers() {}
/*    */   
/*    */   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
/* 36 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void Init() {
/* 43 */     MinecraftForge.EVENT_BUS.register(new CommonHooks());
/* 44 */     FMLCommonHandler.instance().bus().register(new CommonHooks());
/*    */   }

		  // public void registerGuiHandler() {
			   //NetworkRegistry.INSTANCE.registerGuiHandler(HelpfulVillagers.instance, new GuiHandler());
		   //}

/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\main\CommonProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */