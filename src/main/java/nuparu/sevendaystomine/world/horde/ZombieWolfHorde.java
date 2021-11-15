package nuparu.sevendaystomine.world.horde;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;
import nuparu.sevendaystomine.util.Utils;

import java.util.ArrayList;
import java.util.Collection;

public class ZombieWolfHorde extends Horde{
    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(
            new TranslationTextComponent("horde.wolf.name"), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS))
            .setDarkenScreen(false);

    public ZombieWolfHorde(ServerWorld world) {
        super(world);
        entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf"), 20));
/*
        if (center != null) {
            Biome biome = world.getBiome(center);
            if (biome.isSnowyBiome()) {
                entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frigid_hunter"), 15));
                entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frostbitten_worker"), 15));
                entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frozen_lumberjack"), 7));
            }
        }
        if (CommonConfig.bloodmoonFrequency > 0 && Utils.getDay(world) > 2 * CommonConfig.bloodmoonFrequency) {
            entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "feral_zombie"),
                    Math.min(5, (int) Math.floor(Utils.getDay(world) / CommonConfig.bloodmoonFrequency) - 1)));
        }
*/
        this.waves = CommonConfig.bloodmoonHordeWaves.get();
    }

    public ZombieWolfHorde(BlockPos center, ServerWorld world, PlayerEntity player) {
        super(center, world);
        this.playerID = player.getUUID();
        entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf"), 20));
/*
        Biome biome = world.getBiome(center);
        if (biome.isSnowyBiome()) {
            entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frigid_hunter"), 15));
            entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frostbitten_worker"), 15));
            entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "frozen_lumberjack"), 7));
        }
        if (CommonConfig.bloodmoonFrequency > 0 && Utils.getDay(world) > 2 * CommonConfig.bloodmoonFrequency) {
            entries.add(new HordeEntry(new ResourceLocation(SevenDaysToMine.MODID, "feral_zombie"),
                    Math.min(5, (int) Math.floor(Utils.getDay(world) / CommonConfig.bloodmoonFrequency) - 1)));
        }*/
        this.waves = CommonConfig.bloodmoonHordeWaves.get();
    }

    public void addTarget(ServerPlayerEntity player) {
        if (bossInfo == null)
            return;
        bossInfo.addPlayer(player);
    }

    public void removeTarget(ServerPlayerEntity player) {
        if (bossInfo == null)
            return;
        bossInfo.removePlayer(player);
    }

    @Override
    public void onZombieKill(ZombieBaseEntity zombie) {
        super.onZombieKill(zombie);
        if (bossInfo == null)
            return;
        bossInfo.setPercent(MathHelper.clamp((float) zombies.size() / (float) zombiesInWave, 0, 1));
    }

    @Override
    public void addZombie(ZombieBaseEntity zombie) {
        super.addZombie(zombie);
        if (bossInfo == null)
            return;
        bossInfo.setPercent(MathHelper.clamp((float) zombies.size() / (float) zombiesInWave, 0, 1));
    }

    @Override
    public void onPlayerStartTacking(ServerPlayerEntity player, ZombieBaseEntity zombie) {
        addTarget(player);
    }

    @Override
    public void onPlayerStopTacking(ServerPlayerEntity player, ZombieBaseEntity zombie) {
        removeTarget(player);
    }

    public void onRemove() {
        super.onRemove();
        if (bossInfo == null || bossInfo.getPlayers() == null)
            return;
        Collection<ServerPlayerEntity> players = new ArrayList<ServerPlayerEntity>(bossInfo.getPlayers());
        for (ServerPlayerEntity playerMP : players) {
            bossInfo.removePlayer(playerMP);
        }
    }

    @Override
    public void start() {
        super.start();
        PlayerEntity player = getPlayer();
        if (player == null)
            return;
        zombies.clear();
        zombiesInWave = 0;
        this.center = getCenter();
        BlockPos origin = getSpawnOrigin();
        for (int i = 0; i < getZombiesInWave(); i++) {
            BlockPos pos = Utils.getTopGroundBlock(getSpawn(origin), world, true).above();
            HordeEntry entry = getHordeEntry(world.random);
            if (entry != null) {
                ZombieBaseEntity zombie = entry.spawn(world, pos);
                if (zombie == null)
                    continue;
                if (player != null) {
                    zombie.setTarget(player);
                }
                zombie.horde = this;
                zombies.add(zombie);
                zombiesInWave++;
            }
        }
        bossInfo.setPercent(MathHelper.clamp((float) zombies.size() / (float) zombiesInWave, 0, 1));
        data.setDirty();
    }

    public int getZombiesInWave() {
        if (CommonConfig.bloodmoonFrequency.get() <= 0)
            return CommonConfig.wolfHordeZombiesPerWave.get();
        return (int) MathHelper.clampedLerp(CommonConfig.wolfHordeZombiesPerWave.get(),
                CommonConfig.wolfHordeZombiesPerWave.get(),
                ((int) (Math.floor(Utils.getDay(world) / CommonConfig.bloodmoonFrequency.get())) - 1) / 5);
    }

    public BlockPos getSpawnOrigin() {
        double angle = 2.0 * Math.PI * world.random.nextDouble();
        double dist = CommonConfig.hordeMinDistance.get()
                + world.random.nextDouble() * (CommonConfig.hordeMaxDistance.get() - CommonConfig.hordeMinDistance.get());
        double x = center.getX() + dist * Math.cos(angle);
        double z = center.getZ() + dist * Math.sin(angle);

        return new BlockPos(x, 0, z);
    }

    public BlockPos getSpawn(BlockPos origin) {
        double x = world.random.nextDouble() - 0.5;
        double y = world.random.nextDouble() - 0.5;
        double z = world.random.nextDouble() - 0.5;
        double d = world.random.nextDouble() * 1.5;
        return origin.offset(x * d, y * d, z * d);
    }

}
