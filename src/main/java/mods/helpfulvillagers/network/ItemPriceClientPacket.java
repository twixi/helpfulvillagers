/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.econ.ItemPrice;
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
/*    */ public class ItemPriceClientPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private ItemStack item;
/*    */   private int price;
/*    */   private double supply;
/*    */   private double demand;
/*    */   
/*    */   public ItemPriceClientPacket() {}
/*    */   
/*    */   public ItemPriceClientPacket(AbstractVillager villager, ItemPrice itemPrice) {
/* 32 */     this.id = villager.getEntityId();
/* 33 */     this.item = itemPrice.getItem();
/* 34 */     this.price = itemPrice.getOriginalPrice();
/* 35 */     this.supply = itemPrice.getSupply();
/* 36 */     this.demand = itemPrice.getDemand();
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 40 */     buffer.writeInt(this.id);
/*    */     
/* 42 */     buffer.writeInt(this.price);
/* 43 */     buffer.writeDouble(this.supply);
/* 44 */     buffer.writeDouble(this.demand);
/* 45 */     ByteBufUtils.writeItemStack(buffer, this.item);
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 49 */     this.id = buffer.readInt();
/*    */     
/* 51 */     this.price = buffer.readInt();
/* 52 */     this.supply = buffer.readDouble();
/* 53 */     this.demand = buffer.readDouble();
/* 54 */     this.item = ByteBufUtils.readItemStack(buffer);
/*    */   }
/*    */   
/*    */   public static class Handler implements IMessageHandler<ItemPriceClientPacket, IMessage> {
/*    */     public IMessage onMessage(ItemPriceClientPacket message, MessageContext ctx) {
/*    */       try {
/* 60 */         if (ctx.side == Side.CLIENT) {
/* 61 */           Minecraft mc = Minecraft.getMinecraft();
/* 62 */           WorldClient world = mc.theWorld;
/* 63 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 64 */           if (entity == null) {
/* 65 */             return null;
/*    */           }
/*    */           
/* 68 */           ItemPrice itemPrice = new ItemPrice(message.item, message.price, message.supply, message.demand);
/* 69 */           entity.homeVillage.economy.putItemPrice(itemPrice);
/*    */         } 
/* 71 */       } catch (NullPointerException e) {
/* 72 */         e.printStackTrace();
/*    */       } 
/* 74 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\ItemPriceClientPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */