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
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemPriceServerPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private ItemStack item;
/*    */   private int price;
/*    */   private double supply;
/*    */   private double demand;
/*    */   
/*    */   public ItemPriceServerPacket() {}
/*    */   
/*    */   public ItemPriceServerPacket(AbstractVillager villager, ItemPrice itemPrice) {
/* 33 */     this.id = villager.getEntityId();
/* 34 */     this.item = itemPrice.getItem();
/* 35 */     this.price = itemPrice.getOriginalPrice();
/* 36 */     this.supply = itemPrice.getSupply();
/* 37 */     this.demand = itemPrice.getDemand();
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 41 */     buffer.writeInt(this.id);
/*    */     
/* 43 */     buffer.writeInt(this.price);
/* 44 */     buffer.writeDouble(this.supply);
/* 45 */     buffer.writeDouble(this.demand);
/* 46 */     ByteBufUtils.writeItemStack(buffer, this.item);
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 50 */     this.id = buffer.readInt();
/*    */     
/* 52 */     this.price = buffer.readInt();
/* 53 */     this.supply = buffer.readDouble();
/* 54 */     this.demand = buffer.readDouble();
/* 55 */     this.item = ByteBufUtils.readItemStack(buffer);
/*    */   }
/*    */   
/*    */   public static class Handler implements IMessageHandler<ItemPriceServerPacket, IMessage> {
/*    */     public IMessage onMessage(ItemPriceServerPacket message, MessageContext ctx) {
/*    */       try {
/* 61 */         if (ctx.side == Side.SERVER) {
/* 62 */           World world = (ctx.getServerHandler()).playerEntity.worldObj;
/* 63 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 64 */           if (entity == null) {
/* 65 */             return null;
/*    */           }
/*    */           
/* 68 */           ItemPrice itemPrice = new ItemPrice(message.item, message.price, message.supply, message.demand);
/* 69 */           entity.homeVillage.economy.putItemPrice(itemPrice);
/*    */         } 
/* 71 */       } catch (NullPointerException e) {}
/*    */ 
/*    */ 
/*    */       
/* 75 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\ItemPriceServerPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */