/*     */ package mods.helpfulvillagers.main;
/*     */ 
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.common.network.FMLNetworkEvent;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
import java.util.ListIterator;

/*     */ import mods.helpfulvillagers.econ.VillageEconomy;
/*     */ import mods.helpfulvillagers.entity.EntityRegularVillager;
/*     */ import mods.helpfulvillagers.village.HelpfulVillage;
/*     */ import mods.helpfulvillagers.village.HelpfulVillageCollection;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.event.entity.EntityJoinWorldEvent;
/*     */ import net.minecraftforge.event.world.WorldEvent;
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
/*     */ public class CommonHooks
/*     */ {
/*     */   private static final int VILLAGE_UPDATE = 1200;
/*  46 */   public static int villageTicks = 1200;
/*  47 */   private int prevFrameSize = 0;
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
/*     */   @SubscribeEvent
/*     */   public void entityJoinedWorldEventHandler(EntityJoinWorldEvent event) throws UnsupportedEncodingException {
/*  61 */     if (!event.world.isRemote) {
/*  62 */       if (event.entity.getClass() == EntityVillager.class) {
/*  63 */         EntityVillager villager = (EntityVillager)event.entity;
/*     */         
/*  65 */         if (villager.getProfession() > -1 && villager.getProfession() < 6) {
/*  66 */           if (villager.isChild()) {
/*  67 */             EntityRegularVillager newVillager = new EntityRegularVillager(event.world);
/*  68 */             newVillager.setPositionAndRotation(villager.posX, villager.posY, villager.posZ, villager.rotationYaw, villager.rotationPitch);
/*  69 */             newVillager.setGrowingAge(villager.getGrowingAge());
/*  70 */             event.setCanceled(true);
/*  71 */             villager.setDead();
/*  72 */             event.world.spawnEntityInWorld((Entity)newVillager);
/*     */           } else {
/*  74 */             EntityRegularVillager newVillager = new EntityRegularVillager(event.world);
/*  75 */             event.setCanceled(true);
/*  76 */             newVillager.setPositionAndRotation(villager.posX, villager.posY, villager.posZ, villager.rotationYaw, villager.rotationPitch);
/*  77 */             villager.setDead();
/*  78 */             event.world.spawnEntityInWorld((Entity)newVillager);
/*     */           } 
/*     */         }
/*  81 */       } else if (event.entity.getClass() == EntityItemFrame.class) {
/*  82 */         EntityItemFrame itemFrame = (EntityItemFrame)event.entity;
/*  83 */         if (itemFrame.getDisplayedItem() != null) {
/*  84 */           villageTicks = 1200;
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
/*  99 */       Iterator<EntityItemFrame> iterator = HelpfulVillagers.checkedFrames.iterator();
/* 100 */       while (iterator.hasNext()) {
/* 101 */         EntityItemFrame frame = iterator.next();
/* 102 */         if (frame == null || !frame.isEntityAlive() || frame.getDisplayedItem() == null) {
/* 103 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 108 */       if (HelpfulVillagers.checkedFrames.size() != this.prevFrameSize) {
/* 109 */         villageTicks = 1200;
/* 110 */         this.prevFrameSize = HelpfulVillagers.checkedFrames.size();
/*     */       } 
/*     */ 
/*     */       
/* 114 */       for (int i = 0; i < HelpfulVillagers.villages.size(); i++) {
/* 115 */         World world = ((HelpfulVillage)HelpfulVillagers.villages.get(i)).world;
/* 116 */         if (this.dayReset && world.isDaytime()) {
/* 117 */           villageTicks = 1200;
/* 118 */           this.dayReset = false; break;
/*     */         } 
/* 120 */         if (!this.dayReset && !world.isDaytime()) {
/* 121 */           this.dayReset = true;
/*     */         }
/*     */         
/* 124 */         if (this.nightReset && !world.isDaytime()) {
/* 125 */           villageTicks = 1200;
/* 126 */           this.nightReset = false; break;
/*     */         } 
/* 128 */         if (!this.nightReset && world.isDaytime()) {
/* 129 */           this.nightReset = true;
/*     */         }
/*     */       } 
/*     */       
/* 133 */       if (villageTicks >= 1200) {
/* 134 */         ArrayList<Integer> removeVillages = new ArrayList<Integer>();
/*     */ 
/*     */ 
/*     */         
/* 138 */         ListIterator literator = (ListIterator)HelpfulVillagers.villages.iterator();
/* 139 */         while (iterator.hasNext()) {
/* 140 */           HelpfulVillage village = (HelpfulVillage)literator.next();
/* 141 */           if (village.isAnnihilated || (village.isFullyLoaded() && village.getPopulation() <= 0 && village.getTotalAdded() >= village.getTotalVillagers())) {
/* 142 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */         
/*     */         int j;
/* 147 */         for (j = 0; j < HelpfulVillagers.villages.size(); j++) {
/* 148 */           for (int k = 0; k < HelpfulVillagers.villages.size(); k++) {
/* 149 */             if (j != k && 
/* 150 */               !removeVillages.contains(Integer.valueOf(j))) {
/* 151 */               HelpfulVillage currentVillage = HelpfulVillagers.villages.get(j);
/* 152 */               HelpfulVillage otherVillage = HelpfulVillagers.villages.get(k);
/* 153 */               if (currentVillage.actualBounds.intersectsWith(otherVillage.actualBounds)) {
/* 154 */                 currentVillage.mergeVillage(otherVillage);
/* 155 */                 removeVillages.add(Integer.valueOf(k));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 163 */         for (j = 0; j < removeVillages.size(); j++) {
/* 164 */           int removeIndex = ((Integer)removeVillages.get(j)).intValue();
/* 165 */           HelpfulVillagers.villages.remove(removeIndex);
/*     */         } 
/* 167 */         removeVillages.clear();
/*     */         
/* 169 */         for (j = 0; j < HelpfulVillagers.villages.size(); j++) {
/* 170 */           ((HelpfulVillage)HelpfulVillagers.villages.get(j)).updateVillageBox();
/* 171 */           ((HelpfulVillage)HelpfulVillagers.villages.get(j)).findHalls();
/* 172 */           ((HelpfulVillage)HelpfulVillagers.villages.get(j)).checkHalls();
/*     */           
/* 174 */           if (!((HelpfulVillage)HelpfulVillagers.villages.get(j)).pricesCalculated && !((HelpfulVillage)HelpfulVillagers.villages.get(j)).priceCalcStarted) {
/* 175 */             ((HelpfulVillage)HelpfulVillagers.villages.get(j)).economy = new VillageEconomy(HelpfulVillagers.villages.get(j), true);
/*     */           }
/*     */           
/* 178 */           if (villageTicks == 1300) {
/* 179 */             ((HelpfulVillage)HelpfulVillagers.villages.get(j)).economy.decreaseAllDemand();
/*     */           }
/*     */         } 
/*     */         
/* 183 */         HelpfulVillagers.villageCollection.setVillages(HelpfulVillagers.villages);
/*     */         
/* 185 */         villageTicks = 0;
/*     */       } else {
/* 187 */         villageTicks++;
/*     */       } 
/* 189 */     } catch (Exception e) {
/* 190 */       e.printStackTrace();
/* 191 */       villageTicks = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void clientDisconnectEventHandler(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
/* 201 */     HelpfulVillagers.config.load();
/* 202 */     HelpfulVillagers.config.removeCategory(HelpfulVillagers.config.getCategory("general"));
/* 203 */     HelpfulVillagers.config.getInt("deathMessage", "general", HelpfulVillagers.deathMessageOption, 0, 2, "0 - Off, 1 - On, 2 - Verbose");
/* 204 */     HelpfulVillagers.config.getInt("birthMessage", "general", HelpfulVillagers.birthMessageOption, 0, 2, "0 - Off, 1 - On, 2 - Verbose");
/* 205 */     HelpfulVillagers.config.save();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void worldLoadEventHandler(WorldEvent.Load event) {
/* 215 */     if (event.world.isRemote || event.world.provider.getDimensionId() != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 219 */     HelpfulVillagers.villageCollection = HelpfulVillageCollection.forWorld(event.world);
/* 220 */     if (HelpfulVillagers.villageCollection != null && !HelpfulVillagers.villageCollection.isEmpty()) {
/* 221 */       HelpfulVillagers.villages.clear();
/* 222 */       HelpfulVillagers.villages.addAll(HelpfulVillagers.villageCollection.getVillages());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\main\CommonHooks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */