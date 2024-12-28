/*    */ package mods.helpfulvillagers.gui;
/*    */ 
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
/*    */ import mods.helpfulvillagers.inventory.ContainerInventoryVillager;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.opengl.GL11;
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
/*    */ public class GuiVillagerInventory
/*    */   extends GuiContainer
/*    */ {
/* 29 */   private final int xSizeOfTexture = this.xSize;
/* 30 */   private final int ySizeOfTexture = this.ySize + 7;
/*    */   private AbstractVillager villager;
/*    */   
/*    */   public GuiVillagerInventory(AbstractVillager villager, IInventory playerInventory, IInventory villagerInventory) {
/* 34 */     super(new ContainerInventoryVillager(playerInventory, villagerInventory, villager));
/* 35 */     this.villager = villager;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerForegroundLayer(int par1, int par2) {}
/*    */ 
/*    */   
/*    */   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
			 this.mc.getTextureManager().bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/villager_trade.png"));
/* 45 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 46 */     int x = (this.width - this.xSize) / 2;
/* 47 */     int y = (this.height - this.ySize) / 2;
/* 48 */     this.drawTexturedModalRect(x, y, 0, 0, this.xSizeOfTexture, this.ySizeOfTexture);
/*    */ 
/*    */     
/* 51 */     if (this.villager.inventory.getStackInSlot(28) == null) {
/* 52 */     this.drawTexturedModalRect(x + 64, y + 72, 176, 0, 10, 9);
/*    */     }
/*    */     
/* 55 */     if (this.villager.inventory.getStackInSlot(29) == null) {
/* 56 */     this.drawTexturedModalRect(x + 80, y + 70, 176, 9, 14, 13);
/*    */     }
/*    */     
/* 59 */     if (this.villager.inventory.getStackInSlot(30) == null) {
/* 60 */     this.drawTexturedModalRect(x + 100, y + 70, 176, 22, 14, 13);
/*    */     }
/*    */     
/* 63 */     if (this.villager.inventory.getStackInSlot(31) == null) {
/* 64 */    this.drawTexturedModalRect(x + 116, y + 71, 176, 35, 14, 10);
/*    */     }
/*    */     
/* 67 */     if (this.villager.getCurrentItem() == null)
/* 68 */       if (this.villager instanceof mods.helpfulvillagers.entity.EntityLumberjack) {
/* 69 */this.drawTexturedModalRect(x + 43, y + 69, 176, 45, 14, 14);
/* 70 */       } else if (this.villager instanceof mods.helpfulvillagers.entity.EntityMiner) {
/* 71 */this.drawTexturedModalRect(x + 44, y + 70, 176, 59, 14, 14);
/* 72 */       } else if (this.villager instanceof mods.helpfulvillagers.entity.EntityFarmer) {
/* 73 */this.drawTexturedModalRect(x + 43, y + 69, 176, 73, 14, 14);
/* 74 */       } else if (this.villager instanceof mods.helpfulvillagers.entity.EntitySoldier) {
/* 75 */this.drawTexturedModalRect(x + 43, y + 68, 176, 88, 16, 16);
/* 76 */       } else if (this.villager instanceof mods.helpfulvillagers.entity.EntityArcher) {
/* 77 */this.drawTexturedModalRect(x + 44, y + 69, 176, 105, 16, 16);
/* 78 */       } else if (this.villager instanceof mods.helpfulvillagers.entity.EntityFisherman) {
/* 79 */this.drawTexturedModalRect(x + 44, y + 69, 176, 120, 16, 16);
/* 80 */       } else if (this.villager instanceof mods.helpfulvillagers.entity.EntityRancher) {
/* 81 */this.drawTexturedModalRect(x + 44, y + 69, 176, 134, 16, 16);
/* 82 */       } else if (this.villager instanceof mods.helpfulvillagers.entity.EntityBuilder) {
/* 83 */this.drawTexturedModalRect(x + 44, y + 70, 176, 149, 16, 16);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\gui\GuiVillagerInventory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */