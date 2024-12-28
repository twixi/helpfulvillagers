/*      */ package mods.helpfulvillagers.gui;
/*      */ 
/*      */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*      */ import net.minecraftforge.fml.relauncher.Side;
/*      */ import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import mods.helpfulvillagers.entity.AbstractVillager;
/*      */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*      */ import mods.helpfulvillagers.network.PlayerAccountServerPacket;
/*      */ import mods.helpfulvillagers.network.PlayerCraftMatrixResetPacket;
/*      */ import mods.helpfulvillagers.network.PlayerInventoryPacket;
/*      */ import mods.helpfulvillagers.network.PlayerItemStackPacket;
/*      */ import net.minecraft.client.gui.GuiButton;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.gui.GuiTextField;
/*      */ import net.minecraft.client.gui.achievement.GuiAchievements;
/*      */ import net.minecraft.client.gui.achievement.GuiStats;
/*      */ import net.minecraft.client.gui.inventory.GuiContainer;
/*      */ import net.minecraft.client.gui.inventory.GuiInventory;
/*      */ import net.minecraft.client.renderer.RenderHelper;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.creativetab.CreativeTabs;
/*      */ import net.minecraft.enchantment.Enchantment;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.inventory.ContainerPlayer;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.inventory.InventoryBasic;
/*      */ import net.minecraft.inventory.Slot;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @SideOnly(Side.CLIENT)
/*      */ public class GuiBarter
/*      */   extends GuiContainer
/*      */ {
/*   59 */   private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("helpfulvillagers", "textures/gui/barter_inventory/tabs.png");
/*      */   
/*   61 */   private static InventoryBasic barterItems = new InventoryBasic("Barter", true, 45);
/*   62 */   private static InventoryBasic buyItemInv = new InventoryBasic("Buy Item", true, 1);
/*   63 */   private static InventoryBasic sellItemInv = new InventoryBasic("Sell Item", true, 1);
/*   64 */   private static InventoryBasic currencyInputInv = new InventoryBasic("Currency Input", true, 1);
/*   65 */   private static InventoryBasic currencyOutputInv = new InventoryBasic("Currency Output", true, 1);
/*      */   
/*      */   private Slot sellItem;
/*      */   
/*      */   private Slot currencyOutput;
/*      */   
/*   71 */   private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
/*      */   
/*      */   private float currentScroll;
/*      */   
/*      */   private boolean isScrolling;
/*      */   
/*      */   private boolean wasClicking;
/*      */   
/*      */   private GuiTextField searchField;
/*      */   
/*      */   private List field_147063_B;
/*      */   
/*      */   private boolean field_147057_D;
/*      */   private static final String __OBFID = "CL_00000752";
/*   85 */   private static int tabPage = 0;
/*   86 */   private int maxPages = 0;
/*      */   
/*      */   private EntityPlayer player;
/*      */   
/*      */   private AbstractVillager villager;
/*      */   
/*      */   private GuiButton creditBuyButton;
/*      */   private GuiButton creditSellButton;
/*      */   private GuiButton creditWithdrawButton;
/*      */   private ItemStack dragItem;
/*      */   
/*      */   public GuiBarter(EntityPlayer player, AbstractVillager villager) {
/*   98 */     super(new ContainerBarter(player, villager));
/*   99 */     this.ySize = 136;
/*  100 */     this.xSize = 195;
/*      */     
/*  102 */     this.player = player;
/*  103 */     this.villager = villager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initGui() {
/*  110 */     super.initGui();
/*  111 */     this.buttonList.clear();
/*  112 */     Keyboard.enableRepeatEvents(true);
/*  113 */     this.searchField = new GuiTextField(this.maxPages, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
/*  114 */     this.searchField.setMaxStringLength(15);
/*  115 */     this.searchField.setEnableBackgroundDrawing(false);
/*  116 */     this.searchField.setVisible(false);
/*  117 */     this.searchField.setTextColor(16777215);
/*  118 */     int i = selectedTabIndex;
/*  119 */     selectedTabIndex = -1;
/*  120 */     setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
/*  121 */     int tabCount = CreativeTabs.creativeTabArray.length;
/*  122 */     if (tabCount > 12) {
/*  123 */       this.buttonList.add(new GuiButton(101, this.guiLeft, this.guiTop - 50, 20, 20, "<"));
/*  124 */       this.buttonList.add(new GuiButton(102, this.guiLeft + this.xSize - 20, this.guiTop - 50, 20, 20, ">"));
/*  125 */       this.maxPages = (tabCount - 12) / 10 + 1;
/*      */     } 
/*      */     
/*  128 */     this.creditBuyButton = new GuiButton(10, this.guiLeft + 10, this.guiTop + 110, 40, 20, "Buy");
/*  129 */     this.buttonList.add(this.creditBuyButton);
/*  130 */     this.creditBuyButton.enabled = false;
/*  131 */     this.creditBuyButton.visible = (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex());
/*      */     
/*  133 */     this.creditSellButton = new GuiButton(11, this.guiLeft + 85, this.guiTop + 32, 42, 20, "Sell");
/*  134 */     this.buttonList.add(this.creditSellButton);
/*  135 */     this.creditSellButton.enabled = false;
/*  136 */     this.creditSellButton.visible = (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex());
/*      */     
/*  138 */     this.creditWithdrawButton = new GuiButton(12, this.guiLeft + 135, this.guiTop + 32, 50, 20, "Withdraw");
/*  139 */     this.buttonList.add(this.creditWithdrawButton);
/*  140 */     this.creditWithdrawButton.enabled = false;
/*  141 */     this.creditWithdrawButton.visible = (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fupdateScreen() {
/*  148 */     HelpfulVillagers.network.sendToServer((IMessage)new PlayerCraftMatrixResetPacket(this.player.getEntityId()));
/*  149 */     HelpfulVillagers.network.sendToServer((IMessage)new PlayerInventoryPacket(this.player.getEntityId(), this.player.inventory.mainInventory, this.player.inventory.armorInventory));
/*      */ 
/*      */     
/*  152 */     this.creditBuyButton.visible = (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex());
/*  153 */     this.creditSellButton.visible = (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex());
/*  154 */     this.creditWithdrawButton.visible = (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex());
/*      */     
/*  156 */     this.dragItem = this.player.inventory.getItemStack();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleMouseClick(Slot slot, int x, int y, int z) {
/*  167 */     boolean flag = (z == 1);
/*  168 */     z = (x == 64537 && z == 0) ? 4 : z;
/*      */     
/*  170 */     InventoryPlayer inventoryplayer = this.player.inventory;
/*      */     
/*  172 */     if (slot == null) {
/*  173 */       super.handleMouseClick(slot, x, y, z);
/*      */       
/*      */       return;
/*      */     } 
/*  177 */     if (this.creditSellButton.visible) {
/*  178 */       if (slot == this.sellItem) {
/*  179 */         if (slot.getHasStack()) {
/*  180 */           if (inventoryplayer.getItemStack() == null) {
/*  181 */             ItemStack temp = slot.getStack().copy();
/*  182 */             inventoryplayer.setItemStack(temp);
/*  183 */             HelpfulVillagers.network.sendToServer((IMessage)new PlayerItemStackPacket(this.player.getEntityId(), temp));
/*  184 */             slot.putStack(null);
/*      */           } 
/*  186 */         } else if (inventoryplayer.getItemStack() != null) {
/*  187 */           ItemStack temp = inventoryplayer.getItemStack();
/*  188 */           inventoryplayer.setItemStack(null);
/*  189 */           slot.putStack(temp);
/*  190 */           HelpfulVillagers.network.sendToServer((IMessage)new PlayerItemStackPacket(this.player.getEntityId(), null));
/*      */         } 
/*      */         
/*  193 */         calculateCurrencyOutput();
/*  194 */       } else if (slot == this.currencyOutput) {
/*  195 */         if (slot.getHasStack() && inventoryplayer.getItemStack() == null) {
/*  196 */           int amount = (slot.getStack()).stackSize;
/*  197 */           inventoryplayer.setItemStack(slot.getStack());
/*  198 */           slot.putStack(null);
/*  199 */           HelpfulVillagers.network.sendToServer((IMessage)new PlayerItemStackPacket(this.player.getEntityId(), slot.getStack()));
/*  200 */           HelpfulVillagers.network.sendToServer((IMessage)new PlayerCraftMatrixResetPacket(this.player.getEntityId()));
/*      */           
/*  202 */           ItemStack item = sellItemInv.getStackInSlot(0);
/*  203 */           if (item != null) {
/*  204 */             this.villager.homeVillage.economy.getItemPrice(item.getDisplayName()).increaseSupply(item.stackSize);
/*  205 */             this.villager.homeVillage.economy.itemSyncServer(this.villager, item);
/*  206 */             sellItemInv.setInventorySlotContents(0, null);
/*      */           } else {
/*  208 */             this.villager.homeVillage.economy.accountWithdraw(this.player, amount);
/*      */           } 
/*      */         } 
/*      */       } else {
/*  212 */   handleMouseClick(slot, x, y, z);
/*      */       } 
/*      */     } else {
/*  215 */       this.inventorySlots.slotClick(slot.slotNumber, x, y, this.player);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void calculateCurrencyOutput() {
/*  223 */     ItemStack item = sellItemInv.getStackInSlot(0);
/*  224 */     if (item == null) {
/*  225 */       currencyOutputInv.setInventorySlotContents(0, null);
/*      */     } else {
/*  227 */       int price = this.villager.homeVillage.economy.getPrice(item.getDisplayName()) * item.stackSize;
/*  228 */       if (price > 0 && price <= 64) {
/*  229 */         currencyOutputInv.setInventorySlotContents(0, new ItemStack(Items.emerald, price));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onGuiClosed() {
/*  239 */     HelpfulVillagers.network.sendToServer((IMessage)new PlayerInventoryPacket(this.player.getEntityId(), this.player.inventory.mainInventory, this.player.inventory.armorInventory));
/*  240 */     this.player.inventoryContainer = (Container)new ContainerPlayer(this.player.inventory, true, this.player);
/*      */     
/*  242 */     buyItemInv.setInventorySlotContents(0, null);
/*      */     
/*  244 */     ItemStack stack = currencyInputInv.getStackInSlot(0);
/*  245 */     if (stack != null) {
/*  246 */       this.player.dropPlayerItemWithRandomChoice(stack, true);
/*  	*/			this.mc.playerController.sendPacketDropItem(this.dragItem);
/*  248 */       this.player.inventory.setItemStack(null);
/*  249 */       this.dragItem = null;
/*      */     } 
/*      */     
/*  252 */     Keyboard.enableRepeatEvents(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  259 */     if (!CreativeTabs.creativeTabArray[selectedTabIndex].hasSearchBar()) {
/*  260 */       if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
/*  261 */         setCurrentCreativeTab(CreativeTabs.tabAllSearch);
/*      */       } else {
/*  263 */         super.keyTyped(typedChar, keyCode);
/*      */       } 
/*      */     } else {
/*  266 */       if (this.field_147057_D) {
/*  267 */         this.field_147057_D = false;
/*  268 */         this.searchField.setText("");
/*      */       } 
/*      */       
/*  271 */       if (!checkHotbarKeys(keyCode)) {
/*  272 */         if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
/*  273 */           updateCreativeSearch();
/*      */         } else {
/*  275 */           super.keyTyped(typedChar, keyCode);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateCreativeSearch() {
/*  285 */     ContainerBarter containercreative = (ContainerBarter)this.inventorySlots;
/*  286 */     containercreative.itemList.clear();
/*      */     
/*  288 */     CreativeTabs tab = CreativeTabs.creativeTabArray[selectedTabIndex];
/*  289 */     if (tab.hasSearchBar() && tab != CreativeTabs.tabAllSearch) {
/*  290 */       tab.displayAllReleventItems(containercreative.itemList);
/*  291 */       updateFilteredItems(containercreative);
/*      */       
/*      */       return;
/*      */     } 
/*  295 */     Iterator<Item> iterator = Item.itemRegistry.iterator();
/*      */     
/*  297 */     while (iterator.hasNext()) {
/*  298 */       Item item = iterator.next();
/*      */       
/*  300 */       if (item != null && item.getCreativeTab() != null) {
/*  301 */         item.getSubItems(item, (CreativeTabs)null, containercreative.itemList);
/*      */       }
/*      */     } 
/*  304 */     updateFilteredItems(containercreative);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateFilteredItems(ContainerBarter containercreative) {
/*      */     
/*  316 */     for (int i = 0; i < 256; i++) {
/*  317 */       Enchantment enchantment = Enchantment.getEnchantmentById(i);
/*      */       
/*  319 */       if (enchantment != null && enchantment.type != null) {
					ItemStack enchantedBook = new ItemStack(Items.enchanted_book, 1);
/*  320 */         Items.enchanted_book.getAll(enchantment, containercreative.itemList);
/*      */       } else if (enchantment == null){
					break;
					}
/*      */     } 
/*      */     
/*  324 */     Iterator<ItemStack> iterator = containercreative.itemList.iterator();
/*  325 */     String s1 = this.searchField.getText().toLowerCase();
/*      */     
/*  327 */     while (iterator.hasNext()) {
/*  328 */       ItemStack itemstack = iterator.next();
/*  329 */       boolean flag = false;
/*  330 */       Iterator<String> iterator1 = itemstack.getTooltip((EntityPlayer)this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();
/*      */ 
/*      */       
/*  333 */       while (iterator1.hasNext()) {
/*  334 */         String s = iterator1.next();
/*      */         
/*  336 */         if (!s.toLowerCase().contains(s1)) {
/*      */           continue;
/*      */         }
/*      */         
/*  340 */         flag = true;
/*      */       } 
/*      */       
/*  343 */       if (!flag) {
/*  344 */         iterator.remove();
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  351 */     this.currentScroll = 0.0F;
/*  352 */     containercreative.scrollTo(0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/*  359 */     CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
/*      */     
/*  361 */     if (creativetabs != null && creativetabs.drawInForegroundOfTab()) {
/*  362 */       GL11.glDisable(3042);
/*  363 */  	 this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8, 6, 4210752);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  371 */     if (mouseButton == 0) {
/*  372 */       int l =mouseX - this.guiLeft;
/*  373 */       int i1 = mouseY - this.guiTop;
/*  374 */       CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
/*  375 */       int j1 = acreativetabs.length;
/*      */       
/*  377 */       for (int k1 = 0; k1 < j1; k1++) {
/*  378 */         CreativeTabs creativetabs = acreativetabs[k1];
/*      */         
/*  380 */         if (creativetabs != null && func_147049_a(creativetabs, l, i1)) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  386 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/*  394 */     if (state == 0) {
/*  395 */       int l = mouseX - this.guiLeft;
/*  396 */       int i1 = mouseY - this.guiTop;
/*  397 */       CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
/*  398 */       int j1 = acreativetabs.length;
/*      */       
/*  400 */       for (int k1 = 0; k1 < j1; k1++) {
/*  401 */         CreativeTabs creativetabs = acreativetabs[k1];
/*      */         
/*  403 */         if (creativetabs != null && func_147049_a(creativetabs, l, i1)) {
/*  404 */           setCurrentCreativeTab(creativetabs);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/*  410 */     super.mouseReleased(mouseX, mouseY, state);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needsScrollBars() {
/*  417 */     if (CreativeTabs.creativeTabArray[selectedTabIndex] == null) return false; 
/*  418 */     return (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((ContainerBarter)this.inventorySlots).func_148328_e());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setCurrentCreativeTab(CreativeTabs tab) {
/*  426 */     if (tab == null)
/*  427 */       return;  int i = selectedTabIndex;
/*  428 */     selectedTabIndex = tab.getTabIndex();
/*  429 */     ContainerBarter containerbarter = (ContainerBarter)this.inventorySlots;
/*  430 */     this.dragSplittingSlots.clear();
/*  431 */     containerbarter.itemList.clear();
/*  432 */     tab.displayAllReleventItems(containerbarter.itemList);
/*      */     
/*  434 */     if (tab == CreativeTabs.tabInventory) {
/*  435 */       Container container = this.mc.thePlayer.inventoryContainer;
/*      */       
/*  437 */       if (this.field_147063_B == null) {
/*  438 */         this.field_147063_B = containerbarter.inventorySlots;
/*      */       }
/*      */       
/*  441 */       containerbarter.inventorySlots = new ArrayList();
/*      */       
/*  443 */       for (int j = 0; j < containerbarter.inventorySlots.size(); j++) {
/*  444 */         Slot creativeslot = containerbarter.inventorySlots.get(j);
/*  445 */         containerbarter.inventorySlots.add(creativeslot);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  450 */         if (j >= 5 && j < 9) {
/*  451 */           int k = j - 5;
/*  452 */           int l = k / 2;
/*  453 */           int i1 = k % 2;
/*  454 */           creativeslot.xDisplayPosition = 9 + l * 54;
/*  455 */           creativeslot.yDisplayPosition = 6 + i1 * 27;
/*  456 */         } else if (j >= 0 && j < 5) {
/*  457 */           creativeslot.yDisplayPosition = -2000;
/*  458 */           creativeslot.xDisplayPosition = -2000;
/*  459 */         } else if (j < container.inventorySlots.size()) {
/*  460 */           int k = j - 9;
/*  461 */           int l = k % 9;
/*  462 */           int i1 = k / 9;
/*  463 */           creativeslot.xDisplayPosition = 9 + l * 18;
/*      */           
/*  465 */           if (j >= 36) {
/*  466 */             creativeslot.yDisplayPosition = 112;
/*      */           } else {
/*  468 */             creativeslot.yDisplayPosition = 54 + i1 * 18;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  473 */       this.sellItem = new Slot((IInventory)sellItemInv, 0, 99, 12);
/*  474 */       containerbarter.inventorySlots.add(this.sellItem);
/*      */       
/*  476 */       this.currencyOutput = new Slot((IInventory)currencyOutputInv, 0, 153, 12);
/*  477 */       containerbarter.inventorySlots.add(this.currencyOutput);
/*      */     }
/*  479 */     else if (i == CreativeTabs.tabInventory.getTabIndex()) {
/*  480 */       containerbarter.inventorySlots = this.field_147063_B;
/*  481 */       this.field_147063_B = null;
/*      */     } 
/*      */     
/*  484 */     if (this.searchField != null) {
/*  485 */       if (tab.hasSearchBar()) {
/*  486 */         this.searchField.setVisible(true);
/*  487 */         this.searchField.setCanLoseFocus(false);
/*  488 */         this.searchField.setFocused(true);
/*  489 */         this.searchField.setText("");
/*  490 */         updateCreativeSearch();
/*      */       } else {
/*  492 */         this.searchField.setVisible(false);
/*  493 */         this.searchField.setCanLoseFocus(true);
/*  494 */         this.searchField.setFocused(false);
/*      */       } 
/*      */     }
/*      */     
/*  498 */     this.currentScroll = 0.0F;
/*  499 */     containerbarter.scrollTo(0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleMouseInput() throws IOException {
/*  506 */     super.handleMouseInput();
/*  507 */     int i = Mouse.getEventDWheel();
/*      */     
/*  509 */     if (i != 0 && needsScrollBars()) {
/*  510 */       int j = ((ContainerBarter)this.inventorySlots).itemList.size() / 9 - 5 + 1;
/*      */       
/*  512 */       if (i > 0) {
/*  513 */         i = 1;
/*      */       }
/*      */       
/*  516 */       if (i < 0) {
/*  517 */         i = -1;
/*      */       }
/*      */       
/*  520 */       this.currentScroll = (float)(this.currentScroll - i / j);
/*      */       
/*  522 */       if (this.currentScroll < 0.0F) {
/*  523 */         this.currentScroll = 0.0F;
/*      */       }
/*      */       
/*  526 */       if (this.currentScroll > 1.0F) {
/*  527 */         this.currentScroll = 1.0F;
/*      */       }
/*      */       
/*  530 */       ((ContainerBarter)this.inventorySlots).scrollTo(this.currentScroll);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawScreen(int x, int y, float f) {
/*  538 */     if (this.villager.homeVillage.economy == null || !this.villager.homeVillage.pricesCalculated || this.villager.homeVillage.economy.getItemPrices().isEmpty()) {
/*  539 */       int xSizeOfTexture = 140;
/*  540 */       int ySizeOfTexture = 40;
/*      */       
/*  542 */       drawDefaultBackground();
/*      */       
/*  544 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  545 */     mc.renderEngine.bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/dialog_background.png"));
/*      */       
/*  547 */       int posX = (this.width - xSizeOfTexture) / 2;
/*  548 */       int posY = (this.height - ySizeOfTexture) / 2;
/*      */       
/*  550 */       drawString(this.fontRendererObj, "Calculating Village Prices...", posX + 5, posY + 15, 16777215);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  555 */       boolean flag = Mouse.isButtonDown(0);
/*  556 */       int k = this.guiLeft;
/*  557 */       int l = this.guiTop;
/*  558 */       int i1 = k + 175;
/*  559 */       int j1 = l + 18;
/*  560 */       int k1 = i1 + 14;
/*  561 */       int l1 = j1 + 112;
/*      */       
/*  563 */       if (!this.wasClicking && flag && x >= i1 && y >= j1 && x < k1 && y < l1) {
/*  564 */         this.isScrolling = needsScrollBars();
/*      */       }
/*      */       
/*  567 */       if (!flag) {
/*  568 */         this.isScrolling = false;
/*      */       }
/*      */       
/*  571 */       this.wasClicking = flag;
/*      */       
/*  573 */       if (this.isScrolling) {
/*  574 */         this.currentScroll = ((y - j1) - 7.5F) / ((l1 - j1) - 15.0F);
/*      */         
/*  576 */         if (this.currentScroll < 0.0F) {
/*  577 */           this.currentScroll = 0.0F;
/*      */         }
/*      */         
/*  580 */         if (this.currentScroll > 1.0F) {
/*  581 */           this.currentScroll = 1.0F;
/*      */         }
/*      */         
/*  584 */         ((ContainerBarter)this.inventorySlots).scrollTo(this.currentScroll);
/*      */       } 
/*      */       
/*  587 */       super.drawScreen(x, y, f);
/*  588 */       CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
/*  589 */       int start = tabPage * 10;
/*  590 */       int i2 = Math.min(acreativetabs.length, (tabPage + 1) * 10 + 2);
/*  591 */       if (tabPage != 0) start += 2; 
/*  592 */       boolean rendered = false;
/*      */       
/*  594 */       for (int j2 = start; j2 < i2; j2++) {
/*  595 */         CreativeTabs creativetabs = acreativetabs[j2];
/*      */         
/*  597 */         if (creativetabs != null && renderCreativeInventoryHoveringText(creativetabs, x, y)) {
/*  598 */           rendered = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  603 */       if (!rendered && renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, x, y)) {
/*  604 */         renderCreativeInventoryHoveringText(CreativeTabs.tabInventory, x, y);
/*      */       }
/*      */       
/*  607 */       if (this.maxPages != 0) {
/*  608 */         String page = String.format("%d / %d", new Object[] { Integer.valueOf(tabPage + 1), Integer.valueOf(this.maxPages + 1) });
/*  609 */         int width = this.fontRendererObj.getStringWidth(page);
/*  610 */         GL11.glDisable(2896);
/*  611 */         this.zLevel = 300.0F;
/*  612 */         itemRender.zLevel = 300.0F;
/*  613 */         this.fontRendererObj.drawString(page, this.guiLeft + this.xSize / 2 - width / 2, this.guiTop - 44, -1);
/*  614 */         this.zLevel = 0.0F;
/*  615 */         itemRender.zLevel = 0.0F;
/*      */       } 
/*      */       
/*  618 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  619 */       GL11.glDisable(2896);
/*      */     } 
/*      */     
/*  622 */     drawString(this.fontRendererObj, "Credits: $" + this.villager.homeVillage.economy.getAccount(this.player), this.guiLeft, this.guiTop + 165, 16777215);
/*      */     
/*  624 */     if (this.creditBuyButton.visible) {
/*  625 */       int price = -1;
/*  626 */       if (buyItemInv.getStackInSlot(0) != null) {
/*  627 */         ItemStack item = buyItemInv.getStackInSlot(0);
/*  628 */         price = this.villager.homeVillage.economy.getPrice(item.getDisplayName()) * item.stackSize;
/*      */         
/*  630 */         drawString(this.fontRendererObj, "$" + price, this.guiLeft + 145, this.guiTop + 115, 16777215);
/*      */         
/*  632 */         if (price >= 0) {
/*  633 */           if (this.villager.homeVillage.economy.getAccount(this.player) >= price) {
/*  634 */             this.creditBuyButton.enabled = true;
/*      */           } else {
/*  636 */             this.creditBuyButton.enabled = false;
/*  637 */             if (this.creditBuyButton.isMouseOver()) {
/*  638 */               String[] desc = { "Insufficient Credits" };
/*      */               
/*  640 */               List<String> temp = Arrays.asList(desc);
/*  641 */               drawHoveringText(temp, x, y, this.fontRendererObj);
/*      */             } 
/*      */           } 
/*      */         } else {
/*  645 */           this.creditBuyButton.enabled = false;
/*  646 */           if (this.creditBuyButton.isMouseOver()) {
/*  647 */             String[] desc = { "ERROR: Invalid Item Price" };
/*      */             
/*  649 */             List<String> temp = Arrays.asList(desc);
/*  650 */             drawHoveringText(temp, x, y, this.fontRendererObj);
/*      */           } 
/*      */         } 
/*      */       } else {
/*  654 */         this.creditBuyButton.enabled = false;
/*  655 */         if (this.creditBuyButton.isMouseOver()) {
/*  656 */           String[] desc = { "No Item Selected" };
/*      */           
/*  658 */           List<String> temp = Arrays.asList(desc);
/*  659 */           drawHoveringText(temp, x, y, this.fontRendererObj);
/*      */         } 
/*      */       } 
/*      */     } else {
/*  663 */       this.creditBuyButton.enabled = false;
/*      */     } 
/*      */     
/*  666 */     this.creditSellButton.displayString = "Sell";
/*  667 */     if (this.creditSellButton.visible) {
/*  668 */       if (sellItemInv.getStackInSlot(0) != null) {
/*  669 */         if (sellItemInv.getStackInSlot(0).getItem().equals(Items.emerald)) {
/*  670 */           drawString(this.fontRendererObj, "$" + (sellItemInv.getStackInSlot(0)).stackSize, this.guiLeft + 175, this.guiTop + 15, 16777215);
/*  671 */           this.creditSellButton.enabled = true;
/*  672 */           this.creditSellButton.displayString = "Deposit";
/*  673 */         } else if (this.villager.homeVillage.economy.getPrice(sellItemInv.getStackInSlot(0).getDisplayName()) > 0) {
/*  674 */           int price = this.villager.homeVillage.economy.getPrice(sellItemInv.getStackInSlot(0).getDisplayName()) * (sellItemInv.getStackInSlot(0)).stackSize;
/*  675 */           drawString(this.fontRendererObj, "$" + price, this.guiLeft + 175, this.guiTop + 15, 16777215);
/*  676 */           this.creditSellButton.enabled = true;
/*      */         } else {
/*  678 */           this.creditSellButton.enabled = false;
/*  679 */           if (this.creditSellButton.isMouseOver()) {
/*  680 */             String[] desc = { "Item Has No Price" };
/*      */             
/*  682 */             List<String> temp = Arrays.asList(desc);
/*  683 */             drawHoveringText(temp, x, y, this.fontRendererObj);
/*      */           } 
/*      */         } 
/*      */       } else {
/*  687 */         this.creditSellButton.enabled = false;
/*  688 */         if (this.creditSellButton.isMouseOver()) {
/*  689 */           String[] desc = { "No Item Selected" };
/*      */           
/*  691 */           List<String> temp = Arrays.asList(desc);
/*  692 */           drawHoveringText(temp, x, y, this.fontRendererObj);
/*      */         } 
/*      */       } 
/*      */       
/*  696 */       if (this.villager.homeVillage.economy.getAccount(this.player) > 0) {
/*  697 */         if (!this.creditSellButton.enabled && sellItemInv.getStackInSlot(0) == null) {
/*  698 */           this.creditWithdrawButton.enabled = true;
/*      */         } else {
/*  700 */           this.creditWithdrawButton.enabled = false;
/*  701 */           if (this.creditWithdrawButton.isMouseOver()) {
/*  702 */             String[] desc = { "Sell Item Selected" };
/*      */             
/*  704 */             List<String> temp = Arrays.asList(desc);
/*  705 */             drawHoveringText(temp, x, y, this.fontRendererObj);
/*      */           } 
/*      */         } 
/*      */       } else {
/*  709 */         this.creditWithdrawButton.enabled = false;
/*  710 */         if (this.creditWithdrawButton.isMouseOver()) {
/*  711 */           String[] desc = { "Insufficient Credits" };
/*      */           
/*  713 */           List<String> temp = Arrays.asList(desc);
/*  714 */           drawHoveringText(temp, x, y, this.fontRendererObj);
/*      */         } 
/*      */       } 
/*      */     } else {
/*  718 */       this.creditSellButton.enabled = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void renderToolTip(ItemStack inputItem, int x, int y) {
/*  729 */     if (this.villager.homeVillage != null && this.villager.homeVillage.economy != null && !this.villager.homeVillage.economy.getItemPrices().isEmpty()) {
/*  730 */       int price = this.villager.homeVillage.economy.getPrice(inputItem.getDisplayName());
/*  731 */       if (price > 0) {
/*  732 */         List<String> list = new ArrayList();
/*  733 */         list.add("$" + price + " - " + inputItem.getDisplayName());
/*  734 */       drawHoveringText(list, x, y);
/*      */       } else {
/*  736 */         super.renderToolTip(inputItem, x, y);
/*      */       } 
/*      */     } else {
/*  739 */       super.renderToolTip(inputItem, x, y);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/*  747 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  748 */     RenderHelper.enableGUIStandardItemLighting();
/*  749 */     CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
/*  750 */     CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
/*  751 */     int k = acreativetabs.length;
/*      */ 
/*      */     
/*  754 */     int start = tabPage * 10;
/*  755 */     k = Math.min(acreativetabs.length, (tabPage + 1) * 10 + 2);
/*  756 */     if (tabPage != 0) start += 2; 
/*      */     int l;
/*  758 */     for (l = start; l < k; l++) {
/*  759 */       CreativeTabs creativetabs1 = acreativetabs[l];
/*  760 */       this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
/*      */       
/*  762 */       if (creativetabs1 != null)
/*      */       {
/*  764 */         if (creativetabs1.getTabIndex() != selectedTabIndex) {
/*  765 */           func_147051_a(creativetabs1);
/*      */         }
/*      */       }
/*      */     } 
/*  769 */     if (tabPage != 0) {
/*  770 */       if (creativetabs != CreativeTabs.tabAllSearch) {
/*  771 */         this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
/*  772 */         func_147051_a(CreativeTabs.tabAllSearch);
/*      */       } 
/*      */       
/*  775 */       if (creativetabs != CreativeTabs.tabInventory) {
/*  776 */         this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
/*  777 */         func_147051_a(CreativeTabs.tabInventory);
/*      */       } 
/*      */     } 
/*      */     
/*  781 */     this.mc.getTextureManager().bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/barter_inventory/tab_" + creativetabs.getBackgroundImageName()));
/*  7	*/		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
/*  783 */     this.searchField.drawTextBox();
/*  784 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  785 */     int i1 = this.guiLeft + 175;
/*  786 */     k = this.guiTop + 18;
/*  787 */     l = k + 112;
/*  788 */     this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
/*      */     
/*  790 */     if (creativetabs.shouldHidePlayerInventory()) {
/*  	*/		drawTexturedModalRect(i1, k + (int)((l - k - 17) * this.currentScroll), 232 + (needsScrollBars() ? 0 : 12), 0, 12, 15);
/*      */     }
/*      */     
/*  794 */     if ((creativetabs == null || creativetabs.getTabPage() != tabPage) && 
/*  795 */       creativetabs != CreativeTabs.tabAllSearch && creativetabs != CreativeTabs.tabInventory) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  800 */     func_147051_a(creativetabs);
/*      */     
/*  802 */     if (creativetabs == CreativeTabs.tabInventory)
/*  803 */       GuiInventory.drawEntityOnScreen(guiLeft + 43, this.guiTop + 45, 20, (this.guiLeft + 43 - mouseX), (this.guiTop + 45 - 30 - mouseY), (EntityLivingBase)this.mc.thePlayer); 
/*      */   }
/*      */   
/*      */   protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_) {
/*      */     int i1;
/*  808 */     if (p_147049_1_.getTabPage() != tabPage && 
/*  809 */       p_147049_1_ != CreativeTabs.tabAllSearch && p_147049_1_ != CreativeTabs.tabInventory) {
/*  810 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  814 */     int k = p_147049_1_.getTabColumn();
/*  815 */     int l = 28 * k;
/*  816 */     byte b0 = 0;
/*      */     
/*  818 */     if (k == 5) {
/*  819 */       l = this.xSize - 28 + 2;
/*  820 */     } else if (k > 0) {
/*  821 */       l += k;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  826 */     if (p_147049_1_.isTabInFirstRow()) {
/*  827 */       i1 = b0 - 32;
/*      */     } else {
/*  829 */       i1 = b0 + this.ySize;
/*      */     } 
/*      */     
/*  832 */     return (p_147049_2_ >= l && p_147049_2_ <= l + 28 && p_147049_3_ >= i1 && p_147049_3_ <= i1 + 32);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean renderCreativeInventoryHoveringText(CreativeTabs p_147052_1_, int p_147052_2_, int p_147052_3_) {
/*  840 */     int i1, k = p_147052_1_.getTabColumn();
/*  841 */     int l = 28 * k;
/*  842 */     byte b0 = 0;
/*      */     
/*  844 */     if (k == 5) {
/*  845 */       l = this.xSize - 28 + 2;
/*  846 */     } else if (k > 0) {
/*  847 */       l += k;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  852 */     if (p_147052_1_.isTabInFirstRow()) {
/*  853 */       i1 = b0 - 32;
/*      */     } else {
/*  855 */       i1 = b0 + this.ySize;
/*      */     } 
/*      */     
/*  858 */     if (isPointInRegion(l + 3, i1 + 3, 23, 27, p_147052_2_, p_147052_3_)) {
/*  859 */     drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
/*  860 */       return true;
/*      */     } 
/*  862 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void func_147051_a(CreativeTabs p_147051_1_) {
/*  867 */     boolean flag = (p_147051_1_.getTabIndex() == selectedTabIndex);
/*  868 */     boolean flag1 = p_147051_1_.isTabInFirstRow();
/*  869 */     int i = p_147051_1_.getTabColumn();
/*  870 */     int j = i * 28;
/*  871 */     int k = 0;
/*  872 */     int l = this.guiLeft + 28 * i;
/*  873 */     int i1 = this.guiTop;
/*  874 */     byte b0 = 32;
/*      */     
/*  876 */     if (flag) {
/*  877 */       k += 32;
/*      */     }
/*      */     
/*  880 */     if (i == 5) {
/*  881 */       l = this.guiLeft + this.xSize - 28;
/*  882 */     } else if (i > 0) {
/*  883 */       l += i;
/*      */     } 
/*      */     
/*  886 */     if (flag1) {
/*  887 */       i1 -= 28;
/*      */     } else {
/*  889 */       k += 64;
/*  890 */       i1 += this.ySize - 4;
/*      */     } 
/*      */     
/*  893 */     GL11.glDisable(2896);
/*  894 */     GL11.glColor3f(1.0F, 1.0F, 1.0F);
/*  895 */     GL11.glEnable(3042);
/*  896 */     drawTexturedModalRect(l, i1, j, k, 28, b0);
/*  897 */ 		this.zLevel = 100.0F;
/*  898 */     itemRender.zLevel = 100.0F;
/*  899 */     l += 6;
/*  900 */     i1 += 8 + (flag1 ? 1 : -1);
/*  901 */     GL11.glEnable(2896);
/*  902 */     GL11.glEnable(32826);
/*  903 */     ItemStack itemstack = p_147051_1_.getIconItemStack();
/*  904 */  	//itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
/*  905 */ 		//itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, l, i1);
/*  906 */     GL11.glDisable(2896);
/*  907 */		itemRender.zLevel = 0.0F;
/*  908 */     this.zLevel = 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void actionPerformed(GuiButton button) {
/*  916 */     if (button.id == 0) {
/*  917 */       this.mc.displayGuiScreen((GuiScreen)new GuiAchievements((GuiScreen)this, this.mc.thePlayer.getStatFileWriter()));
/*      */     }
/*      */     
/*  920 */     if (button.id == 1) {
/*  921 */       this.mc.displayGuiScreen((GuiScreen)new GuiStats((GuiScreen)this, this.mc.thePlayer.getStatFileWriter()));
/*      */     }
/*      */     
/*  924 */     if (button.id == 101) {
/*  925 */       tabPage = Math.max(tabPage - 1, 0);
/*  926 */     } else if (button.id == 102) {
/*  927 */       tabPage = Math.min(tabPage + 1, this.maxPages);
/*      */     } 
/*      */ 
/*      */     
/*  931 */     if (button.id == 10) {
/*  932 */       InventoryPlayer inventoryplayer = this.player.inventory;
/*  933 */       ItemStack buyItem = buyItemInv.getStackInSlot(0);
/*  934 */       if (buyItem != null) {
/*  935 */         inventoryplayer.setItemStack(buyItem);
/*  936 */         buyItemInv.setInventorySlotContents(0, null);
/*  937 */         int amount = this.villager.homeVillage.economy.getPrice(buyItem.getDisplayName()) * buyItem.stackSize;
/*  938 */         this.villager.homeVillage.economy.accountWithdraw(this.player, amount);
/*  939 */         HelpfulVillagers.network.sendToServer((IMessage)new PlayerAccountServerPacket(this.player, this.villager));
/*  940 */         this.villager.homeVillage.economy.getItemPrice(buyItem.getDisplayName()).increaseDemand(buyItem.stackSize);
/*  941 */         this.villager.homeVillage.economy.itemSyncServer(this.villager, buyItem);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  946 */     if (button.id == 11) {
/*  947 */       ItemStack sellItem = sellItemInv.getStackInSlot(0);
/*  948 */       if (sellItem != null) {
/*  949 */         if (this.creditSellButton.displayString.equals("Deposit")) {
/*  950 */           int amount = sellItem.stackSize;
/*  951 */           this.villager.homeVillage.economy.accountDeposit(this.player, amount);
/*  952 */           sellItemInv.setInventorySlotContents(0, null);
/*  953 */         } else if (this.creditSellButton.displayString.equals("Sell")) {
/*  954 */           ItemStack currencyOutput = currencyOutputInv.getStackInSlot(0);
/*  955 */           if (currencyOutput != null) {
/*  956 */             int amount = currencyOutput.stackSize;
/*  957 */             this.villager.homeVillage.economy.accountDeposit(this.player, amount);
/*  958 */             currencyOutputInv.setInventorySlotContents(0, null);
/*  959 */             sellItemInv.setInventorySlotContents(0, null);
/*      */           } else {
/*  961 */             int amount = this.villager.homeVillage.economy.getPrice(sellItem.getDisplayName()) * sellItem.stackSize;
/*  962 */             this.villager.homeVillage.economy.accountDeposit(this.player, amount);
/*  963 */             sellItemInv.setInventorySlotContents(0, null);
/*      */           } 
/*  965 */           this.villager.homeVillage.economy.getItemPrice(sellItem.getDisplayName()).increaseSupply(sellItem.stackSize);
/*  966 */           this.villager.homeVillage.economy.itemSyncServer(this.villager, sellItem);
/*      */         } 
/*      */       }
/*  969 */       HelpfulVillagers.network.sendToServer((IMessage)new PlayerAccountServerPacket(this.player, this.villager));
/*      */     } 
/*      */ 
/*      */     
/*  973 */     if (button.id == 12) {
/*  974 */       if (this.currencyOutput.getHasStack() && (this.currencyOutput.getStack()).stackSize < this.currencyOutput.getStack().getMaxStackSize() && (this.currencyOutput.getStack()).stackSize < this.villager.homeVillage.economy.getAccount(this.player)) {
/*  975 */         (this.currencyOutput.getStack()).stackSize++;
/*  976 */       } else if (!this.currencyOutput.getHasStack()) {
/*  977 */         this.currencyOutput.putStack(new ItemStack(Items.emerald));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int func_147056_g() {
/*  984 */     return selectedTabIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   static class ContainerBarter
/*      */     extends Container
/*      */   {
/*  995 */     public List itemList = new ArrayList();
/*      */     
/*      */     private static final String __OBFID = "CL_00000753";
/*      */     private AbstractVillager villager;
/*      */     private EntityPlayer player;
/*      */     
/*      */     public ContainerBarter(EntityPlayer player, AbstractVillager villager) {
/* 1002 */       this.villager = villager;
/* 1003 */       this.player = player;
/* 1004 */       InventoryPlayer inventoryplayer = player.inventory;
/*      */       
/* 1006 */       addSlotToContainer(new Slot((IInventory)GuiBarter.buyItemInv, 0, 117, 113));
/*      */       
/* 1008 */ addSlotToContainer(new Slot((IInventory)GuiBarter.currencyInputInv, 0, 63, 113));
/*      */       
/* 1010 */       for (int i = 0; i < 5; i++) {
/* 1011 */         for (int j = 0; j < 9; j++) {
/* 1012 */     addSlotToContainer(new Slot((IInventory)GuiBarter.barterItems, i * 9 + j, 9 + j * 18, 18 + i * 18));
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1017 */       scrollTo(0.0F);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canInteractWith(EntityPlayer player) {
/* 1024 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void scrollTo(float position) {
/* 1032 */       Iterator<ItemStack> iterator = this.itemList.iterator();
/* 1033 */       while (iterator.hasNext()) {
/* 1034 */         ItemStack item = iterator.next();
/* 1035 */         if (!this.villager.homeVillage.economy.hasItem(item) || this.villager.homeVillage.economy.getPrice(item.getDisplayName()) < 0) {
/* 1036 */           iterator.remove();
/*      */         }
/*      */       } 
/*      */       
/* 1040 */       int i = this.itemList.size() / 9 - 5 + 1;
/* 1041 */       int j = (int)((position * i) + 0.5D);
/*      */       
/* 1043 */       if (j < 0) {
/* 1044 */         j = 0;
/*      */       }
/*      */       
/* 1047 */       for (int k = 0; k < 5; k++) {
/* 1048 */         for (int l = 0; l < 9; l++) {
/* 1049 */           int i1 = l + (k + j) * 9;
/*      */           
/* 1051 */           if (i1 >= 0 && i1 < this.itemList.size()) {
/* 1052 */             GuiBarter.barterItems.setInventorySlotContents(l + k * 9, (ItemStack) this.itemList.get(i1));
/*      */           } else {
/* 1054 */             GuiBarter.barterItems.setInventorySlotContents(l + k * 9, (ItemStack)null);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean func_148328_e() {
/* 1061 */       return (this.itemList.size() > 45);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ItemStack slotClick(int x, int y, int z, EntityPlayer player) {
/* 1070 */       InventoryPlayer inventoryplayer = player.inventory;
/*      */       try {
/* 1072 */         Slot slot = getSlot(x);
/*      */ 
/*      */         
/* 1075 */         if (slot.inventory.getName().equals(GuiBarter.barterItems.getName())) {
/* 1076 */           if (GuiBarter.buyItemInv.getStackInSlot(0) != null && GuiBarter.buyItemInv.getStackInSlot(0).getDisplayName().equals(slot.getStack().getDisplayName())) {
/* 1077 */             ItemStack selectedStack = GuiBarter.buyItemInv.getStackInSlot(0);
/* 1078 */             if (selectedStack.stackSize < selectedStack.getMaxStackSize()) {
/* 1079 */            putStackInSlot(0, new ItemStack(selectedStack.getItem(), selectedStack.stackSize + 1, selectedStack.getMetadata()));
/*      */             }
/*      */           } else {
/* 1082 */             putStackInSlot(0, new ItemStack(slot.getStack().getItem(), 1, slot.getStack().getMetadata()));
/*      */           } 
/*      */         } else {
/* 1085 */           if (slot.inventory.equals(player.inventory)) {
/* 1086 */             return super.slotClick(x, y, z, player);
/*      */           }
/*      */           
/* 1089 */           if (slot.inventory.equals(GuiBarter.currencyInputInv)) {
/* 1090 */             if (slot.getHasStack()) {
/* 1091 */               if (inventoryplayer.getItemStack() == null) {
/* 1092 */                 ItemStack temp = slot.getStack().copy();
/* 1093 */                 inventoryplayer.setItemStack(temp);
/* 1094 */                 HelpfulVillagers.network.sendToServer((IMessage)new PlayerItemStackPacket(player.getEntityId(), temp));
/* 1095 */                 slot.putStack(null);
/*      */               }
/*      */             
/* 1098 */             } else if (inventoryplayer.getItemStack() != null) {
/* 1099 */               ItemStack temp = inventoryplayer.getItemStack().copy();
/* 1100 */               slot.putStack(temp);
/* 1101 */               inventoryplayer.setItemStack(null);
/* 1102 */               HelpfulVillagers.network.sendToServer((IMessage)new PlayerItemStackPacket(player.getEntityId(), null));
/*      */             } 
/* 1104 */           } else if (slot.inventory.equals(GuiBarter.buyItemInv)) {
/* 1105 */             int price = this.villager.homeVillage.economy.getPrice(GuiBarter.buyItemInv.getStackInSlot(0).getDisplayName());
/* 1106 */             price *= (GuiBarter.buyItemInv.getStackInSlot(0)).stackSize;
/* 1107 */             if (GuiBarter.currencyInputInv.getStackInSlot(0).getItem().equals(Items.emerald) && (GuiBarter.currencyInputInv.getStackInSlot(0)).stackSize >= price) {
/* 1108 */               int size = (GuiBarter.currencyInputInv.getStackInSlot(0)).stackSize - price;
/* 1109 */               ItemStack currency = GuiBarter.currencyInputInv.getStackInSlot(0).copy();
/* 1110 */               if (size <= 0) {
/* 1111 */                 putStackInSlot(1, null);
/*      */               } else {
/* 1113 */               putStackInSlot(1, new ItemStack(currency.getItem(), size, currency.getMetadata()));
/*      */               } 
/* 1115 */               this.villager.homeVillage.economy.getItemPrice(GuiBarter.buyItemInv.getStackInSlot(0).getDisplayName()).increaseDemand((GuiBarter.buyItemInv.getStackInSlot(0)).stackSize);
/* 1116 */               this.villager.homeVillage.economy.itemSyncServer(this.villager, GuiBarter.buyItemInv.getStackInSlot(0));
/* 1117 */               super.slotClick(x, y, z, player);
/*      */             } 
/*      */           } 
/*      */         } 
/* 1121 */       } catch (Exception e) {}
/*      */       
/* 1123 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ItemStack transferStackInSlot(EntityPlayer player, int p_82index) {
/* 1133 */       return null;
/*      */     }
/*      */     
/*      */     public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
/* 1137 */       return (p_94530_2_.yDisplayPosition > 90);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canDragIntoSlot(Slot p_94531_1_) {
/* 1145 */       return false;
/*      */     }
/*      */     
/*      */     public void onContainerClosed(EntityPlayer player) {}
/*      */   }
/*      */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\gui\GuiBarter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */