/*    */ package mods.helpfulvillagers.gui;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;

/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
/*    */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*    */ import mods.helpfulvillagers.network.NicknamePacket;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiTextField;
/*    */ import net.minecraft.entity.player.EntityPlayer;
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
/*    */ public class GuiNickname
/*    */   extends GuiScreen
/*    */ {
/*    */   private EntityPlayer player;
/*    */   private AbstractVillager villager;
/* 25 */   private final int boxWidth = 150;
/* 26 */   private final int boxHeight = 20;
/*    */   private GuiTextField textField;
/*    */   private String name;
/*    */   private final String START_NAME;
/*    */   private boolean changeName;
/*    */   
/*    */   public GuiNickname(EntityPlayer player, AbstractVillager villager) {
/* 33 */     this.player = player;
/* 34 */     this.villager = villager;
/* 35 */     this.name = villager.nickname;
/* 36 */     this.START_NAME = this.name;
/* 37 */     this.changeName = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 42 */     this.changeName = false;
/* 43 */     int posX = (this.width - 150) / 2;
/* 44 */     int posY = (this.height - 20) / 2;
/* 45 */     this.textField = new GuiTextField(0, this.fontRendererObj, posX, posY, 150, 20);
/* 46 */     this.textField.setFocused(true);
/* 47 */     this.textField.setMaxStringLength(20);
/* 48 */     String name = this.villager.getCustomNameTag();
/* 49 */     this.textField.setText(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int x, int y, float f) {
/* 54 */     int posX = (this.width - 150) / 2;
/* 55 */     int posY = (this.height - 20) / 2;
/* 56 */     drawDefaultBackground();
/* 57 */	 drawString(this.fontRendererObj, "Enter Nickname:", posX, posY - 20, 16777215);
/* 58 */     this.textField.drawTextBox();
/* 59 */     super.drawScreen(x, y, f);
/*    */   }
/*    */ 
/*    */   
/*    */   public void keyTyped(char c, int i) throws IOException {
/* 64 */     super.keyTyped(c, i);
/* 65 */     if (i == 28) {
/* 66 */       this.changeName = true;
/* 67 */       super.keyTyped(c, 1);
/*    */     } 
/* 69 */     this.textField.textboxKeyTyped(c, i);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateScreen() {
/* 74 */     this.name = this.textField.getText();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onGuiClosed() {
/* 79 */     if (this.changeName && this.name != this.START_NAME) {
/* 80 */       this.villager.nickname = this.name;
/* 81 */       HelpfulVillagers.network.sendToServer((IMessage)new NicknamePacket(this.villager.getEntityId(), this.name));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doesGuiPauseGame() {
/* 87 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\gui\GuiNickname.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */