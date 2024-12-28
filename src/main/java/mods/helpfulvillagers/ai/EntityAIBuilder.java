/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntityBuilder;
/*     */ import mods.helpfulvillagers.enums.EnumConstructionType;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import mods.helpfulvillagers.util.ConstructionSite;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ 
/*     */ public class EntityAIBuilder
/*     */   extends EntityAIWorker
/*     */ {
/*     */   private EntityBuilder builder;
/*     */   
/*     */   public EntityAIBuilder(EntityBuilder builder) {
/*  19 */     super((AbstractVillager)builder);
/*  20 */     this.builder = builder;
/*  21 */     this.currentTime = 0;
/*  22 */     this.previousTime = 0;
/*  23 */     this.harvestTime = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean gather() {
/*  28 */     if (this.builder.insideHall()) {
/*  29 */       BlockPos exit = this.builder.homeGuildHall.entranceCoords;
/*  30 */       if (exit == null) {
/*  31 */         exit = AIHelper.getRandInsideCoords((AbstractVillager)this.builder);
/*     */       }
/*  33 */       this.builder.moveTo(exit, this.speed);
/*     */     }
/*  35 */     else if (this.builder.currentSite == null) {
/*  36 */       findSite();
/*     */     } else {
/*  38 */       this.target = this.builder.currentSite.getCenter();
/*  39 */       if (!this.builder.searchBox.intersectsWith(this.builder.currentSite.getBounds())) {
/*  40 */         moveToSite();
/*     */       } else {
/*  42 */         this.builder.getNavigator().clearPathEntity();
/*  43 */         work();
/*     */       } 
/*     */     } 
/*     */     
/*  47 */     return idle();
/*     */   }
/*     */   
/*     */   private void findSite() {
/*  51 */     Iterator<ConstructionSite> i = this.builder.homeVillage.constructionSites.iterator();
/*  52 */     while (i.hasNext()) {
/*  53 */       ConstructionSite site = i.next();
/*     */       
/*  55 */       if (!site.isFinished()) {
/*  56 */         this.builder.currentSite = site;
/*     */         break;
/*     */       } 
/*  59 */       i.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void moveToSite() {
/*  65 */     this.builder.moveTo(this.target, this.speed);
/*     */   }
/*     */   
/*     */   private void work() {
/*  69 */     boolean shouldSwing = false;
/*     */     
/*  71 */     if (this.builder.getNavigator().noPath()) {
/*  72 */       this.builder.getLookHelper().setLookPosition(this.target.getX(), this.target.getY(), this.target.getZ(), 10.0F, 10.0F);
/*  73 */       shouldSwing = true;
/*     */ 
/*     */       
/*  76 */       if (this.previousTime <= 0) {
/*  77 */         this.previousTime = this.builder.ticksExisted;
/*  78 */         this.harvestTime = getHarvestTime();
/*     */       } 
/*     */     } else {
/*  81 */       shouldSwing = false;
/*     */     } 
/*     */     
/*  84 */     if (this.previousTime > 0) {
/*  85 */       this.currentTime = this.builder.ticksExisted;
/*  86 */       if (!this.builder.currentSite.isFinished()) {
/*  87 */         if ((this.currentTime - this.previousTime) >= this.harvestTime) {
/*  88 */           this.previousTime = this.currentTime;
/*  89 */           this.harvestTime = getHarvestTime();
/*  90 */           this.builder.currentSite.doJob((AbstractVillager)this.builder);
/*     */         } 
/*     */       } else {
/*  93 */         this.builder.currentSite = null;
/*  94 */         this.target = null;
/*  95 */         this.previousTime = 0;
/*  96 */         this.currentTime = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 100 */     if (shouldSwing) {
/* 101 */       this.builder.swingItem();
/*     */     } else {
/* 103 */       this.previousTime = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getHarvestTime() {
/* 108 */     if (this.builder.getCurrentItem() == null) {
/* 109 */       return 45.0F;
/*     */     }
/* 111 */     if (this.builder.currentSite.getJobType() == EnumConstructionType.RECORD) {
/* 112 */       return 5.0F;
/*     */     }
/*     */     
/* 115 */     return 60.0F / this.builder.getCurrentItem().getItem().getDigSpeed(this.builder.getCurrentItem(), Blocks.dirt.getDefaultState());
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */