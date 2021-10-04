package nuparu.sevendaystomine.events;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity.SleepResult;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.client.sound.MovingSoundChainsawCut;
import nuparu.sevendaystomine.client.sound.MovingSoundChainsawIdle;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemBackpack;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncScrapsMessage;
import nuparu.sevendaystomine.util.PlayerUtils;
import nuparu.sevendaystomine.util.Utils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PlayerEventHandler {

    public static final Method m_addSlotToContainer = ObfuscationReflectionHelper.findMethod(Container.class, "func_75146_a",
            Slot.class);
    public static long nextChainsawCutSound = 0l;
    protected static long nextChainsawIdleSound = 0l;
    protected static long lastTimeHittingBlock = 0l;
    public Field f_allInventories;

    public static long getLastTimeHittingBlock() {
        return lastTimeHittingBlock;
    }

    @SubscribeEvent
    public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos().relative(event.getFace());
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();
        BlockState state = world.getBlockState(event.getPos());
        Block block = state.getBlock();

        if (item instanceof BlockItem) {
            if (!stack.isEmpty()) {
				/*List<EntityLootableCorpse> list = world.getEntitiesOfClass(EntityLootableCorpse.class,
						new AxisAlignedBB(pos, pos.offset(1, 1, 1)));
				if (list.size() > 0) {
					event.setCanceled(true);
				}*/
            }
        }

        if (item == Items.SHEARS) {
            TileEntity tileEntity = world.getBlockEntity(event.getPos());
            if(tileEntity != null && tileEntity instanceof IVoltage){
            	IVoltage voltage = (IVoltage)tileEntity;
            	/*int inputs = 0;
            	for(ElectricConnection connection : voltage.getInputs()){
            		inputs++;
            		voltage.disconnect(connection.getFrom(world));
				}*/
				int outputs = 0;
				for(ElectricConnection connection : new ArrayList<ElectricConnection>(voltage.getOutputs())){
                    outputs++;
					voltage.disconnect(connection.getTo(world));
				}

				if(outputs > 0) {
					ItemStack wires = new ItemStack(ModItems.WIRE.get(), outputs);
                    InventoryHelper.dropItemStack(world, event.getPos().getX()+0.5,event.getPos().getY()+0.5,event.getPos().getZ()+0.5, wires);
                    stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(event.getHand()));
                    world.playSound((PlayerEntity)null, player, SoundEvents.SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
				}
			}

        }
    }

    private final ResourceLocation NO_BACKPACK_SLOT = new ResourceLocation(SevenDaysToMine.MODID,"items/empty_backpack_slot");

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) {
            return;
        }
        if (!CommonConfig.backpackSlot.get())
            return;
        final PlayerEntity player = (PlayerEntity) event.getEntity();

        IItemHandler extendedInv = player.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null)
                .orElseGet(null);
        Container container = player.containerMenu;
        Slot slotBackpack = new SlotItemHandler(extendedInv, 0, 77, 44) {

            @OnlyIn(Dist.CLIENT)
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, NO_BACKPACK_SLOT);
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return !stack.isEmpty() && stack.getItem() instanceof ItemBackpack;
            }

            @OnlyIn(Dist.CLIENT)
            @Override
            public boolean isActive() {
                return (!player.isCreative() && !player.isSpectator());
            }
        };
        /*
         * Slot slot1 = new SlotItemHandler(extendedInv, 1, 77, 26); Slot slot2 = new
         * SlotItemHandler(extendedInv, 2, 77, 8);
         */
        addSlot(slotBackpack, container);

        /*
         * addSlot(slot1, container); addSlot(slot2, container);
         */

    }

    public void addSlot(Slot slot, Container container) {
        try {
            m_addSlotToContainer.invoke(container, slot);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity))
            return;
        final PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        IItemHandlerExtended extendedInv = CapabilityHelper.getExtendedInventory(player);
        if(player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;
        if (extendedInv == null)
            return;
        for (int i = 0; i < extendedInv.getSlots(); i++) {
            ItemStack stack = extendedInv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                player.drop(stack, false, false);
            }
        } /*
         * player.level.getScoreboard().addPlayerToTeam(player.getName(), "death");
         * player.level.getScoreboard().addScoreObjective("death",
         * IScoreCriteria.DUMMY);
         * player.level.getScoreboard().setObjectiveInDisplaySlot(0,
         * player.level.getScoreboard().getObjective("death"));
         */
        // player.level.getScoreboard().getOrCreateScore(player.getName(),
        // player.level.getScoreboard().getObjective("death")).setScorePoints(player.totalExperience);
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        if (event.getHand() == Hand.OFF_HAND)
            return;

        ItemStack stack = player.getMainHandItem();

        if (event.getResult() != Result.DENY && stack.isEmpty() && !player.isCrouching() && !player.isCreative()
                && !player.isSpectator()) {
            handleDrinkingWater(player);
        }
    }

    public void handleDrinkingWater(PlayerEntity player) {
        IExtendedPlayer ep = CapabilityHelper.getExtendedPlayer(player);

        World world = player.level;
        BlockRayTraceResult ray = getMovingObjectPositionFromPlayer(world, player, RayTraceContext.FluidMode.ANY);

        if (ray != null) {
            boolean flag = false;
            BlockPos blockpos = ray.getBlockPos();
            BlockState state = world.getBlockState(blockpos);
            if (state.getMaterial() == Material.WATER) {
                //
                flag = true;
            } else if (state.getBlock() instanceof CauldronBlock) {
                int level = state.getValue(CauldronBlock.LEVEL);
                if (level > 0) {
                    flag = true;/*
                     * if (!world.isClientSide()) { world.setBlockState(blockpos,
                     * state.setValue(CauldronBlock.LEVEL, level - 1)); }
                     */
                }
            }
            if (flag) {
                world.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK,
                        SoundCategory.BLOCKS, 0.2F, world.random.nextFloat() * 0.1F + 0.9F, false);
                if (!world.isClientSide()) {
                    /*
                     * ep.addThirst(35); ep.addStamina(20);
                     * player.removeEffect(Potions.THIRST.get());
                     *
                     * if (world.random.nextInt(10) == 0) { Effect effect = new
                     * Effect(Potions.dysentery, world.random.nextInt(4000) + 18000, 4, false,
                     * false); effect.setCurativeItems(new ArrayList<ItemStack>());
                     * player.addEffect(effect); }
                     */
                    ep.setDrinkCounter(ep.getDrinkCounter() + 10);
                }
            }

        }
    }

    public BlockRayTraceResult getMovingObjectPositionFromPlayer(World worldIn, PlayerEntity playerIn,
                                                                 RayTraceContext.FluidMode fluidMode) {
        float f = playerIn.xRot;
        float f1 = playerIn.yRot;
        double d0 = playerIn.getX();
        double d1 = playerIn.getY() + (double) playerIn.getEyeHeight();
        double d2 = playerIn.getZ();
        Vector3d vec3 = new Vector3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
        Vector3d vector3d1 = vec3.add((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
        return worldIn
                .clip(new RayTraceContext(vec3, vector3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, playerIn));
    }

    @SubscribeEvent
    public void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
        float speed = (float) (event.getOriginalSpeed()
                / (CommonConfig.immersiveBlockBreaking.get() && (event.getState().getMaterial() != Material.DECORATION
                && event.getState().getMaterial() != Material.WEB)
                ? CommonConfig.immersiveBlockBreakingModifier.get()
                : 1f));
        if (EnumQualityState.isQualitySystemOn()) {
            ItemStack stack = event.getPlayer().getMainHandItem();
            Item item = stack.getItem();
            if (!stack.isEmpty() && PlayerUtils.isQualityItem(stack)) {
                speed = speed * (1 + (float) ItemQuality.getQualityFromStack(stack) / 128f);
            }
        }

        event.setNewSpeed(speed);

    }

    @SubscribeEvent
    public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
        World world = event.getPlayer().level;
        if (Utils.isBloodmoon(world) && (event.getResultStatus() == null) && !event.getPlayer().isCreative()) {
            event.setResult(SleepResult.OTHER_PROBLEM);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        final PlayerEntity player = event.getPlayer();
        if(player instanceof ServerPlayerEntity){
            PacketManager.sendTo(PacketManager.syncScrapsData,new SyncScrapsMessage(ScrapDataManager.instance.save(new CompoundNBT())), (ServerPlayerEntity) player);
        }
        if (!CommonConfig.survivalGuide.get())
            return;
        final ItemStack stack = new ItemStack(ModItems.SURVIVAL_GUIDE.get());
        final CompoundNBT entityData = player.getPersistentData();
        final CompoundNBT persistedData = entityData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        entityData.put(PlayerEntity.PERSISTED_NBT_TAG, persistedData);

        final String key = "7d2m_guide";

        if (!persistedData.getBoolean(key)) {
            persistedData.putBoolean(key, true);

            if (!player.inventory.add(stack)) {
                player.drop(stack, false);
            }
        }

    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if (EnumQualityState.isQualitySystemOn()) {
            if (PlayerUtils.isQualityItem(left) && right.getItem() == left.getItem()) {
                ItemStack stack = left.copy();
                int l = ItemQuality.getQualityFromStack(left);
                int r = ItemQuality.getQualityFromStack(right);
                ItemQuality.setQualityForStack(stack, Math.max(l, r) + 6);
                l = left.getDamageValue();
                r = right.getDamageValue();
                int m = r < l ? right.getMaxDamage() : left.getMaxDamage();
                stack.setDamageValue(Math.max(l, r) - (m - Math.min(l, r)));
                event.setOutput(stack);
                event.setCost(1);
                event.setMaterialCost(1);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {

        PlayerEntity original = event.getOriginal();
        PlayerEntity clone = event.getPlayer();

		/*if (original.inventory instanceof InventoryPlayerExtended
				&& clone.inventory instanceof InventoryPlayerExtended) {
			((InventoryPlayerExtended) original.inventory).copy((InventoryPlayerExtended) clone.inventory);
		}*/
    }

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event) {
        if (event.getPlayer().level.isClientSide())
            return;
        ItemStack stack = event.getCrafting();
        if (EnumQualityState.isQualitySystemOn()) {
            if (PlayerUtils.isQualityItem(stack)) {
                if (!event.getPlayer().isCreative()) {
                    Utils.consumeXp(event.getPlayer(), MathHelper.floor(event.getPlayer().totalExperience
                            * (event.getPlayer().level.random.nextDouble() * 0.04 + 0.01)));
                }
            }
        }
    }

    @SubscribeEvent
    public void onEatenEvent(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
            ItemStack stack = event.getItem();
            if (stack.getItem().getFoodProperties() != null && stack.getItem().getMaxDamage() > 0
                    && (stack.getMaxDamage() - stack.getDamageValue()) > 1) {
                stack.hurt(1, player.level.random, player);
                event.setResultStack(stack);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.level;
        if (!world.isClientSide())
            return;
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            ItemStack activeStack = player.getItemInHand(Hand.MAIN_HAND);
            CompoundNBT nbt = activeStack.getOrCreateTag();
            if (activeStack.isEmpty() || (activeStack.getItem() != ModItems.CHAINSAW.get()
                    && activeStack.getItem() != ModItems.AUGER.get()))
                return;
            if (nbt != null && nbt.contains("FuelMax") && nbt.getInt("FuelMax") > 0) {
                if (SevenDaysToMine.proxy.isHittingBlock(player)) {
                    lastTimeHittingBlock = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() > nextChainsawIdleSound) {
                    Minecraft.getInstance().getSoundManager().play(new MovingSoundChainsawIdle(player));
                    nextChainsawIdleSound = System.currentTimeMillis() + 3000l;
                }
                if (System.currentTimeMillis() > nextChainsawCutSound
                        && System.currentTimeMillis() - getLastTimeHittingBlock() <= 500) {
                    Minecraft.getInstance().getSoundManager().play(new MovingSoundChainsawCut(player));
                    nextChainsawCutSound = System.currentTimeMillis() + 1600l;
                }
            }
        }

    }
}