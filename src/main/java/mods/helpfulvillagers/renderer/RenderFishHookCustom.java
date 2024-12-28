/*     */ package mods.helpfulvillagers.renderer;
/*     */ 
/*     */ import net.minecraftforge.fml.relauncher.Side;
		  import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import mods.helpfulvillagers.entity.EntityFishHookCustom;
import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
		  import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.entity.Render;
		  import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ public class RenderFishHookCustom
/*     */   extends Render
/*     */ {
			public RenderFishHookCustom (RenderManager manager) {
				super(manager);
			}
/*  21 */   private static final ResourceLocation field_110792_a = new ResourceLocation("textures/particle/particles.png");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String __OBFID = "CL_00000996";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doRender(EntityFishHookCustom p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
/*  32 */     GL11.glPushMatrix();
/*  33 */     GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
/*  34 */     GL11.glEnable(32826);
/*  35 */     GL11.glScalef(0.5F, 0.5F, 0.5F);
/*  36 */     bindEntityTexture((Entity)p_76986_1_);
/*  37 */     Tessellator tessellator = Tessellator.getInstance();
				WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  38 */     byte b0 = 1;
/*  39 */     byte b1 = 2;
/*  40 */     float f2 = (b0 * 8 + 0) / 128.0F;
/*  41 */     float f3 = (b0 * 8 + 8) / 128.0F;
/*  42 */     float f4 = (b1 * 8 + 0) / 128.0F;
/*  43 */     float f5 = (b1 * 8 + 8) / 128.0F;
/*  44 */     float f6 = 1.0F;
/*  45 */     float f7 = 0.5F;
/*  46 */     float f8 = 0.5F;
/*  47 */     GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/*  48 */     GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

			  worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

/*  50 */     worldrenderer.putNormal(0.0F, 1.0F, 0.0F);
/*  51 */     worldrenderer.pos((0.0F - f7), (0.0F - f8), 0.0D).tex(f2, f5).endVertex();
/*  52 */     worldrenderer.pos((f6 - f7), (0.0F - f8), 0.0D).tex(f3, f5).endVertex();
/*  53 */     worldrenderer.pos((f6 - f7), (1.0F - f8), 0.0D).tex(f3, f4).endVertex();
/*  54 */     worldrenderer.pos((0.0F - f7), (1.0F - f8), 0.0D).tex(f2, f4).endVertex();
/*  55 */     tessellator.draw();
/*  56 */     GL11.glDisable(32826);
/*  57 */     GL11.glPopMatrix();
/*     */     
/*  59 */     if (p_76986_1_.fisherman != null) {
/*     */       
/*  61 */       float f9 = p_76986_1_.fisherman.getSwingProgress(p_76986_9_);
/*  62 */       float f10 = MathHelper.sin(MathHelper.sqrt_float(f9) * 3.1415927F);
/*  63 */       Vec3 vec3 = new Vec3(-0.5D, 0.03D, 0.8D);
/*  64 */      	vec3.rotatePitch(-(p_76986_1_.fisherman.prevRotationPitch + (p_76986_1_.fisherman.rotationPitch - p_76986_1_.fisherman.prevRotationPitch) * p_76986_9_) * 3.1415927F / 180.0F);
/*  65 */       vec3.rotateYaw(-(p_76986_1_.fisherman.prevRotationYaw + (p_76986_1_.fisherman.rotationYaw - p_76986_1_.fisherman.prevRotationYaw) * p_76986_9_) * 3.1415927F / 180.0F);
/*  66 */       vec3.rotateYaw(f10 * 0.5F);
/*  67 */       vec3.rotatePitch(-f10 * 0.7F);
/*  68 */       double d3 = p_76986_1_.fisherman.prevPosX + (p_76986_1_.fisherman.posX - p_76986_1_.fisherman.prevPosX) * p_76986_9_ + vec3.xCoord;
/*  69 */       double d4 = p_76986_1_.fisherman.prevPosY + (p_76986_1_.fisherman.posY - p_76986_1_.fisherman.prevPosY) * p_76986_9_ + vec3.yCoord;
/*  70 */       double d5 = p_76986_1_.fisherman.prevPosZ + (p_76986_1_.fisherman.posZ - p_76986_1_.fisherman.prevPosZ) * p_76986_9_ + vec3.zCoord;
/*  71 */       double d6 = 1.5299999713897705D;
/*     */       
/*  73 */       float f11 = (p_76986_1_.fisherman.prevRenderYawOffset + (p_76986_1_.fisherman.renderYawOffset - p_76986_1_.fisherman.prevRenderYawOffset) * p_76986_9_) * 3.1415927F / 180.0F;
/*  74 */       double d7 = MathHelper.sin(f11);
/*  75 */       double d9 = MathHelper.cos(f11);
/*  76 */       d3 = p_76986_1_.fisherman.prevPosX + (p_76986_1_.fisherman.posX - p_76986_1_.fisherman.prevPosX) * p_76986_9_ - d9 * 0.35D - d7 * 0.85D;
/*  77 */       d4 = p_76986_1_.fisherman.prevPosY + d6 + (p_76986_1_.fisherman.posY - p_76986_1_.fisherman.prevPosY) * p_76986_9_ - 0.45D;
/*  78 */       d5 = p_76986_1_.fisherman.prevPosZ + (p_76986_1_.fisherman.posZ - p_76986_1_.fisherman.prevPosZ) * p_76986_9_ - d7 * 0.35D + d9 * 0.85D;
/*     */       
/*  80 */       double d14 = p_76986_1_.prevPosX + (p_76986_1_.posX - p_76986_1_.prevPosX) * p_76986_9_;
/*  81 */       double d8 = p_76986_1_.prevPosY + (p_76986_1_.posY - p_76986_1_.prevPosY) * p_76986_9_ + 0.25D;
/*  82 */       double d10 = p_76986_1_.prevPosZ + (p_76986_1_.posZ - p_76986_1_.prevPosZ) * p_76986_9_;
/*  83 */       double d11 = (float)(d3 - d14);
/*  84 */       double d12 = (float)(d4 - d8);
/*  85 */       double d13 = (float)(d5 - d10);
/*  86 */       GL11.glDisable(3553);
/*  87 */       GL11.glDisable(2896);
/*  88 */       worldrenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
/*  89 */       //tessellator.setColorOpaque_I(0);
/*  90 */       byte b2 = 16;
/*     */       
/*  92 */       for (int i = 0; i <= b2; i++) {
/*     */         
/*  94 */         float f12 = i / b2;
/*  95 */         worldrenderer.pos(p_76986_2_ + d11 * f12, p_76986_4_ + d12 * (f12 * f12 + f12) * 0.5D + 0.25D, p_76986_6_ + d13 * f12);
/*     */       } 
/*     */       
/*  98 */       tessellator.draw();
/*  99 */       GL11.glEnable(2896);
/* 100 */       GL11.glEnable(3553);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(EntityFishHookCustom p_110775_1_) {
/* 109 */     return field_110792_a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
/* 117 */     return getEntityTexture((EntityFishHookCustom)p_110775_1_);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
/* 128 */     doRender((EntityFishHookCustom)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
/*     */   }
			}

/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\renderer\RenderFishHookCustom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */