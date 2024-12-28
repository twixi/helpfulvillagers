/*    */ package mods.helpfulvillagers.main;
/*    */ 
/*    */ import net.minecraftforge.fml.client.registry.RenderingRegistry;
/*    */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
/*    */ import mods.helpfulvillagers.entity.EntityFishHookCustom;
/*    */ import mods.helpfulvillagers.renderer.RenderFishHookCustom;
/*    */ import mods.helpfulvillagers.renderer.RenderVillagerCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
/*    */ //import net.minecraft.client.renderer.entity.Render;
		import net.minecraft.client.resources.model.ModelResourceLocation;
		import net.minecraftforge.common.MinecraftForge;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientProxy
/*    */   extends CommonProxy
		   
/*    */ {
			
/*    */   public void registerRenderers() {
			RenderManager manager = Minecraft.getMinecraft().getRenderManager();
			
/* 39 */     RenderingRegistry.registerEntityRenderingHandler(AbstractVillager.class, (Render)new RenderVillagerCustom(Minecraft.getMinecraft().getRenderManager(),new ModelBiped(0.0F), 0.5F));
/* 40 */     //RenderingRegistry.registerEntityRenderingHandler(EntityFishHookCustom.class, (Render)new RenderFishHookCustom(null));
			//RenderingRegistry.registerEntityRenderingHandler(AbstractVillager.class, new MyFactory());
/*    */   }


/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void Init() {
			super.Init();
/* 48 */     MinecraftForge.EVENT_BUS.register(new ClientHooks());
/* 49 */     FMLCommonHandler.instance().bus().register(new ClientHooks());
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\main\ClientProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */