/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import mods.helpfulvillagers.crafting.VillagerRecipe;
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomRecipesPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private int size;
/* 31 */   private ArrayList<VillagerRecipe> recipes = new ArrayList<VillagerRecipe>();
/*    */ 
/*    */ 
/*    */   
/*    */   public CustomRecipesPacket(int id, ArrayList<VillagerRecipe> recipes) {
/* 36 */     this.id = id;
/* 37 */     this.size = recipes.size();
/* 38 */     this.recipes.addAll(recipes);
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 43 */     buffer.writeInt(this.id);
/*    */     
/* 45 */     buffer.writeInt(this.size);
/*    */     
/* 47 */     for (VillagerRecipe i : this.recipes) {
/* 48 */       int length = i.getTotalInputs().size();
/* 49 */       buffer.writeInt(length);
/* 50 */       for (int j = 0; j < length; j++) {
/* 51 */         ItemStack input = i.getTotalInputs().get(j);
/* 52 */         ByteBufUtils.writeItemStack(buffer, input);
/*    */       } 
/* 54 */       ByteBufUtils.writeItemStack(buffer, i.getOutput());
/*    */     } 
/*    */   }
/*    */   public CustomRecipesPacket() {}
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 60 */     this.id = buffer.readInt();
/*    */     
/* 62 */     this.size = buffer.readInt();
/*    */     
/* 64 */     ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();
/*    */     
/* 66 */     for (int i = 0; i < this.size; i++) {
/* 67 */       int length = buffer.readInt();
/* 68 */       for (int j = 0; j < length; j++) {
/* 69 */         ItemStack input = ByteBufUtils.readItemStack(buffer);
/* 70 */         inputs.add(input);
/*    */       } 
/* 72 */       ItemStack output = ByteBufUtils.readItemStack(buffer);
/* 73 */       this.recipes.add(new VillagerRecipe(inputs, output, false));
/* 74 */       inputs.clear();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<CustomRecipesPacket, IMessage> {
/*    */     public IMessage onMessage(CustomRecipesPacket message, MessageContext ctx) {
/*    */       try {
/* 82 */         if (ctx.side == Side.CLIENT) {
/* 83 */           Minecraft mc = Minecraft.getMinecraft();
/* 84 */           WorldClient world = mc.theWorld;
/* 85 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 86 */           if (entity == null) {
/* 87 */             return null;
/*    */           }
/*    */           
/* 90 */           entity.resetRecipes();
/* 91 */           entity.customRecipes.addAll(message.recipes);
/* 92 */           entity.knownRecipes.addAll(message.recipes);
/* 93 */           Collections.sort(entity.knownRecipes);
/*    */         } 
/* 95 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 98 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\CustomRecipesPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */