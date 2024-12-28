/*     */ package mods.helpfulvillagers.renderer;
/*     */ 
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.ModelBiped;
/*     */ import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.ResourceLocation;

/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ public class RenderVillagerCustom
/*     */   extends RenderBiped
/*     */ {
/*  61 */   private static final ResourceLocation villagerTextures = new ResourceLocation("minecraft:textures/entity/villager/villager.png");
/*     */   
/*  63 */   private static final ResourceLocation villagerTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/villager.png");
/*  64 */   private static final ResourceLocation lumberjackTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/lumberjack.png");
/*  65 */   private static final ResourceLocation minerTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/miner.png");
/*  66 */   private static final ResourceLocation farmerTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/farmer.png");
/*  67 */   private static final ResourceLocation soldierTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/soldier.png");
/*  68 */   private static final ResourceLocation archerTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/archer.png");
/*  69 */   private static final ResourceLocation merchantTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/merchant.png");
/*  70 */   private static final ResourceLocation fishermanTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/fisherman.png");
/*  71 */   private static final ResourceLocation rancherTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/rancher.png");
/*  72 */   private static final ResourceLocation builderTexturesCustom = new ResourceLocation("helpfulvillagers", "textures/entity/villager/builder.png");

			private ModelBiped model = new ModelBiped(0.0F);
			private float scale = 0.5F;
			private RenderManager manager = Minecraft.getMinecraft().getRenderManager();
/*     */   
/*     */   public RenderVillagerCustom(RenderManager manager, ModelBiped model, float scale) {
/*  75 */     super(manager, model, scale);
/*     */   }

			protected void preRenderCallBack(AbstractVillager entitylivingbaseIn, float partialTickTime) {
				this.preRenderCallBack(entitylivingbaseIn, partialTickTime);
			}

/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(Entity entity) {
/*  80 */     return textureChanger(((AbstractVillager)entity).getProfession());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourceLocation textureChanger(int profession) {
/*  90 */     switch (profession) {
/*     */       
/*     */       case 0:
/*  93 */         return villagerTexturesCustom;
/*     */       
/*     */       case 1:
/*  96 */         return lumberjackTexturesCustom;
/*     */       
/*     */       case 2:
/*  99 */         return minerTexturesCustom;
/*     */       
/*     */       case 3:
/* 102 */         return farmerTexturesCustom;
/*     */       
/*     */       case 4:
/* 105 */         return soldierTexturesCustom;
/*     */       
/*     */       case 5:
/* 108 */         return archerTexturesCustom;
/*     */       
/*     */       case 6:
/* 111 */         return merchantTexturesCustom;
/*     */       
/*     */       case 7:
/* 114 */         return fishermanTexturesCustom;
/*     */       
/*     */       case 8:
/* 117 */         return rancherTexturesCustom;
/*     */       
/*     */       case 9:
/* 120 */         return builderTexturesCustom;
/*     */     } 
/* 122 */     return villagerTexturesCustom;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\renderer\RenderVillagerCustom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */