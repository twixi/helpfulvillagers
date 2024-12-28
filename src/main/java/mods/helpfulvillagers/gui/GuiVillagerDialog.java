/*     */ package mods.helpfulvillagers.gui;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.GUICommandPacket;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiVillagerDialog
/*     */   extends GuiScreen
/*     */ {
/*     */   private EntityPlayer player;
/*     */   private AbstractVillager villager;
/*     */   public final int xSizeOfTexture;
/*     */   public final int ySizeOfTexture;
/*     */   
/*     */   public GuiVillagerDialog(EntityPlayer player, AbstractVillager villager) {
/*  33 */     this.player = player;
/*  34 */     this.villager = villager;
/*  35 */     this.xSizeOfTexture = 120;
/*  36 */     if (villager.canCraft() || villager instanceof mods.helpfulvillagers.entity.EntityMerchant || villager instanceof mods.helpfulvillagers.entity.EntitySoldier || villager instanceof mods.helpfulvillagers.entity.EntityArcher || villager instanceof mods.helpfulvillagers.entity.EntityBuilder) {
/*  37 */       this.ySizeOfTexture = 130;
/*     */     } else {
/*  39 */       this.ySizeOfTexture = 105;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int x, int y, float f) {
/*  45 */     drawDefaultBackground();
/*     */     
/*  47 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  48 */   this.mc.getTextureManager().bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/dialog_background.png"));
/*     */     
/*  50 */     int posX = (this.width - this.xSizeOfTexture) / 2;
/*  51 */     int posY = (this.height - this.ySizeOfTexture) / 2;
/*     */     
/*  53 */     drawTexturedModalRect(posX, posY, 10, 10, this.xSizeOfTexture, this.ySizeOfTexture);
/*  54 */  super.drawScreen(x, y, f);
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  58 */     this.buttonList.clear();
/*     */     
/*  60 */     int posX = (this.width - this.xSizeOfTexture) / 2;
/*  61 */     int posY = (this.height - this.ySizeOfTexture) / 2;
/*     */     
/*  63 */     if (this.villager.leader == null) {
/*  64 */       this.buttonList.add(new GuiButton(0, posX + 10, posY + 5, 100, 20, "Follow Me"));
/*     */     } else {
/*  66 */       this.buttonList.add(new GuiButton(1, posX + 10, posY + 5, 100, 20, "Stop Following"));
/*     */     } 
/*     */     
/*  69 */     this.buttonList.add(new GuiButton(2, posX + 10, posY + 30, 100, 20, "Trade"));
/*     */     
/*  71 */     this.buttonList.add(new GuiButton(3, posX + 10, posY + 55, 100, 20, "Change Profession"));
/*     */     
/*  73 */     this.buttonList.add(new GuiButton(4, posX + 10, posY + 80, 100, 20, "Give Nickname"));
/*     */     
/*  75 */     if (this.villager instanceof mods.helpfulvillagers.entity.EntitySoldier || this.villager instanceof mods.helpfulvillagers.entity.EntityArcher) {
/*  76 */       this.buttonList.add(new GuiButton(5, posX + 10, posY + 105, 100, 20, "Guard Villager"));
/*  77 */     } else if (this.villager instanceof mods.helpfulvillagers.entity.EntityMerchant) {
/*  78 */       this.buttonList.add(new GuiButton(8, posX + 10, posY + 105, 100, 20, "Barter"));
/*  79 */     } else if (this.villager instanceof mods.helpfulvillagers.entity.EntityBuilder) {
/*  80 */       this.buttonList.add(new GuiButton(9, posX + 10, posY + 105, 100, 20, "Construction"));
/*  81 */     } else if (this.villager.canCraft()) {
/*  82 */       this.buttonList.add(new GuiButton(6, posX + 10, posY + 105, 100, 20, "Crafting"));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(GuiButton button) {
/*  97 */     int guiCommand = -1;
/*  98 */     guiCommand = button.id;
/*  99 */     this.mc.thePlayer.closeScreen();
/*     */     
/* 101 */     if (this.villager.worldObj.isRemote) {
/* 102 */       this.villager.guiCommand = guiCommand;
/* 103 */       HelpfulVillagers.network.sendToServer((IMessage)new GUICommandPacket(this.villager.getEntityId(), guiCommand));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\gui\GuiVillagerDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */