/*     */ package mods.helpfulvillagers.entity;
/*     */ 
/*     */ import net.minecraftforge.fml.common.registry.GameRegistry;
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.ai.EntityAIBuilder;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.enums.EnumConstructionType;
/*     */ import mods.helpfulvillagers.tileentity.TileEntityContructionFence;
/*     */ import mods.helpfulvillagers.util.ConstructionSite;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
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
/*     */ public class EntityBuilder
/*     */   extends AbstractVillager
/*     */ {
/*     */   public ConstructionSite currentSite;
/*  53 */   private final ItemStack[] builderTools = new ItemStack[] { new ItemStack(Items.diamond_shovel), new ItemStack(Items.golden_shovel), new ItemStack(Items.iron_shovel), new ItemStack(Items.stone_shovel), new ItemStack(Items.wooden_shovel) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder(World world) {
/*  62 */     super(world);
/*  63 */     init();
/*     */   }
/*     */   
/*     */   public EntityBuilder(AbstractVillager villager) {
/*  67 */     super(villager);
/*  68 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/*  75 */     this.profession = 9;
/*  76 */     this.profName = "Builder";
/*  77 */     this.currentActivity = EnumActivity.IDLE;
/*  78 */     this.searchRadius = 3;
/*  79 */     setTools(this.builderTools);
/*  80 */     getNewGuildHall();
/*  81 */     addThisAI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addThisAI() {
/*  88 */     ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
/*     */     
/*  90 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.30000001192092896D, 0.3499999940395355D));
/*  91 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIBuilder(this));
/*  92 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processJobRequest(EnumConstructionType type, EntityPlayer player) {
/* 101 */     ArrayList<BlockPos> coords = getValidCoords();
/* 102 */     boolean success = false;
/* 103 */     if (coords.size() == 0) {
/* 104 */       player.addChatMessage((IChatComponent)new ChatComponentText("There is no construction fence nearby."));
/*     */     } else {
/* 106 */       for (BlockPos coord : coords) {
/*     */         try {
/* 108 */           TileEntityContructionFence fence = (TileEntityContructionFence)this.worldObj.getTileEntity(coord);
/* 109 */           if (fence == null) {
/*     */             continue;
/*     */           }
/*     */           
/* 113 */           AxisAlignedBB box = fence.setupConstructionSite(this.worldObj, coord);
/* 114 */           if (box != null) {
/* 115 */             success = true;
/* 116 */             ConstructionSite site = new ConstructionSite(this.worldObj, box, type);
/* 117 */             this.currentSite = site;
/* 118 */             this.homeVillage.constructionSites.add(site);
/*     */             break;
/*     */           } 
/* 121 */         } catch (Exception e) {
/* 122 */           System.out.println(e.getMessage());
/*     */         } 
/*     */       } 
/* 125 */       if (!success) {
/* 126 */         player.addChatMessage((IChatComponent)new ChatComponentText("Something's not right. Double check your construction fences."));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidTool(ItemStack item) {
/* 133 */     return item.getItem() instanceof net.minecraft.item.ItemSpade;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCraft() {
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList getValidCoords() {
/* 143 */     updateBoxes();
/*     */     
/* 145 */     ArrayList<BlockPos> coords = new ArrayList();
/* 146 */     AxisAlignedBB searchBox = this.searchBox;
/*     */     
/* 148 */     for (int x = (int)searchBox.minX; x <= searchBox.maxX; x++) {
/* 149 */       for (int y = (int)searchBox.minY; y <= searchBox.maxY; y++) {
/* 150 */         for (int z = (int)searchBox.minZ; z <= searchBox.maxZ; z++) {
					BlockPos blk = new BlockPos(x, y, z);
/* 151 */           Block block = this.worldObj.getBlockState(blk).getBlock();
/* 152 */           if (block.equals(GameRegistry.findBlock("helpfulvillagers", "construction_fence")) && block.getBlockHardness(this.worldObj, blk) >= 0.0F) {
/* 153 */             coords.add(new BlockPos(x, y, z));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 159 */     return coords;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound compound) {
/* 164 */     super.writeEntityToNBT(compound);
/*     */     
/* 166 */     if (this.currentSite != null) {
/* 167 */       compound.setTag("Site", (NBTBase)this.currentSite.writeToNBT(new NBTTagCompound()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound compound) {
/* 173 */     super.readEntityFromNBT(compound);
/*     */     try {
/* 175 */       if (compound.hasKey("Site")) {
/* 176 */         NBTTagCompound siteCompound = (NBTTagCompound)compound.getTag("Site");
/* 177 */         this.currentSite = ConstructionSite.loadSiteFromNBT(siteCompound, this.worldObj);
/*     */       } 
/* 179 */     } catch (Exception e) {
/* 180 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntityBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */