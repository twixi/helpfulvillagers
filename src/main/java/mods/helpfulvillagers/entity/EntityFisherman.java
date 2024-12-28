/*     */ package mods.helpfulvillagers.entity;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.ai.EntityAIFisherman;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.FishHookPacket;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ //import net.minecraft.util.IIcon;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityFisherman
/*     */   extends AbstractVillager
/*     */ {
/*     */   public EntityFishHookCustom fishEntity;
/*  37 */   public ArrayList<EntitySquid> harvestedSquids = new ArrayList<EntitySquid>();
/*     */ 
/*     */   
/*  40 */   private final ItemStack[] fishermanTools = new ItemStack[] { new ItemStack((Item)Items.fishing_rod) };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public static final ItemStack[] fishermanSmeltables = new ItemStack[] { new ItemStack(Items.cooked_fish) };
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityFisherman(World world) {
/*  50 */     super(world);
/*  51 */     init();
/*     */   }
/*     */   
/*     */   public EntityFisherman(AbstractVillager villager) {
/*  55 */     super(villager);
/*  56 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/*  63 */     this.profession = 7;
/*  64 */     this.profName = "Fisherman";
/*  65 */     this.currentActivity = EnumActivity.IDLE;
/*  66 */     this.searchRadius = 10;
/*  67 */     this.knownRecipes.addAll(HelpfulVillagers.fishermanRecipes);
/*  68 */     this.fishEntity = null;
/*  69 */     setTools(this.fishermanTools);
/*  70 */     getNewGuildHall();
/*  71 */     addThisAI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addThisAI() {
/*  78 */     ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
/*     */     
/*  80 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.30000001192092896D, 0.3499999940395355D));
/*  81 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIFisherman(this));
/*  82 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_onUpdate() {
/*  87 */     super.onUpdate();
/*  88 */     if (!this.worldObj.isRemote && 
/*  89 */       this.fishEntity != null && this.currentActivity != EnumActivity.GATHER) {
/*  90 */       this.fishEntity.setDead();
/*  91 */       this.fishEntity = null;
/*  92 */       HelpfulVillagers.network.sendToAll((IMessage)new FishHookPacket(getEntityId(), true, 0, 0, 0));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidTool(ItemStack item) {
/*  99 */     return item.getItem() instanceof net.minecraft.item.ItemFishingRod;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCraft() {
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList getValidCoords() {
/* 109 */     updateBoxes();
/*     */     
/* 111 */     ArrayList<BlockPos> coords = new ArrayList();
/* 112 */     AxisAlignedBB searchBox = this.searchBox;
/*     */     
/* 114 */     for (int x = (int)searchBox.minX; x <= searchBox.maxX; x++) {
/* 115 */       for (int y = (int)searchBox.minY; y <= searchBox.maxY; y++) {
/* 116 */         for (int z = (int)searchBox.minZ; z <= searchBox.maxZ; z++) {
					BlockPos blk = new BlockPos(x, y, z);
/* 117 */           Block block = this.worldObj.getBlockState(blk).getBlock();
/* 118 */           if (block == Blocks.water) {
/* 119 */             coords.add(new BlockPos(x, y, z));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     return coords;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dayCheck() {
/* 130 */     super.dayCheck();
/* 131 */     this.harvestedSquids.clear();
/*     */   }
/*     */   
///*     */   @SideOnly(Side.CLIENT)
///*     */   public IIcon getItemIcon(ItemStack itemStackIn, int p_70620_2_) {
///* 136 */     IIcon iicon = super.getItemIcon(itemStackIn, p_70620_2_);
///*     */     
///* 138 */     if itemStackIngetItem() == Items.fishing_rod && this.fishEntity != null) {
///* 139 */       iicon = Items.fishing_rod.func_94597_g();
///*     */     }
///* 141 */     return iicon;
///*     */   }
/*     */ 
/*     */   
/*     */   public void funconDeath(DamageSource src) {
/* 146 */     super.onDeath(src);
/* 147 */     if (this.fishEntity != null) {
/* 148 */       this.fishEntity.setDead();
/* 149 */       HelpfulVillagers.network.sendToAll((IMessage)new FishHookPacket(getEntityId(), true, 0, 0, 0));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntityFisherman.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */