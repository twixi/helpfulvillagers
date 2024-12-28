/*     */ package mods.helpfulvillagers.network;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
			import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
				import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
				import net.minecraftforge.fml.relauncher.Side;
				import net.minecraftforge.fml.common.network.ByteBufUtils;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.crafting.VillagerRecipe;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import net.minecraft.item.ItemStack;
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
/*     */ public class AddRecipePacket
/*     */   implements IMessage
/*     */ {
/*     */   private int id;
/*     */   private VillagerRecipe recipe;
/*     */   private int flag;
/*     */   
/*     */   public AddRecipePacket() {}
/*     */   
/*     */   public AddRecipePacket(int id, VillagerRecipe recipe, int flag) {
/*  36 */     this.id = id;
/*  37 */     this.recipe = recipe;
/*  38 */     this.flag = flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toBytes(ByteBuf buffer) {
/*  43 */     buffer.writeInt(this.id);
/*     */     
/*  45 */     int length = this.recipe.getTotalInputs().size();
/*  46 */     buffer.writeInt(length);
/*  47 */     for (int j = 0; j < length; j++) {
/*  48 */       ItemStack input = this.recipe.getTotalInputs().get(j);
/*  49 */       ByteBufUtils.writeItemStack(buffer, input);
/*     */     } 
/*  51 */     ByteBufUtils.writeItemStack(buffer, this.recipe.getOutput());
/*  52 */     buffer.writeBoolean(this.recipe.isSmelted());
/*     */     
/*  54 */     buffer.writeInt(this.flag);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fromBytes(ByteBuf buffer) {
/*  59 */     this.id = buffer.readInt();
/*     */     
/*  61 */     ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();
/*     */     
/*  63 */     int length = buffer.readInt();
/*  64 */     for (int j = 0; j < length; j++) {
/*  65 */       ItemStack input = ByteBufUtils.readItemStack(buffer);
/*  66 */       inputs.add(input);
/*     */     } 
/*  68 */     ItemStack output = ByteBufUtils.readItemStack(buffer);
/*  69 */     boolean smelt = buffer.readBoolean();
/*     */     
/*  71 */     this.recipe = new VillagerRecipe(inputs, output, smelt);
/*     */     
/*  73 */     this.flag = buffer.readInt();
/*     */   }
/*     */   
/*     */   public static class Handler
/*     */     implements IMessageHandler<AddRecipePacket, IMessage> {
/*     */     public IMessage onMessage(AddRecipePacket message, MessageContext ctx) {
/*     */       try {
/*  80 */         if (ctx.side == Side.SERVER) {
/*  81 */           World world = (ctx.getServerHandler()).playerEntity.worldObj;
/*  82 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/*  83 */           if (entity == null) {
/*  84 */             return null;
/*     */           }
/*     */           
/*  87 */           switch (message.flag) {
/*     */             case 0:
/*  89 */               entity.addCustomRecipe(message.recipe);
/*     */               break;
/*     */             
/*     */             case 1:
/*  93 */               entity.replaceCustomRecipe(message.recipe);
/*     */               break;
/*     */             
/*     */             case 2:
/*  97 */               entity.deleteCustomRecipe(message.recipe);
/*     */               break;
/*     */           } 
/*     */         
/*     */         } 
/* 102 */       } catch (NullPointerException e) {}
/*     */ 
/*     */       
/* 105 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\AddRecipePacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */