/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.entity.EntityBuilder;
/*    */ import mods.helpfulvillagers.enums.EnumConstructionType;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConstructionJobPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int villagerID;
/*    */   private int playerID;
/*    */   private int command;
/*    */   
/*    */   public ConstructionJobPacket() {}
/*    */   
/*    */   public ConstructionJobPacket(int villagerID, int playerID, int command) {
/* 31 */     this.villagerID = villagerID;
/* 32 */     this.playerID = playerID;
/* 33 */     this.command = command;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 38 */     buffer.writeInt(this.villagerID);
/* 39 */     buffer.writeInt(this.playerID);
/* 40 */     buffer.writeInt(this.command);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 45 */     this.villagerID = buffer.readInt();
/* 46 */     this.playerID = buffer.readInt();
/* 47 */     this.command = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<ConstructionJobPacket, IMessage> {
/*    */     public IMessage onMessage(ConstructionJobPacket message, MessageContext ctx) {
/*    */       try {
/* 54 */         if (ctx.side == Side.SERVER) {
/* 55 */           EntityBuilder entity; EntityPlayerMP p = (ctx.getServerHandler()).playerEntity;
/* 56 */           World world = p.worldObj;
/*    */ 
/*    */           
/*    */           try {
/* 60 */             entity = (EntityBuilder)world.getEntityByID(message.villagerID);
/* 61 */           } catch (Exception e) {
/* 62 */             e.printStackTrace();
/* 63 */             return null;
/*    */           } 
/*    */           
/* 66 */           EntityPlayer player = (EntityPlayer)world.getEntityByID(message.playerID);
/*    */           
/*    */           try {
/* 69 */             EnumConstructionType type = EnumConstructionType.values()[message.command];
/* 70 */             entity.processJobRequest(type, player);
/* 71 */           } catch (ArrayIndexOutOfBoundsException e) {
/* 72 */             System.out.println("Invalid Construction Type");
/*    */           } 
/*    */         } 
/* 75 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 78 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\ConstructionJobPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */