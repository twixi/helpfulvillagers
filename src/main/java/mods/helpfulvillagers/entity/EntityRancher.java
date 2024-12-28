/*     */ package mods.helpfulvillagers.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.ai.EntityAIRancher;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import mods.helpfulvillagers.village.RanchGuildHall;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityRancher
/*     */   extends AbstractVillager
/*     */ {
/*     */   public boolean searching = false;
/*  33 */   private final ItemStack[] rancherTools = new ItemStack[] { new ItemStack(Items.lead) };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   public static final ItemStack[] rancherSmeltables = new ItemStack[] { new ItemStack(Items.cooked_beef), new ItemStack(Items.cooked_chicken), new ItemStack(Items.cooked_porkchop) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public static final ItemStack[] rancherCraftables = new ItemStack[] { new ItemStack((Item)Items.leather_boots), new ItemStack((Item)Items.leather_chestplate), new ItemStack((Item)Items.leather_helmet), new ItemStack((Item)Items.leather_leggings), new ItemStack(Items.book) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityRancher(World world) {
/*  54 */     super(world);
/*  55 */     init();
/*     */   }
/*     */   
/*     */   public EntityRancher(AbstractVillager villager) {
/*  59 */     super(villager);
/*  60 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/*  67 */     this.profession = 8;
/*  68 */     this.profName = "Rancher";
/*  69 */     this.currentActivity = EnumActivity.IDLE;
/*  70 */     this.searchRadius = 10;
/*  71 */     this.knownRecipes.addAll(HelpfulVillagers.rancherRecipes);
/*  72 */     setTools(this.rancherTools);
/*  73 */     getNewGuildHall();
/*  74 */     addThisAI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addThisAI() {
/*  81 */     ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
/*     */     
/*  83 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.30000001192092896D, 0.3499999940395355D));
/*  84 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIRancher(this));
/*  85 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RanchGuildHall getRanch() {
/*  93 */     if (this.homeGuildHall != null) {
/*  94 */       return (RanchGuildHall)this.homeGuildHall;
/*     */     }
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nearPasture() {
/* 104 */     if (getRanch() == null) {
/* 105 */       return false;
/*     */     }
/*     */     
/* 108 */     BlockPos currentPosition = new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ);
/*     */     
/* 110 */     if ((getRanch()).pastureCoords.contains(currentPosition)) {
/* 111 */       return true;
/*     */     }
/* 113 */     ArrayList<BlockPos> adjacent = AIHelper.getAdjacentCoords(currentPosition);
/* 114 */     for (BlockPos i : adjacent) {
/* 115 */       if ((getRanch()).pastureCoords.contains(i)) {
/* 116 */         return true;
/*     */       }
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidTool(ItemStack item) {
/* 125 */     return item.getItem() instanceof net.minecraft.item.ItemLead;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dayCheck() {
/* 130 */     super.dayCheck();
/* 131 */     this.searching = false;
/* 132 */     if (getRanch() != null) {
/* 133 */       (getRanch()).checkedAnimals.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCraft() {
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList getValidCoords() {
/* 144 */     updateBoxes();
/*     */ 
/*     */     
/* 147 */     ArrayList animals = new ArrayList();
/* 148 */     animals.addAll(this.worldObj.getEntitiesWithinAABB(EntityAnimal.class, this.searchBox));
/* 149 */     return animals;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntityRancher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */