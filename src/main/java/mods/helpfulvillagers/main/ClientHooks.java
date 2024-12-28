/*     */ package mods.helpfulvillagers.main;
/*     */ 
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.PlayerEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.common.network.FMLNetworkEvent;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
import java.util.ListIterator;

/*     */ import mods.helpfulvillagers.econ.VillageEconomy;
/*     */ import mods.helpfulvillagers.entity.EntityRegularVillager;
/*     */ import mods.helpfulvillagers.network.ItemFrameEventPacket;
/*     */ import mods.helpfulvillagers.village.HelpfulVillage;
/*     */ import mods.helpfulvillagers.village.HelpfulVillageCollection;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.client.event.RenderItemInFrameEvent;
/*     */ import net.minecraftforge.event.entity.EntityJoinWorldEvent;
/*     */ import net.minecraftforge.event.world.WorldEvent;
/*     */ import org.apache.commons.io.IOUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientHooks
/*     */ {
/*  66 */   private final String UPDATE_URL = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2162777-helpful-villagers-v1-2-0-new-changes-on-the";
/*  67 */   private final String UPDATE_MESSAGE1 = "We taught your villagers some new tricks!";
/*  68 */   private final String UPDATE_MESSAGE2 = "Click §b§nhere§r to download the new update.";
/*  69 */   private final String CHECK_VERSION_URL = "http://pastebin.com/raw.php?i=DQQvyFEY";
/*     */   
/*     */   private static final int VILLAGE_UPDATE = 1200;
/*     */   
/*  73 */   public static int villageTicks = 1200;
/*  74 */   private int prevFrameSize = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean dayReset = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean nightReset = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void loginEvent(PlayerEvent.PlayerLoggedInEvent event) {
/*     */     try {
/*  91 */       if (!checkVersion()) {
/*  92 */         ClickEvent versionCheckChatClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2162777-helpful-villagers-v1-2-0-new-changes-on-the");
/*  93 */         ChatStyle clickableChatStyle = (new ChatStyle()).setChatClickEvent(versionCheckChatClickEvent);
/*  94 */         ChatComponentText versionWarningChatComponent = new ChatComponentText("Click §b§nhere§r to download the new update.");
/*  95 */         versionWarningChatComponent.setChatStyle(clickableChatStyle);
/*  96 */         event.player.addChatMessage((IChatComponent)new ChatComponentText("We taught your villagers some new tricks!"));
/*  97 */         event.player.addChatMessage((IChatComponent)versionWarningChatComponent);
/*     */       } 
/*  99 */     } catch (Exception e) {}
/*     */   }
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
/*     */   @SubscribeEvent
/*     */   public void entityJoinedWorldEventHandler(EntityJoinWorldEvent event) throws UnsupportedEncodingException {
/* 114 */     if (!event.world.isRemote) {
/* 115 */       if (event.entity.getClass() == EntityVillager.class) {
/* 116 */         EntityVillager villager = (EntityVillager)event.entity;
/*     */         
/* 118 */         if (villager.getProfession() > -1 && villager.getProfession() < 6) {
/* 119 */           if (villager.isChild()) {
/* 120 */             EntityRegularVillager newVillager = new EntityRegularVillager(event.world);
/* 121 */             newVillager.setPositionAndRotation(villager.posX, villager.posY, villager.posZ, villager.rotationYaw, villager.rotationPitch);
/* 122 */             newVillager.setGrowingAge(villager.getGrowingAge());
/* 123 */             event.setCanceled(true);
/* 124 */             villager.setDead();
/* 125 */             event.world.spawnEntityInWorld((Entity)newVillager);
/*     */           } else {
/* 127 */             EntityRegularVillager newVillager = new EntityRegularVillager(event.world);
/* 128 */             event.setCanceled(true);
/* 129 */             newVillager.setPositionAndRotation(villager.posX, villager.posY, villager.posZ, villager.rotationYaw, villager.rotationPitch);
/* 130 */             villager.setDead();
/* 131 */             event.world.spawnEntityInWorld((Entity)newVillager);
/*     */           } 
/*     */         }
/* 134 */       } else if (event.entity.getClass() == EntityItemFrame.class) {
/* 135 */         EntityItemFrame itemFrame = (EntityItemFrame)event.entity;
/* 136 */         if (itemFrame.getDisplayedItem() != null) {
/* 137 */           villageTicks = 1200;
/*     */         }
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
/*     */   @SubscribeEvent
/*     */   public void serverTickEventHandler(TickEvent.ServerTickEvent event) {
/*     */     try {
/* 152 */       Iterator<EntityItemFrame> iterator = HelpfulVillagers.checkedFrames.iterator();
/* 153 */       while (iterator.hasNext()) {
/* 154 */         EntityItemFrame frame = iterator.next();
/* 155 */         if (frame == null || !frame.isEntityAlive() || frame.getDisplayedItem() == null) {
/* 156 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 161 */       if (HelpfulVillagers.checkedFrames.size() != this.prevFrameSize) {
/* 162 */         villageTicks = 1200;
/* 163 */         this.prevFrameSize = HelpfulVillagers.checkedFrames.size();
/*     */       } 
/*     */ 
/*     */       
/* 167 */       for (int i = 0; i < HelpfulVillagers.villages.size(); i++) {
/* 168 */         World world = ((HelpfulVillage)HelpfulVillagers.villages.get(i)).world;
/* 169 */         if (this.dayReset && world.isDaytime()) {
/* 170 */           villageTicks = 1200;
/* 171 */           this.dayReset = false; break;
/*     */         } 
/* 173 */         if (!this.dayReset && !world.isDaytime()) {
/* 174 */           this.dayReset = true;
/*     */         }
/*     */         
/* 177 */         if (this.nightReset && !world.isDaytime()) {
/* 178 */           villageTicks = 1200;
/* 179 */           this.nightReset = false; break;
/*     */         } 
/* 181 */         if (!this.nightReset && world.isDaytime()) {
/* 182 */           this.nightReset = true;
/*     */         }
/*     */       } 
/*     */       
/* 186 */       if (villageTicks >= 1200) {
/* 187 */         ArrayList<Integer> removeVillages = new ArrayList<Integer>();
/*     */ 
/*     */ 
/*     */         
/* 191 */         ListIterator literator = (ListIterator)HelpfulVillagers.villages.iterator();
/* 192 */         while (iterator.hasNext()) {
/* 193 */           HelpfulVillage village = (HelpfulVillage)literator.next();
/* 194 */           if (village.isAnnihilated || (village.isFullyLoaded() && village.getPopulation() <= 0 && village.getTotalAdded() >= village.getTotalVillagers())) {
/* 195 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */         
/*     */         int j;
/* 200 */         for (j = 0; j < HelpfulVillagers.villages.size(); j++) {
/* 201 */           for (int k = 0; k < HelpfulVillagers.villages.size(); k++) {
/* 202 */             if (j != k && 
/* 203 */               !removeVillages.contains(Integer.valueOf(j))) {
/* 204 */               HelpfulVillage currentVillage = HelpfulVillagers.villages.get(j);
/* 205 */               HelpfulVillage otherVillage = HelpfulVillagers.villages.get(k);
/* 206 */               if (currentVillage.actualBounds.intersectsWith(otherVillage.actualBounds)) {
/* 207 */                 currentVillage.mergeVillage(otherVillage);
/* 208 */                 removeVillages.add(Integer.valueOf(k));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 216 */         for (j = 0; j < removeVillages.size(); j++) {
/* 217 */           int removeIndex = ((Integer)removeVillages.get(j)).intValue();
/* 218 */           HelpfulVillagers.villages.remove(removeIndex);
/*     */         } 
/* 220 */         removeVillages.clear();
/*     */         
/* 222 */         for (j = 0; j < HelpfulVillagers.villages.size(); j++) {
/* 223 */           ((HelpfulVillage)HelpfulVillagers.villages.get(j)).updateVillageBox();
/* 224 */           ((HelpfulVillage)HelpfulVillagers.villages.get(j)).findHalls();
/* 225 */           ((HelpfulVillage)HelpfulVillagers.villages.get(j)).checkHalls();
/*     */           
/* 227 */           if (!((HelpfulVillage)HelpfulVillagers.villages.get(j)).pricesCalculated && !((HelpfulVillage)HelpfulVillagers.villages.get(j)).priceCalcStarted) {
/* 228 */             ((HelpfulVillage)HelpfulVillagers.villages.get(j)).economy = new VillageEconomy(HelpfulVillagers.villages.get(j), true);
/*     */           }
/*     */           
/* 231 */           if (villageTicks == 1300) {
/* 232 */             ((HelpfulVillage)HelpfulVillagers.villages.get(j)).economy.decreaseAllDemand();
/*     */           }
/*     */         } 
/*     */         
/* 236 */         HelpfulVillagers.villageCollection.setVillages(HelpfulVillagers.villages);
/*     */         
/* 238 */         villageTicks = 0;
/*     */       } else {
/* 240 */         villageTicks++;
/*     */       } 
/* 242 */     } catch (Exception e) {
/* 243 */       e.printStackTrace();
/* 244 */       villageTicks = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void clientTickEventHandler(TickEvent.ClientTickEvent event) {
/* 255 */     Iterator<EntityItemFrame> iterator = HelpfulVillagers.checkedFrames.iterator();
/* 256 */     while (iterator.hasNext()) {
/* 257 */       EntityItemFrame frame = iterator.next();
/* 258 */       if (frame == null || !frame.isEntityAlive() || frame.getDisplayedItem() == null) {
/* 259 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void renderItemInFrameEventHandler(RenderItemInFrameEvent event) {
/* 270 */     if (event.entityItemFrame.getDisplayedItem() != null && !HelpfulVillagers.checkedFrames.contains(event.entityItemFrame)) {
/* 271 */       HelpfulVillagers.checkedFrames.add(event.entityItemFrame);
/* 272 */       HelpfulVillagers.network.sendToServer((IMessage)new ItemFrameEventPacket(event.entityItemFrame.getEntityId()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void clientDisconnectEventHandler(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
/* 282 */     HelpfulVillagers.config.load();
/* 283 */     HelpfulVillagers.config.removeCategory(HelpfulVillagers.config.getCategory("general"));
/* 284 */     HelpfulVillagers.config.getInt("deathMessage", "general", HelpfulVillagers.deathMessageOption, 0, 2, "0 - Off, 1 - On, 2 - Verbose");
/* 285 */     HelpfulVillagers.config.getInt("birthMessage", "general", HelpfulVillagers.birthMessageOption, 0, 2, "0 - Off, 1 - On, 2 - Verbose");
/* 286 */     HelpfulVillagers.config.save();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void worldLoadEventHandler(WorldEvent.Load event) {
/* 296 */     if (event.world.isRemote || event.world.provider.getDimensionId() != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 300 */     HelpfulVillagers.villageCollection = HelpfulVillageCollection.forWorld(event.world);
/* 301 */     if (HelpfulVillagers.villageCollection != null && !HelpfulVillagers.villageCollection.isEmpty()) {
/* 302 */       HelpfulVillagers.villages.clear();
/* 303 */       HelpfulVillagers.villages.addAll(HelpfulVillagers.villageCollection.getVillages());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkVersion() {
/* 314 */     String slatestVersion = "";
/*     */ 
/*     */     
/* 317 */     InputStream input = null;
/*     */     try {
/* 319 */       input = (new URL("http://pastebin.com/raw.php?i=DQQvyFEY")).openStream();
/* 320 */     } catch (MalformedURLException e) {
/* 321 */       e.printStackTrace();
/* 322 */     } catch (IOException e) {
/* 323 */       e.printStackTrace();
/*     */     } 
/*     */     
/*     */     try {
/* 327 */       slatestVersion = IOUtils.readLines(input).get(0);
/* 328 */     } catch (IOException e) {
/* 329 */       e.printStackTrace();
/*     */     } finally {
/* 331 */       IOUtils.closeQuietly(input);
/*     */     } 
/*     */     
/* 334 */     boolean bLatestVersion = "1.4.0b5".equals(slatestVersion);
/* 335 */     return bLatestVersion;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\main\ClientHooks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */