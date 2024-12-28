/*     */ package mods.helpfulvillagers.network;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import mods.helpfulvillagers.econ.ItemPrice;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.village.HelpfulVillage;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
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
/*     */ public class VillageEconomyClientPacket
/*     */   implements IMessage
/*     */ {
/*  36 */   private int[] coords = new int[3];
/*     */   
/*     */   private int size;
/*     */   
/*     */   private String[] strings;
/*     */   
/*     */   private ItemStack[] items;
/*     */   
/*     */   private int[] prices;
/*     */   private double[] supply;
/*     */   private double[] demand;
/*     */   
/*     */   public VillageEconomyClientPacket(HelpfulVillage village) {
/*  49 */     this.coords[0] = village.initialCenter.getX();
/*  50 */     this.coords[1] = village.initialCenter.getY();
/*  51 */     this.coords[2] = village.initialCenter.getZ();
/*     */     
/*  53 */     this.size = village.economy.getItemPrices().size();
/*     */     
/*  55 */     this.strings = new String[this.size];
/*  56 */     this.items = new ItemStack[this.size];
/*  57 */     this.prices = new int[this.size];
/*  58 */     this.supply = new double[this.size];
/*  59 */     this.demand = new double[this.size];
/*  60 */     int i = 0;
/*  61 */     for (Map.Entry<String, ItemPrice> entry : (Iterable<Map.Entry<String, ItemPrice>>)village.economy.getItemPrices().entrySet()) {
/*  62 */       this.strings[i] = entry.getKey();
/*  63 */       ItemPrice item = entry.getValue();
/*  64 */       this.items[i] = item.getItem();
/*  65 */       this.prices[i] = item.getPrice();
/*  66 */       this.supply[i] = item.getSupply();
/*  67 */       this.demand[i] = item.getDemand();
/*  68 */       i++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void toBytes(ByteBuf buffer) {
/*     */     int i;
/*  75 */     for (i = 0; i < 3; i++) {
/*  76 */       buffer.writeInt(this.coords[i]);
/*     */     }
/*     */     
/*  79 */     buffer.writeInt(this.size);
/*     */     
/*  81 */     for (i = 0; i < this.size; i++) {
/*  82 */       ByteBufUtils.writeUTF8String(buffer, this.strings[i]);
/*  83 */       ByteBufUtils.writeItemStack(buffer, this.items[i]);
/*  84 */       buffer.writeInt(this.prices[i]);
/*  85 */       buffer.writeDouble(this.supply[i]);
/*  86 */       buffer.writeDouble(this.demand[i]);
/*     */     } 
/*     */   }
/*     */   public VillageEconomyClientPacket() {}
/*     */   public void fromBytes(ByteBuf buffer) {
/*     */     int i;
/*  92 */     for (i = 0; i < 3; i++) {
/*  93 */       this.coords[i] = buffer.readInt();
/*     */     }
/*     */     
/*  96 */     this.size = buffer.readInt();
/*  97 */     this.strings = new String[this.size];
/*  98 */     this.items = new ItemStack[this.size];
/*  99 */     this.prices = new int[this.size];
/* 100 */     this.supply = new double[this.size];
/* 101 */     this.demand = new double[this.size];
/*     */     
/* 103 */     for (i = 0; i < this.size; i++) {
/* 104 */       this.strings[i] = ByteBufUtils.readUTF8String(buffer);
/* 105 */       this.items[i] = ByteBufUtils.readItemStack(buffer);
/* 106 */       this.prices[i] = buffer.readInt();
/* 107 */       this.supply[i] = buffer.readInt();
/* 108 */       this.demand[i] = buffer.readInt();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class Handler
/*     */     implements IMessageHandler<VillageEconomyClientPacket, IMessage> {
/*     */     public IMessage onMessage(VillageEconomyClientPacket message, MessageContext ctx) {
/*     */       try {
/* 116 */         if (ctx.side == Side.CLIENT) {
/* 117 */           BlockPos coords = new BlockPos(message.coords[0], message.coords[1], message.coords[2]);
/* 118 */           HashMap<String, ItemPrice> itemPrices = new HashMap<String, ItemPrice>();
/* 119 */           for (HelpfulVillage v : HelpfulVillagers.villages) {
/* 120 */             if (v.initialCenter.equals(coords)) {
/* 121 */               for (int i = 0; i < message.size; i++) {
/* 122 */                 ItemPrice itemPrice = new ItemPrice(message.items[i], message.prices[i], message.supply[i], message.demand[i]);
/* 123 */                 itemPrices.put(message.strings[i], itemPrice);
/*     */               } 
/*     */ 
/*     */               
/* 127 */               System.out.println("MESSAGE SENT");
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 133 */       } catch (NullPointerException e) {}
/*     */ 
/*     */       
/* 136 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\VillageEconomyClientPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */