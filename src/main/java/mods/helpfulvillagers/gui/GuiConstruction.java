/*    */ package mods.helpfulvillagers.gui;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
/*    */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*    */ import mods.helpfulvillagers.network.ConstructionJobPacket;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuiConstruction
/*    */   extends GuiScreen
/*    */ {
/*    */   private EntityPlayer player;
/*    */   private AbstractVillager villager;
/* 22 */   public final int xSizeOfTexture = 120;
/* 23 */   public final int ySizeOfTexture = 105;
/*    */   
/*    */   public GuiConstruction(EntityPlayer player, AbstractVillager villager) {
/* 26 */     this.player = player;
/* 27 */     this.villager = villager;
/*    */   }
/*    */ 
/*    */   
/*    */   public void fdrawScreen(int x, int y, float f) {
/* 32 */     drawDefaultBackground();
/*    */     
/* 34 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 35 */   mc.renderEngine.bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/dialog_background.png"));
/*    */     
/* 37 */     int posX = (this.width - 120) / 2;
/* 38 */     int posY = (this.height - 105) / 2;
/*    */     
/* 40 */     drawTexturedModalRect(posX, posY, 10, 10, 120, 105);
/* 41 */  drawScreen(x, y, f);
/*    */   }
/*    */   
/*    */   public void fuinitGui() {
/* 45 */     this.buttonList.clear();
/*    */     
/* 47 */     int posX = (this.width - 120) / 2;
/* 48 */     int posY = (this.height - 105) / 2;
/*    */     
/* 50 */     this.buttonList.add(new GuiButton(0, posX + 10, posY + 5, 100, 20, "Demolish"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean funcdoesGuiPauseGame() {
/* 56 */     return false;
/*    */   }
/*    */   
/*    */   public void actionPerformed(GuiButton button) {
/* 60 */     HelpfulVillagers.network.sendToServer((IMessage)new ConstructionJobPacket(this.villager.getEntityId(), this.player.getEntityId(), button.id));
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\gui\GuiConstruction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */