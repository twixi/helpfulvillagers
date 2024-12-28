/*     */ package mods.helpfulvillagers.gui;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.crafting.CraftItem;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.CraftQueueServerPacket;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryBasic;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
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
/*     */ public class GuiCraftStats
/*     */   extends GuiContainer
/*     */ {
/*     */   private static final int MAX_SLOTS = 9;
/*  34 */   public final int xSizeOfTexture = 195;
/*  35 */   public final int ySizeOfTexture = 136;
/*     */   
/*     */   private EntityPlayer player;
/*     */   
/*     */   private static AbstractVillager villager;
/*  40 */   private static InventoryBasic craftQueueInv = new InventoryBasic("Craft Queue", true, 9);
/*  41 */   private static InventoryBasic materialsNeededInv = new InventoryBasic("Materials Needed", true, 9);
/*  42 */   private static InventoryBasic materialsCollectedInv = new InventoryBasic("Materials Collected", true, 9);
/*     */   
/*     */   private static int index1;
/*     */   
/*     */   private static int index2;
/*     */   
/*     */   private static int index3;
/*     */   private ScrollButton leftButton1;
/*     */   private ScrollButton leftButton2;
/*     */   private ScrollButton leftButton3;
/*     */   private ScrollButton rightButton1;
/*     */   private ScrollButton rightButton2;
/*     */   private ScrollButton rightButton3;
/*     */   private GuiButton backButton;
/*     */   private GuiButton removeButton;
/*     */   private static int selectedCraftIndex;
/*     */   private static CraftItem selectedCraftItem;
/*     */   
/*     */   public GuiCraftStats(EntityPlayer player, AbstractVillager villager) {
/*  61 */     super(new StatsContainer());
/*  62 */     this.player = player;
/*  63 */	  GuiCraftStats.villager = villager;
/*  64 */     index1 = 0;
/*  65 */     index2 = 0;
/*  66 */     index3 = 0;
/*  67 */     selectedCraftIndex = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void finitGui() {
/*  72 */     super.initGui();
/*  73 */     int posX = (this.width - 195) / 2;
/*  74 */     int posY = (this.height - 136) / 2;
/*     */ 
/*     */     
/*  77 */     this.leftButton1 = new ScrollButton(0, posX + 4, posY + 22, true);
/*  78 */     this.rightButton1 = new ScrollButton(1, posX + 180, posY + 22, false);
/*  79 */     this.buttonList.add(this.leftButton1);
/*  80 */     this.buttonList.add(this.rightButton1);
/*  81 */     this.leftButton1.enabled = false;
/*  82 */     this.rightButton1.enabled = false;
/*     */ 
/*     */     
/*  85 */     this.leftButton2 = new ScrollButton(2, posX + 4, posY + 58, true);
/*  86 */     this.rightButton2 = new ScrollButton(3, posX + 180, posY + 58, false);
/*  87 */     this.buttonList.add(this.leftButton2);
/*  88 */     this.buttonList.add(this.rightButton2);
/*  89 */     this.leftButton2.enabled = false;
/*  90 */     this.rightButton2.enabled = false;
/*     */ 
/*     */     
/*  93 */     this.leftButton3 = new ScrollButton(4, posX + 4, posY + 93, true);
/*  94 */     this.rightButton3 = new ScrollButton(5, posX + 180, posY + 93, false);
/*  95 */     this.buttonList.add(this.leftButton3);
/*  96 */     this.buttonList.add(this.rightButton3);
/*  97 */     this.leftButton3.enabled = false;
/*  98 */     this.rightButton3.enabled = false;
/*     */ 
/*     */     
/* 101 */     this.backButton = new GuiButton(6, posX + 20, posY + 112, 60, 20, "Back");
/* 102 */     this.buttonList.add(this.backButton);
/*     */ 
/*     */     
/* 105 */     this.removeButton = new GuiButton(7, posX + 120, posY + 112, 60, 20, "Remove");
/* 106 */     this.buttonList.add(this.removeButton);
/* 107 */     this.removeButton.enabled = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
			drawDefaultBackground();
/*     */     
/* 114 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 115 */   this.mc.renderEngine.bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/craft_details.png"));
/*     */     
/* 117 */     int posX = (this.width - 195) / 2;
/* 118 */     int posY = (this.height - 136) / 2;
/*     */ 
/*     */     
/* 121 */     drawTexturedModalRect(posX, posY, 0, 0, 195, 136);
/*     */ 
/*     */     
/* 12*/		 drawString(this.fontRendererObj, "Village Craft Queue:", posX + 18, posY + 10, 16777215);
/* 125 */    drawString(this.fontRendererObj, "Materials Needed:", posX + 18, posY + 45, 16777215);
/* 126 */    drawString(this.fontRendererObj, "Materials Collected:", posX + 18, posY + 80, 16777215);
/*     */     
/* 128 */     this.mc.renderEngine.bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/craft_details.png"));
/*     */     
/* 130 */     int indexDiff = selectedCraftIndex - index1;
/* 131 */     if (indexDiff >= 0 && indexDiff <= 8) {
/* 132 */       drawTexturedModalRect(indexDiff * 18 + posX + 16, posY + 17, 232, 0, 24, 22);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 138 */     super.updateScreen();
/* 139 */     this.leftButton1.enabled = (index1 > 0);
/* 140 */     this.rightButton1.enabled = (index1 < villager.homeVillage.craftQueue.getSize() - 9);
/*     */     
/* 142 */     this.leftButton2.enabled = (index2 > 0);
/* 143 */     this.rightButton2.enabled = (index2 < villager.materialsNeeded.size() - 9);
/*     */     
/* 145 */     this.leftButton3.enabled = (index3 > 0);
/* 146 */     this.rightButton3.enabled = (index3 < villager.inventory.materialsCollected.size() - 9);
/*     */     
/* 148 */     int indexDiff = selectedCraftIndex - index1;
/* 149 */     this.removeButton.enabled = (selectedCraftItem != null && (selectedCraftItem.getPriority() <= 0 || selectedCraftItem.getName().equals(this.player.getDisplayName())) && indexDiff >= 0 && indexDiff <= 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73drawScreen(int x, int y, float f) {
/* 154 */     StatsContainer.scrollTo(index1, index2, index3);
/* 155 */     super.drawScreen(x, y, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderToolTip(ItemStack item, int x, int y) {
/* 166 */     if (item != null) {
/* 167 */       CraftItem craftItem = getCraftItemAt(item, x, y);
/* 168 */       if (craftItem != null) {
/* 169 */         List list = craftItem.getTooltip();
/* 170 */         drawHoveringText(list, x, y);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CraftItem getCraftItemAt(ItemStack item, int x, int y) {
/* 183 */     int posX = (this.width - 195) / 2;
/* 184 */     int posY = (this.height - 136) / 2;
/*     */     
/* 186 */     if (y > posY + 40) {
/* 187 */       return null;
/*     */     }
/*     */     
/* 190 */     int n = x - posX;
/* 191 */     n = (n + 4) / 5 * 5;
/* 192 */     n /= 17;
/* 193 */     n--;
/*     */     
/* 195 */     if (n > index1 + 9) {
/* 196 */       n = index1 + 9;
/*     */     }
/*     */     
/*     */     try {
/* 200 */       CraftItem craftItem = villager.homeVillage.craftQueue.getItemStackAt(index1 + n);
/* 201 */       if (craftItem.getItem().getDisplayName().equals(item.getDisplayName())) {
/* 202 */         return craftItem;
/*     */       }
/* 204 */       craftItem = villager.homeVillage.craftQueue.getItemStackAt(index1 + n - 1);
/* 205 */       return craftItem;
/*     */     }
/* 207 */     catch (NullPointerException e) {
/* 208 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setSelectedCraftItem(int slot) {
/* 213 */     if (slot >= 0 && slot <= 8) {
/* 214 */       selectedCraftIndex = index1 + slot;
/* 215 */       if (selectedCraftIndex >= 0 && selectedCraftIndex < villager.homeVillage.craftQueue.getSize()) {
/* 216 */         selectedCraftItem = villager.homeVillage.craftQueue.getItemStackAt(selectedCraftIndex);
/*     */       } else {
/* 218 */         selectedCraftItem = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/* 225 */     switch (button.id) {
/*     */       case 0:
/* 227 */         index1--;
/* 228 */         if (index1 < 0) {
/* 229 */           index1 = 0;
/*     */         }
/*     */         break;
/*     */       
/*     */       case 1:
/* 234 */         index1++;
/* 235 */         if (index1 > villager.homeVillage.craftQueue.getSize()) {
/* 236 */           index1 = villager.homeVillage.craftQueue.getSize();
/*     */         }
/*     */         break;
/*     */       
/*     */       case 2:
/* 241 */         index2--;
/* 242 */         if (index2 < 0) {
/* 243 */           index2 = 0;
/*     */         }
/*     */         break;
/*     */       
/*     */       case 3:
/* 248 */         index2++;
/* 249 */         if (index2 > villager.materialsNeeded.size()) {
/* 250 */           index2 = villager.materialsNeeded.size();
/*     */         }
/*     */         break;
/*     */       
/*     */       case 4:
/* 255 */         index3--;
/* 256 */         if (index3 < 0) {
/* 257 */           index3 = 0;
/*     */         }
/*     */         break;
/*     */       
/*     */       case 5:
/* 262 */         index3++;
/* 263 */         if (index3 > villager.inventory.materialsCollected.size()) {
/* 264 */           index3 = villager.inventory.materialsCollected.size();
/*     */         }
/*     */         break;
/*     */       
/*     */       case 6:
/* 269 */         this.player.openGui(HelpfulVillagers.instance, 4, villager.worldObj, villager.getEntityId(), 0, 0);
/*     */         break;
/*     */       
/*     */       case 7:
/* 273 */         villager.homeVillage.craftQueue.removeItemStackAt(selectedCraftIndex);
/* 274 */         HelpfulVillagers.network.sendToServer((IMessage)new CraftQueueServerPacket(villager.getEntityId(), villager.homeVillage.craftQueue.getAll()));
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StatsContainer
/*     */     extends Container
/*     */   {
/*     */     public StatsContainer() {
/*     */       int i;
/* 288 */       for (i = 0; i < 9; i++) {
/* 289 */         addSlotToContainer(new Slot((IInventory)GuiCraftStats.craftQueueInv, i, i * 18 + 9, 36));
/*     */       }
/*     */ 
/*     */       
/* 293 */       for (i = 0; i < 9; i++) {
/* 294 */   addSlotToContainer(new Slot((IInventory)GuiCraftStats.materialsNeededInv, i, i * 18 + 9, 72));
/*     */       }
/*     */ 
/*     */       
/* 298 */       for (i = 0; i < 9; i++) {
/* 299 */   addSlotToContainer(new Slot((IInventory)GuiCraftStats.materialsCollectedInv, i, i * 18 + 9, 108));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public static void scrollTo(int index1, int index2, int index3) {
/* 305 */       for (int i = 0; i < 9; i++) {
/*     */         
/* 307 */         if (i + index1 >= GuiCraftStats.villager.homeVillage.craftQueue.getSize()) {
/* 308 */           GuiCraftStats.craftQueueInv.setInventorySlotContents(i, null);
/*     */         } else {
/* 310 */           GuiCraftStats.craftQueueInv.setInventorySlotContents(i, GuiCraftStats.villager.homeVillage.craftQueue.getItemStackAt(i + index1).getItem());
/*     */         } 
/*     */ 
/*     */         
/* 314 */         if (i + index2 >= GuiCraftStats.villager.materialsNeeded.size()) {
/* 315 */           GuiCraftStats.materialsNeededInv.setInventorySlotContents(i, null);
/*     */         } else {
/* 317 */           GuiCraftStats.materialsNeededInv.setInventorySlotContents(i, GuiCraftStats.villager.materialsNeeded.get(i + index2));
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 322 */         if (i + index3 >= GuiCraftStats.villager.inventory.materialsCollected.size()) {
/* 323 */           GuiCraftStats.materialsCollectedInv.setInventorySlotContents(i, null);
/*     */         } else {
/* 325 */           GuiCraftStats.materialsCollectedInv.setInventorySlotContents(i, GuiCraftStats.villager.inventory.materialsCollected.get(i + index3));
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canInteractWith(EntityPlayer player) {
/* 333 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player) {
/*     */       try {
/* 339 */         Slot slot = getSlot(slotId);
/* 340 */         if (slot.inventory.getName().equals("Craft Queue")) {
/* 341 */           GuiCraftStats.setSelectedCraftItem(slotId);
/*     */         }
/* 343 */         return null;
/* 344 */       } catch (ArrayIndexOutOfBoundsException e) {
/* 345 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack transferStackInSlot(EntityPlayer player, int p_82index) {
/* 351 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ScrollButton
/*     */     extends GuiButton
/*     */   {
/*     */     private boolean left;
/*     */ 
/*     */ 
/*     */     
/*     */     public ScrollButton(int id, int x, int y, boolean flip) {
/* 365 */       super(id, x, y, 12, 19, "");
/* 366 */       this.left = flip;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void func_drawButton(Minecraft mc, int x, int y) {
/* 374 */       if (this.visible) {
/* 375 */         mc.getTextureManager().bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/craft_details.png"));
/* 376 */         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 377 */         boolean flag = (x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height);
/* 378 */         int u = 195;
/* 379 */         int v = 0;
/*     */         
/* 381 */         if (this.left) {
/* 382 */           v += this.height;
/*     */         }
/*     */         
/* 385 */         if (!this.enabled) {
/* 386 */           u += this.width * 2;
/* 387 */         } else if (flag) {
/* 388 */           u += this.width;
/*     */         } 
/*     */         
/* 391 */         drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\gui\GuiCraftStats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */