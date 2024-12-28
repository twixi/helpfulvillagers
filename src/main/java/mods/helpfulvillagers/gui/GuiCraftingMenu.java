/*     */ package mods.helpfulvillagers.gui;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.crafting.CraftItem;
/*     */ import mods.helpfulvillagers.crafting.VillagerRecipe;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.CraftItemServerPacket;
/*     */ import mods.helpfulvillagers.network.GUICommandPacket;
		import mods.helpfulvillagers.gui.GuiCraftingMenu;
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
/*     */ import org.lwjgl.input.Mouse;
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
/*     */ 
/*     */ public class GuiCraftingMenu
/*     */   extends GuiContainer
/*     */ {
/*  40 */   public final int xSizeOfTexture = 195;
/*  41 */   public final int ySizeOfTexture = 136;
/*     */   
/*     */   public static final int MAX_ROWS = 3;
/*     */   
/*     */   public static final int MAX_COLS = 9;
/*     */   private EntityPlayer player;
/*     */   private static AbstractVillager villager;
/*  48 */   private static InventoryBasic selectedItemInv = new InventoryBasic("Selected Item", true, 1);
/*  49 */   private static InventoryBasic currentCraftInv = new InventoryBasic("Current Craft", true, 1);
/*  50 */   private static InventoryBasic recipesInv = new InventoryBasic("Recipes", true, 27);
/*     */   private AmountButton addButton;
/*     */   private AmountButton subButton;
/*     */   private static AmountButton trigger;
/*     */   private GuiButton addCraftButton;
/*     */   private TeachButton teachButton;
/*     */   private StatsButton statsButton;
/*     */   private static List itemList;
/*     */   private float currentScroll;
/*     */   private boolean wasClicking;
/*     */   private boolean isScrolling;
/*     */   private static int lowestStackSize;
/*     */   
/*     */   public GuiCraftingMenu(EntityPlayer player, AbstractVillager villager) {
/*  64 */     super(new CraftingContainer());
/*  65 */     this.player = player;
/*  66 */     //this;
			GuiCraftingMenu.villager = villager;
/*  67 */     this.currentScroll = 0.0F;
/*  68 */     initItemList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initItemList() {
/*  75 */     itemList = new ArrayList();
/*  76 */     for (VillagerRecipe i : villager.knownRecipes) {
/*  77 */       itemList.add(i.getOutput());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  83 */     super.initGui();
/*  84 */     int posX = (this.width - 195) / 2;
/*  85 */     int posY = (this.height - 136) / 2;
/*     */ 
/*     */     
/*  88 */     this.addButton = new AmountButton(0, posX + 58, posY + 70, true);
/*  89 */     this.subButton = new AmountButton(1, posX + 58, posY + 102, false);
/*  90 */     this.buttonList.add(this.addButton);
/*  91 */     this.buttonList.add(this.subButton);
/*  92 */     this.addButton.enabled = false;
/*  93 */     this.subButton.enabled = false;
/*     */ 
/*     */     
/*  96 */     this.addCraftButton = new GuiButton(3, posX + 83, posY + 110, 60, 20, "");
/*  97 */     this.buttonList.add(this.addCraftButton);
/*  98 */     this.addCraftButton.enabled = false;
/*     */ 
/*     */     
/* 101 */     this.teachButton = new TeachButton(4, posX + 173, posY + 90);
/* 102 */     this.buttonList.add(this.teachButton);
/*     */ 
/*     */     
/* 105 */     this.statsButton = new StatsButton(5, posX + 172, posY + 110);
/* 106 */     this.buttonList.add(this.statsButton);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 116 */     super.updateScreen();
/* 117 */     this.addButton.enabled = (selectedItemInv.getStackInSlot(0) != null && (selectedItemInv.getStackInSlot(0)).stackSize < selectedItemInv.getStackInSlot(0).getMaxStackSize());
/* 118 */     this.subButton.enabled = (selectedItemInv.getStackInSlot(0) != null && (selectedItemInv.getStackInSlot(0)).stackSize > lowestStackSize);
/* 119 */     this.addCraftButton.enabled = (selectedItemInv.getStackInSlot(0) != null);
/*     */     
/* 121 */     if (villager.currentCraftItem == null) {
/* 122 */       this.addCraftButton.displayString = "Craft Item";
/*     */     } else {
/* 124 */       this.addCraftButton.displayString = "Queue Item";
/*     */     } 
/*     */     
/* 127 */     triggerButton();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void triggerButton() {
/* 134 */     if (trigger != null) {
/* 135 */       actionPerformed(trigger);
/* 136 */       trigger = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int x, int y, float f) {
/* 142 */     boolean flag = Mouse.isButtonDown(0);
/* 143 */     int k = (this.width - 195) / 2;
/* 144 */     int l = (this.height - 136) / 2;
/* 145 */     int i1 = k + 174;
/* 146 */     int j1 = l + 8;
/* 147 */     int k1 = i1 + 14;
/* 148 */     int l1 = j1 + 52;
/*     */     
/* 150 */     if (!this.wasClicking && flag && x >= i1 && y >= j1 && x < k1 && y < l1) {
/* 151 */       this.isScrolling = needsScrollBars();
/*     */     }
/*     */     
/* 154 */     if (!flag) {
/* 155 */       this.isScrolling = false;
/*     */     }
/*     */     
/* 158 */     this.wasClicking = flag;
/*     */     
/* 160 */     if (this.isScrolling) {
/* 161 */       this.currentScroll = ((y - j1) - 7.5F) / ((l1 - j1) - 15.0F);
/*     */       
/* 163 */       if (this.currentScroll < 0.0F) {
/* 164 */         this.currentScroll = 0.0F;
/*     */       }
/*     */       
/* 167 */       if (this.currentScroll > 1.0F) {
/* 168 */         this.currentScroll = 1.0F;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 173 */     CraftingContainer.scrollTo(this.currentScroll);
/* 174 */     super.drawScreen(x, y, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean needsScrollBars() {
/* 181 */     return (itemList.size() > 27);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
			drawDefaultBackground();
/*     */     
/* 188 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 189 */   mc.renderEngine.bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/craft_menu.png"));
/*     */     
/* 191 */     int posX = (this.width - 195) / 2;
/* 192 */     int posY = (this.height - 136) / 2;
/*     */ 
/*     */     
/* 195 */     drawTexturedModalRect(posX, posY, 0, 0, 195, 136);
/*     */ 
/*     */     
/* 19	*/	drawTexturedModalRect(posX + 174, (int)(posY + 37.0F * this.currentScroll + 8.0F), needsScrollBars() ? 0 : 12, 241, 12, 15);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/* 203 */     super.handleMouseInput();
/* 204 */     int i = Mouse.getEventDWheel();
/* 205 */     if (i != 0 && needsScrollBars()) {
/* 206 */       int j = itemList.size() / 9 - 5 + 1;
/*     */       
/* 208 */       if (i > 0) {
/* 209 */         i = 1;
/*     */       }
/*     */       
/* 212 */       if (i < 0) {
/* 213 */         i = -1;
/*     */       }
/*     */       
/* 216 */       this.currentScroll = (float)(this.currentScroll - i / j);
/*     */       
/* 218 */       if (this.currentScroll < 0.0F) {
/* 219 */         this.currentScroll = 0.0F;
/*     */       }
/*     */       
/* 222 */       if (this.currentScroll > 1.0F) {
/* 223 */         this.currentScroll = 1.0F;
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
/*     */   protected void renderToolTip(ItemStack item, int x, int y) {
/* 236 */     if (item != null) {
/* 237 */       VillagerRecipe recipe = villager.getRecipe(item);
/* 238 */       if (recipe != null) {
/* 239 */         List list = recipe.getTooltip();
/* 240 */         drawHoveringText(list, x, y);
/*     */       } else {
/* 242 */         super.renderToolTip(item, x, y);
/*     */       } 
/*     */     } else {
/* 245 */       super.renderToolTip(item, x, y);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/*     */     ItemStack item;
/*     */     CraftItem craftItem;
/* 257 */     switch (button.id) {
/*     */       case 0:
/* 259 */         item = selectedItemInv.getStackInSlot(0);
/* 260 */         item.stackSize += lowestStackSize;
/* 261 */         if (item.stackSize > item.getMaxStackSize()) {
/* 262 */           item.stackSize = item.getMaxStackSize();
/*     */         }
/* 264 */         selectedItemInv.setInventorySlotContents(0, item);
/*     */         break;
/*     */       
/*     */       case 1:
/* 268 */         item = selectedItemInv.getStackInSlot(0);
/* 269 */         item.stackSize -= lowestStackSize;
/* 270 */         if (item.stackSize < lowestStackSize) {
/* 271 */           item.stackSize = lowestStackSize;
/*     */         }
/* 273 */         selectedItemInv.setInventorySlotContents(0, item);
/*     */         break;
/*     */       
/*     */       case 3:
/* 277 */         item = selectedItemInv.getStackInSlot(0).copy();
/* 278 */         craftItem = new CraftItem(item, this.player);
/* 279 */         if (villager.currentCraftItem == null) {
/* 280 */           villager.currentCraftItem = craftItem;
/* 281 */           HelpfulVillagers.network.sendToServer((IMessage)new CraftItemServerPacket(villager.getEntityId(), craftItem, true));
/*     */         } else {
/* 283 */           HelpfulVillagers.network.sendToServer((IMessage)new CraftItemServerPacket(villager.getEntityId(), craftItem, false));
/*     */         } 
/* 285 */         selectedItemInv.setInventorySlotContents(0, null);
/*     */         break;
/*     */       
/*     */       case 4:
/* 289 */  mc.thePlayer.closeScreen();
/*     */         
/* 291 */         if (villager.worldObj.isRemote) {
/* 292 */           villager.guiCommand = 7;
/* 293 */           HelpfulVillagers.network.sendToServer((IMessage)new GUICommandPacket(villager.getEntityId(), 7));
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 5:
/* 298 */         this.player.openGui(HelpfulVillagers.instance, 5, villager.worldObj, villager.getEntityId(), 0, 0);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fuonGuiClosed() {
/* 305 */     super.onGuiClosed();
/* 306 */     selectedItemInv.setInventorySlotContents(0, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CraftingContainer
/*     */     extends Container
/*     */   {
/*     */     public CraftingContainer() {
/* 3	*/addSlotToContainer(new Slot((IInventory)GuiCraftingMenu.selectedItemInv, 0, 49, 99));
/*     */ 
/*     */       
/* 321 */ addSlotToContainer(new Slot((IInventory)GuiCraftingMenu.currentCraftInv, 0, 107, 99));
/*     */ 
/*     */       
/* 324 */       int slotIndex = 0;
/* 325 */       for (int i = 0; i < 3; i++) {
/* 326 */         for (int j = 0; j < 9; j++) {
/* 327 */     addSlotToContainer(new Slot((IInventory)GuiCraftingMenu.recipesInv, slotIndex, j * 18 - 2, i * 18 + 23));
/* 328 */           slotIndex++;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static void scrollTo(float amount) {
/* 338 */       int index = (int)(GuiCraftingMenu.itemList.size() * amount);
/* 339 */       if (index > GuiCraftingMenu.itemList.size() - 27) {
/* 340 */         index = GuiCraftingMenu.itemList.size() - 27;
/*     */       }
/*     */       
/* 343 */       if (index < 0) {
/* 344 */         index = 0;
/*     */       }
/*     */ 
/*     */       
/* 348 */       if (GuiCraftingMenu.villager.currentCraftItem != null) {
/* 349 */         GuiCraftingMenu.currentCraftInv.setInventorySlotContents(0, GuiCraftingMenu.villager.currentCraftItem.getItem());
/*     */       } else {
/* 351 */         GuiCraftingMenu.currentCraftInv.setInventorySlotContents(0, null);
/*     */       } 
/*     */ 
/*     */       
/* 355 */       int slotIndex = 0;
/* 356 */       for (int i = 0; i < 3; i++) {
/* 357 */         for (int j = 0; j < 9; j++) {
/* 358 */           if (index >= GuiCraftingMenu.itemList.size()) {
/* 359 */             GuiCraftingMenu.recipesInv.setInventorySlotContents(slotIndex, null);
/*     */           } else {
/* 361 */             GuiCraftingMenu.recipesInv.setInventorySlotContents(slotIndex, (ItemStack) GuiCraftingMenu.itemList.get(index));
/*     */           } 
/* 363 */           slotIndex++;
/* 364 */           index++;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canInteractWith(EntityPlayer player) {
/* 371 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer p_7514player) {
/*     */       try {
/* 377 */         Slot slot = getSlot(slotId);
/*     */         
/* 379 */         if (slot == null || !slot.getHasStack()) {
/* 380 */           return null;
/*     */         }
/*     */         
/* 383 */         if (slot.inventory.getName().equals("Recipes")) {
/* 384 */           GuiCraftingMenu.lowestStackSize = (slot.getStack()).stackSize;
/* 385 */           GuiCraftingMenu.selectedItemInv.setInventorySlotContents(0, slot.getStack().copy());
/*     */         } 
/* 387 */       } catch (ArrayIndexOutOfBoundsException e) {}
/*     */ 
/*     */       
/* 390 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack transferStackInSlot(EntityPlayer player, int p_82index) {
/* 395 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AmountButton
/*     */     extends GuiButton
/*     */   {
/* 407 */     private final int MAX_PRESS = 10;
/*     */     
/*     */     private boolean up;
/*     */     private boolean beingPressed;
/*     */     private int pressCount;
/*     */     
/*     */     public AmountButton(int id, int x, int y, boolean flip) {
/* 414 */       super(id, x, y, 19, 12, "");
/* 415 */       this.up = flip;
/* 416 */       this.beingPressed = false;
/* 417 */       this.pressCount = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void drawButton(Minecraft mc, int x, int y) {
/* 425 */       if (this.visible) {
/* 426 */         mc.getTextureManager().bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/craft_menu.png"));
/* 427 */         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 428 */         boolean flag = (x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height);
/* 429 */         int u = 0;
/* 430 */         int v = 206;
/*     */         
/* 432 */         if (!this.up) {
/* 433 */           u += this.width;
/*     */         }
/*     */         
/* 436 */         if (!this.enabled) {
/* 437 */           v += this.height * 2;
/*     */           
/* 439 */           this.beingPressed = false;
/* 440 */           this.pressCount = 0;
/* 441 */         } else if (flag) {
/* 442 */           v += this.height;
/*     */         } else {
/* 444 */           this.beingPressed = false;
/* 445 */           this.pressCount = 0;
/*     */         } 
/*     */         
/* 448 */         drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
/*     */         
/* 450 */         if (this.beingPressed) {
/* 451 */           if (this.pressCount >= 10) {
/*     */             
/* 453 */             GuiCraftingMenu.trigger = this;
/* 454 */             this.pressCount = 0;
/*     */           } else {
/* 456 */             this.pressCount++;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public void mouseReleased(int mouseX, int p_146mouseY) {
/* 463 */       this.beingPressed = false;
/* 464 */       this.pressCount = 0;
/* 465 */       super.mouseReleased(mouseX, p_146mouseY);
/*     */     }
/*     */     
/*     */     public boolean fmousePressed(Minecraft pmc, int p_146116_mouseX, int p_146mouseY) {
/* 469 */       if (this.enabled) {
/* 470 */         this.beingPressed = true;
/*     */       }
/* 472 */       return super.mousePressed(pmc, p_146116_mouseX, p_146mouseY);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TeachButton
/*     */     extends GuiButton
/*     */   {
/*     */     public TeachButton(int id, int x, int y) {
/* 484 */       super(id, x, y, 14, 16, "");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void drawButton(Minecraft mc, int x, int y) {
/*     */       int u, v;
/* 492 */       mc.getTextureManager().bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/craft_menu.png"));
/* 493 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 494 */       boolean flag = (x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height);
/*     */ 
/*     */ 
/*     */       
/* 498 */       if (flag) {
/* 499 */         this.width = 16;
/* 500 */         this.height = 18;
/* 501 */         u = 0;
/* 502 */         v = 188;
/*     */       } else {
/* 504 */         this.width = 14;
/* 505 */         this.height = 16;
/* 506 */         u = 1;
/* 507 */         v = 189;
/*     */       } 
/*     */       
/* 510 */       drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StatsButton
/*     */     extends GuiButton
/*     */   {
/*     */     public StatsButton(int id, int x, int y) {
/* 522 */       super(id, x, y, 16, 15, "");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void drawButton(Minecraft mc, int x, int y) {
/*     */       int u, v;
/* 530 */       mc.getTextureManager().bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/craft_menu.png"));
/* 531 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 532 */       boolean flag = (x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height);
/*     */ 
/*     */ 
/*     */       
/* 536 */       if (flag) {
/* 537 */         this.width = 18;
/* 538 */         this.height = 17;
/* 539 */         u = 17;
/* 540 */         v = 189;
/*     */       } else {
/* 542 */         this.width = 16;
/* 543 */         this.height = 15;
/* 544 */         u = 18;
/* 545 */         v = 190;
/*     */       } 
/*     */       
/* 548 */       drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\gui\GuiCraftingMenu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */