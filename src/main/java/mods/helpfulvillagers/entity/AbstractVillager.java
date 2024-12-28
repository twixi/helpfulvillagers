/*      */ package mods.helpfulvillagers.entity;
/*      */ 
/*      */ import com.google.common.collect.Multimap;
/*      */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import mods.helpfulvillagers.ai.EntityAIFollowLeader;
/*      */ import mods.helpfulvillagers.ai.EntityAIMoveIndoorsCustom;
/*      */ import mods.helpfulvillagers.ai.EntityAIVillagerMateCustom;
/*      */ import mods.helpfulvillagers.crafting.CraftItem;
/*      */ import mods.helpfulvillagers.crafting.CraftTree;
/*      */ import mods.helpfulvillagers.crafting.VillagerRecipe;
/*      */ import mods.helpfulvillagers.enums.EnumActivity;
/*      */ import mods.helpfulvillagers.enums.EnumMessage;
/*      */ import mods.helpfulvillagers.inventory.InventoryVillager;
/*      */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*      */ import mods.helpfulvillagers.network.CraftItemClientPacket;
/*      */ import mods.helpfulvillagers.network.CraftQueueClientPacket;
/*      */ import mods.helpfulvillagers.network.CustomRecipesPacket;
/*      */ import mods.helpfulvillagers.network.LeaderPacket;
/*      */ import mods.helpfulvillagers.network.PlayerAccountClientPacket;
/*      */ import mods.helpfulvillagers.network.PlayerMessagePacket;
/*      */ import mods.helpfulvillagers.network.SwingPacket;
/*      */ import mods.helpfulvillagers.network.UnlockedHallsPacket;
/*      */ import mods.helpfulvillagers.network.VillageSyncPacket;
/*      */ import mods.helpfulvillagers.util.AIHelper;
/*      */ import mods.helpfulvillagers.util.ResourceCluster;
/*      */ import mods.helpfulvillagers.village.GuildHall;
/*      */ import mods.helpfulvillagers.village.HelpfulVillage;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityCreature;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.EntityAIBase;
/*      */ import net.minecraft.entity.ai.EntityAIFollowGolem;
/*      */ import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
/*      */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*      */ import net.minecraft.entity.ai.EntityAIOpenDoor;
/*      */ import net.minecraft.entity.ai.EntityAIPlay;
/*      */ import net.minecraft.entity.ai.EntityAISwimming;
/*      */ import net.minecraft.entity.ai.EntityAITradePlayer;
/*      */ import net.minecraft.entity.ai.EntityAIWander;
/*      */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*      */ import net.minecraft.entity.ai.EntityAIWatchClosest2;
/*      */ import net.minecraft.entity.ai.RandomPositionGenerator;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.passive.EntityVillager;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.item.ItemSword;
/*      */ import net.minecraft.item.ItemTool;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagInt;
/*      */ import net.minecraft.nbt.NBTTagIntArray;
/*      */ import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
/*      */ import net.minecraft.tileentity.TileEntityChest;
/*      */ import net.minecraft.tileentity.TileEntityFurnace;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.village.MerchantRecipeList;
/*      */ import net.minecraft.world.World;

/*      */ 
/*      */ public abstract class AbstractVillager
/*      */   extends EntityVillager
/*      */ {
/*  119 */   private final int REPRODUCE_TIME = 1000;
/*      */   
/*      */   public int profession;
/*      */   
/*      */   public String nickname;
/*      */   
/*      */   public String profName;
/*      */   public InventoryVillager inventory;
/*      */   public BlockPos villageCenter;
/*      */   public HelpfulVillage homeVillage;
/*      */   public GuildHall homeGuildHall;
/*      */   public EntityLivingBase leader;
/*      */   private int leaderID;
/*      */   public int guiCommand;
/*      */   public boolean hasTool;
/*      */   private boolean isSwinging;
/*      */   private int swingTicks;
/*      */   private int healthTicks;
/*      */   private boolean dayCheck;
/*      */   private boolean hasDied;
/*      */   public boolean changeGuildHall;
/*  140 */   protected ItemStack[] validTools = new ItemStack[0];
/*      */   
/*      */   public AxisAlignedBB searchBox;
/*      */   public AxisAlignedBB pickupBox;
/*      */   protected int searchRadius;
/*      */   protected int pickupRadius;
/*      */   private boolean canPickup;
/*  147 */   public ArrayList<VillagerRecipe> knownRecipes = new ArrayList<VillagerRecipe>();
/*  148 */   public ArrayList<VillagerRecipe> customRecipes = new ArrayList<VillagerRecipe>();
/*      */   public CraftItem currentCraftItem;
/*  150 */   public ArrayList<CraftTree.Node> craftChain = new ArrayList<CraftTree.Node>();
/*  151 */   public ArrayList<ItemStack> materialsNeeded = new ArrayList<ItemStack>();
/*  152 */   public ArrayList<ItemStack> smeltablesNeeded = new ArrayList<ItemStack>();
/*      */   
/*  154 */   public ItemStack queuedTool = null;
/*      */   
/*      */   public EnumActivity currentActivity;
/*      */   
/*      */   public ResourceCluster lastResource;
/*      */   public ResourceCluster currentResource;
/*      */   public int villagerID;
/*      */   
/*      */   public AbstractVillager() {
/*  163 */     super(null);
/*      */   }
/*      */   
/*      */   public AbstractVillager(World world) {
/*  167 */     super(world);
/*  168 */     this.stepHeight = (float)(this.stepHeight + 0.5D);
/*  169 */     this.profession = 0;
/*  170 */     this.nickname = "";
/*  171 */     this.inventory = new InventoryVillager(this);
/*  172 */     this.villageCenter = null;
/*  173 */     this.homeVillage = null;
/*  174 */     this.homeGuildHall = null;
/*  175 */     this.leader = null;
/*  176 */     this.guiCommand = -1;
/*  177 */     this.isSwinging = false;
/*  178 */     this.healthTicks = 0;
/*  179 */     this.searchBox = this.getEntityBoundingBox();
/*  180 */     this.pickupBox = this.getEntityBoundingBox();
/*  181 */     this.searchRadius = 1;
/*  182 */     this.pickupRadius = 1;
/*  183 */     this.canPickup = true;
/*  184 */     this.currentActivity = EnumActivity.IDLE;
/*  185 */     this.lastResource = null;
/*  186 */     this.hasTool = false;
/*  187 */     this.villagerID = 0;
/*  188 */     this.leaderID = 0;
/*  189 */     this.dayCheck = true;
/*  190 */     this.changeGuildHall = false;
/*  191 */     this.hasDied = false;
/*  192 */     this.currentCraftItem = null;
/*  193 */     addAI();
/*      */   }
/*      */   
/*      */   public AbstractVillager(AbstractVillager villager) {
/*  197 */     super(villager.worldObj);
/*  198 */     this.stepHeight = (float)(this.stepHeight + 0.5D);
/*  199 */     this.nickname = villager.nickname;
/*  200 */     villager.inventory.dumpCollected(true);
/*  201 */     villager.inventory.dumpCollected(false);
/*  202 */     this.inventory = villager.inventory;
/*  203 */     this.inventory.owner = this;
/*  204 */     this.villageCenter = villager.villageCenter;
/*  205 */     this.homeVillage = villager.homeVillage;
/*  206 */     this.homeGuildHall = null;
/*  207 */     this.leader = villager.leader;
/*  208 */     this.guiCommand = villager.guiCommand;
/*  209 */     this.isSwinging = false;
/*  210 */     this.healthTicks = 0;
/*  211 */     this.searchBox = this.getEntityBoundingBox();
/*  212 */     this.pickupBox = this.getEntityBoundingBox();
/*  213 */     this.searchRadius = 1;
/*  214 */     this.pickupRadius = 1;
/*  215 */     this.canPickup = true;
/*  216 */     this.currentActivity = EnumActivity.IDLE;
/*  217 */     this.lastResource = null;
/*  218 */     this.hasTool = false;
/*  219 */     this.villagerID = 0;
/*  220 */     this.leaderID = 0;
/*  221 */     this.dayCheck = true;
/*  222 */     this.changeGuildHall = false;
/*  223 */     this.hasDied = false;
/*  224 */     this.customRecipes.addAll(villager.customRecipes);
/*  225 */     villager.addCraftItem(villager.currentCraftItem);
/*  226 */     this.currentCraftItem = null;
/*  227 */     for (ItemStack i : villager.inventory.materialsCollected) {
/*  228 */       this.inventory.addItem(i);
/*      */     }
/*  230 */     this.inventory.materialsCollected.clear();
/*  231 */     setCustomNameTag(villager.getCustomNameTag());
/*  232 */     addAI();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addAI() {
/*  239 */     this.tasks.taskEntries.clear();
/*      */     
/*  241 */     ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
				((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
/*      */     
/*  244 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIFollowLeader(this));
/*  245 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIMoveIndoorsCustom(this));
/*      */     
/*  247 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  248 */     this.tasks.addTask(1, (EntityAIBase)new EntityAITradePlayer(this));
/*  249 */     this.tasks.addTask(1, (EntityAIBase)new EntityAILookAtTradePlayer(this));
/*  250 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIVillagerMateCustom(this));
/*  251 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIOpenDoor((EntityLiving)this, true));
/*  252 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction((EntityCreature)this, 0.30000001192092896D));
/*  253 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIFollowGolem(this));
/*  254 */     this.tasks.addTask(8, (EntityAIBase)new EntityAIPlay(this, 0.3199999928474426D));
/*  255 */     this.tasks.addTask(9, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, EntityPlayer.class, 3.0F, 1.0F));
/*  256 */     this.tasks.addTask(9, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, EntityVillager.class, 5.0F, 0.02F));
/*  257 */     this.tasks.addTask(9, (EntityAIBase)new EntityAIWander((EntityCreature)this, 0.30000001192092896D));
/*  258 */     this.tasks.addTask(10, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityLiving.class, 8.0F));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean interact(EntityPlayer player) {
/*  267 */     if (player.isSneaking()) {
/*  268 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  272 */     if (HelpfulVillagers.player_guard.containsKey(player)) {
/*  273 */       AbstractVillager guard = (AbstractVillager)HelpfulVillagers.player_guard.remove(player);
/*  274 */       if (guard.equals(this)) {
/*  275 */         HelpfulVillagers.player_guard.put(player, guard);
/*      */       } else {
/*  277 */         guard.setLeader((EntityLivingBase)this);
/*      */         
/*  279 */         if (this.worldObj.isRemote) {
/*  280 */           String guardName = guard.getCustomNameTag();
/*  281 */           if (guardName == null || guardName.equals("") || guardName.equals(" ")) {
/*  282 */             guardName = "A " + guard.profName;
/*      */           } else {
/*  284 */             guardName = guardName + " the " + guard.profName;
/*      */           } 
/*      */           
/*  287 */           String leaderName = getCustomNameTag();
/*  288 */           if (leaderName == null || leaderName.equals("") || leaderName.equals(" ")) {
/*  289 */             leaderName = "this " + this.profName;
/*      */           } else {
/*  291 */             leaderName = leaderName + " the " + this.profName;
/*      */           } 
/*      */           
/*  294 */           String message = guardName + " is now guarding " + leaderName;
/*  295 */           player.addChatMessage((IChatComponent)new ChatComponentText(message));
/*      */         } 
/*  297 */         return false;
/*      */       } 
/*      */     } 
/*      */     
/*  301 */     if (isChild()) {
/*  302 */       return false;
/*      */     }
/*      */     
/*  305 */     setCustomer(player);
/*  306 */     if (!this.worldObj.isRemote && !this.customRecipes.isEmpty()) {
/*  307 */       if (!this.customRecipes.isEmpty()) {
/*  308 */         HelpfulVillagers.network.sendTo((IMessage)new CustomRecipesPacket(getEntityId(), this.customRecipes), (EntityPlayerMP)player);
/*      */       }
/*      */       
/*  311 */       if (this.leader == null) {
/*  312 */         HelpfulVillagers.network.sendTo((IMessage)new LeaderPacket(getEntityId(), -1), (EntityPlayerMP)player);
/*      */       } else {
/*  314 */         HelpfulVillagers.network.sendTo((IMessage)new LeaderPacket(getEntityId(), this.leader.getEntityId()), (EntityPlayerMP)player);
/*      */       } 
/*      */     } 
/*  317 */     player.openGui(HelpfulVillagers.instance, 0, this.worldObj, getEntityId(), 0, 0);
/*  318 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleGuiCommand() {
/*  326 */     EntityPlayer player = getCustomer();
/*  327 */     switch (this.guiCommand) {
/*      */       case 0:
/*  329 */         setLeader((EntityLivingBase)player);
/*  330 */         this.currentActivity = EnumActivity.FOLLOW;
/*  331 */         if (this instanceof EntityLumberjack || this instanceof EntityFarmer || this instanceof EntityMiner) {
/*  332 */           this.lastResource = this.currentResource;
/*  333 */           this.currentResource = null;
/*      */         } 
/*      */         break;
/*      */       case 1:
/*  337 */         if (this.leader instanceof EntityMiner) {
/*  338 */           EntityMiner miner = (EntityMiner)this.leader;
/*  339 */           miner.beingFollowed = false;
/*      */         } 
/*  341 */         setLeader((EntityLivingBase)null);
/*  342 */         this.currentActivity = EnumActivity.IDLE;
/*      */         break;
/*      */       
/*      */       case 2:
/*  346 */         if (isEntityAlive() && isChild() && !player.isSneaking()) {
/*  347 */           if (!this.worldObj.isRemote) {
/*  348 */             this.inventory.syncInventory();
/*      */           }
/*  350 */           player.openGui(HelpfulVillagers.instance, 2, this.worldObj, getEntityId(), 0, 0);
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 3:
/*  355 */         if (!this.worldObj.isRemote) {
/*      */           try {
/*  357 */             if (player == null || !(player instanceof EntityPlayerMP)) {
/*  358 */               throw new Exception();
/*      */             }
/*  360 */             HelpfulVillagers.network.sendTo((IMessage)new UnlockedHallsPacket(getEntityId(), this.homeVillage.unlockedHalls), (EntityPlayerMP)player);
/*  361 */           } catch (Exception e) {
/*  362 */             HelpfulVillagers.network.sendToAll((IMessage)new UnlockedHallsPacket(getEntityId(), this.homeVillage.unlockedHalls));
/*      */           } 
/*      */         }
/*  365 */         player.openGui(HelpfulVillagers.instance, 1, this.worldObj, getEntityId(), 0, 0);
/*      */         break;
/*      */       
/*      */       case 4:
/*  369 */         player.openGui(HelpfulVillagers.instance, 3, this.worldObj, getEntityId(), 0, 0);
/*      */         break;
/*      */       
/*      */       case 5:
/*  373 */         HelpfulVillagers.player_guard.put(player, this);
/*  374 */         if (this.leader != null && this.leader instanceof EntityMiner) {
/*  375 */           EntityMiner miner = (EntityMiner)this.leader;
/*  376 */           miner.beingFollowed = false;
/*      */         } 
/*  378 */         setLeader((EntityLivingBase)player);
/*  379 */         this.currentActivity = EnumActivity.FOLLOW;
/*      */         break;
/*      */       
/*      */       case 6:
/*  383 */         if (!this.worldObj.isRemote) {
/*  384 */           HelpfulVillagers.network.sendTo((IMessage)new CraftItemClientPacket(getEntityId(), this.currentCraftItem, this.inventory.materialsCollected, this.materialsNeeded), (EntityPlayerMP)player);
/*      */           try {
/*  386 */             HelpfulVillagers.network.sendTo((IMessage)new CraftQueueClientPacket(getEntityId(), this.homeVillage.craftQueue.getAll()), (EntityPlayerMP)player);
/*  387 */           } catch (Exception e) {
/*  388 */             System.out.println("Packet Error");
/*  389 */             HelpfulVillagers.network.sendToAll((IMessage)new CraftQueueClientPacket(getEntityId(), this.homeVillage.craftQueue.getAll()));
/*      */           } 
/*      */         } 
/*  392 */         player.openGui(HelpfulVillagers.instance, 4, this.worldObj, getEntityId(), 0, 0);
/*      */         break;
/*      */       
/*      */       case 7:
/*  396 */         player.openGui(HelpfulVillagers.instance, 6, this.worldObj, getEntityId(), 0, 0);
/*      */         break;
/*      */       
/*      */       case 8:
/*  400 */         if (!this.worldObj.isRemote) {
/*  401 */           this.homeVillage.economy.fullSyncClient(this, player);
/*  402 */           int amount = this.homeVillage.economy.getAccount(player);
/*  403 */           if (amount < 0) {
/*  404 */             this.homeVillage.economy.setAccount(player, 0);
/*  405 */             amount = 0;
/*      */           } 
/*  407 */           HelpfulVillagers.network.sendTo((IMessage)new PlayerAccountClientPacket(player, this), (EntityPlayerMP)player);
/*      */         } 
/*  409 */         player.openGui(HelpfulVillagers.instance, 7, this.worldObj, getEntityId(), 0, 0);
/*      */         break;
/*      */       
/*      */       case 9:
/*  413 */         player.openGui(HelpfulVillagers.instance, 8, this.worldObj, getEntityId(), 0, 0);
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  418 */     this.guiCommand = -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onUpdate() {
/*  427 */     super.onUpdate();
/*  428 */     if (this.guiCommand >= 0) {
/*      */       try {
/*  430 */         handleGuiCommand();
/*  431 */       } catch (NullPointerException e) {
/*  432 */         this.guiCommand = -1;
/*      */       } 
/*      */     }
/*      */     
/*  436 */     if (this.leader != null) {
/*  437 */       this.currentActivity = EnumActivity.FOLLOW;
/*      */     }
/*      */     
/*  440 */     if (this.dayCheck && this.worldObj.isDaytime()) {
/*  441 */       this.dayCheck = false;
/*  442 */       dayCheck();
/*  443 */     } else if (!this.dayCheck && !this.worldObj.isDaytime()) {
/*  444 */       this.dayCheck = true;
/*      */     } 
/*      */     
/*  447 */     getNewHomeVillage();
/*  448 */     syncVillage();
/*  449 */     getNewGuildHall();
/*  450 */     updateBoxes();
/*  451 */     updateArmor();
/*  452 */     updateSwing();
/*  453 */     updateHealth();
/*  454 */     updateID();
/*  455 */     updateLeader();
/*  456 */     pickupItems();
/*  457 */     resetTool();
/*  458 */     resetArmor();
/*  459 */     getCraftItem();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveTo(BlockPos coords, float speed) {
/*      */     try {
/*  470 */       if (!getNavigator().tryMoveToXYZ(coords.getX(), coords.getY(), coords.getZ(), speed)) {
/*  471 */         Vec3 vector = new Vec3(coords.getX(), coords.getY(), coords.getZ());
/*  472 */         Vec3 tempVec = RandomPositionGenerator.findRandomTargetBlockTowards((EntityCreature)this, 10, 3, vector);
					BlockPos habp = new BlockPos(this.posX, this.posY, this.posZ);
/* */				setHomePosAndDistance(habp, 20);
/*  474 */         if (tempVec != null) {
/*  475 */           getNavigator().tryMoveToXYZ(tempVec.xCoord, tempVec.yCoord, tempVec.zCoord, speed);
/*      */         }
/*      */       } 
/*  478 */     } catch (NullPointerException e) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveTo(Entity entity, float speed) {
/*      */     try {
/*  490 */       if (!getNavigator().tryMoveToEntityLiving(entity, speed)) {
/*  491 */         BlockPos coords = new BlockPos(entity.posX, entity.posY, entity.posZ);
/*  492 */         Vec3 vector = new Vec3(coords);
/*  493 */         Vec3 tempVec = RandomPositionGenerator.findRandomTargetBlockTowards((EntityCreature)this, 10, 3, vector);
				   BlockPos habp = new BlockPos(this.posX, this.posY, this.posZ);
/* */			   setHomePosAndDistance(habp, 20);
/*  495 */         if (tempVec != null) {
/*  496 */           getNavigator().tryMoveToXYZ(tempVec.xCoord, tempVec.yCoord, tempVec.zCoord, speed);
/*      */         }
/*      */       } 
/*  499 */     } catch (NullPointerException e) {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public BlockPos getCoords() {
/*  505 */     return new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ);
/*      */   }
/*      */   
/*      */   public ArrayList getSurroundingCoords() {
/*  509 */     ArrayList<BlockPos> coords = new ArrayList();
/*  510 */     for (int x = (int)this.posX - 1; x <= (int)this.posX + 1; x++) {
/*  511 */       for (int y = (int)this.posY; y <= (int)this.posY + 1; y++) {
/*  512 */         for (int z = (int)this.posZ - 1; z <= (int)this.posZ + 1; z++) {
/*  513 */           coords.add(new BlockPos(x, y, z));
/*      */         }
/*      */       } 
/*      */     } 
/*  517 */     return coords;
/*      */   }
/*      */   
/*      */   public int getDirection() {
/*  521 */     return MathHelper.floor_double((this.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProfession(int par1) {
/*  529 */     if (this.profession != par1) {
/*  530 */       EntityRegularVillager villager; EntityLumberjack lumberjack; EntityMiner miner; EntityFarmer farmer; EntitySoldier soldier; EntityArcher archer; EntityMerchant merchant; EntityFisherman fisherman; EntityRancher rancher; EntityBuilder builder; if (this instanceof EntityFisherman) {
/*  531 */         EntityFisherman entityFisherman = (EntityFisherman)this;
/*  532 */         if (entityFisherman.fishEntity != null) {
/*  533 */           entityFisherman.fishEntity.setDead();
/*      */         }
/*      */       } 
/*      */       
/*  537 */       switch (par1) {
/*      */         case 0:
/*  539 */           villager = new EntityRegularVillager(this);
/*  540 */           villager.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  541 */           if (!this.worldObj.isRemote) {
/*  542 */             this.worldObj.spawnEntityInWorld((Entity)villager);
/*  543 */             if (villager.leader != null && !(villager.leader instanceof AbstractVillager)) {
/*  544 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(villager.getEntityId(), villager.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  547 */           setDead();
/*      */           break;
/*      */         
/*      */         case 1:
/*  551 */           lumberjack = new EntityLumberjack(this);
/*  552 */           lumberjack.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  553 */           if (!this.worldObj.isRemote) {
/*  554 */             this.worldObj.spawnEntityInWorld((Entity)lumberjack);
/*  555 */             if (lumberjack.leader != null && !(lumberjack.leader instanceof AbstractVillager)) {
/*  556 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(lumberjack.getEntityId(), lumberjack.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  559 */           setDead();
/*      */           break;
/*      */         
/*      */         case 2:
/*  563 */           miner = new EntityMiner(this);
/*  564 */           miner.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  565 */           if (!this.worldObj.isRemote) {
/*  566 */             this.worldObj.spawnEntityInWorld((Entity)miner);
/*  567 */             if (miner.leader != null && !(miner.leader instanceof AbstractVillager)) {
/*  568 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(miner.getEntityId(), miner.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  571 */           setDead();
/*      */           break;
/*      */         
/*      */         case 3:
/*  575 */           farmer = new EntityFarmer(this);
/*  576 */           farmer.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  577 */           if (!this.worldObj.isRemote) {
/*  578 */             this.worldObj.spawnEntityInWorld((Entity)farmer);
/*  579 */             if (farmer.leader != null && !(farmer.leader instanceof AbstractVillager)) {
/*  580 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(farmer.getEntityId(), farmer.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  583 */           setDead();
/*      */           break;
/*      */         
/*      */         case 4:
/*  587 */           soldier = new EntitySoldier(this);
/*  588 */           soldier.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  589 */           if (!this.worldObj.isRemote) {
/*  590 */             this.worldObj.spawnEntityInWorld((Entity)soldier);
/*  591 */             if (soldier.leader != null) {
/*  592 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(soldier.getEntityId(), soldier.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  595 */           setDead();
/*      */           break;
/*      */         
/*      */         case 5:
/*  599 */           archer = new EntityArcher(this);
/*  600 */           archer.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  601 */           if (!this.worldObj.isRemote) {
/*  602 */             this.worldObj.spawnEntityInWorld((Entity)archer);
/*  603 */             if (archer.leader != null) {
/*  604 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(archer.getEntityId(), archer.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  607 */           setDead();
/*      */           break;
/*      */         
/*      */         case 6:
/*  611 */           merchant = new EntityMerchant(this);
/*  612 */           merchant.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  613 */           if (!this.worldObj.isRemote) {
/*  614 */             this.worldObj.spawnEntityInWorld((Entity)merchant);
/*  615 */             if (merchant.leader != null && !(merchant.leader instanceof AbstractVillager)) {
/*  616 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(merchant.getEntityId(), merchant.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  619 */           setDead();
/*      */           break;
/*      */         
/*      */         case 7:
/*  623 */           fisherman = new EntityFisherman(this);
/*  624 */           fisherman.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  625 */           if (!this.worldObj.isRemote) {
/*  626 */             this.worldObj.spawnEntityInWorld((Entity)fisherman);
/*  627 */             if (fisherman.leader != null && !(fisherman.leader instanceof AbstractVillager)) {
/*  628 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(fisherman.getEntityId(), fisherman.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  631 */           setDead();
/*      */           break;
/*      */         
/*      */         case 8:
/*  635 */           rancher = new EntityRancher(this);
/*  636 */           rancher.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  637 */           if (!this.worldObj.isRemote) {
/*  638 */             this.worldObj.spawnEntityInWorld((Entity)rancher);
/*  639 */             if (rancher.leader != null && !(rancher.leader instanceof AbstractVillager)) {
/*  640 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(rancher.getEntityId(), rancher.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  643 */           setDead();
/*      */           break;
/*      */         
/*      */         case 9:
/*  647 */           builder = new EntityBuilder(this);
/*  648 */           builder.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  649 */           if (!this.worldObj.isRemote) {
/*  650 */             this.worldObj.spawnEntityInWorld((Entity)builder);
/*  651 */             if (builder.leader != null && !(builder.leader instanceof AbstractVillager)) {
/*  652 */               HelpfulVillagers.network.sendToAll((IMessage)new LeaderPacket(builder.getEntityId(), builder.leader.getEntityId()));
/*      */             }
/*      */           } 
/*  655 */           setDead();
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getProfession() {
/*  666 */     return this.profession;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLeader(EntityLivingBase leader) {
/*  673 */     this.leader = leader;
/*  674 */     if (leader != null && leader instanceof AbstractVillager) {
/*  675 */       if (this.profession == 4 || this.profession == 5) {
/*  676 */         AbstractVillager villager = (AbstractVillager)leader;
/*  677 */         this.leaderID = villager.villagerID;
/*  678 */         if (villager instanceof EntityMiner) {
/*  679 */           EntityMiner miner = (EntityMiner)villager;
/*  680 */           miner.beingFollowed = true;
/*      */         } 
/*      */       } else {
/*  683 */         this.leader = null;
/*  684 */         this.leaderID = -1;
/*      */       } 
/*      */     } else {
/*  687 */       this.leaderID = -1;
/*      */     } 
/*      */     
/*  690 */     if (this.worldObj.isRemote || 
/*  691 */       this.leader == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EntityLivingBase getLeader() {
/*  703 */     return this.leader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MerchantRecipeList getRecipes(EntityPlayer EntityPlayer) {
/*  712 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void takeItemFromPlayer(EntityPlayer player) {
/*  719 */     this.inventory.setCurrentItem(player.inventory.getCurrentItem());
/*  720 */     player.destroyCurrentEquippedItem();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void giveItemToPlayer(EntityPlayer player) {
/*  727 */     if (this.inventory.getCurrentItem() != null) {
/*  728 */ 		player.inventory.addItemStackToInventory(this.inventory.getCurrentItem());
/*  729 */       this.inventory.setCurrentItem(null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack[] getValidTools() {
/*  737 */     return this.validTools;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean isValidTool(ItemStack ItemStack);
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setTools(ItemStack[] items) {
/*  747 */     this.validTools = new ItemStack[items.length];
/*  748 */     System.arraycopy(items, 0, this.validTools, 0, this.validTools.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean canCraft();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canCraft(CraftItem craftItem) {
/*  760 */     ItemStack item = craftItem.getItem();
/*  761 */     return canCraft(item);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canCraft(ItemStack item) {
/*  768 */     if (item == null) {
/*  769 */       return false;
/*      */     }
/*  771 */     for (VillagerRecipe i : this.knownRecipes) {
/*  772 */       if (i.getOutput().getItem().equals(item.getItem())) {
/*  773 */         return true;
/*      */       }
/*      */     } 
/*  776 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VillagerRecipe getRecipe(ItemStack item) {
/*  783 */     if (item == null) {
/*  784 */       return null;
/*      */     }
/*  786 */     for (VillagerRecipe i : this.knownRecipes) {
/*  787 */       if (i.getOutput().getDisplayName().equals(item.getDisplayName())) {
/*  788 */         return i;
/*      */       }
/*      */     } 
/*  791 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCustomRecipe(VillagerRecipe recipe) {
/*  799 */     if (!this.customRecipes.contains(recipe) && !this.knownRecipes.contains(recipe)) {
/*  800 */       this.customRecipes.add(recipe);
/*  801 */       this.knownRecipes.add(recipe);
/*  802 */       Collections.sort(this.knownRecipes);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void replaceCustomRecipe(VillagerRecipe recipe) {
/*  813 */     int index = -1;
/*      */     
/*  815 */     for (int i = 0; i < this.customRecipes.size(); i++) {
/*  816 */       VillagerRecipe r = this.customRecipes.get(i);
/*  817 */       if (r.getOutput().getDisplayName().equals(recipe.getOutput().getDisplayName())) {
/*  818 */         index = i;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  823 */     if (index >= 0) {
/*  824 */       ArrayList<VillagerRecipe> temp = new ArrayList<VillagerRecipe>(this.customRecipes);
/*  825 */       resetRecipes();
/*  826 */       this.customRecipes.addAll(temp);
/*  827 */       this.customRecipes.set(index, recipe);
/*  828 */       this.knownRecipes.addAll(this.customRecipes);
/*  829 */       Collections.sort(this.knownRecipes);
/*      */     } else {
/*  831 */       System.out.println("Recipe Not Found: Replace");
/*  832 */       addCustomRecipe(recipe);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteCustomRecipe(VillagerRecipe recipe) {
/*  842 */     int index = -1;
/*      */     
/*  844 */     for (int i = 0; i < this.customRecipes.size(); i++) {
/*  845 */       VillagerRecipe r = this.customRecipes.get(i);
/*  846 */       if (r.getOutput().getDisplayName().equals(recipe.getOutput().getDisplayName())) {
/*  847 */         index = i;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  852 */     if (index >= 0) {
/*  853 */       ArrayList<VillagerRecipe> temp = new ArrayList<VillagerRecipe>(this.customRecipes);
/*  854 */       resetRecipes();
/*  855 */       this.customRecipes.addAll(temp);
/*  856 */       this.customRecipes.remove(index);
/*  857 */       this.knownRecipes.addAll(this.customRecipes);
/*  858 */       Collections.sort(this.knownRecipes);
/*      */     } else {
/*  860 */       System.out.println("Recipe Not Found: Delete");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetRecipes() {
/*  868 */     this.customRecipes.clear();
/*  869 */     this.knownRecipes.clear();
/*  870 */     switch (this.profession) {
/*      */       case 1:
/*  872 */         this.knownRecipes.addAll(HelpfulVillagers.lumberjackRecipes);
/*      */         break;
/*      */       
/*      */       case 2:
/*  876 */         this.knownRecipes.addAll(HelpfulVillagers.minerRecipes);
/*      */         break;
/*      */       
/*      */       case 3:
/*  880 */         this.knownRecipes.addAll(HelpfulVillagers.farmerRecipes);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMetadataSensitive(ItemStack item) {
/*  890 */     if (item == null) {
/*  891 */       return false;
/*      */     }
/*  893 */     VillagerRecipe recipe = getRecipe(item);
/*  894 */     if (recipe == null) {
/*  895 */       return false;
/*      */     }
/*  897 */     return recipe.getMetadataSensitivity();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void resetTool() {
/*  905 */     if (getCurrentItem() != null && !isValidTool(getCurrentItem())) {
/*  906 */       this.hasTool = false;
/*  907 */       if (!this.inventory.isFull()) {
/*  908 */         this.inventory.addItem(this.inventory.getCurrentItem());
/*      */       } else {
/*  910 */         EntityItem worldItem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, getCurrentItem());
/*  911 */         this.worldObj.spawnEntityInWorld((Entity)worldItem);
/*      */       } 
/*  913 */       this.inventory.setCurrentItem(null);
/*  914 */     } else if (getCurrentItem() == null) {
/*  915 */       this.hasTool = false;
/*  916 */       int index = this.inventory.containsItem();
/*  917 */       if (index >= 0) {
/*  918 */         this.inventory.swapEquipment(index, 0);
/*  919 */         this.hasTool = true;
/*      */       } 
/*      */     } else {
/*  922 */       this.hasTool = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void resetArmor() {
/*  930 */     for (int i = 28; i < 32; i++) {
/*  931 */       ItemStack item = this.inventory.getStackInSlot(i);
/*  932 */       if (!this.inventory.isItemValidForSlot(i, item)) {
/*  933 */         this.inventory.addItem(item);
/*  934 */         this.inventory.setInventorySlotContents(i, null);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack getCurrentItem() {
/*  943 */     return this.inventory.getCurrentItem();
/*      */   }
/*      */   
/*      */   public ItemStack getHeldItem() {
/*  947 */     return getCurrentItem();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void damageItem() {
/*  954 */     if (getCurrentItem() == null) {
/*      */       return;
/*      */     }
/*  957 */     getCurrentItem().setItemDamage(getCurrentItem().getItemDamage() + 1);;
/*  958 */     if (getCurrentItem().getItemDamage() >= getCurrentItem().getMaxDamage()) {
/*  959 */       this.inventory.setCurrentItem(null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void swingItem() {
/*  968 */     if (this.worldObj.isRemote) {
/*  969 */       if (!this.isSwinging || this.swingTicks >= 4 || this.swingTicks < 0) {
/*  970 */         this.swingTicks = -1;
/*  971 */         this.isSwinging = true;
/*      */       } 
/*      */     } else {
/*  974 */       HelpfulVillagers.network.sendToAll((IMessage)new SwingPacket(getEntityId()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateSwing() {
/*  983 */     if (this.isSwinging) {
/*  984 */       this.swingTicks++;
/*      */       
/*  986 */       if (this.swingTicks >= 8) {
/*  987 */         this.swingTicks = 0;
/*  988 */         this.isSwinging = false;
/*      */       } 
/*      */     } else {
/*  991 */       this.swingTicks = 0;
/*      */     } 
/*  993 */     this.swingProgress = this.swingTicks / 8.0F;
/*      */   }
/*      */   
/*      */   public boolean isSwinging() {
/*  997 */     return this.isSwinging;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBoxes() {
/* 1004 */     this.searchBox.fromBounds(this.posX - this.searchRadius, this.posY - this.searchRadius, this.posZ - this.searchRadius, this.posX + this.searchRadius, this.posY + this.searchRadius, this.posZ + this.searchRadius);
/* 1005 */     this.pickupBox.fromBounds(this.posX - this.pickupRadius, this.posY - this.pickupRadius, this.posZ - this.pickupRadius, this.posX + this.pickupRadius, this.posY + this.pickupRadius, this.posZ + this.pickupRadius);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPickupRadius(int radius) {
/* 1013 */     this.pickupRadius = radius;
/* 1014 */     updateBoxes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void pickupItems() {
/* 1021 */     if (isChild() || !this.canPickup) {
/*      */       return;
/*      */     }
/*      */     
/* 1025 */     if (this.inventory.isFull()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1031 */     List items = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.pickupBox);
/* 1032 */     Iterator<EntityItem> iterator = items.iterator();
/*      */     
/* 1034 */     while (iterator.hasNext()) {
/* 1035 */       EntityItem currentItem = iterator.next();
/* 1036 */       if (!currentItem.isDead) {
/* 1037 */         this.inventory.addItem(currentItem.getEntityItem());
/* 1038 */         currentItem.setDead();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateArmor() {
/* 1047 */     this.inventory.syncEquipment();
/* 1048 */     setCurrentItemOrArmor(4, this.inventory.getStackInSlot(28));
/* 1049 */   setCurrentItemOrArmor(3, this.inventory.getStackInSlot(29));
/* 1050 */   setCurrentItemOrArmor(2, this.inventory.getStackInSlot(30));
/* 1051 */   setCurrentItemOrArmor(1, this.inventory.getStackInSlot(31));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateHealth() {
/* 1058 */     this.healthTicks++;
/* 1059 */     if (this.healthTicks == 60) {
/* 1060 */     heal(0.5F);
/* 1061 */       this.healthTicks = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateLeader() {
/* 1069 */     if (!this.worldObj.isRemote) {
/* 1070 */       if (this.leader != null) {
/* 1071 */         EntityLivingBase temp = (EntityLivingBase)this.worldObj.getEntityByID(this.leader.getEntityId());
/* 1072 */         setLeader(temp);
/* 1073 */       } else if (this.leaderID > 0) {
/* 1074 */         AbstractVillager temp = (AbstractVillager)HelpfulVillagers.villager_id.get(Integer.valueOf(this.leaderID));
/* 1075 */         setLeader((EntityLivingBase)temp);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateID() {
/* 1081 */     if (!this.worldObj.isRemote && this.ticksExisted > getRNG().nextInt(10) && this.villagerID <= 0) {
/* 1082 */       int newKey = Math.abs(getRNG().nextInt());
/* 1083 */       while (HelpfulVillagers.villager_id.containsKey(Integer.valueOf(newKey))) {
/* 1084 */         newKey = Math.abs(getRNG().nextInt());
/*      */       }
/* 1086 */       this.villagerID = newKey;
/* 1087 */       HelpfulVillagers.villager_id.put(Integer.valueOf(this.villagerID), this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void dayCheck() {
/* 1095 */     if (this.changeGuildHall) {
/* 1096 */       this.homeGuildHall = null;
/* 1097 */       this.changeGuildHall = false;
/*      */     } 
/*      */     
/* 1100 */     if (this.homeVillage != null) {
/* 1101 */       BlockPos center = this.homeVillage.getActualCenter();
/* 1102 */       setHomePosAndDistance(center, this.homeVillage.getActualRadius());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean shouldReproduce() {
/* 1110 */     if (this.homeVillage != null) {
/* 1111 */       return (Math.abs(this.worldObj.getTotalWorldTime() - this.homeVillage.getLastAdded()) >= 1000L);
/*      */     }
/* 1113 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean attackEntityFrom(DamageSource src, float par2) {
/* 1122 */     for (int i = 28; i < 32; i++) {
/* 1123 */       ItemStack armorPiece = this.inventory.getStackInSlot(i);
/* 1124 */       if (armorPiece != null) {
/* 1125 */         armorPiece.setItemDamage((int)(armorPiece.getItemDamage() + par2));
/* 1126 */         if (armorPiece.getItemDamage() >= armorPiece.getMaxDamage()) {
/* 1127 */           armorPiece = null;
/*      */         }
/* 1129 */         this.inventory.setInventorySlotContents(i, armorPiece);
/*      */       } 
/*      */     } 
/*      */     
/* 1133 */   if(!this.worldObj.isRemote && this.homeVillage != null && this.homeVillage.isInsideVillage((getCoords()).getX(), (getCoords()).getY(), (getCoords()).getZ())) {
/* 1134 */       Entity entity = src.getEntity();
/* 1135 */       if (entity != null && entity instanceof EntityLivingBase) {
/* 1136 */         EntityLivingBase attacker = (EntityLivingBase)entity;
/* 1137 */         if (attacker instanceof net.minecraft.entity.monster.IMob && attacker.isEntityAlive()) {
/* 1138 */           this.homeVillage.lastAggressor = attacker;
/*      */         }
/*      */       } 
/*      */     } 
/* 1142 */     return super.attackEntityFrom(src, par2);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getNewHomeVillage() {
/* 1166 */     if (this.hasDied) {
/*      */       return;
/*      */     }
/*      */     
/* 1170 */     if (this.homeVillage != null && this.homeVillage.isAnnihilated) {
/* 1171 */       this.homeVillage = null;
/*      */     }
/*      */     
/* 1174 */     if (this.homeVillage == null) {
/*      */       
/* 1176 */       if (this.villageCenter != null) {
/* 1177 */         for (int j = 0; j < HelpfulVillagers.villages.size(); j++) {
/* 1178 */           if (!((HelpfulVillage)HelpfulVillagers.villages.get(j)).isAnnihilated && ((HelpfulVillage)HelpfulVillagers.villages.get(j)).initialCenter.equals(this.villageCenter)) {
/* 1179 */             this.homeVillage = HelpfulVillagers.villages.get(j);
/* 1180 */             this.homeVillage.addVillager();
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       }
/* 1186 */       double closestDist = 100.0D;
/* 1187 */       HelpfulVillage closestVillage = null;
/* 1188 */       for (int i = 0; i < HelpfulVillagers.villages.size(); i++) {
/* 1189 */         HelpfulVillage currVillage = HelpfulVillagers.villages.get(i);
/* 1190 */         if (!currVillage.isAnnihilated) {
/*      */ 
/*      */           
/* 1193 */           BlockPos center = currVillage.getActualCenter();
/* 1194 */           double dist = getDistance(center.getX(), center.getY(), center.getZ());
/* 1195 */           if (currVillage.isInsideVillage(this.posX, this.posY, this.posZ) || dist < closestDist) {
/* 1196 */             closestDist = dist;
/* 1197 */             closestVillage = currVillage;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1201 */       if (closestVillage != null) {
/* 1202 */         this.homeVillage = closestVillage;
/* 1203 */         this.villageCenter = this.homeVillage.initialCenter;
/* 1204 */         this.homeVillage.addVillager();
/*      */       } else {
/* 1206 */         int x = (int)this.posX;
/* 1207 */         int z = (int)this.posZ;
				   BlockPos xyzpos = new BlockPos(x, this.posY, z);
/* 1208 */         BlockPos y = this.worldObj.getTopSolidOrLiquidBlock(xyzpos);
/* 1209 */         this.homeVillage = new HelpfulVillage(this.worldObj, y);
/* 1210 */         this.villageCenter = this.homeVillage.initialCenter;
/* 1211 */         HelpfulVillagers.villages.add(this.homeVillage);
/* 1212 */         this.homeVillage.addVillager();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void syncVillage() {
/* 1221 */     if (!this.worldObj.isRemote) {
/* 1222 */       HelpfulVillagers.network.sendToAll((IMessage)new VillageSyncPacket(this.homeVillage, this));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void returnToOrigin() {
/* 1231 */     if (this.homeVillage == null) {
/*      */       
/* 1233 */       getNavigator().tryMoveToXYZ(this.posX, this.posY, this.posZ, 0.30000001192092896D);
/*      */     } else {
/* 1235 */       getNavigator().tryMoveToXYZ((this.homeVillage.getActualCenter()).getX(), (this.homeVillage.getActualCenter()).getY(), (this.homeVillage.getActualCenter()).getZ(), 0.30000001192092896D);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getNewGuildHall() {
/* 1243 */     if (!this.worldObj.isRemote && this.homeVillage != null) {
/* 1244 */       if (this.profession == 0) {
/* 1245 */         this.homeGuildHall = null;
/* 1246 */       } else if (this.homeGuildHall == null) {
/* 1247 */         this.homeGuildHall = this.homeVillage.lookForExistingHall(this.profession);
/* 1248 */       } else if (this.homeGuildHall.itemFrame == null) {
/* 1249 */         this.homeGuildHall = this.homeVillage.lookForExistingHall(this.profession);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkGuildHall() {
/* 1258 */     if (this.currentActivity == EnumActivity.IDLE && this.homeGuildHall != null) {
/*      */       
/* 1260 */       if (!this.homeVillage.guildHallList.contains(this.homeGuildHall)) {
/* 1261 */         this.homeGuildHall = null;
/*      */       }
/*      */       
/* 1264 */       if (this.homeGuildHall == null || 
/* 1265 */         nearHall());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nearHall() {
/* 1277 */     if (this.homeGuildHall == null) {
/* 1278 */       return false;
/*      */     }
/*      */     
/* 1281 */     BlockPos currentPosition = new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ);
/*      */     
/* 1283 */     if (this.homeGuildHall.insideCoords.contains(currentPosition)) {
/* 1284 */       return true;
/*      */     }
/* 1286 */     ArrayList<BlockPos> adjacent = AIHelper.getAdjacentCoords(currentPosition);
/* 1287 */     for (BlockPos i : adjacent) {
/* 1288 */       if (this.homeGuildHall.insideCoords.contains(i)) {
/* 1289 */         return true;
/*      */       }
/*      */     } 
/* 1292 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean insideHall() {
/* 1300 */     BlockPos currentPosition = new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ);
/* 1301 */     if (this.homeGuildHall != null && this.homeGuildHall.insideCoords.contains(currentPosition)) {
/* 1302 */       return true;
/*      */     }
/* 1304 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onDeath(DamageSource src) {
/* 1314 */     super.onDeath(src);
/* 1315 */     if (!this.worldObj.isRemote && this.homeVillage != null) {
/* 1316 */       this.homeVillage.removeVillager();
/*      */     }
/*      */ 
/*      */     
/* 1320 */     this.hasDied = true;
/* 1321 */     this.canPickup = false;
/* 1322 */     this.inventory.dumpInventory();
/*      */     
/* 1324 */     if (this.currentCraftItem != null) {
/* 1325 */       addCraftItem(this.currentCraftItem);
/*      */     }
/*      */     
/* 1328 */     sendDeathMessage(src);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendDeathMessage(DamageSource src) {
/* 1336 */     if (!this.worldObj.isRemote) {
/* 1337 */       String name = getCustomNameTag();
/* 1338 */       if (name == null || name.equals("") || name.equals(" ")) {
/* 1339 */         name = "A " + this.profName;
/*      */       } else {
/* 1341 */         name = name + " the " + this.profName;
/*      */       } 
/*      */       
/* 1344 */       String cause = src.damageType;
/* 1345 */       Entity attacker = src.getEntity();
/* 1346 */       EntityLivingBase entityLivingBase = getAITarget();
/* 1347 */       if (cause.equals("anvil")) {
/* 1348 */         cause = " was squashed by an anvil";
/* 1349 */       } else if (cause.equals("cactus")) {
/* 1350 */         if (entityLivingBase != null && entityLivingBase.isEntityAlive()) {
/* 1351 */           cause = " walked into a cactus whilst trying to escape " + entityLivingBase.getCommandSenderEntity().getDisplayName();
/*      */         } else {
/* 1353 */           cause = " was pricked to death";
/*      */         } 
/* 1355 */       } else if (cause.equals("arrow")) {
/* 1356 */         if (attacker.getCommandSenderEntity().getDisplayName().equals("arrow")) {
/* 1357 */           cause = " was shot by an arrow";
/*      */         } else {
/* 1359 */           cause = " was shot by " + attacker.getCommandSenderEntity().getDisplayName();
/*      */         } 
/* 1361 */       } else if (cause.equals("drown")) {
/* 1362 */         if (entityLivingBase != null && entityLivingBase.isEntityAlive()) {
/* 1363 */           cause = " drowned whilst trying to escape " + entityLivingBase.getCommandSenderEntity().getDisplayName();
/*      */         } else {
/* 1365 */           cause = " drowned";
/*      */         } 
/* 1367 */       } else if (cause.equals("explosion")) {
/* 1368 */         cause = " blew up";
/* 1369 */       } else if (cause.equals("explosion.player")) {
/* 1370 */         cause = " was blown up by " + attacker.getCommandSenderEntity().getDisplayName();
/* 1371 */       } else if (cause.equals("fall")) {
/* 1372 */         if (entityLivingBase != null && entityLivingBase.isEntityAlive()) {
/* 1373 */           cause = " was doomed to fall by " + entityLivingBase.getCommandSenderEntity().getDisplayName();
/*      */         } else {
/* 1375 */           cause = " fell from a high place";
/*      */         } 
/* 1377 */       } else if (cause.equals("inFire")) {
/* 1378 */         EntityLivingBase entityLivingBase1 = getLastAttacker();
/* 1379 */         if (entityLivingBase1 != null && entityLivingBase1.isEntityAlive()) {
/* 1380 */           cause = " walked into a fire whilst fighting " + entityLivingBase1.getCommandSenderEntity().getDisplayName();
/* 1381 */         } else if (entityLivingBase != null && entityLivingBase.isEntityAlive()) {
/* 1382 */           cause = " walked into a fire whilst trying to escape " + entityLivingBase.getCommandSenderEntity().getDisplayName();
/*      */         } else {
/* 1384 */           cause = " went up in flames";
/*      */         } 
/* 1386 */       } else if (cause.equals("onFire")) {
/* 1387 */         EntityLivingBase entityLivingBase1 = getLastAttacker();
/* 1388 */         if (entityLivingBase1 != null && entityLivingBase1.isEntityAlive()) {
/* 1389 */           cause = " was burnt to a crisp whilst fighting " + entityLivingBase1.getCommandSenderEntity().getDisplayName();
/* 1390 */         } else if (entityLivingBase != null && entityLivingBase.isEntityAlive()) {
/* 1391 */           cause = " was burnt to a crisp whilst trying to escape " + entityLivingBase.getCommandSenderEntity().getDisplayName();
/*      */         } else {
/* 1393 */           cause = " burned to death";
/*      */         } 
/* 1395 */       } else if (cause.equals("mob")) {
/* 1396 */         cause = " was slain by a " + attacker.getCommandSenderEntity().getDisplayName();
/* 1397 */       } else if (cause.equals("player")) {
/* 1398 */         cause = " was slain by " + attacker.getCommandSenderEntity().getDisplayName();
/* 1399 */       } else if (cause.equals("fireball")) {
/* 1400 */         cause = " was fireballed by a " + attacker.getCommandSenderEntity().getDisplayName();
/* 1401 */       } else if (cause.equals("indirectMagic")) {
/* 1402 */         cause = " was killed by " + attacker.getCommandSenderEntity().getDisplayName() + " using magic";
/* 1403 */       } else if (cause.equals("magic")) {
/* 1404 */         cause = " was killed by magic";
/* 1405 */       } else if (cause.equals("inWall")) {
/* 1406 */         cause = " suffocated in a wall";
/* 1407 */       } else if (cause.equals("lava")) {
/* 1408 */         if (entityLivingBase != null && entityLivingBase.isEntityAlive()) {
/* 1409 */           cause = " tried to swim in lava while trying to escape " + entityLivingBase.getCommandSenderEntity().getDisplayName();
/*      */         } else {
/* 1411 */           cause = " tried to swim in lava";
/*      */         } 
/* 1413 */       } else if (cause.equals("outOfWorld")) {
/* 1414 */         if (entityLivingBase != null && entityLivingBase.isEntityAlive()) {
/* 1415 */           cause = " was knocked into the void by " + entityLivingBase.getCommandSenderEntity().getDisplayName();
/*      */         } else {
/* 1417 */           cause = " fell out of the world";
/*      */         } 
/* 1419 */       } else if (cause.equals("wither")) {
/* 1420 */         cause = " withered away";
/* 1421 */       } else if (cause.equals("fallingBlock")) {
/* 1422 */         cause = " was squashed by a falling block";
/*      */       } else {
/* 1424 */         cause = " died";
/*      */       } 
/* 1426 */       String message = name + cause;
/* 1427 */       HelpfulVillagers.network.sendToAll((IMessage)new PlayerMessagePacket(message, EnumMessage.DEATH, getEntityId()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean shouldReturn() {
/* 1435 */     return (!this.inventory.isFull() && this.hasTool && this.worldObj.isDaytime() && this.currentCraftItem == null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract ArrayList getValidCoords();
/*      */ 
/*      */   
/*      */   public float getAttackDamage() {
/* 1444 */     if (getCurrentItem() != null && getCurrentItem().getItem() instanceof ItemSword) {
/* 1445 */       ItemSword sword = (ItemSword)getCurrentItem().getItem();
/* 1446 */       return sword.getDamageVsEntity() + 4.0F;
/*      */     } 
/*      */     
/* 1449 */     if (getCurrentItem() != null && getCurrentItem().getItem() instanceof net.minecraft.item.ItemBow) {
/* 1450 */       return 4.0F;
/*      */     }
/*      */ 
/*      */     
/* 1454 */     if (getCurrentItem() != null && getCurrentItem().getItem() instanceof ItemTool) {
/* 1455 */       ItemTool tool = (ItemTool)getCurrentItem().getItem();
/* 1456 */       Multimap map = tool.getItemAttributeModifiers();
/* 1457 */       String s = map.get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName()).toString();
/* 1458 */       String sAmount = "amount=";
/* 1459 */       int i = s.indexOf(sAmount) + sAmount.length();
/* 1460 */       int j = s.indexOf(",");
/* 1461 */       sAmount = s.substring(i, j);
/* 1462 */       float f = Float.parseFloat(sAmount);
/*      */     } 
/*      */     
/* 1465 */     if (this instanceof EntityFarmer) {
/* 1466 */       EntityFarmer farmer = (EntityFarmer)this;
/* 1467 */       return Math.abs(farmer.getHarvestTime() / 10 - 6);
/*      */     } 
/*      */     
/* 1470 */     return 1.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCraftItem(CraftItem item) {
/* 1477 */     if (this.homeVillage != null && item != null) {
/* 1478 */       if (item.getPriority() <= 0) {
/* 1479 */         this.homeVillage.craftQueue.addVillagerItem(item);
/* 1480 */       } else if (item.getPriority() >= 1) {
/* 1481 */         this.homeVillage.craftQueue.addPlayerItem(item);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getCraftItem() {
/* 1490 */ 		if(!this.worldObj.isRemote && this.homeVillage != null && canCraft() && this.currentCraftItem == null) {
/* 1491 */       this.homeVillage.craftQueue.getCraftItem(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetCraftItem() {
/* 1499 */     this.currentCraftItem = null;
/* 1500 */     this.materialsNeeded.clear();
/* 1501 */     this.smeltablesNeeded.clear();
/* 1502 */     this.inventory.materialsCollected.clear();
/* 1503 */     this.craftChain.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void lookForItem(ItemStack item) {
/* 1511 */     ItemStack tempItem = item.copy();
/* 1512 */     for (int i = 0; i < this.homeVillage.guildHallList.size(); i++) {
/* 1513 */       GuildHall hall = this.homeVillage.guildHallList.get(i);
/*      */ 
/*      */       
/* 1516 */       hall.checkChests();
/* 1517 */       ArrayList<TileEntityChest> chests = hall.guildChests;
/* 1518 */       for (int j = 0; j < chests.size(); j++) {
/* 1519 */         TileEntityChest chest = chests.get(j);
/* 1520 */         boolean stopSearch = AIHelper.takeItemFromChest(tempItem, chest, this);
/* 1521 */         if (stopSearch) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1527 */       hall.checkFurnaces();
/* 1528 */       ArrayList<TileEntityFurnace> furnaces = hall.guildFurnaces;
/* 1529 */       for (int k = 0; k < furnaces.size(); k++) {
/* 1530 */         TileEntityFurnace furnace = furnaces.get(k);
/* 1531 */         boolean stopSearch = AIHelper.takeItemFromFurnace(tempItem, furnace, this);
/* 1532 */         if (stopSearch) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean storeCraftedItem() {
/* 1544 */     Iterator<GuildHall> iterator = this.homeVillage.guildHallList.iterator();
/* 1545 */     while (iterator.hasNext()) {
/* 1546 */       GuildHall hall = iterator.next();
/* 1547 */       if (hall.typeMatchesName(this.profName)) {
/* 1548 */         hall.checkChests();
/* 1549 */         Iterator<TileEntityChest> iterator1 = hall.guildChests.iterator();
/* 1550 */         while (iterator1.hasNext()) {
/* 1551 */           TileEntityChest chest = iterator1.next();
/* 1552 */           for (int i = 0; i < chest.getSizeInventory(); i++) {
/* 1553 */             if (chest.getStackInSlot(i) == null) {
/* 1554 */               chest.setInventorySlotContents(i, this.currentCraftItem.getItem());
/* 1555 */               return true;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1561 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeEntityToNBT(NBTTagCompound compound) {
/* 1569 */     super.writeEntityToNBT(compound);
/*      */ 
/*      */     
/* 1572 */     if (this.villageCenter != null) {
/* 1573 */       int[] villageCoords = new int[3];
/* 1574 */       villageCoords[0] = this.villageCenter.getX();
/* 1575 */       villageCoords[1] = this.villageCenter.getY();
/* 1576 */       villageCoords[2] = this.villageCenter.getZ();
/* 1577 */       compound.setTag("Village", (NBTBase)new NBTTagIntArray(villageCoords));
/*      */     } 
/*      */ 
/*      */     
/* 1581 */     compound.setTag("Inventory", (NBTBase)this.inventory.writeToNBT(new NBTTagList()));
/*      */ 
/*      */     
/* 1584 */     if (this.lastResource != null) {
/* 1585 */       compound.setTag("Resource", (NBTBase)this.lastResource.writeToNBT(new NBTTagList()));
/*      */     }
/*      */ 
/*      */     
/* 1589 */     compound.setTag("VillagerID", (NBTBase)new NBTTagInt(this.villagerID));
/*      */ 
/*      */     
/* 1592 */     compound.setTag("LeaderID", (NBTBase)new NBTTagInt(this.leaderID));
/*      */ 
/*      */     
/* 1595 */     if (this.currentCraftItem != null) {
/* 1596 */       NBTTagCompound craftCompound = new NBTTagCompound();
/* 1597 */       this.currentCraftItem.writeToNBT(craftCompound);
/* 1598 */       compound.setTag("Craft Item", (NBTBase)craftCompound);
/*      */     } 
/*      */ 
/*      */     
/* 1602 */     compound.setTag("CustomSize", (NBTBase)new NBTTagInt(this.customRecipes.size()));
/* 1603 */     for (int i = 0; i < this.customRecipes.size(); i++) {
/* 1604 */       compound.setTag("CustomRecipe" + i, (NBTBase)((VillagerRecipe)this.customRecipes.get(i)).writeToNBT(new NBTTagList()));
/*      */     }
/*      */ 
/*      */     
/* 1608 */     if (this.queuedTool != null) {
/* 1609 */       NBTTagCompound queuedCompound = new NBTTagCompound();
/* 1610 */       this.queuedTool.writeToNBT(queuedCompound);
/* 1611 */       compound.setTag("Queued Tool", (NBTBase)queuedCompound);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void readEntityFromNBT(NBTTagCompound compound) {
/* 1621 */     super.readEntityFromNBT(compound);
/*      */ 
/*      */     
/* 1624 */     int[] village = compound.getIntArray("Village");
/* 1625 */     if (village.length > 0) {
/* 1626 */       this.villageCenter = new BlockPos(village[0], village[1], village[2]);
/* 1627 */       if (HelpfulVillagers.villageCollection == null || HelpfulVillagers.villageCollection.isEmpty()) {
/*      */         
/* 1629 */         boolean addVillage = true;
/* 1630 */         for (int j = 0; j < HelpfulVillagers.villages.size(); j++) {
/* 1631 */           HelpfulVillage currVillage = HelpfulVillagers.villages.get(j);
/* 1632 */           if (currVillage.initialCenter.equals(this.villageCenter)) {
/* 1633 */             this.homeVillage = currVillage;
/* 1634 */             this.homeVillage.addVillager();
/* 1635 */             addVillage = false;
/*      */             break;
/*      */           } 
/*      */         } 
/* 1639 */         if (addVillage) {
/* 1640 */           this.homeVillage = new HelpfulVillage(this.worldObj, this.villageCenter);
/* 1641 */           HelpfulVillagers.villages.add(this.homeVillage);
/* 1642 */           this.homeVillage.addVillager();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1648 */     NBTTagList nbttaglist = compound.getTagList("Inventory", compound.getId());
/* 1649 */     this.inventory.readFromNBT(nbttaglist);
/*      */ 
/*      */     
/* 1652 */     if (compound.hasKey("Resource")) {
/* 1653 */       this.lastResource = new ResourceCluster(this.worldObj);
/* 1654 */       nbttaglist = compound.getTagList("Resource", compound.getId());
/* 1655 */       this.lastResource.readFromNBT(nbttaglist);
/*      */     } 
/*      */ 
/*      */     
/* 1659 */     this.villagerID = compound.getInteger("VillagerID");
/* 1660 */     HelpfulVillagers.villager_id.put(Integer.valueOf(this.villagerID), this);
/*      */ 
/*      */     
/* 1663 */     this.leaderID = compound.getInteger("LeaderID");
/*      */ 
/*      */     
/* 1666 */     NBTTagCompound craftCompound = (NBTTagCompound)compound.getTag("Craft Item");
/* 1667 */     if (craftCompound != null) {
/* 1668 */       this.currentCraftItem = CraftItem.loadCraftItemFromNBT(craftCompound);
/*      */     }
/*      */ 
/*      */     
/* 1672 */     int size = compound.getInteger("CustomSize");
/* 1673 */     for (int i = 0; i < size; i++) {
/* 1674 */       nbttaglist = compound.getTagList("CustomRecipe" + i, compound.getId());
/* 1675 */       VillagerRecipe recipe = new VillagerRecipe();
/* 1676 */       recipe.readFromNBT(nbttaglist);
/* 1677 */       this.customRecipes.add(recipe);
/*      */     } 
/* 1679 */     this.knownRecipes.addAll(this.customRecipes);
/* 1680 */     Collections.sort(this.knownRecipes);
/*      */ 
/*      */     
/* 1683 */     NBTTagCompound queuedCompound = (NBTTagCompound)compound.getTag("Queued Tool");
/* 1684 */     if (queuedCompound != null)
/* 1685 */       this.queuedTool = ItemStack.loadItemStackFromNBT(queuedCompound); 
/*      */   }
/*      */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\AbstractVillager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */