/*     */ package mods.helpfulvillagers.gui;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.ProfessionChangePacket;
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
/*     */ public class GuiProfessionDialog
/*     */   extends GuiScreen
/*     */ {
/*     */   private EntityPlayer player;
/*     */   private AbstractVillager villager;
/*  28 */   public final int xSizeOfTexture = 230;
/*  29 */   public final int ySizeOfTexture = 130;
/*     */   
/*     */   public GuiProfessionDialog(EntityPlayer player, AbstractVillager villager) {
/*  32 */     this.player = player;
/*  33 */     this.villager = villager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int x, int y, float f) {
/*  39 */     for (int i = 1; i < this.buttonList.size(); i++) {
/*  40 */       if (this.buttonList.get(i) instanceof GuiButton) {
/*  41 */         GuiButton btn = this.buttonList.get(i);
/*     */         try {
/*  43 */           btn.enabled = this.villager.homeVillage.unlockedHalls[btn.id - 1];
/*  44 */         } catch (NullPointerException e) {
/*  45 */           btn.enabled = false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  50 */     drawDefaultBackground();
/*     */ 
/*     */     
/*  53 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  54 */   mc.getTextureManager().bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/dialog_background.png"));
/*     */     
/*  56 */     int posX = (this.width - 230) / 2;
/*  57 */     int posY = (this.height - 130) / 2;
/*     */     
/*  59 */     this.drawTexturedModalRect(posX, posY, 0, 0, 230, 130);
/*  60 */  super.drawScreen(x, y, f);
/*     */ 
/*     */     
/*  63 */     for (int j = 0; j < this.buttonList.size(); j++) {
/*  64 */       if (this.buttonList.get(j) instanceof GuiButton) {
/*  65 */         GuiButton btn = this.buttonList.get(j);
/*  66 */         if (btn.isMouseOver() && !btn.enabled) {
/*  67 */           String[] desc = { "Build Guild Hall To Unlock" };
/*     */           
/*  69 */           List<String> temp = Arrays.asList(desc);
/*  70 */           drawHoveringText(temp, x, y, this.fontRendererObj);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  77 */     this.buttonList.clear();
/*     */     
/*  79 */     int posX = (this.width - 230) / 2;
/*  80 */     int posY = (this.height - 130) / 2;
/*     */     
/*  82 */     this.buttonList.add(new GuiButton(0, posX + 10, posY + 5, 100, 20, "Villager"));
/*  83 */     this.buttonList.add(new GuiButton(1, posX + 10, posY + 30, 100, 20, "Lumberjack"));
/*  84 */     this.buttonList.add(new GuiButton(2, posX + 10, posY + 55, 100, 20, "Miner"));
/*  85 */     this.buttonList.add(new GuiButton(3, posX + 10, posY + 80, 100, 20, "Farmer"));
/*     */     
/*  87 */     this.buttonList.add(new GuiButton(4, posX + 120, posY + 5, 100, 20, "Soldier"));
/*  88 */     this.buttonList.add(new GuiButton(5, posX + 120, posY + 30, 100, 20, "Archer"));
/*  89 */     this.buttonList.add(new GuiButton(6, posX + 120, posY + 55, 100, 20, "Merchant"));
/*  90 */     this.buttonList.add(new GuiButton(7, posX + 120, posY + 80, 100, 20, "Fisherman"));
/*     */     
/*  92 */     this.buttonList.add(new GuiButton(8, posX + 10, posY + 105, 100, 20, "Rancher"));
/*  93 */     this.buttonList.add(new GuiButton(9, posX + 120, posY + 105, 100, 20, "Builder"));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(GuiButton button) {
/* 106 */     this.mc.thePlayer.closeScreen();
/* 107 */     this.villager.setProfession(button.id);
/* 108 */     HelpfulVillagers.network.sendToServer((IMessage)new ProfessionChangePacket(this.villager.getEntityId(), button.id));
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\gui\GuiProfessionDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */